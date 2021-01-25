package com.subaselvandran.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ChessMove {
    private ChessBoard board;
    private String player;
    private String owner;
    private String piece;
    private boolean capture;
    private String fromPos;
    private String toPos;
    private String fromPosFull;
    private boolean isValid;
    private String promotedPlayer;

    public ChessMove(ChessBoard board, String player, String owner, boolean capture, String fromPos, String toPos,
                     String promotedPlayer) {
        this.board = board;
        this.player = player;
        this.owner = owner;
        piece = owner + player;
        this.capture = capture;
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.promotedPlayer = promotedPlayer;
        isValid = false;
    }

    public boolean validateMove() {
        Function<String, List<String>> pawnValidate = pos -> getPawnMovements(pos) ;
        Function<String, List<String>> rookValidate = pos -> getRookMovements(pos) ;
        Function<String, List<String>> bishopValidate = pos -> getBishopMovements(pos) ;
        Function<String, List<String>> queenValidate = pos -> getQueenMovements(pos) ;
        Function<String, List<String>> kingValidate = pos -> getKingMovements(pos) ;
        Function<String, List<String>> knightValidate = pos -> getKNightMovements(pos) ;
        if (player.equals("P")) isValid = validate(pawnValidate);
        if (player.equals("R")) isValid = validate(rookValidate);
        if (player.equals("B")) isValid = validate(bishopValidate);
        if (player.equals("Q")) isValid = validate(queenValidate);
        if (player.equals("K")) isValid = validate(kingValidate);
        if (player.equals("N")) isValid = validate(knightValidate);

        if (player.equals("P") && isValid) isValid = validatePawnPromotion();
        return isValid;
    }

    public boolean makeMove() {
        boolean isWin = false;
        if (isValid) {
            if (capture) {
                String capturedPiece = board.board.get(toPos);
                board.pieceMap.get(capturedPiece).remove(toPos);
                if (checkWin(capturedPiece)) isWin = true;
            }
            if (!promotedPlayer.equals("invalid")){
                String newPiece = new StringBuffer().append(new char[] {promotedPlayer.charAt(0), owner.charAt(0)}).toString();
                board.board.put(toPos, newPiece);
                board.board.remove(fromPosFull);
                board.pieceMap.get(piece).remove(fromPosFull);
                board.pieceMap.get(newPiece).add(toPos);
                board.moves.add(fromPosFull);
            } else {
                board.board.put(toPos, piece);
                board.board.remove(fromPosFull);
                board.pieceMap.get(piece).remove(fromPosFull);
                board.pieceMap.get(piece).add(toPos);
                board.moves.add(fromPosFull);
            }
        }
        return isWin;
    }

    private boolean checkWin(String piece) {
        return piece.endsWith("K");
    }

    private boolean validate(Function<String, List<String>> func) {
        List<String> sources = new ArrayList<String>();
        for(String pos: board.pieceMap.get(piece)) {
            List<String> possibleMovements = func.apply(pos);
            if (possibleMovements.contains(toPos)) sources.add(pos);
        }
        if (sources.size() == 1) {
            fromPosFull = sources.get(0);
            return true;
        }
        if (sources.size() > 0 && isSourceAvailable(sources)) return true;
        return false;
    }

    private boolean isSourceAvailable(List<String> sources) {
        boolean available = false;
        for(String source: sources) {
            if (source.split("")[0].equals(fromPos)) {
                fromPosFull = source;
                available = true;
            }
        }
        return available;
    }

    private boolean isPositionAvailable(String pos) {
        return !board.board.containsKey(pos);
    }

    private String getOpponent() {
        if (owner.equals("W")) return "B";
        return "W";
    }

    private boolean isPositionAttackable(String pos) {
        return board.board.containsKey(pos) && board.board.get(pos).startsWith(getOpponent());
    }

    private boolean validatePawnPromotion() {
        if (toPos.endsWith("8") || toPos.endsWith("1")) return !promotedPlayer.equals("invalid");
        return true;
    }

    private List<String> getPawnMovements(String pos) {
        List<String> movements = new ArrayList<String>();
        String dest;
        if (capture) {
            dest = move(1, 0, 1, 0, pos);
            if (isPositionAttackable(dest) && isInsideBoard(dest)) movements.add(dest);
            dest = move(1, 0, 0, 1, pos);
            if (isPositionAttackable(dest) && isInsideBoard(dest)) movements.add(dest);
        } else {
            dest = move(1, 0, 0, 0, pos);
            if (isPositionAvailable(dest) && isInsideBoard(dest)) {
                movements.add(dest);
                dest = move(2, 0, 0, 0, pos);
                if (isPositionAvailable(dest) && isInsideBoard(dest) && isBase(pos)) movements.add(dest);
            }

        }
        return movements;
    }

    private boolean isInsideBoard(String pos) {
        char posHor = pos.charAt(0);
        char posVer = pos.charAt(1);
        int posHorVal = (int)posHor;
        int posVerVal = (int)posVer;
        if (posHorVal < 97 || posHorVal > 104) return false;
        if (posVerVal < 49 || posVerVal > 56) return false;
        return true;

    }

    private List<String> getRookMovements(String pos) {
        List<String> movements = new ArrayList<String>();
        String dest;
        for (int i=0; i<4; i++) {
            int forward = i == 0 ? 1: 0;
            int backward = i == 1 ? 1: 0;
            int left = i == 2 ? 1: 0;
            int right = i == 3 ? 1: 0;
            dest = move(forward, backward, left, right, pos);
            while (isInsideBoard(dest) && isPositionAvailable(dest)) {
                if (!capture) movements.add(dest);
                dest = move(forward, backward, left, right, dest);
            }
            if (capture && isInsideBoard(dest) && isPositionAttackable(dest)) movements.add(dest);
        }
        return movements;
    }
    private List<String> getBishopMovements(String pos) {
        List<String> movements = new ArrayList<String>();
        String dest;
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                int forward = i%2 == 0 ? 1: 0;
                int backward = i%2 == 1 ? 1: 0;
                int left = j%2 == 0 ? 1: 0;
                int right = j%2 == 1 ? 1: 0;
                dest = move(forward, backward, left, right, pos);
                while (isInsideBoard(dest) && isPositionAvailable(dest)) {
                    if (!capture) movements.add(dest);
                    dest = move(forward, backward, left, right, dest);
                }
                if (capture && isInsideBoard(dest) && isPositionAttackable(dest)) movements.add(dest);

            }
        }
        return movements;
    }
    private List<String> getQueenMovements(String pos) {
        List<String> movements = new ArrayList<String>();
        String dest;
        for (int i=0; i<4; i++) {
            for (int j=0; j<2; j++) {
                int forward = i%2 == 0 ? 1: 0;
                int backward = i%2 == 1 ? 1: 0;
                int left = j%2 == 0 ? 1: 0;
                int right = j%2 == 1 ? 1: 0;
                dest = move(forward, backward, left, right, pos);
                while (isInsideBoard(dest) && isPositionAvailable(dest)) {
                    if (!capture) movements.add(dest);
                    dest = move(forward, backward, left, right, dest);
                }
                if (capture && isInsideBoard(dest) && isPositionAttackable(dest)) movements.add(dest);

            }
        }
        for (int i=0; i<4; i++) {
            int forward = i == 0 ? 1: 0;
            int backward = i == 1 ? 1: 0;
            int left = i == 2 ? 1: 0;
            int right = i == 3 ? 1: 0;
            dest = move(forward, backward, left, right, pos);
            while (isInsideBoard(dest) && isPositionAvailable(dest)) {
                if (!capture) movements.add(dest);
                dest = move(forward, backward, left, right, dest);
            }
            if (capture && isInsideBoard(dest) && isPositionAttackable(dest)) movements.add(dest);
        }
        return movements;
    }

    private List<String> getKingMovements(String pos) {
        List<String> movements = new ArrayList<String>();
        String dest;
        int[][] combs = new int[][] {
                {1, 0, 0, 0},
                {1, 0, 1, 0},
                {1, 0, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
                {0, 1, 0, 0},
                {0, 1, 1, 0},
                {0, 1, 0, 1}
        };
        for(int i=0; i<combs.length; i++) {
            dest = move(combs[i][0], combs[i][1], combs[i][2], combs[i][3], pos);
            if (capture) {
                if (isInsideBoard(dest) && isPositionAttackable(dest)) movements.add(dest);
            } else if (isInsideBoard(dest) && isPositionAvailable(dest)) movements.add(dest);
        }
        return movements;
    }

    private List<String> getKNightMovements(String pos) {
        List<String> movements = new ArrayList<String>();
        String dest;
        int[][] combs = new int[][] {
                {2, 0, 1, 0},
                {2, 0, 0, 1},
                {1, 0, 2, 0},
                {1, 0, 0, 2},
                {0, 2, 1, 0},
                {0, 2, 0, 1},
                {0, 1, 0, 2},
                {0, 1, 2, 0}
        };
        for(int i=0; i<combs.length; i++) {
            dest = move(combs[i][0], combs[i][1], combs[i][2], combs[i][3], pos);
            if (capture) {
                if (isInsideBoard(dest) && isPositionAttackable(dest)) movements.add(dest);
            } else if (isInsideBoard(dest) && isPositionAvailable(dest)) movements.add(dest);
        }
        return movements;
    }

    private boolean isBase(String pos){
        if (player.equals("P")) {
            if (owner.equals("W")) return pos.endsWith("2");
            else return pos.endsWith("7");
        }
        return false;
    }

    private String move(int forward, int backward, int left, int right, String pos) {
        char posHor = pos.charAt(0);
        char posVer = pos.charAt(1);
        if (owner.equals("W")) {
            char newPosHor = (char) (posHor + right);
            newPosHor = (char) (newPosHor - left);
            char newPosVer = (char) (posVer + forward);
            newPosVer = (char) (newPosVer - backward);
            return new StringBuffer().append(new char[] {newPosHor, newPosVer}).toString();
        } else {
            char newPosHor = (char) (posHor + right);
            newPosHor = (char) (newPosHor - left);
            char newPosVer = (char) (posVer - forward);
            newPosVer = (char) (newPosVer + backward);
            return new StringBuffer().append(new char[] {newPosHor, newPosVer}).toString();
        }
    }

}
