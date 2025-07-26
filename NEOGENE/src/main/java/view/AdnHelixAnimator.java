package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clase que anima una hélice de ADN en un panel JavaFX.
 * Permite resaltar posiciones coincidentes y animar la secuencia.
 */
public class AdnHelixAnimator {

    private final Pane pane;
    private final double baseSpacing = 26.4;
    private final double centerY = 55;
    private final double amplitude = 40;
    private final double radius = 5.0;

    private Timeline timeline;
    private String sequence = "";
    private Set<Integer> posicionesCoincidentes = new HashSet<>();

    /**
     * Constructor que recibe el panel donde se dibujará la animación.
     * 
     * @param pane Panel de JavaFX para la animación
     */
    public AdnHelixAnimator(Pane pane) {
        this.pane = pane;
    }

    /**
     * Establece la secuencia de ADN a animar.
     * 
     * @param seq Secuencia de ADN (solo A, T, C, G)
     */
    public void setSequence(String seq) {
        this.sequence = seq.toUpperCase().replaceAll("[^ATCG]", "");
    }

    /**
     * Establece las posiciones coincidentes a resaltar en la animación.
     * 
     * @param indices Lista de índices coincidentes
     */
    public void setCoincidencias(List<Integer> indices) {
        posicionesCoincidentes.clear();
        posicionesCoincidentes.addAll(indices);
    }

    /**
     * Inicia la animación de la hélice de ADN.
     */
    public void start() {
        if (sequence.isEmpty())
            return;

        pane.getChildren().clear();
        double[] angles = new double[sequence.length()];

        for (int i = 0; i < angles.length; i++) {
            angles[i] = 2 * Math.PI * i / 20.0;
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(80), e -> {
            pane.getChildren().clear();
            for (int i = 0; i < sequence.length(); i++) {
                char base = sequence.charAt(i);
                Color baseColor = getColor(base);

                double x = i * baseSpacing + 24;
                double yOffset = Math.sin(angles[i]) * amplitude;
                double y1 = centerY + yOffset;
                double y2 = centerY - yOffset;

                Color lineColor = posicionesCoincidentes.contains(i) ? Color.GOLD : Color.LIGHTGRAY;

                Circle c1 = new Circle(x, y1, radius, baseColor);
                Circle c2 = new Circle(x, y2, radius, baseColor);

                Line line = new Line(x, y1, x, y2);
                line.setStroke(lineColor);
                line.setStrokeWidth(1.5);

                pane.getChildren().addAll(line, c1, c2);
                angles[i] += 0.08;
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Devuelve el color correspondiente a cada base de ADN.
     * 
     * @param base Base de ADN (A, T, C, G)
     * @return Color asociado
     */
    private Color getColor(char base) {
        return switch (base) {
            case 'A' -> Color.web("#6AC259");
            case 'T' -> Color.web("#F66C6C");
            case 'C' -> Color.web("#6C6CF6");
            case 'G' -> Color.web("#F6C26C");
            default -> Color.LIGHTGRAY;
        };
    }

    /**
     * Detiene la animación si está activa.
     */
    public void stop() {
        if (timeline != null)
            timeline.stop();
    }
}