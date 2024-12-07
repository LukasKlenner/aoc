package aoc.util;

public record Pos(int x, int y) {

    public Pos(Pos pos) {
        this(pos.x, pos.y);
    }

    public Pos add(Direction direction, int value) {
        return switch (direction) {
            case UP -> add(0, -1);
            case DOWN -> add(0, 1);
            case LEFT -> add(-1, 0);
            case RIGHT -> add(1, 0);
        };
    }

    public Pos add(Direction direction) {
        return add(direction, 1);
    }

    public Pos add(int dx, int dy) {
        return new Pos(x + dx, y + dy);
    }

    public <T> boolean isInBounds(T[][] arr) {
        return x >= 0 && y >= 0 && x < arr.length && y < arr[0].length;
    }

    public boolean isInBounds(int[][] arr) {
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
