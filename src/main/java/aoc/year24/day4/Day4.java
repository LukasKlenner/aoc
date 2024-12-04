package aoc.year24.day4;

import aoc.JoinedDay;
import aoc.util.Pos;

import java.util.stream.Stream;

public class Day4 implements JoinedDay {

    @Override
    public Object run(Stream<String> stream, boolean part1) {

        int[][] arr = stream.map(s -> s.chars().map(c -> switch (c) {
            case 'X' -> 0;
            case 'M' -> 1;
            case 'A' -> 2;
            case 'S' -> 3;
            default -> -2;
        }).toArray()).toArray(int[][]::new);

        int count = 0;

        for (int y = 0; y < arr.length; y++) {
            for (int x = 0; x < arr[y].length; x++) {

                if (part1) {
                    if (arr[y][x] == 0 || arr[y][x] == 3) {
                        if (checkForStreak(new Pos(x, y), 1, 0, 4, arr)) count++;
                        if (checkForStreak(new Pos(x, y), 1, 1, 4, arr)) count++;
                        if (checkForStreak(new Pos(x, y), 1, -1, 4, arr)) count++;
                        if (checkForStreak(new Pos(x, y), 0, -1, 4, arr)) count++;
                    }
                } else {
                    if (arr[y][x] == 1 || arr[y][x] == 3) {
                        if (checkForStreak(new Pos(x, y), 1, -1, 3, arr) && checkForStreak(new Pos(x + 2, y), -1, -1, 3, arr)) count++;
                    }
                }

            }
        }

        return count;
    }

    private boolean checkForStreak(Pos pos, int dx, int dy, int length, int[][] arr) {
        if (!pos.isInbounds(arr)) return false;

        int startValue = pos.getValue(arr);

        pos.add(dx, dy);
        if (!pos.isInbounds(arr)) return false;

        int currentValue = pos.getValue(arr);
        int diff = currentValue - startValue;

        if (Math.abs(diff) != 1) return false;

        for (int i = 0; i < length - 2; i++) {
            pos.add(dx, dy);
            if (!pos.isInbounds(arr)) return false;

            if (pos.getValue(arr) - currentValue != diff) return false;
            currentValue = pos.getValue(arr);
        }

        return true;
    }

}
