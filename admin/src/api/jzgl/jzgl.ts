import { httpPost } from '@/util/http';

/**
 * @description 获取兼职岗位列表
 */
export function getList(params: any) {
   return httpPost('/admin/job/list', params);
}

/**
 * @description 获取兼职岗位详情
 */
export function getDetail(params: { id: number }) {
   return httpPost('/admin/job/detail?jobId=' + params.id, params);
}

/**
 * @description 创建兼职岗位
 */
export function createData(params: any) {
   return httpPost('/admin/job/create', params);
}

/**
 * @description 更新兼职岗位
 */
export function updateData(params: any) {
   return httpPost('/admin/job/update', params);
}

/**
 * @description 删除兼职岗位
 */
export function deleteData(params: { id: number }) {
   return httpPost('/admin/job/delete?jobId=' + params.id, params);
}

/**
 * @description 导出兼职岗位列表
 */
export function exportData() {
   return httpPost('/admin/job/export');
}

/**
 * @description 审核通过兼职岗位
 */
export function approveJob(params: { id: number }) {
   return httpPost('/admin/job/approve?jobId=' + params.id, params);
}

/**
 * @description 强制下架兼职岗位
 */
export function forceCloseJob(params: { id: number }) {
   return httpPost('/admin/job/forceClose?jobId=' + params.id, params);
}

/**
 * @description 更新兼职岗位状态
 */
export function updateStatus(params: { jobId: number; status: number }) {
   return httpPost('/admin/job/updateStatus', params);
}
