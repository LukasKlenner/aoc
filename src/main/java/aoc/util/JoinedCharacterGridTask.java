package aoc.util;

import java.util.function.Function;

public abstract class JoinedCharacterGridTask extends JoinedGridTask<Character> {

    public JoinedCharacterGridTask() {
        super(Function.identity(), Character[]::new, Character[][]::new);
    }

}
