package com.github.ki10v01t.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.github.ki10v01t.util.message.Message;

public class Gamer {
    private Socket associatedSocket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public Gamer(Socket associatedSocket) throws IOException {
        this.associatedSocket = associatedSocket;
        this.reader = new ObjectInputStream(associatedSocket.getInputStream());
        this.writer = new ObjectOutputStream(associatedSocket.getOutputStream());
    }

    public void setAssociatedSocket(Socket associatedSocket) {
        this.associatedSocket = associatedSocket;
    }

    public Socket getAssociatedSocket() {
        return associatedSocket;
    }

    public void sendMessage(Message message) throws IOException {
        writer.writeObject(message);
        writer.flush();
    }

    public Message receiveMessage() throws IOException, ClassNotFoundException, ClassCastException {
        Object inputObject = reader.readObject();
        if(inputObject instanceof Message) {
            return (Message) inputObject;
        } else {
            throw new ClassCastException("Error while deserialing");
        }
    }

}
