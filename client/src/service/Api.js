import axios from "axios";
import { refresh } from "./AuthService.js";

const API_URL = "http://localhost:8080/api/v1.0";

const api = axios.create({
    baseURL: API_URL,
    withCredentials: true
});

const refreshAccessToken = async () => {
    try {
        const response = await refresh();
        return response.data.token;
    } catch (error) {
        console.error("Could not refresh token", error);
        return null;
    }
};

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            const newAccessToken = await refreshAccessToken();

            if (newAccessToken) {
                localStorage.setItem("token", newAccessToken);
                api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
                originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

                return api(originalRequest);
            } else {
                localStorage.removeItem("token");
                localStorage.removeItem("role");
                window.location.href = '/login';
            }
        }

        return Promise.reject(error);
    }
);

export default api;