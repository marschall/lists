package com.github.marschall.lists;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static com.github.marschall.junitlambda.LambdaAssert.assertRaises;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class SingletonListTest {

  private List<String> list;
  private List<String> equalList;

  @Before
  public void setUp() {
    this.list = new SingletonList<>("1");
    this.equalList = Collections.singletonList("1");
  }

  @Test
  public void size() {
    assertThat(this.list, hasSize(1));
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
    assertEquals("1", this.list.get(0));
  }

  @Test
  public void equals() {
    assertEquals(this.equalList, this.list);
    assertEquals(this.list, this.equalList);
    assertEquals(Arrays.asList("1"), this.list);
    assertEquals(this.list, Arrays.asList("1"));
  }

  @Test
  public void contains() {
    assertTrue(this.list.contains("1"));
    assertFalse(this.list.contains("2"));
  }

  @Test
  public void indexOf() {
    assertEquals(0, this.list.indexOf("1"));
    assertEquals(-1, this.list.indexOf("2"));
  }

  @Test
  public void lastIndexOf() {
    assertEquals(0, this.list.lastIndexOf("1"));
    assertEquals(-1, this.list.lastIndexOf("2"));
  }

  @Test
  public void testHashCode() {
    assertEquals(this.equalList.hashCode(), this.list.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals(this.equalList.toString(), this.list.toString());

    List<Object> reference = Arrays.asList(new Object());
    reference.set(0, reference);
    List<Object> actual = new SingletonList<Object>(new Object());
    actual.set(0, actual);
    assertEquals(reference.toString(), actual.toString());
  }

  @Test
  public void toArray() {
    assertArrayEquals(this.equalList.toArray(), this.list.toArray());
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
    assertEquals(this.equalList, this.list);
    assertEquals("1", this.list.set(0, "2"));
    assertEquals(Collections.singletonList("2"), this.list);
  }

  @Test
  public void iteratorSet() {
    assertEquals(this.equalList, this.list);

    ListIterator<String> iterator = this.list.listIterator();
    iterator.next();
    iterator.set("2");

    assertEquals(Collections.singletonList("2"), list);
  }

  @Test
  public void iteratorSemantics() {
    assertIteratorSemantics(this.equalList, "1");
    assertIteratorSemantics(Arrays.asList("1"), "1");
    assertIteratorSemantics(this.list, "1");
  }

  @Test
  public void listIteratorSemantics() {
    assertListIteratorSemantics(this.equalList, "1");
    assertListIteratorSemantics(Arrays.asList("1"), "1");
    assertListIteratorSemantics(this.list, "1");
  }

  @Test
  public void listIteratorIntNotEmptySemantics() {
    assertListIteratorSemantics(this.equalList.listIterator(0), "1");
    assertListIteratorSemantics(Arrays.asList("1").listIterator(0), "1");
    assertListIteratorSemantics(this.list.listIterator(0), "1");
  }

  @Test
  public void listIteratorIntEmptySemantics() {
    assertListIteratorSemanticsEmpty(this.equalList, "1");
    assertListIteratorSemanticsEmpty(Arrays.asList("1"), "1");
    assertListIteratorSemanticsEmpty(this.list, "1");
  }

  @Test
  public void subList() {
    assertEquals(this.equalList.subList(0, 0), this.list.subList(0, 0));
    assertEquals(this.equalList.subList(0, 1), this.list.subList(0, 1));
    assertEquals(Arrays.asList("1").subList(0, 0), this.list.subList(0, 0));
    assertEquals(Arrays.asList("1").subList(0, 1), this.list.subList(0, 1));

    assertRaises(() -> this.list.subList(-1, 1), IndexOutOfBoundsException.class);
    assertRaises(() -> this.list.subList(0, 2), IndexOutOfBoundsException.class);
    assertRaises(() -> this.list.subList(1, 0), IndexOutOfBoundsException.class);
  }


  @Test
  public void spliterator() {
    assertEquals(this.equalList, this.list.stream().collect(Collectors.toList()));
    assertEquals(1L, this.list.stream().count());
  }

  @Test
  public void forEach() {
    assertEquals(this.equalList, ListTestUtil.collect(this.list.stream()));
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
