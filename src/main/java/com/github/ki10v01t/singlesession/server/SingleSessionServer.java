package com.github.ki10v01t.singlesession.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import com.github.ki10v01t.util.Player;


public class SingleSessionServer {
    private final ServerSocket serverSock;
    private Socket newClientSock;
    private PlayersStorage playersStorage;
    private ExecutorService threadPool;

    public SingleSessionServer(ServerSocket serverSocket) {
        this.serverSock = serverSocket;
        this.playersStorage = new PlayersStorage(new ArrayList<Player>(2));
        this.threadPool = Executors.newFixedThreadPool(1);

        threadPool.execute(new RequestManager(playersStorage));
    }

    public void start() {
        //logger.trace(String.format("Server starts listening at port %d", serverSock.getLocalPort()));
        System.out.println(String.format("Server starts listening at port %d", serverSock.getLocalPort()));
        Boolean exitFlag = false;
        while(!exitFlag) {
            try {
                newClientSock = serverSock.accept();
                Player player = new Player(newClientSock);
                playersStorage.addPlayer(player);
                System.out.println(String.format("Player has been appended to storage"));

                if(threadPool.isTerminated()) {
                    exitFlag = true;
                }
            } catch (IOException ioe) {
                Stream.of(ioe.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
                exitFlag = true;
            } catch (SecurityException se) {
                Stream.of(se.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
                exitFlag = true;
            }
        }
        System.out.println("Server job stops");
    }
}
