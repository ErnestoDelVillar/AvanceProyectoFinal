package org.example;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicLong;

public class Vehicle implements Comparable<Vehicle> {

    private static AtomicLong idGenerator;
    private int id;
    private boolean emergency;
    private String direction;
    private String calle;
    private double x, y; // Posición del vehículo
    private transient ImageView imageView; // Referencia a la imagen en la interfaz gráfica

    @Override
    public int compareTo(Vehicle other) {
        if (this.emergency != other.emergency) {
            return this.emergency ? -1 : 1; // Emergency vehicles have higher priority
        }
        return Integer.compare(this.id, other.id); // Same emergency status, compare by id
    }

    public Vehicle(boolean emergency, String direction, String calle, ImageView imageView) {
        this.idGenerator = new AtomicLong(0);
        this.emergency = emergency;
        this.direction = direction;
        this.calle = calle;
        this.imageView = imageView;
        this.x = imageView.getLayoutX(); // Inicializa x, y con la posición actual de imageView
        this.y = imageView.getLayoutY();
    }

    public void move(int X, int Y) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), this.getImageView());
        translateTransition.setToX(this.getImageView().getTranslateX() + X);
        translateTransition.setToY(this.getImageView().getTranslateY() + Y);
        translateTransition.play();
    }

    public Integer getId() {
        return id;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}