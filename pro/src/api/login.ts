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
   const response = await httpPost('/pro/login', params);
   return response;
};
