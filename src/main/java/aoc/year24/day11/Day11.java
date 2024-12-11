package aoc.year24.day11;

import aoc.JoinedDay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day11 implements JoinedDay {

    @Override
    public long run(Stream<String> stream, boolean part1) {

        List<Long> numbers = Arrays.stream(stream.findAny().get().split(" ")).map(Long::parseLong).toList();

        Map<Long, List<Long>> createdNumbers = new HashMap<>();
        Map<Long, Long> numbersCount = new HashMap<>();

        for (Long number : numbers) {
            numbersCount.put(number, numbersCount.getOrDefault(number, 0L) + 1);
        }

        for (int i = 0; i < (part1 ? 25 : 75); i++) {
            Map<Long, Long> newNumbersCount = new HashMap<>();

            for (Map.Entry<Long, Long> entry : numbersCount.entrySet()) {
                Long number = entry.getKey();
                Long count = entry.getValue();
                List<Long> newNumbers = createdNumbers.computeIfAbsent(number, this::getCreatedNumbers);

                for (Long newNumber : newNumbers) {
                    newNumbersCount.put(newNumber, newNumbersCount.getOrDefault(newNumber, 0L) + count);
                }
            }

            numbersCount = newNumbersCount;
        }
        
        return numbersCount.values().stream().mapToLong(Long::longValue).sum();
    }

    private List<Long> getCreatedNumbers(Long number) {
        if (number == 0) return List.of(1L);
        String str = String.valueOf(number);
        if (str.length() % 2 == 0) {
            return List.of(Long.parseLong(str.substring(0, str.length() / 2)), Long.parseLong(str.substring(str.length() / 2)));
        }
        return List.of(number * 2024);
    }

}
