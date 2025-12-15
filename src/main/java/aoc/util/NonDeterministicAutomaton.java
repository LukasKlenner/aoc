package aoc.util;

import java.util.List;
import java.util.function.Function;

public class NonDeterministicAutomaton<T> {

    public final NdaNode<T> startNode;

    public NonDeterministicAutomaton() {
        startNode = new NdaNode<>(true);
    }

    public void execute(List<T> inputs, Function<NdaNode<T>, Boolean> onEachEnd) {
        execute(inputs, onEachEnd, startNode, 0);
    }

    public boolean execute(List<T> inputs, Function<NdaNode<T>, Boolean> onEachEnd, NdaNode<T> current, int inputIndex) {

        if (inputIndex == inputs.size()) {
            return onEachEnd.apply(current);
        }

        for (NdaNode<T> nextState : current.getNextStates(inputs.get(inputIndex))) {

            if (nextState == null) continue;

            if (!execute(inputs, onEachEnd, nextState, inputIndex + 1)) return false;
        }

        return true;
    }


    public NdaNode<T> addEdgeToNewNode(NdaNode<T> from, T value) {
        NdaNode<T> newNode = new NdaNode<>(false);
        from.addEdge(value, newNode);
        return newNode;
    }

    public void addEdge(NdaNode<T> from, NdaNode<T> to, T value) {
        from.addEdge(value, to);
    }

}
