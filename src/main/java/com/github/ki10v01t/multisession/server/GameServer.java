package com.github.ki10v01t.multisession.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ki10v01t.multisession.GameProcessor;
import com.github.ki10v01t.util.Player;
import com.github.ki10v01t.util.message.InitMessage;
import com.github.ki10v01t.util.message.Message;

public class GameServer {
    private final Logger logger;
    private final ServerSocket serverSock;
    private final CompletableFuture<String> requestProcessor;
    private Socket newClientSock;
    private ExecutorService lobbyThreadPool;

    public GameServer(ServerSocket serverSocket) {
        this.gameSessions = new ArrayList<>();
        this.serverSock = serverSocket;
        this.logger = LogManager.getLogger("Game Server");
        this.lobbyThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private void handShake(Player gamer) throws IOException, ClassNotFoundException{
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
                break;
            case STEP:
                msg = gamer.receiveMessage();
                break;
            case APPEND_TO_GAME:
                break;
            default:
                break;
        }
    }

    public void start() {
        while(true) {
            try {
                newClientSock = serverSock.accept();
                Player gamer = new Player(newClientSock);
                
                InitMessage initMessage = newClientSock.getInputStream().

                requestProcessor.runAsync(new GameProcessor(null, gamer));
                
            } catch (IOException ioe) {
                logger.error(ioe.getMessage(), ioe);
            } catch (SecurityException se) {
                logger.error(se.getMessage(), se);
            } catch (ClassNotFoundException cnfe) {
                logger.error(cnfe.getMessage(), cnfe);
            } 
        }
    }

}
