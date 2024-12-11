package aoc.year24.day10;

import aoc.JoinedDay;
import aoc.util.JoinedGridTask;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day10 extends JoinedGridTask<Integer> {

    public Day10() {
        super(Character::getNumericValue, Integer[]::new, Integer[][]::new);
    }

    @Override
    public long run(boolean part1) {

        Graph<Integer> graph = toGraph((pos1, pos2) -> getValue(pos1) + 1 == getValue(pos2));

        int result = 0;

        for (Node<Integer> n : graph.getNodes()) {
            if (n.getValue() == 0) {
                Set<Integer> reached = new HashSet<>();
                graph.dfs(n, node -> {
                    if (node.getValue() == 9) reached.add(node.getId());
                });
                result += reached.size();
            }
        }

        return result;
    }
}
