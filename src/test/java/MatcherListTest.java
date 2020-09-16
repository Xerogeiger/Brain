import com.ajax.brain.linguist.Matcher;
import com.ajax.brain.linguist.MatcherList;
import com.ajax.brain.linguist.TokenType;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class MatcherListTest {
    final Matcher[] testMatchers = new Matcher[100];
    {
        for(int x = 0; x < testMatchers.length; x++) {
            testMatchers[x] = Matcher.getSimpleMatcher("TEST", ThreadLocalRandom.current().nextInt(150), TokenType.WORD, ThreadLocalRandom.current().nextBoolean());
        }
        Arrays.sort(testMatchers, Comparator.comparingInt(Matcher::getPriority));
    }

    @Test
    public void shiftArray() {
        int[] arr = {1, 2, 4, 5, 0};

        for(int x = arr.length-1; x > 2; x--) {
            int swapIndex = x-1;
            if(swapIndex >= arr.length)
                break;
            arr[x] = arr[swapIndex];
        }

        arr[2] = 3;

        for(int x = 2; x < arr.length; x++) {
            int swapIndex = x+1;
            if(swapIndex >= arr.length)
                break;
            arr[x] = arr[swapIndex];
        }

        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void size() {
        MatcherList matchers = new MatcherList(testMatchers);

        matchers.checkIfSorted();
        assertEquals(matchers.size(), testMatchers.length);
        System.out.format("Size of MatcherList: %d\n", matchers.size());

        matchers.add(testMatchers[0]);
        matchers.checkIfSorted();
        assertEquals(matchers.size(), testMatchers.length+1);
        System.out.format("Size of MatcherList: %d\n", matchers.size());

        matchers.addAll(Arrays.asList(testMatchers));
        matchers.checkIfSorted();
        assertEquals(matchers.size(), testMatchers.length*2+1);
        System.out.format("Size of MatcherList: %d\n", matchers.size());

        matchers.remove(0);
        matchers.checkIfSorted();
        assertEquals(matchers.size(), testMatchers.length*2);
        System.out.format("Size of MatcherList: %d\n", matchers.size());

        matchers.removeAll(Arrays.asList(testMatchers));
        matchers.checkIfSorted();
        assertEquals(matchers.size(), testMatchers.length);
        System.out.format("Size of MatcherList: %d\n", matchers.size());
    }

    @Test
    public void isEmpty() {
        MatcherList matchers = new MatcherList();

        assertTrue(matchers.isEmpty());

        matchers.add(testMatchers[0]);

        assertFalse(matchers.isEmpty());

        matchers = new MatcherList(testMatchers);

        assertFalse(matchers.isEmpty());

        matchers.removeAll(Arrays.asList(testMatchers));

        assertTrue(matchers.isEmpty());
    }

    @Test
    public void contains() {
        MatcherList matchers = new MatcherList();

        assertFalse(matchers.contains(null));

        matchers.addAll(Arrays.asList(testMatchers));

        assertTrue(matchers.containsAll(Arrays.asList(testMatchers)));

        assertTrue(matchers.contains(testMatchers[4]));

        matchers.remove(testMatchers[4]);

        assertFalse(matchers.contains(testMatchers[4]));
    }

    @Test
    public void toArray() {
        MatcherList matchers = new MatcherList(testMatchers);

        boolean pass = true;

        for(Object o: matchers.toArray()) {
            if(Arrays.binarySearch(testMatchers, (Matcher)(o), Comparator.comparingInt(Matcher::getPriority)) < 0) {
                pass = false;
            }
        }

        assertTrue(pass);

        for(Matcher o: matchers.getMatchers()) {
            if(Arrays.binarySearch(testMatchers, o, Comparator.comparingInt(Matcher::getPriority)) < 0) {
                pass = false;
            }
        }

        assertTrue(pass);
    }

    @Test
    public void add() {
        MatcherList matcherList = new MatcherList();

        matcherList.add(testMatchers[3]);

        assertTrue(matcherList.contains(testMatchers[3]));

        matcherList.remove(0);

        assertFalse(matcherList.contains(testMatchers[3]));
    }

    @Test
    public void remove() {
        MatcherList matcherList = new MatcherList();

        matcherList.add(testMatchers[3]);

        assertTrue(matcherList.contains(testMatchers[3]));

        matcherList.remove(0);

        assertFalse(matcherList.contains(testMatchers[3]));

        try {
            matcherList.remove(-1);
        } catch (Exception e) {
            assertTrue(e instanceof IndexOutOfBoundsException);
        }
    }

    @Test
    public void containsAll() {
        MatcherList matchers = new MatcherList(testMatchers);

        for(int x = 0; x < 10; x++) {
            int size = ThreadLocalRandom.current().nextInt(10);
            ArrayList<Matcher> matcherList = new ArrayList<>(size);

            for(int i = 0; i < size; i++) {
                matcherList.add(testMatchers[ThreadLocalRandom.current().nextInt(testMatchers.length)]);
            }

            assertTrue(matchers.containsAll(matcherList));
        }
    }

    @Test
    public void addAll() {
        MatcherList matchers = new MatcherList();

        for(int x = 0; x < 10; x++) {
            int size = ThreadLocalRandom.current().nextInt(10);
            ArrayList<Matcher> matcherList = new ArrayList<>(size);

            for(int i = 0; i < size; i++) {
                matcherList.add(testMatchers[ThreadLocalRandom.current().nextInt(testMatchers.length)]);
            }

            matchers.addAll(matcherList);
            assertTrue(matchers.containsAll(matcherList));
            matchers.removeAll(matcherList);
        }
    }

    @Test
    public void removeAll() {
        MatcherList matchers = new MatcherList(testMatchers);

        for(int x = 0; x < 10; x++) {
            int size = ThreadLocalRandom.current().nextInt(10);
            ArrayList<Matcher> matcherList = new ArrayList<>(size);

            for(int i = 0; i < size; i++) {
                matcherList.add(testMatchers[ThreadLocalRandom.current().nextInt(testMatchers.length)]);
            }

            matchers.addAll(matcherList);
            matchers.removeAll(matcherList);

            assertEquals(testMatchers.length, matchers.size());
        }
    }

    @Test
    public void retainAll() {
        MatcherList matchers = new MatcherList(testMatchers[2], testMatchers[5], testMatchers[9]);

        ArrayList<Matcher> m = new ArrayList<>();

        m.add(testMatchers[2]);
        m.add(testMatchers[9]);
        m.add(testMatchers[0]);

        matchers.retainAll(m);

        assertEquals(2, m.size());
    }

    @Test
    public void clear() {
        MatcherList matchers = new MatcherList();

        matchers.clear();
        assertTrue(matchers.isEmpty());

        matchers.addAll(Arrays.asList(testMatchers));

        matchers.clear();
        assertTrue(matchers.isEmpty());

        matchers = new MatcherList(testMatchers);
        matchers.clear();
        assertTrue(matchers.isEmpty());

        matchers.clear();
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void get() {
        MatcherList matchers = new MatcherList(testMatchers);

        assertTrue(matchers.get(2).getPriority() == testMatchers[2].getPriority());
    }

    @Test
    public void set() {
        MatcherList matchers = new MatcherList(testMatchers);

        matchers.set(0, matchers.get(1));

        assertTrue(matchers.get(0) == matchers.get(1)); //Closest to an actual test I can do since the array is sorted
    }

    @Test
    public void indexOf() {
        MatcherList matchers = new MatcherList(testMatchers);

        assertTrue(matchers.indexOf(matchers.get(10)) == 10);
    }

    @Test
    public void lastIndexOf() {
        MatcherList matchers = new MatcherList(testMatchers);

        assertTrue(matchers.indexOf(matchers.get(99)) == 99);
    }

    @Test
    public void listIterator() {
        MatcherList matchers = new MatcherList();

        ListIterator<Matcher> matcherIterator = matchers.listIterator();

        try {
            matcherIterator.next();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchElementException);
        }

        matchers.addAll(Arrays.asList(testMatchers));

        matcherIterator = matchers.listIterator();

        int num = 0;

        while (matcherIterator.hasNext()){
            matcherIterator.next();
            matcherIterator.remove();
            num++;
        }

        try {
            matcherIterator.remove();
            fail();
        } catch (Exception exception) {
            assertTrue(exception instanceof IllegalStateException);
        }

        assertTrue(num == 100);
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void subList() {
        MatcherList matchers = new MatcherList(testMatchers);

        MatcherList m = (MatcherList) matchers.subList(20, 30);

        assertTrue(m.size() == 10);
        assertTrue(matchers.containsAll(m));

        m.retainAll(matchers);

        assertTrue(matchers.size() == m.size());
    }

    @Test
    public void iterator() {
        MatcherList matchers = new MatcherList();

        Iterator<Matcher> matcherIterator = matchers.iterator();

        try {
            matcherIterator.next();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NoSuchElementException);
        }

        matchers.addAll(Arrays.asList(testMatchers));

        matcherIterator = matchers.listIterator();

        int num = 0;

        while (matcherIterator.hasNext()){
            matcherIterator.next();
            matcherIterator.remove();
            num++;
        }

        try {
            matcherIterator.remove();
            fail();
        } catch (Exception exception) {
            assertTrue(exception instanceof IllegalStateException);
        }

        assertTrue(num == 100);
        assertTrue(matchers.isEmpty());
    }
}