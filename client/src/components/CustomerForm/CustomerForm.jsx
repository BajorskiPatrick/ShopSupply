import './CustomerForm.css';

const CustomerForm = ({customerName, setCustomerName, phoneNumber, setPhoneNumber}) => {
    return (
        <div className="p-3">
            <div className="mb-3">
                <div className="d-flex align-items-center gap-2">
                    <label htmlFor="customerName" className="col-4">Customer name</label>
                    <input
                        type="text"
                        name="customerName"
                        id="customerName"
                        className="form-control-sm"
                        placeholder="Customer Name"
                        onChange={(e) => setCustomerName(e.target.value)}
                        value={customerName}
                        required={true}
                    />
                </div>
            </div>
            <div className="mb-3">
                <div className="d-flex align-items-center gap-2">
                    <label htmlFor="phoneNumber" className="col-4">Phone number</label>
                    <input
                        type="text"
                        name="phoneNumber"
                        id="phoneNumber"
                        className="form-control-sm"
                        placeholder="Phone number"
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        value={phoneNumber}
                        required={true}
                    />
                </div>
            </div>
        </div>
    )
}

export default CustomerForm;