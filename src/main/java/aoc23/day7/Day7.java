package aoc23.day7;

import aoc23.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 implements Day {


    @Override
    public Object part1(Stream<String> lines) {
        return day7(lines, true);
    }

    @Override
    public Object part2(Stream<String> lines) {
        return day7(lines, false);
    }

    private Object day7(Stream<String> lines, boolean part1) {
        HashMap<Type, List<Long>>  typeToCards = new HashMap<>();
        HashMap<Long, Integer> cardToBid = new HashMap<>();

        lines.map(line -> line.split(" "))
                .map(line -> new String[]{line[0].replaceAll("A", "E")
                        .replaceAll("K", "D")
                        .replaceAll("Q", "C")
                        .replaceAll("J", part1 ? "B" : "1")
                        .replaceAll("T", "A"), line[1]})
                .forEach(line -> {
                    long card = Long.parseLong(line[0].chars().mapToObj(Integer::toString).collect(Collectors.joining()));
                    typeToCards.computeIfAbsent(Type.of(line[0].chars().toArray()), k -> new ArrayList<>()).add(card);
                    cardToBid.put(card, Integer.parseInt(line[1]));
                });

        List<Long> orderedHands = new ArrayList<>();

        for (Type type : Type.values()) {
            if (typeToCards.containsKey(type)) {
                orderedHands.addAll(typeToCards.get(type).stream().sorted(Long::compare).toList());
            }
        }

        return IntStream.range(0, orderedHands.size()).mapToLong(i -> (long) cardToBid.get(orderedHands.get(i)) * (i +1)).sum();
    }

    private enum Type {
        HighCard,
        OnePair,
        TwoPairs,
        ThreeOfAKind,
        FullHouse,
        FourOfAKind,
        FiveOfAKind;

        public static Type of(int[] labels) {
            Arrays.sort(labels);
            List<Integer> labelCounts = new ArrayList<>();
            HashMap<Integer, Integer> counts = new HashMap<>();

            Integer currentLabel = null;
            for (Integer label : labels) {
                if (label.equals(currentLabel)) {
                    labelCounts.set(labelCounts.size() - 1, labelCounts.get(labelCounts.size() - 1) + 1);
                    counts.put(label, counts.get(label) + 1);
                } else {
                    currentLabel = label;
                    labelCounts.add(1);
                    counts.put(label, 1);
                }
            }

            int jokerCount = counts.getOrDefault((int) '1', 0);

            if (jokerCount > 0) {
                labelCounts.remove((Object) jokerCount);

                if (jokerCount == 5) {
                    return FiveOfAKind;
                }
                else if (jokerCount == 4) {
                    return FiveOfAKind;
                } else if (jokerCount == 3) {
                    if (labelCounts.contains(2)) {
                        return FiveOfAKind;
                    } else {
                        return FourOfAKind;
                    }
                } else if (jokerCount == 2) {
                    if (labelCounts.contains(3)) {
                        return FiveOfAKind;
                    } else if (labelCounts.contains(2)) {
                        return FourOfAKind;
                    } else {
                        return ThreeOfAKind;
                    }
                } else {
                    if (labelCounts.contains(4)) {
                        return FiveOfAKind;
                    } else if (labelCounts.contains(3)) {
                        return FourOfAKind;
                    } else if (labelCounts.contains(2)) {
                        if (labelCounts.size() == 2) {
                            return FullHouse;
                        } else {
                            return ThreeOfAKind;
                        }
                    } else {
                        return OnePair;
                    }
                }
            }

            if (labelCounts.size() == 1) {
                return FiveOfAKind;
            }
            if (labelCounts.size() == 2) {
                if (labelCounts.contains(4)) {
                    return FourOfAKind;
                }
                return FullHouse;
            }
            if (labelCounts.size() == 3) {
                if (labelCounts.contains(3)) {
                    return ThreeOfAKind;
                }
                return TwoPairs;
            }
            if (labelCounts.size() == 4) {
                return OnePair;
            }
            return HighCard;
        }
    }
}
