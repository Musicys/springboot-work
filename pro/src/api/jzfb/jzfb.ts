import { httpPost, httpGet } from '@/util/http';

/**
 * @description 获取商家的兼职岗位列表
 */
export function getList(params: { pageNum: number; pageSize: number; keyword?: string }) {
   return httpPost('/pro/job/job/list', params);
}

/**
 * @description 获取待审核的兼职岗位列表
 */
export function getPendingList(params: { pageNum: number; pageSize: number; keyword?: string }) {
   return httpPost('/pro/job/job/list/pending', params);
}

/**
 * @description 获取兼职岗位详情
 */
export function getDetail(params: { id: number }) {
   return httpGet('/pro/job/job/get/' + params.id, null);
}

/**
 * @description 发布新兼职岗位
 */
export function createData(params: any) {
   return httpPost('/pro/job/job/add', params);
}

/**
 * @description 更新兼职岗位
 */
export function updateData(params: any) {
   return httpPost('/pro/job/job/update', params);
}

/**
 * @description 删除兼职岗位
 */
export function deleteJob(params: { id: number }) {
   return httpPost('/pro/job/job/delete/' + params.id, params);
}

/**
 * @description 下架兼职岗位
 */
export function closeJob(params: { id: number }) {
   return httpPost('/pro/job/job/close/' + params.id, params);
}

/**
 * @description 上架兼职岗位
 */
export function openJob(params: { id: number }) {
   return httpPost('/pro/job/job/open/' + params.id, params);
}

/**
 * @description 获取已发布的兼职岗位列表
 */
export function getPublishedList(params: { pageNum: number; pageSize: number; keyword?: string }) {
   return httpPost('/pro/job/job/list/published', params);
}
