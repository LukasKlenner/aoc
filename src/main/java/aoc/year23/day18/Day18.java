package aoc.year23.day18;

import java.util.List;
import java.util.stream.Stream;

public class Day18 implements aoc.Day {
    @Override
    public Object part1(Stream<String> linesStream) {
        return run(linesStream.toList(), true);
    }

    @Override
    public Object part2(Stream<String> linesStream) {
        return run(linesStream.toList(), false);
    }

    private Object run(List<String> lines, boolean part1) {

        Pos[] positions = new Pos[lines.size() + 1];
        long count = 0;

        Pos currentPos = new Pos(0, 0);
        positions[0] = currentPos;

        long minX = 0;
        long minY = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(" ");

            Direction direction;
            long value;

            if (part1) {
                direction = Direction.of(split[0]);
                value = Integer.parseInt(split[1]);
            } else {
                direction = Direction.values()[Character.getNumericValue(split[2].charAt(7))];
                String str = split[2].substring(2, 7);

                value = 0;
                long mul = 1;
                for (int j = str.length() - 1; j >= 0; j--) {

                    if (Character.isDigit(str.charAt(j))) {
                        value += mul * Character.getNumericValue(str.charAt(j));
                    } else {
                        value += mul * (str.charAt(j) - 'a' + 10);
                    }

                    mul *= 16;
                }
            }

            currentPos = currentPos.add(direction, value);

            count += value;

            positions[i + 1] = currentPos;
            minX = Math.min(minX, currentPos.x);
            minY = Math.min(minY, currentPos.y);
        }

        minY = Math.abs(minY);
        minX = Math.abs(minX);

        long maxY = 0;
        long maxX = 0;

        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Pos(positions[i].x + minX + 2, positions[i].y + minY + 2);
            maxX = Math.max(maxX, positions[i].x);
            maxY = Math.max(maxY, positions[i].y);
        }


        long area = 0;

        for (int i = 1; i < lines.size(); i++) {
            area += ((positions[i].x) + (positions[i + 1].x)) * ((positions[i].y) - (positions[i + 1].y));
        }

        area /= 2;

        return (area + 1) - (count / 2.0) + count;
    }

    private enum Direction {
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
    }

    private record Pos(long x, long y) {

        public Pos add(Direction direction, long value) {
            return switch (direction) {
                case UP -> new Pos(x, y + value);
                case DOWN -> new Pos(x, y - value);
                case LEFT -> new Pos(x - value, y);
                case RIGHT -> new Pos(x + value, y);
            };
        }
    }
}
