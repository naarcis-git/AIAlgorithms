
import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        int size = 800;
        int bSize = 10;
        VBox root = new VBox();

        VBox border = new VBox();
        border.setStyle("-fx-background-color: #000000;");
        border.setPadding(new Insets(bSize));
        MotionSpace space = new MotionSpace(size);
        border.getChildren().add(space);

        UserPanel panel = new UserPanel(space);
        root.getChildren().addAll(panel, border);

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1050 + bSize * 2,   size -110));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);}
}
