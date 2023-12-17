package aoc23.day17;

import aoc23.Day;

import java.util.*;
import java.util.stream.Stream;

public class Day17 implements Day {
    @Override
    public Object part1(Stream<String> lines) {
        return run(lines, true);
    }

    @Override
    public Object part2(Stream<String> lines) {
        return run(lines, false);
    }

    private Object run(Stream<String> lines, boolean part1) {
        char[][] input = lines.map(String::toCharArray).toArray(char[][]::new);
        int[][] weights = new int[input.length][input[0].length];

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[y].length; x++) {
                weights[y][x] = Character.getNumericValue(input[y][x]);
            }
        }

        Dijkstra dijkstra = new Dijkstra();

        return dijkstra.getPath(new Pos(0, 0), new Pos(input[0].length - 1, input.length - 1), new Pos(input[0].length, input.length), weights, part1 ? 0 : 4, part1 ? 3 : 10);
    }

    private static class Dijkstra {

        private final Map<Node, Integer> distances = new HashMap<>();

        private final Set<Node> visited = new HashSet<>();

        private Pos bounds;

        private int minSteps;

        private int maxSteps;


        private final PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        public int getPath(Pos start, Pos end, Pos bounds, int[][] weights, int minSteps, int maxSteps) {
            this.bounds = bounds;
            this.minSteps = minSteps;
            this.maxSteps = maxSteps;

            for (Direction dir : Direction.values()) {
                Node startNode = new Node(start, dir, 0);
                distances.put(startNode, 0);
                queue.add(startNode);
            }

            while (!queue.isEmpty()) {

                Node node = queue.poll();

                Set<Node> neighbors = getNeighbors(node);

                for (Node neighbor : neighbors) {

                    if (visited.contains(neighbor)) continue;

                    int newDistance = distances.get(node) + weights[neighbor.pos.y][neighbor.pos.x];

                    if (neighbor.pos.equals(end)) {
                        return newDistance;
                    }

                    if (!distances.containsKey(neighbor)) {
                        distances.put(neighbor, newDistance);
                    } else {
                        if (distances.get(neighbor) > newDistance) {
                            distances.put(neighbor, newDistance);
                            if (!queue.contains(neighbor)) {
                                throw new IllegalStateException();
                            }
                            queue.remove(neighbor);
                        }
                    }

                    queue.add(neighbor);
                    visited.add(neighbor);

                }

            }

            throw new IllegalStateException();
        }

        private Set<Node> getNeighbors(Node node) {

            Set<Node> neighbors = new HashSet<>(3);

            if (node.count < minSteps) {
                Pos target = node.pos.add(node.dir);
                if (target.isOutside(bounds)) return Set.of();
                return Set.of(new Node(target, node.dir, node.count + 1));
            }

            for (Direction dir : Direction.values()) {

                if (dir == node.dir.opposite()) continue;

                Pos target = node.pos.add(dir);
                if (target.isOutside(bounds)) continue;

                if (dir == node.dir) {
                    if (node.count < maxSteps) {
                        neighbors.add(new Node(target, dir, node.count + 1));
                    }
                } else {
                    neighbors.add(new Node(target, dir, 1));
                }
            }

            return neighbors;
        }

    }

    private record Node(Pos pos, Direction dir, int count) {}

    private enum Direction {
        NORTH,
        WEST,
        SOUTH,
        EAST;

        public Direction opposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case WEST -> EAST;
                case EAST -> WEST;
            };
        }
    }

    private record Pos(int x, int y) {

        public boolean isOutside(Pos bounds) {
            return x < 0 || x >= bounds.x || y < 0 || y >= bounds.y;
        }

        public Pos add(Direction direction) {
            return switch (direction) {
                case NORTH -> new Pos(x, y - 1);
                case SOUTH -> new Pos(x, y + 1);
                case WEST -> new Pos(x - 1, y);
                case EAST -> new Pos(x + 1, y);
            };
        }
    }

}
