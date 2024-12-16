package aoc.year24.day15;

import aoc.util.Direction;
import aoc.util.MovementBasedGridTask;
import aoc.util.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Day15 extends MovementBasedGridTask<Character> {

    public Day15() {
        super(input -> input.subList(0, input.indexOf("")), Function.identity(), Character[]::new, Character[][]::new);
    }

    @Override
    public long run(boolean part1) {

        if (!part1) {
            loadGridPart2();
            currentPos = findCharValue('@');
        }

        List<Direction> directions = getDirections();

        for (Direction dir : directions) {
            currentDir = dir;

            if (part1) movePart1();
            else movePart2();


            printGrid();
        }


        AtomicInteger sum = new AtomicInteger();

        foreachCell((pos, c) -> {
            if (c == 'O') {
                sum.addAndGet(100 * pos.y() + pos.x());
            }
        });

        return sum.get();
    }

    private void movePart1() {
        int boxes = getBoxesInFront();

        Pos nextPos = currentPos.add(currentDir);
        Pos newLastBoxPos = nextPos.add(currentDir, boxes);
        if (getValue(newLastBoxPos).equals('#')) return;

        putValue(newLastBoxPos, 'O');
        putValue(nextPos, '@');
        putValue(currentPos, '.');

        currentPos = nextPos;
    }

    private void movePart2() {

        Pos nextPos = currentPos.add(currentDir);

        if (getValue(nextPos).equals('#')) return;
        if (getValue(nextPos).equals('.')) {
            currentPos = nextPos;
            return;
        }

        if (canMoveBox(getPosOfBox(nextPos), currentDir)) {
            moveBox(getPosOfBox(nextPos), currentDir);
        }

        currentPos = nextPos;
    }

    private boolean canMoveBox(Pos boxPos, Direction dir) {
        List<Pos> positionsToCheck = switch (dir) {
            case RIGHT -> List.of(boxPos.right().right());
            case DOWN -> List.of(boxPos.down(), boxPos.right().down());
            case LEFT -> List.of(boxPos.left());
            case UP -> List.of(boxPos.up(), boxPos.right().up());
        };

        for (Pos posToCheck : positionsToCheck) {
            Character value = getValue(posToCheck);
            if (value.equals('#')) return false;
            else if (value.equals(']') || value.equals('[')) {
                return canMoveBox(getPosOfBox(posToCheck), dir);
            }
        }

        return true;
    }

    private void moveBox(Pos boxPos, Direction dir) {

        List<Pos> positionsToCheck = switch (dir) {
            case RIGHT -> List.of(boxPos.right().right());
            case DOWN -> List.of(boxPos.down(), boxPos.right().down());
            case LEFT -> List.of(boxPos.left());
            case UP -> List.of(boxPos.up(), boxPos.right().up());
        };

        putValue(boxPos, '.');
        putValue(boxPos.right(), '.');

        for (Pos posToCheck : positionsToCheck) {
            Character value = getValue(posToCheck);
            if (value.equals(']') || value.equals('[')) {
                moveBox(getPosOfBox(posToCheck), dir);
            }
        }

        putValue(boxPos.add(dir), '[');
        putValue(boxPos.right().add(dir), ']');
    }

    private Pos getPosOfBox(Pos pos) {
        if (getValue(pos).equals('[')) return pos;
        else return pos.left();
    }

    private int getBoxesInFront() {
        int boxes = 0;
        Pos pos = currentPos.add(currentDir);

        while (getValue(pos) == 'O') {
            pos = pos.add(currentDir);
            boxes++;
        }

        return boxes;
    }

    private List<Direction> getDirections() {
        List<Direction> directions = new ArrayList<>();

        for (int i = input.indexOf("") + 1; i < input.size(); i++) {
            for (char c : input.get(i).toCharArray()) {
                directions.add(Direction.fromArrow(c));
            }
        }

        return directions;
    }

    @Override
    protected Pos getStartPos() {
        return findCharValue('@');
    }

    @Override
    protected Direction getStartDir() {
        return null;
    }

    @Override
    protected boolean canMove() {
        return false;
    }

    protected void loadGridPart2() {
        grid = new Character[gridInput.size()][];

        for (int y = 0; y < gridInput.size(); y++) {
            String line = gridInput.get(y);
            grid[y] = new Character[line.length() * 2];

            for (int x = 0; x < line.length(); x++) {
                char first = line.charAt(x);
                char second = line.charAt(x);

                if (first == 'O') {
                    first = '[';
                    second = ']';
                } else if (first == '@') {
                    second = '.';
                }

                grid[y][2 * x] = first;
                grid[y][2 * x + 1] = second;
            }
        }

    }
}
