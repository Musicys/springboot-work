// src/http.js
import Request from 'luch-request';
const service = new Request();
import { baseURL } from '@/util/system/CONFIG';

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
      // 可添加 Token 或其他自定义逻辑

      if (configs.cookie != null) {
         config.header = {
            ...config.header,
            Cookie: configs.cookie
         };
         console.log('请求头', configs.cookie);
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
      // #ifdef APP-PLUS

      if (
         (config.url == '/api/user/login' ||
            config.url == '/api/user/emacode' ||
            config.url == '/api/user/register') &&
         response.cookies[0]
      ) {
         console.log('response.cookies[0]', response.cookies[0]);

         configs.cookie = response.cookies[0];
      }
      if (config.url == '/api/user/logout') {
         configs.cookie = null;
      }
      // #endif
      if (data.code === 40101) {
         return data;
      }

      if (data.code !== 0) {
         uni.showToast({
            title: data.description || data.message || '请求失败',
            icon: 'none'
         });
         return Promise.resolve(data);
      }
      return data;
   },
   error => {
      uni.showToast({ title: '网络错误', icon: 'error' });

      return Promise.resolve(error);
   }
);
export default service;
