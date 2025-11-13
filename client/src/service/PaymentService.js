import axios from "axios";

// ZMIEŃ NAZWĘ z 'createRazorpayOrder' na 'createCheckoutSession'
export const createCheckoutSession = async (data) => {
    // ZMIEŃ ENDPOINT z '/createOrder' na '/create-checkout-session'
    return await axios.post('http://localhost:8080/api/v1.0/payments/create-checkout-session', data, {headers: {'Authorization': "Bearer " + localStorage.getItem("token")}});
}