package com.github.ki10v01t.util;

public class Game {
    private int[][] gameField;

    public Game() {
        this.gameField = new int[3][3];
    }

    private boolean rowColumnCheck(int row, int column) {
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
        if((value < 1) || (value > 1)) {
            return false;
        }
        return true;
    }

    /**
     * Ход участника. Пересечение значений строки и столбца описывают ячейку поля 3х3
     * @param row Строка поля
     * @param column Столбец поля
     * @param value Значение от игрока 1 = 1, от игрока 2 = 2
     */
    public void makeStep(int row, int column, int value) {
        
        if(gameField[row][column] == 0) {

        }
    }
}
