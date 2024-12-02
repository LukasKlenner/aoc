package aoc.year23.day4;

import aoc.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day4 implements Day {
    @Override
    public Object part1(Stream<String> lines) {
        return lines.map(Card::new).mapToInt(Card::getScore).sum();
    }

    @Override
    public Object part2(Stream<String> lines) {

        List<Card> cards = lines.map(Card::new).toList();
        List<Integer> counts = new ArrayList<>();

        for (Card ignored : cards) {
            counts.add(1);
        }

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            int winnerMatches = card.getWinnerMatches();

            for (int j = 0; j < winnerMatches; j++) {
                int index = i + j + 1;
                if (index >= cards.size()) {
                    break;
                }
                counts.set(index, counts.get(index) + counts.get(i));
            }
        }

        return counts.stream().mapToInt(Integer::intValue).sum();

    }

    private static class Card {
        List<String> winners;
        List<String> pulls;

        public Card(String card) {
            String[] split = card.substring(8).split(" \\| ");
            winners = Arrays.stream(split[0].split("( )+")).toList();
            pulls = Arrays.stream(split[1].split("( )+")).toList();
        }

        public int getWinnerMatches() {
            return (int) pulls.stream().filter(s -> winners.contains(s)).count();
        }

        public int getScore() {
            int winnerMatches = getWinnerMatches();
            return winnerMatches == 0 ? 0 : (int) Math.pow(2, winnerMatches - 1);
        }
    }

}
