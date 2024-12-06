package com.github.ki10v01t.singlesession.server;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.github.ki10v01t.util.Player;

public class PlayersStorage {
    private final List<Player> playersList;
    private ReentrantLock locker;
    private Condition waitingPlayer;

    public PlayersStorage(List<Player> playersList) {
        this.playersList = playersList;
        this.locker = new ReentrantLock();
        this.waitingPlayer = locker.newCondition();
    }

    public Integer getPlayersCount() {
        locker.lock();
        try {
            while (playersList.isEmpty()) {
                waitingPlayer.await();
            }
            waitingPlayer.signalAll();
        } catch (InterruptedException ie) {
            
        } finally {
            locker.unlock();
        }
        return playersList.size();
    }

    public void waitPlayer2() {
        locker.lock();
        try {
            while(!playersList.isEmpty() && playersList.size() < 2) {
                waitingPlayer.await();
            }
            waitingPlayer.signalAll();
        } catch (InterruptedException ie) {
            
        } finally {
            locker.unlock();
        }
    }

    public Player getOneById(int id) {
        locker.lock();
        try {
            while(playersList.isEmpty()) {
                waitingPlayer.await();
            }
            waitingPlayer.signalAll();
        } catch (InterruptedException ie) {
            
        } finally {
            locker.unlock();
        }
        return playersList.get(id);
    }

    public void addPlayer(Player player) {
        locker.lock();
        try {
            while(playersList.size() == 2) {
                waitingPlayer.await();
            }
            playersList.add(player);
            waitingPlayer.signalAll();
        } catch (InterruptedException ie) {

        } finally {
            locker.unlock();
        }
    }

    public void removeAllPlayers() {
        playersList.clear();
    }

    public void removePlayer(Player player) {
        locker.lock();
        try {
            while(playersList.isEmpty()) {
                waitingPlayer.await();
            }
            playersList.remove(player);
            waitingPlayer.signalAll();
        } catch (InterruptedException ie) {

        } finally {
            locker.unlock();
        }
    }

    public void removePlayerById(int id) {
        locker.lock();
        try {
            while(playersList.isEmpty()) {
                waitingPlayer.await();
            }
            playersList.remove(id);
            waitingPlayer.signalAll();
        } catch (InterruptedException ie) {

        } finally {
            locker.unlock();
        }
    }

}
