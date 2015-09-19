package com.github.marschall.lists;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.junit.Test;

public class RepeatingListTest {

  @Test
  public void testSize() {
    assertThat(new RepeatingList<>("1", 1), hasSize(1));
    assertThat(new RepeatingList<>("1", 2), hasSize(2));
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
  public void testGet() {
    assertEquals("1", new RepeatingList<>("1", 2).get(0));
    assertEquals("1", new RepeatingList<>("1", 2).get(1));
  }


  @Test
  public void testContains() {
    List<String> list = new RepeatingList<>("1", 3);
    assertTrue(list.contains("1"));
    assertFalse(list.contains("2"));
  }

  @Test
  public void testIndexOf() {
    assertEquals(0, new RepeatingList<>("1", 3).indexOf("1"));
    assertEquals(-1, new RepeatingList<>("1", 3).indexOf("2"));
  }

  @Test
  public void testLastIndexOf() {
    assertEquals(2, new RepeatingList<>("1", 3).lastIndexOf("1"));
    assertEquals(-1, new RepeatingList<>("1", 3).lastIndexOf("2"));
  }

  @Test
  public void testIteratorSemantics() {
    assertIteratorSemantics(Arrays.asList("1", "1", "1"), "1", 3);
    assertIteratorSemantics(new RepeatingList<>("1", 3), "1", 3);
  }

  @Test
  public void testListIteratorSemantics() {
    assertListIteratorSemantics(Collections.singletonList("1"), "1");
    assertListIteratorSemantics(Arrays.asList("1"), "1");
    assertListIteratorSemantics(new RepeatingList<>("1", 1), "1");
    assertListIteratorSemantics(Arrays.asList("1", "1"), "1");
    assertListIteratorSemantics(new RepeatingList<>("1", 2), "1");
  }

  @Test
  public void testListIteratorIntNotEmptySemantics() {
    assertListIteratorSemantics(Arrays.asList("1", "1", "1").listIterator(0), "1", 3);
    assertListIteratorSemantics(new RepeatingList<>("1", 3).listIterator(0), "1", 3);
  }

  @Test
  public void testListIteratorIntEmptySemantics() {
    assertListIteratorSemanticsEmpty(Arrays.asList("1", "1", "1"), "1");
    assertListIteratorSemanticsEmpty(new RepeatingList<>("1", 3), "1");
  }

  @Test
  public void testSubList() {
    assertEquals(Arrays.asList("1", "1", "1"), new RepeatingList<>("1", 6).subList(0, 3));
    assertEquals(Arrays.asList("1", "1", "1"), new RepeatingList<>("1", 6).subList(3, 6));
    assertEquals(Collections.singletonList("1"), new RepeatingList<>("1", 6).subList(0, 1));
    assertEquals(Collections.emptyList(), new RepeatingList<>("1", 6).subList(0, 0));
  }

  @Test
  public void testSpliterator() {
    assertEquals(Arrays.asList("1", "1", "1"), new RepeatingList<>("1", 3).stream().collect(Collectors.toList()));
    assertEquals(3L, new RepeatingList<>("1", 3).stream().count());
  }

  @Test
  public void testForEach() {
    assertEquals(Arrays.asList("1", "1", "1"), ListTestUtil.collect(new RepeatingList<>("1", 3).stream()));
  }

  @Test
  public void testSkip() {
    assertEquals(Collections.singletonList("1"), ListTestUtil.collect(new RepeatingList<>("1", 3).stream().skip(2L)));
  }

  @Test
  public void testEquals() {
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
  public void testToArray() {
    assertArrayEquals(Arrays.asList("1", "1", "1").toArray(), new RepeatingList<>("1", 3).toArray());
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
  }

}
