import axios from '../config/axios';

export const getUser = (id) => axios.get(`/users/${id}`);
export const updateUser = (id, data) => axios.put(`/users/${id}`, data);