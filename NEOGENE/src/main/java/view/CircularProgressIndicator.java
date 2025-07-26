package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

/**
 * Indicador de progreso circular animado para JavaFX.
 * Permite mostrar el avance de una tarea de forma visual y fluida.
 */
public class CircularProgressIndicator extends StackPane {

    private final DoubleProperty progress = new SimpleDoubleProperty(0);
    private final Arc arc;
    private final Label percentLabel;
    private double currentAnimatedProgress = 0;
    private javafx.animation.AnimationTimer progressAnimator;

    /**
     * Crea un nuevo indicador de progreso circular con el radio especificado.
     * 
     * @param radius Radio del círculo
     */
    public CircularProgressIndicator(double radius) {
        Circle bg = new Circle(radius);
        bg.setFill(Color.web("#1a1a1a"));
        bg.setStroke(Color.web("#2f2f2f"));
        bg.setStrokeWidth(2);

        arc = new Arc();
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
        arc.setStartAngle(90);
        arc.setLength(0);
        arc.setType(ArcType.OPEN);
        arc.setStroke(Color.web("#00c6ff"));
        arc.setStrokeWidth(6);
        arc.setFill(null);
        arc.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

        percentLabel = new Label("0%");
        percentLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #cfd8dc; -fx-font-weight: bold;");

        Group group = new Group(bg, arc);
        getChildren().addAll(group, percentLabel);

        progressAnimator = new javafx.animation.AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaSeconds = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                double speed = 4.0;
                double targetValue = progress.get();
                if (Math.abs(currentAnimatedProgress - targetValue) < 0.001) {
                    currentAnimatedProgress = targetValue;
                    progressAnimator.stop();
                } else {
                    double easeFactor = 0.15
                            + 0.85 * Math.pow((1.0 - Math.abs(targetValue - currentAnimatedProgress)), 2);
                    currentAnimatedProgress += (targetValue - currentAnimatedProgress)
                            * Math.min(1.0, deltaSeconds * speed * easeFactor);
                }
                updateView(currentAnimatedProgress);
            }
        };

        progress.addListener((obs, oldValue, newValue) -> {
            progressAnimator.start();
        });

        update();
    }

    /**
     * Actualiza el indicador de progreso al valor actual de la propiedad.
     */
    private void update() {
        double p = Math.max(0, Math.min(1, progress.get()));
        updateView(p);
    }

    /**
     * Actualiza la visualización del círculo y el texto según el progreso.
     * 
     * @param p Progreso entre 0 y 1
     */
    private void updateView(double p) {
        arc.setLength(-360 * p);
        percentLabel.setText(Math.round(p * 100) + "%");
    }

    /**
     * Establece el valor de progreso (entre 0 y 1).
     * 
     * @param value Progreso
     */
    public void setProgress(double value) {
        this.progress.set(value);
    }

    /**
     * Obtiene el valor actual de progreso.
     * 
     * @return Progreso actual
     */
    public double getProgress() {
        return progress.get();
    }

    /**
     * Devuelve la propiedad de progreso para binding.
     * 
     * @return DoubleProperty de progreso
     */
    public DoubleProperty progressProperty() {
        return progress;
    }
}
