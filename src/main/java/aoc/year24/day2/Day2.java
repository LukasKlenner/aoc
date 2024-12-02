package aoc.year24.day2;

import aoc.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 implements Day {

    @Override
    public Object part1(Stream<String> lines) {
        return lines.map(s -> Arrays.stream(s.split(" "))
                .map(Integer::parseInt).toList()
        ).mapToInt(l -> isValid(l) ? 1 : 0).sum();
    }

    @Override
    public Object part2(Stream<String> lines) {
        List<List<Integer>> input = lines.map(s -> Arrays.stream(s.split(" "))
                .map(Integer::parseInt).collect(Collectors.toList())
        ).toList();

        int count = 0;

        outer: for (List<Integer> l : input) {

            if (isValid(l)) count++;
            else {
                for (int i = 0; i < l.size(); i++) {

                    int removed = l.remove(i);
                    if (isValid(l)) {
                        count++;
                        continue outer;
                    }
                    l.add(i, removed);
                }
            }

        }

        return count;
    }

    private static boolean isValid(List<Integer> l) {
        boolean expected = l.get(0) < l.get(1);
        for (int i = 0; i < l.size() - 1; i++) {
            if (!isValid(expected, i, i+1, l)) return false;
        }

        return true;
    }

    private static boolean isValid(boolean expected, int i, int j, List<Integer> l) {
        if (j >= l.size()) return true;

        int diff = l.get(i) - l.get(j);
        return diff < 0 == expected && Math.abs(diff) >= 1 && Math.abs(diff) <= 3;
    }
}
