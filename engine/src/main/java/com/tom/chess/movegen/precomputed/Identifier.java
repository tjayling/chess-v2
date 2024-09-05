package com.tom.chess.movegen.precomputed;

public record Identifier(
    int startSquare,
    long blockPattern
) {}
