import axios from '../config/axios';

export const fetchSomthing = (params) => {
    return axios.get('/api/v1/auth', { params })
}