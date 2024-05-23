package com.tom.chess.util;

import java.util.function.Function;

public class Coord extends Tuple<Integer, Integer> {
  public Coord(Integer x, Integer y) {
    super(x, y);
  }

  public Coord(int startSquare) {
    int x = startSquare % 8;
    int y = Math.floorDivExact(startSquare, 8);
    super(x, y);
  }

  public Coord add(Coord that) {
    return new Coord(this.getX() + that.getX(), this.getY() + that.getY());
  }

  public Coord mult(int m) {
    return new Coord(this.getX() * m, this.getY() * m);
  }

  public boolean isValidSquare() {
    return getX() >= 0 && getX() < 8 && getY() >= 0 && getY() < 8;
  }

  public int getSquareIndex() {
    return getY() * 8 + getX();
  }

  public int getX() {
    return super.getA();
  }

  public int getY() {
    return super.getB();
  }
}
