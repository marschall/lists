package com.github.marschall.lists;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A list containing just one element repeated several times.
 *
 * <p>
 * This list does not support modification.
 *
 * @param <E> the element type
 */
public final class RepeatingList<E> extends AbstractList<E> implements Serializable, RandomAccess {

  private final E value;

  private final int repetitons;

  /**
   * Constructor.
   *
   * @param value the value the value
   * @param repetitons the number of repetitions, must be positive
   */
  public RepeatingList(E value, int repetitons) {
    if (repetitons <= 0) {
      throw new IllegalArgumentException("repetitions must be positive but was: " + repetitons);
    }
    this.value = value;
    this.repetitons = repetitons;
  }

  @Override
  public E get(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("negative index: " + index);
    }
    if (index >= this.repetitons) {
      throw new IndexOutOfBoundsException("index: " + index + " too larget");
    }
    return this.value;
  }

  @Override
  public int size() {
    return this.repetitons;
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    if (fromIndex < 0 || toIndex > this.repetitons) {
      throw new IndexOutOfBoundsException("invalid from index: " + fromIndex + " to index: " + toIndex);
    }
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException("invalid from index: " + fromIndex + " to index: " + toIndex);
    }
    if (toIndex == fromIndex) {
      return Collections.emptyList();
    } else if (toIndex - fromIndex == 1) {
      // seems a better choice than com.github.marschall.lists.SingletonList as this is immutable as well
      return Collections.singletonList(this.value);
    } else {
      return new RepeatingList<>(this.value, toIndex - fromIndex);
    }
  }

  @Override
  public Iterator<E> iterator() {
    return new RepeatingIterator(0);
  }

  @Override
  public ListIterator<E> listIterator() {
    return new RepeatingIterator(0);
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("invalid index: " + index + " must be positive");
    }
    if (index < 0 || index > repetitons) {
      throw new IndexOutOfBoundsException("invalid index: " + index + " too large");
    }
    return new RepeatingIterator(index);
  }

  final class RepeatingIterator implements ListIterator<E> {

    private int index;

    RepeatingIterator(int index) {
      this.index = index;
    }

    @Override
    public boolean hasNext() {
      return this.index < repetitons;
    }

    @Override
    public E next() {
      if (this.index == repetitons) {
        throw new NoSuchElementException();
      }
      this.index += 1;
      return value;
    }

    @Override
    public boolean hasPrevious() {
      return this.index > 0;
    }

    @Override
    public E previous() {
      if (this.index == 0) {
        throw new NoSuchElementException();
      }
      this.index -= 1;
      return value;
    }

    @Override
    public int nextIndex() {
      return this.index;
    }

    @Override
    public int previousIndex() {
      return this.index - 1;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void set(E e) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void add(E e) {
      throw new UnsupportedOperationException();
    }



  }

  static final class RepeatingSpliterator<E> implements Spliterator<E> {

    private final E element;
    private int left;

    RepeatingSpliterator(E element, int left) {
      this.element = element;
      this.left = left;
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
      while (this.left > 0) {
        action.accept(this.element);
        this.left -= 1;
      }
    }

    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
      if (this.left > 0) {
        action.accept(this.element);
        this.left -= 1;
        return true;
      }
      return false;
    }

    @Override
    public Spliterator<E> trySplit() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public long estimateSize() {
      return this.left;
    }

    @Override
    public long getExactSizeIfKnown() {
      return this.estimateSize();
    }

    @Override
    public int characteristics() {
      return Spliterator.SUBSIZED;
    }

  }

}
