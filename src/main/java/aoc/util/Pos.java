package aoc.util;

public class Pos {

    public int x;
    public int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Direction direction, int value) {
        switch (direction) {
            case UP: y -= value; break;
            case DOWN: y += value; break;
            case LEFT: x -= value; break;
            case RIGHT: x += value; break;
        }
    }

    public void add(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public <T> boolean isInbounds(T[][] arr) {
        return x >= 0 && y >= 0 && x < arr.length && y < arr[0].length;
    }

    public boolean isInbounds(int[][] arr) {
        return x >= 0 && y >= 0 && x < arr.length && y < arr[0].length;
    }

    public <T> T getValue(T[][] arr) {
        return arr[y][x];
    }

    public int getValue(int[][] arr) {
        return arr[y][x];
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(x, y);
    }
}
