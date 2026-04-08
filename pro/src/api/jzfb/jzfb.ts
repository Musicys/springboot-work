import { httpPost } from '@/util/http';

/**
 * @description 获取本商户的兼职岗位列表
 */
export function getList(params: { pageNum: number; pageSize: number; params?: any }) {
   return httpPost('/pro/job/list', params);
}

/**
 * @description 获取兼职岗位详情
 */
export function getDetail(params: { id: number }) {
   return httpPost('/pro/job/detail?jobId=' + params.id, params);
}

/**
 * @description 发布新兼职岗位
 */
export function createData(params: any) {
   return httpPost('/pro/job/create', params);
}

/**
 * @description 更新兼职岗位
 */
export function updateData(params: any) {
   return httpPost('/pro/job/update', params);
}

/**
 * @description 结款
 */
export function settleJob(params: { id: number }) {
   return httpPost('/pro/job/settle?jobId=' + params.id, params);
}

/**
 * @description 退押金
 */
export function refundDeposit(params: { id: number }) {
   return httpPost('/pro/job/refundDeposit?jobId=' + params.id, params);
}
