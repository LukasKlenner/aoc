package aoc.year24.day7;

import aoc.JoinedDay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day7 implements JoinedDay {

    @Override
    public long run(Stream<String> stream, boolean part1) {

        List<String> lines = stream.toList();
        long result = 0;
        List<Operator> possibleOperators = part1 ? List.of(Operator.PLUS, Operator.MULTIPLY) : List.of(Operator.PLUS, Operator.MULTIPLY, Operator.CONCAT);

        for (String line : lines) {

            String[] arr = line.split(": ");

            long target = Long.parseLong(arr[0]);
            long[] numbers = Arrays.stream(arr[1].split(" ")).mapToLong(Long::parseLong).toArray();

            Operator[] operators = new Operator[numbers.length - 1];
            Arrays.fill(operators, Operator.PLUS);

            if (checkOperatorsRecursive(target, numbers, operators, 0, possibleOperators)) {
                result += target;
            }
        }

        return result;
    }

    private boolean checkOperatorsRecursive(long target, long[] numbers, Operator[] operators, int index, List<Operator> possibleOperators) {
        if (eval(numbers, operators) == target) return true;

        if (index == operators.length) return false;

        for (Operator operator : possibleOperators) {
            Operator[] copy = new Operator[operators.length];
            System.arraycopy(operators, 0, copy, 0, operators.length);
            copy[index] = operator;
            if (checkOperatorsRecursive(target, numbers, copy, index + 1, possibleOperators)) return true;
        }

        return false;

    }

    private long eval(long[] numbers, Operator[] operators) {

        long acc = numbers[0];

        for (int i = 0; i < operators.length; i++) {
            acc = operators[i].eval(acc, numbers[i + 1]);
        }

        return acc;
    }

    private enum Operator {

        PLUS,
        MULTIPLY,
        CONCAT;

        public long eval(long num1, long num2) {
            return switch (this) {
                case PLUS -> num1 + num2;
                case MULTIPLY -> num1 * num2;
                case CONCAT -> (num1 * ((long) Math.pow(10, Math.floor(Math.log10(num2)) + 1))) + num2;
            };
        }

    }

}
