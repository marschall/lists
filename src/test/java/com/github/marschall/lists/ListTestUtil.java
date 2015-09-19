package com.github.marschall.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

final class ListTestUtil {

  static <T> List<T> collect(Stream<T> stream) {
    List<T> result = new ArrayList<>();
    stream.forEach(each -> result.add(each));
    return result;
  }

  private ListTestUtil() {
    throw new AssertionError("not instantiable");
  }

}
