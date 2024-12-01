package aoc23.day13;

import aoc23.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day13 implements Day {

    @Override
    public Object part1(Stream<String> lines) {
        return run(lines, true);
    }

    @Override
    public Object part2(Stream<String> lines) {
        return run(lines, false);
    }

    private int run(Stream<String> linesStream, boolean part1) {
        List<String> lines = linesStream.toList();

        List<List<List<Character>>> inputs = new ArrayList<>();

        inputs.add(new ArrayList<>());
        List<List<Character>> current = inputs.get(0);

        for (String line : lines) {

            if (line.isEmpty()) {
                current = new ArrayList<>();
                inputs.add(current);
            } else {
                current.add(line.chars().mapToObj(i -> (char) i).toList());
            }
        }

        int sum = 0;

        for (List<List<Character>> input : inputs) {
            sum += process(input, part1);
        }


        return sum;
    }

    private int process(List<List<Character>> input, boolean part1) {

        columns: for (int i = 1; i < input.get(0).size(); i++) {

            int smudgeCount = 0;
            int lastRow = -1;
            int lastColumn = -1;
            int iteration = Math.min(i, input.get(0).size() - i);

            for (int j = 0; j < iteration; j++) {

                int leftColumn = i - j - 1;
                int rightColumn = i + j;

                for (int k = 0; k < input.size(); k++) {

                    if (input.get(k).get(leftColumn) != input.get(k).get(rightColumn)) {

                        if (part1) continue columns;

                        if (smudgeCount == 1) continue columns;
                        smudgeCount++;
                        lastRow = k;
                        lastColumn = leftColumn;
                    }

                }

            }

            if (part1) return i;
            if (smudgeCount == 1) {
                System.out.printf("ColumnMirror: row: %d, column: %d, mirror: %d\n", lastRow, lastColumn, i);
                return i;
            }


        }

        rows: for (int i = 1; i < input.size(); i++) {

            int smudgeCount = 0;
            int iteration = Math.min(i, input.size() - i);
            int lastRow = -1;
            int lastColumn = -1;

            for (int j = 0; j < iteration; j++) {

                int leftRow = i - j - 1;
                int rightRow = i + j;

                for (int k = 0; k < input.get(0).size(); k++) {

                    if (input.get(leftRow).get(k) != input.get(rightRow).get(k)) {

                        if (part1) continue rows;

                        if (smudgeCount == 1) continue rows;
                        smudgeCount++;
                        lastRow = leftRow;
                        lastColumn = k;
                    }

                }

            }

            if (part1) return 100 * i;
            if (smudgeCount == 1) {
                System.out.printf("RowMirror: row: %d, column: %d, mirror: %d\n", lastRow, lastColumn, i);
                return 100 * i;
            }

        }

        throw new RuntimeException("No mirroring");
    }
}
