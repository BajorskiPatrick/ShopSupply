import './ManageCategories.css';
import CategoryForm from "../../components/CategoryForm/CategoryForm.jsx";
import CategoriesList from "../../components/CategoriesList/CategoriesList.jsx";

const ManageCategories = () => {
    return (
        <div className="categories-container text-light">
            <div className="left-column">
                <CategoryForm />
            </div>
            <div className="right-column">
                <CategoriesList />
            </div>
        </div>
    )
}

export default ManageCategories;