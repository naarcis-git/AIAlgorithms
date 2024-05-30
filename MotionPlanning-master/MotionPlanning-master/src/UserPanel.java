import com.sun.javafx.geom.Point2D;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserPanel extends VBox {

    int x, y;

    List<Node> StartAndTargetNode = new ArrayList<>();
    Map<String, Integer> distanceNodes = new HashMap<>();
    Map<String, Point2D> distanceCoordinateNodes = new HashMap<>();
    List<String> listConnectedPoints = new ArrayList<>();

    List<Node> randomPoints = new ArrayList<>();

    private ComboBox strategyBox;
    private ComboBox obstacleSet;

    private long timeValue;

    private TextField textFieldErrorMessage;
    private TextField textFieldTime;

    private Label labelTime;

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

        StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
        StartAndTargetNode.add(new Node(null, new Point2D(370, 450)));

        obstacleSet = new ComboBox();
        obstacleSet.setPromptText("Obstacle Set");
        obstacleSet.setOnAction(this::updateObstacles);
        obstacleSet.getItems().addAll(
                "Set 1",
                "Set 2",
                "Set 3",
                "Set 4",
                "No Obstacles"
        );

        HBox addition = new HBox();
        Button addOne = new Button("Case 1");
        addOne.setOnMouseClicked(event -> {
            if (!handleExceptionCase())
                return;
            setDefaultValueForObstacles();
            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);

            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInput());
                timeValue = space.addPRM(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));

                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });
                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());
                timeValue = space.addRRT(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));
                textFieldTime.setText(String.valueOf(timeValue));

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());
                timeValue = space.addRRTStar(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));
                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                drawDistancePoints();
            }
        });

        Button addTwo = new Button("Case 2");
        addTwo.setOnMouseClicked(event -> {
            if (!handleExceptionCase())
                return;

            setDefaultValueForObstacles();

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase2());
                timeValue = space.addPRM(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));
                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });
                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2());
                timeValue = space.addRRT(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2());
                timeValue = space.addRRTStar(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            }
        });

        Button addThree = new Button("Case 3");
        addThree.setOnMouseClicked(event -> {
            if (!handleExceptionCase())
                return;

            setDefaultValueForObstacles();

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase3());
                timeValue = space.addPRM(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));

                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });
                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                timeValue = space.addRRT(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));
                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                timeValue = space.addRRTStar(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));
                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));


                drawDistancePoints();
            }
        });

        Button addFour = new Button("Case 4");
        addFour.setOnMouseClicked(event -> {

            if (!handleExceptionCase4())
                return;

            // set the option for obstacles
            obstacleSet.getSelectionModel().select(3);
            // action
            space.setObstacles(3);


            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
            if (strategy == Strategy.PRM) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase4());
                timeValue = space.addPRM(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));
                listConnectedPoints.forEach(e -> {
                    if (distanceNodes.keySet().contains(e)) distanceNodes.remove(e);
                });
                listConnectedPoints.forEach(e -> {
                    if (distanceCoordinateNodes.keySet().contains(e)) distanceCoordinateNodes.remove(e);
                });

                drawDistancePoints();
            } else if (strategy == Strategy.RRT) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase4());
                timeValue = space.addRRT(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));

                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            } else if (strategy == Strategy.RRTstar) {
                space.createObstacles();

                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase4());
                timeValue = space.addRRTStar(listConnectedPoints);
                textFieldTime.setText(String.valueOf(timeValue));
                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
            }
        });

        Button randomRRT = new Button("Random");
        randomRRT.setOnMouseClicked(event -> {
            setDefaultValueForObstacles();
            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
            space.createObstacles();
            if (strategy == Strategy.PRM) {
                space.addPRMRandom(listConnectedPoints, randomPoints);
                //timeValue = space.addPRMRandom();
                //textFieldTime.setText(String.valueOf(timeValue));
                measurement(randomPoints);
                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();

                randomPoints.clear();
            }
            if (strategy == Strategy.RRT) {
                space.addRRTRandom(listConnectedPoints, randomPoints);

                measurement(randomPoints);
                distanceNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));
                distanceCoordinateNodes.keySet().removeIf(e -> !listConnectedPoints.contains(e));

                drawDistancePoints();
                // setare initiala//letter pentru noduri
                for (int i = 0; i < randomPoints.size(); i++) {
                    space.setMarkers(randomPoints.get(i).point.x, randomPoints.get(i).point.y, i);
                }
                randomPoints.clear();
            } else if (strategy == Strategy.RRTstar) {
                space.addRRTStarRandom();
                drawDistancePoints();
            }
        });

        textFieldErrorMessage = new TextField();
        textFieldErrorMessage.setMaxWidth(500);
        textFieldErrorMessage.setMaxHeight(100);
        textFieldErrorMessage.setAlignment(Pos.CENTER);
        textFieldErrorMessage.setEditable(false);

        addition.getChildren().addAll(addOne, addTwo, addThree, addFour, randomRRT);
        addition.setSpacing(5);

        Button clear = new Button("Clear Space");
        clear.setOnMouseClicked(event -> {
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

        labelTime = new Label("Time");

        textFieldTime = new TextField();
        textFieldTime.setMaxWidth(500);
        textFieldTime.setMaxHeight(100);
        textFieldTime.setAlignment(Pos.CENTER);
        textFieldTime.setEditable(false);

        HBox slider = new HBox();

        Label label = new Label("RRT Random Increment:  ");

        Slider multiplierSlider = new Slider();
        multiplierSlider.setMin(0);
        multiplierSlider.setMax(100);
        multiplierSlider.setValue(10);
        multiplierSlider.setShowTickLabels(true);
        multiplierSlider.setShowTickMarks(true);
        multiplierSlider.setMajorTickUnit(2);
        multiplierSlider.setMinorTickCount(1);
        multiplierSlider.setBlockIncrement(10);
        multiplierSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            space.setRRTMultiplier(newValue.intValue());
        });
        multiplierSlider.setPrefWidth(300);
        slider.getChildren().addAll(label, multiplierSlider);

        this.getChildren().addAll(addition, clear, strategyBox, obstacleSet, textFieldErrorMessage, labelTime, textFieldTime, slider);

        this.setSpacing(5);
        this.setPadding(new Insets(10));
    }

    private void updateObstacles(Event event) {
        if (obstacleSet.getValue().equals("Set 1")) space.setObstacles(0);
        else if (obstacleSet.getValue().equals("Set 2")) space.setObstacles(1);
        else if (obstacleSet.getValue().equals("Set 3")) space.setObstacles(2);
        else if (obstacleSet.getValue().equals("Set 4")) space.setObstacles(3);
        else if (obstacleSet.getValue().equals("No Obstacles")) space.setObstacles(4);
    }


    private void updateStrategy(Event event) {
        if (strategyBox.getValue().equals("Probabilistic Road Map")) strategy = Strategy.PRM;
        else if (strategyBox.getValue().equals("Rapidly Expanding Random Tree")) strategy = Strategy.RRT;
        else if (strategyBox.getValue().equals("Rapidly Expanding Random Tree Star")) strategy = Strategy.RRTstar;
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

        public static List<Node> getStartAndTargetNode() {
            return StartAndTargetNode;
        }

        public static void setStartAndTargetNode(List<Node> startAndTargetNode) {
            StartAndTargetNode = startAndTargetNode;
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
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 696, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));
        measurement(generatedPoint);

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase3() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 696, StartAndTargetNode.get(0).point.y + 167)));
        //target
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

    public List<Node> generatePrmInputForCase4() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 350, StartAndTargetNode.get(0).point.y - 154)));
        // varf
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 500, StartAndTargetNode.get(0).point.y - 200)));
        // new points
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 650, StartAndTargetNode.get(0).point.y - 100)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 730, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 700, StartAndTargetNode.get(0).point.y + 70)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 530, StartAndTargetNode.get(0).point.y)));
        //target
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));
        measurement(generatedPoint);

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase4() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 350, StartAndTargetNode.get(0).point.y - 154)));
        // varf
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 500, StartAndTargetNode.get(0).point.y - 200)));
        // new points
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 650, StartAndTargetNode.get(0).point.y - 100)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 730, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 700, StartAndTargetNode.get(0).point.y + 70)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 530, StartAndTargetNode.get(0).point.y)));
        //target
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

    private void measurement(List<Node> listNodes) {
        String key;
        for (int i = 0; i < listNodes.size(); i++) {
            for (int j = i + 1; j < listNodes.size(); j++) {
                if (i != j) {
                    key = "" + (char) ((int) 'A' + i) + (char) ((int) 'A' + j);
                    distanceNodes.put(key, (int) calculateDistance(listNodes.get(i), listNodes.get(j)));
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

    public void setDefaultValueForObstacles() {
        if (obstacleSet.getSelectionModel().isSelected(3)) {
            obstacleSet.getSelectionModel().select(0);
            space.setObstacles(0);
        }
    }

    public boolean handleExceptionCase() {
        boolean ok = true;
        if (obstacleSet.getSelectionModel().getSelectedItem() == null) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        } else
            textFieldErrorMessage.setText(obstacleSet.getSelectionModel().getSelectedItem().toString());

        if (textFieldErrorMessage.getText().equals("Set 4")) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        return ok;
    }


    public boolean handleExceptionCase4() {
        boolean ok = true;
        List<String> invalidValues = Arrays.asList("Set 1", "Set 2", "Set 3");
        if (obstacleSet.getSelectionModel().getSelectedItem() == null) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        } else
            textFieldErrorMessage.setText(obstacleSet.getSelectionModel().getSelectedItem().toString());

        if (invalidValues.contains(textFieldErrorMessage.getText())) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        return ok;
    }

    public void paint(GraphicsContext g) {
        g.setFont(new Font("Monospaced", 20));
        g.setFill(Color.RED);
        g.fillOval(x, y, 10, 10);
//        g.(x + "," + y, x + 10, y - 10);
//        g.drawString(str, x + 10, y + 20);
//        showStatus(str + " at " + x + "," + y);
    }

}

