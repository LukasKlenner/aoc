package aoc.util.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Graph<T> {

    private final Set<Node<T>> nodes = new HashSet<>();
    private final Set<Edge<T>> edges = new HashSet<>();

    public Node<T> addNode(T value) {
        Node<T> node = new Node<>(nodes.size(), value);
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

    public void dfs(Node<T> start, Consumer<Node<T>> onDiscover) {
        Set<Node<T>> visited = new HashSet<>();
        dfs(start, visited, onDiscover);
    }


    public void dfs(Node<T> current, Set<Node<T>> visited, Consumer<Node<T>> onDiscover) {
        current.getOutgoingEdges().stream()
                .map(Edge::to)
                .filter(node -> !visited.contains(node))
                .forEach(node -> {
                    visited.add(node);
                    onDiscover.accept(node);
                    dfs(node, visited, onDiscover);
                });
    }
}
