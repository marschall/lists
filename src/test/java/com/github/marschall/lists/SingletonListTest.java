package com.github.marschall.lists;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.junit.Test;

public class SingletonListTest {

  @Test
  public void size() {
    assertThat(new SingletonList<>("1"), hasSize(1));
  }

  @Test
  public void serialize() throws ClassNotFoundException, IOException {
    assertEquals(new SingletonList<>("1"), ListTestUtil.copy(new SingletonList<>("1")));
  }

  @Test
  public void testNull() {
    List<String> customList = new SingletonList<>(null);
    List<String> jdkList = Collections.singletonList(null);

    assertEquals(jdkList, customList);
    assertEquals(customList, jdkList);
    assertEquals(jdkList.hashCode(), customList.hashCode());
    assertEquals(jdkList.toString(), customList.toString());

    assertTrue(customList.contains(null));
    assertEquals(0, customList.indexOf(null));
    assertEquals(0, customList.lastIndexOf(null));
  }

  @Test
  public void get() {
    assertEquals("1", new SingletonList<>("1").get(0));
  }

  @Test
  public void equals() {
    assertEquals(Collections.singletonList("1"), new SingletonList<>("1"));
    assertEquals(Arrays.asList("1"), new SingletonList<>("1"));
  }

  @Test
  public void contains() {
    List<String> list = new SingletonList<>("1");
    assertTrue(list.contains("1"));
    assertFalse(list.contains("2"));
  }

  @Test
  public void indexOf() {
    List<String> list = new SingletonList<>("1");
    assertEquals(0, list.indexOf("1"));
    assertEquals(-1, list.indexOf("2"));
  }

  @Test
  public void lastIndexOf() {
    List<String> list = new SingletonList<>("1");
    assertEquals(0, list.lastIndexOf("1"));
    assertEquals(-1, list.lastIndexOf("2"));
  }

  @Test
  public void testHashCode() {
    assertEquals(Collections.singletonList("1").hashCode(), new SingletonList<>("1").hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(Collections.singletonList("1").toString(), new SingletonList<>("1").toString());
  }

  @Test
  public void toArray() {
    assertArrayEquals(Collections.singletonList("1").toArray(), new SingletonList<>("1").toArray());
  }

  @Test
  public void toArrayArgument() {
    List<String> list = new SingletonList<>("1");
    String[] tooShort = new String[0];
    assertArrayEquals(new String[] {"1"}, list.toArray(tooShort));
    assertEquals(String[].class, list.toArray(tooShort).getClass());
    assertEquals(Object[].class, list.toArray(new Object[] {"foo", "bar"}).getClass());

    assertArrayEquals(new String[] {"1", null, "6"}, list.toArray(new String[] {"6", "6", "6"}));
    assertArrayEquals(new String[] {"1", null}, list.toArray(new String[] {"6", "6"}));

    String[] longEnough = new String[] {"6"};
    assertArrayEquals(new String[] {"1"}, list.toArray(longEnough));
    assertSame(longEnough, list.toArray(longEnough));
  }

  @Test
  public void set() {
    List<String> list = new SingletonList<>("1");
    assertEquals(Collections.singletonList("1"), list);
    assertEquals("1", list.set(0, "2"));
    assertEquals(Collections.singletonList("2"), list);
  }

  @Test
  public void iteratorSet() {
    List<String> list = new SingletonList<>("1");
    assertEquals(Collections.singletonList("1"), list);

    ListIterator<String> iterator = list.listIterator();
    iterator.next();
    iterator.set("2");

    assertEquals(Collections.singletonList("2"), list);
  }

  @Test
  public void iteratorSemantics() {
    assertIteratorSemantics(Collections.singletonList("1"), "1");
    assertIteratorSemantics(Arrays.asList("1"), "1");
    assertIteratorSemantics(new SingletonList<>("1"), "1");
  }

  @Test
  public void listIteratorSemantics() {
    assertListIteratorSemantics(Collections.singletonList("1"), "1");
    assertListIteratorSemantics(Arrays.asList("1"), "1");
    assertListIteratorSemantics(new SingletonList<>("1"), "1");
  }

  @Test
  public void listIteratorIntNotEmptySemantics() {
    assertListIteratorSemantics(Collections.singletonList("1").listIterator(0), "1");
    assertListIteratorSemantics(Arrays.asList("1").listIterator(0), "1");
    assertListIteratorSemantics(new SingletonList<>("1").listIterator(0), "1");
  }

  @Test
  public void listIteratorIntEmptySemantics() {
    assertListIteratorSemanticsEmpty(Collections.singletonList("1"), "1");
    assertListIteratorSemanticsEmpty(Arrays.asList("1"), "1");
    assertListIteratorSemanticsEmpty(new SingletonList<>("1"), "1");
  }

  @Test
  public void subList() {
    assertEquals(Collections.singletonList("1").subList(0, 0), new SingletonList<>("1").subList(0, 0));
    assertEquals(Collections.singletonList("1").subList(0, 1), new SingletonList<>("1").subList(0, 1));
    assertEquals(Arrays.asList("1").subList(0, 0), new SingletonList<>("1").subList(0, 0));
    assertEquals(Arrays.asList("1").subList(0, 1), new SingletonList<>("1").subList(0, 1));
  }


  @Test
  public void spliterator() {
    assertEquals(Collections.singletonList("1"), new SingletonList<>("1").stream().collect(Collectors.toList()));
    assertEquals(1L, new SingletonList<>("1").stream().count());
  }

  @Test
  public void forEach() {
    assertEquals(Collections.singletonList("1"), ListTestUtil.collect(new SingletonList<>("1").stream()));
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
