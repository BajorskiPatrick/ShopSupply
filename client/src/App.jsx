import Menubar from "./components/Menubar/Menubar.jsx";
import ManageCategories from "./pages/ManageCategories/ManageCategories.jsx";
import ManageUsers from "./pages/ManageUsers/ManageUsers.jsx";
import ManageItems from "./pages/ManageItems/ManageItems.jsx";
import Explore from "./pages/Explore/Explore.jsx";
import {Navigate, Routes, useLocation} from "react-router-dom";
import { Route } from "react-router-dom";
import {Toaster} from "react-hot-toast";
import Login from "./pages/Login/Login.jsx";
import OrderHistory from "./components/OrderHistory/OrderHistory.jsx";
import {useContext, useEffect} from "react";
import {AppContext} from "./context/AppContext.jsx";
import NotFound from "./pages/NotFound/NotFound.jsx";
import {useSearchParams} from "react-router-dom";

const App = () => {
    const location = useLocation();
    const {auth} = useContext(AppContext);
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const paymentStatus = searchParams.get("payment");
        if (paymentStatus) {
            sessionStorage.setItem('paymentStatus', paymentStatus);
        }
    }, [searchParams]);

    const LoginRoute = ({element}) => {
        if (auth.token) {
            return <Navigate to="/explore" replace />;
        }
        return element;
    }

    const ProtectedRoute = ({element, allowedRoles = []}) => {
        const { auth, isAuthLoading } = useContext(AppContext);

        if (isAuthLoading) {
            return <div>Loading...</div>; // Lub <Spinner />
        }

        if (!auth.token) {
            return <Navigate to="/login" replace />;
        }

        if (allowedRoles.length > 0 && !allowedRoles.includes(auth.role)) {
            return <Navigate to="/explore" replace />;
        }
        return element;
    };

    return (
        <div>
            {location.pathname !== "/login" && <Menubar />}
            <Toaster />
            <Routes>
                <Route path="/" element={<Navigate to="/explore" replace />} />
                <Route path="/explore" element={<ProtectedRoute element={<Explore />} />} />
                <Route path="/login" element={<LoginRoute element={<Login />} />} />
                <Route path="/orders" element={<ProtectedRoute element={<OrderHistory />} />} />
                <Route path="/categories" element={<ProtectedRoute element={<ManageCategories />} allowedRoles={['ROLE_ADMIN']} />} />
                <Route path="/users" element={<ProtectedRoute element={<ManageUsers />} allowedRoles={['ROLE_ADMIN']} />} />
                <Route path="/items" element={<ProtectedRoute element={<ManageItems />} allowedRoles={['ROLE_ADMIN']} />} />

                <Route path="*" element={<NotFound />} />
            </Routes>
        </div>
    );
}

export default App;