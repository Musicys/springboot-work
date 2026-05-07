import { HttpPost, HttpGet } from '@/util/http';

// 前缀url
const url = '/user';

export const login = (data: { username: string; password: string }) => {
   return HttpPost(url + '/login', data);
};

export const register = (data: {
   username: string;
   password: string;
   studentId?: string;
   realName?: string;
   age?: number;
   gender?: number;
   phone?: string;
   profession?: string;
   introduction?: string;
   avatarUrl?: string;
}) => {
   return HttpPost(url + '/register', data);
};

export const getLoginStatus = () => {
   return HttpPost(url + '/getLoginStatus', {});
};

export const logout = () => {
   return HttpPost(url + '/logout', {});
};
