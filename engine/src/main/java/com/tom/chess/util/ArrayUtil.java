package com.tom.chess.util;

public class ArrayUtil {
  private ArrayUtil() {}

  public static <T> T[] reverse(T[] array) {
    var a = array.clone();
    for (int i = 0; i < a.length / 2; i++) {
      T temp = a[i];
      a[i] = a[a.length - 1 - i];
      a[a.length - 1 - i] = temp;
    }
    return a;
  }
}
