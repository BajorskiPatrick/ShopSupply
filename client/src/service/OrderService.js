import axios from "axios";

export const allOrders = async () => {
    return await axios.get("http://localhost:8080/api/v1.0/admin/orders", {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const userOrders = async () => {
    return await axios.get("http://localhost:8080/api/v1.0/orders/my-orders", {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const createOrder = async (order) => {
    return await axios.post("http://localhost:8080/api/v1.0/orders", order, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const deleteOrder = async (orderId) => {
    return await axios.delete(`http://localhost:8080/api/v1.0/orders/${orderId}`, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}

export const getOrderById = async (orderId) => {
    return await axios.get(`http://localhost:8080/api/v1.0/orders/${orderId}`, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}