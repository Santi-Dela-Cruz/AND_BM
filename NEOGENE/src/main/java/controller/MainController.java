package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.BoyerMooreMatcher;
import view.AdnHelixAnimator;
import view.CircularProgressIndicator;
import view.ParticleNetworkBackground;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador principal de la aplicación NEOGENE.
 * Maneja la lógica de la interfaz de usuario y la interacción con el usuario.
 */
public class MainController {

    @FXML
    private Button btn_UploadFile, btn_Start;
    @FXML
    private TextField txtf_FileName, txtf_Secuence;
    @FXML
    private Label lb_EstadoCarga, lb_ProgresoTexto;
    @FXML
    private TextArea txta_Result, txta_Debug;
    @FXML
    private StackPane progressContainer, uploadProgressContainer;
    @FXML
    private HBox hbox_SecuenceDisplay;
    @FXML
    private HBox hbox_PatronAnimado;
    @FXML
    private Pane pane_AdnAnimado, particleBackground;
    @FXML
    private ToggleButton btnThemeToggle;
    @FXML
    private ScrollPane scroll_Visual;

    private String secuenciaCargada = "";
    private AdnHelixAnimator animador;
    private ParticleNetworkBackground particleEffect;
    private CircularProgressIndicator progressIndicator;
    private CircularProgressIndicator uploadProgressIndicator;
    private boolean isDarkMode = false;

