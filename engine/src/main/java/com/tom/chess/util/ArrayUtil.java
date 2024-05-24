package com.tom.chess.util;

import java.util.Arrays;
import java.util.stream.Stream;

public class ArrayUtil {
  private ArrayUtil() {
  }

  public static <T> T[] reverse(T[] array) {
    var a = array.clone();
    for (int i = 0; i < a.length / 2; i++) {
      T temp = a[i];
      a[i] = a[a.length - 1 - i];
      a[a.length - 1 - i] = temp;
    }
    return a;
  }

  public static <T> T[] concat(T[] a, T[] b) {
    return Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray(size -> Arrays.copyOf(a, size));
  }
}
