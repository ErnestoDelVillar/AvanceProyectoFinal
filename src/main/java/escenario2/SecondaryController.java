package escenario2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SecondaryController {

    private boolean CenterWest;
    private Queue<Runnable> tasks = new LinkedList<>();

    private volatile boolean crossingNorthOccupied = false;
    private volatile boolean crossingSouthOccupied = false;
    public volatile boolean  crossingEastOccupied = false;
    private volatile boolean crossingWestOccupied = false;



    private CountDownLatch latch;


    private static PriorityBlockingQueue<Vehicle> queue = new PriorityBlockingQueue<>();
    private static PriorityBlockingQueue<Vehicle> Emergencyqueue = new PriorityBlockingQueue<>();

    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread workerThread;

    public SecondaryController() {
        workerThread = new Thread(this::processQueue);
        running.set(true);
        workerThread.start();
        this.CenterWest = false; ///
    }


    private void scheduleNext() {
        if (!tasks.isEmpty()) {
            tasks.poll().run();
        }
    }

    private void processQueue() {
        while (running.get()) {
            if (!Emergencyqueue.isEmpty()) {
                Vehicle emgvehicle = Emergencyqueue.poll();
                for (Vehicle vehicle : queue) {
                    if (vehicle.getCalle().equals(emgvehicle.getCalle())) {
                        addVehicleAnimation(vehicle);
                        // No need to remove the vehicle from the queue
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } else {
                if (!queue.isEmpty()) {
                    Vehicle vehicle = queue.peek(); // Peek instead of poll to avoid removing the vehicle
                    if (vehicle != null) {
                        addVehicleAnimation(vehicle);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            // Pequeña pausa para evitar uso excesivo de CPU 3000 120
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private synchronized void addVehicleAnimation(Vehicle vehicle) {
        tasks.offer(() -> {

            CountDownLatch latch = new CountDownLatch(1);
            HelloController.setLatch(latch);
            // HelloController.updatePositionsCenterWest();
            HelloController.update();

            try {
                latch.await(); // Esperar a que update termine
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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

    public void handleRegularVehicle() {
        Vehicle vehicle = queue.poll(); // Poll to remove the vehicle from the queue
        if (vehicle != null) {
            addVehicleAnimation(vehicle);
        }
    }


    public boolean isCenterWest() {
        return CenterWest;
    }


    public void setCenterWest() {
        if(CenterWest){
            CenterWest = false;
        }else{
            CenterWest = true;
        }

    }

    public static void removeVehicle(Vehicle vehicle) {
        if (queue.contains(vehicle)) {
            queue.remove(vehicle);
        }
        if (Emergencyqueue.contains(vehicle)) {
            Emergencyqueue.remove(vehicle);
        }
    }

}