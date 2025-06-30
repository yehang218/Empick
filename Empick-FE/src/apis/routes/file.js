import api from '@/apis/apiClient'

export const FileAPI = {
    UPLOAD: '/api/v1/files/upload',
    GET_FILE_URL: (fileId) => `/api/v1/files/${fileId}`,
    DELETE_FILE: (fileId) => `/api/v1/files/${fileId}`,
    
    // S3 파일 업로드 (백엔드 API 스펙에 맞춤)
    uploadFile: async (file, prefix = '', fileName = '') => {
        const formData = new FormData();
        formData.append('file', file);
        
        // URL에 query parameter로 prefix와 fileName 전달
        const params = new URLSearchParams();
        if (prefix) params.append('prefix', prefix);
        if (fileName) params.append('fileName', fileName);
        
        const url = `${FileAPI.UPLOAD}?${params.toString()}`;
        
        return await api.post(url, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    },
    
    // 파일 URL 조회
    getFileUrl: async (fileId) => {
        return await api.get(FileAPI.GET_FILE_URL(fileId))
    },
    
    // 파일 삭제
    deleteFile: async (fileId) => {
        return await api.delete(FileAPI.DELETE_FILE(fileId))
    }
}; 