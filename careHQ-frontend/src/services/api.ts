import axios from 'axios';

const API = axios.create({
  baseURL: 'http://192.168.1.100:8080/api',
});

export const registerUser = (email: string, password: string, name: string) =>
  API.post('/auth/register', { email, password, name });

export const loginUser = (email: string, password: string) =>
  API.post('/auth/login', { email, password });
