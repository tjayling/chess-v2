package com.tom.commandline;

import static com.tom.commandline.util.TextUtil.printHelp;
import static com.tom.commandline.util.TextUtil.printInputError;
import static com.tom.commandline.util.TextUtil.printInvalidFenError;

import java.util.List;
import java.util.Scanner;
import com.tom.chess.engine.Engine;
import com.tom.chess.engine.EngineV2;
import com.tom.chess.model.Fen;
import com.tom.commandline.util.TextUtil;
import com.tom.exception.InvalidFenException;

public class CommandProcessor {
  private boolean isRunning;
  private Engine engine;
  private Fen fen;

  public void start() {
    fen = Fen.defaultFen();
    engine = new EngineV2(fen);

    isRunning = true;
    TextUtil.printTitle();

    while (isRunning) {
      var command = getUserInput();
      processCommand(command);
    }
  }

  public static String getUserInput() {
    System.out.print("chess:>");
    var scanner = new Scanner(System.in);
    return scanner.nextLine();
  }

  private void processCommand(String input) {
    var commands = List.of(input.split("\\s+"));
    var initialCommand = commands.getFirst();

    switch (initialCommand) {
      case "quit" -> stop();
      case "help" -> printHelp();
      case "makeMove" -> go(commands);
      case "position" -> setFen(commands.get(1));
      default -> printInputError();
    }
  }

  private void go(List<String> commands) {
    var secondCommand = commands.get(1);
    var success = true;
    switch (secondCommand) {
      case "perft" -> engine.go(commands, fen);
      default -> success = false;
    }
    if (!success) {
      printInputError();
    }
  }

  private void setFen(String fen) {
    try {
      this.fen = Fen.parse(fen);
    } catch (InvalidFenException e) {
      printInvalidFenError();
    }
  }

  private void stop() {
    isRunning = false;
    System.out.println("): bye bye :(");
  }
}
