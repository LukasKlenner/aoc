package aoc23.day10;

import aoc23.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 implements Day {


    @Override
    public Object part1(Stream<String> linesStream) {

        Character[][] arr = linesStream.map(line -> line.chars().mapToObj(i -> (char) i).toArray(Character[]::new)).toArray(Character[][]::new);
        PipeType[][] pipes = Arrays.stream(arr).map(a -> Arrays.stream(a).map(PipeType::of).toArray(PipeType[]::new)).toArray(PipeType[][]::new);

        for (int i = 0; i < pipes.length; i++) {
            for (int j = 0; j < pipes[i].length; j++) {
                if (pipes[i][j] == PipeType.START) {

                    if (pipes[i + 1][j].direction1 == Direction.NORTH || pipes[i + 1][j].direction2 == Direction.NORTH) {
                        return findPath(pipes, new Pos(j, i + 1), Direction.NORTH);
                    } else if (pipes[i - 1][j].direction1 == Direction.SOUTH || pipes[i - 1][j].direction2 == Direction.SOUTH) {
                        return findPath(pipes, new Pos(j, i - 1), Direction.SOUTH);
                    } else if (pipes[i][j + 1].direction1 == Direction.EAST || pipes[i][j + 1].direction2 == Direction.EAST) {
                        return findPath(pipes, new Pos(j + 1, i), Direction.EAST);
                    } else if (pipes[i][j - 1].direction1 == Direction.WEST || pipes[i][j - 1].direction2 == Direction.WEST) {
                        return findPath(pipes, new Pos(j - 1, i), Direction.WEST);
                    }

                }
            }
        }

        return null;
    }

    @Override
    public Object part2(Stream<String> linesStream) {

        Character[][] arr = linesStream.map(line -> line.chars().mapToObj(i -> (char) i).toArray(Character[]::new)).toArray(Character[][]::new);
        PipeType[][] pipes = Arrays.stream(arr).map(a -> Arrays.stream(a).map(PipeType::of).toArray(PipeType[]::new)).toArray(PipeType[][]::new);
        boolean[][] marked = null;
        boolean[][] outside = new boolean[2 * pipes.length][2 * pipes[0].length];

        for (int i = 0; i < pipes.length; i++) {
            for (int j = 0; j < pipes[i].length; j++) {
                if (pipes[i][j] == PipeType.START) {

                    if (pipes[i + 1][j].direction1 == Direction.NORTH || pipes[i + 1][j].direction2 == Direction.NORTH) {
                        marked = markPath(pipes, new Pos(j, i + 1), Direction.NORTH);
                    } else if (pipes[i - 1][j].direction1 == Direction.SOUTH || pipes[i - 1][j].direction2 == Direction.SOUTH) {
                        marked = markPath(pipes, new Pos(j, i - 1), Direction.SOUTH);
                    } else if (pipes[i][j + 1].direction1 == Direction.EAST || pipes[i][j + 1].direction2 == Direction.EAST) {
                        marked = markPath(pipes, new Pos(j + 1, i), Direction.EAST);
                    } else if (pipes[i][j - 1].direction1 == Direction.WEST || pipes[i][j - 1].direction2 == Direction.WEST) {
                        marked = markPath(pipes, new Pos(j - 1, i), Direction.WEST);
                    }

                }
            }
        }

        if (marked == null) throw new RuntimeException("No path found");

        LinkedList<Pos> workList = new LinkedList<>();

        int maxX = 2 * pipes[0].length - 1;
        int maxY = 2 * pipes.length - 1;

        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {
                if (!marked[i][j]) {
                    if (i == 0 || i == maxY || j == 0 || j == maxX) {
                        outside[i][j] = true;
                        workList.addAll(new Pos(j, i).adjacentPos(maxX, maxY));
                    }
                }
            }
        }

        while (!workList.isEmpty()) {

            Pos pos = workList.removeFirst();
            if (marked[pos.y()][pos.x()]) continue;
            if (outside[pos.y()][pos.x()]) continue;

            workList.addAll(pos.adjacentPos(maxX,maxY));
            outside[pos.y()][pos.x()] = true;

        }

        boolean[][] finalMarked = marked;
        return IntStream.range(0, pipes.length)
                .mapToObj(i -> IntStream.range(0, pipes[i].length)
                        .filter(j -> !finalMarked[2 * i][2 * j] && !outside[2 * i][2 * j]).count())
                .mapToLong(Long::longValue)
                .sum();
    }

    private double findPath(PipeType[][] pipes, Pos pos, Direction direction) {
        PipeType pipeType = pipes[pos.y()][pos.x()];
        int count = 1;

        while (pipeType != PipeType.START) {
            count++;

            Direction nextDirection = pipeType.direction1 == direction ? pipeType.direction2 : pipeType.direction1;
            pos = nextDirection.apply(pos);
            pipeType = pipes[pos.y()][pos.x()];
            direction = nextDirection.opposite();
        }

        return count / 2.0;
    }

    private boolean[][] markPath(PipeType[][] pipes, Pos pos, Direction direction) {
        PipeType pipeType = pipes[pos.y()][pos.x()];
        boolean[][] marked = new boolean[2 * pipes.length][2 * pipes[0].length];
        Direction initialDirection = direction;

        while (pipeType != PipeType.START) {
            marked[2 * pos.y()][2 * pos.x()] = true;

            Direction nextDirection = pipeType.direction1 == direction ? pipeType.direction2 : pipeType.direction1;

            Pos filling = nextDirection.apply(new Pos(2 * pos.x(), 2 * pos.y()));
            marked[filling.y()][filling.x()] = true;

            pos = nextDirection.apply(pos);
            pipeType = pipes[pos.y()][pos.x()];
            direction = nextDirection.opposite();
        }

        marked[2 * pos.y()][2 * pos.x()] = true;
        Pos filling = initialDirection.opposite().apply(new Pos(2 * pos.x(), 2 * pos.y()));
        marked[filling.y()][filling.x()] = true;

        return marked;
    }

    private enum PipeType {

        HORIZONTAL(Direction.WEST, Direction.EAST),
        VERTICAL(Direction.NORTH, Direction.SOUTH),
        TOP_RIGHT(Direction.NORTH, Direction.EAST),
        TOP_LEFT(Direction.NORTH, Direction.WEST),
        BOTTOM_RIGHT(Direction.SOUTH, Direction.EAST),
        BOTTOM_LEFT(Direction.SOUTH, Direction.WEST),
        START(null, null);

        private final Direction direction1;
        private final Direction direction2;

        PipeType(Direction direction1, Direction direction2) {
            this.direction1 = direction1;
            this.direction2 = direction2;
        }

        public static PipeType of(char type) {
            return switch (type) {
                case '|' -> VERTICAL;
                case '-' -> HORIZONTAL;
                case 'L' -> TOP_RIGHT;
                case 'J' -> TOP_LEFT;
                case '7' -> BOTTOM_LEFT;
                case 'F' -> BOTTOM_RIGHT;
                case '.' -> null;
                case 'S' -> START;
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }
    }

    private enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST;

        public Direction opposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case EAST -> WEST;
                case WEST -> EAST;
            };
        }

        public Pos apply(Pos pos) {
            return switch (this) {
                case NORTH -> new Pos(pos.x(), pos.y() - 1);
                case SOUTH -> new Pos(pos.x(), pos.y() + 1);
                case EAST -> new Pos(pos.x() + 1, pos.y());
                case WEST -> new Pos(pos.x() - 1, pos.y());
            };
        }
    }

    private record Pos(int x, int y) {

        List<Pos> adjacentPos(int maxX, int maxY) {

            List<Pos> list = new ArrayList<>();

            if (x > 0) list.add(new Pos(x - 1, y));
            if (x < maxX) list.add(new Pos(x + 1, y));
            if (y > 0) list.add(new Pos(x, y - 1));
            if (y < maxY) list.add(new Pos(x, y + 1));

            return list;
        }

    }

}


