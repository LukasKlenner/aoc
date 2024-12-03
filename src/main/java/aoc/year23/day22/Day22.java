package aoc.year23.day22;

import aoc.Day;

import java.util.*;
import java.util.stream.Stream;

public class Day22 implements Day {

    @Override
    public Object part1(Stream<String> lines) {

        List<Brick> bricks = lines.map(Brick::new)
                .sorted(Comparator.comparingInt(Brick::getMinZ))
                .toList();

        int maxX = bricks.stream().mapToInt(Brick::getMaxX).max().getAsInt();
        int maxY = bricks.stream().mapToInt(Brick::getMaxY).max().getAsInt();

        Brick[][] highestBricks = new Brick[maxY + 1][maxX + 1];

        for (Brick brick : bricks) {

            int highestZ = -1;

            for (Pos pos : brick) {
                Brick highestBrick = highestBricks[pos.y][pos.x];
                if (highestBrick != null) {
                    highestZ = Math.max(highestZ, highestBrick.getMaxZ());
                }
            }

            brick.setZ(highestZ + 1);

            for (Pos pos : brick) {

                Brick highestBrick = highestBricks[pos.y][pos.x];
                if (highestBrick != null && highestBrick.getMaxZ() == brick.getMinZ() - 1) {
                    highestBrick.addSupports(brick);
                    brick.addSupporter(highestBrick);
                }

                highestBricks[pos.y][pos.x] = brick;

            }

        }

        bricks = bricks.stream()
                .sorted(Comparator.comparingInt(Brick::getMinZ))
                .toList();

        int count = 0;

        outer: for (Brick brick : bricks) {

            for (Brick supported : brick.supports) {
                if (supported.supportedBy.size() == 1) {
                    continue outer;
                }
            }

            count++;
        }


        return count;
    }

    @Override
    public Object part2(Stream<String> lines) {
        List<Brick> bricks = lines.map(Brick::new)
                .sorted(Comparator.comparingInt(Brick::getMinZ))
                .toList();

        int maxX = bricks.stream().mapToInt(Brick::getMaxX).max().getAsInt();
        int maxY = bricks.stream().mapToInt(Brick::getMaxY).max().getAsInt();

        Brick[][] highestBricks = new Brick[maxY + 1][maxX + 1];

        for (Brick brick : bricks) {

            int highestZ = -1;

            for (Pos pos : brick) {
                Brick highestBrick = highestBricks[pos.y][pos.x];
                if (highestBrick != null) {
                    highestZ = Math.max(highestZ, highestBrick.getMaxZ());
                }
            }

            brick.setZ(highestZ + 1);

            for (Pos pos : brick) {

                Brick highestBrick = highestBricks[pos.y][pos.x];
                if (highestBrick != null && highestBrick.getMaxZ() == brick.getMinZ() - 1) {
                    highestBrick.addSupports(brick);
                    brick.addSupporter(highestBrick);
                }

                highestBricks[pos.y][pos.x] = brick;

            }

        }

        bricks = bricks.stream()
                .sorted(Comparator.comparingInt(Brick::getMinZ))
                .toList();

        long count = 0;

        for (Brick brick : bricks) {

            Set<Brick> nextLayer = brick.supports;
            HashMap<Brick, Boolean> falls = new HashMap<>();

            while (!nextLayer.isEmpty()) {
                Set<Brick> newNextLayer = new HashSet<>();
                for (Brick next : nextLayer) {
                    if (next.supportedBy.stream().allMatch(b -> falls.getOrDefault(b, false) || b == brick)) {
                        count++;
                        falls.put(next, true);
                        newNextLayer.addAll(next.supports);
                    }
                }
                nextLayer = newNextLayer;
            }

        }


        return count;
    }

    private static class Brick implements Iterable<Pos> {

        int[] minCoords;
        int maxCoord;
        int coord;

        Set<Brick> supports = new HashSet<>();
        Set<Brick> supportedBy = new HashSet<>();
        public Brick(String line) {
            String[] split = line.split("~");

            minCoords = Arrays.stream(split[0].split(",")).mapToInt(Integer::parseInt).toArray();

            Integer[] maxCoords = Arrays.stream(split[1].split(",")).map(Integer::parseInt).toArray(Integer[]::new);

            boolean set = false;
            for (int i = 0; i < 3; i++) {
                if (maxCoords[i] != minCoords[i]) {
                    int min = Math.min(maxCoords[i], minCoords[i]);
                    int max = Math.max(maxCoords[i], minCoords[i]);

                    minCoords[i] = min;
                    maxCoord = max;
                    
                    coord = i;

                    set = true;
                }
            }

            if (!set) {
                coord = 0;
                maxCoord = minCoords[0];
            }
        }

        public void setZ(int z) {
            if (zOriented()) {
                int diff = minCoords[2] - z;
                maxCoord = maxCoord - diff;
            }
            minCoords[2] = z;
        }

        public void addSupporter(Brick brick) {
            supportedBy.add(brick);
        }

        public void addSupports(Brick brick) {
            supports.add(brick);
        }

        public int getMinZ() {
            return minCoords[2];
        }

        public int getMaxX() {
            return xOriented() ? maxCoord : minCoords[0];
        }

        public int getMaxY() {
            return yOriented() ? maxCoord : minCoords[1];
        }

        public int getMaxZ() {
            return zOriented() ? maxCoord : minCoords[2];
        }

        public boolean xOriented() {
            return coord == 0;
        }

        public boolean yOriented() {
            return coord == 1;
        }

        public boolean zOriented() {
            return coord == 2;
        }

        @Override
        public Iterator<Pos> iterator() {
            if (zOriented()) {
                return new Iterator<>() {

                    boolean first = true;

                    @Override
                    public boolean hasNext() {
                        return first;
                    }

                    @Override
                    public Pos next() {
                        first = false;
                        return new Pos(minCoords[0], minCoords[1]);
                    }
                };
            }

            return new Iterator<>() {

                int current = minCoords[coord];

                @Override
                public boolean hasNext() {
                    return current <= maxCoord;
                }

                @Override
                public Pos next() {
                    Pos next = xOriented() ? new Pos(current, minCoords[1]) : new Pos(minCoords[0], current);
                    current++;
                    return next;
                }
            };
        }
    }

    private record Pos(int x, int y) {}
}
