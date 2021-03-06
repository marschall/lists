package com.github.marschall.lists;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RepeatingListTest {

  private List<String> list;

  @BeforeEach
  public void setUp() {
    this.list = new RepeatingList<>("1", 3);
  }

  @Test
  public void size() {
    assertThat(new RepeatingList<>("1", 1), hasSize(1));
    assertThat(this.list, hasSize(3));
  }

  @Test
  public void serialize() throws ClassNotFoundException, IOException {
    assertEquals(new RepeatingList<>("1", 3), ListTestUtil.copy(this.list));
  }

  @Test
  public void testNull() {
    List<String> customList = new RepeatingList<>(null, 3);
    List<String> jdkList = Arrays.asList(null, null, null);

    assertEquals(jdkList, customList);
    assertEquals(customList, jdkList);
    assertEquals(jdkList.hashCode(), customList.hashCode());
    assertEquals(jdkList.toString(), customList.toString());

    assertTrue(customList.contains(null));
    assertEquals(0, customList.indexOf(null));
    assertEquals(2, customList.lastIndexOf(null));
  }

  @Test
  public void sort() {
    List<String> list = new RepeatingList<>("1", 3);
    list.sort(String::compareTo);
    assertEquals(Arrays.asList("1", "1", "1"), this.list);
  }

  @Test
  public void get() {
    assertEquals("1", this.list.get(0));
    assertEquals("1", this.list.get(1));

    assertThrows(IndexOutOfBoundsException.class, () -> this.list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> this.list.get(3));
  }


  @Test
  public void contains() {
    List<String> list = new RepeatingList<>("1", 3);
    assertTrue(list.contains("1"));
    assertFalse(list.contains("2"));
  }

  @Test
  public void lndexOf() {
    assertEquals(0, new RepeatingList<>("1", 3).indexOf("1"));
    assertEquals(-1, new RepeatingList<>("1", 3).indexOf("2"));
  }

  @Test
  public void lastIndexOf() {
    assertEquals(2, new RepeatingList<>("1", 3).lastIndexOf("1"));
    assertEquals(-1, new RepeatingList<>("1", 3).lastIndexOf("2"));
  }

  @Test
  public void iteratorSemantics() {
    assertIteratorSemantics(Arrays.asList("1", "1", "1"), "1", 3);
    assertIteratorSemantics(new RepeatingList<>("1", 3), "1", 3);
  }

  @Test
  public void listIteratorSemantics() {
    assertListIteratorSemantics(Collections.singletonList("1"), "1");
    assertListIteratorSemantics(Arrays.asList("1"), "1");
    assertListIteratorSemantics(new RepeatingList<>("1", 1), "1");
    assertListIteratorSemantics(Arrays.asList("1", "1"), "1");
    assertListIteratorSemantics(new RepeatingList<>("1", 2), "1");
  }

  @Test
  public void listIteratorIntNotEmptySemantics() {
    assertListIteratorSemantics(Arrays.asList("1", "1", "1").listIterator(0), "1", 3);
    assertListIteratorSemantics(new RepeatingList<>("1", 3).listIterator(0), "1", 3);
  }

  @Test
  public void listIteratorIntEmptySemantics() {
    assertListIteratorSemanticsEmpty(Arrays.asList("1", "1", "1"), "1");
    assertListIteratorSemanticsEmpty(new RepeatingList<>("1", 3), "1");
  }

  @Test
  public void subList() {
    assertEquals(Arrays.asList("1", "1", "1"), new RepeatingList<>("1", 6).subList(0, 3));
    assertEquals(Arrays.asList("1", "1", "1"), new RepeatingList<>("1", 6).subList(3, 6));
    assertEquals(Collections.singletonList("1"), new RepeatingList<>("1", 6).subList(0, 1));
    assertEquals(Collections.emptyList(), new RepeatingList<>("1", 6).subList(0, 0));



    assertThrows(IndexOutOfBoundsException.class, () -> this.list.subList(-1, 1));
    assertThrows(IndexOutOfBoundsException.class, () -> this.list.subList(0, 8));
    assertThrows(IndexOutOfBoundsException.class, () -> this.list.subList(1, 0));
  }

  @Test
  public void spliterator() {
    assertEquals(Arrays.asList("1", "1", "1"), new RepeatingList<>("1", 3).stream().collect(Collectors.toList()));
    assertEquals(3L, new RepeatingList<>("1", 3).stream().count());
  }

  @Test
  public void parallelForEach() {
    int count = 1_000_000;
    List<String> list = new RepeatingList<>("1", count);
    LongAdder adder = new LongAdder();

    list.parallelStream().forEach(s -> {
      if ("1".equals(s)) {
        adder.add(1L);
      }
    });
    assertEquals(count, adder.intValue());
  }

  @Test
  public void parallelDistinct() {
    List<String> list = new RepeatingList<>("1", 1_000_000);

    List<String> distinct = list.parallelStream().distinct().collect(Collectors.toList());
    assertEquals(Collections.singletonList("1"), distinct);
  }

  @Test
  public void forEach() {
    assertEquals(Arrays.asList("1", "1", "1"), ListTestUtil.collect(new RepeatingList<>("1", 3)));
  }

  @Test
  public void forEachStream() {
    assertEquals(Arrays.asList("1", "1", "1"), ListTestUtil.collect(new RepeatingList<>("1", 3).stream()));
  }

  @Test
  public void skip() {
    assertEquals(Collections.singletonList("1"), ListTestUtil.collect(new RepeatingList<>("1", 3).stream().skip(2L)));
  }

  @Test
  public void equals() {
    assertEquals(Collections.singletonList("1"), new RepeatingList<>("1", 1));
    assertEquals(new RepeatingList<>("1", 1), Collections.singletonList("1"));

    assertEquals(Arrays.asList("1", "1"), new RepeatingList<>("1", 2));
    assertEquals(new RepeatingList<>("1", 2), Arrays.asList("1", "1"));

    assertEquals(Arrays.asList("1", "1", "1"), new RepeatingList<>("1", 3));
    assertEquals(new RepeatingList<>("1", 3), Arrays.asList("1", "1", "1"));
  }

  @Test
  public void testHashCode() {
    assertEquals(Arrays.asList("1", "1", "1").hashCode(), new RepeatingList<>("1", 3).hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(Arrays.asList("1", "1", "1").toString(), new RepeatingList<>("1", 3).toString());
  }

  @Test
  public void toArray() {
    assertArrayEquals(Arrays.asList("1", "1", "1").toArray(), new RepeatingList<>("1", 3).toArray());
  }

  @Test
  public void toArrayArgument() {
    List<String> list = new RepeatingList<>("1", 5);
    String[] tooShort = new String[] {"foo", "bar"};
    assertArrayEquals(new String[] {"1", "1", "1", "1", "1"}, list.toArray(tooShort));
    assertEquals(String[].class, list.toArray(tooShort).getClass());
    assertEquals(Object[].class, list.toArray(new Object[] {"foo", "bar"}).getClass());

    assertArrayEquals(new String[] {"1", "1", "1", "1", "1", null, "6"}, list.toArray(new String[] {"6", "6", "6", "6", "6", "6", "6"}));
    assertArrayEquals(new String[] {"1", "1", "1", "1", "1", null}, list.toArray(new String[] {"6", "6", "6", "6", "6", "6"}));

    String[] longEnough = new String[] {"6", "6", "6", "6", "6"};
    assertArrayEquals(new String[] {"1", "1", "1", "1", "1"}, list.toArray(longEnough));
    assertSame(longEnough, list.toArray(longEnough));
  }

  private static void assertListIteratorSemanticsEmpty(List<?> list, Object next) {
    int size = list.size();
    ListIterator<?> iterator = list.listIterator(size);
    assertFalse(iterator.hasNext());
    assertTrue(iterator.hasPrevious());

    assertEquals(size - 1, iterator.previousIndex());
    assertEquals(next, iterator.previous());

    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasPrevious());
    assertEquals(size - 1, iterator.nextIndex());
    assertEquals(size - 2, iterator.previousIndex());
  }

  private static void assertListIteratorSemantics(List<?> list, Object next) {
    assertListIteratorSemantics(list.listIterator(), next, list.size());
  }

  private static void assertListIteratorSemantics(ListIterator<?> iterator, Object next, int count) {

    for (int i = 0; i < count; i++) {
      assertTrue(iterator.hasNext());
      assertEquals(i > 0, iterator.hasPrevious());
      assertEquals(i, iterator.nextIndex());
      assertEquals(i - 1, iterator.previousIndex());

      // next
      assertEquals(next, iterator.next());

      assertEquals(i + 1, iterator.nextIndex());
      assertEquals(i, iterator.previousIndex());

    }

    assertFalse(iterator.hasNext());
    assertTrue(iterator.hasPrevious());

  }

  private static void assertIteratorSemantics(List<?> list, Object next, int count) {
    Iterator<?> iterator = list.iterator();

    for (int i = 0; i < count; i++) {
      assertTrue(iterator.hasNext());

      assertEquals(next, iterator.next());
    }

    assertFalse(iterator.hasNext());

    iterator = list.iterator();
    Accumulator acc = new Accumulator();
    iterator.forEachRemaining(each -> {
      assertEquals(next, each);
      acc.increment();
    });
    assertEquals(count, acc.sum);

    iterator = list.iterator();
    iterator.next();
    Accumulator acc2 = new Accumulator();
    iterator.forEachRemaining(each -> {
      assertEquals(next, each);
      acc2.increment();
    });
    assertEquals(count - 1, acc2.sum);

  }

  static final class Accumulator {

      int sum;

      void increment() {
        this.sum += 1;

      }

  }

}
