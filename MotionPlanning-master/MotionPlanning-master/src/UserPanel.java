import com.sun.javafx.geom.Point2D;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPanel extends VBox {

    List<Node> StartAndTargetNode = new ArrayList<>();

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
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInput());
                timeValue = space.addPRM();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRT) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());
                timeValue = space.addRRT();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRTstar) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());
                timeValue = space.addRRTStar();
                textFieldTime.setText(String.valueOf(timeValue));
            }
        });

        Button addTwo = new Button("Case 2");
        addTwo.setOnMouseClicked(event -> {
            if (!handleExceptionCase())
                return;

            setDefaultValueForObstacles();

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
            if (strategy == Strategy.PRM) {
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase2());
                timeValue = space.addPRM();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRT) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2());
                timeValue = space.addRRT();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRTstar) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2());
                timeValue = space.addRRTStar();
                textFieldTime.setText(String.valueOf(timeValue));
            }
        });

        Button addThree = new Button("Case 3");
        addThree.setOnMouseClicked(event -> {
            if (!handleExceptionCase())
                return;

            setDefaultValueForObstacles();

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
            if (strategy == Strategy.PRM) {
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase3());
                timeValue = space.addPRM();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRT) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                timeValue = space.addRRT();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRTstar) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                timeValue = space.addRRTStar();
                textFieldTime.setText(String.valueOf(timeValue));
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
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase4());
                timeValue = space.addPRM();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRT) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase4());
                timeValue = space.addRRT();
                textFieldTime.setText(String.valueOf(timeValue));
            } else if (strategy == Strategy.RRTstar) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase4());
                timeValue = space.addRRTStar();
                textFieldTime.setText(String.valueOf(timeValue));
            }
        });

        textFieldErrorMessage = new TextField();
        textFieldErrorMessage.setMaxWidth(500);
        textFieldErrorMessage.setMaxHeight(100);
        textFieldErrorMessage.setAlignment(Pos.CENTER);
        textFieldErrorMessage.setEditable(false);

        addition.getChildren().addAll(addOne, addTwo, addThree, addFour);
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
        this.getChildren().addAll(addition, clear, strategyBox, obstacleSet, textFieldErrorMessage, labelTime, textFieldTime);

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

        return generatedPoint;
    }

    public List<Node> generateRRTInput() {
        List<Node> generatedPoint = new ArrayList<>();

        //new
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        return generatedPoint;
    }

    public List<Node> generatePrmInputForCase2() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase2() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        return generatedPoint;
    }

    public List<Node> generatePrmInputForCase3() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 696, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase3() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 696, StartAndTargetNode.get(0).point.y + 167)));
        //target
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

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

        return generatedPoint;
    }

    public void setDefaultValueForObstacles() {
        if (obstacleSet.getSelectionModel().isSelected(3)) {
            obstacleSet.getSelectionModel().select(0);
            space.setObstacles(0);
        }
    }

    public boolean handleExceptionCase() {
        boolean ok = true;
        if (obstacleSet.getSelectionModel().getSelectedItem() == null)
        {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }
        else
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
        }
        else
        textFieldErrorMessage.setText(obstacleSet.getSelectionModel().getSelectedItem().toString());

        if (invalidValues.contains(textFieldErrorMessage.getText())) {
            textFieldErrorMessage.setText("NOT ALLOWED");
            ok = false;
        }

        return ok;
    }
}

