import axios from 'axios';

const token = localStorage.getItem('token');
const headers = {
    'Content-Type': 'application/json',
    ...(token && { Authorization: `Bearer ${token}` }),
}

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: headers,
});

export default api;