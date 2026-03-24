import { HttpGet, HttpPost } from '@/util/http';

/**
 * @description 距离动态列表
 * @param data { lat: number, lng: number, page: number, pageSize: number, province: string }
 * @returns Promise
 */
export const getDynamicList = (data: {
   lat: number;
   lng: number;
   page: number;
   pageSize: number;
   province: string;
   argId?: string;
   type?: number;
}) => {
   return HttpPost('/api/dynamic/list', data);
};

/**
 * @description 添加动态
 * @param data { context: string, imgArr: string, lat: number, lng: number, userId: number }
 * @returns Promise
 */
export const addDynamic = (data: {
   context: string;
   imgArr?: string;
   lat: number;
   lng: number;
   userId: number;
   type: number;
   video?: string;
   argId?: string;
}) => {
   return HttpPost('/api/dynamic/add', data);
};

/**
 * @description 动态详情
 * @param data { dynamicId: number, page: number, pageSize: number, type: number }
 * @returns Promise
 */
export const getDynamicDetail = (data: {
   dynamicId: number;
   page: number;
   pageSize: number;
   type: number;
}) => {
   return HttpPost('/api/comments/dynamic/list', data);
};

/**
 * @description 回复分页
 * @param data { commentsId: number, page: number, pageSize: number, replyId: number, type: number }
 * @returns Promise
 */

export const getReplyList = (data: {
   commentsId: number; // 评论id
   page: number;
   pageSize: number;
   replyId: number;
   type: number;
}) => {
   return HttpPost('/api/comments/comments/list', data);
};
/**
 * @description 单个动态
 * @param data id
 * @returns Promise
 */

export const getDynamicOne = (id: number) => {
   return HttpGet('/api/dynamic/one', {
      id: id
   });
};

/**
 * @description 评论回复
 * @param data  { context: string; imgArr: string; replyId: number; replyUserId: number; }
 * @returns Promise
 */
export const addComment = (data: {
   context: string; // 评论内容
   imgArr: string;
   replyId: number;
   replyUserId?: number;
}) => {
   return HttpPost('/api/comments/add', data);
};

/**
 * @description 获取固定ID的动态
 * @param data  { page: number; pageSize: number; id: number; }
 * @ret
 * */
export const getDynamicById = (data: {
   page: number;
   pageSize: number;
   id: number;
}) => {
   return HttpPost('/api/detail/get/user/dynamic', {
      current: data.page,
      pageSize: data.pageSize, // id: 1
      province: '',
      sortField: '',
      sortOrder: '',
      userid: data.id
   });
};

/**
 * @description 删除动态
 * @param data { dynamicId: number }
 * @returns Promise
 */
export const deleteDynamic = id => {
   return HttpGet('/api/dynamic/delete', {
      id: id
   });
};
