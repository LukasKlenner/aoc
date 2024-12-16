package aoc.util.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Dijkstra<T, N> {

    public final Map<T, T> predecessors = new HashMap<>();
    public final Map<T, Long> distances = new HashMap<>();
    public final PriorityQueue<T> queue = new PriorityQueue<>(Comparator.comparingLong(distances::get));

    public final Set<T> allNodes;
    public final Function<T, Node<N>> nodeFunction;
    public final BiFunction<T, T, Long> costFunction;
    public final BiFunction<T, Node<N>, T> nodeToStateFct;

    private final HashMap<T, Boolean> inQueue = new HashMap<>();

    public Dijkstra(Set<T> allNodes, Function<T, Node<N>> nodeFunction, BiFunction<T, T, Long> costFunction, BiFunction<T, Node<N>, T> nodeToStateFct) {
        this.allNodes = allNodes;
        this.nodeFunction = nodeFunction;
        this.costFunction = costFunction;
        this.nodeToStateFct = nodeToStateFct;
    }


    public void run(T start) {

        init(start, allNodes);

        while (!queue.isEmpty()) {
            T state = queue.poll();
            Node<N> node = nodeFunction.apply(state);

            inQueue.put(state, false);

            for (Edge<N> edge : node.getOutgoingEdges()) {
                T nextState = nodeToStateFct.apply(state, edge.to());

                if (!inQueue.get(nextState)) continue;

                Long edgeCost = costFunction.apply(state, nextState);

                if (edgeCost == -1L) continue;

                Long newCost = distances.get(state) + edgeCost;
                Long oldCost = distances.get(nextState);

                if (newCost < oldCost) {
                    queue.remove(nextState);
                    distances.put(nextState, newCost);
                    queue.add(nextState);

                    predecessors.put(nextState, state);
                }
            }
        }
    }

    protected List<T> reconstructPath(T target) {
        List<T> path = new LinkedList<>();

        T current = target;

        while (current != null) {
            path.addFirst(current);
            current = predecessors.get(current);
        }

        return path;
    }


    protected void init(T start, Set<T> allNodes) {
        for (T node : allNodes) {
            distances.put(node, Long.MAX_VALUE);
            inQueue.put(node, true);
        }

        distances.put(start, 0L);
        queue.add(start);
    }

}
