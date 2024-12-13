package aoc.year24.day13;

import aoc.JoinedDay;
import aoc.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Stream;

public class Day13 implements JoinedDay {

    @Override
    public long run(Stream<String> stream, boolean part1) {

        List<String> input = stream.toList();

        long prize = 0;

        for (int i = 0; i < input.size(); i += 4) {
            Pair<Integer, Integer> equation1 = parseXY(input.get(i));
            Pair<Integer, Integer> equation2 = parseXY(input.get(i + 1));
            Pair<Integer, Integer> targetXY = parseXY(input.get(i + 2));

            BigDecimal x1 = BigDecimal.valueOf(equation1.first, 100);
            BigDecimal y1 = BigDecimal.valueOf(equation1.second, 100);
            BigDecimal x2 = BigDecimal.valueOf(equation2.first, 100);
            BigDecimal y2 = BigDecimal.valueOf(equation2.second, 100);
            BigDecimal Px = BigDecimal.valueOf(targetXY.first + (part1 ? 0 : 10000000000000L), 100);
            BigDecimal Py = BigDecimal.valueOf(targetXY.second + (part1 ? 0 : 10000000000000L), 100);

            BigDecimal bd = Py.subtract(Px.multiply(y1.divide(x1, RoundingMode.HALF_DOWN)))
                    .divide(y2.subtract(y1.multiply(x2.divide(x1, RoundingMode.HALF_DOWN)))
                            , RoundingMode.HALF_DOWN);
            BigDecimal ad = Px.subtract(bd.multiply(x2)).divide(x1, RoundingMode.HALF_DOWN);

            double a = ad.doubleValue();
            double b = bd.doubleValue();

            double epsilon = 0.0000001;

            if (Math.abs(a - Math.round(a)) > epsilon || Math.abs(b - Math.round(b)) > epsilon) {
                continue;
            }

            prize += Math.round(a) * 3L + Math.round(b);

        }

        return prize;
    }

    private Pair<Integer, Integer> parseXY(String str) {
        String[] split = str.split(",");
        return new Pair<>(Integer.parseInt(split[0].substring(split[0].indexOf("X") + 2)),
                Integer.parseInt(split[1].substring(split[1].indexOf("Y") + 2)));
    }

}
