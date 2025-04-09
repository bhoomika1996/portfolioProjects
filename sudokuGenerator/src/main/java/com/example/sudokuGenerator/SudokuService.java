package com.example.sudokuGenerator;

import org.springframework.stereotype.Service;

@Service
public class SudokuService {

    public int[][] generatePuzzle(String difficulty) {
        int clues;
        switch (difficulty.toLowerCase()) {
            case "easy":
                clues = 36;
                break;
            case "medium":
                clues = 32;
                break;
            case "hard":
                clues = 28;
                break;
            default:
                clues = 36;
        }

        SudokuGenerator generator = new SudokuGenerator();
        return generator.generate(clues);
    }
}

