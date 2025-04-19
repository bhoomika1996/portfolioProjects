import axios from 'axios';

const API_BASE_URL = '/api'; // Update if needed


export const submitCodeSnippet = ({ code, language }) => {
    return axios.post(`${API_BASE_URL}/code-review`, { code, language });
  };

export const getAllReviews = () => {
  return axios.get(`${API_BASE_URL}/code-snippet-records`);
};
