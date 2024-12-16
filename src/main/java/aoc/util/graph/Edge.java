package aoc.util.graph;

import aoc.util.Direction;

public record Edge<T>(Node<T> from, Node<T> to, int weight) {

    public Edge(Node<T> from, Node<T> to) {
        this(from, to, 1);
    }

    public Direction getDirection() {
        if (from.getPos().up().equals(to.getPos())) return Direction.UP;
        if (from.getPos().down().equals(to.getPos())) return Direction.DOWN;
        if (from.getPos().left().equals(to.getPos())) return Direction.LEFT;
        if (from.getPos().right().equals(to.getPos())) return Direction.RIGHT;
        throw new IllegalArgumentException();
    }

}
