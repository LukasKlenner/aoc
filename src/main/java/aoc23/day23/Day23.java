package aoc23.day23;

import aoc23.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day23 implements Day {
    @Override
    public Object part1(Stream<String> lines) {
        return run(lines, true);
    }

    private String arrToStr(char[][] arr) {
        StringBuilder builder = new StringBuilder();
        for (char[] chars : arr) {
            builder.append(Arrays.toString(chars)).append("\n");
        }
        return builder.toString();
    }

    @Override
    public Object part2(Stream<String> lines) {
        return run(lines, false);
    }

    private Object run(Stream<String> lines, boolean part1) {
        char[][] grid = lines.map(String::toCharArray).toArray(char[][]::new);

        Path.bounds = new Pos(grid[0].length, grid.length);

        Path path = new Path(grid);

        for (int x = 0; x < grid[0].length; x++) {

            if (grid[0][x] == '.') {
                return path.calculate(new Pos(x, 0), part1) - 1;
            }

        }

        throw new IllegalStateException();
    }

    private static class Path {

        char[][] grid;

        static Pos bounds;

        public Path(char[][] grid) {
            this.grid = grid;
        }

        public int calculate(Pos pos, boolean part1) {

            List<Pos> neighbours = getNeighbours(pos, part1);

            int length = 1;

            while (neighbours.size() <= 1) {

                if (neighbours.isEmpty()) {
                    return 0;
                }

                grid[pos.y][pos.x] = '-';
                length++;

                pos = neighbours.get(0);

                if (pos.y == bounds.y - 1) return length;

                neighbours = getNeighbours(pos, part1);
            }

            grid[pos.y][pos.x] = '-';

            int max = 0;

            for (int i = 1; i < neighbours.size(); i++) {
                Path newPath = copy();
                int pathLength = newPath.calculate(neighbours.get(i), part1);
                max = Math.max(max, pathLength + length);
            }

            max = Math.max(max, calculate(neighbours.get(0), part1) + length);

            return max;
        }

        private List<Pos> getNeighbours(Pos pos, boolean part1) {
            List<Pos> neighbours = new ArrayList<>();
            for (Direction dir : Direction.values()) {
                Pos neighbour = pos.add(dir);

                if (neighbour.isOutside(bounds)) continue;

                char slope = switch (dir) {
                    case UP -> 'v';
                    case DOWN -> '^';
                    case LEFT -> '>';
                    case RIGHT -> '<';
                };

                char neighbourChar = grid[neighbour.y][neighbour.x];
                if ((part1 && neighbourChar == slope) || neighbourChar == '#' || neighbourChar == '-') continue;

                neighbours.add(neighbour);
            }
            return neighbours;
        }

        public Path copy() {
            char[][] newGrid = new char[grid.length][grid[0].length];
            for (int i = 0; i < grid.length; i++) {
                System.arraycopy(grid[i], 0, newGrid[i], 0, grid[i].length);
            }
            return new Path(newGrid);
        }
    }

    private enum Direction {
        RIGHT, DOWN, LEFT, UP
    }

    private record Pos(int x, int y) {

        public boolean isOutside(Pos bounds) {
            return x < 0 || x >= bounds.x || y < 0 || y >= bounds.y;
        }

        public Pos add(Direction direction) {
            return switch (direction) {
                case UP -> new Pos(x, y - 1);
                case DOWN -> new Pos(x, y + 1);
                case LEFT -> new Pos(x - 1, y);
                case RIGHT -> new Pos(x + 1, y);
            };
        }
    }

}
