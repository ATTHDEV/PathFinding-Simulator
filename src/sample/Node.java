package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Node {

    public int i;
    public int j;
    public float x;
    public float y;
    public float width;
    public float height;

    public float f = 0;
    public float g = 0;
    public float h = 0;

    public boolean passing = true;

    ArrayList<Node> neighbors = new ArrayList<>();
    Node previous = null;

    public Node(int i, int j, float w, float h) {
        this.i = i;
        this.j = j;
        this.x = i * w;
        this.y = j * h;
        this.width = w - 0.75f;
        this.height = h - 0.75f;
    }

    public boolean isPassing() {
        return passing;
    }

    public void setPassing(boolean passing) {
        this.passing = passing;
    }

    public void randomPassing(float length) {
        if (Math.random() < length) {
            passing = false;
        }
    }

    public void addNeighbors(Node[][] grid, Pathfinder.MODE mode) {
        var i = this.i;
        var j = this.j;
        if (i < grid.length - 1) {
            neighbors.add(grid[i + 1][j]);
        }
        if (i > 0) {
            neighbors.add(grid[i - 1][j]);
        }
        if (j < grid.length - 1) {
            neighbors.add(grid[i][j + 1]);
        }
        if (j > 0) {
            neighbors.add(grid[i][j - 1]);
        }
        if (mode.equals(Pathfinder.MODE.OPEN_DIAGONAL)) {
            if (i > 0 && j > 0) {
                neighbors.add(grid[i - 1][j - 1]);
            }
            if (i < grid.length - 1 && j > 0) {
                neighbors.add(grid[i + 1][j - 1]);
            }
            if (i < grid.length - 1 && j < grid.length - 1) {
                neighbors.add(grid[i + 1][j + 1]);
            }
            if (i > 0 && j < grid.length - 1) {
                neighbors.add(grid[i - 1][j + 1]);
            }
        }
    }

    public void show(GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", x, y);
    }

}
