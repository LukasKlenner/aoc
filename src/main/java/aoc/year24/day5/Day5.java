package aoc.year24.day5;

import aoc.JoinedDay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 implements JoinedDay {

    @Override
    public long run(Stream<String> stream, boolean part1) {

        Iterator<String> input = stream.iterator();

        Map<Integer, Set<Integer>> greaterMap = new HashMap<>();

        String line;
        while (!(line = input.next()).isEmpty()) {
            String[] numbers = line.split("\\|");
            greaterMap.computeIfAbsent(Integer.parseInt(numbers[1]), k -> new HashSet<>()).add(Integer.parseInt(numbers[0]));
        }

        int result = 0;

        while (input.hasNext()) {

            line = input.next();

            List<Integer> page = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());

            boolean changed = false;
            for (int i = 0; i < page.size(); i++) {
                for (int j = 0; j < i; j++) {
                    if (greaterMap.containsKey(page.get(j)) && greaterMap.get(page.get(j)).contains(page.get(i))) {
                        page.add(j, page.remove(i));
                        changed = true;
                    }
                }
            }

            if ((part1 && !changed) || (!part1 && changed)) result += page.get(page.size() / 2);
        }

        return result;
    }
}
