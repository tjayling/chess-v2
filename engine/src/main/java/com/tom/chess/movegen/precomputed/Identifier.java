package com.tom.chess.movegen.precomputed;

import lombok.Value;

public record Identifier(
    int startSquare,
    long blockPattern
) {}
