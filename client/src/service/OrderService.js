import api from './Api.js';

export const allOrders = async () => {
    return await api.get("/admin/orders", {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const userOrders = async () => {
    return await api.get("/orders/my-orders", {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const createOrder = async (order) => {
    return await api.post("/orders", order, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const deleteOrder = async (orderId) => {
    return await api.delete(`/orders/${orderId}`, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const getOrderById = async (orderId) => {
    return await api.get(`/orders/${orderId}`, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}