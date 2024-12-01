package aoc23.day12;

import aoc23.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 implements Day {
    @Override
    public Object part1(Stream<String> lines) {

        return lines.mapToLong(line -> {

            char[] input = line.split(" ")[0].toCharArray();
            Integer[] counts = Arrays.stream(line.split(" ")[1].split(",")).map(Integer::parseInt).toArray(Integer[]::new);

            long count = getCount(input, counts, 0, 0);
            System.out.println(Arrays.toString(input) + "->" + count);

            return count;
        }).sum();
    }

    @Override
    public Object part2(Stream<String> lines) {
        return lines.mapToLong(line -> {

            char[] input = Stream.generate(() -> line.split(" ")[0]).limit(5).collect(Collectors.joining("?")).toCharArray();
            Integer[] counts = Arrays.stream(Stream.generate(() -> line.split(" ")[1]).limit(5).collect(Collectors.joining(",")).split(",")).map(Integer::parseInt).toArray(Integer[]::new);

            long count = getCount(input, counts, 0, 0);
            System.out.println(Arrays.toString(input) + "->" + count);

            return count;
        }).sum();
    }

    public long getCount(char[] input, Integer[] counts, int inputIndex, int countsIndex) {

        if (countsIndex == counts.length) return 1;

        if (input.length - inputIndex < counts.length - countsIndex) return 0;

        int initialCountsIndex = countsIndex;

        int currentLength = 0;
        boolean inSequence = false;
        StringBuilder sequenceBuilder = new StringBuilder();
        for (; inputIndex < input.length; inputIndex++) {
            if (inSequence && input[inputIndex] == '.') {
                while (inputIndex < input.length && input[inputIndex] == '.') inputIndex++;
                break;
            } else if (input[inputIndex] == '?' || input[inputIndex] == '#') {
                sequenceBuilder.append(input[inputIndex]);
                inSequence = true;
                currentLength++;
            }
        }

        char[] sequence = sequenceBuilder.toString().toCharArray();

        boolean onlyCross = true;
        for (char c : sequence) {
            if (c != '#') onlyCross = false;
        }

        if (onlyCross) {
            if (sequence.length != counts[countsIndex]) return 0;
            return getCount(input, counts, inputIndex, countsIndex + 1);
        }

        if (currentLength < counts[countsIndex]) return getCount(input, counts, inputIndex, countsIndex + 1);

        List<List<Integer>> removeCounts = new ArrayList<>();
        List<Integer> allRemoved = new ArrayList<>();
        List<Integer> distinctCount = new ArrayList<>();

        int totalLength = 0;
        int initialCurrentLength = currentLength;

        while (currentLength >= counts[countsIndex]) {
            Integer removed = counts[countsIndex++];
            allRemoved.add(removed);
            distinctCount.add((int) allRemoved.stream().distinct().count());
            currentLength--;
            currentLength -= removed;
            if (totalLength != 0) totalLength++;
            totalLength += removed;

            if (countsIndex == counts.length) break;
        }

        List<List<boolean[]>> a = new ArrayList<>();

        for (int i = initialCountsIndex; i < countsIndex; i++) {

            List<boolean[]> list = new ArrayList<>();
            int count = counts[i];

            for (int j = 0; j < sequence.length - count + 1; j++) {

                if (j + count < sequence.length && sequence[j + count] == '#') continue;

                if (j > 0 && sequence[j - 1] == '#') continue;


                boolean[] arr = new boolean[sequence.length];

                for (int k = 0; k < count; k++) {
                    arr[j + k] = true;
                }

                if (j > 0) {
                    arr[j - 1] = true;
                }

                if (j + count < sequence.length) {
                    arr[j + count] = true;
                }

                list.add(arr);
            }

            a.add(list);

        }

        if (inputIndex == input.length) {
            if (countsIndex != counts.length) return 0;

            while (a.size() > 1) combine(a);

            return a.get(0).size();
        }

        boolean onlyQuestion = true;
        for (char c : sequence) {
            if (c != '?') {
                onlyQuestion = false;
                break;
            }
        }

        long sum = onlyQuestion ? getCount(input, counts, inputIndex, initialCountsIndex) : 0;

        for (int i = initialCountsIndex + 1; i <= countsIndex; i++) {
            sum += a.get(0).size() * getCount(input, counts, inputIndex, i);
            if (a.size() > 1) combine(a);
        }

        return sum;
    }

    private void combine(List<List<boolean[]>> a) {

        List<boolean[]> first = a.get(0);
        List<boolean[]> second = a.get(1);

        List<boolean[]> result = new ArrayList<>();

        for (boolean[] arr1 : first) {
            for (boolean[] arr2 : second) {

                int lastTrue = -1;
                for (int i = 0; i < arr1.length; i++) {
                    if (arr1[i]) lastTrue = i;
                }

                int firstTrue = -1;
                for (int i = 0; i < arr2.length; i++) {
                    if (arr2[i]) {
                        firstTrue = i;
                        break;
                    }
                }

                if (firstTrue < lastTrue) continue;

                boolean[] newArr = new boolean[arr1.length];

                for (int i = 0; i < arr1.length; i++) {
                    newArr[i] = arr1[i] | arr2[i];
                }

                result.add(newArr);
            }
        }

        a.remove(0);
        a.set(0, result);

    }

    private long fac(long n) {

        if (n == 0 || n == 1) {
            return 1;
        }

        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }

        return result;
    }

