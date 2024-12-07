package aoc.util;

import aoc.Day;

import java.util.List;
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
