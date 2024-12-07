package aoc.year24.day6;

import aoc.util.Direction;
import aoc.util.MovementBasedGridTask;
import aoc.util.Pos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class Day6 extends MovementBasedGridTask<Boolean> {

    public Day6() {
        super(c -> c == '#', Boolean[]::new, Boolean[][]::new);
    }

    @Override
    public long run(boolean part1) {
        Map<Pos, Set<Direction>> visited = new HashMap<>();

        if (part1) {
            tracePath(visited, () -> {});
            return visited.size();
        } else {
            Set<Pos> loopPositions = new HashSet<>();

            tracePath(visited, () -> {

                Pos obstaclePos = currentPos.add(currentDir);

                if (loopPositions.contains(obstaclePos) || !isInBounds(obstaclePos) || getValue(obstaclePos) || visited.containsKey(obstaclePos)) {
                    return;
                }

                Map<Pos, Set<Direction>> visitedCopy = copyVisited(visited);

                storeCurrentLocation();
                Boolean old = putValue(obstaclePos, true);

                if (tracePath(visitedCopy, () -> {})) {
                    loopPositions.add(obstaclePos);
                }

                loadLastLocation();
                putValue(obstaclePos, old);
            });

            return loopPositions.size();
        }
    }

    private boolean tracePath(Map<Pos, Set<Direction>> visited, Runnable onDiscover) {
        while (isInBounds()) {

            visited.putIfAbsent(currentPos, new HashSet<>());
            Set<Direction> dirs = visited.get(currentPos);

            if (dirs.contains(currentDir)) {
                return true;
            }

            onDiscover.run();
            dirs.add(currentDir);

            while (!canMove()) {
                rotate90();

                if (dirs.contains(currentDir)) {
                    return true;
                }

                onDiscover.run();
                dirs.add(currentDir);
            }

            move();
        }

        return false;
    }

    private Map<Pos, Set<Direction>> copyVisited(Map<Pos, Set<Direction>> visited) {
        Map<Pos, Set<Direction>> copy = new HashMap<>();
        visited.forEach((pos, dirs) -> copy.put(new Pos(pos), new HashSet<>(dirs)));
        return copy;
    }

    @Override
    protected Pos getStartPos() {
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);

            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '^') {
                    return new Pos(x, y);
                }
            }
        }

        throw new IllegalArgumentException();
    }

    @Override
    protected Direction getStartDir() {
        return Direction.UP;
    }

    @Override
    protected boolean canMove() {
        Pos nextPos = currentPos.add(currentDir);
        return !isInBounds(nextPos) || !grid[nextPos.y()][nextPos.x()];
    }
}
