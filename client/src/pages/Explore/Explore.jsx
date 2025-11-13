import "./Explore.css";
import {useContext, useEffect, useState} from "react";
import {AppContext} from "../../context/AppContext.jsx";
import { useSearchParams } from 'react-router-dom';
import DisplayCategories from "../../components/DisplayCategories/DisplayCategories.jsx";
import DisplayItems from "../../components/DisplayItems/DisplayItems.jsx";
import CustomerForm from "../../components/CustomerForm/CustomerForm.jsx";
import CartItems from "../../components/CartItems/CartItems.jsx";
import CartSummary from "../../components/CartSummary/CartSummary.jsx";
import toast from "react-hot-toast";

const Explore = () => {
    const {categories} = useContext(AppContext);
    const [selectedCategory, setSelectedCategory] = useState("");
    const [customerName, setCustomerName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [searchParams, setSearchParams] = useSearchParams();

    useEffect(() => {
        const paymentStatus = searchParams.get("payment");

        if (paymentStatus === 'success') {
            toast.success('Payment completed successfully!');

            setSearchParams({}, { replace: true });
        } else if (paymentStatus === 'cancelled') {
            toast.error('Error while completing payment!');

            setSearchParams({}, { replace: true });
        }
    }, [searchParams, setSearchParams]);

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
        </div>
    )
}

export default Explore;