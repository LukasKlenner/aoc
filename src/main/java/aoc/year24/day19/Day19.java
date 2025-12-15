package aoc.year24.day19;

import aoc.Day;
import aoc.util.NonDeterministicAutomaton;
import aoc.util.NdaNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day19 implements Day {

    @Override
    public long part1(Stream<String> lines) {
        List<String> input = lines.toList();

        Map<String, Boolean> possiblePatterns = new HashMap<>();
        Arrays.stream(input.get(0).split(", ")).forEach(s -> possiblePatterns.put(s, true));

        return input.subList(2, input.size()).stream().filter(s -> isPossible(s, possiblePatterns)).count();
    }

    private boolean isPossible(String design, Map<String, Boolean> possiblePatterns) {

        for (int i = 1; i < design.length(); i++) {
            String part1 = design.substring(0, i);
            String part2 = design.substring(i);

            if (!possiblePatterns.containsKey(part1)) {
                possiblePatterns.put(part1, isPossible(part1, possiblePatterns));
            }

            if (!possiblePatterns.containsKey(part2)) {
                possiblePatterns.put(part2, isPossible(part2, possiblePatterns));
            }

            if (possiblePatterns.get(part1) && possiblePatterns.get(part2)) {
                possiblePatterns.put(design, true);
                return true;
            }

        }

        possiblePatterns.put(design, false);
        return false;
    }

    @Override
    public long part2(Stream<String> lines) {
        List<String> inputs = lines.toList();

/*        NonDeterministicAutomaton<Character> automaton = createAutomaton(inputs.get(0).split(", "));

        AtomicInteger result = new AtomicInteger(0);

        for (String input : inputs.subList(2, inputs.size())) {
            System.out.println(input);
            automaton.execute(input.chars().mapToObj(i -> (char) i).toList(), n -> {
                if (n.isStartNode()) result.incrementAndGet();
                return true;
            });
        }

        return result.get();
*/

        Map<String, Long> combinationCount = new HashMap<>();
        Arrays.stream(inputs.get(0).split(", ")).forEach(s -> combinationCount.put(s, 1L));

        combinationCount.put("br", 2L);

        return inputs.subList(2, inputs.size()).stream().mapToLong(s -> getCombinations(s, combinationCount, Arrays.asList(inputs.get(0).split(", ")))).sum();

    }

    private NonDeterministicAutomaton<Character> createAutomaton(String[] inputs) {

        NonDeterministicAutomaton<Character> automaton = new NonDeterministicAutomaton<>();

        /*for (String input : inputs) {

            NdaNode<Character> current = automaton.startNode;

            for (int i = 0; i < input.length(); i++) {
                char value = input.charAt(i);

                if (i == input.length() - 1) current.addEdge(value, automaton.startNode);
                else current = automaton.addEdgeToNewNode(current, value);
            }

        }*/



        Map<Character, NdaNode<Character>> previousNodes = null;

        int i = 0;
        while (true) {

            Map<Character, NdaNode<Character>> currentNodes = new HashMap<>();
            boolean stop = true;

            for (String str : inputs) {

                if (i >= str.length()) continue;

                char currentValue = str.charAt(i);

                if (i == str.length() - 1) {

                    if (i == 0) automaton.startNode.addEdge(currentValue, automaton.startNode);
                    else previousNodes.get(str.charAt(i - 1)).addEdge(currentValue, automaton.startNode);
                    continue;
                }
                stop = false;


                NdaNode<Character> currentNode = currentNodes.computeIfAbsent(currentValue, k -> new NdaNode<>(false));
                NdaNode<Character> previousNode = i == 0 ? automaton.startNode : previousNodes.get(str.charAt(i - 1));

                previousNode.addEdge(currentValue, currentNode);
            }

            if (stop) break;

            i++;
            previousNodes = currentNodes;
        }

        return automaton;
    }

    private long getCombinations(String design, Map<String, Long> combinationCount, List<String> inputs) {

        long count = 0;
        Set<String> used = new HashSet<>();

        outer: for (int i = 1; i < design.length(); i++) {
            String part1 = design.substring(0, i);
            String part2 = design.substring(i);

            //TODO input der sich aus anderen inputs zusammensetzt rausnehmen und irgendwie reinrechnen wenn part1 mit so einem endet
            for (String input : inputs) {
                if (part1.endsWith(input) && used.contains(part1.substring(0, part1.length() - input.length()))) {
                    used.add(part1);
                    continue outer;
                }
            }

            if (!combinationCount.containsKey(part1)) {
                combinationCount.put(part1, getCombinations(part1, combinationCount, inputs));
            }

            if (!combinationCount.containsKey(part2)) {
                combinationCount.put(part2, getCombinations(part2, combinationCount, inputs));
            }

            Long part1Count = combinationCount.get(part1);
            Long part2Count = combinationCount.get(part2);

            long combinations = part1Count * part2Count;
            count += combinations;

            if (combinations != 0) used.add(part1);

            /*
            r wrr -> r wr r
            rwr r -> r wr r

            1. r wrwrr -> r wr wr r
            2. rwr wr r ->
            3. rwrwr r

            rwr wrr

            r -> 1
            wrwrr -> 1


            rwr ->

            1. r wrrqr
            2. rwr rgr
             */

        }

        combinationCount.put(design, count);
        return count;
    }
}
