import './ManageItems.css'
import ItemForm from "../../components/ItemForm/ItemForm.jsx";
import ItemsList from "../../components/ItemsList/ItemsList.jsx";

const manageItems = () => {
    return (
        <div className="items-container text-light">
            <div className="left-column">
                <ItemForm />
            </div>
            <div className="right-column">
                <ItemsList />
            </div>
        </div>
    )
}

export default manageItems;