package com.tom.middleware;

import com.tom.chess.model.Fen;
import com.tom.chess.model.Moves;
import lombok.NonNull;

public record MediatorState(
    @NonNull
    Fen fen,
    @NonNull
    Moves moves
) {}