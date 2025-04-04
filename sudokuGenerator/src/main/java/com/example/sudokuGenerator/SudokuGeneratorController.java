package com.example.sudokuGenerator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/sudoku")
public class SudokuGeneratorController {
    @GetMapping("/generate")
    public int[][] generateSudoku() {
        int[][] board = new int[9][9];
        fillBoard(board);
        removeNumbers(board);
        return board;
    }

    private void fillBoard(int[][] board) {
        solveSudoku(board, 0, 0);
    }

    private void removeNumbers(int[][] board) {
        Random rand = new Random();
        int removeCount = 40; // Adjust difficulty by changing this number

        while (removeCount > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (board[row][col] != -1) {
                board[row][col] = -1;
                removeCount--;
            }
        }
    }

    private boolean solveSudoku(int[][] board, int row, int col) {
        if (row == 9) return true;
        if (col == 9) return solveSudoku(board, row + 1, 0);
        if (board[row][col] != 0) return solveSudoku(board, row, col + 1);

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board, row, col + 1)) return true;
                board[row][col] = -1;
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
}
