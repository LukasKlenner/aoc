package aoc.util;

import java.util.Objects;

public class Pos {

    public int x;
    public int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos(Pos pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Pos add(Direction direction, int value) {
        switch (direction) {
            case UP: y -= value; break;
            case DOWN: y += value; break;
            case LEFT: x -= value; break;
            case RIGHT: x += value; break;
        }

        return this;
    }

    public Pos add(Direction direction) {
        add(direction, 1);
        return this;
    }

    public Pos add(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
