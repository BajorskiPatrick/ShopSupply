import api from './Api.js';

export const addUser = async (user) => {
    return await api.post('/admin/register', user, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}});
}

export const deleteUser = async (userId) => {
    return await api.delete(`/admin/users/${userId}`, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}});
}

export const fetchUsers = async () => {
    return await api.get('/admin/users', {headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}});
}