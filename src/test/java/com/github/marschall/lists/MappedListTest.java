package com.github.marschall.lists;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MappedListTest {

  private List<String> list;
  private List<String> equalList;

  @BeforeEach
  public void setUp() {
    List<Integer> delegate = Arrays.asList(0, 1, 2, 3, 4);
    this.list = new MappedList<>(identity(), delegate);
    this.equalList = Arrays.asList("0", "1", "2", "3", "4");
  }

  private static Function<Integer, String> identity() {
    return (Function<Integer, String> & Serializable) i -> i.toString();
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
  public void forEach() {
    assertEquals(this.equalList, ListTestUtil.collect(this.list));
  }

  @Test
  public void forEachStream() {
    assertEquals(this.equalList, ListTestUtil.collect(this.list.stream()));
  }

  @Test
  public void size() {
    assertEquals(5, this.list.size());
  }

  @Test
  public void serialize() throws ClassNotFoundException, IOException {
    assertEquals(this.equalList, ListTestUtil.copy(this.list));
  }

  @Test
  public void isEmpty() {
    assertFalse(this.list.isEmpty());

    List<String> emptyList = new MappedList<>(i -> i.toString(), Collections.emptyList());
    assertTrue(emptyList.isEmpty());
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
    assertEquals(0, list.indexOf("0"));
    assertEquals(4, list.indexOf("4"));
    assertEquals(-1, list.indexOf("5"));
    assertEquals(-1, list.indexOf(0));
    assertEquals(-1, list.indexOf(5));
  }

  @Test
  public void lastIndexOf() {
    List<Integer> delegate = Arrays.asList(4, 4, 4, 4, 4);
    List<String> listOfFours = new MappedList<>(i -> i.toString(), delegate);

    assertEquals(4, listOfFours.lastIndexOf("4"));
    assertEquals(-1, listOfFours.lastIndexOf("0"));
    assertEquals(-1, listOfFours.indexOf(4));
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
    assertThrows(NoSuchElementException.class, () -> iterator.next());
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

  public void example() {
    List<Class<?>> classes = Collections.emptyList();
    List<String> classNames = new MappedList<>(clazz -> clazz.getName(), classes);
  }

}
