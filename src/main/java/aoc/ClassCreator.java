package aoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassCreator {

    public static final int YEAR = 24;

    public static void main(String[] args) throws IOException {

        for (int i = 4; i < 26; i++) {
            File file = new File("src/main/java/aoc/year" + YEAR + "/day" + i + "/Day" + i + ".java");
            file.getParentFile().mkdirs();
            file.createNewFile();

            try (FileWriter fw = new FileWriter(file)) {

                fw.write("""
package aoc.year24.day%d;

import aoc.JoinedDay;

import java.util.stream.Stream;

public class Day%d implements JoinedDay {

    @Override
    public Object run(Stream<String> stream, boolean part1) {
        return null;
    }
    
}
""".formatted(i, i));
            }

            file = new File("src/main/resources/aoc/year" + YEAR + "/day" + i + "/input.txt");
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

    }

}
