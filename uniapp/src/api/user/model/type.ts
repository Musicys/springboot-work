// 用户标签使用字符串数组
export interface User {
   id: number;
   username: string;
   userAccount: string;
   avatarUrl: string;
   gender: number; // 0: 女, 1: 男, 可选：可用枚举
   phone: string;
   email: string;
   tags: string[]; // 注意：后端传的是 JSON 字符串，需转换
   userStatus: number; // 用户状态，如 0: 正常
   createTime: string; // ISO 时间字符串格式
   updateTime: string; // ISO 时间字符串格式
   userRole: number; // 0: 普通用户, 1: 管理员等
   planetCode: string;
   province: string;
   district: string;
   lat: number;
   lng: number;
   introductory: string;
   age: number;
   login: number; // 登录状态？1 表示已登录？根据业务定义
   is_default?: number; // 是否默认？1 表示是默认？根据业务定义
   imagsarr: string[]; // 图片数组，注意：后端传的是 JSON 字符串，需转换
   tokens?: {
      userAccount: string;
      userPassword: string;
   };
}
