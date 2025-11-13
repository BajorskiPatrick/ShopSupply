import {useContext, useState} from "react";
import {AppContext} from "../../context/AppContext.jsx";
import {deleteItem} from "../../service/ItemService.js";
import toast from "react-hot-toast";
import './ItemsList.css';

const ItemsList = () => {
    const {items, setItems, categories, setCategories} = useContext(AppContext);
    const [searchTerm, setSearchTerm] = useState("");
    const [loading, setLoading] = useState(false);

    const filteredItems = items.filter((item) =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase())
    )

    const deleteItemById = async (id) => {
        setLoading(true);
        try {
            const response = await deleteItem(id);
            if (response.status !== 204) {
                toast.error("Error deleting user: " + response.status);
            }

            const deletedItem = items.filter(item => item.itemId === id)[0];
            const updatedCategories = categories.map((category) =>
                category.categoryId === deletedItem.categoryId ? {...category, itemsCount: category.itemsCount - 1} : category
            );
            setItems(items.filter(item => item.itemId !== deletedItem.itemId))
            setCategories(updatedCategories);

            toast.success("Successfully deleted user");
        } catch (error) {
            console.error(error);
            toast.error("Error deleting user: " + error);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="items-list-container" style={{height: '100vh', overflowY: 'auto', overflowX: 'hidden'}}>
            <div className="row pe-2">
                <div className="input-group mb-3">
                    <input type="text" name="keyword" id="keyword" placeholder="Search by keyword" className="form-control"
                           onChange={(e) => setSearchTerm(e.target.value)}
                           value={searchTerm}
                    />
                    <span className="input-group-text bg-warning">
                        <i className="bi bi-search"></i>
                    </span>
                </div>
            </div>
            <div className="row g-3 pe-2">
                {filteredItems.map((item, index) => (
                    <div className="col-12" key={index}>
                        <div className="card p-3 bg-dark">
                            <div className="d-flex align-items-center">
                                <div style={{marginRight: '15px'}}>
                                    <img src={item.imgUrl} alt={item.name} className="item-image" />
                                </div>
                                <div className="flex-grow-1">
                                    <h6 className="mb-1 text-white">
                                        {item.name}
                                    </h6>
                                    <p className="mb-0 text-white">
                                        Category: {item.categoryName}
                                    </p>
                                    <span className="mb-0 text-block badge rounded-pill text-bg-warning">
                                        &#36;{item.price}
                                    </span>
                                </div>
                                <div>
                                    <button className="btn btn-danger btn-sm"
                                            onClick={() => deleteItemById(item.itemId)}
                                    >
                                        {loading ? (
                                            <>
                                                <span
                                                    className="spinner-border spinner-border-sm"
                                                    aria-hidden="true"
                                                ></span>
                                                <span className="ms-1">Deleting...</span>
                                            </>
                                        ) : (
                                            <>
                                                <i className="bi bi-trash"></i>
                                                <span className="ms-1">Delete</span>
                                            </>
                                        )}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default ItemsList;