package com.subaselvandran.chess;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChessService {

    private int maxGames = 10;

    public String game(String requestString, String token) {
        if (requestString.equals("START")) {
            if (ChessGame.games.size() < maxGames) {
                ChessGame chess = new ChessGame();
                return chess.getId();
            } else {
                return "Maximum number of parallel games reached. Please wait";
            }
        } else if(token != null) {
            System.out.println(ChessGame.games.keySet());
            if (ChessGame.games.containsKey(token)) {
                ChessGame chess = ChessGame.games.get(token);
                return chess.playMove(requestString);
            } else {
                return "Not a valid game token";
            }
        } else {
            return "Game token is required to play the game";
        }
    }
}
