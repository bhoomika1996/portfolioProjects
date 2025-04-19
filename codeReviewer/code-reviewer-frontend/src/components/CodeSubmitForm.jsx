import React, { useState } from 'react';
import { submitCodeSnippet } from '../services/api';

function CodeSubmitForm({ onReviewSubmitted }) {
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('');
  const [loading, setLoading] = useState(false);
  const [reviewResult, setReviewResult] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await submitCodeSnippet({ code, language });
      setReviewResult(res.data);
      setCode('');
      setLanguage('');
      if (onReviewSubmitted) onReviewSubmitted();
    } catch (err) {
      console.error('Review failed', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      <h2>Submit Code for Review</h2>
      {/* <form onSubmit={handleSubmit}>
        <label>Language:</label>
        <input
          type="text"
          placeholder="e.g., Java, Python"
          value={language}
          onChange={(e) => setLanguage(e.target.value)}
          required
        />
        <br /><br />

        <label>Code:</label>
        <textarea
          rows="10"
          cols="70"
          placeholder="Paste your code here..."
          value={code}
          onChange={(e) => setCode(e.target.value)}
          required
        ></textarea>
        <br />
        <button type="submit" disabled={loading}>
          {loading ? 'Reviewing...' : 'Submit'}
        </button>
      </form> */}
      <form onSubmit={handleSubmit}>
        <div>
            <label><strong>Language:</strong></label><br />
            <input
            type="text"
            placeholder="e.g., Java, Python"
            value={language}
            onChange={(e) => setLanguage(e.target.value)}
            required
            />
        </div>

        <div>
            <label><strong>Code:</strong></label><br />
            <textarea
            rows="10"
            placeholder="Paste your code here..."
            value={code}
            onChange={(e) => setCode(e.target.value)}
            required
            ></textarea>
        </div>

        <button type="submit" disabled={loading}>
            {loading ? 'Reviewing...' : 'Submit for Review'}
        </button>
      </form>

      {reviewResult && (
        <div className="review-result">
          <h3>Review Result:</h3>
          <strong>Difficulty:</strong> {reviewResult.difficulty}
          <br />
          <strong>Issues:</strong>
          <ul>
            {reviewResult.issues.map((issue, idx) => (
              <li key={idx}>{issue}</li>
            ))}
          </ul>
          <strong>Suggestions:</strong>
          <ul>
            {reviewResult.suggestions.map((sugg, idx) => (
              <li key={idx}>{sugg}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default CodeSubmitForm;
