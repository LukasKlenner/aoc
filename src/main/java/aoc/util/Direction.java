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
}
