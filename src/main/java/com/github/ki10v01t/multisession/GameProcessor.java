package com.github.ki10v01t.multisession;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import com.github.ki10v01t.util.message.InstructionMessage;
import com.github.ki10v01t.util.message.Message;

public class GameProcessor implements Runnable {
    private Map<String, GameSession> activeGamesList;
    private BlockingQueue<Message> gameCommandsQueue;

    public GameProcessor(BlockingQueue<InstructionMessage> gameCommandsQueue) {
        activeGamesList = new HashMap<>();
    }

    private void startNewGameSession() {
        byte[] byteArray = new byte[20];
        Random random = new Random();
        random.nextBytes(byteArray);
        String gameId = new String(byteArray, Charset.forName("UTF-8"));

        Game game = new Game();
        GameSession gameSession = new GameSession(game, gameId);
        activeGamesList.put(gameId, game);
    }

    private void appendToGame(String gameId) {

    }

    private void processingRequests(){
        
    }
}
