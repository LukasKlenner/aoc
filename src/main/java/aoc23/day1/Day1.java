package aoc23.day1;

import aoc23.Day;

import java.util.*;
import java.util.stream.Stream;

public class Day1 implements Day {

    private static final Map<String, String> stringToNumber = Map.of(
            "one", "o1e",
            "two", "t2o",
            "three", "t3e",
            "four", "f4r",
            "five", "f5e",
            "six", "s6x",
            "seven", "s7n",
            "eight", "e8t",
            "nine", "n9e"
    );

    public Object run(Stream<String> lines) {

        return lines
                .map(s -> {
                    for (Map.Entry<String, String> entry : stringToNumber.entrySet()) {
                        s = s.replaceAll(entry.getKey(), entry.getValue());
                    }

                    return s;
                })
                .map(s -> s.chars().filter(Character::isDigit).mapToObj(Character::getNumericValue).toList())
                .map(s -> s.get(0).toString() + s.get(s.size() - 1).toString())
                .mapToInt(Integer::valueOf)
                .sum();
    }
}