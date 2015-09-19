package com.github.marschall.lists;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * A {@link java.util.List} with only a single element.
 *
 * <p>
 * Unlike {@link java.util.Collections#singletonList(Object)} this one is mutable.
 *
 * @param <E> the element type
 */
public final class SingletonList<E> extends AbstractCollection<E> implements List<E>, Serializable, RandomAccess {

  private E value;

  /**
   * Constructor.
   *
   * @param value the value, can be {@code null}
   */
  public SingletonList(E value) {
    this.value = value;
  }

  @Override
  public E set(int index, E element) {
    E old = this.value;
    if (index != 0) {
      throw new IndexOutOfBoundsException("invalid index: " + index + " only 0 allowed");
    }
    this.value = element;
    return old;
  }

  @Override
  public E get(int index) {
    if (index != 0) {
      throw new IndexOutOfBoundsException("invalid index: " + index + " only 0 allowed");
    }
    return this.value;
  }

  @Override
  public boolean contains(Object o) {
    return this.value.equals(o);
  }

  @Override
  public int indexOf(Object o) {
    if (this.value.equals(o)) {
      return 0;
    } else {
      return -1;
    }
  }

  @Override
  public int lastIndexOf(Object o) {
    return this.indexOf(o);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public Iterator<E> iterator() {
    return new SingletonIterator(true);
  }

  @Override
  public ListIterator<E> listIterator() {
    return new SingletonIterator(true);
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    if (index < 0 || index > 1) {
      throw new IndexOutOfBoundsException("invalid index: " + index + " only 0 and 1 allowed");
    }
    return new SingletonIterator(index == 0);
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    if (fromIndex < 0 || toIndex > 1) {
      throw new IndexOutOfBoundsException("invalid from index: " + fromIndex + " to index: " + toIndex);
    }
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException("invalid from index: " + fromIndex + " to index: " + toIndex);
    }
    if (toIndex == fromIndex) {
      return Collections.emptyList();
    } else {
      return this;
    }
  }

  @Override
  public void add(int index, E element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public E remove(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  final class SingletonIterator implements ListIterator<E> {

    private boolean isFirst;

    SingletonIterator(boolean isFirst) {
      this.isFirst = isFirst;
    }

    @Override
    public boolean hasNext() {
      return this.isFirst;
    }

    @Override
    public E next() {
      if (this.isFirst) {
        this.isFirst = false;
        return value;
      } else {
        throw new NoSuchElementException();
      }
    }

    @Override
    public boolean hasPrevious() {
      return !this.isFirst;
    }

    @Override
    public E previous() {
      if (this.isFirst) {
        throw new NoSuchElementException();
      } else {
        this.isFirst = true;
        return value;
      }
    }

    @Override
    public int nextIndex() {
      if (this.isFirst) {
        return 0;
      } else {
        return 1;
      }
    }

    @Override
    public int previousIndex() {
      if (this.isFirst) {
        return -1;
      } else {
        return 0;
      }
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(E e) {
      if (this.isFirst) {
        throw new IllegalStateException();
      } else {
        value = e;
      }
    }

    @Override
    public void add(E e) {
      throw new UnsupportedOperationException();
    }

  }

}
