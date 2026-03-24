import { HttpGet, HttpPost } from '@/util/http';

/**
 * @description 消息查看
 * @param data { mobile: string; }
 * @returns Promise
 */
export const DynamciTagList = (data: { page: number; pageSize: number }) => {
   return HttpPost('/api/dyntag/list', data);
};

export const DynamciTagGetone = (id: number) => {
   return HttpGet('/api/dyntag/get', { tagId: id });
};
export const DynamciTagGetRandom = (num: number) => {
   return HttpGet('/api/dyntag/random', { number: num });
};
