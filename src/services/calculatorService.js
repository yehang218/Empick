// src/services/calculatorService.js
import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

export const add = async (a, b) => {
    try {
        const res = await api.post(API.CALCULATOR.ADD, { a, b });
        console.log('[POST] /calculator/add - 성공:', res.data);
        return res.data;
    } catch (error) {
        console.error('[POST] /calculator/add - 실패:', error);
        throw error;
    }
};

export const subtract = async (a, b) => {
    try {
        const res = await api.post(API.CALCULATOR.SUB, { a, b });
        console.log('[POST] /calculator/sub - 성공:', res.data);
        return res.data;
    } catch (error) {
        console.error('[POST] /calculator/sub - 실패:', error);
        throw error;
    }
};
