package aoc;

import java.util.stream.Stream;

public interface JoinedDay extends Day {

    Object run(Stream<String> stream, boolean part1);

    @Override
    default Object part1(Stream<String> lines) {
        return run(lines, true);
    }

    @Override
    default Object part2(Stream<String> lines) {
        return run(lines, false);
    }
}
