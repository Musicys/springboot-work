import { HttpPost } from '@/util/http';
/**
 * @description 被查看的用户列表
 */

export const LookGetList = (data: { page: number; pageSize: number }) => {
   return HttpPost('/api/dynamicview/list', data);
};
