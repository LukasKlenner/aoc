package aoc.year24.day17;

import aoc.JoinedDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day17 implements JoinedDay {

    @Override
    public long run(Stream<String> stream, boolean part1) {

        List<String> input = stream.toList();

        long regAInitialValue = Long.parseLong(input.get(0).split(": ")[1]);
        long regBInitialValue = Long.parseLong(input.get(1).split(": ")[1]);
        long regCInitialValue = Long.parseLong(input.get(2).split(": ")[1]);

        Reg regA = new Reg(regAInitialValue);
        Reg regB = new Reg(regBInitialValue);
        Reg regC = new Reg(regCInitialValue);

        Reg instructionPointer = new Reg(0);
        List<Integer> out = new ArrayList<>();

        List<Integer> inputs = Arrays.stream(input.get(4).substring(9).split(",")).map(Integer::parseInt).toList();
        List<Instruction> instructions = new ArrayList<>();

        List<Reg> regs = List.of(regA, regB, regC, instructionPointer);

        for (int i = 0; i < inputs.size(); i += 2) {
            instructions.add(parseInstruction(inputs.get(i), inputs.get(i + 1), out, regs));
        }

        if (part1) {
            runProgram(regs, instructions);
            System.out.println(out.stream().map(Object::toString).collect(Collectors.joining(",")));
            return 0;
        }


        Integer[] result = new Integer[64];
        List<Rule> rules = new ArrayList<>();
        int index = 63;

        for (Integer value : inputs) {
            int[] targetBits = last3Bit(value);
            rules.add(new Rule(index, targetBits));

            index -= 3;
        }

        Integer[] res = part2(result, rules);

        return bitsToLong(res);
    }

    private long bitsToLong(Integer[] bits) {

        int exp = 0;
        long res = 0;
        for (int i = 63; i >= 0; i--) {

            res += bits[i] * (2L << i);

            exp++;
        }

        return res;
    }

    private int[] last3Bit(int value) {
        int[] result = new int[3];
        result[0] = (value >> 2) & 0b1;
        result[1] = (value >> 1) & 0b1;
        result[2] = value & 0b1;
        return result;
    }

    private Integer[] part2(Integer[] result, List<Rule> rules) {

        int nextIndex = findNextIndex(result);

        if (nextIndex == -1) return result;

        Integer[] copy = Arrays.copyOf(result, result.length);
        copy[nextIndex] = 0;

        if (applyRules(copy, rules)) {
            Integer[] res = part2(copy, rules);
            if (res != null) return res;
        }

        copy = Arrays.copyOf(result, result.length);
        copy[nextIndex] = 1;

        if (applyRules(copy, rules)) {
            return part2(copy, rules);
        }

        return null;
    }

    private int findNextIndex(Integer[] result) {
        for (int i = result.length -  1; i >= 0; i--) {
            if (result[i] == null) return i;
        }

        return -1;
    }

    private boolean applyRules(Integer[] result, List<Rule> rules) {

        boolean changed = true;

        while (changed) {

            changed = false;

            for (Rule rule : rules) {

                if (!rule.apply(result)) return false;

                if (rule.changed) changed = true;
            }
        }

        return true;
    }

    private static class Rule {

        boolean changed = false;

        private final int index;
        private final int[] targetBits;

        private Rule(int index, int[] targetBits) {
            this.index = index;
            this.targetBits = targetBits;
        }

        boolean apply(Integer[] result) {
            changed = false;

            Integer a1 = result[index - 2];
            Integer a2 = result[index - 1];
            Integer a3 = result[index];

            if (a1 == null || a2 == null || a3 == null) return true;

            for (int i = 0; i < 3; i++) {
                int actualIndex = index - (a1 * 4 + a2 * 2 + (a3 == 0 ? 1 : 0) +  i);

                Integer actual = result[actualIndex];
                Integer xor = result[index - i];

                if (actual == null && xor == null) continue;

                int expected = targetBits[i];

                //TODO wenn xor A3 ist, ist es notA3?

                if (actual == null) {
                    result[actualIndex] = expected ^ xor;
                    changed = true;
                    continue;
                }

                if (xor == null) {
                    result[index + i] = expected ^ actual; //TODO correct?
                    changed = true;
                    continue;
                }

                if ((actual ^ xor) != expected) return false;
            }

            return true;
        }

        /*
                    while A != 0

1: B = A % 8      > b letzte 3 bits

2: B = B xor 1    > B = A1 A2 notA3

3: C = A / (2^B)  > C = A um 1 - 7 Stellen nach rechts ???

4: B = B xor 5  	> B = notA1 A2 A3		5 = 101

5: B = B xor C	>

6: out B % 8	>

7: A = A >>> 3



A = 001
B = 001
C = A00







target = XYZ

B6 = XYZ

B5 = notA1 A2 0    C5 = ??Z


B3 = A1 A2 1

C4 = A(A1*4 + A2*2 + notA3 + 2) A(A1*4 + A2*2 + notA3 + 1) A(notA1*4 + A2*2 + notA3)


B6 = A(A1*4 + A2*2 + notA3 + 2) ^ A1     A(A1*4 + A2*2 + notA3 + 1) ^ A2      A(notA1*4 + A2*2 + notA3) ^ A3
	= X					= Y					= Z


A1 = 0	A2 = 0 A3 = 0

	 000

XXXXXXXX|XX101|

B 101
C


1000100100010111100110101001111110101110111100111010
0000000000001000100100010111100110101001111110101110111100111010
         */

    }

    private void printBits(int[] bits) {
        for (int b : bits) {
            System.out.print(b);
        }
        System.out.println();
    }

    private void runProgram(List<Reg> regs, List<Instruction> instructions) {
        Reg instructionPointer = regs.get(3);

        while (instructionPointer.get() < instructions.size()) {
            long index = instructionPointer.get();
            Instruction instruction = instructions.get((int) index);

            instruction.execute();

            if (instructionPointer.get() == index) instructionPointer.set(index + 1);
        }

    }

    private Instruction parseInstruction(Integer opCode, Integer operand, List<Integer> out, List<Reg> regs) {
        Reg regA = regs.get(0);
        Reg regB = regs.get(1);
        Reg regC = regs.get(2);
        Reg instructionPointer = regs.get(3);

        return switch (opCode) {
            case 0 -> new DV(regA, regA, parseComboLiteral(operand, regs));
            case 1 -> new BXL(regB, new Literal(operand));
            case 2 -> new BST(regB, parseComboLiteral(operand, regs));
            case 3 -> new JNZ(regA, new Literal(operand), instructionPointer);
            case 4 -> new BXC(regB, regC);
            case 5 -> new OUT(parseComboLiteral(operand, regs), out);
            case 6 -> new DV(regA, regB, parseComboLiteral(operand, regs));
            case 7 -> new DV(regA, regC, parseComboLiteral(operand, regs));
            default -> throw new IllegalArgumentException("Unknown opCode: " + opCode);
        };
    }

    private ValueLike parseComboLiteral(Integer literal, List<Reg> regs) {
        return switch (literal) {
            case 4 -> regs.get(0);
            case 5 -> regs.get(1);
            case 6 -> regs.get(2);
            default -> new Literal(literal);
        };
    }

    private interface ValueLike {

        long get();

        void set(long value);
    }

    private class Literal implements ValueLike {

        private final long value;

        private Literal(long value) {
            this.value = value;
        }

        @Override
        public long get() {
            return value;
        }

        @Override
        public void set(long value) {
            throw new UnsupportedOperationException();
        }
    }

    private static class Reg implements ValueLike {

        long value;

        public Reg(long value) {
            this.value = value;
        }

        public long get() {
            return value;
        }

        public void set(long value) {
            this.value = value;
        }

    }

    private interface Instruction {

        void execute();

    }

    private class DV implements Instruction {

        private final Reg regA;
        private final Reg targetReg;
        private final ValueLike comboLiteral;

        private DV(Reg regA, Reg targetReg, ValueLike comboLiteral) {
            this.regA = regA;
            this.targetReg = targetReg;
            this.comboLiteral = comboLiteral;
        }

        @Override
        public void execute() {
            targetReg.set(regA.get() >>> comboLiteral.get());
        }
    }

    private class BXL implements Instruction {

        private final Reg regB;
        private final Literal literal;

        private BXL(Reg regB, Literal literal) {
            this.regB = regB;
            this.literal = literal;
        }

        @Override
        public void execute() {
            regB.set(regB.get() ^ literal.get());
        }
    }

    private class BST implements Instruction {

        private final Reg regB;
        private final ValueLike comboLiteral;

        private BST(Reg regB, ValueLike comboLiteral) {
            this.regB = regB;
            this.comboLiteral = comboLiteral;
        }

        @Override
        public void execute() {
            regB.set(comboLiteral.get() & 0b111);
        }
    }

    private class JNZ implements Instruction {

        private final Reg regA;
        private final Literal literal;
        private final Reg instructionPointer;

        private JNZ(Reg regA, Literal literal, Reg instructionPointer) {
            this.regA = regA;
            this.literal = literal;
            this.instructionPointer = instructionPointer;
        }

        @Override
        public void execute() {
            if (regA.get() != 0) {
                instructionPointer.set(literal.get() / 2);
            }
        }
    }

    private class BXC implements Instruction {

        private final Reg regB;
        private final Reg regC;

        private BXC(Reg regB, Reg regC) {
            this.regB = regB;
            this.regC = regC;
        }


        @Override
        public void execute() {
            regB.set(regB.get() ^ regC.get());
        }
    }

    private class OUT implements Instruction {

        private final ValueLike comboLiteral;
        private final List<Integer> out;

        private OUT(ValueLike comboLiteral, List<Integer> out) {
            this.comboLiteral = comboLiteral;
            this.out = out;
        }

        @Override
        public void execute() {
            out.add((int) (comboLiteral.get() & 0b111));
        }
    }

}
