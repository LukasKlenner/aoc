package aoc.year23.day16;

import java.util.*;
import java.util.stream.Stream;

public class Day16 implements aoc.Day {
    @Override
    public long part1(Stream<String> linesStream) {
        return run(linesStream, true);
    }

    @Override
    public long part2(Stream<String> linesStream) {
        long start = System.currentTimeMillis();
        long run = run(linesStream, false);
        System.out.println(System.currentTimeMillis() - start);
        return run;
    }

    private long run(Stream<String> linesStream, boolean part1) {
        char[][] input = linesStream.map(String::toCharArray).toArray(char[][]::new);
        Mirror[][] mirrors = new Mirror[input.length][input[0].length];

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                if (input[y][x] == '/') {
                    mirrors[y][x] = Mirror.BOTTOM_TOP;
                } else if (input[y][x] == '\\') {
                    mirrors[y][x] = Mirror.TOP_BOTTOM;
                } else if (input[y][x] == '-') {
                    mirrors[y][x] = Mirror.HORIZONTAL;
                } else if (input[y][x] == '|') {
                    mirrors[y][x] = Mirror.VERTICAL;
                }
            }
        }

        List<PosAndDir> startPositions = new ArrayList<>();

        if (part1) {
            startPositions.add(new PosAndDir(new Pos(0, 0), Direction.EAST));
        } else {
            for (int y = 0; y < input.length; y++) {
                startPositions.add(new PosAndDir(new Pos(0, y), Direction.EAST));
                startPositions.add(new PosAndDir(new Pos(input[0].length - 1, y), Direction.WEST));
            }
            for (int x = 0; x < input[0].length; x++) {
                startPositions.add(new PosAndDir(new Pos(x, 0), Direction.SOUTH));
                startPositions.add(new PosAndDir(new Pos(x, input.length - 1), Direction.NORTH));
            }
        }

        long max = -1;

        for (PosAndDir startPos : startPositions) {

            boolean[][][] beams = new boolean[input.length][input[0].length][4];

            Queue<PosAndDir> beamsToProcess = new LinkedList<>();
            beamsToProcess.add(startPos);

            int maxX = input[0].length - 1;
            int maxY = input.length - 1;

            while (!beamsToProcess.isEmpty()) {

                PosAndDir beam = beamsToProcess.poll();

                int x = beam.pos.x;
                int y = beam.pos.y;
                Direction direction = beam.dir;
                Pos pos = beam.pos;

                if (pos.isOutside(maxX, maxY)) continue;
                if (beams[y][x][direction.ordinal()]) continue;

                beams[y][x][direction.ordinal()] = true;

                Mirror mirror = mirrors[y][x];

                if (mirror == null) {
                    beamsToProcess.add(new PosAndDir(pos.add(direction), direction));
                } else {
                    beamsToProcess.addAll(mirror.getNextPos(beam));
                }

            }

            long count = Arrays.stream(beams)
                    .flatMap(Arrays::stream)
                    .filter(arr -> arr[0] || arr[1] || arr[2] || arr[3])
                    .count();

            max = Math.max(count, max);
        }

        return max;
    }

    private record PosAndDir(Pos pos, Direction dir) {}

    private enum Mirror {

        HORIZONTAL,
        VERTICAL,
        BOTTOM_TOP,
        TOP_BOTTOM;

        public List<PosAndDir> getNextPos(PosAndDir posAndDir) {

            Direction direction = posAndDir.dir();
            Pos mirrorPos = posAndDir.pos();

            Direction[] nextDirections = switch (this) {
                case HORIZONTAL -> switch (direction) {
                    case NORTH:
                    case SOUTH:
                        yield new Direction[]{Direction.EAST, Direction.WEST};
                    case WEST:
                    case EAST:
                        yield new Direction[]{direction};
                };
                case VERTICAL -> switch (direction) {
                    case EAST:
                    case WEST:
                        yield new Direction[]{Direction.SOUTH, Direction.NORTH};
                    case NORTH:
                    case SOUTH:
                        yield new Direction[]{direction};
                };
                case BOTTOM_TOP -> switch (direction) {
                    case NORTH:
                        yield new Direction[]{Direction.EAST};
                    case SOUTH:
                        yield new Direction[]{Direction.WEST};
                    case WEST:
                        yield new Direction[]{Direction.SOUTH};
                    case EAST:
                        yield new Direction[]{Direction.NORTH};
                };
                case TOP_BOTTOM -> switch (direction) {
                    case NORTH:
                        yield new Direction[]{Direction.WEST};
                    case SOUTH:
                        yield new Direction[]{Direction.EAST};
                    case WEST:
                        yield new Direction[]{Direction.NORTH};
                    case EAST:
                        yield new Direction[]{Direction.SOUTH};
                };
            };

            return Arrays.stream(nextDirections)
                    .map(dir -> new PosAndDir(mirrorPos.add(dir), dir))
                    .toList();
        }

    }

    private record Pos(int x, int y) {

        public Pos add(Direction direction) {
            return switch (direction) {
                case NORTH -> new Pos(x, y - 1);
                case SOUTH -> new Pos(x, y + 1);
                case WEST -> new Pos(x - 1, y);
                case EAST -> new Pos(x + 1, y);
            };
        }

        public boolean isOutside(int maxX, int maxY) {
            return x < 0 || x > maxX || y < 0 || y > maxY;
        }

    }

    private enum Direction {
        NORTH,
        SOUTH,
        WEST,
        EAST;

    }

}
