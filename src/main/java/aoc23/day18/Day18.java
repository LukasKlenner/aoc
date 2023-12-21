package aoc23.day18;

import aoc23.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day18 implements Day {
    @Override
    public Object part1(Stream<String> linesStream) {

        List<String> lines = linesStream.toList();

        Pos[] positions = new Pos[lines.size() + 1];

        Pos currentPos = new Pos(0, 0);
        positions[0] = currentPos;

        long minX = 0;
        long minY = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(" ");

            Direction direction = Direction.of(split[0]);
            int value = Integer.parseInt(split[1]);

            currentPos = currentPos.add(direction, value);

            positions[i + 1] = currentPos;
            minX = Math.min(minX, currentPos.x);
            minY = Math.min(minY, currentPos.y);
        }

        minY = Math.abs(minY);
        minX = Math.abs(minX);

        //positions = new Pos[]{new Pos(1, 6), new Pos(3, 1), new Pos(7, 2), new Pos(4, 4), new Pos(8, 5), new Pos(1, 6)};

        long maxY = 0;
        long maxX = 0;
        
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Pos(positions[i].x + minX, positions[i].y + minY);
            maxX = Math.max(maxX, positions[i].x);
            maxY = Math.max(maxY, positions[i].y);
        }

        for (long y = maxY; y >= 0; y--) {
            for (int x = 0; x < maxX; x++) {
                int finalX = x;
                long finalY = y;
                System.out.print(Arrays.stream(positions).anyMatch(pos -> pos.x == finalX && pos.y == finalY) ? "#" : ".");
            }
            System.out.print("\n");
        }

        long sum = 0;

        for (int i = 0; i < 5; i++) {
            long add = ((positions[i].y + minY) + (positions[i + 1].y + minY)) * ((positions[i].x + minX) - (positions[i + 1].x + minX));
            System.out.println(add);
            sum += add;
        }

        return 0.5 * sum;
    }

    @Override
    public Object part2(Stream<String> linesStream) {
        return null;
    }

    private enum Direction {
        UP, DOWN, RIGHT, LEFT;

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

        public Pos add(Direction direction, int value) {
            return switch (direction) {
                case UP -> new Pos(x, y + value);
                case DOWN -> new Pos(x, y - value);
                case LEFT -> new Pos(x - value, y);
                case RIGHT -> new Pos(x + value, y);
            };
        }
    }
}
