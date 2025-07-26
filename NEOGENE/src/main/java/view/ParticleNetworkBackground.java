package view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Fondo animado de partículas y conexiones para JavaFX.
 * Permite modo claro/oscuro y animación de partículas conectadas.
 */
public class ParticleNetworkBackground extends Pane {

    private Canvas canvas;
    private GraphicsContext gc;
    private List<Particle> particles;
    private AnimationTimer timer;
    private boolean isDarkMode = true;
    private Color particleColor = Color.rgb(0, 198, 255, 0.6);
    private Color lineColor = Color.rgb(0, 198, 255, 0.2);
    private Color bgColor = Color.rgb(10, 15, 20, 1.0);

    /**
     * Constructor. Inicializa el fondo animado de partículas.
     */
    public ParticleNetworkBackground() {
        initialize();
    }

    /**
     * Inicializa los componentes y animaciones del fondo.
     */
    private void initialize() {
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();
        particles = new ArrayList<>();

        widthProperty().addListener((obs, oldVal, newVal) -> resetParticles());
        heightProperty().addListener((obs, oldVal, newVal) -> resetParticles());

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };

        setOpacity(1.0);
    }

    /**
     * Cambia el modo claro/oscuro del fondo.
     * 
     * @param darkMode true para modo oscuro, false para claro
     */
    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
        if (darkMode) {
            particleColor = Color.rgb(0, 198, 255, 0.8);
            lineColor = Color.rgb(0, 198, 255, 0.4);
            bgColor = Color.rgb(10, 15, 20, 1.0);
        } else {
            particleColor = Color.rgb(0, 90, 160, 0.8);
            lineColor = Color.rgb(0, 90, 160, 0.4);
            bgColor = Color.rgb(240, 245, 250, 1.0);
        }
    }

    /**
     * Inicia la animación del fondo de partículas.
     */
    public void start() {
        resetParticles();
        timer.start();
    }

    /**
     * Detiene la animación del fondo de partículas.
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Reinicia y genera las partículas según el tamaño del panel.
     */
    private void resetParticles() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        particles.clear();
        Random random = new Random();

        int particleCount = (int) (getWidth() * getHeight() / 6000);

        for (int i = 0; i < particleCount; i++) {
            double x = random.nextDouble() * getWidth();
            double y = random.nextDouble() * getHeight();
            double speedX = (random.nextDouble() - 0.5) * 0.8;
            double speedY = (random.nextDouble() - 0.5) * 0.8;
            double radius = 1.8 + random.nextDouble() * 1.8;

            particles.add(new Particle(x, y, speedX, speedY, radius));
        }
    }

    /**
     * Dibuja las partículas y conexiones en el canvas.
     */
    private void draw() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        gc.setFill(bgColor);
        gc.fillRect(0, 0, getWidth(), getHeight());

        for (Particle p : particles) {
            p.update(getWidth(), getHeight());
        }

        gc.setStroke(lineColor);
        double connectionDistance = 180;

        for (int i = 0; i < particles.size(); i++) {
            Particle p1 = particles.get(i);

            for (int j = i + 1; j < particles.size(); j++) {
                Particle p2 = particles.get(j);
                double distance = distance(p1.x, p1.y, p2.x, p2.y);

                if (distance < connectionDistance) {
                    gc.setGlobalAlpha((1 - distance / connectionDistance) * 0.7);
                    gc.setLineWidth(0.8);
                    gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        gc.setGlobalAlpha(0.8);
        gc.setFill(particleColor);

        for (Particle p : particles) {
            gc.fillOval(p.x - p.radius, p.y - p.radius, p.radius * 2, p.radius * 2);
        }
    }

    /**
     * Calcula la distancia euclidiana entre dos puntos.
     * 
     * @param x1 x del primer punto
     * @param y1 y del primer punto
     * @param x2 x del segundo punto
     * @param y2 y del segundo punto
     * @return distancia
     */
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Clase interna que representa una partícula animada.
     */
    private static class Particle {
        double x, y;
        double speedX, speedY;
        double radius;

        /**
         * Constructor de partícula.
         * 
         * @param x      posición x
         * @param y      posición y
         * @param speedX velocidad x
         * @param speedY velocidad y
         * @param radius radio
         */
        Particle(double x, double y, double speedX, double speedY, double radius) {
            this.x = x;
            this.y = y;
            this.speedX = speedX;
            this.speedY = speedY;
            this.radius = radius;
        }

        /**
         * Actualiza la posición de la partícula y rebota en los bordes.
         * 
         * @param width  ancho del panel
         * @param height alto del panel
         */
        void update(double width, double height) {
            x += speedX;
            y += speedY;

            if (x < 0) {
                x = 0;
                speedX *= -1;
            } else if (x > width) {
                x = width;
                speedX *= -1;
            }

            if (y < 0) {
                y = 0;
                speedY *= -1;
            } else if (y > height) {
                y = height;
                speedY *= -1;
            }
        }
    }
}
