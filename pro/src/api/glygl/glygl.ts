import { httpPost } from '@/util/http';

/**
 * @description 获取数据列表
 */
export function getList(params: any) {
   return httpPost('/admin/glygl/list', params);
}

/**
 * @description 获取数据详情
 */
export function getDetail(params: { id: number }) {
   return httpPost('/admin/glygl/detail?id=' + params.id, params);
}

/**
 * @description 创建数据
 */
export function createData(params: any) {
   return httpPost('/admin/glygl/create', params);
}

/**
 * @description 更新数据
 */
export function updateData(params: any) {
   return httpPost('/admin/glygl/update', params);
}

/**
 * @description 删除数据
 */
export function deleteData(params: { id: number }) {
   return httpPost('/admin/glygl/delete?id=' + params.id, params);
}

/**
 * @description 导出数据列表
 */
export function exportData() {
   return httpPost('/admin/glygl/export');
}
