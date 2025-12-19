package aoc.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Range<T extends Comparable<T>> implements Iterable<Range.InclusiveRange<T>> {

    public abstract boolean contains(T value);

    public abstract Range<T> union(Range<T> other);

    public abstract T getLowerBound();

    public abstract T getUpperBound();

    public static <T extends Comparable<T>> Range<T> inclusive(T start, T end) {
        return new InclusiveRange<>(start, end);
    }

    public static <T extends Comparable<T>> Range<T> ofList(List<InclusiveRange<T>> ranges) {
        return new ListRange<>(ranges);
    }

    public static <T extends Comparable<T>> Range<T> empty() {
        return new EmptyRange<>();
    }

    public static class InclusiveRange<T extends Comparable<T>> extends Range<T> {

        private final T start;
        private final T end;

        public InclusiveRange(T start, T end) {
            this.start = start;
            this.end = end;
        }

        public boolean overlaps(InclusiveRange<T> other) {
            return !(this.end.compareTo(other.start) < 0 || other.end.compareTo(this.start) < 0);
        }

        @Override
        public boolean contains(T value) {
            return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
        }

        @Override
        public Range<T> union(Range<T> other) {
            return switch (other) {
                case InclusiveRange<T> o -> new ListRange<>(this, o);
                case ListRange<T> o -> other.union(this);
                case EmptyRange<T> o -> this;
                case null -> this;
                default -> throw new IllegalStateException("Unexpected value: " + other);
            };
        }

        @Override
        public T getLowerBound() {
            return start;
        }

        @Override
        public T getUpperBound() {
            return end;
        }

        @NotNull
        @Override
        public Iterator<InclusiveRange<T>> iterator() {
            return List.of(this).iterator();
        }

        @Override
        public String toString() {
            return "[" + start + ", " + end + "]";
        }
    }

    public static class ListRange<T extends Comparable<T>> extends Range<T> {

        private final List<InclusiveRange<T>> ranges = new ArrayList<>();

        public ListRange(List<InclusiveRange<T>> ranges) {
            ranges.forEach(this::insertRange);
        }

        @SafeVarargs
        public ListRange(InclusiveRange<T>... ranges) {
            for (InclusiveRange<T> range : ranges) {
                insertRange(range);
            }
        }

        @Override
        public boolean contains(T value) {
            return ranges.stream().anyMatch(r -> r.contains(value));
        }

        @Override
        public Range<T> union(Range<T> other) {
            if (other instanceof InclusiveRange<T> o) {
                insertRange(o);
            } else if (other instanceof ListRange<T> o) {
                o.ranges.forEach(this::insertRange);
            }
            return this;
        }

        private void insertRange(InclusiveRange<T> newRange) {
            for (int i = 0; i < ranges.size(); i++) {
                InclusiveRange<T> current = ranges.get(i);

                if (newRange.getUpperBound().compareTo(current.getLowerBound()) < 0) {
                    ranges.add(i, newRange);
                    return;
                } else if (current.overlaps(newRange)) {
                    T newStart = current.start.compareTo(newRange.start) < 0 ? current.start : newRange.start;
                    T newEnd = current.end.compareTo(newRange.end) > 0 ? current.end : newRange.end;
                    ranges.set(i, new InclusiveRange<>(newStart, newEnd));

                    while (i < ranges.size() - 1) {
                        InclusiveRange<T> next = ranges.get(i + 1);
                        InclusiveRange<T> merged = ranges.get(i);

                        if (merged.overlaps(next)) {
                            T mergedEnd = merged.end.compareTo(next.end) > 0 ? merged.end : next.end;
                            ranges.set(i, new InclusiveRange<>(merged.start, mergedEnd));
                            ranges.remove(i + 1);
                        } else {
                            break;
                        }

                    }

                    while (i > 0) {
                        InclusiveRange<T> previous = ranges.get(i - 1);
                        InclusiveRange<T> merged = ranges.get(i);

                        if (merged.overlaps(previous)) {
                            T mergedStart = merged.start.compareTo(previous.start) < 0 ? merged.start : previous.start;
                            ranges.set(i, new InclusiveRange<>(mergedStart, merged.end));
                            ranges.remove(i - 1);
                            i--;
                        } else {
                            break;
                        }
                    }

                    return;
                }
            }
            ranges.add(newRange);
        }

        @Override
        public T getLowerBound() {
            return ranges.getFirst().start;
        }

        @Override
        public T getUpperBound() {
            return ranges.getLast().end;
        }

        @NotNull
        @Override
        public Iterator<Range.InclusiveRange<T>> iterator() {
            return ranges.iterator();
        }

        public void validate() {
            for (int i = 0; i < ranges.size() - 1; i++) {
                InclusiveRange<T> current = ranges.get(i);
                InclusiveRange<T> next = ranges.get(i + 1);

                if (current.getUpperBound().compareTo(next.getLowerBound()) >= 0) {
                    throw new IllegalStateException("Ranges are overlapping or not sorted: " + current + " and " + next);
                }
            }
        }
    }

    public static class EmptyRange<T extends Comparable<T>> extends Range<T> {

        @Override
        public boolean contains(T value) {
            return false;
        }

        @Override
        public Range<T> union(Range<T> other) {
            return other;
        }

        @Override
        public T getLowerBound() {
            return null;
        }

        @Override
        public T getUpperBound() {
            return null;
        }

        @NotNull
        @Override
        public Iterator<Range.InclusiveRange<T>> iterator() {
            return Collections.emptyIterator();
        }
    }
}
