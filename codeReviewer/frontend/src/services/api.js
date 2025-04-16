import axios from 'axios';

const API_BASE = '/api/code-review';

export const submitCode = async (data) => {
  const res = await axios.post(API_BASE, data);
  return res.data;
};
