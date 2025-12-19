package aoc.util.graph;

import aoc.util.Pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Graph<T> {

    private final Set<Node<T>> nodes = new HashSet<>();
    private final Set<Edge<T>> edges = new HashSet<>();

    public Node<T> addNode(T value, Pos pos) {
        Node<T> node = new Node<>(nodes.size(), value, pos);
        nodes.add(node);
        return node;
    }

    public Edge<T> addEdge(Node<T> from, Node<T> to) {
        Edge<T> edge = new Edge<>(from, to);
        from.getOutgoingEdges().add(edge);
        to.getIngoingEdges().add(edge);
        edges.add(edge);
        return edge;
    }

    public Set<Node<T>> getNodes() {
        return nodes;
    }

    public Set<Edge<T>> getEdges() {
        return edges;
    }

    public void dfs(Node<T> start, Function<Node<T>, Boolean> onDiscover, Consumer<Node<T>> onFinish) {
        dfs(start, new HashSet<>(), onDiscover, onFinish);
    }

    public void dfs(Node<T> current, Set<Node<T>> visited, Function<Node<T>, Boolean> onDiscover, Consumer<Node<T>> onFinish) {
        visited.add(current);
        if (!onDiscover.apply(current)) return;

        current.getOutgoingEdges().stream()
                .map(Edge::to)
                .filter(node -> !visited.contains(node))
                .forEach(node -> dfs(node, visited, onDiscover, onFinish));

        onFinish.accept(current);
    }

    public void bfs(Node<T> start, BiConsumer<Node<T>, Node<T>> onDiscover, BiConsumer<Node<T>, Node<T>> onRevisit, Consumer<Node<T>> onFinish) {

        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(start);

        Set<Node<T>> visited = new HashSet<>();

        while (!queue.isEmpty()) {

            Node<T> current = queue.poll();
            visited.add(current);

            current.getOutgoingEdges().stream()
                    .map(Edge::to)
                    .forEach(node -> {
                        if (visited.contains(node)) {
                            onRevisit.accept(current, node);
                            return;
                        }

                        visited.add(node);
                        queue.add(node);
                        onDiscover.accept(current, node);
                    });

            onFinish.accept(current);
        }
    }
}
