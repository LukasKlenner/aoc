package aoc.year24.day3;

import aoc.JoinedDay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 implements JoinedDay {

    @Override
    public Object run(Stream<String> stream, boolean part1) {

        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\)");
        Matcher matcher = pattern.matcher(stream.collect(Collectors.joining("")));

        long res = 0;
        boolean enabled = true;

        while (matcher.find()) {
            String match = matcher.group();

            if (match.equals("do()")) {
                enabled = true;
            } else if (match.equals("don't()")) {
                enabled = false;
            } else if (enabled || part1) {
                String[] numbers = match.substring(4, match.length() - 1).split(",");
                res += (long) Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
            }
        }

        return res;
    }

}
