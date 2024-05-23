package com.tom.exception;

public class InvalidFenException extends RuntimeException {
  public InvalidFenException(Throwable throwable) {
    super("Could not parse invalid fen", throwable);
  }

  public InvalidFenException(String message) {
    super(message);
  }

  public InvalidFenException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
