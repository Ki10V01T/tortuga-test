package com.github.ki10v01t.singlesession.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {   

    public static void main(String[] args) {
        Integer port;
        try{
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            port = 11555;
            System.err.println(String.format("Number format is not correct. Sets default port to %d", port));
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            port = 11555;
        }
        
        try(ServerSocket serverSocket = new ServerSocket(port,2)){
            SingleSessionServer sss = new SingleSessionServer(serverSocket);
            sss.start();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        };
    }
}
