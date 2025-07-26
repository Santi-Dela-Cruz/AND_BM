package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación NEOGENE.
 * Esta clase inicia la aplicación JavaFX y carga la vista principal.
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método que inicia la aplicación JavaFX.
     * Carga el archivo FXML de la vista principal y establece el título y las
     * propiedades de la ventana.
     *
     * @param stage La ventana principal de la aplicación.
     * @throws Exception Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/main_view.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

        stage.setTitle("NEOGENE - DNA Sequence Analyzer");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }
}