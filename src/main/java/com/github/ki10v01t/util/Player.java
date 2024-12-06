package com.github.ki10v01t.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import com.github.ki10v01t.util.message.Message;

public class Player {
    private Integer playerId;
    private Socket associatedSocket;
    // private ObjectInputStream reader;
    // private ObjectOutputStream writer;

    public Player(Socket associatedSocket) throws IOException {
        this.associatedSocket = associatedSocket;
        // this.reader = new ObjectInputStream(associatedSocket.getInputStream());
        // this.writer = new ObjectOutputStream(associatedSocket.getOutputStream());
    }

    public Player(Socket associatedSocket, Integer playerId) throws IOException {
        this.associatedSocket = associatedSocket;
        this.playerId = playerId;
    }

    public void setAssociatedSocket(Socket associatedSocket) {
        this.associatedSocket = associatedSocket;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public Socket getAssociatedSocket() {
        return associatedSocket;
    }

    public void sendMessage(Message message) throws IOException {
        ObjectOutputStream writer = new ObjectOutputStream(associatedSocket.getOutputStream());
        writer.writeObject(message);
        writer.flush();
    }

    public Message receiveMessage() throws IOException, StreamCorruptedException, ClassNotFoundException {
        ObjectInputStream reader = new ObjectInputStream(associatedSocket.getInputStream());
        Object inputObject = reader.readObject();
        if(inputObject instanceof Message) {
            return (Message) inputObject;
        } else {
            throw new ClassCastException("Error while deserializing");
        }
    }

}
