package com.github.marschall.lists;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

/**
 * Test for {@link SingletonList}.
 */
public class SingletonListTest {

  /**
   * Tests the {@link SingletonList#size()} method.
   */
  @Test
  public void testSize() {
    assertThat(new SingletonList<>("1"), hasSize(1));
  }

  /**
   * Tests the {@link SingletonList#get(int)} method.
   */
  @Test
  public void testGet() {
    assertEquals("1", new SingletonList<>("1").get(0));
  }

  /**
   * Tests the {@link SingletonList#equals(Object)} method.
   */
  @Test
  public void testEquals() {
    assertEquals(Collections.singletonList("1"), new SingletonList<>("1"));
    assertEquals(Arrays.asList("1"), new SingletonList<>("1"));
  }

  /**
   * Tests the {@link SingletonList#set(int, Object)} method.
   */
  @Test
  public void testSet() {
    List<String> list = new SingletonList<>("1");
    assertEquals(Collections.singletonList("1"), list);
    assertEquals("1", list.set(0, "2"));
    assertEquals(Collections.singletonList("2"), list);
  }

  /**
   * Tests the {@link SingletonList#set(int, Object)} method.
   */
  @Test
  public void testSetIterator() {
    List<String> list = new SingletonList<>("1");
    assertEquals(Collections.singletonList("1"), list);

    ListIterator<String> iterator = list.listIterator();
    iterator.next();
    iterator.set("2");

    assertEquals(Collections.singletonList("2"), list);
  }

  /**
   * Tests {@link SingletonList#iterator()}.
   */
  @Test
  public void testIteratorSemantics() {
    assertIteratorSemantics(Collections.singletonList("1"), "1");
    assertIteratorSemantics(Arrays.asList("1"), "1");
    assertIteratorSemantics(new SingletonList<>("1"), "1");
  }

  /**
   * Tests {@link SingletonList#listIterator()}.
   */
  @Test
  public void testListIteratorSemantics() {
    assertListIteratorSemantics(Collections.singletonList("1"), "1");
    assertListIteratorSemantics(Arrays.asList("1"), "1");
    assertListIteratorSemantics(new SingletonList<>("1"), "1");
  }

  /**
   * Tests {@link SingletonList#listIterator(int)}.
   */
  @Test
  public void testListIteratorIntNotEmptySemantics() {
    assertListIteratorSemantics(Collections.singletonList("1").listIterator(0), "1");
    assertListIteratorSemantics(Arrays.asList("1").listIterator(0), "1");
    assertListIteratorSemantics(new SingletonList<>("1").listIterator(0), "1");
  }

  /**
   * Tests {@link SingletonList#listIterator(int)}.
   */
  @Test
  public void testListIteratorIntEmptySemantics() {
    assertListIteratorSemanticsEmpty(Collections.singletonList("1"), "1");
    assertListIteratorSemanticsEmpty(Arrays.asList("1"), "1");
    assertListIteratorSemanticsEmpty(new SingletonList<>("1"), "1");
  }

  /**
   * Tests {@link SingletonList#subList(int, int)}.
   */
  @Test
  public void testSubList() {
    assertEquals(Collections.singletonList("1").subList(0, 0), new SingletonList<>("1").subList(0, 0));
    assertEquals(Collections.singletonList("1").subList(0, 1), new SingletonList<>("1").subList(0, 1));
    assertEquals(Arrays.asList("1").subList(0, 0), new SingletonList<>("1").subList(0, 0));
    assertEquals(Arrays.asList("1").subList(0, 1), new SingletonList<>("1").subList(0, 1));
  }

  private static void assertListIteratorSemanticsEmpty(List<?> list, Object next) {
    ListIterator<?> iterator = list.listIterator(1);
    assertFalse(iterator.hasNext());
    assertTrue(iterator.hasPrevious());

    assertEquals(0, iterator.previousIndex());
    assertEquals(next, iterator.previous());

    assertTrue(iterator.hasNext());
    assertFalse(iterator.hasPrevious());
    assertEquals(0, iterator.nextIndex());
    assertEquals(-1, iterator.previousIndex());
  }

  private static void assertListIteratorSemantics(List<?> list, Object next) {
    assertListIteratorSemantics(list.listIterator(), next);
  }

  private static void assertListIteratorSemantics(ListIterator<?> iterator, Object next) {
    assertTrue(iterator.hasNext());
    assertFalse(iterator.hasPrevious());
    assertEquals(0, iterator.nextIndex());
    assertEquals(-1, iterator.previousIndex());

    assertEquals(next, iterator.next());

    assertFalse(iterator.hasNext());
    assertTrue(iterator.hasPrevious());
    assertEquals(1, iterator.nextIndex());

    assertEquals(0, iterator.previousIndex());
    assertEquals(next, iterator.previous());

    assertTrue(iterator.hasNext());
    assertFalse(iterator.hasPrevious());
    assertEquals(0, iterator.nextIndex());
    assertEquals(-1, iterator.previousIndex());
  }

  private static void assertIteratorSemantics(List<?> list, Object next) {
    Iterator<?> iterator = list.iterator();
    assertTrue(iterator.hasNext());

    assertEquals(next, iterator.next());

    assertFalse(iterator.hasNext());
  }

}
