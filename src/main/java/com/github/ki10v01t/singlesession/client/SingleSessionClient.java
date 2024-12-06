package com.github.ki10v01t.singlesession.client;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Stream;

import com.github.ki10v01t.util.Game;
import com.github.ki10v01t.util.Player;
import com.github.ki10v01t.util.message.Command;
import com.github.ki10v01t.util.message.Message;
import com.github.ki10v01t.util.message.Type;

public class SingleSessionClient {
    private final Player player;
    private final Game gameSession;


    public SingleSessionClient(Socket clientSocket) throws IOException {
        this.player = new Player(clientSocket);
        this.gameSession = new Game();
    }

    private void inputProcessor(String userInput, Scanner sc) throws IOException, InputMismatchException{
        System.out.println("Enter command: ");
        userInput = sc.next();
        switch (userInput) {
            case "s":
                System.out.println("enter the coordinates in next format: (without brackets) \n [row]");
                int inputRaw = sc.nextInt();
                System.out.println("[column]");
                int inputColumn = sc.nextInt();
                player.sendMessage(new Message.MessageBuilder(Type.COMMAND).setCommand(Command.STEP).setRow(inputRaw).setColumn(inputColumn).setText(String.valueOf(player.getPlayerId())).build());
                break;
            case "e":
                player.sendMessage(new Message.MessageBuilder(Type.COMMAND).setCommand(Command.EXIT).build());
                break;
            default:
                System.out.println("Unknown command. Type again");
                inputProcessor(userInput, sc);
                //player.sendMessage(new Message.MessageBuilder(Type.ERROR).setCommand(Command.REPEAT_MSG).setText(String.valueOf(player.getPlayerId())).build());
                break;
        }
    }

    // private void inputProcessor(String userInput, Scanner sc) throws IOException, InputMismatchException{
    //     switch (userInput) {
    //         case "s":
    //             System.out.println("enter the coordinates in next format: (without brackets) \n [row]");
    //             int inputRaw = sc.nextInt();
    //             System.out.println("[column]");
    //             int inputColumn = sc.nextInt();
    //             player.sendMessage(new Message.MessageBuilder(Type.COMMAND).setCommand(Command.STEP).setRow(inputRaw).setColumn(inputColumn).setText(String.valueOf(player.getPlayerId())).build());
    //             break;
    //         case "e":
    //             player.sendMessage(new Message.MessageBuilder(Type.COMMAND).setCommand(Command.EXIT).build());
    //             break;
    //         default:
    //             System.out.println("Unknown command. Type again");
    //             inputProcessor(userInput, sc);
    //             //player.sendMessage(new Message.MessageBuilder(Type.ERROR).setCommand(Command.REPEAT_MSG).setText(String.valueOf(player.getPlayerId())).build());
    //             break;
    //     }
    // }


    public void start() {
        System.out.println(String.format("Client started at thread %s", Thread.currentThread().getName()));
        Boolean exitFlag = false;   

        try(Scanner sc = new Scanner(System.in)) {
        while(!exitFlag) {
            Message msg;
            String userInput = null;
                msg = player.receiveMessage();

                switch (msg.getType()) {
                    case COMMAND:
                        switch (msg.getCommand()) {
                            case REFRESH:
                                gameSession.makeStep(msg.getRow(), msg.getColumn(), Integer.parseInt(msg.getText()));
                                gameSession.printGameField();
                                if(Integer.parseInt(msg.getText()) == player.getPlayerId()) {
                                    System.out.println("Waiting for next step of the opponent");
                                    continue;
                                }
                                break;
                            case END_GAME:
                                System.out.println(String.format("Session is over. %s", msg.getText()));
                                exitFlag = true;
                                continue;
                            case EXIT:
                                exitFlag = true;
                                continue;
                            default:
                                break;
                        }
                        break;
                    case INFO:
                        switch (msg.getCommand()) {
                            case ADD_PLAYER_1:
                                player.setPlayerId(Integer.parseInt(msg.getText()));
                                System.out.println(String.format("Your id is %s", player.getPlayerId()));
                                System.out.println("Waiting for the opponent to connect");
                                continue;
                            case ADD_PLAYER_2:
                                player.setPlayerId(Integer.parseInt(msg.getText()));
                                System.out.println(String.format("Your id is %s", player.getPlayerId()));
                                System.out.println("Waiting for first step of the opponent");
                                continue;
                            case RECONNECT:
                                player.setPlayerId(Integer.parseInt(msg.getText()));
                                System.out.println("Your opponent has been disconnected. New game started.");
                                continue;
                            case READY:
                                break;
                            default:
                                break;
                        }
                        break;
                    case ERROR:
                        System.err.println(msg.getText());
                        break;
                    default:
                        player.sendMessage(new Message.MessageBuilder(Type.COMMAND).setCommand(Command.EXIT).build());
                        System.err.println("Unknown error");
                        break;
                }

                
                // System.out.println("Enter command: ");
                // userInput = sc.next();

                inputProcessor(userInput, sc);
                
            }
        } catch (IOException ioe) {
            Stream.of(ioe.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
            exitFlag = true;
        } catch (ClassNotFoundException cnfe) {
            Stream.of(cnfe.getStackTrace()).map(el -> el.toString()).forEach(System.err::println);
            exitFlag= true;
        } catch (NumberFormatException nfe) {
            System.err.println("Entered coordinates is not numbers");
            exitFlag = true;
        } catch (InputMismatchException ime) {
            System.err.println("Entered coordinates is not numbers");
            exitFlag = true;
        }
            System.out.println(String.format("Client finilized itself work at thread %s", Thread.currentThread().getName()));
    }
}
