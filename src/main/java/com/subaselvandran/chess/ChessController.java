package com.subaselvandran.chess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessController {

    @Autowired
    private ChessService chessService ;

    @PostMapping("/game")
    public String game(@RequestBody String body, @RequestHeader (name="Authorization", required = false) String token) {
        System.out.println(body + token);
        if (token != null) {
            token = token.split(" ")[1];
        }
        return chessService.game(body, token);
    }
}
