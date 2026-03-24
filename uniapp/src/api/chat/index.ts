/**
 * @description 获取群聊列表
 */

import { HttpPost } from '@/util/http';

export const getChatList = (data: {
   page: number;
   pageSize: number;
   sach: string;
}) => {
   return HttpPost('/api/chat/lst', data);
};

/**
 * @description 个人消息分页
 */
export const getSendMsgList = (data: {
   page: number;
   pageSize: number;
   sendId: string;
}) => {
   return HttpPost('/api/chat/get/sendlst', data);
};

/**
 * @description 获取群聊消息分页
 */

export const getChatMsgList = (data: {
   page: number;
   pageSize: number;
   chatId: string;
}) => {
   return HttpPost('/api/chat/get/chatdetailslst', data);
};
