package aoc.util.graph;

public record Edge<T>(Node<T> from, Node<T> to, int weight) {

    public Edge(Node<T> from, Node<T> to) {
        this(from, to, 1);
    }

}
