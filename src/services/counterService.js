// src/services/counterService.js
import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

export const getCounter = async () => {
    console.log('[GET] /counter - 요청 시작');
    try {
        const res = await api.get(API.COUNTER.GET);
        console.log('[GET] /counter - 성공:', res.data);
        return res.data;
    } catch (error) {
        console.error('[GET] /counter - 실패:', error);
        throw error;
    }
};

export const incrementCounter = async () => {
    console.log('[POST] /counter/increment - 요청 시작');
    try {
        const res = await api.post(API.COUNTER.INCREMENT);
        console.log('[POST] /counter/increment - 성공:', res.data);
        return res.data;
    } catch (error) {
        console.error('[POST] /counter/increment - 실패:', error);
        throw error;
    }
};

export const decrementCounter = async () => {
    console.log('[POST] /counter/decrement - 요청 시작');
    try {
        const res = await api.post(API.COUNTER.DECREMENT);
        console.log('[POST] /counter/decrement - 성공:', res.data);
        return res.data;
    } catch (error) {
        console.error('[POST] /counter/decrement - 실패:', error);
        throw error;
    }
};
