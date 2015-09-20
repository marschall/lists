package com.github.marschall.lists;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A Lisp-style list with a head and a tail.
 *
 * <p>This list exists because <code>ArrayList.add(0, element)</code>
 * has linear complexity. This list is intended to be used when you
 * want to add to the start of an {@link java.util.ArrayList}</p>
 *
 * <p>
 * This list does support modification if the underlying list supports it.
 *
 * @param <E> the element type
 */
@NotThreadSafe
public final class PrefixedList<E> extends AbstractList<E>implements Serializable, RandomAccess {
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
  public boolean isEmpty() {
    return false;
  }

  @Override
  public int size() {
    return this.cdr.size() + 1;
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
