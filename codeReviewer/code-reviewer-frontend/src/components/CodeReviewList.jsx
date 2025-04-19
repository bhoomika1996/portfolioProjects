import React, { useEffect, useState } from 'react';
import { getAllReviews } from '../services/api';

function CodeReviewList({ refreshTrigger }) {
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    fetchReviews();
  }, [refreshTrigger]); // Re-fetch whenever this changes

  const fetchReviews = async () => {
    try {
      const res = await getAllReviews();
      setReviews(res.data);
    } catch (err) {
      console.error('Error fetching reviews', err);
    }
  };

  return (
    <div className="review-list">
      <h2>Previous Code Reviews</h2>
      {reviews.length === 0 ? (
        <p>No reviews found.</p>
      ) : (
        <table border="1" cellPadding="10" cellSpacing="0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Language</th>
              <th>Code</th>
              <th>Issues</th>
              <th>Suggestions</th>
              <th>Difficulty</th>
              <th>User ID</th>
              <th>Created At</th>
            </tr>
          </thead>
          <tbody>
            {reviews.map((r) => (
              <tr key={r.id}>
                <td>{r.id}</td>
                <td>{r.lang}</td>
                <td><pre>{r.code}</pre></td>
                <td>
                  <ul>
                    {r.issues?.split('\n').map((issue, idx) => (
                      <li key={idx}>{issue.trim()}</li>
                    ))}
                  </ul>
                </td>
                <td>
                  <ul>
                    {r.suggestions?.split('\n').map((suggestion, idx) => (
                      <li key={idx}>{suggestion.trim()}</li>
                    ))}
                  </ul>
                </td>
                <td>{r.difficulty}</td>
                <td>{r.userId}</td>
                <td>{new Date(r.createdAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default CodeReviewList;
