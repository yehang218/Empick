import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

export const add = (a, b) =>
    api.post(API.CALCULATOR.ADD, { a, b }).then(res => res.data);

export const subtract = (a, b) =>
    api.post(API.CALCULATOR.SUB, { a, b }).then(res => res.data);
