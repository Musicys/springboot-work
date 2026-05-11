import { httpPost } from '@/util/http';

/**
 * @description 获取数据列表
 */
export function getList(params: any) {
   return httpPost('/admin/sjgl/list', params);
}

/**
 * @description 获取数据详情
 */
export function getDetail(params: { id: number }) {
   return httpPost('/admin/sjgl/detail?id=' + params.id, params);
}

/**
 * @description 创建数据
 */
export function createData(params: any) {
   return httpPost('/admin/sjgl/create', params);
}

/**
 * @description 更新数据
 */
export function updateData(params: any) {
   return httpPost('/admin/sjgl/update', params);
}

/**
 * @description 删除数据
 */
export function deleteData(params: { id: number }) {
   return httpPost('/admin/sjgl/delete?id=' + params.id, params);
}

/**
 * @description 导出数据列表
 */
export function exportData() {
   return httpPost('/admin/sjgl/export');
}

/**
 * @description 封禁商户
 */
export function banMerchant(params: { id: number }) {
   return httpPost('/admin/sjgl/ban?id=' + params.id, params);
}

/**
 * @description 解封商户
 */
export function unbanMerchant(params: { id: number }) {
   return httpPost('/admin/sjgl/unban?id=' + params.id, params);
}

/**
 * @description 审核通过商户
 */
export function approveMerchant(params: { id: number }) {
   return httpPost('/admin/sjgl/approve?id=' + params.id, params);
}
