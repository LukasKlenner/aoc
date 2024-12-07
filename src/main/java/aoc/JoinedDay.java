package aoc;

import java.util.stream.Stream;

public interface JoinedDay extends Day {

    long run(Stream<String> stream, boolean part1);

    @Override
    default long part1(Stream<String> lines) {
        return run(lines, true);
    }

    @Override
    default long part2(Stream<String> lines) {
        return run(lines, false);
    }
}
