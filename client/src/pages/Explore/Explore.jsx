import "./Explore.css";
import {useContext, useEffect, useState} from "react";
import {AppContext} from "../../context/AppContext.jsx";
import { useSearchParams } from 'react-router-dom';
import DisplayCategories from "../../components/DisplayCategories/DisplayCategories.jsx";
import DisplayItems from "../../components/DisplayItems/DisplayItems.jsx";
import CustomerForm from "../../components/CustomerForm/CustomerForm.jsx";
import CartItems from "../../components/CartItems/CartItems.jsx";
import CartSummary from "../../components/CartSummary/CartSummary.jsx";
import ReceiptPopup from "../../components/ReceiptPopup/ReceiptPopup.jsx";
import toast from "react-hot-toast";
import { getOrderById } from "../../service/OrderService.js";

const Explore = () => {
    const {categories, clearCart, isExploreRendered, setIsExploreRendered} = useContext(AppContext);
    const [selectedCategory, setSelectedCategory] = useState("");
    const [customerName, setCustomerName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [searchParams, setSearchParams] = useSearchParams();

    const [showReceiptPopup, setShowReceiptPopup] = useState(false);
    const [orderDetails, setOrderDetails] = useState(null);

    useEffect(() => {
        if (!isExploreRendered) {
            return;
        }

        const paymentStatus = sessionStorage.getItem('paymentStatus');
        const urlPaymentStatus = searchParams.get("payment");

        if (urlPaymentStatus) {
            setSearchParams({}, { replace: true });
        }

        if (paymentStatus === 'success') {
            const pendingOrderId = sessionStorage.getItem('pendingOrderId');

            if (pendingOrderId) {
                getOrderById(pendingOrderId)
                    .then(response => {
                        if (response.status === 200) {
                            setOrderDetails(response.data);
                            setShowReceiptPopup(true);
                            sessionStorage.removeItem('pendingOrderId');
                            sessionStorage.removeItem('paymentStatus');
                        }
                    })
                    .catch(error => {
                        console.error('Error fetching order details:', error);
                        toast.error('Could not fetch order details');
                    });
            }
        } else if (paymentStatus === 'cancelled') {
            toast.error('Error while completing payment!');

            sessionStorage.removeItem('pendingOrderId');
            sessionStorage.removeItem('paymentStatus');
        }
    }, [isExploreRendered, searchParams, setSearchParams]);

    const handleCloseReceipt = () => {
        setShowReceiptPopup(false);
        setOrderDetails(null);
        setCustomerName("");
        setPhoneNumber("");
        setIsExploreRendered(false);
        clearCart();
    };

    const handlePrintReceipt = () => {
        window.print();
    };

    return (
        <div className="explore-container text-light">
            <div className="left-column">
                <div className="first-row">
                    <DisplayCategories
                        categories={categories}
                        selectedCategory={selectedCategory}
                        setSelectedCategory={setSelectedCategory}
                    />
                </div>
                <hr className="horizontal-line" />
                <div className="second-row">
                    <DisplayItems selectedCategory={selectedCategory} />
                </div>
            </div>
            <div className="right-column d-flex flex-column">
                <div className="customer-form-container" style={{height: '15%'}}>
                    <CustomerForm
                        customerName={customerName}
                        setCustomerName={setCustomerName}
                        phoneNumber={phoneNumber}
                        setPhoneNumber={setPhoneNumber}
                    />
                </div>
                <hr className="my-3 text-light border-3" />
                <div className="cart-items-container" style={{height: '55%', overflowY: 'auto'}}>
                    <CartItems />
                </div>
                <hr className="my-3 text-light border-3" />
                <div className="cart-summary-container" style={{height: '30%'}}>
                    <CartSummary
                        customerName={customerName}
                        setCustomerName={setCustomerName}
                        phoneNumber={phoneNumber}
                        setPhoneNumber={setPhoneNumber}
                    />
                </div>
            </div>

            {showReceiptPopup && orderDetails && (
                <ReceiptPopup
                    orderDetails={{
                        ...orderDetails,
                        paymentTransactionId: orderDetails.paymentDetails?.paymentTransactionId,
                        status: orderDetails.paymentDetails?.status,
                    }}
                    onClose={handleCloseReceipt}
                    onPrint={handlePrintReceipt}
                />
            )}
        </div>
    )
}

export default Explore;