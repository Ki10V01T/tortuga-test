package com.github.ki10v01t.singlesession;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.ki10v01t.util.Player;


public class SingleSessionServer {
    private final Logger logger;
    private final ServerSocket serverSock;
    private Socket newClientSock;
    private final List<Player> playersList;
    private ExecutorService threadPool;
    private ReentrantLock locker;
    private Condition enoughPlayers;

    public SingleSessionServer(ServerSocket serverSocket) {
        //this.logger = LogManager.getLogger("SingleSessionServer");
        this.logger = LogManager.getLogger(SingleSessionServer.class);
        this.serverSock = serverSocket;
        this.playersList = new ArrayList<Player>(2);
        this.threadPool = Executors.newFixedThreadPool(1);
        this.locker = new ReentrantLock();
        this.enoughPlayers = locker.newCondition();
    }

    public void start() {
        //logger.trace(String.format("Server starts listening at port %d", serverSock.getLocalPort()));
        logger.info(String.format("Server starts listening at port %d", serverSock.getLocalPort()));
        Integer playerId = 0;
        while(true) {
            try {
                if(playersList.size() > 1) {
                    if(!threadPool.isShutdown()) {
                        //threadPool.execute(new RequestManager(playersList));
                        threadPool.execute(new RequestManager(playersList, locker, enoughPlayers));
                        threadPool.shutdown();
                    }
                    this.wait();
                    break;
                } else {
                    if(playersList.size() < 2) {
                        locker.lock();
                    }
                    newClientSock = serverSock.accept();
                    playerId++;
                    Player player = new Player(newClientSock, playerId);
                    playersList.add(player);
                    logger.info(String.format("Player %d has been connected to server", playerId));
                }
            } catch (IOException ioe) {
                logger.error(ioe.getMessage(), ioe);
            } catch (SecurityException se) {
                logger.error(se.getMessage(), se);
            } catch (InterruptedException ie) {
                logger.error(ie.getMessage(), ie);
            }
        }
        logger.info("Server job stops");
    }
}
