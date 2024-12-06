package com.github.ki10v01t.singlesession.server;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.stream.Stream;

import com.github.ki10v01t.util.Game;
import com.github.ki10v01t.util.Player;
import com.github.ki10v01t.util.message.Command;
import com.github.ki10v01t.util.message.Message;
import com.github.ki10v01t.util.message.Type;

public class RequestManager implements Runnable {
    private final PlayersStorage playersStorage;
    private Boolean disconnectionFlag;
    private final Game gameSession;

    public RequestManager(PlayersStorage playersStorage) {
        this.playersStorage = playersStorage;
        this.gameSession = new Game();
        this.disconnectionFlag = false;
    }

    private void disconnectAllClients() {
        playersStorage.removeAllPlayers();
    }

    private void sendToOnePlayer(Integer id, Message message) throws IOException {
        if(!playersStorage.getOneById(id).getAssociatedSocket().isConnected()) {
            playersStorage.removePlayerById(id);
            disconnectionFlag = true;
        } else {
            playersStorage.getOneById(id).sendMessage(message);
        }
    }

    private void broadcastMessage(Message message) throws IOException {
        for(int i = 0; i < playersStorage.getPlayersCount(); i++) {
            if(!playersStorage.getOneById(i).getAssociatedSocket().isConnected()) {
                playersStorage.removePlayerById(i);
                disconnectionFlag = true;
            } else {
                playersStorage.getOneById(i).sendMessage(message);
            }
        }
    }

    private void prepareGame() {
        String playerId;
        try {
            if(playersStorage.getPlayersCount() < 2) {
                Command messageCommand;
                if(disconnectionFlag) {
                    messageCommand = Command.RECONNECT;
                    this.gameSession.resetGameSession();
                } else {
                    messageCommand = Command.ADD_PLAYER_1;
                }
                playerId = "1";
                Player p = playersStorage.getOneById(0);
                p.sendMessage(new Message.MessageBuilder(Type.INFO).setCommand(messageCommand).setText(playerId).build());
                p.setPlayerId(Integer.parseInt(playerId));
                System.out.println(String.format("Player %s has been connected to server", playerId));
            } 
            playersStorage.waitPlayer2(); 
            Player p1 = playersStorage.getOneById(0);
            Player p2 = playersStorage.getOneById(1);
            playerId = "2";
            p2.sendMessage(new Message.MessageBuilder(Type.INFO).setCommand(Command.ADD_PLAYER_2).setText(playerId).build());
            p2.setPlayerId(Integer.parseInt(playerId));
            p1.sendMessage(new Message.MessageBuilder(Type.INFO).setCommand(Command.READY).build());
            //p1.setPlayerId(Integer.parseInt(playerId));
            disconnectionFlag = false;
            System.out.println(String.format("Player %s has been connected to server", playerId));
        } catch (IOException ioe) {
            // Stream.of(ioe.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
            // return;
        }
    }

    @Override
    public void run() {
        System.out.println(String.format("RequestManager started at thread with name %s", Thread.currentThread().getName()));
        Boolean exitFlag = false;
        Message msg;

        prepareGame();
        while(!exitFlag) {
            if(disconnectionFlag) {
                prepareGame();
            }
            for(int i = 0; i < playersStorage.getPlayersCount(); i++) {
                Player p = playersStorage.getOneById(i);
                try{
                    msg = p.receiveMessage();

                    switch (msg.getType()) {
                        case COMMAND:
                            switch (msg.getCommand()) {
                                case STEP:
                                    int tempId = Integer.parseInt(msg.getText());
                                    msg = gameSession.makeStep(msg.getRow(), msg.getColumn(), Integer.parseInt(msg.getText()));  
                                    // if(msg.getType() == Type.ERROR && tempId == p.getPlayerId()-1) {
                                        if(msg.getType() == Type.ERROR) {
                                        sendToOnePlayer(tempId-1, msg);
                                        i = tempId;
                                        // sendToOnePlayer(p.getPlayerId()-1, msg);
                                        continue;
                                    }                              
                                    broadcastMessage(msg);
                                    break;
                                case EXIT:
                                    disconnectionFlag = true;
                                    //exitFlag = true;
                                    msg = new Message.MessageBuilder(Type.COMMAND).setCommand(Command.EXIT).build();
                                    broadcastMessage(msg);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ERROR:
                            switch (msg.getCommand()) {
                                case REPEAT_MSG:
                                    sendToOnePlayer(Integer.parseInt(msg.getText())-1, msg);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                } catch (StreamCorruptedException sce) {
                    System.err.println("Stream Corrupted");
                    //Stream.of(sce.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
                    exitFlag = true;
                } catch (IOException ioe) {
                    System.err.println("All players has been disconnected");
                    //disconnectionFlag = true;
                    disconnectAllClients();
                    break;
                    //Stream.of(ioe.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
                    
                    //exitFlag = true;
                } catch (ClassNotFoundException cnfe) {
                    System.err.println("RM ClassNotFound");
                    Stream.of(cnfe.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
                    exitFlag = true;
                }
            }
        }
        System.out.println(String.format("RequestManager finilized itself work at thread %s", Thread.currentThread().getName()));
    }

}
