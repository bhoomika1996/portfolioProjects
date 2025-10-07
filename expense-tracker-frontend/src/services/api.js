import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// Add JWT to requests
API.interceptors.request.use((req) => {
  const token = localStorage.getItem('token');
  if (token) {
    req.headers.Authorization = `Bearer ${token}`;
  }
  return req;
});

export default API;
