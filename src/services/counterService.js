// src/services/counterService.js
import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

export const getCounter = () =>
    api.get(API.COUNTER.GET).then(res => res.data);

export const incrementCounter = () =>
    api.post(API.COUNTER.INCREMENT).then(res => res.data);

export const decrementCounter = () =>
    api.post(API.COUNTER.DECREMENT).then(res => res.data);
