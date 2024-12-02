package aoc.year23.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day19 implements aoc.Day {
    @Override
    public Object part1(Stream<String> linesStream) {
        List<String> input = linesStream.toList();

        int workFlowCount = readWorkFlows(input);
        int sum = 0;

        for (int j = workFlowCount + 1; j < input.size(); j++) {

            Rating rating = Rating.of(input.get(j));

            if (WorkFlowBuilder.of("in").build().apply(rating)) {
                sum += rating.sum();
            }

        }

        return sum;
    }

    @Override
    public Object part2(Stream<String> linesStream) {

        List<String> input = linesStream.toList();

        readWorkFlows(input);

        RatingRange range = new RatingRange();

        List<RatingRange> result = WorkFlowBuilder.of("in").build().shrink(range);

        System.out.println(result);

        return result.stream()
                .mapToLong(r -> r.getxRanges().length() * r.getmRanges().length() * r.getaRanges().length() * r.getsRanges().length())
                .sum();
    }

    private int readWorkFlows(List<String> input) {
        int i = 0;
        while (!input.get(i).isEmpty()) {
            String line = input.get(i);
            String name = line.substring(0, line.indexOf('{'));
            String[] rules = line.substring(line.indexOf('{') + 1, line.indexOf('}')).split(",");

            WorkFlowBuilder workFlowBuilder = WorkFlowBuilder.of(name);
            RuleBuilder ruleBuilderHead = null;
            RuleBuilder ruleBuilderTail = null;
            for (int j = 0; j < rules.length; j++) {
                String rule = rules[j];

                RuleBuilder ruleBuilder;

                if (j == rules.length - 1) {
                    ruleBuilder = new RuleBuilder(rule, AlwaysTrueRatingPredicate.of());
                } else {
                    RatingExtractor extractor = RatingExtractor.of(rule.charAt(0));
                    RatingPredicate predicate = RatingPredicate.of(extractor, rule.charAt(1), Integer.parseInt(rule.substring(2, rule.indexOf(":"))));

                    ruleBuilder = new RuleBuilder(rule.substring(rule.indexOf(":") + 1), predicate);
                }

                if (ruleBuilderHead == null) {
                    ruleBuilderHead = ruleBuilderTail = ruleBuilder;
                } else {
                    ruleBuilderTail.setNext(ruleBuilder);
                    ruleBuilderTail = ruleBuilder;
                }
            }

            workFlowBuilder.setRuleBuilder(ruleBuilderHead);

            i++;
        }

        return i;
    }

    private static class RatingRange {

        private Range xRanges;
        private Range mRanges;
        private Range aRanges;
        private Range sRanges;

        public RatingRange() {
            this.xRanges = new Range(1, 4000);
            this.mRanges = new Range(1, 4000);
            this.aRanges = new Range(1, 4000);
            this.sRanges = new Range(1, 4000);
        }

        public RatingRange(Range xRanges, Range mRanges, Range aRanges, Range sRanges) {
            this.xRanges = xRanges;
            this.mRanges = mRanges;
            this.aRanges = aRanges;
            this.sRanges = sRanges;
        }

        public RatingRange copy() {
            return new RatingRange(new Range(xRanges.min, xRanges.max), new Range(mRanges.min, mRanges.max), new Range(aRanges.min, aRanges.max), new Range(sRanges.min, sRanges.max));
        }

        public Range getxRanges() {
            return xRanges;
        }

        public void setxRanges(Range xRanges) {
            this.xRanges = xRanges;
        }

        public Range getmRanges() {
            return mRanges;
        }

        public void setmRanges(Range mRanges) {
            this.mRanges = mRanges;
        }

        public Range getaRanges() {
            return aRanges;
        }

        public void setaRanges(Range aRanges) {
            this.aRanges = aRanges;
        }

        public Range getsRanges() {
            return sRanges;
        }

        public void setsRanges(Range sRanges) {
            this.sRanges = sRanges;
        }
    }

    private record Range(int min, int max) {

        public long length() {
            return max - min + 1;
        }
    }

    private record Rating(int x, int m, int a, int s) {

        public int sum() {
            return x + m + a + s;
        }

        public static Rating of(String line) {

            String[] split = line.substring(1, line.length() - 1).split(",");
            return new Rating(Integer.parseInt(split[0].substring(2)),
                    Integer.parseInt(split[1].substring(2)),
                    Integer.parseInt(split[2].substring(2)),
                    Integer.parseInt(split[3].substring(2)));
        }

    }

    private static class WorkFlow {

        private Rule rule;

        public void setRule(Rule rule) {
            this.rule = rule;
        }

        public boolean apply(Rating rating) {
            return rule.apply(rating);
        }

        public List<RatingRange> shrink(RatingRange ranges) {
            return rule.shrink(ranges);
        }

    }
    private static class AcceptWorkFlow extends WorkFlow {


        private static final AcceptWorkFlow workFlow = new AcceptWorkFlow();

        private AcceptWorkFlow() {}

        @Override
        public boolean apply(Rating rating) {
            return true;
        }

        @Override
        public List<RatingRange> shrink(RatingRange ranges) {
            return new ArrayList<>(List.of(ranges));
        }

        public static AcceptWorkFlow of() {
            return workFlow;
        }

    }
    private static class RejectWorkFlow extends WorkFlow {


        private static final RejectWorkFlow workFlow = new RejectWorkFlow();

        private RejectWorkFlow() {}

        @Override
        public boolean apply(Rating rating) {
            return false;
        }

        @Override
        public List<RatingRange> shrink(RatingRange ranges) {
            return new ArrayList<>();
        }

        public static RejectWorkFlow of() {
            return workFlow;
        }

    }

    private static class Rule {

        private final RatingPredicate predicate;
        private final WorkFlow nextWorkFlow;

        private final Rule nextRule;

        public Rule(RatingPredicate predicate, WorkFlow nextWorkFlow, Rule next) {
            this.predicate = predicate;
            this.nextWorkFlow = nextWorkFlow;
            this.nextRule = next;
        }

        public boolean apply(Rating rating) {
            return predicate.test(rating) ? nextWorkFlow.apply(rating) : nextRule.apply(rating);
        }

        public List<RatingRange> shrink(RatingRange ranges) {

            RatingRange[] split = predicate.split(ranges);

            List<RatingRange> list = nextWorkFlow.shrink(split[0]);
            if (nextRule != null) list.addAll(nextRule.shrink(split[1]));
            return list;
        }

    }

    private static abstract class RatingPredicate implements Predicate<Rating>{

        protected final RatingExtractor extractor;

        protected final int value;

        private RatingPredicate(RatingExtractor extractor, int value) {
            this.extractor = extractor;
            this.value = value;
        }

        public abstract RatingRange[] split(RatingRange ranges);

        public static RatingPredicate of(RatingExtractor extractor, char c, int value) {
            return switch (c) {
                case '<' -> new LessThanRatingPredicate(extractor, value);
                case '>' -> new GreaterThanRatingPredicate(extractor, value);
                default -> throw new IllegalArgumentException();
            };
        }

    }

    private static class LessThanRatingPredicate extends RatingPredicate {

        public LessThanRatingPredicate(RatingExtractor extractor, int value) {
            super(extractor, value);
        }

        @Override
        public boolean test(Rating rating) {
            return extractor.getValue(rating) < value;
        }

        @Override
        public RatingRange[] split(RatingRange ranges) {
            Range range = extractor.getRange(ranges);
            Range trueRange = new Range(range.min(), Math.min(range.max(), value - 1));
            Range falseRange = new Range(Math.max(range.min(), value), range.max());

            RatingRange trueRatingRange = ranges.copy();
            extractor.setRange(trueRatingRange, trueRange);

            RatingRange falseRatingRange = ranges.copy();
            extractor.setRange(falseRatingRange, falseRange);

            return new RatingRange[] {trueRatingRange, falseRatingRange};
        }
    }

    private static class GreaterThanRatingPredicate extends RatingPredicate {

        public GreaterThanRatingPredicate(RatingExtractor extractor, int value) {
            super(extractor, value);
        }

        @Override
        public boolean test(Rating rating) {
            return extractor.getValue(rating) > value;
        }

        @Override
        public RatingRange[] split(RatingRange ranges) {
            Range range = extractor.getRange(ranges);
            Range trueRange = new Range(Math.max(range.min(), value + 1), range.max());
            Range falseRange = new Range(range.min(), Math.min(range.max(), value));

            RatingRange trueRatingRange = ranges.copy();
            extractor.setRange(trueRatingRange, trueRange);

            RatingRange falseRatingRange = ranges.copy();
            extractor.setRange(falseRatingRange, falseRange);

            return new RatingRange[] {trueRatingRange, falseRatingRange};
        }
    }

    private static class AlwaysTrueRatingPredicate extends RatingPredicate {

        private static final AlwaysTrueRatingPredicate predicate = new AlwaysTrueRatingPredicate();

        private AlwaysTrueRatingPredicate() {
            super(null, 0);
        }

        @Override
        public boolean test(Rating rating) {
            return true;
        }

        @Override
        public RatingRange[] split(RatingRange ranges) {
            return new RatingRange[] {ranges};
        }

        public static AlwaysTrueRatingPredicate of() {
            return predicate;
        }
    }

    private static abstract class RatingExtractor {

        public abstract int getValue(Rating rating);

        public abstract Range getRange(RatingRange ranges);

        public abstract void setRange(RatingRange ranges, Range range);

        public static RatingExtractor of(char c) {
            return switch (c) {
                case 'x' -> new XExtractor();
                case 'm' -> new MExtractor();
                case 'a' -> new AExtractor();
                case 's' -> new SExtractor();
                default -> throw new IllegalArgumentException();
            };
        }

    }

    private static class XExtractor extends RatingExtractor {

        @Override
        public int getValue(Rating rating) {
            return rating.x;
        }

        @Override
        public Range getRange(RatingRange ranges) {
            return ranges.getxRanges();
        }

        @Override
        public void setRange(RatingRange ranges, Range range) {
            ranges.setxRanges(range);
        }

    }

    private static class MExtractor extends RatingExtractor {

        @Override
        public int getValue(Rating rating) {
            return rating.m;
        }

        @Override
        public Range getRange(RatingRange ranges) {
            return ranges.getmRanges();
        }

        @Override
        public void setRange(RatingRange ranges, Range range) {
            ranges.setmRanges(range);
        }

    }

    private static class AExtractor extends RatingExtractor {

        @Override
        public int getValue(Rating rating) {
            return rating.a;
        }

        @Override
        public Range getRange(RatingRange ranges) {
            return ranges.aRanges;
        }

        @Override
        public void setRange(RatingRange ranges, Range range) {
            ranges.setaRanges(range);
        }
    }

    private static class SExtractor extends RatingExtractor {

        @Override
        public int getValue(Rating rating) {
            return rating.s;
        }

        @Override
        public Range getRange(RatingRange ranges) {
            return ranges.sRanges;
        }

        @Override
        public void setRange(RatingRange ranges, Range range) {
            ranges.setsRanges(range);
        }
    }

    private static class RuleBuilder {

        private final String workFlowName;
        private final RatingPredicate predicate;

        private RuleBuilder next;

        private RuleBuilder(String workFlowName, RatingPredicate predicate) {
            this.workFlowName = workFlowName;
            this.predicate = predicate;
        }

        public void setNext(RuleBuilder next) {
            this.next = next;
        }

        public Rule build() {
            return new Rule(predicate, WorkFlowBuilder.of(workFlowName).build(), next == null ? null : next.build());
        }

    }

    private static class WorkFlowBuilder {

            private static final Map<String, WorkFlowBuilder> builders = new HashMap<>();

            private RuleBuilder rule;

            private WorkFlow workFlow;

            private WorkFlowBuilder() {}

            public void setRuleBuilder(RuleBuilder rule) {
                this.rule = rule;
            }

                public static WorkFlowBuilder of(String name) {
                    if (name.equals("A")) return AcceptWorkFlowBuilder.of();
                    if (name.equals("R")) return RejectWorkFlowBuilder.of();
                    return builders.computeIfAbsent(name, n -> new WorkFlowBuilder());
                }

            public WorkFlow build() {
                if (workFlow != null) {
                    return workFlow;
                }

                workFlow = new WorkFlow();
                workFlow.setRule(rule.build());
                return workFlow;
            }
    }

    private static class AcceptWorkFlowBuilder extends WorkFlowBuilder {

        private static final AcceptWorkFlowBuilder builder = new AcceptWorkFlowBuilder();

        private AcceptWorkFlowBuilder() {}

        public static AcceptWorkFlowBuilder of() {
            return builder;
        }

        @Override
        public WorkFlow build() {
            return AcceptWorkFlow.of();
        }
    }

    private static class RejectWorkFlowBuilder extends WorkFlowBuilder {

        private static final RejectWorkFlowBuilder builder = new RejectWorkFlowBuilder();

        private RejectWorkFlowBuilder() {}

        public static RejectWorkFlowBuilder of() {
            return builder;
        }

        @Override
        public WorkFlow build() {
            return RejectWorkFlow.of();
        }
    }

}
