package com.github.ki10v01t.server;

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

import com.github.ki10v01t.util.GameSession;
import com.github.ki10v01t.util.Gamer;
import com.github.ki10v01t.util.message.InitMessage;
import com.github.ki10v01t.util.message.Message;

public class GameServer {
    private final Logger logger;
    private final ServerSocket serverSock;
    private final List<CompletableFuture<String>> gameSessions;
    private Socket newClientSock;
    private ExecutorService lobbyThreadPool;
    private 

    public GameServer(ServerSocket serverSocket) {
        this.gameSessions = new ArrayList<>();
        this.serverSock = serverSocket;
        this.logger = LogManager.getLogger("Game Server");
        this.lobbyThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void start() {
        while(true) {
            try {
                newClientSock = serverSock.accept();
                Gamer gamer = new Gamer(newClientSock);
                
                InitMessage initMessage = newClientSock.getInputStream().

                gameSessions.add(CompletableFuture.runAsync(new GameSession(null, gamer)));
                
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
