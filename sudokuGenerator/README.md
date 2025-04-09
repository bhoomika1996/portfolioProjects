# Sudoku Puzzle Generator & Solver Web App

## Overview
This project is a full-stack web application built to generate Sudoku puzzles, allow users to solve them interactively, and provide instant validation or auto-solving options. It includes a Java Spring Boot backend to handle puzzle generation and a ReactJS frontend that delivers an engaging, interactive Sudoku playing experience.

---

## 🔧 Tech Stack
- **Backend**: Java, Spring Boot
- **Frontend**: ReactJS, JavaScript, HTML, CSS
- **Build Tools**: Maven
- **API Testing**: Postman (for local testing)

---

## 🎮 Game Features
- 🧩 **Puzzle Generation**: Random puzzles based on difficulty levels - Easy, Medium, Hard
- ⏱️ **Timer**: Real-time tracking of how long the user takes to solve the puzzle
- 🧠 **Manual Solving**: Users can enter values into the grid and solve it manually
- 🧪 **Validation**: A "Check" button lets users validate their progress
- 🧙 **Auto Solve**: Solver fills in the correct solution based on initial puzzle
- 🔁 **Reset Button**: Reset puzzle back to original unsolved state
- 🆕 **New Puzzle Button**: Loads a new puzzle and restarts the timer
- 🎉 **Congratulatory Animation**: When the puzzle is solved correctly
- 🎮 **Disable Inputs**: Prevent editing of pre-filled cells
- ❌ **Disable Buttons on Solve**: Ensures puzzle integrity after solving

---

## 📋 Functional Requirements
1. **API Endpoint for Puzzle Generation**
   - Endpoint: `/api/sudoku/generate`
   - Input: `difficulty` (easy/medium/hard)
   - Output: 9x9 puzzle grid with `-1` for empty cells

2. **User Interaction**
   - Editable cells where users can input numbers (1-9)
   - Check, Solve, Reset, New buttons for interaction
   - Timer starts when puzzle loads and stops when solved

3. **Puzzle Solver Logic**
   - Backtracking algorithm on frontend to solve puzzle
   - Checks for valid placements (row, column, 3x3 box)

---

## 📦 Backend Logic (Java - Spring Boot)

### `SudokuController.java`
- `@GetMapping("/api/sudoku/generate")`
  - Accepts `difficulty` as a query parameter
  - Returns a new Sudoku puzzle (as a 2D array of integers with -1 as blanks)

### `SudokuGenerator.java`
- `generateSudoku(String difficulty)`
  - High-level method to create a solvable Sudoku puzzle of given difficulty
  - Calls `fillGrid()` and `removeCells()` internally

- `fillGrid()`
  - Recursive backtracking algorithm to create a fully filled valid Sudoku grid
  - Randomly shuffles numbers to increase variability

- `isValid(int[][] grid, int row, int col, int num)`
  - Checks if placing `num` at `(row, col)` obeys Sudoku rules (no repeat in row, column, or box)

- `removeCells(String difficulty)`
  - Removes a specific number of cells based on difficulty:
    - Easy: fewer cells removed
    - Hard: more cells removed
  - Ensures puzzle remains solvable (removal count predefined)

---

## 🎨 Frontend Logic (ReactJS)

### State Variables
- `sudokuArr`: Current editable Sudoku grid
- `initial`: Original puzzle grid (used to disable inputs)
- `elapsedTime`, `startTime`, `timerRef`: For timer tracking
- `difficulty`: User-selected difficulty (easy/medium/hard)
- `isSolved`: Whether puzzle is already solved (used to control UI state)

### Key Methods

#### `fetchSudoku()`
- Fetches puzzle from backend based on selected difficulty
- Resets timer and updates UI with new puzzle

#### `startTimer()` / `stopTimer()`
- Handles interval-based time tracking while solving
- `startTimer()` begins interval
- `stopTimer()` clears the interval

#### `onInputChange(e, row, col)`
- Validates user input and updates Sudoku grid
- Ensures input is either empty or number between 1-9

#### `checkSudoku()`
- Uses internal solver to solve initial puzzle and compares with user's grid
- Triggers congratulatory message and animation if solved correctly
- Encourages if still solvable, warns if unsolvable

#### `solveSudoku()`
- Automatically solves puzzle using backtracking
- Disables all buttons except "New Sudoku"
- Restarts timer

#### `resetSudoku()`
- Resets the board to the original puzzle (restores `initial` grid)

#### `getDeepCopy(arr)`
- Deep copies a 2D array to avoid mutation side-effects in state updates

#### Solver Helpers (Backtracking Based)
- `solver(grid, row, col)`
  - Recursive function to solve the puzzle
  - Tries placing numbers from 1–9, backtracks on failure
- `checkValid()`
  - Checks row, column, and box for legality
- `checkRow()`, `checkCol()`, `checkBox()`
  - Individual helper functions used in validation logic
- `getNext()`
  - Finds the next cell to proceed during solving

---

## 💡 Challenges Solved
- 🧠 Dynamic difficulty puzzle generation
- 🧪 Accurate real-time puzzle validation
- 🎯 Ensuring inputs cannot be changed for original clues
- 🔄 Timer handling for game consistency
- 🎉 Animations and smooth UI flow after solving

---

## 📸 Screenshots / Demo

![Screenshot 2025-04-09 at 2 51 48 PM](https://github.com/user-attachments/assets/c7a19f74-d8d9-400c-9a58-5f2ac13b5e05)

---

## 🚀 Future Enhancements
- Add leaderboard with top solving times
- Save and resume previous puzzles
- Mobile responsive design
- Dark mode theme

---

## 🧩 Run the Project
### Backend
```bash
cd src/main/java/com/example/sudokuGenerator
mvn spring-boot:run
```

### Frontend
```bash
cd app
npm install
npm start
```

---

## 🤝 Contributing
Contributions welcome! Feel free to fork the repo and submit PRs.

---

## 👩‍💻 Author
**Bhoomika** - Independent Software Developer passionate about solving problems through code.

---

## 📎 Links
- GitHub: [sudokuGenerator](https://github.com/bhoomika1996/portfolioProjects/tree/main/sudokuGenerator)

