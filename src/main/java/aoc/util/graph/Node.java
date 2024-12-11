package aoc.util.graph;

import java.util.HashSet;
import java.util.Set;

public class Node<T> {

    private final int id;
    private final T value;

    private final Set<Edge<T>> ingoingEdges;
    private final Set<Edge<T>> outgoingEdges;

    public Node(int id, T value, Set<Edge<T>> ingoingEdges, Set<Edge<T>> outgoingEdges) {
        this.id = id;
        this.value = value;
        this.ingoingEdges = new HashSet<>(ingoingEdges);
        this.outgoingEdges = new HashSet<>(outgoingEdges);
    }

    public Node(int id, T value) {
        this.id = id;
        this.value = value;
        this.ingoingEdges = new HashSet<>();
        this.outgoingEdges = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public Set<Edge<T>> getIngoingEdges() {
        return ingoingEdges;
    }

    public Set<Edge<T>> getOutgoingEdges() {
        return outgoingEdges;
    }

}
