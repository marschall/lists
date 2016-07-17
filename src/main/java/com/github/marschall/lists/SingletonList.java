package com.github.marschall.lists;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A list with only a single element.
 *
 * <p>Unlike {@link java.util.Collections#singletonList(Object)} this one is mutable.</p>
 *
 * @param <E> the element type
 */
@NotThreadSafe
public final class SingletonList<E> extends AbstractCollection<E> implements List<E>, Serializable, RandomAccess {

  @CheckForNull
  private E element;

  /**
   * Constructs mutable list with only the specified element.
   *
   * @param element the only element in the list, can be changed later, can be {@code null}
   */
  public SingletonList(@Nullable E element) {
    this.element = element;
  }

  @Override
  public E set(int index, E element) {
    E old = this.element;
    if (index != 0) {
      throw new IndexOutOfBoundsException("invalid index: " + index + " only 0 allowed");
    }
    this.element = element;
    return old;
  }

  @Override
  public E get(int index) {
    if (index != 0) {
      throw new IndexOutOfBoundsException("invalid index: " + index + " only 0 allowed");
    }
    return this.element;
  }

  @Override
  public void forEach(Consumer<? super E> action) {
    action.accept(this.element);
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
    return this.indexOf(o);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public void sort(Comparator<? super E> c) {
    // no op, only one element
  }

  @Override
  public int hashCode() {
    return 31 + Objects.hashCode(this.element);
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
    if (other.size() != 1) {
      return false;
    }
    return Objects.equals(this.element, other.get(0));
  }

  @Override
  public String toString() {
    if (this.element == this) {
      return "[(this Collection)]";
    } else {
      return "[" + this.element + ']';
    }
  }

  @Override
  public Object[] toArray() {
    return new Object[]{this.element};
  }

  @Override
  @SuppressWarnings("unchecked") // because arrays don't play well with generics
  public <T> T[] toArray(T[] a) {
    int length = a.length;
    if (length == 0) {
      T[] result = (T[]) Array.newInstance(a.getClass().getComponentType(), 1);
      result[0] = (T) this.element;
      return result;
    } else {
      a[0] = (T) this.element;
      if (length > 1) {
        a[1] = null;
      }
      return a;
    }
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
  public Spliterator<E> spliterator() {
    return new SingletonSpliterator<>(this.element);
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
      throw new IndexOutOfBoundsException("invalid from index: " + fromIndex + " to index: " + toIndex);
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
    // non-static because #set can modify the list

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
        return element;
      } else {
        throw new NoSuchElementException();
      }
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
      if (this.isFirst) {
        action.accept(element);
        this.isFirst = false;
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
        return element;
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
        element = e;
      }
    }

    @Override
    public void add(E e) {
      throw new UnsupportedOperationException();
    }

  }


  static final class SingletonSpliterator<E> implements Spliterator<E> {

    private final E element;
    private boolean isFirst;

    SingletonSpliterator(E value) {
      this.element = value;
      this.isFirst = true;
    }
    @Override
    public void forEachRemaining(Consumer<? super E> action) {
      if (this.isFirst) {
        action.accept(this.element);
        this.isFirst = false;
      }
    }

    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
      if (this.isFirst) {
        action.accept(this.element);
        this.isFirst = false;
        return true;
      }
      return false;
    }

    @Override
    public Spliterator<E> trySplit() {
      return null;
    }

    @Override
    public long estimateSize() {
      if (this.isFirst) {
        return 1;
      } else {
        return 0;
      }
    }

    @Override
    public long getExactSizeIfKnown() {
      return this.estimateSize();
    }

    @Override
    public int characteristics() {
      return Spliterator.ORDERED
              | Spliterator.DISTINCT
              | Spliterator.SIZED
              | Spliterator.SUBSIZED;
    }

  }

}
