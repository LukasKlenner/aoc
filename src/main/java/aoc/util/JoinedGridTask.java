package aoc.util;

import java.util.function.Function;

public abstract class JoinedGridTask<T> extends GridTask<T> {

    public JoinedGridTask(Function<Character, T> gridParser, Function<Integer, T[]> arrayFct, Function<Integer, T[][]> arrayFct2D) {
        super(gridParser, arrayFct, arrayFct2D);
    }

    @Override
    public long part1() {
        return run(true);
    }

    @Override
    public long part2() {
        return run(false);
    }

    public abstract long run(boolean part1);
}
