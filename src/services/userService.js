import { getUser, updateUser } from '@/apis/userApi';

export const fetchUserWithFormat = async (id) => {
    const res = await getUser(id);
    const user = res.data;

    return {
        ...user,
        fullName: `${user.firstName} ${user.lastName}`,
    };
};