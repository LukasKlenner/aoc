package aoc.year23.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 implements aoc.Day {

    @Override
    public long part1(Stream<String> linesStream) {

        List<String> lines = linesStream.collect(Collectors.toList());
        lines.add("");
        List<Long> seeds = Arrays.stream(lines.get(0).substring(7).split(" ")).map(Long::parseLong).toList();

        AtomicInteger index = new AtomicInteger(2);

        Node seedToSoil = readNextMap(lines, index);
        Node soilToFertilizer = readNextMap(lines, index);
        Node fertilizerToWater = readNextMap(lines, index);
        Node waterToLight = readNextMap(lines, index);
        Node lightToTemperature = readNextMap(lines, index);
        Node temperatureHumidity = readNextMap(lines, index);
        Node humidityToLocation = readNextMap(lines, index);


        return seeds.stream().mapToLong(seed ->
                        humidityToLocation.getMapping(
                                temperatureHumidity.getMapping(
                                        lightToTemperature.getMapping(
                                                waterToLight.getMapping(
                                                        fertilizerToWater.getMapping(
                                                                soilToFertilizer.getMapping(
                                                                        seedToSoil.getMapping(seed))))))))
                .min().getAsLong();
    }

    @Override
    public long part2(Stream<String> linesStream) {
        List<String> lines = linesStream.collect(Collectors.toList());
        lines.add("");
        List<Long> seeds = Arrays.stream(lines.get(0).substring(7).split(" ")).map(Long::parseLong).toList();
        List<ValueRange> seedRanges = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i += 2) {
            seedRanges.add(new ValueRange(seeds.get(i), seeds.get(i + 1)));
        }

        AtomicInteger index = new AtomicInteger(2);

        Node seedToSoil = readNextMap(lines, index);
        Node soilToFertilizer = readNextMap(lines, index);
        Node fertilizerToWater = readNextMap(lines, index);
        Node waterToLight = readNextMap(lines, index);
        Node lightToTemperature = readNextMap(lines, index);
        Node temperatureHumidity = readNextMap(lines, index);
        Node humidityToLocation = readNextMap(lines, index);

        return seedRanges.stream().map(seed ->
                humidityToLocation.getMapping(
                        temperatureHumidity.getMapping(
                                lightToTemperature.getMapping(
                                        waterToLight.getMapping(
                                                fertilizerToWater.getMapping(
                                                        soilToFertilizer.getMapping(
                                                                seedToSoil.getMapping(seed))))))))
                .flatMap(List::stream).mapToLong(ValueRange::start).min().getAsLong();
    }

    private Node readNextMap(List<String> lines, AtomicInteger index) {
        String line;
        index.incrementAndGet();

        Node treeMap = null;

        while (!(line = lines.get(index.get())).isEmpty()) {
            Long[] values = Arrays.stream(line.split(" ")).map(Long::parseLong).toArray(Long[]::new);

            if (treeMap == null) {
                treeMap = new Node(new MappingRange(values[0], values[1], values[2]));
            } else {
                treeMap.insert(new MappingRange(values[0], values[1], values[2]));
            }

            index.incrementAndGet();
        }

        index.incrementAndGet();
        return treeMap;
    }

    private record ValueRange(long start, long length) implements Comparable<ValueRange> {

        private ValueRange {
            if (length < 0) {
                throw new RuntimeException("Length cannot be negative");
            }
            if (start < 0) {
                throw new RuntimeException("Start cannot be negative");
            }
        }

        public long getEnd() {
            return start + length - 1;
        }

        public boolean isAdjacentToSorted(ValueRange valueRange) {
            return start <= valueRange.start && getEnd() + 1 >= valueRange.start;
        }

        public ValueRange merge(ValueRange valueRange) {
            long newStart = Math.min(start, valueRange.start);
            long newEnd = Math.max(getEnd(), valueRange.getEnd());
            return new ValueRange(newStart, newEnd - newStart + 1);
        }

        public ValueRange[] split(MappingRange range) {

            ValueRange[] ranges = new ValueRange[3];

            if (getEnd() < range.sourceStart) {
                ranges[0] = this;
                return ranges;
            }

            if (start > range.getSourceEnd()) {
                ranges[2] = this;
                return ranges;
            }

            if (range.sourceStart <= start && getEnd() <= range.getSourceEnd()) {
                ranges[1] = this;
                return ranges;
            }


            if (start < range.sourceStart) {
                ranges[0] = new ValueRange(start, range.sourceStart - start);
            }

            long valuesLeft = length - (ranges[0] == null ? 0 : ranges[0].length);

            long middleRangeStart = Math.max(range.sourceStart, start);
            if (valuesLeft < range.getSourceEnd() - middleRangeStart + 1) {
                ranges[1] = new ValueRange(middleRangeStart, valuesLeft);
            } else {
                ranges[1] = new ValueRange(middleRangeStart, range.getSourceEnd() - middleRangeStart + 1);
            }

            valuesLeft -= ranges[1].length;

            if (valuesLeft > 0) {
                ranges[2] = new ValueRange(range.getSourceEnd() + 1, valuesLeft);
            }

            return ranges;
        }

        @Override
        public int compareTo(ValueRange o) {
            return Long.compare(start, o.start);
        }

        public static ValueRange of(long start, long end) {
            return new ValueRange(start, end - start + 1);
        }

        @Override
        public String toString() {
            return "[%d, %d]".formatted(start, getEnd());
        }
    }

    private record MappingRange(long destinationStart, long sourceStart, long length) {

        public long getDestinationEnd() {
            return destinationStart + length - 1;
        }

        public long getSourceEnd() {
            return sourceStart + length - 1;
        }

        public boolean isHigherThan(MappingRange mappingRange) {
            return sourceStart > mappingRange.sourceStart;
        }

        public boolean isLowerThan(MappingRange mappingRange) {
            return sourceStart < mappingRange.sourceStart;
        }

        public long getMapping(long index) {
            return destinationStart + (index - sourceStart);
        }

        public ValueRange getMapping(ValueRange range) {
            if (range.start < sourceStart) {
                throw new RuntimeException("Range is lower than source");
            }

            if (range.getEnd() > getSourceEnd()) {
                throw new RuntimeException("Range is higher than source");
            }

            return new ValueRange(getMapping(range.start), range.length);
        }

        @Override
        public String toString() {
            return "[%d, %d] -> [%d, %d]".formatted(sourceStart, getSourceEnd(), destinationStart, getDestinationEnd());
        }
    }

    private static class Node {

        MappingRange mappingRange;
        Node left;
        Node right;

        public Node(MappingRange mappingRange) {
            this.mappingRange = mappingRange;
        }

        public void insert(MappingRange mappingRange) {
            if (mappingRange.isLowerThan(this.mappingRange)) {
                if (left == null) left = new Node(mappingRange);
                else left.insert(mappingRange);
            } else if (mappingRange.isHigherThan(this.mappingRange)) {
                if (right == null) right = new Node(mappingRange);
                else right.insert(mappingRange);
            } else {
                throw new RuntimeException("Range already exists");
            }
        }

        public long getMapping(long index) {
            if (index < mappingRange.sourceStart) {
                return left == null ? index : left.getMapping(index);
            }
            if (index > mappingRange.getSourceEnd()) {
                return right == null ? index : right.getMapping(index);
            }

            return mappingRange.getMapping(index);
        }

        public List<ValueRange> getMapping(ValueRange range) {


            ValueRange[] split = range.split(mappingRange);

            List<ValueRange> mappedRanges = new ArrayList<>();
            if (split[0] != null) mappedRanges.addAll(left == null ? List.of(split[0]) : left.getMapping(split[0]));
            if (split[1] != null) mappedRanges.add(mappingRange.getMapping(split[1]));
            if (split[2] != null) mappedRanges.addAll(right == null ? List.of(split[2]) : right.getMapping(split[2]));

            return mappedRanges;
        }

        public List<ValueRange> getMapping(List<ValueRange> ranges) {
            List<ValueRange> mappedRanges = new ArrayList<>();
            for (ValueRange range : ranges) {
                mappedRanges.addAll(getMapping(range));
            }

            mappedRanges.sort(ValueRange::compareTo);

            for (int i = 0; i < mappedRanges.size() - 1; i++) {
                if (mappedRanges.get(i).isAdjacentToSorted(mappedRanges.get(i + 1))) {
                    mappedRanges.set(i, mappedRanges.get(i).merge(mappedRanges.get(i + 1)));
                    mappedRanges.remove(i + 1);
                    i--;
                }
            }

            return mappedRanges;
        }

    }

    // Testing

    public static void main(String[] args) {
        ValueRange rangeToSplit = ValueRange.of(0, 5);
        MappingRange range = new MappingRange(0, 5, 5);
        testSplit(rangeToSplit, range);

        for (int i = 0; i < 13; i++) {
            rangeToSplit = ValueRange.of(i, i + 5);
            testSplit(rangeToSplit, range);
        }

        rangeToSplit = ValueRange.of(0, 15);
        testSplit(rangeToSplit, range);

        rangeToSplit = ValueRange.of(4, 10);
        testSplit(rangeToSplit, range);

        rangeToSplit = ValueRange.of(4, 11);
        testSplit(rangeToSplit, range);

        rangeToSplit = ValueRange.of(5, 10);
        testSplit(rangeToSplit, range);

        rangeToSplit = ValueRange.of(5, 11);
        testSplit(rangeToSplit, range);

        rangeToSplit = ValueRange.of(5, 9);
        testSplit(rangeToSplit, range);

        rangeToSplit = ValueRange.of(90, 98);
        range = new MappingRange(60, 56, 37);
        testSplit(rangeToSplit, range);
    }

    private static void testSplit(ValueRange rangeToSplit, MappingRange range) {
        System.out.println(rangeToSplit);
        System.out.println(range);

        System.out.println(Arrays.toString(rangeToSplit.split(range)));
        System.out.println("---");
    }

}