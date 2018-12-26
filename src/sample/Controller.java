package sample;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class Controller extends Group {

    public final int CANVAS_WIDTH = 600;
    public final int CANVAS_HEIGHT = 600;

    GraphicsContext gc;

    int cols , rows;
    Node[][] grid;
    Pathfinder pathfinder, pathfinder2;
    Bot bot, bot2;
    Node maker;
    Label label_alert1, label_alert2;

    public Controller() {

        var label1 = new Label("ขนาดตาราง");
        label1.setFont(new Font(14));
        label1.setTextFill(Color.WHITE);
        label1.setMaxWidth(100);
        label1.setLayoutX(630);
        label1.setLayoutY(100);
        getChildren().add(label1);

        var textField = new TextField("20");
        textField.setMaxWidth(100);
        textField.setLayoutX(625);
        textField.setLayoutY(125);
        getChildren().add(textField);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        var label2 = new Label("อุปสรรค");
        label2.setFont(new Font(14));
        label2.setTextFill(Color.WHITE);
        label2.setMaxWidth(100);
        label2.setLayoutX(630);
        label2.setLayoutY(155);
        getChildren().add(label2);

        var textField2 = new TextField("0.2");
        textField2.setMaxWidth(100);
        textField2.setLayoutX(625);
        textField2.setLayoutY(175);
        getChildren().add(textField2);
        textField2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?")) {
                textField2.setText(oldValue);
            }
        });

        var button = new Button("OK");
        button.setMinWidth(100);
        button.setLayoutX(625);
        button.setLayoutY(215);
        getChildren().add(button);
        button.setOnAction(event -> {
            var gridSize = textField.getText();
            if (gridSize.equals("")) gridSize = "20";
            var passingPoint = textField2.getText();
            if (passingPoint.equals("")) passingPoint = "0.2";
            initialAnimation(Integer.parseInt(gridSize), Float.parseFloat(passingPoint));
        });

        label_alert1 = new Label("Red ไม่พบทาง");
        label_alert1.setFont(new Font(14));
        label_alert1.setTextFill(Color.DARKRED);
        label_alert1.setMaxWidth(100);
        label_alert1.setLayoutX(630);
        label_alert1.setLayoutY(250);
        getChildren().add(label_alert1);

        label_alert2 = new Label("Blue ไม่พบทาง");
        label_alert2.setFont(new Font(14));
        label_alert2.setTextFill(Color.DARKRED);
        label_alert2.setMaxWidth(100);
        label_alert2.setLayoutX(630);
        label_alert2.setLayoutY(275);
        getChildren().add(label_alert2);

        initialAnimation(20, 0.2f);

        var canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setLayoutX(10);
        canvas.setLayoutY(10);
        getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        var animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                render();
            }
        };
        animationTimer.start();
    }

    private int randRange(int min, int max) {
        return (int) (min + (Math.random() * (max - min)));
    }

    private void initialAnimation(int gridSize, float passingPoint) {
        label_alert1.setVisible(false);
        label_alert2.setVisible(false);
        cols = gridSize;
        rows = gridSize;
        grid = new Node[cols][rows];
        var w = (float) (CANVAS_WIDTH / cols);
        var h = (float) (CANVAS_HEIGHT / rows);

        for (var i = 0; i < cols; i++) {
            grid[i] = new Node[rows];
        }

        for (var i = 0; i < cols; i++) {
            for (var j = 0; j < rows; j++) {
                grid[i][j] = new Node(i, j, w, h);
                grid[i][j].randomPassing(passingPoint);
            }
        }
        var ti = randRange(0, cols - 1);
        var tj = randRange(0, rows - 1);

        maker = new Node(ti, tj, w, h);
        pathfinder = new Pathfinder(grid, Pathfinder.MODE.CLOSE_DIAGONAL);
        pathfinder.calculatePath(grid[0][0], grid[ti][tj]);
        if (!pathfinder.isFindPath()) {
            label_alert1.setVisible(true);
        }
        bot = new Bot(0, 0, w, h, 3);
        bot.setPath(pathfinder.getPath());

        pathfinder2 = new Pathfinder(grid, Pathfinder.MODE.CLOSE_DIAGONAL);
        pathfinder2.calculatePath(grid[0][rows - 1], grid[ti][tj]);
        if (!pathfinder2.isFindPath()) {
            label_alert2.setVisible(true);
        }
        bot2 = new Bot(grid[0][rows - 1].x, grid[0][rows - 1].y, w, h, 3);
        bot2.setPath(pathfinder2.getPath());
    }

    public void render() {
        for (var i = 0; i < cols; i++) {
            for (var j = 0; j < rows; j++) {
                if (!grid[i][j].isPassing()) {
                    grid[i][j].show(gc, Color.BLACK);
                } else
                    grid[i][j].show(gc, Color.WHITE);
            }
        }

        for (var p : pathfinder.getPath()) {
            p.show(gc, Color.ORANGERED);
        }
        for (var p : pathfinder2.getPath()) {
            p.show(gc, Color.BLUE);
        }
        maker.show(gc, Color.YELLOW);
        bot.run();
        bot.render(gc, Color.CYAN, "B1");
        bot2.run();
        bot2.render(gc, Color.CYAN, "B2");
    }
}