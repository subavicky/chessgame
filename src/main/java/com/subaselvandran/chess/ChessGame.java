package com.subaselvandran.chess;

import javax.swing.*;
import java.util.*;

public class ChessGame {
    public static Map<String, ChessGame> games = new HashMap<String, ChessGame>();
    private String id;
    private String currentMove = "W";
    private ChessBoard board;

    public String getId() {
        return id;
    }

    public ChessGame() {
        String gameId = UUID.randomUUID().toString();
        id = gameId;
        games.put(gameId, this);
        board = new ChessBoard();
    }

    private String getPlayer(String[] move) {
        List<String> playerNotations = new ArrayList<String>(Arrays.asList("K", "Q", "R", "N", "B"));
        if (playerNotations.contains(move[0])) {
            return move[0];
        } else {
            return "invalid";
        }
    }

    private boolean getCapture(String[] move) {
        if (move[0].equals("x")) return true;
        return false;
    }

    private String getFromPos(String[] move) {
        if (move.length == 3) {
            if (checkPosHor(move[0])) {
                return move[0];
            }
        }
        return "invalid";
    }

    private String getToPos(String[] move) {
        if (checkPosHor(move[0])) {
            return move[0];
        }
        return "invalid";
    }

    private String getToPosVer(String[] move) {
        if (checkPosVer(move[0])) {
            return move[0];
        }
        return "invalid";
    }

    private boolean checkPosHor(String pos) {
        List<String> posArr = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
        return posArr.contains(pos);
    }

    private boolean checkPosVer(String pos) {
        List<String> posArr = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
        return posArr.contains(pos);
    }

    private String getPromotedPlayer(String[] move) {
        if (move[0].equals("=")) {
            List<String> playerNotations = new ArrayList<String>(Arrays.asList("K", "Q", "R", "N", "B", "P"));
            if (playerNotations.contains(move[1])) return move[1];
        }
        return "invalid";
    }

    private void toggleMove() {
        if (currentMove.equals("W")) currentMove = "B";
        else currentMove = "W";
    }

    public String playMove(String moveCommand) {
        boolean debug = true;
        String[] moveArray = moveCommand.split("");
        String player = getPlayer(moveArray);
        if (player.equals("invalid")) player = "P";
        else moveArray = Arrays.copyOfRange(moveArray, 1, moveArray.length);
        if (debug) System.out.println(Arrays.toString(moveArray));

        boolean capture = getCapture(moveArray);
        if (capture) moveArray = Arrays.copyOfRange(moveArray, 1, moveArray.length);
        if (debug) System.out.println(Arrays.toString(moveArray));

        String fromPos = getFromPos(moveArray);
        if (!fromPos.equals("invalid"))moveArray = Arrays.copyOfRange(moveArray, 1, moveArray.length);
        if (debug) System.out.println(Arrays.toString(moveArray));

        String toPosHor = getToPos(moveArray);
        if (!toPosHor.equals("invalid")) moveArray = Arrays.copyOfRange(moveArray, 1, moveArray.length);
        else return "Invalid";
        if (debug) System.out.println(Arrays.toString(moveArray));

        String toPosVer = getToPosVer(moveArray);
        if (!toPosVer.equals("invalid")) moveArray = Arrays.copyOfRange(moveArray, 1, moveArray.length);
        else return "Invalid";

        String promotedPlayer = "invalid";
        if (moveArray.length > 0) {
            if (moveArray.length == 2) {
                promotedPlayer = getPromotedPlayer(moveArray);
                if (promotedPlayer.equals("invalid")) return "Invalid";
            } else return "Invalid";
        }

        String toPos = new StringBuffer().append(new char[] {toPosHor.charAt(0), toPosVer.charAt(0)}).toString();
        if (debug) {
            System.out.println(Arrays.toString(moveArray));
            System.out.println(player);
            System.out.println(capture);
            System.out.println(fromPos);
            System.out.println(toPosHor);
            System.out.println(toPosVer);
        }
        ChessMove move = new ChessMove(board, player, currentMove, capture, fromPos, toPos, promotedPlayer);
        if (!move.validateMove()) return "Invalid";
        boolean isWin = move.makeMove();
        if (debug) board.printBoard();
        if (isWin) return String.format("%s wins", currentMove);
        // checkMoveValidity(player, capture, fromPos, toPos, toPosVer);
        toggleMove();
        return "Valid";
    }
}
