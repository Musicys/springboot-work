import { httpPost } from '@/util/http';

// 登录请求参数
interface LoginParams {
   username: string;
   password: string;
}

// 登录响应数据
interface LoginResponse {
   code: number;
   message: string;
   data: {
      id: number;
      username: string;
      userType: number;
      token: string;
   };
}

/**
 * 管理员登录
 * @param params 登录参数
 * @returns 登录响应
 */
export const adminLogin = async (params: LoginParams): Promise<LoginResponse> => {
   const response = await httpPost('/admin/login', params);
   return response;
};
/**
 * @description 获取用户信息
 */
export const getUserInfo = async () => {
   return await httpPost('/admin/getLoginStatus/info');
};

/**
 * @description 退出登录
 */
export const logout = async () => {
   return await httpPost('/admin/logout');
};
