package aoc.year24.day22;

import aoc.Day;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day22 implements Day {

    @Override
    public long part1(Stream<String> lines) {
        return lines.mapToLong(Long::parseLong).map(this::generateAll).sum();
    }

    private long generateAll(long number) {
        for (int i = 0; i < 2000; i++) {
            number = generateNext(number);
        }

        return number;
    }

    private long generateNext(long number) {
        number = prune(mix(number, number * 64));
        number = prune(mix(number, number / 32L));
        number = prune(mix(number, number * 2048));
        return number;
    }

    private long mix(long num1, long num2) {
        return num1 ^ num2;
    }

    private long prune(long number) {
        return number % 16777216L;
    }


    @Override
    public long part2(Stream<String> lines) {

        List<Map<List<Long>, Long>> sequenceToPrices = new ArrayList<>();
        Set<List<Long>> allSequences = new HashSet<>();

        lines.mapToLong(Long::parseLong).forEach(l -> sequenceToPrices.add(generateAll(l, allSequences)));

        long maxSum = 0;
        
        for (List<Long> sequence : allSequences) {
            long sum = 0;


            // sequence = List.of(2L, 1L, -1L, 3L);

            for (Map<List<Long>, Long> sequenceToPrice : sequenceToPrices) {
                sum += sequenceToPrice.getOrDefault(sequence, 0L);
            }

            //System.out.println("%s -> %s".formatted(sequence, sum));

            maxSum = Math.max(sum, maxSum);
        }

        return maxSum;
    }

    private Map<List<Long>, Long> generateAll(long number, Set<List<Long>> allSequence) {
        Queue<Long> queue = new ArrayDeque<>();
        Map<List<Long>, Long> sequenceToPrice = new HashMap<>();

        long prev = number;
        for (int i = 0; i < 2000; i++) {
            number = generateNext(number);

            if (prev != -1) queue.offer((number % 10) - (prev % 10));
            if (queue.size() == 5) queue.poll();

            List<Long> sequence = new ArrayList<>(queue);
            if (sequence.size() == 4) {
                allSequence.add(sequence);
                sequenceToPrice.putIfAbsent(sequence, number % 10);
            }

            prev = number;
        }

        return sequenceToPrice;
    }
}
