package com.github.ki10v01t.util;

import com.github.ki10v01t.util.message.Command;
import com.github.ki10v01t.util.message.Message;
import com.github.ki10v01t.util.message.Type;

public class Game {
    private int[][] gameField;
    private int stepCounter;
    private int winner;

    public Game() {
        this.gameField = new int[3][3];
        this.stepCounter = 0;
        this.winner = 0;
    }

    /**
     * Game field initialization. Empty value of cell = 0. 1 or -1 otherwise. 
     */
    private void initGameField() {
        for(int i = 0; i < gameField.length; i++) {
            for(int j = 0; j < gameField[i].length; j++) {
                gameField[i][j] = 0;
            }            
        }
    }

    private boolean boundsCheck(int row, int column) {
        boolean rowCheck = false, columnCheck = false;
        if(row < 0 || row > gameField.length-1) {
            rowCheck = true;
        }

        if(column < 0 || column > gameField.length-1 ) {
            columnCheck = true;
        }

        if(rowCheck && columnCheck) {
            return true;
        }
        return false;
    }

    private boolean valueCheck(int value) {
        if((value == 1) || (value == -1)) {
            return true;
        }
        return false;
    }

    private int verticalCheck() {
        int playersVerticalCounter = 0;

        for(int j = 0; j < gameField.length; j++) {
            for(int i = 0; i < gameField.length; i++) {
                if(gameField[i][j] == 1) {
                    playersVerticalCounter++;
                }
                if(gameField[i][j] == -1) {
                    playersVerticalCounter--;
                }
            }
            
            if(playersVerticalCounter == 3) {
                return 1;
            }
            if(playersVerticalCounter == -3) {
                return 2;
            }
            playersVerticalCounter = 0;
        }
        return 0;
    }

    private int horizontalCheck() {
        int playersHorizontalCounter = 0;

        for(int i = 0; i < gameField.length; i++) {
            for(int j = 0; j < gameField.length; j++) {
                if(gameField[i][j] == 1) {
                    playersHorizontalCounter++;
                }
                if(gameField[i][j] == -1) {
                    playersHorizontalCounter--;
                }
            }
            
            if(playersHorizontalCounter == 3) {
                return 1;
            }
            if(playersHorizontalCounter == -3) {
                return 2;
            }
            playersHorizontalCounter = 0;
        }
        return 0;
    }

    private int diagonalCheck() {
        int diagonalCheckLeft = 0, diagonalCheckRight = 0;

        for(int li = 0, ri = gameField.length-1; li < gameField.length && ri > -1; li++, ri--) {
            if(gameField[li][li] == 1) {
                diagonalCheckLeft++;
            }
            if(gameField[li][li] == -1) {
                diagonalCheckLeft--;
            }
            if(gameField[ri][ri] == 1) {
                diagonalCheckRight++;
            }
            if(gameField[ri][ri] == -1) {
                diagonalCheckRight--;
            }
        }

        if(diagonalCheckLeft == 3 || diagonalCheckRight == 3) {
            return 1;
        }
        if(diagonalCheckLeft == -3 || diagonalCheckRight == -3) {
            return 2;
        }

        return 0;
    }

    /**
     * 
     * @return
     */
    private int winnerCheck() {
        int verticalResult = verticalCheck();
        int horizontalResult = horizontalCheck();
        int diagonalResult = diagonalCheck();

        if(verticalResult == 1 || horizontalResult == 1 || diagonalResult == 1) {
            return 1;
        }
        if(verticalResult == -1 || horizontalResult == -1 || diagonalResult == -1) {
            return 2;
        }
        
        return 0;
    }

    /**
     * Player step. The intersection of the row and column values describes a cell that is a 3x3 playing field
     * @param row Field id of 3x3 playing field
     * @param column Column id of 3x3 playing field
     * @param value Value that equals 1 for Player 1, and equals -1 for Player 2
     */
    public Message makeStep(Integer row, Integer column, int value) {

        if(!boundsCheck(row, column) || !valueCheck(value)) {
            return new Message.MessageBuilder(Type.ERROR).setText("Error insert invalid column_id or row_id").build();
        }
        
        if(gameField[row][column] != 0) {
            return new Message.MessageBuilder(Type.ERROR).setText(String.format("Value at cell, that represented as column_id = %d and row_id = %d is already taken your opponent", column, row)).build();
        }
        
        gameField[row][column] = value;

        int winnerPlayer = winnerCheck();
        
        String endGameMessage;
        Boolean earlyVictoryFlag = false;

        switch (winnerPlayer) {
            case 1:
                endGameMessage = "Player 1 win!";
                earlyVictoryFlag = true;
                break;
            case -1:
                endGameMessage = "Player 2 win!";
                earlyVictoryFlag = true;
                break;
            case 0:
                endGameMessage = "The game ended in a draw.";
                break;
            default:
                endGameMessage = "Unexpected result";
            break;
        }

        if(earlyVictoryFlag) {
            return new Message.MessageBuilder(Type.COMMAND).setCommand(Command.END_GAME).setText(endGameMessage).build();
        }
  
        return new Message.MessageBuilder(Type.COMMAND).setCommand(Command.END_GAME).setText(endGameMessage).build();
    }
}
