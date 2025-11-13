import './CartSummary.css';
import {useContext, useState} from "react";
import {AppContext} from "../../context/AppContext.jsx";
import {createOrder, deleteOrder} from "../../service/OrderService.js";
import toast from "react-hot-toast";
import {createCheckoutSession} from "../../service/PaymentService.js";
import ReceiptPopup from "../ReceiptPopup/ReceiptPopup.jsx";

const CartSummary = ({customerName, setCustomerName, phoneNumber, setPhoneNumber}) => {
    const {cartItems, clearCart} = useContext(AppContext);

    const [isProcessing, setIsProcessing] = useState(false);

    const [orderDetails, setOrderDetails] = useState(null);

    const [showPopup, setShowPopup] = useState(false);

    const totalAmount = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

    const tax = totalAmount * 0.01;

    const grandTotal = totalAmount + tax;

    const clearAll = () => {
        setCustomerName("");
        setPhoneNumber("");
        clearCart();
    }

    const placeOrder = () => {
        setShowPopup(true);
        clearAll();
    }

    const handlePrintReceipt = () => {
        window.print();
    }

    const completePayment = async (paymentMode) => {
        if (!customerName || !phoneNumber) {
            toast.error("Please enter customer details!");
            return;
        }

        if (cartItems.length === 0) {
            toast.error("Your cart is empty!");
            return;
        }

        setIsProcessing(true);
        const orderData = {
            customerName,
            phoneNumber,
            cartItems,
            subtotal: totalAmount,
            tax,
            grandTotal,
            paymentMethod: paymentMode.toUpperCase()
        }

        try {
            const response = await createOrder(orderData);
            const savedData = response.data;

            if (response.status !== 201) {
                toast.error("Payment processing failed: " + response.status);
                return;
            }

            if (paymentMode === "cash") {
                toast.success("Cash received!");
                setOrderDetails(savedData);
            }
            else if (paymentMode === "card") {
                const checkoutData = {
                    ...orderData,
                    orderId: savedData.orderId
                };

                const stripeResponse = await createCheckoutSession(checkoutData);

                if (stripeResponse.data && stripeResponse.data.url) {
                    window.location.href = stripeResponse.data.url;
                } else {
                    toast.error("Could not initiate Stripe payment.");
                    await deleteOrder(savedData.orderId);
                }
            }
        } catch (error) {
            console.error(error);
            toast.error("Payment processing failed: " + error.message);
        } finally {
            setIsProcessing(false);
        }
    }

    return (
        <div className="mt-2">
            <div className="cart-summary-details">
                <div className="d-flex justify-content-between mb-2">
                    <span className="text-light">Items: </span>
                    <span className="text-light">&#36;{totalAmount.toFixed(2)}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                    <span className="text-light">Tax (1%):</span>
                    <span className="text-light">&#36;{tax.toFixed(2)}</span>
                </div>
                <hr/>
                <div className="d-flex justify-content-between mb-2">
                    <h4 className="text-light d-inline">Total:</h4>
                    <span className="text-light">&#36;{grandTotal.toFixed(2)}</span>
                </div>
            </div>
            <div className="d-flex gap-3">
                <button className="btn btn-success flex-grow-1"
                        onClick={() => completePayment("cash")}
                        disabled={isProcessing}
                >
                    {isProcessing ? "Processing.." : "Cash"}
                </button>
                <button className="btn btn-primary flex-grow-1"
                        onClick={() => completePayment("card")}
                        disabled={isProcessing}
                >
                    {isProcessing ? "Processing.." : "Card"}
                </button>
            </div>
            <div className="d-flex gap-3 mt-3">
                <button className="btn btn-warning flex-grow-1"
                        onClick={placeOrder}
                        disabled={isProcessing || !orderDetails}
                >
                    Place Order
                </button>
            </div>
            {
                showPopup && (
                    <ReceiptPopup
                        orderDetails={{
                            ...orderDetails,
                            paymentTransactionId: orderDetails.paymentDetails?.paymentTransactionId,
                            status: orderDetails.paymentDetails?.status,
                        }}
                        onClose={() => setShowPopup(false)}
                        onPrint={handlePrintReceipt}
                    />
                )
            }
        </div>
    )
}

export default CartSummary;