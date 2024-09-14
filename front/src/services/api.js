import axios from 'axios';

const api = axios.create({
    baseURL: process.env.DEFAULT_API_URL,
    headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('token')
    }
});

export default api;