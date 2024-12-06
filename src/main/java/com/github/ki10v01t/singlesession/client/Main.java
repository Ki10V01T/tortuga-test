package com.github.ki10v01t.singlesession.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Integer port;
        String host = null;

        try{
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            port = 11555;
            if(host == null || host == "") {
                host = "127.0.0.1";
            }
            System.err.println(String.format("Number format is not correct. Sets default port to %d", port));
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            host = "127.0.0.1";
            port = 11555;
        }

        try(Socket clientSocket = new Socket(host, 11555)) {
            SingleSessionClient ssc = new SingleSessionClient(clientSocket);
            ssc.start();
        } catch (UnknownHostException uhe) {
            System.err.println("Unknown host");
        } catch (IOException ioe) {
            Stream.of(ioe.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
        }
    }
}
