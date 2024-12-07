package aoc.year23.day11;

import aoc.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day11 implements Day {
    @Override
    public long part1(Stream<String> linesStream) {
        return run(linesStream, true);
    }

    @Override
    public long part2(Stream<String> linesStream) {
        return run(linesStream, false);
    }

    private long run(Stream<String> linesStream, boolean part1) {
        List<String> lines = linesStream.toList();

        boolean[] emptyRows = getEmptyRows(lines);
        boolean[] emptyColumns = getEmptyColumns(lines);
        List<Pos> positions = getPositions(lines);

        long length = 0;
        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                length += positions.get(i).distance(positions.get(j), emptyRows, emptyColumns, part1 ? 2 : 1000000);
            }
        }

        return length;
    }

    private boolean[] getEmptyRows(List<String> lines) {
        boolean[] emptyRows = new boolean[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            emptyRows[i] = !lines.get(i).contains("#");
        }

        return emptyRows;
    }

    private boolean[] getEmptyColumns(List<String> lines) {
        boolean[] emptyColumns = new boolean[lines.get(0).length()];

        for (int i = 0; i < lines.get(0).length(); i++) {
            boolean empty = true;
            for (String line : lines) {
                if (line.charAt(i) != '.') {
                    empty = false;
                    break;
                }
            }
            emptyColumns[i] = empty;
        }

        return emptyColumns;
    }

    private List<Pos> getPositions(List<String> lines) {
        List<Pos> positions = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    positions.add(new Pos(j, i));
                }
            }
        }

        return positions;
    }

    private record Pos(int x, int y) {

        public long distance(Pos other, boolean[] emptyRows, boolean[] emptyColumns, int replaceCount) {

            int minX = Math.min(x, other.x);
            int maxX = Math.max(x, other.x);
            int minY = Math.min(y, other.y);
            int maxY = Math.max(y, other.y);

            long distance = 0;

            for (int i = minX; i < maxX; i++) {
                if (!emptyColumns[i]) {
                    distance++;
                } else{
                    distance += replaceCount;
                }
            }

            for (int i = minY; i < maxY; i++) {
                if (!emptyRows[i]) {
                    distance++;
                } else{
                    distance += replaceCount;
                }
            }

            return distance;

        }

    }
}
