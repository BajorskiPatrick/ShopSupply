import api from './Api.js';

export const addCategory = async (category) => {
    return await api.post('/categories', category, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}});
}

export const deleteCategory = async (categoryId) => {
    return await api.delete(`/admin/categories/${categoryId}`, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}})
}

export const fetchCategories = async () => {
    return await api.get('/categories', {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}})
}
