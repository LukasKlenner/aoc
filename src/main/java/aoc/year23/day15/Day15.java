package aoc.year23.day15;

import java.util.*;
import java.util.stream.Stream;

public class Day15 implements aoc.Day {
    @Override
    public long part1(Stream<String> lines) {
        return lines.flatMap(l -> Arrays.stream(l.split(",")))
                .mapToInt(str -> str.chars().reduce(0, (i, j) -> ((i + j) * 17) % 256))
                .sum();
    }

    @Override
    public long part2(Stream<String> lines) {

        Map<String, Integer> map = new LinkedHashMap<>();

        lines.flatMap(l -> Arrays.stream(l.split(",")))
                .forEach(str -> {
                    int index = str.indexOf('=');
                    if (index == -1) index = str.indexOf('-');
                    if (str.charAt(index) == '=') {
                        map.put(str.substring(0, index), Integer.parseInt(str.substring(index + 1)));
                    } else {
                        map.remove(str.substring(0, str.length() - 1));
                    }
                });

        Map<Integer, Integer> boxCount = new HashMap<>();

        return map.entrySet().stream()
                .mapToInt(entry -> (entry.getKey().chars().reduce(0, (i1, j1) -> ((i1 + j1) * 17) % 256) + 1) * (Objects.requireNonNullElse(boxCount.put(entry.getKey().chars().reduce(0, (i11, j11) -> ((i11 + j11) * 17) % 256), boxCount.getOrDefault(entry.getKey().chars().reduce(0, (i, j) -> ((i + j) * 17) % 256), 0) + 1), 0) + 1) * entry.getValue())
                .sum();
    }
}
