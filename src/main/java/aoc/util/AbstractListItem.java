package aoc.util;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractListItem<L extends AbstractListItem<L>> {

    public L prev;
    public L next;

    private final L thiz;

    public AbstractListItem() {
        //noinspection unchecked
        thiz = (L) this;
    }

    public L findNext(Predicate<L> p, Consumer<L> onEach, boolean startNext) {
        L current = startNext ? next : thiz;

        while (current != null) {
            onEach.accept(current);
            if (p.test(current)) return current;
            current = current.next;
        }

        return null;
    }

    public L findNextUntilStop(Predicate<L> p, L stop, boolean startNext) {
        L current = startNext ? next : thiz;

        while (current != null && current != stop) {
            if (p.test(current)) return current;
            current = current.next;
        }

        return null;
    }


    public L findPrev(Predicate<L> p, Consumer<L> onEach, boolean startPrev) {
        L current = startPrev ? prev : thiz;

        while (current != null) {
            onEach.accept(current);
            if (p.test(current)) return current;
            current = current.prev;
        }

        return null;
    }

    public L findPrefUntilStop(Predicate<L> p, L stop, boolean startPrev) {
        L current = startPrev ? prev : thiz;

        while (current != null && current != stop) {
            if (p.test(current)) return current;
            current = current.prev;
        }

        return null;
    }

    public void insertAfter(L toInsert) {
        L nextNext = next;

        this.next = toInsert;
        toInsert.prev = thiz;

        toInsert.next = nextNext;
        nextNext.prev = toInsert;
    }

}