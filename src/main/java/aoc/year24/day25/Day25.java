package aoc.year24.day25;

import aoc.JoinedDay;
import aoc.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Day25 implements JoinedDay {

    @Override
    public long run(Stream<String> stream, boolean part1) {

        Pair<List<Key>, List<Lock>> input = parse(stream.toList());

        List<Key> keys = input.first;
        List<Lock> locks = input.second;

        keys.sort(Comparator.comparingInt(k -> k.max));

        long result = 0;

        locks: for (Lock lock : locks) {
            keys: for (Key key : keys) {

                if (lock.min + key.max > 5) continue locks;

                for (int i = 0; i < 5; i++) {
                    if (lock.heights[i] + key.heights[i] > 5) continue keys;
                }

                result++;
            }
        }

        return result;
    }

    private Pair<List<Key>, List<Lock>> parse(List<String> input) {

        List<Key> keys = new ArrayList<>();
        List<Lock> locks = new ArrayList<>();

        for (int i = 0; i < input.size(); i += 8) {
            String line = input.get(i);

            boolean isKey = line.equals(".....");
            char heightOf = isKey ? '.' : '#';

            int[] heights = new int[5];

            for (int y = i + 1; y < i + 6; y++) {
                line = input.get(y);
                for (int x = 0; x < 5; x++) {
                    if (line.charAt(x) == heightOf) heights[x]++;
                }
            }

            if (isKey) {
                for (int j = 0; j < heights.length; j++) {
                    heights[j] = 5 - heights[j];
                }
            }

            if (isKey) keys.add(new Key(heights));
            else locks.add(new Lock(heights));
        }

        return new Pair<>(keys, locks);
    }

    private static class Key {

        final int[] heights;
        final int max;

        public Key(int[] heights) {
            this.heights = heights;
            max = Arrays.stream(heights).max().getAsInt();
        }

    }

    private static class Lock {

        final int[] heights;
        final int min;

        public Lock(int[] heights) {
            this.heights = heights;
            min = Arrays.stream(heights).min().getAsInt();
        }
    }

}
