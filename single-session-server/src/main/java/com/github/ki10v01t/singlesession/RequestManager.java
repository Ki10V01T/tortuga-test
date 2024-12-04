package com.github.ki10v01t.singlesession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ki10v01t.util.Game;
import com.github.ki10v01t.util.Player;
import com.github.ki10v01t.util.message.Command;
import com.github.ki10v01t.util.message.Message;
import com.github.ki10v01t.util.message.Type;

public class RequestManager implements Runnable {
    private final Logger logger;
    private final List<Player> playersList;
    private final ReentrantLock locker;
    private final Condition enoughPlayers;
    private final Game gameSession;

    public RequestManager(List<Player> playersList, ReentrantLock locker, Condition enoughPlayers) {
        this.playersList = playersList;
        this.locker = locker;
        this.enoughPlayers = enoughPlayers;
        this.gameSession = new Game();
        this.logger = LogManager.getLogger(RequestManager.class);  
    }

    @Override
    public void run() {
        logger.info(String.format("RequestManager started at thread with name %s", Thread.currentThread().getName()));

        try {
            while(playersList.size() < 2) {
                playersList.get(0).sendMessage(new Message.MessageBuilder(Type.INFO).setText("Waiting for player 2").build());
                enoughPlayers.await();
            }
        } catch (InterruptedException ie) {
            locker.unlock();
            this.notifyAll();
        } catch (IOException ioe) {

        }

        while(true) {
            Message msg;
            Boolean exitFlag = false;
            for(int i = 0; i < playersList.size(); i++) {
                Player p = playersList.get(i);
                try{
                    msg = p.receiveMessage();

                    if(msg.getType() == Type.COMMAND) {
                        switch (msg.getCommand()) {
                            case STEP:
                                msg = gameSession.makeStep(msg.getRow(), msg.getColumn(), p.getPlayerId());

                                int shiftPlayerIndex = 0;
                                if(i == 0) shiftPlayerIndex = 1;
                                
                                playersList.get(shiftPlayerIndex).sendMessage(new Message.MessageBuilder(Type.COMMAND).setCommand(Command.REFRESH).build());
                                break;
                            case EXIT:
                                exitFlag = true;
                                this.notifyAll();
                                break;
                            default:
                                break;
                        }
                    }
                    if(exitFlag) break;

                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                    break;
                } catch (ClassNotFoundException cnfe) {
                    logger.error(cnfe.getMessage(), cnfe);
                    break;
                }
            }
            logger.info(String.format("RequestManager finilized itself work at thread %s", Thread.currentThread().getName()));
        }
    }

}
