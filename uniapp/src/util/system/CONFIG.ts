let baseURLs = '';
// #ifdef H5
baseURLs = import.meta.env.VITE_APP_BASE_API || `${location.origin}/api`; // 根据环境变量设置基础 URL
// #endif
// #ifdef APP-PLUS
baseURLs = 'http://101.42.172.99:8080'; // 根据环境变量设置基础 URL
// #endif

export const baseURL = baseURLs;
