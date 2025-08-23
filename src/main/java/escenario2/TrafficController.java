package escenario2;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrafficController {
    private Queue<Runnable> tasks = new LinkedList<>();

    private volatile boolean crossingNorthOccupied = false;
    private volatile boolean crossingSouthOccupied = false;
    public volatile boolean crossingEastOccupied = false;
    private volatile boolean crossingWestOccupied = false;





    private PriorityBlockingQueue<Vehicle> queue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Vehicle> Emergencyqueue = new PriorityBlockingQueue<>();

    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread workerThread;

    public TrafficController() {
        workerThread = new Thread(this::processQueue);
        running.set(true);
        workerThread.start();
    }

    private void scheduleNext() {
        if (!tasks.isEmpty()) {
            tasks.poll().run();
        }
    }

    private void processQueue() {
        while (running.get()) {
            if(!Emergencyqueue.isEmpty()){
                Vehicle emgvehicle = Emergencyqueue.poll();
                for (Vehicle vehicle : queue) {
                    if(vehicle.getCalle().equals(emgvehicle.getCalle())){
                        addVehicleAnimation(vehicle);
                        if(vehicle==emgvehicle){
                            queue.remove(vehicle);
                            break;
                        }
                        queue.remove(vehicle);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                }
            }else {
                if (!queue.isEmpty()) {


                    Vehicle vehicle = queue.poll();
                    if (vehicle != null) {

                        addVehicleAnimation(vehicle);

                    }

                }
            }
            // Pequeña pausa para evitar uso excesivo de CPU
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private synchronized void addVehicleAnimation(Vehicle vehicle) {
        tasks.offer(() -> {
            //VIENE DEL NORTE Y SIGUE DERECHO
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("South")) {

                while (crossingWestOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingWestOccupied = true; // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingWestOccupied = false; // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            //VIENE DEL NORTE Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("North")) {
                while (crossingNorthOccupied||crossingWestOccupied||crossingEastOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true;
                crossingWestOccupied = true;
                crossingEastOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingNorthOccupied = false;
                    crossingWestOccupied = false;
                    crossingEastOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            //VIENE DEL NORTE Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("West")) {
                while (crossingNorthOccupied||crossingWestOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true;
                crossingWestOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingNorthOccupied = false;
                    crossingWestOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            //VIENE DEL NORTE Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("East")) {
                while (crossingNorthOccupied||crossingWestOccupied||crossingEastOccupied||crossingSouthOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true;
                crossingWestOccupied = true;
                crossingEastOccupied = true;
                crossingSouthOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {


                    crossingNorthOccupied = false;
                    crossingWestOccupied = false;
                    crossingEastOccupied = false;
                    crossingSouthOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }






            //VIENE DEL SUR Y SIGUE DERECHO
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("North")) {

                while (crossingEastOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingEastOccupied = true; // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingEastOccupied = false; // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            //VIENE DEL SUR Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("South")) {
                while (crossingSouthOccupied||crossingWestOccupied||crossingEastOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied = true;
                crossingWestOccupied = true;
                crossingEastOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {


                    crossingSouthOccupied = false;
                    crossingWestOccupied = false;
                    crossingEastOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            //VIENE DEL SUR Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("East")) {
                while (crossingSouthOccupied||crossingEastOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied = true;
                crossingEastOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingSouthOccupied = false;
                    crossingEastOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            //VIENE DEL SUR Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("West")) {
                while (crossingNorthOccupied||crossingWestOccupied||crossingEastOccupied||crossingSouthOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true;
                crossingWestOccupied = true;
                crossingEastOccupied = true;
                crossingSouthOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingNorthOccupied = false;
                    crossingWestOccupied = false;
                    crossingEastOccupied = false;
                    crossingSouthOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }



            //VIENE DEL ESTE Y SIGUE DERECHO
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("West")) {

                while (crossingNorthOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true; // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingNorthOccupied = false; // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }


            //VIENE DEL ESTE Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("East")) {
                while (crossingSouthOccupied||crossingNorthOccupied||crossingEastOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied = true;
                crossingNorthOccupied = true;
                crossingEastOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingSouthOccupied = false;
                    crossingNorthOccupied = false;
                    crossingEastOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            //VIENE DEL ESTE Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("North")) {
                while (crossingNorthOccupied||crossingEastOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true;
                crossingEastOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingNorthOccupied = false;
                    crossingEastOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            //VIENE DEL ESTE Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("South")) {
                while (crossingNorthOccupied||crossingWestOccupied||crossingEastOccupied||crossingSouthOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true;
                crossingWestOccupied = true;
                crossingEastOccupied = true;
                crossingSouthOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingNorthOccupied = false;
                    crossingWestOccupied = false;
                    crossingEastOccupied = false;
                    crossingSouthOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }






            //VIENE DEL OESTE Y SIGUE DERECHO
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("East")) {

                while (crossingSouthOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied = true; // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingSouthOccupied = false; // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }


            //VIENE DEL OESTE Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("West")) {
                while (crossingSouthOccupied||crossingNorthOccupied||crossingWestOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied = true;
                crossingNorthOccupied = true;
                crossingWestOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingSouthOccupied = false;
                    crossingNorthOccupied = false;
                    crossingWestOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            //VIENE DEL OESTE Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("South")) {
                while (crossingSouthOccupied||crossingWestOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied = true;
                crossingWestOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingSouthOccupied = false;
                    crossingWestOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }


            //VIENE DEL OESTE Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("North")) {
                while (crossingNorthOccupied||crossingWestOccupied||crossingEastOccupied||crossingSouthOccupied) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied = true;
                crossingWestOccupied = true;
                crossingEastOccupied = true;
                crossingSouthOccupied = true;


                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {

                    crossingNorthOccupied = false;
                    crossingWestOccupied = false;
                    crossingEastOccupied = false;
                    crossingSouthOccupied = false;
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

        });
        scheduleNext();
    }

    public void startControl() {
        while (!queue.isEmpty()) {
            Vehicle vehicle = queue.poll();
            if(vehicle!=null){
                addVehicleAnimation(vehicle);
            }
        }
        scheduleNext();
    }

    public void stopControl() {
        running.set(false);
        workerThread.interrupt();
    }

    public void setQueue(PriorityBlockingQueue<Vehicle> queue) {
        this.queue = queue;
    }

    public void addVehicle(Vehicle vehicle) {
        if(vehicle.isEmergency()){
            Emergencyqueue.add(vehicle);
        }
        queue.add(vehicle);
    }
}