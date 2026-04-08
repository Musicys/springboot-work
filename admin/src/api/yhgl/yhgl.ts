import { httpPost } from '@/util/http';

/**
 * @description 获取数据列表
 */
export function getList(params: any) {
   return httpPost('/admin/yhgl/list', params);
}

/**
 * @description 获取数据详情
 */
export function getDetail(params: { id: number }) {
   return httpPost('/admin/yhgl/detail?id=' + params.id, params);
}

/**
 * @description 创建数据
 */
export function createData(params: any) {
   return httpPost('/admin/yhgl/create', params);
}

/**
 * @description 更新数据
 */
export function updateData(params: any) {
   return httpPost('/admin/yhgl/update', params);
}

/**
 * @description 删除数据
 */
export function deleteData(params: { id: number }) {
   return httpPost('/admin/yhgl/delete?id=' + params.id, params);
}

/**
 * @description 导出数据列表
 */
export function exportData() {
   return httpPost('/admin/yhgl/export');
}

/**
 * @description 封禁用户
 */
export function banUser(params: { id: number }) {
   return httpPost('/admin/yhgl/ban?id=' + params.id, params);
}

/**
 * @description 解封用户
 */
export function unbanUser(params: { id: number }) {
   return httpPost('/admin/yhgl/unban?id=' + params.id, params);
}
