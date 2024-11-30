package com.github.ki10v01t.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ki10v01t.util.Gamer;

public class GameServer {
    private final Logger logger;
    private final ServerSocket serverSock;
    private final List<CompletableFuture<Void>> connectedGamersList;
    private Socket newClientSock;

    public GameServer(ServerSocket serverSocket) {
        this.connectedGamersList = new ArrayList<>();
        this.serverSock = serverSocket;
        this.logger = LogManager.getLogger("Game Server");
    }

    public void start() {
        while(true) {
            try {
                newClientSock = serverSock.accept();
                Gamer gamer = new Gamer(newClientSock);
                connectedGamersList.add(CompletableFuture.runAsync(gamer));
                
            } catch (IOException ioe) {
                logger.error(ioe.getMessage(), ioe);
            } catch (SecurityException se) {
                logger.error(se.getMessage(), se);
            }
        }
    }

}
