package escenario2;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.atomic.AtomicBoolean;

public class TrafficLight {
    private String id;
    private AtomicBoolean green;
    private Circle circle;


    public TrafficLight(String id, AtomicBoolean green, Circle circle) {
        this.id = id;
        this.green = green;
        this.circle = circle;
    }


    public void changeLight(){
        if(green.get()){
            green.set(false);
            circle.setFill(Color.RED);
        }else{
            green.set(true);
            circle.setFill(Color.GREEN);
        }
    }


    public boolean isGreen(){
        return green.get();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AtomicBoolean getGreen() {
        return green;
    }

    public void setGreen(AtomicBoolean green) {
        this.green = green;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}
