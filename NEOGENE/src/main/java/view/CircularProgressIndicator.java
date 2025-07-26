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

public class CircularProgressIndicator extends StackPane {

    private final DoubleProperty progress = new SimpleDoubleProperty(0);
    private final Arc arc;
    private final Label percentLabel;

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

        progress.addListener((obs, ov, nv) -> update());
        update();
    }

    private void update() {
        double p = Math.max(0, Math.min(1, progress.get()));
        arc.setLength(-360 * p);
        percentLabel.setText(Math.round(p * 100) + "%");
    }

    public void setProgress(double value) {
        this.progress.set(value);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }
}
