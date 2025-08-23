package com.example;

import org.example.TrafficSimulation;
import org.example.Intersection;
import org.example.Vehicle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class HelloApplication extends Application {
    private static volatile boolean running = true;

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlLocation = HelloApplication.class.getResource("/hello-view.fxml");
        if (fxmlLocation == null) {
            System.err.println("No se puedo encontrar /hello-view.fxml");
            throw new IOException("FXML file not found");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 750, 800);
        stage.setTitle("Avance Proyecto");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }


    public void escenario1() throws IOException {
        URL fxmlLocation = HelloApplication.class.getResource("/escenario1/hello-view.fxml");
        if (fxmlLocation == null) {
            System.err.println("No se encontro el FXML en /escenario1/hello-view.fxml");
            throw new IOException("FXML file not found");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 750, 800);
        Stage stage = new Stage();
        stage.setTitle("Escenario 1");
        stage.setScene(scene);
        stage.show();

        HelloController controller = fxmlLoader.getController();
        Intersection intersection01 = new Intersection("1", true);
        AtomicReference<PriorityBlockingQueue<Vehicle>> vehiclesNorth = new AtomicReference<>(new PriorityBlockingQueue<>());

        Thread workerThread = new Thread(() -> {
            try {
                while (running) {
                    Thread.sleep(1000);
                    Vehicle vehicle = vehiclesNorth.get().poll();
                    if (vehicle != null) {
                        Thread.sleep(2000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        workerThread.start();
    }

    public void escenario2() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(escenario2.HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);

        Stage stage = new Stage();
        stage.setTitle("Escenario 2");
        stage.setScene(scene);
        stage.show();


        // Obtener el controlador
        escenario2.HelloController controller = fxmlLoader.getController();

        escenario2.Intersection intersection01 = new escenario2.Intersection("1", true);

        AtomicReference<PriorityBlockingQueue<escenario2.Vehicle>> vehiclesNorth = new AtomicReference<>(new PriorityBlockingQueue<>());

        Thread workerThread = new Thread(() -> {
            try {
                while (running) {
                    Thread.sleep(1000);



                    escenario2.Vehicle vehicle = vehiclesNorth.get().poll();
                    if(vehicle != null) {
                        // HelloController.moveNorth(vehicle);

                        // controller.reduceNorth();
                        Thread.sleep(2000);
                    }



                    /*
                    intersection01.setVehicles(controller.get_vehiclesNorth());
                    Vehicle vehicle = intersection01.getNextVehicle();
                    System.out.println(vehicle.getCalle());

                    */


                    /*
                    while (!controller.getvehicleQueue().isEmpty()) {
                        Vehicle vehicle = controller.getvehicleQueue().poll();
                        System.out.println(vehicle);
                    }*/



                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        workerThread.start();
    }
}