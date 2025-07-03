package org.example;

import java.util.concurrent.atomic.AtomicLong;

public class Vehicle implements Comparable<Vehicle> {
    private static final AtomicLong idGenerator = new AtomicLong(0);
    private final String id;
    private final String type; // "normal" or "emergency"
    private final String direction; // "right", "straight", "left", "u-turn"
    private boolean inIntersection;
    private final long arrivalTime;

    public Vehicle(String type, String direction) {
        this.id = "V" + idGenerator.incrementAndGet();
        this.type = type;
        this.direction = direction;
        this.inIntersection = false;
        this.arrivalTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDirection() {
        return direction;
    }

    public boolean isInIntersection() {
        return inIntersection;
    }

    public void setInIntersection(boolean inIntersection) {
        this.inIntersection = inIntersection;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public int compareTo(Vehicle other) {
        // Emergencia tiene prioridad; si no, orden de llegada
        if (this.type.equals("emergency") && !other.type.equals("emergency")) return -1;
        if (!this.type.equals("emergency") && other.type.equals("emergency")) return 1;
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }
}