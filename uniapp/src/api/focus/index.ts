import { HttpPost, HttpGet } from '@/util/http';
/**
 * @description 关注
 */

export const FocusAdd = (data: { focusUserId: number }) => {
   return HttpGet('/api/focus/add', data);
};

/**
 * @description 取消关注
 */

export const FocusDel = (data: { focusUserId: number }) => {
   return HttpGet('/api/focus/del', data);
};
/**
 * @description 我关注的动态
 * @data { page: number; pageSize: number }
 * @return Promise
 */

export const focusdrslst = (data: {
   page: number;
   pageSize: number;
   sach: string;
}) => {
   return HttpPost('/api/focus/focusdrslst', data);
};

/**
 * @description 判断是否关注
 */
export const isFocus = (data: number) => {
   return HttpGet('/api/focus/isfocus', {
      focusUserId: data
   });
};

/**
 * @description 获取关注列表
 */
export const myFocusLst = (data: {
   page: number;
   pageSize: number;
   sach: string;
}) => {
   return HttpPost('/api/focus/myfocuslst', data);
};

/**
 * @description 获取粉丝列表
 */
export const myFansLst = (data: {
   page: number;
   pageSize: number;
   sach: string;
}) => {
   return HttpPost('/api/focus/youfocuslst', data);
};
