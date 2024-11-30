package com.github.ki10v01t.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Gamer {
    private Socket associatedSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Gamer(Socket associatedSocket) throws IOException {
        this.associatedSocket = associatedSocket;
        this.reader = new BufferedReader(new InputStreamReader(associatedSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(associatedSocket.getOutputStream()));
    }

    public void sendMessage(String message) throws IOException {
        writer.write(message);
        writer.flush();
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

}
