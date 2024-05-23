package com.tom.chess.util;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class Tuple<T, U> {
  T a;
  U b;
}
