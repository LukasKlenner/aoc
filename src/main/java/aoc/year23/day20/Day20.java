package aoc.year23.day20;

import aoc.Day;

import java.util.*;
import java.util.stream.Stream;

public class Day20 implements Day {
    @Override
    public long part1(Stream<String> lines) {

        Simulation simulation = createSimulation(lines.toList());

        simulation.part1(1000);

        System.out.println(simulation.lowCount);
        System.out.println(simulation.highCount);

        return simulation.lowCount * simulation.highCount;
    }

    @Override
    public long part2(Stream<String> lines) {

        Simulation simulation = createSimulation(lines.toList());

        return simulation.part2();
    }

    private Simulation createSimulation(List<String> lines) {
        Simulation simulation = new Simulation();
        BroadcasterModule broadcaster = null;

        HashMap<String, Module> modules = new HashMap<>();

        for (String line : lines) {
            String moduleName = line.split(" -> ")[0];

            if (moduleName.startsWith("%")) {
                String name = moduleName.substring(1);
                modules.put(name, new FlipFlopModule(simulation));
                modules.get(name).name = name;
            } else if (moduleName.startsWith("&")) {
                String name = moduleName.substring(1);
                modules.put(name, new ConjunctionModule(simulation));
                modules.get(name).name = name;
            } else if (moduleName.equals("broadcaster")) {
                broadcaster = new BroadcasterModule(simulation);
                modules.put(moduleName, broadcaster);
                broadcaster.name = moduleName;
            } else {
                throw new IllegalArgumentException(moduleName);
            }
        }

        simulation.setBroadcaster(broadcaster);

        for (String line : lines) {
            String[] split = line.split(" -> ");

            Module module = modules.get(split[0].replace("%", "").replace("&", ""));

            module.setReceivers(Arrays.stream(split[1].split(", "))
                    .map(m -> {
                        if (m.equals("rx")) {
                            UntypedModule rx = new UntypedModule(simulation);
                            simulation.setRx(rx);
                            return rx;
                        }
                        return modules.get(m);
                    })
                    .peek(m -> {
                        if (m instanceof ConjunctionModule cm) {
                            cm.increaseInputSize();
                        }
                    })
                    .map(m -> Objects.requireNonNullElseGet(m, () -> new UntypedModule(simulation)))
                    .toList()
            );
        }

        return simulation;
    }

    private static class Simulation {

        private final Queue<Signal> signalQueue = new LinkedList<>();
        private Module broadcaster;
        private UntypedModule rx;

        long lowCount = 0;
        long highCount = 0;

        public void part1(int iterations) {

            for (int i = 0; i < iterations; i++) {

                addToQueue(new LowSignal(broadcaster, null));
                run();
            }
        }

        public long part2() {

            long count = 0;
            while (true) {
                count++;
                addToQueue(new LowSignal(broadcaster, null));
                run();

                if (rx.receivedLow) {
                    return count;
                }

            }

        }

        private void run() {
            while (!signalQueue.isEmpty()) {
                signalQueue.poll().send();
            }
        }

        public void addToQueue(Signal signal) {
            signalQueue.offer(signal);
        }

        public void setBroadcaster(Module broadcaster) {
            this.broadcaster = broadcaster;
        }

        public void setRx(UntypedModule rx) {
            this.rx = rx;
        }

    }

    private static abstract class Module {

        List<Module> receivers = new ArrayList<>();

        Simulation simulation;

        String name;

        public Module(Simulation simulation) {
            this.simulation = simulation;
        }

        abstract void handleLow(Module sender);

        abstract void handleHigh(Module sender);

        public void setReceivers(List<Module> receivers) {
            this.receivers = receivers;
        }

        protected void sendLow() {
            for (Module receiver : receivers) {
                simulation.addToQueue(new LowSignal(receiver, this));
            }
        }

        protected void sendHigh() {
            for (Module receiver : receivers) {
                simulation.addToQueue(new HighSignal(receiver, this));
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class FlipFlopModule extends Module {

        boolean on = false;

        public FlipFlopModule(Simulation simulation) {
            super(simulation);
        }

        @Override
        void handleLow(Module sender) {
            on = !on;

            if (on) {
                sendHigh();
            } else {
                sendLow();
            }
        }

        @Override
        void handleHigh(Module sender) {

        }
    }

    private static class ConjunctionModule extends Module {

        boolean[] inputs = new boolean[0];

        Map<Module, Integer> indices = new HashMap<>();

        int nextIndex = 0;

        public ConjunctionModule(Simulation simulation) {
            super(simulation);
        }

        public void increaseInputSize() {
            inputs = new boolean[inputs.length + 1];
        }

        @Override
        void handleLow(Module sender) {
            inputs[getIndex(sender)] = false;
            send();
        }

        @Override
        void handleHigh(Module sender) {
            inputs[getIndex(sender)] = true;
            send();
        }

        private void send() {

            boolean allHigh = true;

            for (int i = 0; i < inputs.length; i++) {
                if (!inputs[i]) {
                    allHigh = false;
                    break;
                }
            }

            if (allHigh) {
                sendLow();
            } else{
                sendHigh();
            }

        }

        private Integer getIndex(Module sender) {

            return indices.computeIfAbsent(sender, s -> nextIndex++);
        }
    }

    private static class BroadcasterModule extends Module {

        public BroadcasterModule(Simulation simulation) {
            super(simulation);
        }

        @Override
        void handleLow(Module sender) {
            sendLow();
        }

        @Override
        void handleHigh(Module sender) {
            sendHigh();
        }
    }

    private static class UntypedModule extends Module {

        boolean receivedLow = false;

        public UntypedModule(Simulation simulation) {
            super(simulation);
        }

        @Override
        void handleLow(Module sender) {
            receivedLow = true;
        }

        @Override
        void handleHigh(Module sender) {

        }
    }

    private static abstract class Signal {

        Module sender;
        Module receiver;

        abstract void send();
    }

    private static class LowSignal extends Signal {

        public LowSignal(Module receiver, Module sender) {
            this.receiver = receiver;
            this.sender = sender;
        }

        @Override
        void send() {
            //System.out.println(sender + " -low-> " + receiver);
            receiver.simulation.lowCount++;
            receiver.handleLow(sender);
        }
    }

    private static class HighSignal extends Signal {

        public HighSignal(Module receiver, Module sender) {
            this.receiver = receiver;
            this.sender = sender;
        }

        @Override
        void send() {
            //System.out.println(sender + " -high-> " + receiver);
            receiver.simulation.highCount++;
            receiver.handleHigh(sender);
        }
    }
}
