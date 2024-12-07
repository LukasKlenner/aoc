package aoc.year23.day24;

import aoc.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day24 implements Day {
    @Override
    public long part1(Stream<String> linesStream) {

        List<Line> lines = new ArrayList<>();

        linesStream.map(Line::of).forEach(lines::add);

        int count = 0;

        for (int i = 0; i < lines.size() - 1; i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                if (lines.get(i).intersects2D(lines.get(j))) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override
    public long part2(Stream<String> lines) {
        return 0;
    }

    private record Line(Vec pos, Vec dir) {

        private static final long min = 200000000000000L;
        private static final long max = 400000000000000L;

        public boolean isParallel2D(Line other) {

            double c1 = (double) dir.x / other.dir.x;
            double c2 = (double) dir.y / other.dir.y;

            return Math.abs(c1 - c2) < 0.0000001;
        }


        public boolean intersects2D(Line other) {

            if (isParallel2D(other)) {
                return false;
            }

            //s= (b_1 + a_0*y_1/x_1  - a_1*y_1/x_1 - b_0) / (y_0 - x_0*y_1/x_1)

            double c1 = (double) other.dir.y / other.dir.x;

            double numerator = other.pos.y + pos.x * c1 - other.pos.x * c1 - pos.y;
            double denominator = dir.y - dir.x * c1;

            double s = numerator / denominator;

            //t = (a_0  + s*x_0 - a_1) / x_1

            double t = (pos.x + s * dir.x - other.pos.x) / other.dir.x;

            double x0 = pos.x + s * dir.x;
            double y0 = pos.y + s * dir.y;

            double x1 = other.pos.x + t * other.dir.x;
            double y1 = other.pos.y + t * other.dir.y;

            System.out.println(x0 + " " + y0);
            System.out.println(x1 + " " + y1);

            if (s >= 0 && t >= 0) {
                return x0 >= min && x0 <= max && y0 >= min && y0 <= max;
            }
            return false;
        }

        public static Line of(String str) {

            String[] split = str.split(" @ ");

            String[] coords = split[0].split(",");
            String[] dir = split[1].split(",");

            Vec pos = new Vec(Long.parseLong(coords[0].trim()), Long.parseLong(coords[1].trim()), Long.parseLong(coords[2].trim()));
            Vec dirVec = new Vec(Long.parseLong(dir[0].trim()), Long.parseLong(dir[1].trim()), Long.parseLong(dir[2].trim()));

            return new Line(pos, dirVec);
        }
    }

    private record Vec(long x, long y, long z) {
    }

}
