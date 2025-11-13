import './DisplayCategories.css';
import Category from "../Category/Category.jsx";
import {assets} from "../../assets/assets.js";

const DisplayCategories = ({categories, selectedCategory, setSelectedCategory}) => {
    return (
        <div className="row g-3 display-categories-container">
            <div key="all" className="col-md-3 col-sm-6" style={{padding:'0 10px'}}>
                <Category
                    categoryName="All Items"
                    imgUrl={assets.device}
                    numberOfItems={categories.reduce((acc, cat) => acc + cat.itemsCount, 0)}
                    bgColor="#6c757d"
                    isSelected={selectedCategory === ""}
                    onClick={() => setSelectedCategory("")}
                />
            </div>
            {categories.map((category, index) => (
                <div key={index} className="col-md-3 col-sm-6 category-component-container">
                    <Category
                        categoryName={category.name}
                        imgUrl={category.imgUrl}
                        numberOfItems={category.itemsCount}
                        bgColor={category.bgColor}
                        isSelected={selectedCategory === category.categoryId}
                        onClick={() => setSelectedCategory(category.categoryId)}
                    />
                </div>
            ))}
        </div>
    )
}

export default DisplayCategories;