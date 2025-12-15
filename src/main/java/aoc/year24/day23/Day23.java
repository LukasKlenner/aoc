package aoc.year24.day23;

import aoc.Day;
import aoc.JoinedDay;
import aoc.util.graph.Graph;
import aoc.util.graph.Node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 implements Day {

    private Graph<String> createGraph(Stream<String> stream) {

        Graph<String> graph = new Graph<>();

        stream.forEach(s -> {
            graph.addEdgeAndNodes(s.split("-")[0], s.split("-")[1]);
            graph.addEdgeAndNodes(s.split("-")[1], s.split("-")[0]);
        });

        return graph;
    }

    @Override
    public long part1(Stream<String> lines) {
        Graph<String> graph = createGraph(lines);

        List<Set<Node<String>>> groups = new ArrayList<>();

        for (Node<String> node : graph.getNodes()) {
            HashSet<Node<String>> group = new HashSet<>();
            group.add(node);
            groups.add(group);
        }

        for (Node<String> node : graph.getNodes()) {
            List<Set<Node<String>>> copies = new ArrayList<>();
            outer: for (Set<Node<String>> group : groups) {

                if (group.size() == 3) continue;

                for (Node<String> nodeInGroup : group) {
                    if (!node.hasEdgeTo(nodeInGroup)) continue outer;
                }
                copies.add(new HashSet<>(group));
                group.add(node);
            }
            groups.addAll(copies);
        }

        return groups.stream()
                .filter(g -> g.size() == 3)
                .filter(g -> g.stream().anyMatch(n -> n.getValue().startsWith("t")))
                .distinct()
                .count();
    }

    @Override
    public long part2(Stream<String> lines) {
        Graph<String> graph = createGraph(lines);

        List<Set<Node<String>>> groups = new ArrayList<>();

        for (Node<String> node : graph.getNodes()) {
            HashSet<Node<String>> group = new HashSet<>();
            group.add(node);
            groups.add(group);
        }

        for (Node<String> node : graph.getNodes()) {
            List<Set<Node<String>>> copies = new ArrayList<>();
            outer: for (Set<Node<String>> group : groups) {

                //if (group.size() == 3) continue;

                for (Node<String> nodeInGroup : group) {
                    if (!node.hasEdgeTo(nodeInGroup)) continue outer;
                }
                copies.add(new HashSet<>(group));
                group.add(node);
            }
            //groups.addAll(copies);
        }


        System.out.println(groups.stream()
                .sorted(Comparator.<Set<Node<String>>>comparingInt(Set::size).reversed())
                .findFirst()
                .get()
                .stream()
                .map(Node::getValue)
                .sorted()
                .collect(Collectors.joining(",")));

        return 0;
    }
}
