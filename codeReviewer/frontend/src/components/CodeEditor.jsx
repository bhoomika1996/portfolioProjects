import React from 'react';

const CodeEditor = ({ code, setCode, language, setLanguage }) => {
  return (
    <div className="p-4">
      <select
        value={language}
        onChange={(e) => setLanguage(e.target.value)}
        className="mb-2 p-2 border rounded"
      >
        <option value="java">Java</option>
        <option value="python">Python</option>
      </select>
      <textarea
        className="w-full h-64 p-2 border rounded"
        value={code}
        onChange={(e) => setCode(e.target.value)}
        placeholder="Paste your code here..."
      />
    </div>
  );
};

export default CodeEditor;
