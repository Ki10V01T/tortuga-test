package com.github.ki10v01t;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private int[][] gameField;

    public AppTest() {
        this.gameField = new int[3][3];
        initGameField();
    }
    /**
     * Rigorous Test :-)
     */

    private void initGameField() {
        int counter = 0;
        for(int i = 0; i < gameField.length; i++) {
            for(int j = 0; j < gameField[i].length; j++) {
                gameField[i][j] = counter++;
            }            
        }
    }
    @Test
    public void shouldAnswerWithTrue()
    {
        for(int i = 0; i < gameField.length; i++) {
            for(int j = 0; j < gameField[i].length; j++) {
                System.out.print(gameField[i][j] + " ");
            }
            System.out.println();
        }
        Assertions.assertTrue( true );
    }
}
