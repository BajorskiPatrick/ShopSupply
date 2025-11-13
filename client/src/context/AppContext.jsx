import {createContext, useEffect, useState, useCallback} from "react";
import {fetchCategories} from "../service/CategoryService.js";
import {fetchItems} from "../service/ItemService.js";

export const AppContext = createContext(null);

export const AppContextProvider = (props) => {

    const [categories, setCategories] = useState([]);
    const [items, setItems] = useState([]);
    const [auth, setAuth] = useState({token: null, role: null});
    const [cartItems, setCartItems] = useState([]);
    const [isExploreRendered, setIsExploreRendered] = useState(false);
    const [isAuthLoading, setIsAuthLoading] = useState(true);

    const addToCart = useCallback((item) => {
        setCartItems(prevCartItems => {
            const existingItem = prevCartItems.find(cartItem => cartItem.itemId === item.itemId);
            if (existingItem) {
                return prevCartItems.map(cartItem => cartItem.itemId === item.itemId ? {...cartItem, quantity: cartItem.quantity + 1 } : cartItem);
            } else {
                return [...prevCartItems, item];
            }
        });
    }, []);

    const clearCart = useCallback(() => {
        setCartItems([]);
    }, []);

    const removeFromCart = useCallback((itemId) => {
        setCartItems(prevCartItems => prevCartItems.filter(cartItem => cartItem.itemId !== itemId));
    }, []);

    const updateQuantity = useCallback((itemId, quantity) => {
        setCartItems(prevCartItems => prevCartItems.map(cartItem => cartItem.itemId === itemId ? {...cartItem, quantity: quantity} : cartItem));
    }, []);

    const setAuthData = useCallback((token, role) => {
        setAuth({token, role});
    }, []);

    useEffect(() => {
        try {
            if (localStorage.getItem("token") && localStorage.getItem("role")) {
                setAuthData(localStorage.getItem("token"), localStorage.getItem("role"));
            }
        } catch (error) {
            console.error("Error reading from localStorage", error);
        } finally {
            setIsAuthLoading(false);
        }
    }, [setAuthData]);

    useEffect(() => {
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

        if (!isAuthLoading) {
            loadData().then( () =>
                setIsExploreRendered(true)
            );
        }
    }, [auth.token, isAuthLoading]);

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
        updateQuantity,
        isAuthLoading,
        isExploreRendered,
        setIsExploreRendered
    }

    return <AppContext.Provider value={contextValue}>{props.children}</AppContext.Provider>
}