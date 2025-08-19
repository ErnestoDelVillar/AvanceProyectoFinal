package org.example;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrafficController {

    private Queue<Runnable> tasks = new LinkedList<>();

    private final AtomicBoolean crossingNorthOccupied = new AtomicBoolean(false);
    private final AtomicBoolean crossingSouthOccupied = new AtomicBoolean(false);
    private final AtomicBoolean crossingEastOccupied = new AtomicBoolean(false);
    private final AtomicBoolean crossingWestOccupied = new AtomicBoolean(false);

    private PriorityBlockingQueue<Vehicle> queue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Vehicle> Emergencyqueue = new PriorityBlockingQueue<>();

    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread workerThread;

    //crea un hilo que estara ejecutando constatemente los vehiculos en fila
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

    // se encarga de los vehiculos y le da prioridad a las ambulancias
    private void processQueue() {
        while (running.get()) {
            if (!Emergencyqueue.isEmpty()) {
                Vehicle emgvehicle = Emergencyqueue.poll();
                for (Vehicle vehicle : queue) {
                    if (vehicle.getCalle().equals(emgvehicle.getCalle())) {
                        addVehicleAnimation(vehicle);
                        if (vehicle == emgvehicle) {
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
            } else {
                if (!queue.isEmpty()) {
                    Vehicle vehicle = queue.poll();
                    if (vehicle != null) {
                        addVehicleAnimation(vehicle);
                    }
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //
    private synchronized void addVehicleAnimation(Vehicle vehicle) {
        tasks.offer(() -> {
            // VIENE DEL NORTE Y SIGUE DERECHO
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("South")) {
                while (crossingWestOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingWestOccupied.set(true); // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveNorth(vehicle);
                    TrafficSimulation.updatePositionsNorth();
                    crossingWestOccupied.set(false); // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            // VIENE DEL NORTE Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("North")) {
                while (crossingNorthOccupied.get() || crossingWestOccupied.get() || crossingEastOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true);
                crossingWestOccupied.set(true);
                crossingEastOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveNorthUTurn(vehicle);
                    crossingNorthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    crossingEastOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            // VIENE DEL NORTE Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("West")) {
                while (crossingNorthOccupied.get() || crossingWestOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true);
                crossingWestOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveNorthRightTurn(vehicle);
                    crossingNorthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            // VIENE DEL NORTE Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("North") && vehicle.getDirection().equals("East")) {
                while (crossingNorthOccupied.get() || crossingWestOccupied.get() || crossingEastOccupied.get() || crossingSouthOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true);
                crossingWestOccupied.set(true);
                crossingEastOccupied.set(true);
                crossingSouthOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveNorthLeftTurn(vehicle);
                    crossingNorthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    crossingEastOccupied.set(false);
                    crossingSouthOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL SUR Y SIGUE DERECHO
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("North")) {
                while (crossingEastOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingEastOccupied.set(true); // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveSouth(vehicle);
                    TrafficSimulation.updatePositionsNorth();
                    crossingEastOccupied.set(false); // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            // VIENE DEL SUR Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("South")) {
                while (crossingSouthOccupied.get() || crossingWestOccupied.get() || crossingEastOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied.set(true);
                crossingWestOccupied.set(true);
                crossingEastOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveSouthUTurn(vehicle);
                    crossingSouthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    crossingEastOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            // VIENE DEL SUR Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("East")) {
                while (crossingSouthOccupied.get() || crossingEastOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied.set(true);
                crossingEastOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveSouthRightTurn(vehicle);
                    crossingSouthOccupied.set(false);
                    crossingEastOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }
            // VIENE DEL SUR Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("South") && vehicle.getDirection().equals("West")) {
                while (crossingNorthOccupied.get() || crossingWestOccupied.get() || crossingEastOccupied.get() || crossingSouthOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true);
                crossingWestOccupied.set(true);
                crossingEastOccupied.set(true);
                crossingSouthOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveSouthLeftTurn(vehicle);
                    crossingNorthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    crossingEastOccupied.set(false);
                    crossingSouthOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL ESTE Y SIGUE DERECHO
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("West")) {
                while (crossingNorthOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true); // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveEast(vehicle);
                    TrafficSimulation.updatePositionsEast();
                    crossingNorthOccupied.set(false); // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL ESTE Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("East")) {
                while (crossingSouthOccupied.get() || crossingNorthOccupied.get() || crossingEastOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied.set(true);
                crossingNorthOccupied.set(true);
                crossingEastOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveEastUTurn(vehicle);
                    crossingSouthOccupied.set(false);
                    crossingNorthOccupied.set(false);
                    crossingEastOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL ESTE Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("North")) {
                while (crossingNorthOccupied.get() || crossingEastOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true);
                crossingEastOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveEastRightTurn(vehicle);
                    crossingNorthOccupied.set(false);
                    crossingEastOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL ESTE Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("East") && vehicle.getDirection().equals("South")) {
                while (crossingNorthOccupied.get() || crossingWestOccupied.get() || crossingEastOccupied.get() || crossingSouthOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true);
                crossingWestOccupied.set(true);
                crossingEastOccupied.set(true);
                crossingSouthOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveEastLeftTurn(vehicle);
                    crossingNorthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    crossingEastOccupied.set(false);
                    crossingSouthOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL OESTE Y SIGUE DERECHO
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("East")) {
                while (crossingSouthOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied.set(true); // Marca el cruce como ocupado

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveWest(vehicle);
                    TrafficSimulation.updatePositionsWest();
                    crossingSouthOccupied.set(false); // Libera el cruce
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL OESTE Y DA LA VUELTA EN U
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("West")) {
                while (crossingSouthOccupied.get() || crossingNorthOccupied.get() || crossingWestOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied.set(true);
                crossingNorthOccupied.set(true);
                crossingWestOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveWestUTurn(vehicle);
                    crossingSouthOccupied.set(false);
                    crossingNorthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL OESTE Y GIRA A LA DERECHA
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("South")) {
                while (crossingSouthOccupied.get() || crossingWestOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingSouthOccupied.set(true);
                crossingWestOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveWestRightTurn(vehicle);
                    crossingSouthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    synchronized (this) {
                        notifyAll(); // Notifica a otros vehículos que el cruce está libre
                    }
                    scheduleNext();
                });
                pause.play();
            }

            // VIENE DEL OESTE Y GIRA A LA IZQUIERDA
            if (vehicle.getCalle().equals("West") && vehicle.getDirection().equals("North")) {
                while (crossingNorthOccupied.get() || crossingWestOccupied.get() || crossingEastOccupied.get() || crossingSouthOccupied.get()) {
                    try {
                        wait(); // Espera hasta que el cruce se libere
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                crossingNorthOccupied.set(true);
                crossingWestOccupied.set(true);
                crossingEastOccupied.set(true);
                crossingSouthOccupied.set(true);

                PauseTransition pause = new PauseTransition(Duration.millis(1));
                pause.setOnFinished(event -> {
                    TrafficSimulation.moveWestLeftTurn(vehicle);
                    crossingNorthOccupied.set(false);
                    crossingWestOccupied.set(false);
                    crossingEastOccupied.set(false);
                    crossingSouthOccupied.set(false);
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

    //Agrega un vehiculo a la simulacion y si es de emergencia tambien
    public void addVehicle(Vehicle vehicle) {
        if (vehicle.isEmergency()) {
            Emergencyqueue.add(vehicle);
        }
        queue.add(vehicle);
    }


}