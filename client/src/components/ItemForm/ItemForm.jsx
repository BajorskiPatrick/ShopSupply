import {useContext, useState} from "react";
import {assets} from "../../assets/assets.js";
import {AppContext} from "../../context/AppContext.jsx";
import toast from "react-hot-toast";
import {addItem} from "../../service/ItemService.js";

const ItemForm = () => {
    const {categories, setCategories, items, setItems} = useContext(AppContext);
    const [loading, setLoading] = useState(false);
    const [image, setImage] = useState(false);
    const [data, setData] = useState({
        name: "",
        categoryId: "",
        price: "",
        description: ""
    });

    const onChangeHandler = (e) => {
        const name = e.target.name;
        const value = e.target.value;
        setData((data) => ({...data, [name]: value}));
    }

    const onSubmitHandler = async (e) => {
        e.preventDefault();
        setLoading(true);

        const formData = new FormData();
        formData.append("item", JSON.stringify(data));
        formData.append("file", image);
        try {
            if (!image) {
                toast.error("Provide image");
                return;
            }
            const response = await addItem(formData);
            if (response.status !== 201) {
                toast.error("Error adding item");
                return;
            }
            setItems([...items, response.data]);

            const updatedCategories = categories.map((category) =>
                category.categoryId === response.data.categoryId ? {...category, itemsCount: category.itemsCount + 1} : category);
            setCategories(updatedCategories);

            toast.success("Item added successfully");

            setData({
                name: "",
                categoryId: "",
                price: "",
                description: ""
            })
            setImage(false);
        } catch (error) {
            console.error(error);
            toast.error("Error adding item: " + error.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="mx-2 mt-2">
            <div className="row">
                <div className="card col-md-12 form-container">
                    <div className="card-body">
                        <form onSubmit={onSubmitHandler}>
                            <div className="mb-3">
                                <label htmlFor="image" className="form-label">
                                    <img src={image ? URL.createObjectURL(image) : assets.upload} alt="" width={48} />
                                </label>
                                <input
                                    type="file"
                                    name="image"
                                    id="image"
                                    className="form-control"
                                    hidden
                                    onChange={(e) => {
                                        setImage(e.target.files[0]);
                                    }}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="name" className="form-label">Name</label>
                                <input
                                    type="text"
                                    name="name"
                                    id="name"
                                    className="form-control"
                                    placeholder="Item Name"
                                    onChange={onChangeHandler}
                                    value={data.name}
                                    required={true}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="categoryId" className="form-label">Category</label>
                                <select
                                    name="categoryId"
                                    id="categoryId"
                                    className="form-control"
                                    onChange={onChangeHandler}
                                    value={data.categoryId}
                                    required={true}
                                >
                                    <option value="" disabled>Select a category</option>
                                    {categories.map((category, index) => (
                                        <option key={index} value={category.categoryId}>
                                            {category.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="mb-3">
                                <label htmlFor="price" className="form-label">Price</label>
                                <input
                                    type="number"
                                    name="price"
                                    id="price"
                                    className="form-control"
                                    placeholder="&#36;100.00"
                                    onChange={onChangeHandler}
                                    value={data.price}
                                    required={true}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="description" className="form-label">Description</label>
                                <textarea
                                    rows={5}
                                    name="description"
                                    id="description"
                                    className="form-control"
                                    placeholder="Write content here..."
                                    onChange={onChangeHandler}
                                    value={data.description}
                                ></textarea>
                            </div>
                            <button
                                type="submit"
                                className="btn btn-warning w-100"
                                disabled={loading}
                            >
                                {loading ? "Saving..." : "Save"}
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ItemForm;