//    public int getCount(char[] input, List<Integer> counts) {
//
//        if (finished(input)) {
//            return isValid(input, counts) ? 1 : 0;
//        }
//
//        int index = -1;
//
//        for (int i = 0; i < input.length; i++) {
//            if (input[i] == '?') {
//                index = i;
//                break;
//            }
//        }
//
//        char[] dot = new char[input.length];
//        System.arraycopy(input, 0, dot, 0, input.length);
//        dot[index] = '.';
//
//        char[] cross = new char[input.length];
//        System.arraycopy(input, 0, cross, 0, input.length);
//        cross[index] = '#';
//
////        System.out.println(Arrays.toString(dot) + "->" + isPossible(dot, counts) + counts.toString());
////        System.out.println(Arrays.toString(cross) + "->" + isPossible(cross, counts) + counts.toString());
//
//        return (isPossible(dot, counts) ? getCount(dot, counts) : 0) +
//                (isPossible(cross, counts) ? getCount(cross, counts) : 0);
//    }

    public boolean finished(char[] input) {
        for (char c : input) {
            if (c == '?') return false;
        }
        return true;
    }

    public boolean isPossible(char[] input, List<Integer> counts) {

        List<Integer> currentCounts = new ArrayList<>();

        int currentCount = 0;
        boolean inSequence = false;

        for (char c : input) {

            if (c == '.' && inSequence) {
                currentCounts.add(currentCount);
                currentCount = 0;
                inSequence = false;
            } else if (c == '#' || c == '?') {
                inSequence = true;
                currentCount++;
            }

        }

        if ( currentCount > 0) currentCounts.add(currentCount);

        int countsIndex = 0;
        for (int i = 0; i < currentCounts.size(); i++) {

            if (countsIndex == counts.size()) break;

            if (currentCounts.get(i).equals(counts.get(countsIndex))) {
                countsIndex++;
            } else if (currentCounts.get(i) > counts.get(countsIndex)){
                currentCounts.set(i, currentCounts.get(i) - counts.get(countsIndex) - 1);
                i--;
                countsIndex++;
            }
        }

        return countsIndex == counts.size();
    }

    public boolean isValid(char[] input, List<Integer> counts) {

        int currentSequence = 0;
        int currentCount = 0;
        boolean inSequence = false;

        for (char c : input) {

            if (c == '.' && inSequence) {
                if (currentSequence >= counts.size() || currentCount != counts.get(currentSequence)) return false;
                currentCount = 0;
                currentSequence++;
                inSequence = false;
            } else if (c == '#') {
                inSequence = true;
                currentCount++;
            }

        }

        if (inSequence) {
            if (currentSequence >= counts.size() || currentCount != counts.get(currentSequence)) return false;
            currentSequence++;
        }

        return currentSequence == counts.size();
    }
}
