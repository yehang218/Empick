import api from '@/apis/apiClient';
import { API } from '@/apis/routes';
import LoginResponseDTO from '@/dto/LoginResponseDTO';

export const loginService = async (loginRequest) => {
    const response = await api.post(API.AUTH.LOGIN, loginRequest);
    return new LoginResponseDTO(response.data.accessToken, response.data.refreshToken);
};