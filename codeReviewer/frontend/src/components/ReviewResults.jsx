import React from 'react';

const ReviewResults = ({ result }) => {
  if (!result) return null;

  return (
    <div className="p-4 border-t mt-4">
      <h2 className="text-lg font-bold">Suggestions</h2>
      <ul className="list-disc pl-5">
        {result.suggestions.map((s, i) => <li key={i}>{s}</li>)}
      </ul>
      <h2 className="text-lg font-bold mt-4">Issues</h2>
      <ul className="list-disc pl-5">
        {result.issues.map((i, idx) => <li key={idx}>{i}</li>)}
      </ul>
      <p className="mt-4 font-semibold">Difficulty: {result.difficulty}</p>
    </div>
  );
};

export default ReviewResults;
