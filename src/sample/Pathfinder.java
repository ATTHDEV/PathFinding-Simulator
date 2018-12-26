package sample;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Pathfinder {

    enum MODE {
        OPEN_DIAGONAL, CLOSE_DIAGONAL
    }

    Node[][] grid;

    MODE mode = MODE.OPEN_DIAGONAL;

    ArrayList<Node> openSet;
    ArrayList<Node> closeSet;
    ArrayDeque<Node> path;

    Node start, current, end;

    private boolean isFindPath = false;

    public Pathfinder(Node[][] grid) {
        initial(grid, mode);
    }

    public Pathfinder(Node[][] grid, MODE mode) {
        initial(grid, mode);
    }

    public void initial(Node[][] grid, MODE mode) {
        this.grid = grid;
        this.mode = mode;
        var cols = grid.length;
        var rows = grid[0].length;
        for (var i = 0; i < cols; i++) {
            for (var j = 0; j < rows; j++) {
                grid[i][j].addNeighbors(grid, mode);
            }
        }
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public ArrayList<Node> getOpenSet() {
        return openSet;
    }

    public ArrayList<Node> getCloseSet() {
        return closeSet;
    }

    public ArrayDeque<Node> getPath() {
        return path;
    }

    public boolean isFindPath() {
        return isFindPath;
    }

    public float heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
    /*
    *  i use A* algorithm for solve path
    * */
    public boolean calculatePath(Node start, Node end) {
        openSet = new ArrayList<>();
        closeSet = new ArrayList<>();
        path = new ArrayDeque<>();
        this.start = start;
        this.end = end;
        openSet.add(this.start);
        this.start.setPassing(true);
        this.end.setPassing(true);
        while (openSet.size() > 0) {
            var lowf_index = 0;
            for (var i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).f < openSet.get(lowf_index).f) {
                    lowf_index = i;
                }
                if (openSet.get(i).f == openSet.get(lowf_index).f) {
                    if (openSet.get(i).g > openSet.get(lowf_index).g) {
                        lowf_index = i;
                    }
                }
            }
            current = openSet.get(lowf_index);
            if (current.equals(end)) {
                var temp = current;
                path.clear();
                path.push(temp);
                while (temp.previous != null) {
                    path.push(temp.previous);
                    temp = temp.previous;
                }
                return isFindPath = true;
            }
            openSet.remove(current);
            closeSet.add(current);
            var neighbors = current.neighbors;
            for (var neighbor : neighbors) {
                if (!closeSet.contains(neighbor) && neighbor.isPassing()) {
                    var tempG = current.g + heuristic(neighbor, current);
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else if (tempG >= neighbor.g) {
                        continue;
                    }
                    neighbor.g = tempG;
                    neighbor.h = heuristic(neighbor, end);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.previous = current;
                }
            }
        }
        return isFindPath = false;
    }
}