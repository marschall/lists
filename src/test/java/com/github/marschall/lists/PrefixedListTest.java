package com.github.marschall.lists;

import static com.github.marschall.junitlambda.LambdaAssert.assertRaises;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PrefixedListTest {

  private List<String> list;
  private List<String> equalList;

  @Before
  public void setUp() {
    this.list = new PrefixedList<>("0", new ArrayList<>(Arrays.asList("1", "2", "3", "4")));
    this.equalList = Arrays.asList("0", "1", "2", "3", "4");
  }

  @Test
  public void get() {
    assertEquals("0", this.list.get(0));
    assertEquals("1", this.list.get(1));
    assertEquals("2", this.list.get(2));
    assertEquals("3", this.list.get(3));
    assertEquals("4", this.list.get(4));
  }

  @Test
  public void add() {
    assertTrue(this.list.add("5"));
    assertEquals(Arrays.asList("0", "1", "2", "3", "4", "5"), this.list);
  }

  @Test
  public void addInt() {
    this.list.add(2, "5");
    assertEquals(Arrays.asList("0", "1", "5", "2", "3", "4"), this.list);
  }

  @Test
  public void addAll() {
    assertTrue(this.list.addAll(Arrays.asList("5", "6")));
    assertEquals(Arrays.asList("0", "1", "2", "3", "4", "5", "6"), this.list);
  }

  @Test
  public void addAllInst() {
    assertTrue(this.list.addAll(2, Arrays.asList("5", "6")));
    assertEquals(Arrays.asList("0", "1", "5", "6", "2", "3", "4"), this.list);
  }

  @Test
  public void removeObject() {
    assertTrue(this.list.remove("1"));
    assertEquals(Arrays.asList("0", "2", "3", "4"), this.list);
  }

  @Test
  public void removeInt() {
    assertEquals("1", this.list.remove(1));
    assertEquals(Arrays.asList("0", "2", "3", "4"), this.list);
  }

  @Test
  public void set() {
    this.list.set(0, "A");
    assertEquals(Arrays.asList("A", "1", "2", "3", "4"), this.list);
    this.list.set(1, "B");
    assertEquals(Arrays.asList("A", "B", "2", "3", "4"), this.list);
  }

  @Test
  public void size() {
    assertEquals(5, this.list.size());
  }

  @Test
  public void forEach() {
    assertEquals(this.equalList, ListTestUtil.collect(this.list));
  }

  @Test
  public void forEachStream() {
    assertEquals(this.equalList, ListTestUtil.collect(this.list.stream()));
  }

  @Test
  public void serialize() throws ClassNotFoundException, IOException {
    assertEquals(this.equalList, ListTestUtil.copy(this.list));
  }

  @Test
  public void isEmpty() {
    assertFalse(this.list.isEmpty());
  }

  @Test
  public void testToString() {
    assertEquals(this.equalList.toString(), this.list.toString());
  }

  @Test
  public void equals() {
    assertEquals(this.equalList, this.list);
    assertEquals(this.list, this.equalList);
  }

  @Test
  public void testHashCode() {
    assertEquals(this.equalList.hashCode(), this.list.hashCode());
  }

  @Test
  public void subList() {
    assertEquals(this.equalList.subList(1, 4), this.list.subList(1, 4));

    assertEquals(Collections.emptyList(), this.list.subList(0, 0));
    assertEquals(Collections.singletonList("0"), this.list.subList(0, 1));
    assertEquals(Arrays.asList("0", "1"), this.list.subList(0, 2));
    assertEquals(Arrays.asList("1", "2", "3", "4"), this.list.subList(1, 5));
    assertEquals(Arrays.asList("0", "1", "2", "3", "4"), this.list.subList(0, 5));

    this.list.subList(1, 5).set(0, "X");
    assertEquals(Arrays.asList("0", "X", "2", "3", "4"), this.list);

  }

  @Test
  @Ignore
  public void subListRemove() {
    this.list.subList(1, 5).remove(3);
    assertEquals(Arrays.asList("0", "1", "2", "3"), this.list);
  }

  @Test
  public void contains() {
    assertTrue(list.contains("0"));
    assertFalse(list.contains("5"));
    assertFalse(list.contains(0));
    assertFalse(list.contains(5));
  }

  @Test
  public void indexOf() {
    assertEquals(0, this.list.indexOf("0"));
    assertEquals(4, this.list.indexOf("4"));
    assertEquals(-1, this.list.indexOf("6"));
  }

  @Test
  public void lastIndexOf() {
    assertEquals(4, new PrefixedList<>("0", Arrays.asList("0", "1", "1", "1")).lastIndexOf("1"));
    assertEquals(-1, new PrefixedList<>("0", Arrays.asList("0", "1", "1", "1")).lastIndexOf("2"));
    assertEquals(0, new PrefixedList<>("0", Arrays.asList("1", "1", "1", "1")).lastIndexOf("0"));
  }

  @Test
  public void iterator() {
    Iterator<String> iterator = this.list.iterator();

    assertTrue(iterator.hasNext());
    assertEquals("0", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("1", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("2", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("3", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("4", iterator.next());

    assertFalse(iterator.hasNext());
    assertRaises(() -> iterator.next(), NoSuchElementException.class);
  }

  @Test
  public void listIterator() {
    ListIterator<String> iterator = this.list.listIterator();

    assertTrue(iterator.hasNext());
    assertFalse(iterator.hasPrevious());
    assertEquals(-1, iterator.previousIndex());
    assertEquals("0", iterator.next());
    assertTrue(iterator.hasPrevious());
    assertEquals(0, iterator.previousIndex());

    assertTrue(iterator.hasNext());
    assertEquals("1", iterator.next());
    assertTrue(iterator.hasPrevious());
    assertEquals(1, iterator.previousIndex());
    assertEquals(2, iterator.nextIndex());


    assertTrue(iterator.hasNext());
    assertEquals("2", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("3", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("4", iterator.next());

    assertFalse(iterator.hasNext());
    assertTrue(iterator.hasPrevious());
    assertEquals("4", iterator.previous());
  }

  @Test
  public void listIteratorArgument() {
    ListIterator<String> iterator = this.list.listIterator(1);

    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasPrevious());
    assertEquals(0, iterator.previousIndex());
    assertEquals("1", iterator.next());
    assertTrue(iterator.hasPrevious());
    assertEquals(1, iterator.previousIndex());

    assertTrue(iterator.hasNext());
    assertEquals("2", iterator.next());
    assertTrue(iterator.hasPrevious());
    assertEquals(2, iterator.previousIndex());
    assertEquals(3, iterator.nextIndex());


    assertTrue(iterator.hasNext());
    assertEquals("3", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("4", iterator.next());

    assertFalse(iterator.hasNext());
    assertTrue(iterator.hasPrevious());
    assertEquals("4", iterator.previous());
  }

  @Test
  public void toArray() {
    assertArrayEquals(new Object[] {"0", "1", "2", "3", "4"}, this.list.toArray());
  }

  @Test
  public void toArrayArgument() {
    String[] tooShort = new String[] {"foo", "bar"};
    assertArrayEquals(new String[] {"0", "1", "2", "3", "4"}, this.list.toArray(tooShort));
    assertEquals(String[].class, this.list.toArray(tooShort).getClass());
    assertEquals(Object[].class, this.list.toArray(new Object[] {"foo", "bar"}).getClass());

    assertArrayEquals(new String[] {"0", "1", "2", "3", "4", null, "6"}, this.list.toArray(new String[] {"6", "6", "6", "6", "6", "6", "6"}));
    assertArrayEquals(new String[] {"0", "1", "2", "3", "4", null}, this.list.toArray(new String[] {"6", "6", "6", "6", "6", "6"}));

    String[] longEnough = new String[] {"6", "6", "6", "6", "6"};
    assertArrayEquals(new String[] {"0", "1", "2", "3", "4"}, this.list.toArray(longEnough));
    assertSame(longEnough, this.list.toArray(longEnough));
  }

}
