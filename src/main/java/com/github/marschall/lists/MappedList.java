package com.github.marschall.lists;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A read only view on a {@link List} after applying a {@link Function}.
 *
 * <p>This class is intended to be used when you already have a potentially
 * large list and want another list based on invoking the same function on
 * every element.</p>
 *
 * <p>This list behaves similar to {@link java.util.stream.Stream#map(Function)}.</p>
 *
 * <p>
 * This list does not support modification.
 *
 * @param <E> the type of elements in this list
 * @param <O> the original element type of the underlying list
 */
public final class MappedList<E, O> extends AbstractCollection<E> implements List<E>, Serializable, RandomAccess {
  // extend AbstractCollection instead of AbstractList to avoid the unused modcount instance variable
  // RandomAccess because likely the underlying list implements it as well (eg. ArrayList)

  private final Function<O, E>  mapFunction;

  private final List<O> delegate;

  /**
   * Constructor.
   *
   * @param mapFunction the function to produce the items in this list
   * @param delegate the list on who to run {@code mapFunction}
   */
  public MappedList(Function<O, E> mapFunction, List<O> delegate) {
    this.mapFunction = mapFunction;
    this.delegate = delegate;
  }

  @Override
  public int size() {
    return this.delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    int size = this.size();
    for (int i = 0; i < size; ++i) {
      if (Objects.equals(o, this.get(i))) {
        return true;
      }
    }
    return false;
  }


  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!this.contains(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Iterator<E> iterator() {
    return new MappedIterator<>(this.mapFunction, this.delegate.iterator());
  }

  public int hashCode() {
    int hashCode = 1;
    for (O each : this.delegate) {
      hashCode = 31 * hashCode + Objects.hashCode(this.mapFunction.apply(each));
    }
    return hashCode;
  }

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;

    }
    if (!(obj instanceof List)) {
      return false;
    }
    List<?> other = (List<?>) obj;
    int size = this.delegate.size();
    if (size != other.size()) {
      return false;
    }
    // TODO check if other implements RandomAccess
    for (int i = 0; i < size; ++i) {
      if (!Objects.equals(this.mapFunction.apply(this.delegate.get(i)), other.get(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Object[] toArray() {
    int size = this.size();
    Object[] result = new Object[size];
    for (int i = 0; i < size; ++i) {
      result[i] = this.mapFunction.apply(this.delegate.get(i));
    }
    return result;
  }

  @SuppressWarnings("unchecked") // because arrays don't play well with generics
  @Override
  public <T> T[] toArray(T[] a) {
    int size = this.size();
    if (a.length < size) {
      T[] result = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
      for (int i = 0; i < size; ++i) {
        result[i] = (T) this.mapFunction.apply(this.delegate.get(i));
      }
      return result;
    } else {
      for (int i = 0; i < size; ++i) {
        a[i] = (T) this.mapFunction.apply(this.delegate.get(i));
      }
      if (a.length > size) {
        a[size] = null;
      }
      return a;
    }
  }

  @Override
  public boolean add(E e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o) {
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

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public E get(int index) {
    return this.mapFunction.apply(this.delegate.get(index));
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
  public E remove(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int indexOf(Object o) {
    int size = this.size();
    for (int i = 0; i < size; ++i) {
      if (Objects.equals(o, this.get(i))) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    for (int i = this.size() - 1; i >= 0; --i) {
      if (Objects.equals(o, this.get(i))) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public ListIterator<E> listIterator() {
    return new MappedListIterator<>(this.mapFunction, this.delegate.listIterator());
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return new MappedListIterator<>(this.mapFunction, this.delegate.listIterator(index));
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return new MappedList<>(this.mapFunction, this.delegate.subList(fromIndex, toIndex));
  }

  @Override
  public Spliterator<E> spliterator() {
    return new MappedSpliterator<>(this.mapFunction, this.delegate.spliterator());
  }

  static final class MappedIterator<E, T> implements Iterator<E> {

    private final Function<T, E> mapFunction;
    private final Iterator<T> delegate;

    MappedIterator(Function<T, E> mapFunction, Iterator<T> delegate) {
      this.mapFunction = mapFunction;
      this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
      return this.delegate.hasNext();
    }

    @Override
    public E next() {
      return this.mapFunction.apply(this.delegate.next());
    }

  }

  static final class MappedListIterator<E, T> implements ListIterator<E> {

    private final Function<T, E> mapFunction;
    private final ListIterator<T> delegate;

    MappedListIterator(Function<T, E> mapFunction, ListIterator<T> delegate) {
      this.mapFunction = mapFunction;
      this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
      return this.delegate.hasNext();
    }

    @Override
    public E next() {
      return this.mapFunction.apply(this.delegate.next());
    }

    @Override
    public boolean hasPrevious() {
      return this.delegate.hasPrevious();
    }

    @Override
    public E previous() {
      return this.mapFunction.apply(this.delegate.previous());
    }

    @Override
    public int nextIndex() {
      return this.delegate.nextIndex();
    }

    @Override
    public int previousIndex() {
      return this.delegate.previousIndex();
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

  static final class MappedSpliterator<E, O> implements Spliterator<E> {
    // done to preserve the characteristics of the Spliterator of the underlying list
    // getComparator currently missing, would be slow

    private final Function<O, E> mapFunction;

    private final Spliterator<O> delegate;

    MappedSpliterator(Function<O, E> mapFunction, Spliterator<O> delegate) {
      this.mapFunction = mapFunction;
      this.delegate = delegate;
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
      this.delegate.forEachRemaining(e -> action.accept(this.mapFunction.apply(e)));
    }

    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
      return this.delegate.tryAdvance(e -> action.accept(this.mapFunction.apply(e)));
    }

    @Override
    public Spliterator<E> trySplit() {
      Spliterator<O> splitted = this.delegate.trySplit();
      if (splitted != null) {
        return new MappedSpliterator<>(this.mapFunction, splitted);
      } else {
        return null;
      }
    }

    @Override
    public long estimateSize() {
      return this.delegate.estimateSize();
    }

    @Override
    public long getExactSizeIfKnown() {
      return this.delegate.getExactSizeIfKnown();
    }

    @Override
    public int characteristics() {
      return this.delegate.characteristics();
    }

  }

}
