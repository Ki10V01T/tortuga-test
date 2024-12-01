package com.github.ki10v01t.util;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.github.ki10v01t.util.message.InitMessage;
import com.github.ki10v01t.util.message.Message;

public class Lobby implements Callable<String> {
    private final Gamer opponent;
    private final Game game;

    private void handShake(Gamer gamer) throws IOException, ClassNotFoundException{
        Message msg = new InitMessage("Приветствую, игрок! Ты присоединился к серверу игры в крестики-нолики.\nВыбери необходимое действие:");
        gamer.sendMessage(msg);
        msg = gamer.receiveMessage();
        
        switch (msg.getCommand()) {
            case NEW_GAME:
                initGameSession();
                break;
            case EXIT:
                msg.setText("До новых встреч!");
                gamer.sendMessage(msg);
                gamer.getAssociatedSocket().close();
            default:
                break;
        }
    }
    
    private void initGameSession(){

    }

    @Override
    public String call() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'call'");
    }

}
