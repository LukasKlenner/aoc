package aoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Framework {

    public static final int DAY = 10;
    public static final int YEAR = 2024;

    public static final boolean useCurrentDay = true;
    public static final boolean useCurrentYear = true;

    public static void main(String[] args) throws Exception {

        int currentDay = useCurrentDay ? getCurrentDay() : DAY;
        String currentYear = Integer.toString(useCurrentYear ? getCurrentYear() : YEAR).substring(2, 4);

        Class<?> dayClass = Class.forName("aoc.year" + currentYear + ".day" + currentDay + ".Day" + currentDay);
        Day day = (Day) dayClass.getConstructors()[0].newInstance();

        System.out.println("Running year " + currentYear + " day " + currentDay);

        long startTime = System.nanoTime();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(dayClass.getResourceAsStream("input.txt"))))) {
            System.out.println("Part1: " + day.part1(reader.lines()));
        }
        System.out.printf("Time: %.2fms\n", (System.nanoTime() - startTime) / 1e6);
        System.out.println();

        startTime = System.nanoTime();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(dayClass.getResourceAsStream("input.txt"))))) {
            System.out.println("Part2: " + day.part2(reader.lines()));
        }
        System.out.printf("Time: %.2fms\n", (System.nanoTime() - startTime) / 1e6);
    }

    private static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    private static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }

}
