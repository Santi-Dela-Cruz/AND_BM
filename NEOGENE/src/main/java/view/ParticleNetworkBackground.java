package view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleNetworkBackground extends Pane {

    private Canvas canvas;
    private GraphicsContext gc;
    private List<Particle> particles;
    private AnimationTimer timer;
    private boolean isDarkMode = true;
    private Color particleColor = Color.rgb(0, 198, 255, 0.6);
    private Color lineColor = Color.rgb(0, 198, 255, 0.2);
    private Color bgColor = Color.rgb(10, 15, 20, 1.0);

    public ParticleNetworkBackground() {
        initialize();
    }

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
        
        setOpacity(0.9);
    }
    
    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
        if (darkMode) {
            particleColor = Color.rgb(0, 198, 255, 0.6);
            lineColor = Color.rgb(0, 198, 255, 0.2);
            bgColor = Color.rgb(10, 15, 20, 1.0);
        } else {
            particleColor = Color.rgb(0, 90, 160, 0.6);
            lineColor = Color.rgb(0, 90, 160, 0.2);
            bgColor = Color.rgb(240, 245, 250, 1.0);
        }
    }

    public void start() {
        resetParticles();
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void resetParticles() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        
        particles.clear();
        Random random = new Random();
        
        int particleCount = (int)(getWidth() * getHeight() / 7000); // Density adjustment
        
        for (int i = 0; i < particleCount; i++) {
            double x = random.nextDouble() * getWidth();
            double y = random.nextDouble() * getHeight();
            double speedX = (random.nextDouble() - 0.5) * 0.8;
            double speedY = (random.nextDouble() - 0.5) * 0.8;
            double radius = 1.5 + random.nextDouble() * 1.5;
            
            particles.add(new Particle(x, y, speedX, speedY, radius));
        }
    }

    private void draw() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        
        gc.setFill(bgColor);
        gc.fillRect(0, 0, getWidth(), getHeight());
        
        for (Particle p : particles) {
            p.update(getWidth(), getHeight());
        }
        
        // Draw connections between particles
        gc.setStroke(lineColor);
        double connectionDistance = 150;
        
        for (int i = 0; i < particles.size(); i++) {
            Particle p1 = particles.get(i);
            
            for (int j = i + 1; j < particles.size(); j++) {
                Particle p2 = particles.get(j);
                double distance = distance(p1.x, p1.y, p2.x, p2.y);
                
                if (distance < connectionDistance) {
                    gc.setGlobalAlpha((1 - distance / connectionDistance) * 0.6);
                    gc.setLineWidth(0.5);
                    gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
        
        // Draw particles
        gc.setGlobalAlpha(0.8);
        gc.setFill(particleColor);
        
        for (Particle p : particles) {
            gc.fillOval(p.x - p.radius, p.y - p.radius, p.radius * 2, p.radius * 2);
        }
    }
    
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    private static class Particle {
        double x, y;
        double speedX, speedY;
        double radius;
        
        Particle(double x, double y, double speedX, double speedY, double radius) {
            this.x = x;
            this.y = y;
            this.speedX = speedX;
            this.speedY = speedY;
            this.radius = radius;
        }
        
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
