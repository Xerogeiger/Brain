package com.ajax.brain.linguist;

import com.ajax.brain.utils.UnsortedArrayException;
import com.ajax.brain.utils.sorts.QuickSort;
import com.ajax.brain.utils.sorts.Representor;

import java.util.*;

/**
 * Organizes a list of matchers based on priority
 * Although the {@code MatcherList} implements {@link java.util.List} any method that puts a value in at a certain index is ignored because the list is sorted
 * However methods like {@link #indexOf(Object)} and {@link #lastIndexOf(Object)} do work and so does {@link #remove(int)} just remember not to modify the list between getting an index and removing it
 * The {@link #set(int, Matcher)} method does work just not in a traditional way, it removes the value at the index and then adds the other one into it's sorted spot
 */
public class MatcherList implements List<Matcher> {
    //Make sure all null values are at the end of the list
    private static final QuickSort<Matcher> QUICK_SORT = QuickSort.getInstance(matcher -> matcher == null? Integer.MAX_VALUE: matcher.getPriority());

    //As per IntelliJ's suggestion I will use an empty array when calling toArray(T[])
    private static final Matcher[] EMPTY_MATCHER_ARRAY = new Matcher[0];

    //The initial padding value
    private static final int INITIAL_PADDING = 10;

    /**
     * The organized list of {@code Matcher}s
     */
    private Matcher[] matchers;

    /**
     * The number of non-null {@code Matcher}s in the list
     */
    private int availableMatchers;

    /**
     * Exponentially increasing padding to avoid to much array copying
     */
    private int padding = 10;

    /**
     * The number of modifications to the list
     */
    private int listMods = 0;

    /**
     * Constructs an empty {@code MatcherList}
     */
    public MatcherList() {
        this.matchers = new Matcher[0];
        this.availableMatchers = 0;
    }

    /**
     * Constructs a {@code MatcherList} with the given length
     * @param len the length of the empty matcher list
     */
    public MatcherList(int len) {
        this.matchers = new Matcher[len];
        this.availableMatchers = 0;
    }

    /**
     * Constructs a {@code MatcherList} from the given array with 0 padding
     * Performs a copy so that elements are not re-arranged
     * @param matchers the array to copy the elements from
     */
    public MatcherList(Matcher... matchers) {
        this(0, matchers);
    }

    /**
     * Constructs a {@code MatcherList} from an array and adds padding for more {@code Matcher}s
     * @param padding the padding at the end of the array
     * @param matchers the array to copy the elements from
     */
    public MatcherList(int padding, Matcher... matchers) {
        this.matchers = new Matcher[matchers.length + padding];

        for(int x = 0; x < matchers.length; x++) {
            this.matchers[x] = matchers[x];
        }

        organize();
        countNonNulls();
    }

    /**
     * Constructs a {@code MatcherList} from the {@code Collection}
     * @param matchers the {@code Collection} of {@code Matcher}s to use
     */
    public MatcherList(Collection<Matcher> matchers) {
        this(0,matchers.toArray(EMPTY_MATCHER_ARRAY));
    }

    /**
     * Used by {@link #subList(int, int)} to create a sub {@code MatcherList}
     */
    private MatcherList(MatcherList other, int offset, int len) {
        this.matchers = new Matcher[len];
        this.availableMatchers = len;

        int parentIndex = offset;
        for(int x = 0; x < len; x++) {
            this.matchers[x] = other.matchers[parentIndex++];
        }
    }

    /**
     * Checks if the array is sorted
     *
     * @throws UnsortedArrayException if the array is not sorted
     */
    public void checkIfSorted() {
        for(int x = 1; x < availableMatchers; x++) {
            if(matchers[x-1].getPriority() > matchers[x].getPriority()) {
                throw new UnsortedArrayException("MatcherList not sorted");
            }
        }
    }

    /**
     * Sorts all of the matchers by priority using a {@code QuickSort}
     *
     * @see QuickSort
     */
    private void organize() {
        QUICK_SORT.sort(matchers);
    }

    /**
     * Counts the number of non-null values in the matchers array
     * Only use this if the array is empty or sorted
     * The sort should push all null values to the back so the first null is the end of available matchers
     * Don't use all the time as iteration is slow
     */
    private void countNonNulls() {
        for(int x = 0; x < matchers.length; x++) {
            if(matchers[x] == null) {
                availableMatchers = x;
            }
        }
        availableMatchers = matchers.length;
    }

