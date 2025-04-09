package com.example.sudokuGenerator;

import java.util.Random;

public class SudokuGenerator {

    public int[][] generate(int clues) {
        int[][] board = new int[9][9];

        // Initialize with -1
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = -1;

        fillDiagonalBoxes(board);
        solveSudoku(board, 0, 0);
        return removeCells(board, 81 - clues);
    }

    private void fillDiagonalBoxes(int[][] board) {
        for (int i = 0; i < 9; i += 3) {
            fillBox(board, i, i);
        }
    }

    private void fillBox(int[][] board, int row, int col) {
        Random rand = new Random();
        for (int i = 0; i < 9; ) {
            int num = rand.nextInt(9) + 1;
            if (isBoxSafe(board, row, col, num)) {
                board[row + i / 3][col + i % 3] = num;
                i++;
            }
        }
    }

    private boolean isBoxSafe(int[][] board, int rowStart, int colStart, int num) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[rowStart + i][colStart + j] == num)
                    return false;
        return true;
    }

    private boolean solveSudoku(int[][] board, int row, int col) {
        if (row == 9) return true;
        if (col == 9) return solveSudoku(board, row + 1, 0);
        if (board[row][col] != -1) return solveSudoku(board, row, col + 1);

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board, row, col + 1)) return true;
                board[row][col] = -1; // backtrack
            }
        }

        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                    board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private int[][] removeCells(int[][] board, int toRemove) {
        Random rand = new Random();
        int removed = 0;

        while (removed < toRemove) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            if (board[row][col] != -1) {
                board[row][col] = -1;
                removed++;
            }
        }
        return board;
    }
}
