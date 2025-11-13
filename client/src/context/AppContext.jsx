import {createContext, useEffect, useState} from "react";
import {fetchCategories} from "../service/CategoryService.js";
import {fetchItems} from "../service/ItemService.js";

export const AppContext = createContext(null);

export const AppContextProvider = (props) => {

    const [categories, setCategories] = useState([]);
    const [items, setItems] = useState([]);
    const [auth, setAuth] = useState({token: null, role: null});
    const [cartItems, setCartItems] = useState([]);

    const addToCart = (item) => {
        const existingItem = cartItems.find(cartItem => cartItem.itemId === item.itemId);
        if (existingItem) {
            setCartItems(cartItems.map(cartItem => cartItem.itemId === item.itemId ? {...cartItem, quantity: cartItem.quantity + 1 } : cartItem));
        } else {
            setCartItems([...cartItems, item]);
        }
    }

    const clearCart = () => {
        setCartItems([]);
    }

    const removeFromCart = (itemId) => {
        setCartItems(cartItems.filter(cartItem => cartItem.itemId !== itemId));
    }

    const updateQuantity = (itemId, quantity) => {
        setCartItems(cartItems.map(cartItem => cartItem.itemId === itemId ? {...cartItem, quantity: quantity} : cartItem));
    }

    useEffect(() => {
        // Sprawdź czy użytkownik jest już zalogowany (token w localStorage)
        if (localStorage.getItem("token") && localStorage.getItem("role")) {
            setAuthData(localStorage.getItem("token"), localStorage.getItem("role"));
        }
    }, []);

    useEffect(() => {
        // Pobieraj dane tylko gdy użytkownik jest zalogowany
        async function loadData() {
            if (auth.token) {
                try {
                    const categoriesResponse = await fetchCategories();
                    const itemsResponse = await fetchItems();
                    setCategories(categoriesResponse.data);
                    setItems(itemsResponse.data);
                } catch (error) {
                    console.error("Błąd podczas ładowania danych:", error);
                }
            }
        }
        loadData();
    }, [auth.token]);

    const setAuthData = (token, role) => {
        setAuth({token, role});
    }

    const contextValue = {
        categories,
        setCategories,
        items,
        setItems,
        auth,
        setAuthData,
        addToCart,
        clearCart,
        cartItems,
        removeFromCart,
        updateQuantity
    }

    return <AppContext.Provider value={contextValue}>{props.children}</AppContext.Provider>
}