// src/http.js
import Request from 'luch-request';
const service = new Request();
import { baseURL } from '@/util/system/CONFIG';
import router from '@/router';
export const configs = {
   cookie: null
};

// 全局配置
service.setConfig(config => {
   config.baseURL = baseURL;

   config.timeout = 25000; // 设置超时时间
   config.withCredentials = true; // 跨域请求时是否携带凭证（cookies）
   return config;
});
// 请求拦截器
service.interceptors.request.use(
   config => {
      // 从本地存储读取 token

      const token = uni.getStorageSync('token');
      console.log('读取', token);

      if (token) {
         config.header = {
            ...config.header,
            'User-Authorization': `Bearer ${token}`
         };
      }

      if (configs.cookie != null) {
         config.header = {
            ...config.header,
            Cookie: configs.cookie
         };
      } else {
         config.header = {
            ...config.header
         };
      }

      return config;
   },
   error => {
      return Promise.reject(error);
   }
);
// 响应拦截器
service.interceptors.response.use(
   response => {
      const { data, config } = response;
      console.log(data.code);

      if (data.code === 40300) {
         // 无权限

         uni.showToast({
            title: data.message || '无权限',
            icon: 'none'
         });
         return Promise.reject(data);
      }
      if (config.url == '/user/login') {
         configs.cookie = response.cookies[0];
         // 如果返回了 token，存储到本地
         if (data.data && data.data.token) {
            uni.setStorageSync('token', data.data.token);
         }
         console.log('restoken', data.data.token);
      }
      if (config.url == '/user/logout') {
         configs.cookie = null;
         // 清除本地存储的 token
         uni.removeStorageSync('token');
      }

      console.log('456', data.code);

      if (data.code === 40100) {
         // 未登录

         uni.showToast({
            title: '登录过期，请重新登录',
            icon: 'none',
            success: () => {
               router.push({
                  name: 'login'
               });
            }
         });
         return Promise.reject(data);
      }

      if (data.code !== 0) {
         uni.showToast({
            title: data.description || data.message || '请求失败',
            icon: 'none'
         });
         return Promise.reject(data);
      }
      return Promise.resolve(data);
   },
   error => {
      uni.showToast({ title: '网络错误', icon: 'error' });

      return Promise.reject(error);
   }
);
export default service;
