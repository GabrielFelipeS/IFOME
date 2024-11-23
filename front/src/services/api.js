import axios from 'axios';


const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        'Content-Type': 'application/json',
        ...(localStorage.getItem('token') && { Authorization: `Bearer ${localStorage.getItem('token')}` }),
    },
});

export default api;