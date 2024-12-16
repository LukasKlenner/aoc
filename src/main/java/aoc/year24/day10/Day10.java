package aoc.year24.day10;

import aoc.util.JoinedGridTask;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Day10 extends JoinedGridTask<Integer> {

    public Day10() {
        super(Character::getNumericValue, Integer[]::new, Integer[][]::new);
    }

    @Override
    public long run(boolean part1) {

        Graph<Integer> graph = toGraph((pos1, pos2) -> getValue(pos1) + 1 == getValue(pos2)).first;

        AtomicInteger result = new AtomicInteger();

        for (Node<Integer> n : graph.getNodes()) {
            if (n.getValue() == 0) {
                if (part1) {
                    Set<Integer> reached = new HashSet<>();
                    graph.dfs(n, node -> {
                        if (node.getValue() == 9) reached.add(node.getId());
                        return true;
                    }, node -> {});
                    result.addAndGet(reached.size());
                } else {

                    Map<Node<Integer>, Integer> pathsToNode = new HashMap<>();
                    pathsToNode.put(n, 1);

                    graph.bfs(n, (from, newNode) -> {
                        pathsToNode.put(newNode, pathsToNode.get(from));
                        if (newNode.getValue() == 9) result.addAndGet(pathsToNode.get(from));
                    }, (from, to) -> {
                        pathsToNode.put(to, pathsToNode.get(to) + pathsToNode.get(from));
                        if (to.getValue() == 9) result.addAndGet(pathsToNode.get(from));
                    }, node -> {});
                }
            }
        }

        return result.get();
    }
}
