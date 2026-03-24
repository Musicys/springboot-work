import { HttpGet, HttpPost } from '@/util/http';

/**
 * @description 距离获取用户列表
 * @param data { page: number; pageSize: number; }
 * @returns Promise
 */
export const UserListLongitude = (data: {
   lat: number;
   lng: number;
   page: number;
   pageSize: number;
}) => {
   return HttpPost('/api/user/userListLongitude', data);
};

/**
 * @description 全部用户
 * @param data { page: number; pageSize: number; }
 * @returns Promise
 */
export const UserList = (data: {
   current: number;
   gender?: 0 | 1;
   id?: number;
   pageSize: number;
   userAccount?: string;
   userStatus: 0;
   username?: string;
}) => {
   return HttpPost('/api/user/userlist', data);
};

/**
 * @description 系统标签
 * @param data    { page: number; pageSize: number; }
 * @returns Promise
 */
export const SystemTag = (data: {
   current: number;
   pageSize: number;
   sortField: string;
   sortOrder: string;
   tagName: string;
}) => {
   return HttpPost('/api/system/usertaglist', data);
};

/**
 * @description 获取查看我的统计
 */

export const MyViewStatistics = () => {
   return HttpGet('/api/system/mynumber');
};
/**
 * @description 获取我查看的统计
 */
export const YoUViewStatistics = () => {
   return HttpGet('/api/system/looknumber');
};
