package aoc23;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public abstract class Framework {

    public static final int DAY = 1;
    public static final boolean useCurrentDay = true;

    public static void main(String[] args) throws Exception {

        int currentDay = useCurrentDay ? getCurrentDay() : DAY;
        Class<?> dayClass = Class.forName("aoc23.day" + currentDay + ".Day" + currentDay);
        Day day = (Day) dayClass.getConstructors()[0].newInstance();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(dayClass.getResourceAsStream("input.txt"))))) {
            System.out.println(day.run(reader.lines()));
        }

    }

    private static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

}
