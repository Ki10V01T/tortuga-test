package com.github.ki10v01t;

import java.io.IOException;
import java.net.ServerSocket;

import com.github.ki10v01t.singlesession.SingleSessionServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try(ServerSocket serverSocket = new ServerSocket(11500,2)){
            SingleSessionServer sss = new SingleSessionServer(serverSocket);
        } catch (IOException ioe) {
            //TODO
        };
    }
}
