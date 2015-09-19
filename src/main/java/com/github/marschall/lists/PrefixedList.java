package com.github.marschall.lists;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * A Lisp-style list with a head and a tail.
 *
 * <p>This list exists because <code>ArrayList.add(0, element)</code>
 * has linear complexity. This list is intended to be used when you
 * want to add to the start of an {@link ArrayList}</p>
 *
 * <p>
 * This list does not support modification if the underlying list supports it.
 *
 * @param <E> the element type
 */
public final class PrefixedList<E> extends AbstractList<E>implements Serializable, RandomAccess {
  // extend AbstractCollection instead of AbstractList to avoid the unused modcount instance variable
  // RandomAccess because likely the cdr list implements it as well (eg. ArrayList)

  private final E car;
  private final List<E> cdr;

  public PrefixedList(E head, List<E> tail) {
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
  public boolean isEmpty() {
    return false;
  }

  @Override
  public int size() {
    return this.cdr.size() + 1;
  }

}
