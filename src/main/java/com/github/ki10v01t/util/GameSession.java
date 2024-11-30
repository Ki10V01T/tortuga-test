package com.github.ki10v01t.util;

import java.util.concurrent.Callable;

public class GameSession implements Callable<String>{
    private final Game game;
    private final Gamer[] gamers;

    public GameSession(Game game, Gamer[] gamers) {
        this.game = game;
        this.gamers = gamers;
    }

    @Override
    public String call() throws Exception {
        while (true) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'call'");
        }
    }
}
