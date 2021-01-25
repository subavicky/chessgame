package com.subaselvandran.chess;

public class test {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.printBoard();
        ChessMove move = new ChessMove(board, "P", "W", false, "-1", "a4", "invalid");
        System.out.println(move.validateMove());
        move.makeMove();
        move = new ChessMove(board, "P", "B", false, "-1", "a5", "invalid");
        System.out.println(move.validateMove());
        move.makeMove();
        move = new ChessMove(board, "P", "W", false, "-1", "b4", "invalid");
        System.out.println(move.validateMove());
        move.makeMove();
        // move = new ChessMove(board, "P", "B", false, "-1", "c5", "invalid");
        // System.out.println(move.validateMove());
        // move.makeMove();
        move = new ChessMove(board, "P", "B", true, "-1", "b4", "invalid");
        System.out.println(move.validateMove());
        move.makeMove();
        // move = new ChessMove(board, "P", "W", false, "a", "a6", "invalid");
        // System.out.println(move.validateMove());
        // move.makeMove();
        // move = new ChessMove(board, "P", "W", false, "-1", "d4", "invalid");
        // System.out.println(move.validateMove());
        // move.makeMove();
        // move = new ChessMove(board, "Q", "W", false, "-1", "d2", "invalid");
        // System.out.println(move.validateMove());
        // move.makeMove();
        // move = new ChessMove(board, "Q", "W", true, "-1", "b4", "invalid");
        // System.out.println(move.validateMove());
        // move.makeMove();
        // move = new ChessMove(board, "N", "W", false, "-1", "d2", "invalid");
        // System.out.println(move.validateMove());
        // move.makeMove();
        board.printBoard();
    }
}
