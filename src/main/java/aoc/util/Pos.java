package aoc.util;

import java.util.Comparator;

public record Pos(int x, int y) implements Comparable<Pos> {

    public static final Comparator<Pos> COMPARATOR = Comparator.comparingInt(Pos::x).thenComparing(Pos::y);

    public Pos(Pos pos) {
        this(pos.x, pos.y);
    }

    public Pos add(Direction direction, int value) {
        return switch (direction) {
            case UP -> add(0, -value);
            case DOWN -> add(0, value);
            case LEFT -> add(-value, 0);
            case RIGHT -> add(value, 0);
        };
    }

    public Pos add(Direction direction) {
        return add(direction, 1);
    }

    public Pos add(int dx, int dy) {
        return new Pos(x + dx, y + dy);
    }

    public Pos add(Pos pos) {
        return add(pos.x, pos.y);
    }

    public Pos sub(Direction direction, int value) {
        return add(direction, -value);
    }

    public Pos sub(int dx, int dy) {
        return new Pos(x - dx, y - dy);
    }

    public Pos sub(Pos pos) {
        return sub(pos.x, pos.y);
    }

    public Pos up() {
        return add(Direction.UP);
    }

    public Pos down() {
        return add(Direction.DOWN);
    }

    public Pos left() {
        return add(Direction.LEFT);
    }

    public Pos right() {
        return add(Direction.RIGHT);
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

    @Override
    public int compareTo(Pos o) {
        return COMPARATOR.compare(this, o);
    }
}
