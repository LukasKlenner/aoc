package aoc.util;

public enum Direction {
    RIGHT, DOWN, LEFT, UP;

    public static Direction of(String str) {
        return switch (str) {
            case "U" -> UP;
            case "D" -> DOWN;
            case "R" -> RIGHT;
            case "L" -> LEFT;
            default -> throw new IllegalStateException("Unexpected value: " + str);
        };
    }

    public Direction rotate90() {
        return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }

    public Direction rotate180() {
        return switch (this) {
            case UP -> DOWN;
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case LEFT -> RIGHT;
        };
    }

    public Direction rotate270() {
        return switch (this) {
            case UP -> LEFT;
            case RIGHT -> UP;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
        };
    }
}
