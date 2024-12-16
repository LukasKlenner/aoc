package aoc.year24.day16;

import aoc.util.Direction;
import aoc.util.JoinedCharacterGridTask;
import aoc.util.Pair;
import aoc.util.Pos;
import aoc.util.graph.Dijkstra;
import aoc.util.graph.Edge;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day16 extends JoinedCharacterGridTask {


    @Override
    public long run(boolean part1) {

        Pair<Graph<Character>, Map<Pos, Node<Character>>> graphAndMap = toGraph((from, to) -> !getValue(from).equals('#') && !getValue(to).equals('#'));
        Graph<Character> graph = graphAndMap.first;
        Map<Pos, Node<Character>> map = graphAndMap.second;

        Pos endPos = findCharValue('E');

        BiFunction<Pair<Node<Character>, Direction>, Pair<Node<Character>, Direction>, Long> costFunction = (from, to) -> {

            if (from.first.getPos().equals(endPos)) return 1L;

            if (from.first.getPos().add(from.second).equals(to.first.getPos())) return 1L;
            if (from.first.getPos().add(from.second.rotate90()).equals(to.first.getPos())) return 1001L;
            if (from.first.getPos().add(from.second.rotate270()).equals(to.first.getPos())) return 1001L;
            return -1L;
        };

        BiFunction<Pair<Node<Character>, Direction>, Node<Character>, Pair<Node<Character>, Direction>> nodeToStateFct = (from, toNode) -> {

            Direction dir;

            if (toNode.getPos().down().equals(from.first.getPos())) dir = Direction.UP;
            else if (toNode.getPos().up().equals(from.first.getPos())) dir = Direction.DOWN;
            else if (toNode.getPos().right().equals(from.first.getPos())) dir = Direction.LEFT;
            else dir = Direction.RIGHT;

            return new Pair<>(toNode, dir);
        };

        Dijkstra<Pair<Node<Character>, Direction>, Character> dijkstra = new Dijkstra<>(
                graph.getNodes().stream().flatMap(n -> Arrays.stream(Direction.values()).map(d -> new Pair<>(n, d))).collect(Collectors.toSet()),
                pair -> pair.first,
                costFunction,
                nodeToStateFct
        );

        List<Pair<Node<Character>, Direction>> targets = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            targets.add(new Pair<>(map.get(endPos), dir));
        }

        Node<Character> startNode = map.get(findCharValue('S'));
        dijkstra.run(new Pair<>(startNode, Direction.RIGHT));

        long minDistance = Long.MAX_VALUE;
        for (var target : targets) {
            Long distance = dijkstra.distances.get(target);
            if (distance == null) continue;
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        if (part1) return minDistance;

        Dijkstra<Pair<Node<Character>, Direction>, Character> reverseDijkstra = new Dijkstra<>(
                graph.getNodes().stream().flatMap(n -> Arrays.stream(Direction.values()).map(d -> new Pair<>(n, d))).collect(Collectors.toSet()),
                pair -> pair.first,
                costFunction,
                nodeToStateFct
        );

        reverseDijkstra.run(new Pair<>(map.get(endPos), Direction.LEFT));

        long result = 0;

        outer: for (Node<Character> node : graph.getNodes()) {
            for (Direction fromDir : Direction.values()) {
                var from = new Pair<>(node, fromDir);
                Long fromDistance = dijkstra.distances.get(from);
                if (fromDistance > minDistance) continue;

                for (Direction toDir : new Direction[]{fromDir, fromDir.rotate90(), fromDir.rotate270()}) {
                    var to = new Pair<>(node, toDir.rotate180());
                    if (to.first == null) continue;

                    Long toDistance = reverseDijkstra.distances.get(to);
                    if (toDistance > minDistance) continue;

                    Long edgeCost = fromDir.equals(toDir) ? 0L : 1000L;

                    long sum = fromDistance + edgeCost + toDistance;

                    if (sum == minDistance) {
                        result++;
                        continue outer;
                    }
                }

            }

        }

        return result; //TODO start wird nur mitgezählt wenn es nicht in Sackgasse ist
    }

}
