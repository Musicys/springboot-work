import { HttpGet, HttpPost } from '@/util/http';

/**
 * @description 获取被查看详情页的浏览
 * @param data
 * @returns Promise
 */
export function getDetailViews(data: { page: number; pageSize: number }) {
   return HttpPost('/api/detail/get', data);
}

/**
 * @description 获取查看详情页的浏览
 * @param data
 * @returns Promise
 */

export function getMeDetailViews(data: { page: number; pageSize: number }) {
   return HttpPost('/api/detail/get/me', data);
}
/**
 * @description 查看详情页
 * @param data
 * @returns Promise
 */
export function getUsersDetailViews(lookid: number) {
   return HttpGet('/api/detail/get/user?lookId=' + lookid);
}
