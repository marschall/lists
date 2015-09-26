package com.github.marschall.lists;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A list containing just one element repeated several times.
 *
 * <p>This list does not support modification.</p>
 *
 * @param <E> the element type
 */
@Immutable
public final class RepeatingList<E> extends AbstractCollection<E> implements List<E>, Serializable, RandomAccess {
  // extend AbstractCollection instead of AbstractList to avoid the unused modcount instance variable

  @CheckForNull
  private final E element;

  private final int repetitons;

  /**
   * Constructor.
   *
   * @param value the value the value, can be {@code null}
   * @param repetitons the number of repetitions, must be positive
   */
  public RepeatingList(@Nullable E value, int repetitons) {
    if (repetitons <= 0) {
      throw new IllegalArgumentException("repetitions must be positive but was: " + repetitons);
    }
    this.element = value;
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
    return this.element;
  }

  @Override
  public int size() {
    return this.repetitons;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    if (fromIndex < 0 || toIndex > this.repetitons) {
      throw new IndexOutOfBoundsException("invalid from index: " + fromIndex + " to index: " + toIndex);
    }
    if (fromIndex > toIndex) {
      throw new IndexOutOfBoundsException("invalid from index: " + fromIndex + " to index: " + toIndex);
    }
    if (toIndex == fromIndex) {
      return Collections.emptyList();
    } else if (toIndex - fromIndex == 1) {
      // no longer connected to this list but this is ok because
      // this list is immutable and so is Collections.singletonList
      // therefore no difference can be observed
      return Collections.singletonList(this.element);
    } else {
      return new RepeatingList<>(this.element, toIndex - fromIndex);
    }
  }

  @Override
  public boolean contains(Object o) {
    return Objects.equals(this.element, o);
  }

  @Override
  public int indexOf(Object o) {
    if (Objects.equals(this.element, o)) {
      return 0;
    } else {
      return -1;
    }
  }

  @Override
  public int lastIndexOf(Object o) {
    if (Objects.equals(this.element, o)) {
      return this.repetitons - 1;
    } else {
      return -1;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof List)) {
      return false;
    }
    List<?> other = (List<?>) obj;
    if (other.size() != this.repetitons) {
      return false;
    }
    for (Object each : other) {
      if (!Objects.equals(each, this.element)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    int valueHash = Objects.hashCode(this.element);
    // TODO should be optimized further
    for (int i = 0; i < this.repetitons; ++i) {
      hashCode = 31 * hashCode + valueHash;
    }
    return hashCode;
  }

  @Override
  public String toString() {
    // this.value will never be the same as this
    String stringValue = String.valueOf(this.element);
    int finalSize = 2 + this.repetitons * stringValue.length() + (this.repetitons - 1) * 2;
    StringBuilder buffer = new StringBuilder(finalSize);
    buffer.append('[');
    for (int i = 0; i < repetitons; ++i) {
      if (i > 0) {
        buffer.append(',').append(' ');
      }
      buffer.append(stringValue);
    }
    buffer.append(']');
    return buffer.toString();
  }

  @Override
  public Object[] toArray() {
    Object[] result = new Object[this.repetitons];
    Arrays.fill(result, this.element);
    return result;
  }

  @Override
  public <T> T[] toArray(T[] a) {
    int length = a.length;
    if (length < this.repetitons) {
      @SuppressWarnings("unchecked") // because arrays don't play well with generics
      T[] result = (T[]) Array.newInstance(a.getClass().getComponentType(), this.repetitons);
      Arrays.fill(result, 0, this.repetitons, this.element);
      return result;
    } else {
      Arrays.fill(a, 0, this.repetitons, this.element);
      if (length > this.repetitons) {
        a[this.repetitons] = null;
      }
      return a;
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

  @Override
  public Spliterator<E> spliterator() {
    return new RepeatingSpliterator<>(this.element, this.repetitons);
  }

  @Override
  public E remove(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public E set(int index, E element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(int index, E element) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    throw new UnsupportedOperationException();
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
      return element;
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
      return element;
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
      if (this.left <= 1) {
        return null;
      }
      int half = this.left / 2;
      this.left -= half;
      return new RepeatingSpliterator<>(this.element, half);
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