    /**
     * Método que se llama al inicializar el controlador.
     * Configura los elementos de la interfaz y los eventos.
     */
    @FXML
    public void initialize() {
        // Configurar fondo de partículas
        particleEffect = new ParticleNetworkBackground();
        particleBackground.getChildren().add(particleEffect);
        particleEffect.prefWidthProperty().bind(particleBackground.widthProperty());
        particleEffect.prefHeightProperty().bind(particleBackground.heightProperty());
        particleEffect.start();

        progressIndicator = new CircularProgressIndicator(20);
        progressContainer.getChildren().add(progressIndicator);

        uploadProgressIndicator = new CircularProgressIndicator(20);
        uploadProgressContainer.getChildren().add(uploadProgressIndicator);

        // Configurar elementos iniciales
        lb_ProgresoTexto.setText("Ready");
        pane_AdnAnimado.setVisible(false);
        lb_EstadoCarga.setVisible(false);
        lb_EstadoCarga.setText("No file loaded");
        lb_EstadoCarga.setStyle("");
        txta_Debug.setEditable(false);
        txta_Result.setEditable(false);

        btn_UploadFile.setOnAction(e -> cargarArchivo());
        btn_Start.setOnAction(e -> animarBoyerMoore());
        btnThemeToggle.setOnAction(e -> toggleTheme());

        particleBackground.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                applyTheme(isDarkMode);
            }
        });
    }

    /**
     * Cambia entre el modo oscuro y claro de la aplicación.
     */
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme(isDarkMode);
    }

    /**
     * Aplica el tema oscuro o claro a la aplicación.
     * 
     * @param darkMode true para modo oscuro, false para modo claro
     */
    private void applyTheme(boolean darkMode) {
        if (particleBackground.getScene() == null) {
            return;
        }

        Node root = particleBackground.getScene().getRoot();
        if (darkMode) {
            root.getStyleClass().remove("light-mode");
            btnThemeToggle.setText("🌙");
            particleEffect.setDarkMode(true);
        } else {
            root.getStyleClass().add("light-mode");
            btnThemeToggle.setText("☀️");
            particleEffect.setDarkMode(false);
        }
    }

    /**
     * Carga un archivo de ADN y actualiza la interfaz con la secuencia cargada.
     * Inicia el animador de ADN si la secuencia es válida.
     */
    private void cargarArchivo() {
        btn_Start.setDisable(true);
        btn_UploadFile.setDisable(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona archivo de ADN");

        // Configurar filtro para archivos .txt
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        File archivo = fileChooser.showOpenDialog(null);

        if (archivo == null) {
            btn_Start.setDisable(true);
            btn_UploadFile.setDisable(false);
            debugLog("Operación de carga de archivo cancelada por el usuario");
            return;
        }

        if (archivo != null) {
            txtf_FileName.setText(archivo.getName());
            try {
                // Limpiar visualización previa si existe
                if (animador != null) {
                    animador.stop();
                    animador = null;
                }
                pane_AdnAnimado.setVisible(false);
                hbox_SecuenceDisplay.getChildren().clear();

                List<String> lineas = Files.readAllLines(archivo.toPath());
                String contenido = String.join("", lineas).replaceAll("\\s", "").toUpperCase();

                // Almacenar temporalmente para validación
                String tempSecuencia = contenido;

                lb_EstadoCarga.setVisible(true);
                lb_EstadoCarga.setText("Loading...");
                hbox_PatronAnimado.getChildren().clear();
                txta_Result.clear();
                txta_Debug.clear();

                // Iniciar indicador de progreso
                uploadProgressIndicator.setProgress(0);

                debugLog("Iniciando carga de archivo: " + archivo.getName());
                debugLog("Tamaño de la secuencia: " + tempSecuencia.length() + " caracteres");

                Timeline cargaTimeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> uploadProgressIndicator.setProgress(0)),
                        new KeyFrame(Duration.seconds(0.8), e -> uploadProgressIndicator.setProgress(0.2)),
                        new KeyFrame(Duration.seconds(1.6), e -> uploadProgressIndicator.setProgress(0.4)),
                        new KeyFrame(Duration.seconds(2.4), e -> uploadProgressIndicator.setProgress(0.6)),
                        new KeyFrame(Duration.seconds(3.2), e -> uploadProgressIndicator.setProgress(0.8)),
                        new KeyFrame(Duration.seconds(4.0), e -> {
                            uploadProgressIndicator.setProgress(1.0);

                            boolean esValido = validarSecuenciaADN(tempSecuencia);

                            if (esValido) {
                                secuenciaCargada = tempSecuencia;
                                mostrarSecuenciaEnHBox();
                                pane_AdnAnimado.setVisible(true);
                                animador = new AdnHelixAnimator(pane_AdnAnimado);
                                animador.setSequence(secuenciaCargada);
                                animador.start();
                                lb_EstadoCarga.setText("File loaded successfully");
                                lb_EstadoCarga.setStyle(""); // Resetear estilo a normal
                                debugLog("Archivo cargado exitosamente");
                            } else {
                                pane_AdnAnimado.setVisible(false);
                                lb_EstadoCarga.setText("Invalid DNA sequence - Only A, T, C, G allowed");
                                lb_EstadoCarga.setStyle("-fx-text-fill: #e74c3c;");
                                debugLog("Archivo rechazado: Secuencia de ADN inválida");
                            }
                            btn_Start.setDisable(!esValido);
                            btn_UploadFile.setDisable(false);
                        }));
                cargaTimeline.play();

            } catch (IOException ex) {
                lb_EstadoCarga.setText("Failed to load file");
                lb_EstadoCarga.setStyle("-fx-text-fill: #e74c3c;");
                btn_Start.setDisable(true);
                btn_UploadFile.setDisable(false);
                debugLog("Error al cargar archivo: " + ex.getMessage());
            }
        }
    }

    /**
     * Valida que una secuencia contenga únicamente caracteres de ADN válidos (A, T,
     * C, G)
     * 
     * @param secuencia La secuencia a validar
     * @return true si la secuencia es válida, false en caso contrario
     */
    private boolean validarSecuenciaADN(String secuencia) {
        if (secuencia == null || secuencia.isEmpty()) {
            return false;
        }

        return secuencia.matches("^[ATCG]+$");
    }

    private void mostrarSecuenciaEnHBox() {
        hbox_SecuenceDisplay.getChildren().clear();
        for (char base : secuenciaCargada.toCharArray()) {
            Label lbl = new Label(String.valueOf(base));
            lbl.setFont(Font.font("Consolas", 14));
            lbl.getStyleClass().add("base");

            String baseClass = String.valueOf(base);
            if ("ATCG".contains(baseClass)) {
                lbl.getStyleClass().add(baseClass);
            }

            hbox_SecuenceDisplay.getChildren().add(lbl);
        }
    }

    /**
     * Limpia los estilos de resaltado de la secuencia y el patrón animado.
     * Se utiliza para reiniciar el estado visual antes de un nuevo análisis.
     */
    private void limpiarEstilosSecuencia() {
        for (int i = 0; i < hbox_SecuenceDisplay.getChildren().size(); i++) {
            Label lbl = (Label) hbox_SecuenceDisplay.getChildren().get(i);
            lbl.setStyle("");
            lbl.getStyleClass().removeIf(s -> s.equals("highlighted") || s.equals("match"));
        }
    }

    /**
     * Registra un mensaje de depuración en el área de texto de depuración.
     * Se ejecuta en el hilo de la interfaz de usuario para evitar problemas de
     * concurrencia.
     * 
     * @param message El mensaje a registrar
     */
    private void debugLog(String message) {
        Platform.runLater(() -> {
            txta_Debug.appendText("[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + message + "\n");
        });
    }

    /**
     * Inicia el análisis de la secuencia de ADN utilizando el algoritmo
     * Boyer-Moore.
     * Muestra el patrón en la posición correspondiente y actualiza el progreso.
     */
    private void animarBoyerMoore() {
        btn_Start.setDisable(true);
        btn_UploadFile.setDisable(true);
        btnThemeToggle.setDisable(true);

        limpiarEstilosSecuencia();
        hbox_PatronAnimado.getChildren().clear();
        txta_Result.clear();

        String pattern = txtf_Secuence.getText().toUpperCase();
        if (secuenciaCargada.isEmpty() || pattern.isEmpty()) {
            txta_Result.setText("Por favor, cargue una secuencia y escriba un patrón de búsqueda.");
            btn_Start.setDisable(false);
            btn_UploadFile.setDisable(false);
            btnThemeToggle.setDisable(false);
            debugLog("Error: No hay secuencia cargada o patrón ingresado");
            return;
        }

        progressIndicator.setProgress(0);
        lb_ProgresoTexto.setText("Analyzing");

        debugLog("Iniciando búsqueda de patrón: " + pattern);
        debugLog("Algoritmo: Boyer-Moore");
        debugLog("Longitud del patrón: " + pattern.length());
        debugLog("Longitud de la secuencia: " + secuenciaCargada.length());

        long inicioAlg = System.nanoTime();
        BoyerMooreMatcher matcher = new BoyerMooreMatcher(secuenciaCargada, pattern);
        List<BoyerMooreMatcher.Step> pasos = matcher.getSearchSteps();
        long finAlg = System.nanoTime();
        double tiempoMs = (finAlg - inicioAlg) / 1_000_000.0;

        debugLog("Tiempo de procesamiento del algoritmo: " + String.format("%.3f ms", tiempoMs));
        debugLog("Número total de pasos: " + pasos.size());

        List<Integer> coincidenciasActuales = new ArrayList<>();
        int[] totalCoincidencias = { 0 };
        int[] comparacionesTotales = { 0 };

        Timeline timeline = new Timeline();
        Duration frameGap = Duration.millis(600);
        Duration timePoint = Duration.ZERO;

        for (int idx = 0; idx < pasos.size(); idx++) {
            final int frameIdx = idx;
            BoyerMooreMatcher.Step paso = pasos.get(idx);
            KeyFrame frame = new KeyFrame(timePoint, e -> {
                mostrarPatronEnPosicion(paso.patternPosition, pattern, paso.match);
                progressIndicator.setProgress((double) (frameIdx + 1) / pasos.size());

                comparacionesTotales[0] += paso.comparisons;
                if (paso.match) {
                    totalCoincidencias[0]++;
                    for (int i = 0; i < pattern.length(); i++) {
                        if (paso.patternPosition + i < secuenciaCargada.length()) {
                            coincidenciasActuales.add(paso.patternPosition + i);
                        }
                    }
                    debugLog("Coincidencia encontrada en posición: " + paso.patternPosition);
                }
                animador.setCoincidencias(coincidenciasActuales);
                txta_Result.setText("Posiciones: " + coincidenciasActuales + "\n" +
                        "Total ocurrencias: " + totalCoincidencias[0]);
            });
            timeline.getKeyFrames().add(frame);
            timePoint = timePoint.add(frameGap);
        }

        timeline.setOnFinished(e -> {
            txta_Result.setText("Posiciones encontradas: " + coincidenciasActuales + "\n\n" +
                    "Total ocurrencias: " + totalCoincidencias[0] + "\n" +
                    "Total comparaciones: " + comparacionesTotales[0] + "\n" +
                    String.format("Tiempo de ejecución: %.3f ms", tiempoMs));
            progressIndicator.setProgress(1.0);
            lb_ProgresoTexto.setText("Complete");
            btn_Start.setDisable(false);
            btn_UploadFile.setDisable(false);
            btnThemeToggle.setDisable(false);
            debugLog("Análisis completado");
            debugLog("Total de coincidencias encontradas: " + totalCoincidencias[0]);
            debugLog("Total de comparaciones realizadas: " + comparacionesTotales[0]);
        });

        timeline.play();
    }

    private void mostrarPatronEnPosicion(int pos, String pattern, boolean match) {
        hbox_PatronAnimado.getChildren().clear();

        for (int i = 0; i < pos; i++) {
            Label spacer = new Label();
            spacer.setMinSize(24, 24);
            spacer.setStyle("-fx-background-color: transparent;");
            hbox_PatronAnimado.getChildren().add(spacer);
        }

        for (int i = 0; i < pattern.length(); i++) {
            if (pos + i >= secuenciaCargada.length())
                break;

            Label lbl = new Label(String.valueOf(pattern.charAt(i)));
            lbl.setMinSize(24, 24);
            lbl.setAlignment(javafx.geometry.Pos.CENTER);
            lbl.setContentDisplay(ContentDisplay.CENTER);

            if (match) {
                lbl.setStyle(
                        "-fx-background-color: #6AC259; -fx-border-color: darkgreen; -fx-text-fill: white; -fx-border-radius: 4; -fx-background-radius: 4;");
            } else {
                lbl.setStyle(
                        "-fx-background-color: #F66C6C; -fx-border-color: darkred; -fx-text-fill: white; -fx-border-radius: 4; -fx-background-radius: 4;");
            }

            hbox_PatronAnimado.getChildren().add(lbl);

            if (match) {
                Label secLbl = (Label) hbox_SecuenceDisplay.getChildren().get(pos + i);
                secLbl.setStyle(
                        "-fx-background-color: #FFD700; -fx-border-color: gray; -fx-padding: 4; -fx-text-fill: black; -fx-border-radius: 4; -fx-background-radius: 4;");
            }
        }

        double visibleWidth = scroll_Visual.getViewportBounds().getWidth();
        double contentWidth = hbox_SecuenceDisplay.getWidth();

        double elementWidth = 24.0;
        double patternWidth = pattern.length() * elementWidth;
        double positionInPixels = pos * elementWidth;

        double scrollValue;
        if (contentWidth <= visibleWidth) {
            scrollValue = 0;
        } else {
            double centerPosition = positionInPixels + (patternWidth / 2);
            double scrollCenter = centerPosition - (visibleWidth / 2);

            scrollValue = Math.max(0, Math.min(1, scrollCenter / (contentWidth - visibleWidth)));

            if (pos >= secuenciaCargada.length() - pattern.length()) {
                scrollValue = 1.0;
            }
        }

        javafx.animation.Timeline scrollAnimation = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.ZERO,
                        new javafx.animation.KeyValue(scroll_Visual.hvalueProperty(), scroll_Visual.getHvalue())),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(300),
                        new javafx.animation.KeyValue(scroll_Visual.hvalueProperty(), scrollValue,
                                javafx.animation.Interpolator.EASE_BOTH)));
        scrollAnimation.play();
    }
}