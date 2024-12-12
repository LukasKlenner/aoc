package aoc.util;

import aoc.Day;
import aoc.util.graph.Edge;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class GridTask<T> implements Day {

    private final Function<Character, T> gridParser;
    private final Function<Integer, T[]> arrayFct;
    private final Function<Integer, T[][]> arrayFct2D;

    protected T[][] grid;

    protected List<String> input;

    public GridTask(Function<Character, T> gridParser, Function<Integer, T[]> arrayFct, Function<Integer, T[][]> arrayFct2D) {
        this.gridParser = gridParser;
        this.arrayFct = arrayFct;
        this.arrayFct2D = arrayFct2D;
    }

    @Override
    public long part1(Stream<String> lines) {
        if (grid == null) loadGrid(lines);

        return part1();
    }

    public abstract long part1();

    @Override
    public long part2(Stream<String> lines) {
        if (grid == null) loadGrid(lines);

        return part2();
    }

    public abstract long part2();

    protected T putValue(Pos pos, T value) {
        T oldValue = getValue(pos);
        grid[pos.y()][pos.x()] = value;
        return oldValue;
    }

    protected T getValue(Pos pos) {
        return grid[pos.y()][pos.x()];
    }

    protected boolean isInBounds(Pos pos) {
        return pos.isInBounds(grid);
    }

    protected void foreachCell(BiConsumer<Pos, ? super T> consumer) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                consumer.accept(new Pos(x, y), grid[y][x]);
            }
        }
    }

    protected Pair<Graph<T>, Map<Pos, Node<T>>> toGraph(BiFunction<Pos, Pos, Boolean> edgeAcceptor) {
        Graph<T> graph = new Graph<>();
        Map<Pos, Node<T>> posToNode = new HashMap<>();

        foreachCell((pos, value) -> posToNode.put(pos, graph.addNode(value, pos)));

        foreachCell((pos, value) -> {
            for (Direction direction : Direction.values()) {
                Pos newPos = pos.add(direction);
                if (isInBounds(newPos) && edgeAcceptor.apply(pos, newPos)) {
                    graph.addEdge(posToNode.get(pos), posToNode.get(newPos));
                }
            }
        });

        return new Pair<>(graph, posToNode);
    }

    private void loadGrid(Stream<String> lines) {
        input = lines.toList();
        grid = arrayFct2D.apply(input.size());

        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            grid[y] = arrayFct.apply(line.length());

            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = gridParser.apply(line.charAt(x));
            }
        }
    }

}
