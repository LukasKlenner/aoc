package aoc.year24.day21;

import aoc.Day;
import aoc.util.Direction;
import aoc.util.Pair;
import aoc.util.Pos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;

public class Day21 implements Day {

    private final Map<Integer, Pos> numberKeypad = new HashMap<>();
    private final Map<Integer, Pos> directionalKeypad = new HashMap<>();

    private final Map<Pair<Pos, Integer>, Long> cache = new HashMap<>();

    @Override
    public long part1(Stream<String> lines) {
        return run(lines, 2);
    }

    @Override
    public long part2(Stream<String> lines) {
        return run(lines, 25);
    }

    public long run(Stream<String> stream, int count) {

        cache.clear();

        numberKeypad.put(7, new Pos(0, 0));
        numberKeypad.put(8, new Pos(1, 0));
        numberKeypad.put(9, new Pos(2, 0));

        numberKeypad.put(4, new Pos(0, 1));
        numberKeypad.put(5, new Pos(1, 1));
        numberKeypad.put(6, new Pos(2, 1));

        numberKeypad.put(1, new Pos(0, 2));
        numberKeypad.put(2, new Pos(1, 2));
        numberKeypad.put(3, new Pos(2, 2));

        numberKeypad.put(0, new Pos(1, 3));
        numberKeypad.put(-1, new Pos(2, 3));


        directionalKeypad.put(Direction.UP.ordinal(), new Pos(1, 0));
        directionalKeypad.put(Direction.LEFT.ordinal(), new Pos(0, 1));
        directionalKeypad.put(Direction.DOWN.ordinal(), new Pos(1, 1));
        directionalKeypad.put(Direction.RIGHT.ordinal(), new Pos(2, 1));
        directionalKeypad.put(-1, new Pos(2, 0));

        List<String> input = stream.toList();
        long result = 0L;

        for (String code : input) {

            Stack<Pos> currentPos = new Stack<>();

            for (int i = 0; i < count; i++) {
                currentPos.push(directionalKeypad.get(-1));
            }

            currentPos.push(numberKeypad.get(-1));

            long movesCount = 0;

            for (Integer toPress : code.chars().mapToObj(c -> c == 'A' ? -1 : Character.getNumericValue(c)).toList()) {
                movesCount += makeParentEnter(numberKeypad.get(toPress), currentPos, true);
            }

            result += movesCount * Integer.parseInt(code.substring(0, code.length() - 1));
        }

        return result;
    }

    private long moveTo(Pos currentPos, Pos targetPos, Stack<Pos> currentPositions, boolean numberPad) {

        if (currentPos.equals(targetPos)) return 0;

        Pos diff = currentPos.sub(targetPos);
        long movesCount = 0;

        if (numberPad) {
            if (currentPos.x() == 0 && targetPos.y() == 3) {
                movesCount += moveRight(currentPositions, diff);
                movesCount += moveDown(currentPositions, diff);
            } else if (currentPos.y() == 3 && targetPos.x() == 0) {
                movesCount += moveUp(currentPositions, diff);
                movesCount += moveLeft(currentPositions, diff);
            } else {
                movesCount += moveLeft(currentPositions, diff);
                movesCount += moveDown(currentPositions, diff);
                movesCount += moveUp(currentPositions, diff);
                movesCount += moveRight(currentPositions, diff);
            }
        } else {
            if (currentPos.x() == 0 && targetPos.y() == 0) {
                movesCount += moveRight(currentPositions, diff);
                movesCount += moveUp(currentPositions, diff);
            } else if (currentPos.y() == 0 && targetPos.x() == 0) {
                movesCount += moveDown(currentPositions, diff);
                movesCount += moveLeft(currentPositions, diff);
            } else {
                movesCount += moveLeft(currentPositions, diff);
                movesCount += moveDown(currentPositions, diff);
                movesCount += moveUp(currentPositions, diff);
                movesCount += moveRight(currentPositions, diff);
            }
        }

        return movesCount;
    }

    private long moveUp(Stack<Pos> currentPositions, Pos diff) {
        long movesCount = 0;
        if (diff.y() > 0) {
            for (int y = 0; y < diff.y(); y++) {
                movesCount += move(Direction.UP, currentPositions);
            }
        }
        return movesCount;
    }

    private long moveRight(Stack<Pos> currentPositions, Pos diff) {
        long movesCount = 0;
        if (diff.x() < 0) {
            for (int x = 0; x < -diff.x(); x++) {
                movesCount += move(Direction.RIGHT, currentPositions);
            }
        }
        return movesCount;
    }

    private long moveLeft(Stack<Pos> currentPositions, Pos diff) {
        long movesCount = 0;
        if (diff.x() > 0) {
            for (int x = 0; x < diff.x(); x++) {
                movesCount += move(Direction.LEFT, currentPositions);
            }
        }
        return movesCount;
    }

    private long moveDown(Stack<Pos> currentPositions, Pos diff) {
        long movesCount = 0;
        if (diff.y() < 0) {
            for (int y = 0; y < -diff.y(); y++) {
                movesCount += move(Direction.DOWN, currentPositions);
            }
        }
        return movesCount;
    }

    private long move(Direction dir, Stack<Pos> currentPositions) {
        if (currentPositions.isEmpty()) return 1;
        return makeParentEnter(dir, currentPositions);
    }

    private long makeParentEnter(Direction dir, Stack<Pos> currentPositions) {
        if (currentPositions.isEmpty()) {
            return 1;
        }

        return makeParentEnter(directionalKeypad.get(dir.ordinal()), currentPositions, false);
    }

    private long makeParentEnter(Pos parentTargetPos, Stack<Pos> currentPositions, boolean numberPad) {
        if (currentPositions.isEmpty()) {
            return 1;
        }

        Pos parentCurrentPos = currentPositions.pop();

        boolean updateCache = false;
        Pair<Pos, Integer> key = new Pair<>(parentTargetPos, currentPositions.size());
        if (parentCurrentPos.equals(new Pos(2, 0))) {

            updateCache = true;
            if (cache.containsKey(key)) {
                currentPositions.push(parentTargetPos);
                return cache.get(key);
            }
        }

        long movesCount = moveTo(parentCurrentPos, parentTargetPos, currentPositions, numberPad);
        movesCount += makeParentEnter(directionalKeypad.get(-1), currentPositions, false);

        if (updateCache) cache.put(key, movesCount);

        currentPositions.push(parentTargetPos);

        return movesCount;
    }

}
