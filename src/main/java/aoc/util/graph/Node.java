package aoc.util.graph;

import aoc.util.Direction;
import aoc.util.Pos;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node<T> {

    private final int id;
    private final T value;

    private final Pos pos;

    private final Set<Edge<T>> ingoingEdges;
    private final Set<Edge<T>> outgoingEdges;

    public Node(int id, T value, Pos pos, Set<Edge<T>> ingoingEdges, Set<Edge<T>> outgoingEdges) {
        this.id = id;
        this.value = value;
        this.pos = pos;
        this.ingoingEdges = new HashSet<>(ingoingEdges);
        this.outgoingEdges = new HashSet<>(outgoingEdges);
    }

    public Node(int id, T value, Pos pos) {
        this.id = id;
        this.value = value;
        this.pos = pos;
        this.ingoingEdges = new HashSet<>();
        this.outgoingEdges = new HashSet<>();
    }

    public boolean hasEdgeTo(Node<T> node) {
        return getOutgoingEdges().stream().map(Edge::to).anyMatch(n -> n.equals(node));
    }

    public boolean hasConnection(Direction direction) {
        for (Edge<T> edge : outgoingEdges) {
            if (edge.to().getPos().equals(pos.add(direction))) return true;
        }
        return false;
    }

    public Node<T> getNeighbor(Direction direction) {
        for (Edge<T> edge : outgoingEdges) {
            if (edge.to().getPos().equals(pos.add(direction))) return edge.to();
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public Pos getPos() {
        return pos;
    }

    public Set<Edge<T>> getIngoingEdges() {
        return ingoingEdges;
    }

    public Set<Edge<T>> getOutgoingEdges() {
        return outgoingEdges;
    }

    @Override
    public String toString() {
        return "value: %s, pos: %s".formatted(value, pos);
    }
}
