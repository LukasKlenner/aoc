package aoc.util;

import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public abstract class MovementBasedGridTask<T> extends JoinedGridTask<T> {

    protected Pos currentPos;
    protected Direction currentDir;

    private final Stack<Pos> storedPositions = new Stack<>();
    private final Stack<Direction> storedDirs = new Stack<>();

    public MovementBasedGridTask(Function<Character, T> gridParser, Function<Integer, T[]> arrayFct, Function<Integer, T[][]> arrayFct2D) {
        super(gridParser, arrayFct, arrayFct2D);
    }

    public MovementBasedGridTask(Function<List<String>, List<String>> gridInputFct, Function<Character, T> gridParser, Function<Integer, T[]> arrayFct, Function<Integer, T[][]> arrayFct2D) {
        super(gridInputFct, gridParser, arrayFct, arrayFct2D);
    }

    @Override
    public long part1() {
        currentPos = getStartPos();
        currentDir = getStartDir();
        return super.part1();
    }

    @Override
    public long part2() {
        currentPos = getStartPos();
        currentDir = getStartDir();
        return super.part2();
    }

    protected abstract Pos getStartPos();

    protected abstract Direction getStartDir();

    protected abstract boolean canMove();

    protected boolean isInBounds() {
        return isInBounds(currentPos);
    }

    protected void move() {
        currentPos = currentPos.add(currentDir);
    }

    public void moveUntilBarrier() {
        while (canMove()) {
            move();
        }
    }

    protected void rotate90() {
        currentDir = currentDir.rotate90();
    }

    protected void storeCurrentLocation() {
        storedPositions.push(currentPos);
        storedDirs.push(currentDir);
    }

    protected void loadLastLocation() {
        currentPos = storedPositions.pop();
        currentDir = storedDirs.pop();
    }

}
