package aoc.year23.day3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Character.isDigit;

public class Day3 implements aoc.Day {


    @Override
    public Object part1(Stream<String> lines) {

        char[][] arr = lines.map(String::toCharArray).toArray(char[][]::new);
        int sum = 0;

        for (int i = 0; i < arr.length; i++) {
            StringBuilder sb = new StringBuilder();
            boolean adjacentToSymbol = false;

            for (int j = 0; j < arr[i].length; j++) {

                char c = arr[i][j];

                if (isDigit(c)) {
                    sb.append(c);
                    if (!adjacentToSymbol && isAdjacentToSymbol(arr, i, j)) {
                        adjacentToSymbol = true;
                    }
                } else if (!isDigit(c) && !sb.isEmpty()) {
                    if (adjacentToSymbol) {
                        sum += Integer.parseInt(sb.toString());
                    }
                    sb = new StringBuilder();
                    adjacentToSymbol = false;
                }

            }

            if (!sb.isEmpty() && adjacentToSymbol) {
                sum += Integer.parseInt(sb.toString());
            }
        }

        return sum;
    }

    @Override
    public Object part2(Stream<String> lines) {

        char[][] arr = lines.map(String::toCharArray).toArray(char[][]::new);
        int[][] adjacentNumbers = new int[arr.length][arr[0].length];
        int[][] product = new int[arr.length][arr[0].length];

        for (int i = 0; i < product.length; i++) {
            for (int j = 0; j < product[0].length; j++) {
                product[i][j] = 1;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            StringBuilder sb = new StringBuilder();
            boolean adjacentToSymbol = false;
            Set<Point> dependents = new HashSet<>();

            for (int j = 0; j < arr[i].length; j++) {

                char c = arr[i][j];

                if (isDigit(c)) {
                    sb.append(c);
                    dependents.addAll(getAdjacentPoints(i, j, arr.length, arr[0].length).stream().filter(p -> arr[p.x()][p.y()] == '*').toList());
                    if (!adjacentToSymbol && isAdjacentToSymbol(arr, i, j)) {
                        adjacentToSymbol = true;
                    }
                } else if (!isDigit(c) && !sb.isEmpty()) {
                    if (adjacentToSymbol) {

                        for (Point p : dependents) {
                            product[p.x()][p.y()] *= Integer.parseInt(sb.toString());
                            adjacentNumbers[p.x()][p.y()]++;
                        }

                    }

                    dependents.clear();
                    sb = new StringBuilder();
                    adjacentToSymbol = false;
                }

            }

            if (!sb.isEmpty() && adjacentToSymbol) {
                for (Point p : dependents) {
                    product[p.x()][p.y()] *= Integer.parseInt(sb.toString());
                    adjacentNumbers[p.x()][p.y()]++;
                }
            }
        }

        long sum = 0;

        for (int i = 0; i < product.length; i++) {
            for (int j = 0; j < product[0].length; j++) {
                if (adjacentNumbers[i][j] == 2) {
                    sum += product[i][j];
                }
            }
        }

        return sum;
    }



    private boolean isAdjacentToSymbol(char[][] list, int i, int j) {

        return getAdjacentPoints(i, j, list.length, list[0].length).stream().anyMatch(p -> isSymbol(list[p.x()][p.y()]));
    }

    private List<Point> getAdjacentPoints(int i, int j, int maxHeight, int maxWidth) {
        List<Point> points = new ArrayList<>(8);

        for (int k = Math.max(i - 1, 0); k < Math.min(i + 2, maxHeight); k++) {
            points.add(new Point(k, Math.max(j - 1, 0)));
        }

        for (int k = Math.max(i - 1, 0); k < Math.min(i + 2, maxHeight); k++) {
            if (k != i) points.add(new Point(k, j));
        }

        for (int k = Math.max(i - 1, 0); k < Math.min(i + 2, maxHeight); k++) {
            points.add(new Point(k, Math.min(j + 1, maxWidth - 1)));
        }

        return points;
    }

    private boolean isSymbol(char c) {
        return !Character.isDigit(c) && c != '.';
    }

    record Point(int x, int y) {
    }
}