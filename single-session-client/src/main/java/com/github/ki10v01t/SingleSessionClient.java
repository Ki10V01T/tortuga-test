package com.github.ki10v01t;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.Command;
import org.apache.logging.log4j.message.Message;

public class SingleSessionClient {
    private final Logger logger;
    private final Socket clientSocket;

    public SingleSessionClient(Socket clientSocket) {
        this.logger = LogManager.getLogger(SingleSessionClient.class);
        this.clientSocket = clientSocket;
    }

    public void start() {
        while(true) {
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
