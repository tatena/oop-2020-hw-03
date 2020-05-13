package Sudoku;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.SupportedSourceVersion;
import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuTest {
    // Provided easy 1 6 grid
    // (can paste this text into the GUI too)

    private String emptyGrid;
    private Sudoku easy;
    private Sudoku medium;
    private Sudoku hard;
    private Sudoku empty;

    private final String[] easyGridInput = new String[] {
            "1 6 4 0 0 0 0 0 2",
            "2 0 0 4 0 3 9 1 0",
            "0 0 5 0 8 0 4 0 7",
            "0 9 0 0 0 6 5 0 0",
            "5 0 0 1 0 2 0 0 8",
            "0 0 8 9 0 0 0 3 0",
            "8 0 9 0 4 0 2 0 0",
            "0 7 3 5 0 9 0 0 1",
            "4 0 0 0 0 0 6 7 9"
    };

    private final String[] mediumGridInput = new String[] {
            "530070000",
            "600195000",
            "098000060",
            "800060003",
            "400803001",
            "700020006",
            "060000280",
            "000419005",
            "000080079"
    };

    private final String[]  hardGridInput = new String[] {
            "3 7 0 0 0 0 0 8 0",
            "0 0 1 0 9 3 0 0 0",
            "0 4 0 7 8 0 0 0 3",
            "0 9 3 8 0 0 0 1 2",
            "0 0 0 0 4 0 0 0 0",
            "5 2 0 0 0 6 7 9 0",
            "6 0 0 0 2 1 0 4 0",
            "0 0 0 5 3 0 9 0 0",
            "0 3 0 0 0 0 0 5 1"
    };


    private String stringsToOneString(String... rows) {
        String res = "";
        for (int row = 0; row<rows.length; row++) {
            res += rows[row];
        }
        return res;
    }

    @BeforeEach
    public void setSudoku() {
        easy = new Sudoku(Sudoku.easyGrid);
        medium = new Sudoku(Sudoku.mediumGrid);
        hard = new Sudoku(Sudoku.hardGrid);
        emptyGrid = "";
        for(int i = 0; i < Sudoku.SIZE * Sudoku.SIZE; i++)  {
            emptyGrid += "0 ";
        }
        empty = new Sudoku(emptyGrid);
    }

    @Test
    public void testToString() {
        int[][] expected = Sudoku.textToGrid(stringsToOneString(easyGridInput));
        int[][] actual = Sudoku.textToGrid(easy.toString());
        assertTrue(Arrays.deepEquals(expected, actual));

        expected = Sudoku.textToGrid(emptyGrid);
        actual = Sudoku.textToGrid(empty.toString());
        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void testSolve() {
        int numSol = easy.solve();
        assertEquals(1, numSol);

        String ansText = easy.getSolutionText();
        int[][] resArr = Sudoku.textToGrid(ansText);
        assertTrue(isCorrect(resArr));

        numSol = medium.solve();
        assertEquals(1, numSol);

        ansText = medium.getSolutionText();
        resArr = Sudoku.textToGrid(ansText);
        assertTrue(isCorrect(resArr));

        numSol = hard.solve();
        assertEquals(1, numSol);

        ansText = hard.getSolutionText();
        resArr = Sudoku.textToGrid(ansText);
        assertTrue(isCorrect(resArr));
    }

    @Test
    public void testSolveEmpty() {
        int numSol = empty.solve();
        assertEquals(100, numSol);

        String ansText = empty.getSolutionText();
        int[][] resArr = Sudoku.textToGrid(ansText);
        assertTrue(isCorrect(resArr));

        resArr = Sudoku.textToGrid(empty.toString());
        assertFalse(isCorrect(resArr));

        for(int i = 0; i< Sudoku.SIZE; i++)
            resArr[0][i] = i + 1;
        assertFalse(isCorrect(resArr));

        for(int i = 0; i< Sudoku.SIZE; i++)
            resArr[i][0] = i + 1;
        assertFalse(isCorrect(resArr));
    }


    @Test
    public  void testParsingException() {
        Exception exception  = assertThrows(RuntimeException.class, () -> new Sudoku("7 7 8 "));
        String expectedMessage = "Needed 81 numbers, but got:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testMain() {
        hard.main(new String[1]);
    }

    private boolean isCorrect(int[][] easy) {
        for (int i=0; i < Sudoku.SIZE; i++) {
            for(int j = 0; j < Sudoku.SIZE; j++) {
                if (!checkCurrSpot(i, j, easy))
                    return false;
            }
        }
        return true;
    }

    private boolean checkCurrSpot(int x, int y, int[][] grid) {
        HashSet<Integer> nums = new HashSet<>();
        nums.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int i = 0; i < Sudoku.SIZE; i++) {
            if(!nums.contains(grid[x][i]))
                return false;
            nums.remove(grid[x][i]);
        }


        nums.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int i = 0; i < Sudoku.SIZE; i++) {
            if(!nums.contains(grid[i][y]))
                return false;
            nums.remove(grid[i][y]);
        }

        nums.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        int x0 = (x / 3) * 3;
        int y0 = (y / 3) * 3;
        for (int i = 0; i < Sudoku.PART; i++) {
            for (int j = 0; j < Sudoku.PART; j++) {
                if(!nums.contains(grid[x0+i][y0+j]))
                    return false;
                nums.remove(grid[x0+i][y0+j]);
            }
        }
        return true;
    }

}