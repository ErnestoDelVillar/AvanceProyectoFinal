package escenario2;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloController implements Initializable {


    static boolean inter01;
    static boolean inter02;
    static boolean inter03;

    static boolean inter01_Right;
    static boolean inter02_Right;
    static boolean inter03_Right;

    static boolean inter01_New;
    static boolean inter02_New;
    static boolean inter03_New;




    static boolean inter01_New_West;
    static boolean inter02_New_West;
    static boolean inter03_New_West;

    static boolean inter01_Right_West;
    static boolean inter02_Right_West;
    static boolean inter03_Right_West;





    static boolean inter01_Left;
    static boolean inter02_Left;
    static boolean inter03_Left;







    public ImageView backgroundImage;

    @FXML
    public Button btnCenterWest;

    @FXML
    public Button btnCenterEast;
    public Button btnRightTurnWest;
    public Button btnLeftTurnWest;
    public Button btnLeftTurnEast;
    public Button btnRightTurnEast;

    private boolean isClickable = true;
    private boolean isOnCooldown = false;
    private boolean isOnCooldownCenterEast = false;
    private boolean isOnCooldownRightWest = false;
    private boolean isOnCooldownRightEast = false;
    private boolean isOnCooldownLeftWest = false;
    private boolean isOnCooldownLeftEast = false;
    //  private final int COOLDOWN_DURATION = 5000; // Cooldown duration in milliseconds
    private final int COOLDOWN_DURATION = 2000; // Cooldown duration in milliseconds

    @FXML
    private StackPane stackContainer;





    @FXML
    private RadioButton emergencia;

    @FXML
    private RadioButton exit01;

    @FXML
    private RadioButton exit02;

    @FXML
    private RadioButton exit03;

    private ToggleGroup toggleGroup = new ToggleGroup();


    private static boolean emergency_bool =  false;
    private static boolean emergency_bool_CenterEast =  false;
    private static boolean emergency_bool_RightWest =  false;
    private static boolean emergency_bool_RightEast =  false;

    private static boolean emergency_bool_LeftWest =  false;
    private static boolean emergency_bool_LeftEast =  false;

    static int cantNorth = 0;
    static int cantSouth = 0;
    static int cantEast = 0;
    static int cantWest = 0;


    private static PriorityBlockingQueue<Vehicle> AllVehicles = new PriorityBlockingQueue<>();

    private List<Vehicle> vehicles = new ArrayList<>();
    private PriorityBlockingQueue<Vehicle> vehicleQueue = new PriorityBlockingQueue<>();
    TrafficController trafficController = new TrafficController();
    private int numVehiculos = 0;

    private static List<TrafficLight> trafficLights = new ArrayList<>();

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        inter01 = false;
        inter02 = false;
        inter03 = false;

        inter01_Right = false;
        inter02_Right = false;
        inter03_Right = false;

        cantNorth = 0;
        cantSouth = 0;
        cantEast = 0;
        cantWest = 0;

        toggleGroup = new ToggleGroup();
        exit01.setToggleGroup(toggleGroup);
        exit02.setToggleGroup(toggleGroup);
        exit03.setToggleGroup(toggleGroup);



        btnCenterWest.setOnAction(event -> handleButtonClick());
        btnCenterEast.setOnAction(event -> handleButtonClickCenterEast());
        btnRightTurnWest.setOnAction(event -> handleButtonClickRightEast());
        btnRightTurnEast.setOnAction(event -> handleButtonClickRightWest());
        btnLeftTurnWest.setOnAction(event -> handleButtonClickLeftWest());
        btnLeftTurnEast.setOnAction(event -> handleButtonClickLeftEast());

        emergency_bool =  false;
        emergency_bool_CenterEast =  false;
        emergency_bool_RightWest =  false;
        emergency_bool_RightEast =  false;



        exit01.setSelected(true);

        // ESCENARIO 2
        // Semaforos
        Circle circle = new Circle(20, Color.GREEN);
        translateCircle(circle, 355, -108);
        stackContainer.getChildren().add(circle);
        trafficLights.add(new TrafficLight("01", new AtomicBoolean(false), circle));


        Circle circle02 = new Circle(20, Color.GREEN);
        translateCircle(circle02, 0, -108);
        stackContainer.getChildren().add(circle02);
        trafficLights.add(new TrafficLight("02", new AtomicBoolean(false), circle02));


        Circle circle03 = new Circle(20, Color.GREEN);
        translateCircle(circle03, -352, -108);
        stackContainer.getChildren().add(circle03);
        trafficLights.add(new TrafficLight("03", new AtomicBoolean(false), circle03));


        Circle circle04 = new Circle(20, Color.RED);
        translateCircle(circle04, 355, 82);
        stackContainer.getChildren().add(circle04);
        trafficLights.add(new TrafficLight("04", new AtomicBoolean(true), circle04));

        Circle circle05 = new Circle(20, Color.RED);
        translateCircle(circle05, 0, 82);
        stackContainer.getChildren().add(circle05);
        trafficLights.add(new TrafficLight("05", new AtomicBoolean(true), circle05));

        Circle circle06 = new Circle(20, Color.RED);
        translateCircle(circle06, -352, 82);
        stackContainer.getChildren().add(circle06);
        trafficLights.add(new TrafficLight("06", new AtomicBoolean(true), circle06));


        startTrafficLightCycle();

    }



    // SISTEMA DE CAMBIO DE LUCES
    private static int currentSecond = 0;
    private void startTrafficLightCycle() {
        new AnimationTimer() {
            long lastUpdate = 0;
            final long interval = 6000000000L; // 6 seconds in nanoseconds

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1000000000L) { // 1 second in nanoseconds
                    currentSecond++;
                    lastUpdate = now;

                    if (currentSecond == 6) {
                        updateTrafficLights();
                        currentSecond = 0;
                    }
                }
            }
        }.start();
    }

    public int getCurrentSecond() {
        return currentSecond;
    }

    public static boolean isCurrentSecondLessThanFive() {
        return currentSecond < 5;
    }

    private void updateTrafficLights() {
        for (TrafficLight light : trafficLights) {
            light.changeLight();
        }
        secondaryController.setCenterWest();

    }

    private void translateCircle(Circle circle, double targetX, double targetY) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), circle);
        translateTransition.setToX(targetX);
        translateTransition.setToY(targetY);
        translateTransition.play();
    }


    private void handleButtonClick() {
        if (isOnCooldown) {
            System.out.println("Button is on cooldown. Please wait.");
            return;
        }

        handleCreateVehicleCenterWest();

        isOnCooldown = true;
        btnCenterWest.setDisable(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(COOLDOWN_DURATION),
                ae -> {
                    isOnCooldown = false;
                    btnCenterWest.setDisable(false);

                }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void handleButtonClickCenterEast() {
        if (isOnCooldownCenterEast) {
            System.out.println("Button is on cooldown. Please wait.");
            return;
        }

        handleCreateVehicleCenterEast();

        isOnCooldownCenterEast = true;
        btnCenterEast.setDisable(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(COOLDOWN_DURATION),
                ae -> {
                    isOnCooldownCenterEast = false;;
                    btnCenterEast.setDisable(false);

                }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }



    public String getSelectedExit() {
        if (exit01.isSelected()) {
            return "Exit01";
        } else if (exit02.isSelected()) {
            return "Exit02";
        } else if (exit03.isSelected()) {
            return "Exit03";
        }
        return null;
    }

    @FXML
    private void handleButtonClickRightEast() {
        if (isOnCooldownRightWest) {
            System.out.println("Button is on cooldown. Please wait.");
            return;
        }

        handleCreateVehicleWestRight();

        isOnCooldownRightWest = true;
        btnRightTurnWest.setDisable(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(COOLDOWN_DURATION),
                ae -> {
                    isOnCooldownRightWest = false;
                    btnRightTurnWest.setDisable(false);

                }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    private void handleButtonClickLeftWest() {
        if (isOnCooldownLeftWest) {
            System.out.println("Button is on cooldown. Please wait.");
            return;
        }

        handleCreateVehicleWestLeft();

        isOnCooldownLeftWest = true;
        btnLeftTurnWest.setDisable(true);


        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(COOLDOWN_DURATION),
                ae -> {
                    isOnCooldownLeftWest = false;
                    btnLeftTurnWest.setDisable(false);

                }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }


    @FXML
    private void handleButtonClickLeftEast() {
        if (isOnCooldownLeftEast) {
            System.out.println("Button is on cooldown. Please wait.");
            return;
        }

        handleCreateVehicleEastLeft();

        isOnCooldownLeftEast = true;
        btnLeftTurnEast.setDisable(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(COOLDOWN_DURATION),
                ae -> {
                    isOnCooldownLeftEast = false;
                    btnLeftTurnEast.setDisable(false);

                }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }


    @FXML
    private void handleButtonClickRightWest() {
        if (isOnCooldownRightEast) {
            System.out.println("Button is on cooldown. Please wait.");
            return;
        }

        handleCreateVehicleEastRight();

        isOnCooldownRightEast = true;
        btnRightTurnEast.setDisable(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(COOLDOWN_DURATION),
                ae -> {
                    isOnCooldownRightEast = false;
                    btnRightTurnEast.setDisable(false);

                }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }




    static int cantCenterWest = 0;
    static int cantPositions = 9;
    static int[] positions = {600, 520, 440, 270, 190, 110, -90, -170, -250, -815};
    static int[] positionsTags = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static Vehicle vehicle_emergency;



    static int cantCenterCenterEast = 0;
    static int cantPositionsCenterEast = 9;
    //static int[] positionsCenterEast = {-815, -250, -170, -90, 110, 190, 270, 440, 520, 600};
    static int[] positionsCenterEast = {-600, -520, -440, -270, -190, -110, 90, 170, 250, 815};

    static int[] positionsTagsCenterEast = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static Vehicle vehicle_emergency_CenterEast;

    SecondaryController secondaryController = new SecondaryController();
    private static PriorityBlockingQueue<Vehicle> vehiclesCenterWest = new PriorityBlockingQueue<>();
    private static PriorityBlockingQueue<Vehicle> vehiclesCenterEast = new PriorityBlockingQueue<>();


    private static PriorityBlockingQueue<Vehicle> vehiclesWestRight = new PriorityBlockingQueue<>();
    static int cantWestRight = 0;
    static int cantPositionsWestRight = 9;
    static int[] positionsWestRight = {600, 520, 440, 270, 190, 110, -90, -170, -250, -815};
    static int[] positionsTagsWestRight = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static Vehicle vehicle_emergency_WestRight;


    private static PriorityBlockingQueue<Vehicle> vehiclesWestLeft = new PriorityBlockingQueue<>();
    static int cantWestLeft = 0;
    static int cantPositionsWestLeft = 9;
    static int[] positionsWestLeft = {600, 520, 440, 270, 190, 110, -90, -170, -250, -815};
    static int[] positionsTagsWestLeft = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static Vehicle vehicle_emergency_WestLeft;


    private static PriorityBlockingQueue<Vehicle> vehiclesEastRight = new PriorityBlockingQueue<>();
    static int cantEastRight = 0;
    static int cantPositionsEastRight = 9;
    static int[] positionsEastRight = {-600, -520, -440, -270, -190, -110, 90, 170, 250, 815};
    static int[] positionsTagsEastRight = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static Vehicle vehicle_emergency_EastRight;


    private static PriorityBlockingQueue<Vehicle> vehiclesEastLeft = new PriorityBlockingQueue<>();
    static int cantEastLeft = 0;
    static int cantPositionsEastLeft = 9;
    static int[] positionsEastLeft = {-600, -520, -440, -270, -190, -110, 90, 170, 250, 815};
    static int[] positionsTagsEastLeft = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static Vehicle vehicle_emergency_EastLeft;



    public void handleCreateVehicleCenterWest() {
        if(vehiclesCenterWest.size() == 9) {
            return;
        }
        ImageView carImage = null;
        Vehicle vehicle = null;
        if(!emergencia.isSelected()) {
            carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/Auto.png")));
            carImage.setFitHeight(60);
            carImage.setFitWidth(40);
            vehicle = new Vehicle(false, "East", "West", carImage);

            vehiclesCenterWest.add(vehicle);
            cantCenterWest++;
            numVehiculos++;

            vehicle.getImageView().setTranslateX(680);
            vehicle.getImageView().setTranslateY(-108);
            vehicle.getImageView().setRotate(-90);

            stackContainer.getChildren().add(vehicle.getImageView());
            vehicles.add(vehicle);
            AllVehicles.add(vehicle);

            secondaryController.addVehicle(vehicle);

        }else {
            if(!emergency_bool){
                carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/AutoEmergencia.png")));
                carImage.setFitHeight(60);
                carImage.setFitWidth(40);
                vehicle = new Vehicle(true, "East", "West", carImage);
                emergency_bool = true;
                vehicle_emergency = vehicle;


                vehiclesCenterWest.add(vehicle);
                cantCenterWest++;
                numVehiculos++;

                vehicle.getImageView().setTranslateX(680);
                vehicle.getImageView().setTranslateY(-108);
                vehicle.getImageView().setRotate(-90);

                stackContainer.getChildren().add(vehicle.getImageView());
                vehicles.add(vehicle);
                AllVehicles.add(vehicle);

                secondaryController.addVehicle(vehicle);
            }
        }






    }
    public void handleCreateVehicleCenterEast() {

        if(vehiclesCenterEast.size() == 9) {
            return;
        }

        ImageView carImage = null;
        Vehicle vehicle = null;
        if(!emergencia.isSelected()) {
            carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/Auto.png")));
            carImage.setFitHeight(60);
            carImage.setFitWidth(40);
            vehicle = new Vehicle(false, "West", "East", carImage);

            vehiclesCenterEast.add(vehicle);
            cantCenterCenterEast++;
            numVehiculos++;

            vehicle.getImageView().setTranslateX(-680);
            vehicle.getImageView().setTranslateY(83);
            vehicle.getImageView().setRotate(90);

            stackContainer.getChildren().add(vehicle.getImageView());
            vehicles.add(vehicle);
            AllVehicles.add(vehicle);

            secondaryController.addVehicle(vehicle);

        }else{
            if(!emergency_bool_CenterEast){
                carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/AutoEmergencia.png")));
                carImage.setFitHeight(60);
                carImage.setFitWidth(40);
                vehicle = new Vehicle(true, "West", "East", carImage);
                emergency_bool_CenterEast = true;
                vehicle_emergency_CenterEast = vehicle;

                vehiclesCenterEast.add(vehicle);
                cantCenterCenterEast++;
                numVehiculos++;

                vehicle.getImageView().setTranslateX(-680);
                vehicle.getImageView().setTranslateY(83);
                vehicle.getImageView().setRotate(90);

                stackContainer.getChildren().add(vehicle.getImageView());
                vehicles.add(vehicle);
                AllVehicles.add(vehicle);

                secondaryController.addVehicle(vehicle);
            }

        }



    }

    public void handleCreateVehicleWestRight() {
        if (vehiclesWestRight.size() == 9) {
            return;
        }

        ImageView carImage = null;
        Vehicle vehicle = null;



        if (!emergencia.isSelected()) {
            carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/Auto.png")));
            carImage.setFitHeight(60);
            carImage.setFitWidth(40);
            vehicle = new Vehicle(false, "East",  getSelectedExit(), carImage);

            vehiclesWestRight.add(vehicle);
            cantWestRight++;
            numVehiculos++;

            vehicle.getImageView().setTranslateX(680);
            vehicle.getImageView().setTranslateY(-164);
            vehicle.getImageView().setRotate(-90);

            stackContainer.getChildren().add(vehicle.getImageView());
            vehicles.add(vehicle);
            AllVehicles.add(vehicle);

            secondaryController.addVehicle(vehicle);
        } else {
            if(!emergency_bool_RightWest){
                carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/AutoEmergencia.png")));
                carImage.setFitHeight(60);
                carImage.setFitWidth(40);
                vehicle = new Vehicle(true, "East",  getSelectedExit(), carImage);
                // System.out.println("CALLE: " + calle);
                emergency_bool_RightWest = true;
                vehicle_emergency_WestRight = vehicle;


                vehiclesWestRight.add(vehicle);
                cantWestRight++;
                numVehiculos++;

                vehicle.getImageView().setTranslateX(680);
                vehicle.getImageView().setTranslateY(-164);
                vehicle.getImageView().setRotate(-90);

                stackContainer.getChildren().add(vehicle.getImageView());
                vehicles.add(vehicle);
                AllVehicles.add(vehicle);

                secondaryController.addVehicle(vehicle);
            }


        }

    }


    public void handleCreateVehicleWestLeft() {
        if (vehiclesWestLeft.size() == 9) {
            return;
        }

        ImageView carImage = null;
        Vehicle vehicle = null;



        if (!emergencia.isSelected()) {
            carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/Auto.png")));
            carImage.setFitHeight(60);
            carImage.setFitWidth(40);
            vehicle = new Vehicle(false, "East",  getSelectedExit(), carImage);
            System.out.println("CALLE: " + getSelectedExit());

            vehiclesWestLeft.add(vehicle);
            cantWestLeft++;
            numVehiculos++;

            vehicle.getImageView().setTranslateX(680);
            vehicle.getImageView().setTranslateY(-50);
            vehicle.getImageView().setRotate(-90);

            stackContainer.getChildren().add(vehicle.getImageView());
            vehicles.add(vehicle);
            AllVehicles.add(vehicle);

            secondaryController.addVehicle(vehicle);
        } else {
            if(!emergency_bool_LeftWest && !emergency_bool_LeftEast){
                carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/AutoEmergencia.png")));
                carImage.setFitHeight(60);
                carImage.setFitWidth(40);
                vehicle = new Vehicle(true, "East",  getSelectedExit(), carImage);
                emergency_bool_LeftWest = true;
                emergency_bool_LeftEast = false;
                vehicle_emergency_WestLeft = vehicle;


                vehiclesWestLeft.add(vehicle);
                cantWestLeft++;
                numVehiculos++;

                vehicle.getImageView().setTranslateX(680);
                vehicle.getImageView().setTranslateY(-50);
                vehicle.getImageView().setRotate(-90);

                stackContainer.getChildren().add(vehicle.getImageView());
                vehicles.add(vehicle);
                AllVehicles.add(vehicle);

                secondaryController.addVehicle(vehicle);
            }



        }

    }




    public void handleCreateVehicleEastRight() {
        if (vehiclesEastRight.size() == 9) {
            return;
        }

        ImageView carImage = null;
        Vehicle vehicle = null;



        if (!emergencia.isSelected()) {
            carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/Auto.png")));
            carImage.setFitHeight(60);
            carImage.setFitWidth(40);
            vehicle = new Vehicle(false, "West",  getSelectedExit(), carImage);

            vehiclesEastRight.add(vehicle);
            cantEastRight++;
            numVehiculos++;

            vehicle.getImageView().setTranslateX(-680);
            vehicle.getImageView().setTranslateY(140);
            vehicle.getImageView().setRotate(90);

            stackContainer.getChildren().add(vehicle.getImageView());
            vehicles.add(vehicle);
            AllVehicles.add(vehicle);

            secondaryController.addVehicle(vehicle);
        } else {
            if(!emergency_bool_RightEast){
                carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/AutoEmergencia.png")));
                carImage.setFitHeight(60);
                carImage.setFitWidth(40);
                vehicle = new Vehicle(true, "West",  getSelectedExit(), carImage);
                // System.out.println("CALLE: " + calle);
                emergency_bool_RightEast = true;
                vehicle_emergency_EastRight = vehicle;


                vehiclesEastRight.add(vehicle);
                cantEastRight++;
                numVehiculos++;

                vehicle.getImageView().setTranslateX(-680);
                vehicle.getImageView().setTranslateY(140);
                vehicle.getImageView().setRotate(90);

                stackContainer.getChildren().add(vehicle.getImageView());
                vehicles.add(vehicle);
                AllVehicles.add(vehicle);

                secondaryController.addVehicle(vehicle);
            }


        }

    }

    public void handleCreateVehicleEastLeft() {
        if (vehiclesEastLeft.size() == 9) {
            return;
        }

        ImageView carImage = null;
        Vehicle vehicle = null;



        if (!emergencia.isSelected()) {
            carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/Auto.png")));
            carImage.setFitHeight(60);
            carImage.setFitWidth(40);
            vehicle = new Vehicle(false, "West",  getSelectedExit(), carImage);
            System.out.println("CALLE: " + getSelectedExit());

            vehiclesEastLeft.add(vehicle);
            cantEastLeft++;
            numVehiculos++;

            vehicle.getImageView().setTranslateX(-680);
            vehicle.getImageView().setTranslateY(25);
            vehicle.getImageView().setRotate(90);

            stackContainer.getChildren().add(vehicle.getImageView());
            vehicles.add(vehicle);
            AllVehicles.add(vehicle);

            secondaryController.addVehicle(vehicle);
        } else {
            if(!emergency_bool_LeftEast && !emergency_bool_LeftWest){
                carImage = new ImageView(new Image(getClass().getResourceAsStream("/escenario2/AutoEmergencia.png")));
                carImage.setFitHeight(60);
                carImage.setFitWidth(40);
                vehicle = new Vehicle(true, "West",  getSelectedExit(), carImage);
                emergency_bool_LeftEast = true;
                emergency_bool_LeftWest = false;
                vehicle_emergency_EastLeft = vehicle;


                vehiclesEastLeft.add(vehicle);
                cantEastLeft++;
                numVehiculos++;

                vehicle.getImageView().setTranslateX(-680);
                vehicle.getImageView().setTranslateY(25);
                vehicle.getImageView().setRotate(90);

                stackContainer.getChildren().add(vehicle.getImageView());
                vehicles.add(vehicle);
                AllVehicles.add(vehicle);

                secondaryController.addVehicle(vehicle);
            }



        }

    }





    private static CountDownLatch latch;


    public static void update(){
        updatePositionsRightWest();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        updatePositionsCenterWest();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        updatePositionsCenterEast();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        updatePositionsLeftWest();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        updatePositionsRightEast();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        updatePositionsLeftEast();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("EMERGENCY LEFT EAST: " + emergency_bool_LeftEast);
        System.out.println("EMERGENCY LEFT WEST: " + emergency_bool_LeftWest);


        System.out.println("\n");
        System.out.println("INTER01: " + inter01);
        System.out.println("INTER02: " + inter02);
        System.out.println("INTER03: " + inter03);

        System.out.println("INTER01 RIGHT: " + inter01_Right);
        System.out.println("INTER02 RIGHT: " + inter02_Right);
        System.out.println("INTER03 RIGHT: " + inter03_Right);
        System.out.println("\n");


        if (latch != null) {
            latch.countDown();

        }
    }

    // WEST
    public static void updatePositionsCenterWest() {

        Iterator<Vehicle> iterator = vehiclesCenterWest.iterator();
        while (iterator.hasNext()) {
            System.out.println("EMERGENCY: " + emergency_bool);
            Vehicle vehicle = iterator.next();
            int centerInt = vehicle.getCenterInt();
            int nextPos =0;
            if (centerInt < cantPositions) {


                if(vehicle == vehicle_emergency)
                    nextPos = getNextPositionEmergency(centerInt, cantPositions, positionsTags, emergency_bool, trafficLights.getFirst());
                else
                    nextPos = getNextPosition_Fix(centerInt, cantPositions, positionsTags, emergency_bool, vehicle_emergency, trafficLights.getFirst() );

                if (nextPos != -1) {

                    moveVehicle_Fix(vehicle, nextPos, positionsTags, positions, vehiclesCenterWest, cantCenterWest, 0);

                }
            }
        }

    }

    public static void updatePositionsRightWest() {

        Iterator<Vehicle> iterator = vehiclesWestRight.iterator();
        while (iterator.hasNext()) {
            System.out.println("EMERGENCY: " + emergency_bool_RightWest);
            Vehicle vehicle = iterator.next();
            int centerInt = vehicle.getCenterInt();
            int nextPos =0;
            if (centerInt < cantPositionsWestRight) {


                if(vehicle.isEmergency()) {
                    nextPos = getNextPosition_Fix_Right(vehicle, centerInt, cantPositionsWestRight, positionsTagsWestRight, emergency_bool_RightWest, vehicle_emergency_WestRight, trafficLights.getFirst());
                }
                else
                    nextPos = getNextPosition_Fix_Right(vehicle, centerInt, cantPositionsWestRight, positionsTagsWestRight, emergency_bool_RightWest, vehicle_emergency_WestRight, trafficLights.getFirst() );

                System.out.println("Next Post: " + nextPos);

                if (nextPos != -1) {

                    moveVehicle_Fix_Right(vehicle, nextPos, positionsTagsWestRight, positionsWestRight, vehiclesWestRight, cantWestRight, 0);

                }
            }
        }

    }




    public static void updatePositionsLeftWest() {

        Iterator<Vehicle> iterator = vehiclesWestLeft.iterator();
        while (iterator.hasNext()) {
            System.out.println("emergency_bool_LeftWest: " + emergency_bool_LeftWest);
            Vehicle vehicle = iterator.next();
            int centerInt = vehicle.getCenterInt();
            int nextPos =0;
            if (centerInt < cantPositionsWestLeft) {


                if(vehicle.isEmergency()) {
                    nextPos = getNextPosition_FixNewWest(centerInt, cantPositionsWestLeft, positionsTagsWestLeft, emergency_bool_LeftWest, vehicle_emergency_WestLeft, trafficLights.getFirst());
                }

                else
                    nextPos = getNextPosition_FixNewWest(centerInt, cantPositionsWestLeft, positionsTagsWestLeft, emergency_bool_LeftWest, vehicle_emergency_WestLeft, trafficLights.getFirst() );

                System.out.println("Next Post: " + nextPos);

                if (nextPos != -1) {

                    moveVehiclet_FixTurnWest(vehicle, nextPos, positionsTagsWestLeft, positionsWestLeft, vehiclesWestLeft, cantWestLeft, 0);



                }
            }
        }

    }
    private static int getNextPosition_FixNewWest(int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, Vehicle vehicle_emergency, TrafficLight trafficLight) {
        System.out.println("getNextPosition");

        if (currentPos == 2) {
            if (inter03 && inter03_Right && inter01_New && inter01_Left) return -1;
        } else if (currentPos == 5) {
            if (inter02 && inter02_Right && inter02_New && inter02_Left) return -1;
        } else if (currentPos == 8) {
            if (inter01 && inter01_Right && inter03_New && inter03_Left) return -1;
        }



        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;



            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                // trafficLights.getFirst().getCircle().getFill() == Color.GREEN
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()   ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                if(vehicle_emergency.getCenterInt() > currentPos){
                    if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()  ) {
                        return 8;
                    }
                }
                else
                    return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }
    private static void moveVehiclet_FixTurnWest(Vehicle vehicle, int nextPos, int[] positionsTags, int[] positions, PriorityBlockingQueue vehicles, int cant, int cantChoose) {
        System.out.println("moveVehiclet_FixTurnWest");


        if(vehicle.getCenterInt() == 2)
        {
            inter03 = true;

            inter03_Left = true;
            inter03_Right_West = true;
            inter03_New_West = true;

        }else if(vehicle.getCenterInt() == 5)
        {
            inter02 = true;

            inter02_Left = true;
            inter02_Right_West = true;
            inter02_New_West= true;

        }else if(vehicle.getCenterInt() == 8)
        {
            inter01 = true;

            inter01_Left = true;
            inter01_New_West = true;
            inter01_Right_West = true;

        }

        int newCenterInt;
        if (nextPos == 8) {
            newCenterInt = 9;
            positionsTags[newCenterInt - 1] = 0;
        } else {
            newCenterInt = vehicle.getCenterInt() + nextPos;
            positionsTags[newCenterInt] = 1;

            if (vehicle.getCenterInt() >= 0) {
                positionsTags[vehicle.getCenterInt()] = 0;
            }
        }

        int Xpos = positions[newCenterInt];

        if (vehicle.getCenterInt() == 2 && vehicle.getCalle().equals("Exit01")) {
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[2] - 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(-90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {





                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesWestLeft.remove(vehicle);
                cantWestLeft--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsWestLeft[newCenterInt] = 0;

                inter03 = false;

                inter03_Left = false;
                inter03_New_West = false;
                inter03_Right_West = false;
                if(vehicle.isEmergency())
                    emergency_bool_LeftWest = false;

            });
            sequentialTransition.play();
        } else if(vehicle.getCenterInt() == 5 && vehicle.getCalle().equals("Exit02")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[5] - 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(-90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {



                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesWestLeft.remove(vehicle);
                cantWestLeft--;


                SecondaryController.removeVehicle(vehicle);
                positionsTagsWestLeft[newCenterInt] = 0;

                inter02 = false;

                inter02_Left = false;
                inter02_New_West = false;
                inter02_Right_West = false;
                if(vehicle.isEmergency())
                    emergency_bool_LeftWest = false;

            });
            sequentialTransition.play();
        }else if(vehicle.getCenterInt() == 8 && vehicle.getCalle().equals("Exit03")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[8] - 65);

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(-90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {


                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());
                // vehicles.remove(vehicle);

                vehiclesWestLeft.remove(vehicle);
                cantWestLeft--;


                SecondaryController.removeVehicle(vehicle);
                positionsTagsWestLeft[newCenterInt] = 0;

                inter01 = false;
                inter01_Left = false;
                inter01_New_West = false;
                inter01_Right_West = false;
                if(vehicle.isEmergency())
                    emergency_bool_LeftWest = false;


            });
            sequentialTransition.play();
        }



        else {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.8), vehicle.getImageView());
            translateTransition.setToX(Xpos);
            translateTransition.setOnFinished(event -> {



                if(vehicle.getCenterInt() == 2)
                {
                    inter03 = false;
                    inter03_Left = false;
                    inter03_New_West = false;
                    inter03_Right_West = false;

                }else if(vehicle.getCenterInt() == 5)
                {
                    inter02 = false;

                    inter02_Left = false;
                    inter02_New_West = false;
                    inter02_Right_West = false;

                }else if(vehicle.getCenterInt() == 8)
                {
                    inter01 = false;

                    inter01_Left = false;
                    inter01_New_West = false;
                    inter01_Right_West = false;

                }

                vehicle.setCenterInt(newCenterInt);

            });
            translateTransition.play();
        }


        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.toString(positionsTags));
        System.out.println("Pos " + nextPos);
        System.out.println("\n");
    }


    public static void updatePositionsLeftEast() {

        Iterator<Vehicle> iterator = vehiclesEastLeft.iterator();
        while (iterator.hasNext()) {
            System.out.println("emergency_bool_LeftEast: " + emergency_bool_LeftEast);
            Vehicle vehicle = iterator.next();
            int centerInt = vehicle.getCenterInt();
            int nextPos =0;
            if (centerInt < cantPositionsEastLeft) {

                //    private static int getNextPosition(int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, Vehicle vehicle_emergency, TrafficLight trafficLight) {

                if(vehicle.isEmergency()) {
                    nextPos =  getNextPosition_FixNew(centerInt, cantPositionsEastLeft, positionsTagsEastLeft, emergency_bool_LeftEast,  vehicle_emergency_EastLeft, trafficLights.get(3));
                }

                else
                    nextPos = getNextPosition_FixNew(centerInt, cantPositionsEastLeft, positionsTagsEastLeft, emergency_bool_LeftEast, vehicle_emergency_EastLeft, trafficLights.get(3) );

                System.out.println("Next Post: " + nextPos);

                if (nextPos != -1) {


                    moveVehiclet_FixTurn(vehicle, nextPos, positionsTagsEastLeft, positionsEastLeft, vehiclesEastLeft, cantEastLeft, 0);



                }
            }
        }

    }




    private static void moveVehicle_Fix(Vehicle vehicle, int nextPos, int[] positionsTags, int[] positions, PriorityBlockingQueue vehicles, int cant, int cantChoose) {
        System.out.println("moveVehicle_Fix");



        if(vehicle.getCenterInt() == 2)
        {
            inter03 = true;

            inter03_Left = true;
            inter03_Right_West = true;
            inter03_New_West = true;

        }else if(vehicle.getCenterInt() == 5)
        {
            inter02 = true;

            inter02_Left = true;
            inter02_Right_West = true;
            inter02_New_West= true;

        }else if(vehicle.getCenterInt() == 8)
        {
            inter01 = true;

            inter01_Left = true;
            inter01_New_West = true;
            inter01_Right_West = true;

        }



        int newCenterInt;
        if (nextPos == 8) {
            newCenterInt = 9;
            positionsTags[newCenterInt - 1] = 0;
        } else {
            newCenterInt = vehicle.getCenterInt() + nextPos;
            positionsTags[newCenterInt] = 1;

            if (vehicle.getCenterInt() >= 0) {
                positionsTags[vehicle.getCenterInt()] = 0;
            }
        }

        int Xpos = positions[newCenterInt];



        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.8), vehicle.getImageView());
        translateTransition.setToX(Xpos);
        translateTransition.setOnFinished(event -> {


            if(vehicle.getCenterInt() == 2)
            {
                inter03 = false;

                inter03_Left = false;
                inter03_Right_West = false;
                inter03_New_West = false;

            }else if(vehicle.getCenterInt() == 5)
            {
                inter02 = false;

                inter02_Left = false;
                inter02_Right_West = false;
                inter02_New_West= false;

            }else if(vehicle.getCenterInt() == 8)
            {
                inter01 = false;

                inter01_Left = false;
                inter01_New_West = false;
                inter01_Right_West = false;

            }



            vehicle.setCenterInt(newCenterInt);


            if (newCenterInt == 9) {
                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());

                if (vehicle.isEmergency()) {
                    emergency_bool = false;


                    vehicles.remove(vehicle);
                } else {
                    vehicles.remove(vehicle);
                }
            }
        });
        translateTransition.play();

        if (newCenterInt == 9) {
            if (cantChoose == 0)
                cantCenterWest--;
            else
                cantCenterCenterEast--;

            SecondaryController.removeVehicle(vehicle);
            System.out.println("Car has reached the final position!");
        }

        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.toString(positionsTags));
        System.out.println("Pos " + nextPos);
        System.out.println("\n");


    }
    private static int getNextPosition_Fix(int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, Vehicle vehicle_emergency, TrafficLight trafficLight) {
        System.out.println("getNextPosition");
        if (currentPos == 2) {
            if (inter03 && inter03_Right && inter01_New && inter01_Left) return -1;
        } else if (currentPos == 5) {
            if (inter02 && inter02_Right && inter02_New && inter02_Left) return -1;
        } else if (currentPos == 8) {
            if (inter01 && inter01_Right && inter03_New && inter03_Left) return -1;
        }




        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;



            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                // trafficLights.getFirst().getCircle().getFill() == Color.GREEN
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()   ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                if(vehicle_emergency.getCenterInt() > currentPos){
                    if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()  ) {
                        return 8;
                    }
                }
                else
                    return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }


    private static void moveVehicle_Fix_Right(Vehicle vehicle, int nextPos, int[] positionsTags, int[] positions, PriorityBlockingQueue vehicles, int cant, int cantChoose) {
        System.out.println("moveVehicle_Fix_Right");

        int newCenterInt;
        if (nextPos == 8) {
            newCenterInt = 9;
            positionsTags[newCenterInt - 1] = 0;
        } else {
            newCenterInt = vehicle.getCenterInt() + nextPos;
            positionsTags[newCenterInt] = 1;

            if (vehicle.getCenterInt() >= 0) {
                positionsTags[vehicle.getCenterInt()] = 0;
            }
        }

        if(vehicle.getCenterInt() == 2)
        {
            inter01_Right  = true;

        }else if(vehicle.getCenterInt() == 5)
        {
            inter02_Right  = true;

        }else if(vehicle.getCenterInt() == 8)
        {
            inter03_Right  = true;

        }



        int Xpos = positions[newCenterInt];

        if (vehicle.getCenterInt() == 2 && vehicle.getCalle().equals("Exit01")) {
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[2] - 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(-330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {





                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesWestRight.remove(vehicle);
                cantWestRight--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsWestRight[newCenterInt] = 0;

                inter01_Right = false;
                if(vehicle.isEmergency())
                    emergency_bool_RightWest = false;





            });
            sequentialTransition.play();
        } else if(vehicle.getCenterInt() == 5 && vehicle.getCalle().equals("Exit02")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[5] - 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(-330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {



                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesWestRight.remove(vehicle);
                cantWestRight--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsWestRight[newCenterInt] = 0;

                inter02_Right = false;
                if(vehicle.isEmergency())
                    emergency_bool_RightWest = false;

            });
            sequentialTransition.play();
        }else if(vehicle.getCenterInt() == 8 && vehicle.getCalle().equals("Exit03")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[8] - 65);

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(-330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {


                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesWestRight.remove(vehicle);
                cantWestRight--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsWestRight[newCenterInt] = 0;

                inter03_Right = false;
                if(vehicle.isEmergency())
                    emergency_bool_RightWest = false;


            });
            sequentialTransition.play();
        }



        else {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.8), vehicle.getImageView());
            translateTransition.setToX(Xpos);
            translateTransition.setOnFinished(event -> {



                if(vehicle.getCenterInt() == 2)
                {
                    inter01_Right  = false;

                }else if(vehicle.getCenterInt() == 5)
                {
                    inter02_Right  = false;

                }else if(vehicle.getCenterInt() == 8)
                {
                    inter03_Right = false;

                }

                vehicle.setCenterInt(newCenterInt);

            });
            translateTransition.play();
        }


        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.toString(positionsTags));
        System.out.println("Pos " + nextPos);
        System.out.println("\n");


    }
    private static int getNextPosition_Fix_Right(Vehicle vehicle, int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, Vehicle vehicle_emergency, TrafficLight trafficLight) {



        if (currentPos == 2 && vehicle.getCalle().equals("Exit01")) {
            if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                return 3;
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;
        }else if(currentPos == 5 && vehicle.getCalle().equals("Exit02")){
            if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                return 3;
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;}
        else if(currentPos == 8 && vehicle.getCalle().equals("Exit03")){

            return 8;
        }

        if (currentPos == 2) {
            if (inter01_Right ) return -1;
        } else if (currentPos == 5) {
            if (inter02_Right ) return -1;
        } else if (currentPos == 8) {
            if (inter03_Right ) return -1;
        }



        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;



            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                // trafficLights.getFirst().getCircle().getFill() == Color.GREEN
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()   ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                if(vehicle_emergency.getCenterInt() > currentPos){
                    if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()  ) {
                        return 8;
                    }
                }
                else
                    return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }






    private static int getNextPosition_FixNew(int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, Vehicle vehicle_emergency, TrafficLight trafficLight) {
        System.out.println("getNextPosition");

        if (currentPos == 2) {
            if (inter03 && inter03_Right && inter01_New) return -1;
        } else if (currentPos == 5) {
            if (inter02 && inter02_Right && inter02_New) return -1;
        } else if (currentPos == 8) {
            if (inter01 && inter01_Right && inter03_New) return -1;
        }

        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;



            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                // trafficLights.getFirst().getCircle().getFill() == Color.GREEN
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()   ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                if(vehicle_emergency.getCenterInt() > currentPos){
                    if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()  ) {
                        return 8;
                    }
                }
                else
                    return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }
    private static void moveVehiclet_FixTurn(Vehicle vehicle, int nextPos, int[] positionsTags, int[] positions, PriorityBlockingQueue vehicles, int cant, int cantChoose) {
        System.out.println("moveVehiclet_FixTurn");


        if(vehicle.getCenterInt() == 2)
        {
            inter03 = true;
            inter03_Right = true;
            inter03_New = true;


        }else if(vehicle.getCenterInt() == 5)
        {
            inter02 = true;
            inter02_Right = true;
            inter02_New = true;

        }else if(vehicle.getCenterInt() == 8)
        {
            inter01 = true;
            inter01_New = true;
            inter01_Right = true;

        }

        int newCenterInt;
        if (nextPos == 8) {
            newCenterInt = 9;
            positionsTags[newCenterInt - 1] = 0;
        } else {
            newCenterInt = vehicle.getCenterInt() + nextPos;
            positionsTags[newCenterInt] = 1;

            if (vehicle.getCenterInt() >= 0) {
                positionsTags[vehicle.getCenterInt()] = 0;
            }
        }

        int Xpos = positions[newCenterInt];

        if (vehicle.getCenterInt() == 2 && vehicle.getCalle().equals("Exit01")) {
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[2] + 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(-90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(-330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {

                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesEastLeft.remove(vehicle);
                cantEastLeft--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsEastLeft[newCenterInt] = 0;

                inter03 = false;
                inter03_New = false;
                inter03_Right = false;
                if(vehicle.isEmergency())
                    emergency_bool_LeftEast = false;

            });
            sequentialTransition.play();
        } else if(vehicle.getCenterInt() == 5 && vehicle.getCalle().equals("Exit02")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[5] + 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(-90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(-330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {

                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesEastLeft.remove(vehicle);
                cantEastLeft--;


                SecondaryController.removeVehicle(vehicle);
                positionsTagsEastLeft[newCenterInt] = 0;

                inter02 = false;
                inter02_New = false;
                inter02_Right = false;
                if(vehicle.isEmergency())
                    emergency_bool_LeftEast = false;

            });
            sequentialTransition.play();
        }else if(vehicle.getCenterInt() == 8 && vehicle.getCalle().equals("Exit03")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[8] + 65);

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(-90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(-330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {


                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());

                vehiclesEastLeft.remove(vehicle);
                cantEastLeft--;


                SecondaryController.removeVehicle(vehicle);
                positionsTagsEastLeft[newCenterInt] = 0;

                inter01 = false;
                inter01_New = false;
                inter01_Right = false;
                if(vehicle.isEmergency())
                    emergency_bool_LeftEast = false;


            });
            sequentialTransition.play();
        }



        else {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.8), vehicle.getImageView());
            translateTransition.setToX(Xpos);
            translateTransition.setOnFinished(event -> {



                if(vehicle.getCenterInt() == 2)
                {
                    inter03 = false;
                    inter03_New = false;
                    inter03_Right = false;

                }else if(vehicle.getCenterInt() == 5)
                {
                    inter02 = false;
                    inter02_New = false;
                    inter02_Right = false;

                }else if(vehicle.getCenterInt() == 8)
                {
                    inter01 = false;
                    inter01_New = false;
                    inter01_Right = false;

                }

                vehicle.setCenterInt(newCenterInt);

            });
            translateTransition.play();
        }


        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.toString(positionsTags));
        System.out.println("Pos " + nextPos);
        System.out.println("\n");
    }


    public static void updatePositionsCenterEast() {

        Iterator<Vehicle> iterator = vehiclesCenterEast.iterator();
        while (iterator.hasNext()) {
            System.out.println("EMERGENCY: " + emergency_bool_CenterEast);
            Vehicle vehicle = iterator.next();
            int centerInt = vehicle.getCenterInt();
            int nextPos =0;
            if (centerInt < cantPositionsCenterEast) {

                if(vehicle == vehicle_emergency_CenterEast)
                    nextPos = getNextPositionEmergency_FixEast( centerInt, cantPositionsCenterEast, positionsTagsCenterEast, emergency_bool_CenterEast, trafficLights.get(3));
                else
                    nextPos = getNextPosition_FixEast(centerInt, cantPositionsCenterEast, positionsTagsCenterEast, emergency_bool_CenterEast, vehicle_emergency_CenterEast, trafficLights.get(3));

                if (nextPos != -1) {
                    moveVehicle_FixEast(vehicle, nextPos, positionsTagsCenterEast, positionsCenterEast, vehiclesCenterEast, cantCenterCenterEast, 1);
                }
            }
        }


    }

    private static void moveVehicle_FixEast(Vehicle vehicle, int nextPos, int[] positionsTags, int[] positions, PriorityBlockingQueue vehicles, int cant, int cantChoose) {
        System.out.println("moveVehicle_FixEast");



        if(vehicle.getCenterInt() == 2)
        {
            inter01_New= true;

        }else if(vehicle.getCenterInt() == 5)
        {
            inter02_New = true;

        }else if(vehicle.getCenterInt() == 8)
        {
            inter03_New = true;

        }

        int newCenterInt;
        if (nextPos == 8) {
            newCenterInt = 9;
            positionsTags[newCenterInt - 1] = 0;
        } else {
            newCenterInt = vehicle.getCenterInt() + nextPos;
            positionsTags[newCenterInt] = 1;

            if (vehicle.getCenterInt() >= 0) {
                positionsTags[vehicle.getCenterInt()] = 0;
            }
        }

        int Xpos = positions[newCenterInt];


        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.8), vehicle.getImageView());
        translateTransition.setToX(Xpos);
        translateTransition.setOnFinished(event -> {


            if(vehicle.getCenterInt() == 2)
            {
                inter01_New = false;

            }else if(vehicle.getCenterInt() == 5)
            {
                inter02_New = false;

            }else if(vehicle.getCenterInt() == 8)
            {
                inter03_New = false;

            }

            vehicle.setCenterInt(newCenterInt);



            if (newCenterInt == 9) {
                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());

                if (vehicle.isEmergency()) {
                    emergency_bool = false;


                    vehicles.remove(vehicle);
                } else {
                    vehicles.remove(vehicle);
                }
            }
        });
        translateTransition.play();

        if (newCenterInt == 9) {
            if (cantChoose == 0)
                cantCenterWest--;
            else
                cantCenterCenterEast--;

            SecondaryController.removeVehicle(vehicle);
            System.out.println("Car has reached the final position!");
        }

        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.toString(positionsTags));
        System.out.println("Pos " + nextPos);
        System.out.println("\n");


    }
    private static int getNextPosition_FixEast(int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, Vehicle vehicle_emergency, TrafficLight trafficLight) {
        System.out.println("getNextPosition_FixEast");

        if (currentPos == 2) {
            if (inter01_New) return -1;
        } else if (currentPos == 5) {
            if (inter02_New) return -1;
        } else if (currentPos == 8) {
            if (inter03_New) return -1;
        }



        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;



            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                // trafficLights.getFirst().getCircle().getFill() == Color.GREEN
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()   ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                if(vehicle_emergency.getCenterInt() > currentPos){
                    if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()  ) {
                        return 8;
                    }
                }
                else
                    return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }
    private static int getNextPositionEmergency_FixEast(int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, TrafficLight trafficLight) {
        System.out.println("getNextPositionEmergency_FixEast");

        if (currentPos == 2) {
            if (inter01_New) return -1;
        } else if (currentPos == 5) {
            if (inter02_New) return -1;
        } else if (currentPos == 8) {
            if (inter03_New) return -1;
        }

        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;
            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia


                if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                    return 3;
                if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                    return 2;
                if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                    return 1;

            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){
                if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                    return 3;
                if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                    return 2;
                if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                    return 1;
            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }




















    public static void updatePositionsRightEast() {

        Iterator<Vehicle> iterator = vehiclesEastRight.iterator();
        while (iterator.hasNext()) {
            System.out.println("EMERGENCY: " + emergency_bool_RightEast);
            Vehicle vehicle = iterator.next();
            int centerInt = vehicle.getCenterInt();
            int nextPos =0;
            if (centerInt < cantPositionsEastRight) {


                if(vehicle.isEmergency()) {
                    nextPos = getNextPosition_Fix_Right_East(vehicle, centerInt, cantPositionsEastRight, positionsTagsEastRight, emergency_bool_RightEast, vehicle_emergency_EastRight, trafficLights.get(3));
                }
                else
                    nextPos = getNextPosition_Fix_Right_East(vehicle, centerInt, cantPositionsEastRight, positionsTagsEastRight, emergency_bool_RightEast, vehicle_emergency_EastRight, trafficLights.get(3) );

                System.out.println("Next Post: " + nextPos);

                if (nextPos != -1) {

                    moveVehicle_Fix_Right_East(vehicle, nextPos, positionsTagsEastRight, positionsEastRight, vehiclesEastRight, cantEastRight, 0);

                }
            }
        }

    }

    private static void moveVehicle_Fix_Right_East(Vehicle vehicle, int nextPos, int[] positionsTags, int[] positions, PriorityBlockingQueue vehicles, int cant, int cantChoose) {
        System.out.println("moveVehicle_Fix_Right");

        int newCenterInt;
        if (nextPos == 8) {
            newCenterInt = 9;
            positionsTags[newCenterInt - 1] = 0;
        } else {
            newCenterInt = vehicle.getCenterInt() + nextPos;
            positionsTags[newCenterInt] = 1;

            if (vehicle.getCenterInt() >= 0) {
                positionsTags[vehicle.getCenterInt()] = 0;
            }
        }

        if(vehicle.getCenterInt() == 2)
        {
            inter01_Right_West  = true;

        }else if(vehicle.getCenterInt() == 5)
        {
            inter02_Right_West  = true;

        }else if(vehicle.getCenterInt() == 8)
        {
            inter03_Right_West  = true;

        }



        int Xpos = positions[newCenterInt];

        if (vehicle.getCenterInt() == 2 && vehicle.getCalle().equals("Exit01")) {
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[2] + 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {





                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesEastRight.remove(vehicle);
                cantEastRight--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsEastRight[newCenterInt] = 0;

                inter01_Right_West = false;
                if(vehicle.isEmergency())
                    emergency_bool_RightEast = false;





            });
            sequentialTransition.play();
        } else if(vehicle.getCenterInt() == 5 && vehicle.getCalle().equals("Exit02")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[5] + 65); // Adjust the value as needed

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {



                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesEastRight.remove(vehicle);
                cantEastRight--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsEastRight[newCenterInt] = 0;

                inter02_Right_West = false;
                if(vehicle.isEmergency())
                    emergency_bool_RightEast = false;

            });
            sequentialTransition.play();
        }else if(vehicle.getCenterInt() == 8 && vehicle.getCalle().equals("Exit03")){
            TranslateTransition moveForward1 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward1.setToX(positions[8] + 65);

            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), vehicle.getImageView());
            rotate.setByAngle(90);

            TranslateTransition moveForward2 = new TranslateTransition(Duration.seconds(0.5), vehicle.getImageView());
            moveForward2.setToY(330);

            SequentialTransition sequentialTransition = new SequentialTransition(moveForward1, rotate, moveForward2);
            sequentialTransition.setOnFinished(event -> {


                ((StackPane) vehicle.getImageView().getParent()).getChildren().remove(vehicle.getImageView());                //vehicles.remove(vehicle);

                vehiclesEastRight.remove(vehicle);
                cantEastRight--;

                SecondaryController.removeVehicle(vehicle);
                positionsTagsEastRight[newCenterInt] = 0;

                inter03_Right_West = false;
                if(vehicle.isEmergency())
                    emergency_bool_RightEast = false;


            });
            sequentialTransition.play();
        }



        else {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.8), vehicle.getImageView());
            translateTransition.setToX(Xpos);
            translateTransition.setOnFinished(event -> {



                if(vehicle.getCenterInt() == 2)
                {
                    inter01_Right_West  = false;

                }else if(vehicle.getCenterInt() == 5)
                {
                    inter02_Right_West  = false;

                }else if(vehicle.getCenterInt() == 8)
                {
                    inter03_Right_West = false;

                }

                vehicle.setCenterInt(newCenterInt);

            });
            translateTransition.play();
        }


        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.toString(positionsTags));
        System.out.println("Pos " + nextPos);
        System.out.println("\n");


    }

    private static int getNextPosition_Fix_Right_East(Vehicle vehicle, int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, Vehicle vehicle_emergency, TrafficLight trafficLight) {
        System.out.println("getNextPosition_Fix_Right");


        if (currentPos == 2 && vehicle.getCalle().equals("Exit01")) {
            if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                return 3;
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;
        }else if(currentPos == 5 && vehicle.getCalle().equals("Exit02")){
            if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                return 3;
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;}
        else if(currentPos == 8 && vehicle.getCalle().equals("Exit03")){

            return 8;
        }



        if (currentPos == 2 ) {
            if (inter01_Right_West ) return -1;
        } else if (currentPos == 5) {
            if (inter02_Right_West ) return -1;
        } else if (currentPos == 8) {
            if (inter03_Right_West ) return -1;
        }



        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;



            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                // trafficLights.getFirst().getCircle().getFill() == Color.GREEN
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()   ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){

                if(vehicle_emergency.getCenterInt() > currentPos)
                {
                    if (trafficLight.getCircle().getFill() == Color.GREEN ) {
                        if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                            return 3;
                        if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                            return 2;
                        if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                            return 1;
                    }
                }else{
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive() ) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                if(vehicle_emergency.getCenterInt() > currentPos){
                    if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()  ) {
                        return 8;
                    }
                }
                else
                    return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }








    public static void setLatch(CountDownLatch latch) {
        HelloController.latch = latch;
    }

    private static int getNextPositionEmergency(int currentPos, int cantPositions, int [] positionsTags, boolean emergency_bool, TrafficLight trafficLight) {
        System.out.println("getNextPositionEmergency");

        if (currentPos == 2) {
            if (inter01) return -1;
        } else if (currentPos == 5) {
            if (inter02) return -1;
        } else if (currentPos == 8) {
            if (inter03) return -1;
        }

        if (currentPos == 0) {
            if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                return 2;
            if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                return 1;
            return -1;
        } else if (currentPos == 2) {
            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }

            }else if(isCurrentSecondLessThanFive()){ // Emergencia


                if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                    return 3;
                if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                    return 2;
                if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                    return 1;

            }

            return -1;
        }  else if (currentPos == 5) {


            if(!emergency_bool)
            {
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()) {
                    if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                        return 3;
                    if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                        return 2;
                    if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                        return 1;
                }
            }else if(isCurrentSecondLessThanFive() ){
                if (currentPos + 3 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0 && positionsTags[currentPos + 3] == 0)
                    return 3;
                if (currentPos + 2 < cantPositions && positionsTags[currentPos + 1] == 0 && positionsTags[currentPos + 2] == 0)
                    return 2;
                if (currentPos + 1 < cantPositions && positionsTags[currentPos + 1] == 0)
                    return 1;
            }

            return -1;

        } else if (currentPos == 8) {
            if(!emergency_bool){
                if (trafficLight.getCircle().getFill() == Color.GREEN && isCurrentSecondLessThanFive()) {
                    return 8;
                }
            }
            else if (isCurrentSecondLessThanFive()) {
                return 8;
            }


            return -1;
        } else if (currentPos > 0 && currentPos < cantPositions - 1) {
            if (positionsTags[currentPos + 1] == 0)
                return 1;
        }
        return -1;
    }










}