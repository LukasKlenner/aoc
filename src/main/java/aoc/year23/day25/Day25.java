package aoc.year23.day25;

import aoc.Day;

import java.util.*;
import java.util.stream.Stream;

public class Day25 implements Day {

    @Override
    public long part1(Stream<String> lines) {

        HashMap<String, Node> nodes = new HashMap<>();

        lines.forEach(line -> {
                    String[] split = line.split(": ");
                    String[] split2 = split[1].split(" ");
                    for (String s : split2) {
                        Node from = nodes.computeIfAbsent(split[0], Node::new);
                        Node to = nodes.computeIfAbsent(s, Node::new);

                        Edge edge = new Edge(from, to);
                        from.edges.add(edge);
                        to.edges.add(edge);
                    }
                });

        Set<Edge> edges = new HashSet<>();

        for (Node node : nodes.values()) {
            edges.addAll(node.edges);
        }

        dfs(nodes.values().stream().findAny().get());


        outer: for (Node node : nodes.values()) {
            int count = 0;

            for (Edge edge : edges) {

                boolean fromIsChild = node.isChild(edge.from);
                boolean toIsChild = node.isChild(edge.to);

                if (fromIsChild ^ toIsChild ) {
                    count++;
                    if (count == 4) {
                        continue outer;
                    }
                }

            }

            if (count == 3) {
                int leftSize = node.childCount + 1;
                int rightSize = nodes.size() - leftSize;
                return leftSize * rightSize;
            }
        }

         return 0;
    }

    private int dfs(Node node) {

        node.visited = true;
        int childCount = 0;

        node.assignNextId();

        for (Edge edge : node.edges) {
            if (!edge.getOther(node).visited) {
                childCount += dfs(edge.getOther(node)) + 1;
            }
        }

        node.childCount = childCount;
        return childCount;
    }

    @Override
    public long part2(Stream<String> lines) {
        return 0;
    }

    private static class Node {

        boolean visited = false;
        int childCount = 0;
        int id = -1;

        static int idCounter = 0;

        List<Edge> edges = new ArrayList<>();
        String name;

        public Node(String name) {
            this.name = name;
        }

        public void assignNextId() {
            id = idCounter++;
        }

        public boolean isChild(Node other) {
            return other.id >= id && other.id <= id + childCount;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Edge {

        Node from;
        Node to;

        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
        }

        public Node getOther(Node node) {
            if (node == from) {
                return to;
            } else if (node == to) {
                return from;
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return (Objects.equals(from, edge.from) && Objects.equals(to, edge.to)) ||
                    (Objects.equals(from, edge.to) && Objects.equals(to, edge.from));
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to) % 100000 + Objects.hash(to, from) % 100000;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    '}';
        }
    }
}
