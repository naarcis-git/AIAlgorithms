import com.sun.javafx.geom.Point2D;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class UserPanel extends VBox {

    List<Node> StartAndTargetNode = new ArrayList<>();

    private ComboBox strategyBox;
    private ComboBox obstacleSet;

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

        HBox addition = new HBox();
        Button addOne = new Button("Case 1");
        addOne.setOnMouseClicked(event -> {

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);

            if (strategy == Strategy.PRM) {
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInput());
                space.addPRM();
            } else if (strategy == Strategy.RRT) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());
                space.addRRT();
            } else if (strategy == Strategy.RRTstar) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInput());
                space.addRRTStar();
            }
        });
//idk if it's needed :
        StartAndTargetNode.add(new Node(null, new Point2D(240, 260)));
        StartAndTargetNode.add(new Node(null, new Point2D(370, 450)));

        Button addTwo = new Button("Case 2a");
        addTwo.setOnMouseClicked(event -> {
//            deleted today 23.04.2024
//            List<Node> generatedPoint = new ArrayList<>();
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
//            // generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
            //deleted today 23.04.2024
//            CoordonateAlgorithms.setGeneratedPoint(CoordonateAlgorithms.generatedPoint);
            if (strategy == Strategy.PRM) {
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase2a());
                space.addPRMforCase2a();
            } else if (strategy == Strategy.RRT) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2a());
                space.addRRTforCase2a();
            } else if (strategy == Strategy.RRTstar) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2a());
                space.addRRTStarforCase2a();
            }
        });

        Button addThree = new Button("Case 2b");
        addThree.setOnMouseClicked(event -> {
            // TODO: 22/04/2024 De modificat puncte !!!
//            List<Node> generatedPoint = new ArrayList<>();
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
//            CoordonateAlgorithms.setGeneratedPoint(generatedPoint);
            if (strategy == Strategy.PRM){
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase2b());
                space.addPRM();
            }
            else if (strategy == Strategy.RRT){
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2b());
                space.addRRT();
            }
            else if (strategy == Strategy.RRTstar){
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase2b());
                space.addRRTStar();
            }
        });

        Button addFour = new Button("Case 3");
        // why case 3 is not running ?

        addFour.setOnMouseClicked(event -> {
            // TODO: 22/04/2024 De modificat puncte !!!
//            List<Node> generatedPoint = new ArrayList<>();
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
//            generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

            CoordonateAlgorithms.setStartAndTargetNode(StartAndTargetNode);
//            CoordonateAlgorithms.setGeneratedPoint(generatedPoint);
            if (strategy == Strategy.PRM) {
                CoordonateAlgorithms.setGeneratedPoint(generatePrmInputForCase3());
                space.addPRM();
            }
            else if (strategy == Strategy.RRT){
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                space.addRRT();
            }
            else if (strategy == Strategy.RRTstar) {
                CoordonateAlgorithms.setGeneratedPoint(generateRRTInputForCase3());
                space.addRRTStar();
            }
        });

        Button connect = new Button("Connect");
        connect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                space.connect();
            }
        });

        addition.getChildren().addAll(addOne, addTwo, addThree, addFour, connect);
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

        obstacleSet = new ComboBox();
        obstacleSet.setPromptText("Obstacle Set");
        obstacleSet.setOnAction(this::updateObstacles);
        obstacleSet.getItems().addAll(
                "Set 1",
                "Set 2",
                "Set 3",
                "No Obstacles"
        );

        HBox slider = new HBox();

        Label label = new Label("RRT Increment:  ");

        Slider multiplierSlider = new Slider();
        multiplierSlider.setMin(0);
        multiplierSlider.setMax(240);
        multiplierSlider.setValue(234);
        multiplierSlider.setShowTickLabels(true);
        multiplierSlider.setShowTickMarks(true);
        multiplierSlider.setMajorTickUnit(2);
        multiplierSlider.setMinorTickCount(1);
        multiplierSlider.setBlockIncrement(10);
        multiplierSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            space.setRRTMultiplier(newValue.intValue());
        });
        multiplierSlider.setPrefWidth(500);
        slider.getChildren().addAll(label, multiplierSlider);

        this.getChildren().addAll(addition, clear, strategyBox, obstacleSet, slider);


        this.setSpacing(5);
        this.setPadding(new Insets(10));
    }

    private void updateObstacles(Event event) {
        if (obstacleSet.getValue().equals("Set 1")) space.setObstacles(0);
        else if (obstacleSet.getValue().equals("Set 2")) space.setObstacles(1);
        else if (obstacleSet.getValue().equals("Set 3")) space.setObstacles(2);
        else if (obstacleSet.getValue().equals("No Obstacles")) space.setObstacles(3);
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
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) -140)));
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) 223)));
        generatedPoint.add(new Node(null, new Point2D((float) 626, (float) 250)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(1).point.x,
                StartAndTargetNode.get(1).point.y)));

        return generatedPoint;
    }

    public List<Node> generatePrmInputForCase2a() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
//        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase2a() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) -140)));
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) 223)));
        generatedPoint.add(new Node(null, new Point2D((float) 626, (float) 250)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(1).point.x,
                StartAndTargetNode.get(1).point.y)));

        return generatedPoint;
    }
    public List<Node> generatePrmInputForCase2b() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
//        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase2b()
    {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) -140)));
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) 223)));
        generatedPoint.add(new Node(null, new Point2D((float) 626, (float) 250)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(1).point.x,
                StartAndTargetNode.get(1).point.y)));

        return generatedPoint;
    }
    public List<Node> generatePrmInputForCase3() {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x, StartAndTargetNode.get(0).point.y)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 176, StartAndTargetNode.get(0).point.y - 154)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 392, StartAndTargetNode.get(0).point.y - 65)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 363, StartAndTargetNode.get(0).point.y + 167)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(0).point.x + 130, StartAndTargetNode.get(0).point.y + 189)));

        return generatedPoint;
    }

    public List<Node> generateRRTInputForCase3()
    {
        List<Node> generatedPoint = new ArrayList<>();
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) -140)));
        generatedPoint.add(new Node(null, new Point2D((float) 700, (float) 223)));
        generatedPoint.add(new Node(null, new Point2D((float) 626, (float) 250)));
        generatedPoint.add(new Node(null, new Point2D(StartAndTargetNode.get(1).point.x,
                StartAndTargetNode.get(1).point.y)));

        return generatedPoint;
    }
}

