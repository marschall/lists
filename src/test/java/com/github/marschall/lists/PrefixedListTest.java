package com.github.marschall.lists;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PrefixedListTest {


  private List<String> list;
  private List<String> equalList;

  @Before
  public void setUp() {
    this.list = new PrefixedList<>("0", Arrays.asList("1", "2", "3", "4"));
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

}
