package aoc.year24.day8;

import aoc.JoinedDay;
import aoc.util.GridTask;
import aoc.util.JoinedGridTask;
import aoc.util.Pos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day8 extends JoinedGridTask<Character> {


    public Day8() {
        super(Function.identity(), Character[]::new, Character[][]::new);
    }

    @Override
    public long run(boolean part1) {

        Map<Character, Set<Pos>> antennasMap = new HashMap<>();
        Set<Pos> results = new HashSet<>();

        foreachCell((pos, c) -> {
            if (c == '.') return;
            antennasMap.computeIfAbsent(c, k -> new HashSet<>()).add(pos);
        });

        for (Set<Pos> antennas : antennasMap.values()) {
            for (Pos pos1 : antennas) {
                for (Pos pos2 : antennas) {
                    if (pos1.equals(pos2)) continue;

                    Pos smaller = pos1.compareTo(pos2) < 0 ? pos1 : pos2;
                    Pos larger = smaller == pos1 ? pos2 : pos1;

                    Pos diff = larger.sub(smaller);

                    Pos res1 = part1 ? larger.add(diff) : larger;
                    Pos res2 = part1 ? smaller.sub(diff) : smaller;

                    while (isInBounds(res1) || isInBounds(res2)) {
                        if (isInBounds(res1)) results.add(res1);
                        if (isInBounds(res2)) results.add(res2);

                        res1 = res1.add(diff);
                        res2 = res2.sub(diff);

                        if (part1) break;
                    }
                }
            }
        }

        return results.size();
    }
}
