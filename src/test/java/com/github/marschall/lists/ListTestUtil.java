package com.github.marschall.lists;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

final class ListTestUtil {

  static <T> List<T> collect(Stream<T> stream) {
    List<T> result = new ArrayList<>();
    stream.forEach(each -> result.add(each));
    return result;
  }

  static Object copy(Object serializable) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(serializable);
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais)) {
      return ois.readObject();
    }
  }

  private ListTestUtil() {
    throw new AssertionError("not instantiable");
  }

}
