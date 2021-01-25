package com.subaselvandran.chess;

import java.util.*;

public class ChessBoard {
    public Map<String, String> board = new HashMap<String, String>();
    public Map<String, ArrayList<String>> pieceMap = new HashMap<String, ArrayList<String>>();
    public List<String> moves = new ArrayList<String>();

    public ChessBoard() {
        board.put("a1", "WR");
        board.put("b1", "WN");
        board.put("c1", "WB");
        board.put("d1", "WQ");
        board.put("e1", "WK");
        board.put("f1", "WB");
        board.put("g1", "WN");
        board.put("h1", "WR");
        board.put("a8", "BR");
        board.put("b8", "BN");
        board.put("c8", "BB");
        board.put("d8", "BQ");
        board.put("e8", "BK");
        board.put("f8", "BB");
        board.put("g8", "BN");
        board.put("h8", "BR");
        for (int i=0; i<8; i++) {
            StringBuffer sbw = new StringBuffer();
            sbw.append(new char[] {(char) (97 + i), '2'});
            board.put(sbw.toString(), "WP");
            StringBuffer sbb = new StringBuffer();
            sbb.append(new char[] {(char) (97 + i), '7'});
            board.put(sbb.toString(), "BP");
        }

        for(String pos: board.keySet()) {
            String piece = board.get(pos);
            if (!pieceMap.containsKey(piece)) pieceMap.put(piece, new ArrayList<>());
            pieceMap.get(piece).add(pos);
        }
    }

    public void printBoard() {
        for (int i=8; i>0; i--) {
            for (int j=1; j<9; j++) {
                StringBuffer sb = new StringBuffer();
                sb.append(new char[] {(char) (96 + j), (char) (48 + i)});
                String pos = sb.toString();
                String piece = "x";
                if (board.containsKey(pos)) piece = board.get(pos);
                System.out.printf("%s-%s\t",pos, piece);
            }
            System.out.println();
        }
    }
}
