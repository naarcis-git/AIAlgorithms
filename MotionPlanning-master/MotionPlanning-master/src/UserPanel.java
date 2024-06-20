import com.sun.javafx.geom.Point2D;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserPanel extends VBox {

//    int x, y;

    List<Node> StartAndTargetNode = new ArrayList<>();
    Map<String, Double> distanceNodes = new HashMap<>();
    Map<String, Point2D> distanceCoordinateNodes = new HashMap<>();
    List<String> listConnectedPoints = new ArrayList<>();

    List<Node> randomPoints = new ArrayList<>();

    Slider multiplierSlider = new Slider();

    private ComboBox strategyBox;
    private ComboBox pointNrBox;
    private ComboBox obstacleSet;

    private int n;
    private long timeValue;
    private double roadDistanceDecimalResult;

    private TextField textFieldErrorMessage;
    private TextField textFieldTime;
    private Label labelError;
    private Label labelTime;
    private Label totalDistance;
    private Label optimalDistance;
    private TextField totalDistanceTextField;
    private TextField optimalDistanceTextField;

    private enum Strategy {
        PRM,
        RRT,
        RRTstar;
    }

    private Strategy strategy;
    private MotionSpace space;

    public UserPanel(MotionSpace space) {
        super();

        this.space = space;
        setup();
    }

    private void setup() {

        obstacleSet = new ComboBox();
        obstacleSet.setPromptText("Obstacle Set");
        obstacleSet.setOnAction(this::updateObstacles);
        obstacleSet.getItems().addAll(
                "Scene 1",
                "Scene 2",
                "Scene 3",
                "Scene 4",
                "No Obstacles"
        );

        HBox addition = new HBox();
        Button addOne = new Button("Case 1");
        addOne.setOnMouseClicked(event -> {
            pointNrBox.setDisable(true);
            textFieldErrorMessage.setText("");
            multiplierSlider.setDisable(true);
            StringBuilder sum = new StringBuilder();


            StartAndTargetNode.clear();
            StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
            StartAndTargetNode.add(new Node(null, new Point2D(370, 450)));
            if (!handleExceptionCase()) {
                textFieldErrorMessage.setBackground(createSignalForError());
                return;
            }

            setDefaultValueForObstacles(0);

            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInput());
                timeValue = space.addPRM(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();
                multiplierSlider.setDisable(true);

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());

                timeValue = space.addRRT(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));

                optimalDistanceTextField.setText(sum.toString());
                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());
                timeValue = space.addRRTStar(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                drawDistancePoints();
            }
        });

        Button addTwo = new Button("Case 2");
        addTwo.setOnMouseClicked(event -> {
            textFieldErrorMessage.setText("");
            pointNrBox.setDisable(true);
            multiplierSlider.setDisable(true);
            StringBuilder sum = new StringBuilder();

            StartAndTargetNode.clear();
            StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
            StartAndTargetNode.add(new Node(null, new Point2D(370, 450)));
            if (!handleExceptionCase()) {
                textFieldErrorMessage.setBackground(createSignalForError());
                return;
            }

            setDefaultValueForObstacles(0);

            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase2());

                timeValue = space.addPRM(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));

                optimalDistanceTextField.setText(sum.toString());
                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2());
                timeValue = space.addRRT(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2());
                timeValue = space.addRRTStar(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            }
        });

        Button addThree = new Button("Case 3");
        addThree.setOnMouseClicked(event -> {
            textFieldErrorMessage.setText("");
            pointNrBox.setDisable(true);
            multiplierSlider.setDisable(true);
            StringBuilder sum = new StringBuilder();

            StartAndTargetNode.clear();
            StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
            StartAndTargetNode.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 696, StartAndTargetNode.get(0).point.y + 167)));
            if (!handleExceptionCase()) {
                textFieldErrorMessage.setBackground(createSignalForError());
                return;
            }

            setDefaultValueForObstacles(0);

            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase3());
                timeValue = space.addPRM(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));

                optimalDistanceTextField.setText(sum.toString());
                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                timeValue = space.addRRT(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                timeValue = space.addRRTStar(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            }
        });

        Button addFour = new Button("Case 4.");
        addFour.setOnMouseClicked(event -> {
            textFieldErrorMessage.setText("");
            pointNrBox.setDisable(true);
            multiplierSlider.setDisable(true);
            StringBuilder sum = new StringBuilder();

            StartAndTargetNode.clear();
            StartAndTargetNode.add(new Node(null, new Point2D(90, 330)));
            StartAndTargetNode.add(new Node(null, new Point2D(940, 280)));

            if (!handleExceptionCase4()) {
                textFieldErrorMessage.setBackground(createSignalForError());
                return;
            }

            // set the option for obstacles
            setDefaultValueForObstacles(3);

            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase4());
                timeValue = space.addPRM(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase4());
                timeValue = space.addRRT(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase4());
                timeValue = space.addRRTStar(listConnectedPoints, StartAndTargetNode, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            }
        });

        Button randomRRT = new Button("Random");
        randomRRT.setOnMouseClicked(event -> {
            textFieldErrorMessage.setText("");
            StringBuilder sum = new StringBuilder();

            if (strategyBox.getSelectionModel().isEmpty()) {
                textFieldErrorMessage.setText("NOT ALLOWED");
            }

            // setare valoare default daca butnoul de setare numar puncte e dezactivat
            if (pointNrBox.isDisabled() || n == 0) {
                n = 10;
                pointNrBox.getSelectionModel().select(1);
                pointNrBox.getSelectionModel().clearAndSelect(1);
            }

            space.createObstacles();
            if (strategy == Strategy.PRM) {
                timeValue = space.addPRMRandom(listConnectedPoints, randomPoints, n, sum);
                textFieldTime.setText(String.valueOf(timeValue));
                measurement(randomPoints);

                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
                randomPoints.clear();
            }
            if (strategy == Strategy.RRT) {
                timeValue = space.addRRTRandom(listConnectedPoints, randomPoints, n,sum);
                textFieldTime.setText(String.valueOf(timeValue));
                measurement(randomPoints);

                optimalDistanceTextField.setText(sum.toString());

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));

                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                drawDistancePoints();
                randomPoints.clear();
            } else if (strategy == Strategy.RRTstar) {
                timeValue = space.addRRTStarRandom(listConnectedPoints, randomPoints, n,sum);
                textFieldTime.setText(String.valueOf(timeValue));
                optimalDistanceTextField.setText(sum.toString());

                measurement(randomPoints);
                //case expection
                List<String> edgePoints = listConnectedPoints.stream().filter(e -> !distanceNodes.containsKey(e)).collect(Collectors.toList());
                distanceNodes.keySet().removeIf(e -> (!listConnectedPoints.contains(e) && !edgePoints.contains(e)));

                // calculate the total distance
                roadDistanceDecimalResult = roundTo2Decimals(calculateDistanceSum(new ArrayList<>(distanceNodes.values())));
                totalDistanceTextField.setText(String.valueOf(roadDistanceDecimalResult));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e) && !edgePoints.contains(e));

                drawDistancePoints();
                randomPoints.clear();
            }
        });

        HBox slider = new HBox();

        Label label = new Label("RRT Random Increment:  ");

        multiplierSlider.setMin(100);
        multiplierSlider.setMax(200);
        multiplierSlider.setValue(120);
        multiplierSlider.setShowTickLabels(true);
        multiplierSlider.setShowTickMarks(true);
        multiplierSlider.setMajorTickUnit(2);
        multiplierSlider.setMinorTickCount(1);
        multiplierSlider.setBlockIncrement(10);
        multiplierSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            space.setRRTMultiplier(newValue.intValue());
        });
        multiplierSlider.setDisable(true);
        multiplierSlider.setPrefWidth(300);
        slider.getChildren().addAll(label, multiplierSlider);
        //setting placement for button
        slider.setPadding(new Insets(0, 0, 0, 100));

        addition.getChildren().addAll(addOne, addTwo, addThree, addFour, randomRRT, slider);
        addition.setSpacing(5);

        Button enablePointNumber = new Button("Enable random features");
        enablePointNumber.setOnMouseClicked(event -> {
            pointNrBox.setDisable(false);
            multiplierSlider.setDisable(false);
        });

        Button clear = new Button("Clear Space");
        clear.setOnMouseClicked(event -> {
            StartAndTargetNode.clear();
            space.reset();
        });

        strategyBox = new ComboBox();
        strategyBox.setPromptText("Search Strategy");
        strategyBox.setOnAction(this::updateStrategy);
        strategyBox.getItems().addAll(
                "Probabilistic Road Map",
                "Rapidly Expanding Random Tree",
                "Rapidly Expanding Random Tree Star"
        );

        pointNrBox = new ComboBox();
        pointNrBox.setPromptText("Point number");
        pointNrBox.setOnAction(this::updatePointNr);
        pointNrBox.getItems().addAll(
                "5",
                "10",
                "20"
        );
        pointNrBox.setDisable(true);

        // textField for error
        labelError = new Label("Error");
        textFieldErrorMessage = new TextField();
        textFieldErrorMessage.setMaxWidth(900);
        textFieldErrorMessage.setMaxHeight(100);
        textFieldErrorMessage.setAlignment(Pos.CENTER);
        textFieldErrorMessage.setEditable(false);

        // textField for time
        labelTime = new Label("Time");
        textFieldTime = new TextField();
        textFieldTime.setMaxWidth(500);
        textFieldTime.setMaxHeight(100);
        textFieldTime.setAlignment(Pos.CENTER);
        textFieldTime.setEditable(false);

        HBox boxWithTextFields = new HBox(20);
        boxWithTextFields.getChildren().addAll(labelError, textFieldErrorMessage, labelTime, textFieldTime);
        boxWithTextFields.setPadding(new Insets(0, 0, 0, 310));

        HBox clearAndTextFields = new HBox();
        //setting placement for button
        clearAndTextFields.getChildren().addAll(clear, enablePointNumber, boxWithTextFields);

        totalDistance = new Label("Total Distance");
        totalDistanceTextField = new TextField();
        totalDistanceTextField.setMaxWidth(500);
        totalDistanceTextField.setMaxHeight(100);
        totalDistanceTextField.setAlignment(Pos.CENTER);
        totalDistanceTextField.setEditable(false);

        optimalDistance = new Label("Optimal Distance");
        optimalDistanceTextField = new TextField();
        optimalDistanceTextField.setMaxWidth(500);
        optimalDistanceTextField.setMaxHeight(100);
        optimalDistanceTextField.setAlignment(Pos.CENTER);
        optimalDistanceTextField.setEditable(false);

        HBox distanceBox = new HBox(5);
        distanceBox.getChildren().addAll(totalDistance, totalDistanceTextField, optimalDistance, optimalDistanceTextField);
        distanceBox.setPadding(new Insets(0, 0, 0, 55));

        HBox comboBoxList = new HBox();
        comboBoxList.getChildren().addAll(strategyBox, obstacleSet, pointNrBox, distanceBox);

        this.getChildren().addAll(addition, clearAndTextFields, comboBoxList);

        this.setSpacing(5);
        this.setPadding(new Insets(10));
    }

    private void updateObstacles(Event event) {
        if (obstacleSet.getValue().equals("Scene 1")) space.setObstacles(0);
        else if (obstacleSet.getValue().equals("Scene 2")) space.setObstacles(1);
        else if (obstacleSet.getValue().equals("Scene 3")) space.setObstacles(2);
        else if (obstacleSet.getValue().equals("Scene 4")) space.setObstacles(3);
        else if (obstacleSet.getValue().equals("No Obstacles")) space.setObstacles(4);
    }


    private void updateStrategy(Event event) {
        if (strategyBox.getValue().equals("Probabilistic Road Map")) strategy = Strategy.PRM;
        else if (strategyBox.getValue().equals("Rapidly Expanding Random Tree")) strategy = Strategy.RRT;
        else if (strategyBox.getValue().equals("Rapidly Expanding Random Tree Star")) strategy = Strategy.RRTstar;
    }

    private void updatePointNr(Event event) {
        if (pointNrBox.getValue().equals("5")) n = 5;
        else if (pointNrBox.getValue().equals("10")) n = 10;
        else if (pointNrBox.getValue().equals("20")) n = 20;
    }

    public static class CoordonateAlgorithms {
        public static List<Node> generatedPoint;
        public static List<Node> StartAndTargetNode;

        public static List<Node> getGeneratedPoint() {
            return generatedPoint;
        }

        public static void setGeneratedPoint(List<Node> generated) {
            generatedPoint = generated;
        }
    }

    public List<Node> generatePrmInput() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));
        measurement(generatedPoint);

        return generatedPoint;
    }

    public List<Node> generateRRTInput() {
        List<Node> generatedPoint = new ArrayList<>();

        //new
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        measurement(
                Stream.of(
                                Arrays.asList(new Node(
                                        new Point2D(
                                                StartAndTargetNode.get(0).point.x,
                                                StartAndTargetNode.get(0).point.y))),
                                generatedPoint)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        return generatedPoint;
    }

    public List<Node> generatePrmInputForCase2() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));
        measurement(generatedPoint);

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase2() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        measurement(
                Stream.of(
                                Arrays.asList(new Node(
                                        new Point2D(
                                                StartAndTargetNode.get(0).point.x,
                                                StartAndTargetNode.get(0).point.y))),
                                generatedPoint)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        return generatedPoint;
    }

    public List<Node> generatePrmInputForCase3() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        //new d
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 500, StartAndTargetNode.get(0).point.y + 189)));
//puntul new target
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 696, StartAndTargetNode.get(0).point.y + 167)));
        measurement(generatedPoint);

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase3() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        //new d
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 500, StartAndTargetNode.get(0).point.y + 189)));
//punctul new target
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 696, StartAndTargetNode.get(0).point.y + 167)));

        measurement(
                Stream.of(
                                Arrays.asList(new Node(
                                        new Point2D(
                                                StartAndTargetNode.get(0).point.x,
                                                StartAndTargetNode.get(0).point.y))),
                                generatedPoint)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        return generatedPoint;
    }

    public List<Node> generatePrmInputForCase4() {
        List<Node> generatedPoint = new ArrayList<>();
        //start
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(365, 100)));
        generatedPoint.add(new Node(null, new Point2D(660, 65)));

        generatedPoint.add(new Node(null, new Point2D(97, 450)));
        generatedPoint.add(new Node(null, new Point2D(150, 515)));
        generatedPoint.add(new Node(null, new Point2D(270, 525)));
        generatedPoint.add(new Node(null, new Point2D(256, 420)));
        generatedPoint.add(new Node(null, new Point2D(320, 308)));
        generatedPoint.add(new Node(null, new Point2D(475, 300)));
        generatedPoint.add(new Node(null, new Point2D(405, 330)));
        generatedPoint.add(new Node(null, new Point2D(435, 380)));
        generatedPoint.add(new Node(null, new Point2D(196, 416)));
        generatedPoint.add(new Node(null, new Point2D(370, 470)));
        generatedPoint.add(new Node(null, new Point2D(457, 470)));
        generatedPoint.add(new Node(null, new Point2D(463, 527)));
        generatedPoint.add(new Node(null, new Point2D(670, 530)));
        generatedPoint.add(new Node(null, new Point2D(564, 544)));
        generatedPoint.add(new Node(null, new Point2D(670, 465)));
        generatedPoint.add(new Node(null, new Point2D(670, 265)));

        generatedPoint.add(new Node(null, new Point2D(940, 280)));

        measurement(generatedPoint);

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase4() {
        List<Node> generatedPoint = new ArrayList<>();
        //start
//        de sus
        generatedPoint.add(new Node(null, new Point2D(365, 100)));
//        generatedPoint.add(new Node(null, new Point2D(350, 140)));
        generatedPoint.add(new Node(null, new Point2D(660, 65)));

        //de jos
//        generatedPoint.add(new Node(null, new Point2D(75, 450)));
        generatedPoint.add(new Node(null, new Point2D(97, 450)));
        generatedPoint.add(new Node(null, new Point2D(150, 515)));
        //pct d:
        generatedPoint.add(new Node(null, new Point2D(270, 525)));
        generatedPoint.add(new Node(null, new Point2D(256, 420)));
        generatedPoint.add(new Node(null, new Point2D(320, 308)));
        generatedPoint.add(new Node(null, new Point2D(475, 300)));
        generatedPoint.add(new Node(null, new Point2D(405, 330)));
        generatedPoint.add(new Node(null, new Point2D(435, 380)));
        generatedPoint.add(new Node(null, new Point2D(196, 416)));
        generatedPoint.add(new Node(null, new Point2D(370, 470)));

        generatedPoint.add(new Node(null, new Point2D(457, 470)));
        generatedPoint.add(new Node(null, new Point2D(463, 527)));
        generatedPoint.add(new Node(null, new Point2D(670, 530)));
        generatedPoint.add(new Node(null, new Point2D(564, 544)));
        generatedPoint.add(new Node(null, new Point2D(670, 465)));
        //punctul d
        generatedPoint.add(new Node(null, new Point2D(670, 265)));
//target
        generatedPoint.add(new Node(null, new Point2D(940, 280)));

        measurement(
                Stream.of(
                                Arrays.asList(new Node(
                                        new Point2D(
                                                StartAndTargetNode.get(0).point.x,
                                                StartAndTargetNode.get(0).point.y))),
                                generatedPoint)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        return generatedPoint;
    }

    private void measurement(List<Node> listNodes) {
        String key;
        for (int i = 0; i < listNodes.size(); i++) {
            for (int j = i + 1; j < listNodes.size(); j++) {
                if (i != j) {
                    key = "" + (char) ((int) 'A' + i) + (char) ((int) 'A' + j);
                    distanceNodes.put(key, calculateDistance(listNodes.get(i), listNodes.get(j)));
                    distanceCoordinateNodes.put(key, new Point2D((listNodes.get(i).point.x + listNodes.get(j).point.x) / 2, (listNodes.get(i).point.y + listNodes.get(j).point.y) / 2));
                }
            }
        }
    }

    private double calculateDistance(Node node1, Node node2) {
        return Math.sqrt((node1.point.x - node2.point.x) * (node1.point.x - node2.point.x) + (node1.point.y - node2.point.y) * (node1.point.y - node2.point.y));
    }

    private void drawDistancePoints() {
        for (String key : distanceCoordinateNodes.keySet()) {
            space.setDistanceNodes(distanceCoordinateNodes.get(key).x, distanceCoordinateNodes.get(key).y, key, distanceNodes.get(key));
        }

        distanceNodes.clear();
        distanceCoordinateNodes.clear();
        listConnectedPoints.clear();
    }

    public void setDefaultValueForObstacles(int defaultIndexValue) {
        if (!obstacleSet.getSelectionModel().isSelected(defaultIndexValue)) {
            obstacleSet.getSelectionModel().select(defaultIndexValue);
            space.setObstacles(defaultIndexValue);
        }
    }

    public boolean handleExceptionCase() {
        boolean ok = true;
        if (obstacleSet.getSelectionModel().getSelectedItem() == null) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        if ((textFieldErrorMessage.getText().equals("Scene 4")) || (textFieldErrorMessage.getText().equals("Scene 3")) || (textFieldErrorMessage.getText().equals("Scene 2"))) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        if (strategyBox.getSelectionModel().isEmpty()) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        return ok;
    }


    public boolean handleExceptionCase4() {
        boolean ok = true;
        List<String> invalidValues = Arrays.asList("Scene 1", "Scene 2", "Scene 3");
        if (obstacleSet.getSelectionModel().getSelectedItem() == null) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        if (invalidValues.contains(textFieldErrorMessage.getText())) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        if (strategyBox.getSelectionModel().isEmpty()) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        return ok;
    }

    private Background createSignalForError() {
        List<Color> colorList = Arrays.asList(Color.BURLYWOOD, Color.CORAL, Color.DIMGRAY);
        BackgroundFill background_fill = new BackgroundFill(colorList.get(new Random().nextInt(colorList.size())),
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        return background;
    }

    private double calculateDistanceSum(List<Double> distanceList) {
        return distanceList.stream().reduce(0.0, (a, b) -> a + b);
    }

    private double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

