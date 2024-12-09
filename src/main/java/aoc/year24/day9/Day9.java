package aoc.year24.day9;

import aoc.Day;
import aoc.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day9 implements Day {

    @Override
    public long part1(Stream<String> stream) {
        Pair<ListItem, ListItem> list = createList(stream);

        ListItem start = list.first;
        ListItem end = list.second;

        ListItem currentEmpty = start.getNextEmpty();
        ListItem currentFull = end.getPrevFull();

        while (isEmptyInFront(currentEmpty, currentFull)) {

            int transferred = 0;

            while (transferred < currentFull.size && isEmptyInFront(currentEmpty, currentFull)) {
                transferred += currentEmpty.transfer(currentFull, transferred);
                currentEmpty = currentEmpty.getNextEmpty();
            }

            if (transferred == currentFull.size) {
                currentFull.id = -1;
            } else {
                currentFull.insertNewItem(transferred);
                currentFull.size = currentFull.size - transferred;
                break;
            }

            currentFull = currentFull.getPrevFull();
        }

        return getResult(start);
    }

    @Override
    public long part2(Stream<String> stream) {
        Pair<ListItem, ListItem> list = createList(stream);

        ListItem start = list.first;
        ListItem end = list.second;

        ListItem currentEmpty = start.getNextEmpty();
        ListItem currentFull = end.getPrevFull();

        List<ListItem> emptyItems = new ArrayList<>();
        emptyItems.add(currentEmpty);

        while (isEmptyInFront(currentEmpty, currentFull) || !emptyItems.isEmpty()) {

            ListItem foundItem = null;

            for (int i = 0; i < emptyItems.size(); i++) {
                ListItem item = emptyItems.get(i);
                if (item.isEmpty() && item.size < currentFull.size) {
                    foundItem = item;
                    emptyItems.remove(item);
                }
            }

            if (foundItem == null) {
                while (isEmptyInFront(currentEmpty, currentFull)) {
                    currentEmpty = currentEmpty.getNextEmpty();
                    if (currentEmpty.size < currentFull.size) {
                        foundItem = currentEmpty;
                        break;
                    }
                    else emptyItems.add(currentEmpty);
                }
            }

            if (foundItem == null) continue;


            //TODO letztes item aus emtpyItems entfernen wenn PrevFull darauf trifft
        }

       return getResult(start);
    }

    private Pair<ListItem, ListItem> createList(Stream<String> stream) {
        List<Integer> sizes = stream.findAny().get().chars().mapToObj(Character::getNumericValue).toList();

        ListItem start = null;
        ListItem end = null;

        ListItem prev = null;

        for (int i = 0; i < sizes.size(); i++) {
            ListItem newItem = new ListItem(sizes.get(i), i % 2 == 0 ? i / 2 : -1);

            newItem.prev = prev;

            if (prev != null) prev.next = newItem;
            prev = newItem;

            if (start == null) start = newItem;
            end = newItem;
        }

        return new Pair<>(start, end);
    }

    private boolean isEmptyInFront(ListItem empty, ListItem full) {
//        return empty.next.id <= full.id;
        return empty.prev != full;
    }

    private long getResult(ListItem start) {
        long result = 0;
        int index = 0;

        ListItem current = start;

        while (current != null) {

            for (int i = 0; i < current.size; i++) {
                if (!current.isEmpty()) {
                    result += (long) current.id * index;
                    index++;
                }
            }

            current = current.next;
        }

        return result;
    }

    private void print(ListItem start) {

        ListItem current = start;

        while (current != null) {
            for (int i = 0; i < current.size; i++) {
                if (current.isEmpty()) System.out.print('.');
                else System.out.print(current.id);
            }

            current = current.next;
        }

        System.out.println();
    }


    private class ListItem {

        int size;
        int id;

        ListItem next;
        ListItem prev;

        public ListItem(int size, int id) {
            this.size = size;
            this.id = id;
        }

        public boolean isEmpty() {
            return id == -1;
        }

        public ListItem getNextEmpty() {
            ListItem current = next;

            while (!current.isEmpty()) {
                current = current.next;
            }

            return current;
        }

        public ListItem getPrevFull() {
            ListItem current = this;

            while (current.isEmpty()) {
                current = current.prev;
            }

            return current;
        }

        public int transfer(ListItem item, int transferred) {
            int maxTransfer = Math.min(size, item.size - transferred);

            id = item.id;

            if (maxTransfer < size) {
                insertNewItem(size - maxTransfer);
                size = maxTransfer;
            }

            return maxTransfer;
        }

        private void insertNewItem(int size) {
            ListItem newItem = new ListItem(size, -1);

            newItem.next = next;
            newItem.prev = this;

            if (next != null) next.prev = newItem;
            next = newItem;
        }

    }
}
