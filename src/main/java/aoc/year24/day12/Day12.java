package aoc.year24.day12;

import aoc.util.Direction;
import aoc.util.JoinedGridTask;
import aoc.util.Pos;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Day12 extends JoinedGridTask<Character> {

    public Day12() {
        super(Function.identity(), Character[]::new, Character[][]::new);
    }

    @Override
    public long run(boolean part1) {

        Graph<Character> graph = toGraph((prev, next) -> (getValue(prev).equals(getValue(next)))).first;

        int result =  0;

        Set<Node<Character>> visited = new HashSet<>();
        for (Node<Character> node : graph.getNodes()) {

            if (visited.contains(node)) continue;

            AtomicInteger area = new AtomicInteger();
            AtomicInteger perimeter = new AtomicInteger();

            Map<Direction, Set<Node<Character>>> usedForSide = new HashMap<>();
            for (Direction direction : Direction.values()) usedForSide.put(direction, new HashSet<>());

            graph.dfs(node, visited, n -> {
                area.getAndIncrement();

                if (part1) {
                    perimeter.addAndGet(4 - n.getOutgoingEdges().size());
                } else {
                    for (Direction direction : Direction.values()) {
                        if (!n.hasConnection(direction) && !usedForSide.get(direction).contains(n)) {
                            perimeter.incrementAndGet();

                            Set<Node<Character>> usedSet = usedForSide.get(direction);

                            for (Direction walkDir : List.of(direction.rotate90(), direction.rotate270())) {
                                Node<Character> current = n;
                                while (current != null) {
                                    if (current.hasConnection(direction)) break;

                                    usedSet.add(current);

                                    current = current.getNeighbor(walkDir);
                                }
                            }

                        }
                    }
                }
            }, n -> {});

            result += area.get() * perimeter.get();

        }

        return result;
    }

}
