package aoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassCreator {

    public static final int YEAR = 25;

    public static void main(String[] args) throws IOException {

        for (int i = 1; i < 26; i++) {

            String day = i < 10 ? "0" + i : Integer.toString(i);

            File file = new File("src/main/kotlin/aoc/year" + YEAR + "/Day" + day + ".kt");
            file.getParentFile().mkdirs();
            file.createNewFile();

            try (FileWriter fw = new FileWriter(file)) {

                fw.write("""
package aoc.year%s

import aoc.JoinedDay
import java.util.stream.Stream

class Day%s : JoinedDay {
    
    override fun run(stream: Stream<String?>?, part1: Boolean): Long {
        TODO("Not yet implemented")
    }
}
""".formatted(YEAR, day));
            }

            file = new File("src/main/resources/aoc/year" + YEAR + "/day" + day + "_input.txt");
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

    }

}
