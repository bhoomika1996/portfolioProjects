package com.example.sudokuGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
public class SudokuGeneratorController {

    @Autowired
    private SudokuService sudokuService;

    @GetMapping("/generate")
    public ResponseEntity<int[][]> generatePuzzle(@RequestParam(defaultValue = "easy") String difficulty) {
        int[][] puzzle = sudokuService.generatePuzzle(difficulty);
        return ResponseEntity.ok(puzzle);
    }
}


