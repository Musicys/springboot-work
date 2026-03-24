import { HttpGet } from '@/util/http';

/**
 * @description 消息查看
 * @param data { mobile: string; }
 * @returns Promise
 */
export const UpdateSendsye = (senid: number) => {
   return HttpGet('/api/send/sye', { sendId: senid });
};
