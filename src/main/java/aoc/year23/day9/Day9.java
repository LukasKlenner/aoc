package aoc.year23.day9;

import aoc.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day9 implements Day {
    @Override
    public Object part1(Stream<String> lines) {
        return run(lines, true);
    }

    @Override
    public Object part2(Stream<String> lines) {
        return run(lines, false);
    }

    private Object run(Stream<String> lines, boolean part1) {
        return lines.map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).toList()).mapToInt(l -> extrapolate(l, part1)).sum();
    }

    private int extrapolate(List<Integer> list, boolean part1) {

        List<Integer> newList = new ArrayList<>();
        boolean nonZero = false;
        for (int i = 1; i < list.size(); i++) {
            int diff = list.get(i) - list.get(i - 1);
            newList.add(diff);
            if (diff != 0) nonZero = true;
        }

        Integer value = part1 ? list.get(list.size() - 1) : list.get(0);

        if (!nonZero) return value;
        return value + (part1 ? extrapolate(newList, part1) : -extrapolate(newList, part1));
    }
}
