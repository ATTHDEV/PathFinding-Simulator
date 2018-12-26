package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayDeque;

public class Bot {

    ArrayDeque<Node> path;

    private float next_x;
    private float next_y;
    private float speed_x = 0;
    private float speed_y = 0;

    float x, y, w, h;
    public int speed;

    public Bot(float x, float y, float w, float h, int speed) {
        initail(x, y, w, h, speed);
    }

    public Bot(float w, float h, int speed) {
        initail(0, 0, w, h, speed);
    }

    private void initail(float x, float y, float w, float h, int speed) {
        this.x = x;
        this.y = y;
        this.next_x = (int) x;
        this.next_y = (int) y;
        this.w = w;
        this.h = h;
        path = new ArrayDeque<>();
        this.speed = speed;
    }

    public void render(GraphicsContext gc, Color color, String name) {
        gc.setFill(color);
        gc.fillRect(x, y, w, h);
    }

    public void setPath(ArrayDeque<Node> path) {
        this.path = path.clone();
    }

    public void run() {
        if (x != next_x) {
            x += speed_x;
        }
        if (y != next_y) {
            y += speed_y;
        }
        var updated = (int) Math.abs(next_x - x) <= 0 && (int) Math.abs(next_y - y) <= 0;

        if (updated && path.size() > 0) {
            var currentPath = path.getFirst();
            next_x = currentPath.x;
            next_y = currentPath.y;
            speed_x =  (next_x - x) / (int) (w / speed);
            speed_y =  (next_y - y) / (int) (h / speed);
            path.removeFirst();

        } else if (updated) {
            speed_x = 0;
            speed_y = 0;
        }
    }
}
