package aoc.year24.day18;

import aoc.JoinedDay;
import aoc.util.GridTask;
import aoc.util.Pair;
import aoc.util.Pos;
import aoc.util.graph.Dijkstra;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day18 extends GridTask<Boolean> {

    private final int size = 71;
    private final int part1Count = 1024;

    public Day18() {
        super(Function.identity(), null, null, null);
    }

    @Override
    public long part1() {
        createGrid(part1Count);
        return findPath().size() - 1;
    }

    private void createGrid(int count) {
        loadGrid();

        for (int i = 0; i < count; i++) {
            markPiece(input.get(i));
        }
    }

    private List<Node<Boolean>> findPath() {

        Pair<Graph<Boolean>, Map<Pos, Node<Boolean>>> graphAndMap = toGraph((from, to) -> !getValue(from) && !getValue(to));
        Graph<Boolean> graph = graphAndMap.first;
        Map<Pos, Node<Boolean>> map = graphAndMap.second;

        Node<Boolean> start = map.get(new Pos(0, 0));
        Node<Boolean> target = map.get(new Pos(size - 1, size - 1));

        Dijkstra<Node<Boolean>, Boolean> dijkstra = new Dijkstra<>(graph.getNodes(), Function.identity(),
                (from, to) -> 1L, (from, to) -> to);

        dijkstra.run(start);

        return dijkstra.reconstructPath(start, target);
    }

    private void markPiece(String piece) {
        int x = Integer.parseInt(piece.split(",")[0]);
        int y = Integer.parseInt(piece.split(",")[1]);

        putValue(new Pos(x, y), true);
    }

    @Override
    public long part2() {
        int low = part1Count;
        int high = input.size() - 1;

        while (low < high) {
            int mid = (low + high) >>> 1;

            createGrid(mid);
            boolean pathExists = findPath() != null;

            if (pathExists) low = mid + 1;
            else high = mid - 1;
        }

        createGrid(low);
        System.out.println(findPath() == null ? input.get(low - 1) : input.get(low));
        return -1;
    }

    @Override
    protected void loadGrid() {
        grid = new Boolean[size][size];
        foreachCell((pos, val) -> putValue(pos, false));
    }
}
