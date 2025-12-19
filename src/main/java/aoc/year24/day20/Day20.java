package aoc.year24.day20;

import aoc.JoinedDay;
import aoc.util.GridTask;
import aoc.util.Pair;
import aoc.util.Pos;
import aoc.util.graph.Dijkstra;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day20 extends GridTask<Character> {

    public Day20() {
        super(Function.identity(), Character[]::new, Character[][]::new);
    }

    @Override
    public long part1() {

        Pair<Graph<Character>, Map<Pos, Node<Character>>> graphAndMap = toGraph((from, to) -> !getValue(from).equals('#') && !getValue(to).equals('#'));
        Graph<Character> graph = graphAndMap.first;
        Map<Pos, Node<Character>> map = graphAndMap.second;

        Pos startPos = findValue('S');
        Pos endPos = findValue('E');

        //Dijkstra<Pair<Node<Character>, Pair<Pos, Pos>>, Character>

        return 0;
    }

    @Override
    public long part2() {
        return 0;
    }
}
