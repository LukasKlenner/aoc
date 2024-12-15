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
        List<Direction> directions = getDirections();

        for (Direction dir : directions) {
            currentDir = dir;
            int boxes = getBoxesInFront();


            /*printGrid();
            System.out.println("Boxes: " + boxes);
            System.out.println("Dir: " + currentDir);
*/
            Pos nextPos = currentPos.add(currentDir);
            Pos newLastBoxPos = nextPos.add(currentDir, boxes);
            if (getValue(newLastBoxPos).equals('#')) continue;

            putValue(newLastBoxPos, 'O');
            putValue(nextPos, '@');
            putValue(currentPos, '.');

            currentPos = nextPos;
        }

        //printGrid();

        AtomicInteger sum = new AtomicInteger();

        foreachCell((pos, c) -> {
            if (c == 'O') {
                sum.addAndGet(100 * pos.y() + pos.x());
            }
        });

        return sum.get();
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
}
