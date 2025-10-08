import axios from 'axios';

// Creates a reusable axios instance for API calls
const API = axios.create({
  baseURL: 'http://localhost:8080/api', // Change if backend URL differs
});

// Attaches JWT to each request, if available
API.interceptors.request.use((req) => {
  const token = localStorage.getItem('token');
  if (token) {
    req.headers.Authorization = `Bearer ${token}`;
  }
  return req;
});

export default API;
