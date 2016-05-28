package com.github.marschall.lists;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A Lisp-style list with a head and a tail.
 *
 * <p>This list exists because <code>ArrayList#add(0, element)</code>
 * has linear complexity. This list is intended to be used when you
 * want to add to the start of an {@link java.util.ArrayList}</p>
 *
 * <p>This list does support modification if the underlying list supports it.</p>
 *
 * <p>This list gets more ineffient the more of them are chained together.</p>
 *
 * @param <E> the element type
 */
@NotThreadSafe
public final class PrefixedList<E> extends AbstractList<E>implements List<E>, Serializable, RandomAccess {
  // extend AbstractCollection instead of AbstractList to avoid the unused modcount instance variable
  // RandomAccess because likely the cdr list implements it as well (eg. ArrayList)

  @CheckForNull
  private E car;
  private final List<E> cdr;

  public PrefixedList(@Nullable E head, List<E> tail) {
    this.car = head;
    this.cdr = tail;
  }

  @Override
  public E get(int index) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("negative index: " + index);
    }
    if (index == 0) {
      return this.car;
    }
    return this.cdr.get(index - 1);
  }

  @Override
  public E set(int index, E element) {
    if (index < 0) {
      throw new IndexOutOfBoundsException("negative index: " + index);
    }
    if (index == 0) {
      E previous = this.car;
      this.car = element;
      return previous;
    }
    return this.cdr.set(index - 1, element);
  }

  @Override
  public void forEach(Consumer<? super E> action) {
    action.accept(this.car);
    this.cdr.forEach(action);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public int size() {
    return this.cdr.size() + 1;
  }

  @Override
  public boolean add(E e) {
    return this.cdr.add(e);
  }

  @Override
  public void add(int index, E element) {
    if (index == 0) {
      throw new UnsupportedOperationException();
    }
    this.cdr.add(index - 1, element);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return this.cdr.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    if (index == 0) {
      throw new UnsupportedOperationException();
    }
    return this.cdr.addAll(index - 1, c);
  }

  @Override
  public boolean remove(Object o) {
    if (Objects.equals(this.car, o)) {
      throw new UnsupportedOperationException();
    }
    return this.cdr.remove(o);
  }

  @Override
  public E remove(int index) {
    if (index == 0) {
      throw new UnsupportedOperationException();
    }
    return this.cdr.remove(index - 1);
  }

  @Override
  public boolean contains(Object o) {
    return Objects.equals(this.car, o) || this.cdr.contains(o);
  }

  @Override
  public int indexOf(Object o) {
    if (Objects.equals(this.car, o)) {
      return 0;
    } else {
      int cdrIndex = this.cdr.indexOf(o);
      if (cdrIndex == -1) {
        return cdrIndex;
      } else {
        return cdrIndex + 1;
      }
    }
  }

  @Override
  public int lastIndexOf(Object o) {
    int cdrIndex = this.cdr.lastIndexOf(o);
    if (cdrIndex == -1) {
      return Objects.equals(this.car, o) ? 0 : -1;
    } else {
      return cdrIndex + 1;
    }
  }

}
