package aoc.year24.day1;

import aoc.JoinedDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day1 implements JoinedDay {

    @Override
    public long run(Stream<String> stream, boolean part1) {

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        stream.map(s -> s.split(" {3}")).forEach(arr -> {
            list1.add(Integer.parseInt(arr[0]));
            list2.add(Integer.parseInt(arr[1]));
        });

        if (part1) {
            list1.sort(Integer::compareTo);
            list2.sort(Integer::compareTo);

            int result = 0;

            for (int i = 0; i < list1.size(); i++) {
                result += Math.abs(list1.get(i) - list2.get(i));
            }

            return result;
        }

        Map<Integer, Integer> counts1 = new HashMap<>();
        Map<Integer, Integer> counts2 = new HashMap<>();

        list1.forEach(i -> counts1.merge(i, 1, Integer::sum));
        list2.forEach(i -> counts2.merge(i, 1, Integer::sum));

        int result = 0;

        for (Map.Entry<Integer, Integer> entry : counts1.entrySet()) {
            result += entry.getKey() * entry.getValue() * counts2.getOrDefault(entry.getKey(), 0);
        }

        return result;
    }
}
