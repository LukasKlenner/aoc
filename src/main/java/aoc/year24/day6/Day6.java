package aoc.year24.day6;

import aoc.JoinedDay;
import aoc.util.Direction;
import aoc.util.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Day6 implements JoinedDay {

    @Override
    public Object run(Stream<String> stream, boolean part1) {

        List<String> list = stream.toList();

        AtomicReference<Pos> start = new AtomicReference<>();

        Boolean[][] grid = list.stream().map(s ->
                s.chars().mapToObj(c -> {
                    if (c == '^') start.set(new Pos(s.indexOf(c), list.indexOf(s)));
                    return c == '#';
                }).toArray(Boolean[]::new)
                ).toArray(Boolean[][]::new);

        Pos startPos = start.get();
        Direction startDir = Direction.UP;

        if (part1) {
            Map<Pos, Set<Direction>> visited = new HashMap<>();
            tracePath(startPos, startDir, grid, visited, (p, d) -> {});
            return visited.size();
        } else {
            Map<Pos, Set<Direction>> visited = new HashMap<>();

            Set<Pos> loopPositions = new HashSet<>();

            tracePath(startPos, startDir, grid, visited, (p, d) -> {
                Map<Pos, Set<Direction>> visitedCopy = copyVisited(visited);

                Pos obstaclePos = new Pos(p).add(d);

                if (loopPositions.contains(obstaclePos) || !obstaclePos.isInbounds(grid)) {
                    return;
                }

                Direction newDir = d.rotate90();
                if (tracePath(p, newDir, grid, visitedCopy, (p2, d2) -> {})) {
                    loopPositions.add(obstaclePos);
                }

            });
            return loopPositions.size();
        }

    }

    private boolean tracePath(Pos pos, Direction dir, Boolean[][] grid, Map<Pos, Set<Direction>> visited,
                              BiConsumer<Pos, Direction> onDiscover) {
        while (pos.isInbounds(grid)) {
            if (!visited.containsKey(pos)) {
                visited.put(new Pos(pos), new HashSet<>());
            }

            Set<Direction> dirs = visited.get(pos);

            if (dirs.contains(dir)) {
                return true;
            } else {
                onDiscover.accept(new Pos(pos), dir);
            }
            dirs.add(dir);

            while (!canMove(pos, dir, grid)) {
                dir = dir.rotate90();

                if (dirs.contains(dir)) {
                    return true;
                } else {
                    onDiscover.accept(new Pos(pos), dir);
                }
                dirs.add(dir);

            }

            pos.add(dir);
        }

        return false;
    }

    private boolean canMove(Pos pos, Direction dir, Boolean[][] grid) {
        Pos nextPos = new Pos(pos).add(dir);
        return !nextPos.isInbounds(grid) || !grid[nextPos.y][nextPos.x];
    }

    private Direction getNextDirection(Pos pos, Direction dir, Boolean[][] grid) {
        Direction nextDir = dir;

        while (true) {
            Pos nextPos = new Pos(pos).add(nextDir);
            if (!nextPos.isInbounds(grid) || !grid[nextPos.y][nextPos.x]) {
                return nextDir;
            }

            nextDir = nextDir.rotate90();
        }
    }

    private Map<Pos, Set<Direction>> copyVisited(Map<Pos, Set<Direction>> visited) {
        Map<Pos, Set<Direction>> copy = new HashMap<>();
        visited.forEach((pos, dirs) -> copy.put(new Pos(pos), new HashSet<>(dirs)));
        return copy;
    }

}
