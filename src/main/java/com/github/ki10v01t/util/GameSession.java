package com.github.ki10v01t.util;

import java.util.concurrent.Callable;

public class GameSession {
    private final String gameId;
    private final Gamer gamer1, gamer2;

    public GameSession(Game game, Gamer gamer1) {
        this.game = game;
        this.gamer1 = gamer1;
    }

    private void waitingForGamer2() {

    }

    private void processingRequests(){
        
    }
}
