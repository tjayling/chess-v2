package com.tom.chess.util;

import java.util.Objects;

public class StringUtil {
  private StringUtil() {
  }

  public static boolean isEmpty(String s) {
    return Objects.isNull(s) || !s.isBlank();
  }

  public static boolean isNotEmpty(String s) {
    return !isEmpty(s);
  }
}
