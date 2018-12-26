package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int WIDTH = 740;
    public static final int HEIGHT = 620;

    @Override
    public void start(Stage primaryStage) throws Exception{

        var scene = new Scene(new Controller(), WIDTH, HEIGHT);
        scene.setFill(Color.DARKGRAY);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Path Finding Simulator");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
