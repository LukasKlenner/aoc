package aoc.util;

public class ListItem<T> extends AbstractListItem<ListItem<T>> {

    public T value;

    public ListItem(T value) {
        this.value = value;
    }

}
