package aoc.year24.day19;

import aoc.Day;
import aoc.JoinedDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Day19 implements Day {

    @Override
    public long part1(Stream<String> lines) {
        List<String> input = lines.toList();

        Map<String, Boolean> possiblePatterns = new HashMap<>();
        Arrays.stream(input.get(0).split(", ")).forEach(s -> possiblePatterns.put(s, true));

        return input.subList(2, input.size()).stream().filter(s -> isPossible(s, possiblePatterns)).count();
    }


    private boolean isPossible(String design, Map<String, Boolean> possiblePatterns) {

        for (int i = 1; i < design.length(); i++) {
            String part1 = design.substring(0, i);
            String part2 = design.substring(i);

            if (!possiblePatterns.containsKey(part1)) {
                possiblePatterns.put(part1, isPossible(part1, possiblePatterns));
            }

            if (!possiblePatterns.containsKey(part2)) {
                possiblePatterns.put(part2, isPossible(part2, possiblePatterns));
            }

            if (possiblePatterns.get(part1) && possiblePatterns.get(part2)) {
                possiblePatterns.put(design, true);
                return true;
            }

        }

        possiblePatterns.put(design, false);
        return false;
    }

    @Override
    public long part2(Stream<String> lines) {
        List<String> input = lines.toList();

        Map<String, Long> combinationCount = new HashMap<>();
        Arrays.stream(input.get(0).split(", ")).forEach(s -> combinationCount.put(s, 1L));

        return input.subList(2, input.size()).stream().mapToLong(s -> getCombinations(s, combinationCount)).sum();
    }


    private long getCombinations(String design, Map<String, Long> combinationCount) {

        long count = 0;
        Set<String> used = new HashSet<>();

        for (int i = 1; i < design.length(); i++) {
            String part1 = design.substring(0, i);
            String part2 = design.substring(i);

            if (part1.compareTo(part2) > 0) {
                String tmp = part1;
                part1 = part2;
                part2 = tmp;
            }

            if (!combinationCount.containsKey(part1)) {
                combinationCount.put(part1, getCombinations(part1, combinationCount));
            }

            if (!combinationCount.containsKey(part2)) {
                combinationCount.put(part2, getCombinations(part2, combinationCount));
            }

            Long part1Count = combinationCount.get(part1);
            Long part2Count = combinationCount.get(part2);

            String idString = part1 + "|" + part2;

            if (!used.contains(idString)) {
                count += part1Count * part2Count;
                used.add(idString);
            }

            /*
            r wrr -> r wr r
            rwr r -> r wr r
             */

        }

        combinationCount.put(design, count);
        return count;
    }
}
