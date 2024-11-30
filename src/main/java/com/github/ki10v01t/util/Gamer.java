package com.github.ki10v01t.util;

import java.net.Socket;

public class Gamer {
    private Socket associatedSocket;

    public Gamer(Socket associatedSocket) {
        this.associatedSocket = associatedSocket;
    }

}