    /**
     * Shifts all the elements in the array by an amount
     * If a shift is negative, after an element at the end is shifted the new value will be null
     *
     * WARNING if the shift is negative all elements at offset+shift to offset will be overrided
     *
     * @param offset the offset to start shifting at
     * @param shift the amount to shift
     */
    private void shiftArray(int offset, int shift) {
        if(shift == 0)
            return;

        if(shift > 0) {
            if(availableMatchers + shift > this.matchers.length)
                increaseSize(shift);
            for (int x = availableMatchers; x > offset; x--) {
                int swapIndex = x - shift;
                if (swapIndex >= this.matchers.length)
                    break;
                this.matchers[x] = this.matchers[swapIndex];
            }
        } else {
            for(int x = offset; x < availableMatchers; x++) {
                int swapIndex = x+1;
                if(swapIndex >= availableMatchers)
                    break;
                this.matchers[x] = this.matchers[swapIndex];
            }
            availableMatchers+=shift; //Only removing actually subtracts from the available matchers
        }
    }

    /**
     * Adds 1 value to the array in the spot it goes and returns the index it was put at
     * Used by the Iterator and ListIterator because adding creates an uncertainty about the new location because of the QuickSort but if I just insert the value then we are all good
     *
     * @param matcher the matcher to add
     * @return the index the matcher was put at
     */
    private int add1(Matcher matcher) {
        listMods++;
        for(int x = 0; x < availableMatchers; x++) {
            if(matchers[x].getPriority() >= matcher.getPriority()) {
                shiftArray(x, 1); //{1, 2, 4, 5} -> {1, 2, 4, 4, 5}
                matchers[x] = matcher; //{1, 2, 4, 4, 5} -> {1, 2, 3, 4, 5}
                availableMatchers++;
                return x;
            }
        }
        if(availableMatchers == matchers.length)
            increaseSize(1);
        matchers[availableMatchers] = matcher;
        return availableMatchers++;
    }

    /**
     * Increases the size of the internal array
     * Increases padding to fit the desired increase
     *
     * @param inc the amount to increase by
     */
    private void increaseSize(int inc) {
        if(inc > padding)
            while(inc > padding)
                padding *= 2; //Make sure the padding can handle a number this big next time
        matchers = Arrays.copyOf(matchers, matchers.length + padding);
        padding *= 2; //Increase padding more
    }

    //Explicitly state that the JavaDoc will be inherited


