package org.example;

import java.util.concurrent.PriorityBlockingQueue;

public class Intersection {
    private final String id;
    private final boolean rightTurnAllowed;
    private final PriorityBlockingQueue<Vehicle> vehicleQueue;

    public Intersection(String id, boolean rightTurnAllowed) {
        this.id = id;
        this.rightTurnAllowed = rightTurnAllowed;
        this.vehicleQueue = new PriorityBlockingQueue<>();
    }

    public String getId() {
        return id;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleQueue.add(vehicle);
    }

    public Vehicle getNextVehicle() {
        return vehicleQueue.peek();
    }

    public Vehicle removeNextVehicle() {
        return vehicleQueue.poll();
    }

    public boolean isRightTurnAllowed() {
        return rightTurnAllowed;
    }
}