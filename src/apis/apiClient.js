import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:5001/api', // Spring Boot API 주소
    timeout: 5000,
});

export default api;
