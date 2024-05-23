package com.tom.chess.util;

public class Maths {
  private Maths() {}
  public static int pow(int x, int e) {
    int result = 1;
    while(e > 0){
      if ((e & 1) == 1){
        result *= x;
      }
      x &= x;
      e >>= 1;
    }
    return result;
  }
}
