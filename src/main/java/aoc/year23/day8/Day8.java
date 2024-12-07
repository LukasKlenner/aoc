package aoc.year23.day8;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day8 implements aoc.Day {
    @Override
    public long part1(Stream<String> linesStream) {
        List<String> lines = linesStream.toList();

        char[] next = lines.get(0).toCharArray();

        HashMap<String, Node> map = new HashMap<>();

        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            map.put(line.substring(0,3), new Node(line.startsWith("ZZZ")));
        }

        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            Node node = map.get(line.substring(0, 3));
            Node left = map.get(line.substring(7, 10));
            Node right = map.get(line.substring(12, 15));
            node.left = left;
            node.right = right;
        }

        Node currentNode = map.get("AAA");
        int currentIndex = 0;
        long count = 0;
        while (!currentNode.end) {
            currentNode = next[currentIndex++] == 'R' ? currentNode.right : currentNode.left;
            count++;
            if (currentIndex == next.length) currentIndex = 0;
        }

        return count;
    }

    @Override
    public long part2(Stream<String> linesStream) {
        List<String> lines = linesStream.toList();

        char[] next = lines.get(0).toCharArray();

        HashMap<String, Node> map = new HashMap<>();

        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            map.put(line.substring(0,3), new Node(line.substring(0, 3).endsWith("Z")));
        }

        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            Node node = map.get(line.substring(0, 3));
            Node left = map.get(line.substring(7, 10));
            Node right = map.get(line.substring(12, 15));
            node.left = left;
            node.right = right;
        }

        List<Node> startNodes = new ArrayList<>();

        for (Map.Entry<String, Node> entry : map.entrySet()) {
            if (entry.getKey().endsWith("A")) {
                startNodes.add(entry.getValue());
            }
        }

        List<BigInteger> iterations = new ArrayList<>();
        for (Node startNode : startNodes) {

            Node currentNode = startNode;
            int currentIndex = 0;
            long count = 0;
            while (true) {
                currentNode = next[currentIndex++] == 'R' ? currentNode.right : currentNode.left;
                count++;
                if (currentNode.end) {
                    iterations.add(BigInteger.valueOf(count));
                    break;
                }
                if (currentIndex == next.length) {
                    currentIndex = 0;
                }
            }
        }

        return iterations.stream().reduce(BigInteger.ONE, (x, y) -> x.multiply(y.divide(x.gcd(y)))).longValue();
    }

    private static class Node {
        Node left;
        Node right;

        boolean end;

        public Node(boolean end) {
            this.end = end;
        }
    }

}
