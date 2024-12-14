package aoc.year24.day14;

import aoc.Day;
import aoc.JoinedDay;

import java.util.List;
import java.util.stream.Stream;

public class Day14 implements Day {

    int width = 101;
    int height = 103;

    @Override
    public long part1(Stream<String> lines) {
        List<Robot> robots = getRobots(lines);

        for (Robot robot : robots) {
            robot.move(100);
        }

        long quad1 = 0;
        long quad2 = 0;
        long quad3 = 0;
        long quad4 = 0;

        int midX = width / 2;
        int midY = height / 2;

        for (Robot robot : robots) {
            if (robot.x < midX && robot.y < midY) {
                quad1++;
            } else if (robot.x > midX && robot.y < midY) {
                quad2++;
            } else if (robot.x < midX && robot.y > midY) {
                quad3++;
            } else if (robot.x > midX && robot.y > midY){
                quad4++;
            }
        }

        return quad1 * quad2 * quad3 * quad4;
    }

    @Override
    public long part2(Stream<String> lines) {
        List<Robot> robots = getRobots(lines);


        for (int i = 0; i < 100000; i++) {
            boolean[][] grid = new boolean[height][width];

            for (Robot robot : robots) {
                robot.move(1);
                grid[robot.y][robot.x] = true;
            }

            double distances = 0;

            for (int j = 0; j < robots.size(); j++) {
                for (int k = 0; k < j; k++) {
                    distances += Math.sqrt(
                            Math.pow(Math.abs(robots.get(j).x - robots.get(k).x), 2) +
                            Math.pow(Math.abs(robots.get(j).y - robots.get(k).y), 2));
                }
            }

            double averageDistance = distances / (robots.size() * (robots.size() - 1) / 2.0);

            if (averageDistance < 40) {
                System.out.println("Time: " + (i + 1) + " Average distance: " + averageDistance);
                print(grid);
                System.out.println();
            }
        }

        return 0;
    }

    private List<Robot> getRobots(Stream<String> stream) {
        return stream.map(line -> {
            String[] parts = line.split(" ");

            String[] pos = parts[0].substring(2).split(",");
            String[] vel = parts[1].substring(2).split(",");

            int x = Integer.parseInt(pos[0]);
            int y = Integer.parseInt(pos[1]);
            int dx = Integer.parseInt(vel[0]);
            int dy = Integer.parseInt(vel[1]);
            return new Robot(x, y, dx, dy);
        }).toList();
    }

    private void print(boolean[][] grid) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[y][x] ? '#' : '.');
            }
            System.out.println();
        }
    }

    private class Robot {

        int x;
        int y;

        int dx;
        int dy;

        public Robot(int x, int y, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }

        public void move(int count) {
            for (int i = 0; i < count; i++) {
                x = Math.floorMod(x + dx, width);
                y = Math.floorMod(y + dy, height);
            }
        }
    }

}
