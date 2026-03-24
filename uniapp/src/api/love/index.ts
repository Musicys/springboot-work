import { HttpPost, HttpGet } from '@/util/http';

/**
 * @description 点赞
 */

export const LoveAdd = (data: { commentsId: number; dynamicId: number }) => {
   return HttpPost('/api/love/good', data);
};

/**
 * @description 取消点赞
 */

export const LoveDel = (data: { commentsId: number; dynamicId: number }) => {
   return HttpPost('/api/love/cancelgood', data);
};

/**
 * @description 我点赞的动态
 * @data
 * @return Promise
 */
export const Lovemygooddrs = (data: { page: number; pageSize: number }) => {
   return HttpPost('/api/love/mygooddrs', data);
};
/**
 * @description 收到的点赞
 */
export const LovemygoodList = (data: { page: number; pageSize: number }) => {
   return HttpPost('/api/love/getmygooddrs', data);
};
/**
 * @description 收到的回复列表
 */
export const LovemyCommentList = (data: { page: number; pageSize: number }) => {
   return HttpPost('/api/love/getmydrscomment', data);
};
