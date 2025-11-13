import api from './Api.js';

export const login = async (data) => {
    return await api.post('/login', data);
}

export const logout = async () => {
    return await api.post('/logout');
}

export const refresh = async () => {
    return await api.post('/refresh');
}