import './Category.css';

const Category = ({categoryName, imgUrl, numberOfItems, bgColor, isSelected, onClick}) => {
    const bgColorStyle = {
        "--bg-color": bgColor
    };

    return (
        <div className="d-flex align-items-center p-3 rounded gap-1 position-relative category-container"
             style={bgColorStyle}
             onClick={onClick}
        >
            <div className="image-container">
                <img src={imgUrl} alt={categoryName} className="category-image"/>
            </div>
            <div>
                <h6 className="text-white mb-0">
                    {categoryName}
                </h6>
                <p className="text-white mb-0">
                    Items: {numberOfItems}
                </p>
            </div>
            {isSelected &&
                <div className="active-category"></div>
            }
        </div>
    )
}

export default Category;