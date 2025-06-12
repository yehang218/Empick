import api from '@/apis/apiClient';
import { API } from '@/apis/routes';

/**
 * 파일 업로드 서비스
 * @param {File} file - 업로드할 파일 객체
 * @param {string} prefix - S3 저장 경로 prefix (예: 'profiles/')
 * @param {string} fileName - 저장될 파일명 (예: 'profile.png')
 * @returns {Promise<Object>} 업로드 결과 (url 등)
 */
export const uploadFileService = async (file, prefix = '', fileName = 'profile.png') => {
    const formData = new FormData();
    formData.append('file', file);
    if (prefix) formData.append('prefix', prefix);
    if (fileName) formData.append('fileName', fileName);

    try {
        const response = await api.post(API.FILE.UPLOAD, formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        return response.data;
    } catch (error) {
        if (error.response) {
            throw new Error(error.response.data?.message || '파일 업로드 중 오류가 발생했습니다.');
        }
        throw error;
    }
}; 