package com.github.ki10v01t.multisession;

import java.util.ArrayList;
import java.util.List;

import com.github.ki10v01t.multisession.exception.AppendException;
import com.github.ki10v01t.util.message.InstructionMessage;
import com.github.ki10v01t.util.message.Message;

public class GameSession {
    private List<String> playerIdsList;
    private Game game;

    public GameSession(Game game, String playerId) {
        this.game = game;
        this.playerIdsList = new ArrayList<>();
        this.playerIdsList.add(playerId);
    }

    public Message appendToGame(String gameId, String playerId) throws AppendException {
        if(playerIdsList.size() > 2) {
            throw new AppendException(String.format("Error appending to game with gameId = %s\nNumber of players is higher than 2", gameId));
        }
        if(playerIdsList.contains(playerId)) { 
            throw new AppendException(String.format("Error appending to game: player with id = %s is already appended to game session", playerId));
        }
        playerIdsList.add(playerId);
    }

}
