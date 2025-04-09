import logo from './logo.svg';
import './App.css';
import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Confetti from 'react-confetti';
import { useWindowSize } from '@react-hook/window-size';

// let initial=[
//   [-1, 5, -1, 9, -1, -1, -1, -1, -1],
//   [8, -1, -1, -1, 4, -1, 3, -1, 7],
//   [-1, -1, -1, 2, 8, -1, 1, 9, -1],
//   [5, 3, 8, 6, -1, 7, 9, 4, -1],
//   [-1, 2, -1, 3, -1, 1, -1, -1, -1],
//   [1, -1, 9, 8, -1, 4, 6, 2, 3],
//   [9, -1, 7, 4, -1, -1, -1, -1, -1],
//   [-1, 4, 5, -1, -1, -1, 2, -1, 9],
//   [-1, -1, -1, -1, 3, -1, -1, 7, -1]
// ]
function App() {
  const [sudokuArr, setSudokuArr] = useState([]);
  const [initial,setInitial] = useState([]);
  const [difficulty, setDifficulty] = useState("easy");
  const [startTime, setStartTime] = useState(null);
  const [elapsedTime, setElapsedTime] = useState(0);
  //const [timerInterval, setTimerInterval] = useState(null);
  const timerRef = useRef(null);
  const [isSolved, setIsSolved] = useState(false);
  const [showConfetti, setShowConfetti] = useState(false);
  const [width, height] = useWindowSize();

  useEffect(() => {
    fetchSudoku();
  }, []);

  async function fetchSudoku() {
    try {
      const response = await axios.get("/api/sudoku/generate", {
        params: { difficulty }
      });
      console.log("Fetched Sudoku:", response.data);
      if (response.data && response.data.length === 9) {
        const newSudoku = getDeepCopy(response.data);
        setSudokuArr(newSudoku);
        setInitial(getDeepCopy(newSudoku));

        stopTimer();     // 🛑 Stop any previous timer
        setElapsedTime(0);
        startTimer();    // ▶️ Start fresh timer
      } else {
        console.error("Invalid Sudoku Data:", response.data);
      }
    } catch (error) {
      console.error("Error fetching Sudoku:", error);
    }
  } 

  function startTimer() {
    stopTimer(); // Ensure no duplicate intervals
    const start = Date.now();
    setStartTime(start);
    timerRef.current = setInterval(() => {
      setElapsedTime(Math.floor((Date.now() - start) / 1000));
    }, 1000);
  }
  
  function stopTimer() {
    if (timerRef.current) {
      clearInterval(timerRef.current);
      timerRef.current = null;
    }
  }  

 function newSudoku(){
    setIsSolved(false); // Enable all buttons
    setShowConfetti(false); // 🎈 Hide confetti
    fetchSudoku();
  }
  
  
  function getDeepCopy(arr){
    return JSON.parse(JSON.stringify(arr));
  }

  function onInputChange(e, row, col){
    var val = parseInt(e.target.value) || -1, grid = getDeepCopy(sudokuArr);
    if(val===-1 || val>=1 && val<=9){
      grid[row][col] = val;
    }
    setSudokuArr(grid);
  }

  function compareSudokus(currSudoku, solvedSudoku){
    let res = {
      isComplete: true,
      isSolvable: true
    }
    for(var i=0;i<9;i++){
      for(var j=0;j<9;j++){
        if(currSudoku[i][j] != solvedSudoku[i][j]){
          if(currSudoku[i][j] != -1){
            res.isSolvable = false;
          }
          res.isComplete = false;
        }
      }
    }
    return res;
  }

  function checkSudoku(){
    let sudoku = getDeepCopy(initial);
    solver(sudoku);
    let compare = compareSudokus(sudokuArr,sudoku);
    console.log(sudokuArr);
    console.log(sudoku);

    if(compare.isComplete){
      stopTimer();
      setShowConfetti(true); // 🎉 Show animation
      alert(`🎉 Congratulations! You solved it in ${formatTime(elapsedTime)}.`);
      setIsSolved(true); // disable other buttons

    } else if(compare.isSolvable){
      alert("Keep Going!");
    }else{
      alert("Sudoku can't be solved, try again!")
    }
  }
  
  function formatTime(seconds) {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  }
  

  function checkBox(grid, row, col, num){
    let boxArr = [],
    rowStart = row-(row%3),
    colStart = col - (col%3);
    for(let i=0;i<3;i++){
      for(let j=0;j<3;j++){
        boxArr.push(grid[rowStart+i][colStart+j]);
      }
    }
    return boxArr.indexOf(num) === -1;
  }

  function checkCol(grid, col, num){
    return grid.map(row=> row[col]).indexOf(num) === -1;
  }

  function checkRow(grid, row, num){
    return grid[row].indexOf(num) === -1;
  }

  function checkValid(grid, row, col, num){
    if(checkRow(grid, row, num) && checkCol(grid, col, num) && checkBox(grid, row, col, num)){
      return true;
    }
    return false;
  }

  function getNext(row, col){
    return col !== 8 ? [row, col+1] : row !==8 ? [row+1,0]: [0,0];
  }

  function solver(grid, row=0, col=0){

    if(grid[row][col] !== -1){
      let isLast = row>=8 && col>=8;
      if (isLast) return true;
      if(!isLast){
        let [newRow, newCol] = getNext(row, col);
        return solver(grid, newRow, newCol);
      }
    }

    for(let num=1;num<=9;num++){
      if(checkValid(grid, row, col, num)){
        grid[row][col] = num;
        let [newRow, newCol] = getNext(row, col);
        if(!newRow && !newCol){
          return true;
        }

        if(solver(grid, newRow, newCol)){
          return true;
        }
      }
    }
    grid[row][col] = -1;
    return false;
  }

  function solveSudoku() {
    let sudoku = getDeepCopy(initial);
    solver(sudoku);
    setSudokuArr(sudoku);
    console.log(sudokuArr);
    stopTimer();
    setIsSolved(true); // Disable buttons except New Sudoku
  }
  
  function resetSudoku(){
    let sudoku = getDeepCopy(initial);
    setSudokuArr(sudoku);
    console.log(sudokuArr);

  }
  useEffect(() => {
    return () => {
      stopTimer();
    };
  }, []);
  
  return (
    <div className="App">
      <div className="App-header">

      {showConfetti && <Confetti width={width} height={height} />}

        <h3> 🧩 Sudoku Solver </h3>
        <p className="time-elapsed">🕒 Time Elapsed: <strong>{formatTime(elapsedTime)}</strong></p>

        {/* Difficulty Selector */}
        <div className='difficulty-select'>
          <label style={{ marginRight: "10px" }}>Select Difficulty:</label>
          <select
            value={difficulty}
            onChange={(e) => setDifficulty(e.target.value)}
          >
            <option value="easy">Easy</option>
            <option value="medium">Medium</option>
            <option value="hard">Hard</option>
          </select>
        </div>

        <table>
          <tbody>
            {
            sudokuArr.length > 0 && sudokuArr.map((row, rIndex) => (
              <tr key={rIndex} className={((rIndex + 1) % 3 === 0) ? 'bBorder' : ''}>
                {row.map((col, cIndex) => (
                  <td key={rIndex + "-" + cIndex} className={((cIndex + 1) % 3 === 0) ? 'rBorder' : ''}>
                    <input
                      onChange={(e) => onInputChange(e, rIndex, cIndex)}
                      value={sudokuArr[rIndex][cIndex] === -1 ? '' : sudokuArr[rIndex][cIndex]}
                      className='cellInput'
                      disabled={initial.length > 0 && initial[rIndex][cIndex] !== -1}
                    />
                  </td>
                ))}
              </tr>
            ))}
            
          </tbody>
        </table>
        <div className='buttonContainer'>
          <button className='checkButton' onClick={checkSudoku} disabled={isSolved}>Check</button>
          <button className='solveButton' onClick={solveSudoku} disabled={isSolved}>Solve</button>
          <button className='resetButton' onClick={resetSudoku} disabled={isSolved}>Reset</button>
          <button className='newButton' onClick={newSudoku}>New Sudoku</button>
        </div>
      </div>
    </div>
  );
  
}

export default App;
