import api from './Api.js';

export const createCheckoutSession = async (data) => {
    return await api.post('/payments/create-checkout-session', data, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}