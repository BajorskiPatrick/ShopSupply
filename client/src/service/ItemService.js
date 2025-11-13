import api from './Api.js';

export const addItem = async (item) => {
    return await api.post('/admin/items', item, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}});
}

export const deleteItem = async (id) => {
    return await api.delete(`/admin/items/${id}`, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}});
}

export const fetchItems = async () => {
    return api.get('/items', {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}});
}