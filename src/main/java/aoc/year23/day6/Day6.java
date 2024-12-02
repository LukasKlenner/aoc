package aoc.year23.day6;

import aoc.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day6 implements Day {

    @Override
    public Object part1(Stream<String> linesStream) {
        List<String> lines = linesStream.toList();

        List<Integer> times = Arrays.stream(lines.get(0).substring(11).split(" ")).filter(s -> !s.isEmpty()).map(Integer::parseInt).toList();
        List<Integer> maxDistances = Arrays.stream(lines.get(1).substring(11).split(" ")).filter(s -> !s.isEmpty()).map(Integer::parseInt).toList();

        int maxTime = times.stream().mapToInt(Integer::intValue).max().orElseThrow();

        Map<Integer, Map<Integer, Integer>> timeToDistance = new HashMap<>();

        for (int i = 0; i < times.size(); i++) {
            timeToDistance.put(i, new HashMap<>());
        }

        for (int timeHold = 0; timeHold <= maxTime; timeHold++) {
            for (int raceIndex = 0; raceIndex < times.size(); raceIndex++) {

                if (timeHold > times.get(raceIndex)) {
                    continue;
                }

                int timeToDrive = times.get(raceIndex) - timeHold;
                int distance = timeHold * timeToDrive;

                timeToDistance.get(raceIndex).put(timeHold, distance);

            }
        }

        int product = 1;

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : timeToDistance.entrySet()) {

            int maxDistance = (int) entry.getValue().values().stream().filter(i -> i > maxDistances.get(entry.getKey())).count();

            if (maxDistance > 0) {
                product *= maxDistance;
            } else {
                throw new RuntimeException("No max distance found");
            }
        }

        return product;
    }

    @Override
    public Object part2(Stream<String> linesStream) {
        List<String> lines = linesStream.toList();

        List<Long> times = List.of(Long.parseLong(lines.get(0).substring(11).replaceAll(" ", "")));
        List<Long> maxDistances = List.of(Long.parseLong(lines.get(1).substring(11).replaceAll(" ", "")));

        long maxTime = times.stream().mapToLong(Long::longValue).max().orElseThrow();

        Map<Integer, Map<Long, Long>> timeToDistance = new HashMap<>();

        for (int i = 0; i < times.size(); i++) {
            timeToDistance.put(i, new HashMap<>());
        }

        for (Long timeHold = 0L; timeHold <= maxTime; timeHold++) {
            for (int raceIndex = 0; raceIndex < times.size(); raceIndex++) {

                if (timeHold > times.get(raceIndex)) {
                    continue;
                }

                Long timeToDrive = times.get(raceIndex) - timeHold;
                Long distance = timeHold * timeToDrive;

                timeToDistance.get(raceIndex).put(timeHold, distance);

            }
        }

        long product = 1L;

        for (Map.Entry<Integer, Map<Long, Long>> entry : timeToDistance.entrySet()) {

            int maxDistance = (int) entry.getValue().values().stream().filter(i -> i > maxDistances.get(entry.getKey())).count();

            if (maxDistance > 0) {
                product *= maxDistance;
            } else {
                throw new RuntimeException("No max distance found");
            }
        }

        return product;
    }
}
