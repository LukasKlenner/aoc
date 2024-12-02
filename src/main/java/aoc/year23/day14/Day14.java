package aoc.year23.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Day14 implements aoc.Day {
    @Override
    public Object part1(Stream<String> lines) {

        Character[][] input = lines.map(str -> str.chars().mapToObj(i -> (char) i).toArray(Character[]::new)).toArray(Character[][]::new);
        boolean[][] blocked = new boolean[input.length][input[0].length];
        boolean[][] rocks = new boolean[input.length][input[0].length];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                if (input[i][j] == '#') blocked[i][j] = true;
                if (input[i][j] == 'O') rocks[i][j] = true;
            }
        }

        tilt(blocked, rocks, new Pos(0, -1));

        return getWeight(rocks, (x, y) -> input.length - y);
    }

    @Override
    public Object part2(Stream<String> lines) {

        Character[][] input = lines.map(str -> str.chars().mapToObj(i -> (char) i).toArray(Character[]::new)).toArray(Character[][]::new);
        boolean[][] blocked = new boolean[input.length][input[0].length];
        boolean[][] rocks = new boolean[input.length][input[0].length];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                if (input[i][j] == '#') blocked[i][j] = true;
                if (input[i][j] == 'O') rocks[i][j] = true;
            }
        }

        List<boolean[][]> allRocks = new ArrayList<>();

        while (true) {

            boolean[][] currentRocks = arrayDeepCopy(rocks);
            allRocks.add(currentRocks);
            tilt(blocked, rocks, new Pos(0, -1));
            tilt(blocked, rocks, new Pos(-1, 0));
            tilt(blocked, rocks, new Pos(0, 1));
            tilt(blocked, rocks, new Pos(1, 0));

            for (int i = allRocks.size() - 1; i > 0; i--) {

                if (Arrays.deepEquals(allRocks.get(i), rocks)) {
                    int cycleLength = allRocks.size() - i;
                    int remaining = 1000000000 - i;
                    int additional = remaining % cycleLength;

                    return getWeight(allRocks.get(i + additional), (x, y) -> input.length - y);
                }

            }

        }
    }

    private boolean[][] arrayDeepCopy(boolean[][] original) {

        boolean[][] copy = new boolean[original.length][original[0].length];

        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[0].length);
        }

        return copy;
    }

    private void tilt(boolean[][] blocked, boolean[][] rocks, Pos dir) {

        int startY = dir.y == 1 ? blocked.length - 1: 0;
        int startX = dir.x == 1 ? blocked[0].length - 1: 0;

        int endY = startY == 0 ? blocked.length : -1;
        int endX = startX == 0 ? blocked[0].length : -1;

        int dy = startY == 0 ? 1 : -1;
        int dx = startX == 0 ? 1 : -1;

        int maxY = blocked.length;
        int maxX = blocked[0].length;

        for (int y = startY; y != endY ; y += dy) {
            for (int x = startX; x != endX ; x += dx) {

                if (!rocks[y][x]) continue;

                Pos currentPos = new Pos(x, y);
                currentPos.add(dir);

                while (currentPos.inside(maxX, maxY) && !blocked[currentPos.y][currentPos.x] && !rocks[currentPos.y][currentPos.x]) {
                    currentPos.add(dir);
                }

                currentPos.sub(dir);

                rocks[y][x] = false;
                rocks[currentPos.y][currentPos.x] = true;
            }
        }
    }

    private int getWeight(boolean[][] rocks, BiFunction<Integer, Integer, Integer> weight) {

        int totalWeight = 0;

        for (int y = 0; y < rocks.length; y++) {
            for (int x = 0; x < rocks[0].length; x++) {

                if (!rocks[y][x]) continue;

                totalWeight += weight.apply(x, y);
            }
        }

        return totalWeight;
    }
    private static class Pos {
        public int x;

        public int y;

        private Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void add(Pos pos) {
            x += pos.x;
            y += pos.y;
        }

        public void sub(Pos pos) {
            x -= pos.x;
            y -= pos.y;
        }
        public boolean inside(int maxX, int maxY) {
            return x >= 0 && y >= 0 && x < maxX && y < maxY;
        }

    }
}
