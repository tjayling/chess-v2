package com.tom.chess.model;

import java.util.List;
import java.util.stream.Stream;
import lombok.Data;

@Data
public class BitBoards {
  // WHITE
  private BitBoard whitePieces;
  private BitBoard whitePawns;
  private BitBoard whiteKnights;
  private BitBoard whiteBishops;
  private BitBoard whiteRooks;
  private BitBoard whiteQueens;
  private BitBoard whiteKing;

  // BLACK
  private BitBoard blackPieces;
  private BitBoard blackPawns;
  private BitBoard blackKnights;
  private BitBoard blackBishops;
  private BitBoard blackRooks;
  private BitBoard blackQueens;
  private BitBoard blackKing;

  private BitBoard empty;

  public BitBoards(Fen fen) {
    var fenString = fen.getFenBoardString();

    this.whitePawns = generateWhitePawnsBitBoard(fenString);
    this.whiteKnights = generateWhiteKnightsBitBoard(fenString);
    this.whiteBishops = generateWhiteBishopsBitBoard(fenString);
    this.whiteRooks = generateWhiteRooksBitBoard(fenString);
    this.whiteQueens = generateWhiteQueensBitBoard(fenString);
    this.whiteKing = generateWhiteKingBitBoard(fenString);

    this.whitePieces = BitBoard.orAll(whitePawns, whiteKnights, whiteBishops, whiteRooks, whiteQueens, whiteKing);

    this.blackPawns = generateBlackPawnsBitBoard(fenString);
    this.blackKnights = generateBlackKnightsBitBoard(fenString);
    this.blackBishops = generateBlackBishopsBitBoard(fenString);
    this.blackRooks = generateBlackRooksBitBoard(fenString);
    this.blackQueens = generateBlackQueensBitBoard(fenString);
    this.blackKing = generateBlackKingBitBoard(fenString);

    this.blackPieces = BitBoard.orAll(blackPawns, blackKnights, blackBishops, blackRooks, blackQueens, blackKing);

    this.empty = BitBoard.orAll(whitePieces, blackPieces).not();

//    stream().forEach(BitBoard::print);
  }
//  public BitBoards(BitBoard empty, BitBoard white, BitBoard whitePawns, BitBoard whiteKnights, BitBoard whiteBishops, BitBoard whiteRooks, BitBoard whiteQueens, BitBoard whiteKing, BitBoard black,
//      BitBoard blackPawns, BitBoard blackKnights, BitBoard blackBishops, BitBoard blackRooks, BitBoard blackQueens, BitBoard blackKing) {
//    this.empty = empty;
//    this.white = white;
//    this.whitePawns = whitePawns;
//    this.whiteKnights = whiteKnights;
//    this.whiteBishops = whiteBishops;
//    this.whiteRooks = whiteRooks;
//    this.whiteQueens = whiteQueens;
//    this.whiteKing = whiteKing;
//    this.black = black;
//    this.blackPawns = blackPawns;
//    this.blackKnights = blackKnights;
//    this.blackBishops = blackBishops;
//    this.blackRooks = blackRooks;
//    this.blackQueens = blackQueens;
//    this.blackKing = blackKing;
//  }

  public List<BitBoard> asList() {
    return List.of(empty, whitePieces, whitePawns, whiteKnights, whiteBishops, whiteRooks, whiteQueens, whiteKing, blackPieces, blackPawns, blackKnights, blackBishops, blackRooks, blackQueens, blackKing);
  }

  public Stream<BitBoard> stream() {
    return asList().stream();
  }


  private BitBoard generateWhitePawnsBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'P');
  }

  private BitBoard generateWhiteKnightsBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'N');
  }

  private BitBoard generateWhiteBishopsBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'B');
  }

  private BitBoard generateWhiteRooksBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'R');
  }

  private BitBoard generateWhiteQueensBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'Q');
  }

  private BitBoard generateWhiteKingBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'K');
  }

  private BitBoard generateBlackPawnsBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'p');
  }

  private BitBoard generateBlackKnightsBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'n');
  }

  private BitBoard generateBlackBishopsBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'b');
  }

  private BitBoard generateBlackRooksBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'r');
  }

  private BitBoard generateBlackQueensBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'q');
  }

  private BitBoard generateBlackKingBitBoard(String fenString) {
    return generateBitBoardFromFen(fenString, 'k');
  }

  private BitBoard generateBitBoardFromFen(String fenString, char piece) {
    return BitBoard.parse(fenString.split("\\s+")[0].replaceAll(String.valueOf(piece), "1").replaceAll("[^1]", "0"));
  }
}