    @Override
    public String toString() {
        if (matchers == null)
            return "null";

        int iMax = availableMatchers - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(matchers[i].getPriority());
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return availableMatchers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return availableMatchers == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        if(!(o instanceof Matcher))
            return false;

        for(int x = 0; x < availableMatchers; x++) {
            if(matchers[x].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return matchers;
    }

    /**
     * Returns a copy of the internal {@code Matcher}s array
     * @return a copy of the internal {@code Matcher}s array
     */
    public Matcher[] getMatchers() {
        return Arrays.copyOf(matchers, availableMatchers);
    }

    /**
     * Don't use this method use getMatchers this one will throw an exception if you try to use it
     * @return nothing
     * @throws UnsupportedOperationException this is not a supported method
     *
     * @see #getMatchers()
     */
    @Deprecated
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Use MatcherList.getMatchers instead");
    }

    /**
     * Add a {@code Matcher} to the list of {@code Matcher}s
     * Ignores any null values
     *
     * @param matcher the {@code Matcher} to add
     * @return {@code false} if the value is null and not added otherwise {@code true}
     */
    @Override
    public boolean add(Matcher matcher) {
        if(matcher == null) {
            return false;
        }

        add1(matcher);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        if(o == null)
            return false;

        listMods++;

        for(int x = 0; x < availableMatchers; x++) {
            if(matchers[x].equals(o)) {
                shiftArray(x, -1); //Shift array back one override the removed value
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o))
                return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends Matcher> c) {
        if(c.size()+availableMatchers > matchers.length) {
            increaseSize(c.size());
        }

        int b4 = availableMatchers;

        for(Matcher o: c) {
            if(o != null)
                add1(o);
        }

        if(b4 != availableMatchers) {
            listMods++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection<? extends Matcher> c) {
        return addAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean change = false;

        for(Object o: c) {
            change = remove(o);
        }

        return change;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return c.removeIf(o -> !contains(o)); //Elegant solution provided by IntelliJ
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        Arrays.fill(matchers, null);
        availableMatchers = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Matcher get(int index) {
        return matchers[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Matcher set(int index, Matcher element) {
        Matcher m = matchers[index];

        availableMatchers--;
        remove(index);
        if(element != null) {//Organization not needed because removing an element in an organized list does not disorder it eg. {1, 2, 3, 4} -> {1, 2, 4}
            add1(element);
        }

        return m;
    }

    /**
     * The {@code MatcherList} is organized so indexes don't matter
     *
     * {@inheritDoc}
     */
    @Override
    public void add(int index, Matcher element) {
        if(element == null)
            return;

        add1(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Matcher remove(int index) {
        listMods++;
        Matcher m = matchers[index];
        shiftArray(index, -1);
        return m;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        for(int x = 0; x < availableMatchers; x++) {
            if(matchers[x].equals(o))
                return x;
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        for(int x = availableMatchers-1; x > 0; x--) {
            if(matchers[x].equals(o))
                return x;
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<Matcher> listIterator() {
        return new MatcherListIterator(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<Matcher> listIterator(int index) {
        if(index > availableMatchers)
            throw new IllegalArgumentException("Index should be less than size()");
        return new MatcherListIterator(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Matcher> subList(int fromIndex, int toIndex) {
        if(fromIndex < 0 || fromIndex > availableMatchers)
            throw new IllegalArgumentException("fromIndex must be greater than 0 and less than size()");
        else if(toIndex < 0 || toIndex > availableMatchers)
            throw new IllegalArgumentException("toIndex must be greater than 0 and less than size()");
        else if(toIndex < fromIndex)
            throw new IllegalArgumentException("fromIndex should be less than or equal to toIndex");

        return new MatcherList(this, fromIndex, toIndex-fromIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Matcher> iterator() {
        return new MatcherIterator(0);
    }

    /**
     * Iterator for the {@code MatcherList}
     * Throws an exception if the {@code MatcherList} is edited while the iterator is being used
     */
    private class MatcherIterator implements Iterator<Matcher> {
        static final String MOD_EXCEPTION = "Must call next or previous before %s (If you call add, remove, or set you must call next or previous again)";

        /**
         * The number of mods that the MatcherIterator knows about
         */
        protected int mods = listMods;

        /**
         * The current index of the iterator
         */
        protected int index;

        /**
         * The offset of the iterator
         */
        protected int offset;

        /**
         * The last index from {@link #next()} or {@link MatcherListIterator#previous()}
         */
        protected int removable;

        /**
         * Constructs a {@code MatcherIterator} for the {@code MatcherList} with the offset
         * @param offset the offset of the list
         */
        public MatcherIterator(int offset) {
            this.offset = offset;
            this.index = offset;
        }

        /**
         * Checks if the list has been modified outside of the iterator
         */
        protected void checkMods() {
            if(mods != listMods)
                throw new ConcurrentModificationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            checkMods();
            return index != availableMatchers;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Matcher next() {
            checkMods();
            if(!hasNext())
                throw new NoSuchElementException("No elements left in MatcherList");
            removable = index;
            return matchers[index++];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove() {
            checkMods();
            if(removable == -1)
                throw new IllegalStateException(String.format(MOD_EXCEPTION, "remove"));

            MatcherList.this.remove(removable);

            index = removable;
            removable = -1;
            mods++;
        }
    }

    /**
     * ListIterator for the {@code MatcherList}
     * Throws an exception if the {@code MatcherList} is edited while the iterator is being used
     */
    private class MatcherListIterator extends MatcherIterator implements ListIterator<Matcher> {

        /**
         * Constructs a new {@code MatcherListIterator} for the {@code MatcherList}
         * @param offset the offset of the list
         */
        public MatcherListIterator(int offset) {
            super(offset);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void set(Matcher matcher) {
            checkMods();
            Objects.requireNonNull(matcher);
            if(removable == -1)
                throw new IllegalStateException(String.format(MOD_EXCEPTION, "set"));

            MatcherList.this.remove(removable);
            if (MatcherList.this.add1(matcher) <= index) {
                index++;
            }
            mods++;
            removable = -1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void add(Matcher matcher) {
            checkMods();
            Objects.requireNonNull(matcher);
            if(removable == -1)
                throw new IllegalStateException(String.format(MOD_EXCEPTION, "add"));
            if (MatcherList.this.add1(matcher) <= index) {
                index++;
            }
            mods++;
            removable = -1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasPrevious() {
            checkMods();
            return index != offset;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Matcher previous() {
            checkMods();
            if(index == offset)
                throw new NoSuchElementException("No previous element in iterator");
            removable = --index;
            return matchers[index];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int nextIndex() {
            checkMods();
            return index;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int previousIndex() {
            checkMods();
            if(index == offset)
                return -1;
            return index-1; //If the index is at the beginning of the list than 0-1 = -1
        }
    }
}
