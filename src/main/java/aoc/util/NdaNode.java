package aoc.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NdaNode<T> {

    private final boolean isStartNode;
    private final Map<T, Set<NdaNode<T>>> nextState = new HashMap<>();

    public NdaNode(boolean isStartNode) {
        this.isStartNode = isStartNode;
    }

    public Set<NdaNode<T>> getNextStates(T value) {
        return nextState.getOrDefault(value, Set.of());
    }

    public void addEdge(T value, NdaNode<T> to) {
        nextState.computeIfAbsent(value, k -> new HashSet<>()).add(to);
    }

    public boolean isStartNode() {
        return isStartNode;
    }
}
