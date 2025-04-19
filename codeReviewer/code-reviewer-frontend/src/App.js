import React, { useState } from 'react';
import CodeSubmitForm from './components/CodeSubmitForm';
import CodeReviewList from './components/CodeReviewList';
import './App.css';

function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleReviewSubmitted = () => {
    setRefreshTrigger((prev) => prev + 1); // Increment to trigger refresh
  };

  return (
    <div className="App">
      <h1>AI Code Reviewer</h1>
      <CodeSubmitForm onReviewSubmitted={handleReviewSubmitted} />
      <hr />
      <CodeReviewList refreshTrigger={refreshTrigger} />
    </div>
  );
}

export default App;
