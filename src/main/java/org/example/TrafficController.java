package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficController {
    private final List<Intersection> intersections;
    private final ScheduledExecutorService scheduler;
    private final List<Runnable> vehicleUpdateListeners;
    private final Object lock = new Object();

    public TrafficController(List<Intersection> intersections) {
        this.intersections = intersections;
        this.scheduler = Executors.newScheduledThreadPool(10);
        this.vehicleUpdateListeners = new ArrayList<>();
    }

    public void addVehicleUpdateListener(Runnable listener) {
        vehicleUpdateListeners.add(listener);
    }

    public void startControl() {
        // Manage intersections every 500ms for smooth updates
        scheduler.scheduleAtFixedRate(this::manageIntersections, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void manageIntersections() {
        synchronized (lock) {
            for (Intersection intersection : intersections) {
                Vehicle nextVehicle = intersection.getNextVehicle();
                if (nextVehicle != null && !nextVehicle.isInIntersection()) {
                    if (isSafeToMove(nextVehicle, intersection)) {
                        // Aqui permite que un vehiculo cruce
                        Vehicle vehicle = intersection.removeNextVehicle();
                        vehicle.setInIntersection(true);
                        System.out.println("Vehicle " + vehicle.getId() + " (" + vehicle.getType() +
                                ") moving " + vehicle.getDirection() + " at intersection " + intersection.getId());
                        // Curce simulado
                        Vehicle finalVehicle = vehicle;
                        scheduler.schedule(() -> {
                            synchronized (lock) {
                                finalVehicle.setInIntersection(false);
                                notifyVehicleUpdate();
                            }
                        }, 2, TimeUnit.SECONDS);
                        // Solo un vehiculo cruza
                        break;
                    }
                }
            }
        }
    }

    private boolean isSafeToMove(Vehicle vehicle, Intersection intersection) {
        // Rivisa si es seguro cruzar
        return !vehicle.isInIntersection() &&
                (!vehicle.getDirection().equals("right") || intersection.isRightTurnAllowed()) &&
                !isIntersectionConflict(intersection);
    }

    private boolean isIntersectionConflict(Intersection current) {
        // Prevent adjacent intersections from having vehicles crossing simultaneously
        int currentIndex = intersections.indexOf(current);
        List<Integer> adjacentIndices;
        switch (currentIndex) {
            case 0: // I1 (top-left)
                adjacentIndices = List.of(1, 2);
                break;
            case 1: // I2 (top-right)
                adjacentIndices = List.of(0, 3);
                break;
            case 2: // I3 (bottom-left)
                adjacentIndices = List.of(0, 3);
                break;
            case 3: // I4 (bottom-right)
                adjacentIndices = List.of(1, 2);
                break;
            default:
                adjacentIndices = List.of();
        }
        for (int index : adjacentIndices) {
            Vehicle otherVehicle = intersections.get(index).getNextVehicle();
            if (otherVehicle != null && otherVehicle.isInIntersection()) {
                return true;
            }
        }
        return false;
    }

    private void notifyVehicleUpdate() {
        vehicleUpdateListeners.forEach(Runnable::run);
    }

    public void stopControl() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }
}