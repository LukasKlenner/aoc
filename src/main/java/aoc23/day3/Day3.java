package aoc23.day3;

import aoc23.Day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day3 implements Day {


    @Override
    public Object part1(Stream<String> lines) {

        Character[][] arr = lines.map(s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new)).toArray(Character[][]::new);
        int sum = 0;

        for (int i = 0; i < arr.length; i++) {
            int currentLength = arr[i].length;
            StringBuilder sb = new StringBuilder();
            boolean building = false;
            boolean adjacentToSymbol = false;

            for (int j = 0; j < currentLength; j++) {

                Character c = arr[i][j];

                if (isDigit(c)) {
                    sb.append(c);
                    building = true;
                    if (!adjacentToSymbol && isAdjacentToSymbol(arr, i, j)) {
                        adjacentToSymbol = true;
                    }
                } else if (!isDigit(c) && building) {
                    if (adjacentToSymbol) {
                        sum += Integer.parseInt(sb.toString());
                    }
                    sb = new StringBuilder();
                    building = false;
                    adjacentToSymbol = false;
                }

            }

            if (building && adjacentToSymbol) {
                sum += Integer.parseInt(sb.toString());
            }
        }

        return sum;
    }

    @Override
    public Object part2(Stream<String> lines) {

        Character[][] arr = lines.map(s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new)).toArray(Character[][]::new);
        int[][] adjacentNumbers = new int[arr.length][arr[0].length];
        int[][] product = new int[arr.length][arr[0].length];

        for (int i = 0; i < product.length; i++) {
            for (int j = 0; j < product[0].length; j++) {
                product[i][j] = 1;
            }
        }

        for (int i = 0; i < arr.length; i++) {
            int currentLength = arr[i].length;
            StringBuilder sb = new StringBuilder();
            boolean building = false;
            boolean adjacentToSymbol = false;
            Set<Point> dependents = new HashSet<>();

            for (int j = 0; j < currentLength; j++) {

                Character c = arr[i][j];

                if (isDigit(c)) {
                    sb.append(c);
                    building = true;
                    dependents.addAll(getAdjacentPoints(i, j, arr.length, arr[0].length).stream().filter(p -> arr[p.x()][p.y()].equals('*')).toList());
                    if (!adjacentToSymbol && isAdjacentToSymbol(arr, i, j)) {
                        adjacentToSymbol = true;
                    }
                } else if (!isDigit(c) && building) {
                    if (adjacentToSymbol) {

                        for (Point p : dependents) {
                            product[p.x()][p.y()] *= Integer.parseInt(sb.toString());
                            adjacentNumbers[p.x()][p.y()]++;
                        }

                    }

                    dependents.clear();
                    sb = new StringBuilder();
                    building = false;
                    adjacentToSymbol = false;
                }

            }

            if (building && adjacentToSymbol) {
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



    private boolean isAdjacentToSymbol(Character[][] list, int i, int j) {

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

    private boolean isSymbol(Character c) {
        return !Character.isDigit(c) && c != '.';
    }

    private boolean isDigit(Character c) {
        return Character.isDigit(c);
    }

    record Point(int x, int y) {
    }
}