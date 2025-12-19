package aoc.year24.day24;

import aoc.Day;
import aoc.JoinedDay;
import aoc.util.Pair;
import aoc.util.Triple;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day24 implements Day {

    @Override
    public long part1(Stream<String> lines) {
        Triple<List<Wire>, List<Wire>, Map<String, Wire>> input = parse(lines.toList());

        Queue<Wire> updatedWires = new ArrayDeque<>(input.first);

        while (!updatedWires.isEmpty()) {

            Wire updatedWire = updatedWires.poll();

            for (Gate reachedGate : updatedWire.toGates) {
                if (reachedGate.update()) {
                    updatedWires.add(reachedGate.out);
                }
            }
        }

        return getValue(input.second);
    }

    @Override
    public long part2(Stream<String> lines) {
        Triple<List<Wire>, List<Wire>, Map<String, Wire>> input = parse(lines.toList());

        Map<String, Wire> wireMap = input.third;

        swapped.clear();

        swap(wireMap.get("mwk"), wireMap.get("z10"));
        swap(wireMap.get("z18"), wireMap.get("qgd"));
        swap(wireMap.get("jmh"), wireMap.get("hsw"));
        swap(wireMap.get("z33"), wireMap.get("gqp"));

        Wire in1 = wireMap.get("x01");
        Wire in2 = wireMap.get("y01");
        Wire out = wireMap.get("z01");
        Gate c = wireMap.get("x00").toGates.get(1);

        while (in1 != null) {
            Gate xorGate1 = in1.toGates.get(0) instanceof XorGate ? in1.toGates.get(0) : in1.toGates.get(1);
            Gate xorGate2 = out.fromGate;

            Gate andGate1 = in1.toGates.get(0) instanceof AndGate ? in1.toGates.get(0) : in1.toGates.get(1);
            Gate andGate2 = xorGate1.out.toGates.get(0) instanceof AndGate ? xorGate1.out.toGates.get(0) : xorGate1.out.toGates.get(1);

            Gate orGate = andGate1.out.toGates.get(0);


            if (!xorGate2.hasInput(xorGate1)) {
                System.out.println(0);
            }

            if (!xorGate2.hasInput(c)) {
                System.out.println(0);
            }

            if (!andGate2.hasInput(xorGate1)) {
                System.out.println(0);
            }

            if (!andGate2.hasInput(c)) {
                System.out.println(0);
            }

            if (!orGate.hasInput(andGate1)) {
                System.out.println(0);
            }

            if (!orGate.hasInput(andGate2)) {
                System.out.println(0);
            }

            if (xorGate2.out != out) {
                System.out.println();
            }

            if (andGate1.out.toGates.get(0) != andGate2.out.toGates.get(0)) {
                System.out.println(0);
            }

            c = orGate;
            in1 = wireMap.get(getNext(in1.name));
            in2 = wireMap.get(getNext(in2.name));
            out = wireMap.get(getNext(out.name));

        }

        System.out.println(swapped.stream().sorted().collect(Collectors.joining(",")));

        return 0;
    }

    List<String> swapped = new ArrayList<>();

    private void swap(Wire wire1, Wire wire2) {
        swapped.add(wire1.name);
        swapped.add(wire2.name);

        Gate wire1From = wire1.fromGate;
        Gate wire2From = wire2.fromGate;

        wire1From.out = wire2;
        wire2From.out = wire1;

        wire1.fromGate = wire2From;
        wire2.fromGate = wire1From;

    }

    private String getNext(String name) {
        int newValue = Integer.parseInt(name.substring(1)) + 1;
        if (newValue < 10) return name.charAt(0) + "0" + newValue;
        return name.substring(0, 1) + newValue;
    }

    private long getValue(List<Wire> outputWires) {
        long result = 0;

        for (Wire wire : outputWires) {
            result |= wire.value * (long) Math.pow(2, Long.parseLong(wire.name.substring(1)));
        }

        return result;
    }

    private Triple<List<Wire>, List<Wire>, Map<String, Wire>> parse(List<String> input) {

        List<Wire> inputWires = new ArrayList<>();
        List<Wire> outputWires = new ArrayList<>();

        Map<String, Wire> wireMap = new HashMap<>();

        boolean first = true;
        for (String line : input) {
            if (line.isEmpty()) {
                first = false;
                continue;
            }

            if (first) {
                String name = line.split(": ")[0];
                int value = Integer.parseInt(line.split(": ")[1]);
                Wire wire = new Wire(name, value);

                inputWires.add(wire);
                wireMap.put(name, wire);
            } else {
                String[] arr = line.split(" ");

                Wire in1 = wireMap.computeIfAbsent(arr[0], Wire::new);
                Wire in2 = wireMap.computeIfAbsent(arr[2], Wire::new);

                Wire out = wireMap.computeIfAbsent(arr[4], Wire::new);
                if (out.name.startsWith("z")) outputWires.add(out);

                Gate gate = switch (arr[1]) {
                    case "AND" -> new AndGate(in1, in2, out);
                    case "OR" -> new OrGate(in1, in2, out);
                    case "XOR" -> new XorGate(in1, in2, out);
                    default -> throw new IllegalStateException("Unexpected value: " + arr[1]);
                };

                in1.toGates.add(gate);
                in2.toGates.add(gate);
                out.fromGate = gate;
            }
        }

        return new Triple<>(inputWires, outputWires, wireMap);
    }

    private abstract static class Gate {

        Wire in1;
        Wire in2;
        Wire out;

        public Gate(Wire in1, Wire in2, Wire out) {
            this.in1 = in1;
            this.in2 = in2;
            this.out = out;
        }

        public boolean update() {
            if (in1.value == null || in2.value == null) return false;

            out.value = computeValue();
            return true;
        }

        public boolean hasInput(Gate input) {
            return in1 == input.out || in2 == input.out;
        }

        public abstract int computeValue();

    }

    private static class AndGate extends Gate {

        public AndGate(Wire in1, Wire in2, Wire out) {
            super(in1, in2, out);
        }

        @Override
        public int computeValue() {
            return in1.value & in2.value;
        }

        @Override
        public String toString() {
            return "%s AND %s -> %s".formatted(in1, in2, out);
        }
    }

    private static class OrGate extends Gate {

        public OrGate(Wire in1, Wire in2, Wire out) {
            super(in1, in2, out);
        }

        @Override
        public int computeValue() {
            return in1.value | in2.value;
        }

        @Override
        public String toString() {
            return "%s OR %s -> %s".formatted(in1, in2, out);
        }
    }

    private static class XorGate extends Gate {

        public XorGate(Wire in1, Wire in2, Wire out) {
            super(in1, in2, out);
        }

        @Override
        public int computeValue() {
            return in1.value ^ in2.value;
        }

        @Override
        public String toString() {
            return "%s XOR %s -> %s".formatted(in1, in2, out);
        }
    }

    private static final class Wire {
        final String name;
        Integer value;

        Gate fromGate;
        List<Gate> toGates = new ArrayList<>();

        private Wire(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public Wire(String name) {
            this(name, null);
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
