package aoc.year23.day2;

import aoc.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("DuplicatedCode")
public class Day2 implements Day {


    @Override
    public long part1(Stream<String> lines) {
        return lines.mapToInt(l -> Arrays.stream(l.split(": ")[1].split("(; )|(, )"))
                        .map(s -> s.split(" "))
                        .anyMatch(arr -> arr[1].equals("red") && Integer.parseInt(arr[0]) > 12 ||
                                arr[1].equals("green") && Integer.parseInt(arr[0]) > 13 ||
                                arr[1].equals("blue") && Integer.parseInt(arr[0]) > 14)
                        ? 0 : Integer.parseInt(l.split(":")[0].substring(5)))
                .sum();
    }

    @Override
    public long part2(Stream<String> lines) {

        return lines.map(l -> Arrays.stream(l.split(": ")[1].split("; "))
                        .map(s -> Arrays.stream(s.split(", "))
                                .collect(Collectors.toMap(
                                        e -> e.split(" ")[1].charAt(0),
                                        e -> Integer.parseInt(e.split(" ")[0]))
                                )
                        )
                        .collect(
                                () -> new HashMap<>(Map.of('b', 0, 'r', 0, 'g', 0)),
                                (m , n) -> m.replaceAll((e, v) -> Math.max(v, n.getOrDefault(e, 0))),
                                (a, b) -> {}
                        )
                )
                .mapToInt(m -> m.values().stream().reduce(1, (a,b) -> a*b))
                .sum();
    }
}
