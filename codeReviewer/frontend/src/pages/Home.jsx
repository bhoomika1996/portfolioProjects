import React, { useState } from 'react';
import CodeEditor from '../components/CodeEditor';
import ReviewResults from '../components/ReviewResults';
import { submitCode } from '../services/api';

const Home = () => {
  const [code, setCode] = useState('');
  const [language, setLanguage] = useState('java');
  const [result, setResult] = useState(null);

  const handleSubmit = async () => {
    const response = await submitCode({ language, code });
    setResult(response);
  };

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold text-center mt-6">AI Code Reviewer</h1>
      <CodeEditor code={code} setCode={setCode} language={language} setLanguage={setLanguage} />
      <button onClick={handleSubmit} className="bg-blue-600 text-white px-4 py-2 rounded mt-4">Submit</button>
      <ReviewResults result={result} />
    </div>
  );
};

export default Home;
