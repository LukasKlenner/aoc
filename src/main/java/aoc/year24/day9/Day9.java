package aoc.year24.day9;

import aoc.Day;
import aoc.util.AbstractListItem;
import aoc.util.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Day9 implements Day {

    @Override
    public long part1(Stream<String> stream) {
        Pair<ListItemDay9, ListItemDay9> list = createList(stream);

        ListItemDay9 start = list.first;
        ListItemDay9 end = list.second;

        ListItemDay9 currentEmpty = start.findNextEmpty(l -> {}, false);
        ListItemDay9 currentFull = end.findPrevFull(l -> {}, false);

        while (currentFull != null) {
            int transferred = 0;

            while (transferred < currentFull.size && currentEmpty != null) {
                transferred += currentEmpty.transfer(currentFull, transferred);
                currentEmpty = currentEmpty.findNextEmptyUntilStop(currentFull, false);
            }

            if (transferred == currentFull.size) {
                currentFull.id = -1;
            } else {
                currentFull.insertAfter(new ListItemDay9(transferred, -1));
                currentFull.size = currentFull.size - transferred;
                break;
            }

            currentFull = currentFull.findPrevFullUntilStop(currentEmpty, false);
        }

        return getResult(start);
    }

    @Override
    public long part2(Stream<String> stream) {
        Pair<ListItemDay9, ListItemDay9> list = createList(stream);

        ListItemDay9 start = list.first;
        ListItemDay9 end = list.second;

        ListItemDay9 firstEmpty = start.findNextEmpty(l -> {}, false);
        ListItemDay9 currentFull = end.findPrevFull(l -> {}, false);

        while (currentFull != null) {
            int neededSize = currentFull.size;

            ListItemDay9 fittingEmpty = firstEmpty.findNextUntilStop(l -> l.isEmpty() && l.size >= neededSize, currentFull, false);

            if (fittingEmpty != null) {
                int remainingSize = fittingEmpty.size - currentFull.size;

                fittingEmpty.id = currentFull.id;
                fittingEmpty.size = currentFull.size;

                currentFull.id = -1;

                if (remainingSize > 0) {
                    fittingEmpty.insertAfter(new ListItemDay9(remainingSize, -1));
                }
            }

            currentFull = currentFull.findPrevFullUntilStop(firstEmpty, true);
        }

        return getResult(start);
    }

    private Pair<ListItemDay9, ListItemDay9> createList(Stream<String> stream) {
        List<Integer> sizes = stream.findAny().get().chars().mapToObj(Character::getNumericValue).toList();

        ListItemDay9 start = null;
        ListItemDay9 end = null;

        ListItemDay9 prev = null;

        for (int i = 0; i < sizes.size(); i++) {
            ListItemDay9 newItem = new ListItemDay9(sizes.get(i), i % 2 == 0 ? i / 2 : -1);

            newItem.prev = prev;

            if (prev != null) prev.next = newItem;
            prev = newItem;

            if (start == null) start = newItem;
            end = newItem;
        }

        return new Pair<>(start, end);
    }

    private long getResult(ListItemDay9 start) {
        long result = 0;
        int index = 0;

        ListItemDay9 current = start;

        while (current != null) {

            for (int i = 0; i < current.size; i++) {
                if (!current.isEmpty()) result += (long) current.id * index;

                index++;
            }

            current = current.next;
        }

        return result;
    }

    private static class ListItemDay9 extends AbstractListItem<ListItemDay9> {

        int size;
        int id;
        
        public ListItemDay9(int size, int id) {
            this.size = size;
            this.id = id;
        }
        
        public boolean isEmpty() {
            return id == -1;
        }

        public boolean isFull() {
            return !isEmpty();
        }

        public ListItemDay9 findNextEmpty(Consumer<ListItemDay9> onEach, boolean startPrev) {
            return findNext(ListItemDay9::isEmpty, onEach, startPrev);
        }

        public ListItemDay9 findNextEmptyUntilStop(ListItemDay9 stop, boolean startPrev) {
            return findNextUntilStop(ListItemDay9::isEmpty, stop, startPrev);
        }

        public ListItemDay9 findPrevFull(Consumer<ListItemDay9> onEach, boolean startPrev) {
            return findPrev(ListItemDay9::isFull, onEach, startPrev);
        }

        public ListItemDay9 findPrevFullUntilStop(ListItemDay9 stop, boolean startPrev) {
            return findPrefUntilStop(ListItemDay9::isFull, stop, startPrev);
        }

        public int transfer(ListItemDay9 item, int transferred) {
            int maxTransfer = Math.min(size, item.size - transferred);

            id = item.id;

            if (maxTransfer < size) {
                insertAfter(new ListItemDay9(size - maxTransfer, -1));
                size = maxTransfer;
            }

            return maxTransfer;
        }

    }
}
