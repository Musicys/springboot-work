import { httpPost } from '@/util/http';

/**
 * @description 获取订单列表
 */
export function getList(params: any) {
   return httpPost('/admin/yydd/list', params);
}

/**
 * @description 获取订单详情
 */
export function getDetail(params: { id: number }) {
   return httpPost('/admin/yydd/detail', params);
}

/**
 * @description 更新订单
 */
export function updateData(params: any) {
   return httpPost('/admin/yydd/update', params);
}

/**
 * @description 删除订单
 */
export function deleteData(params: { id: number }) {
   return httpPost('/admin/yydd/delete', params);
}

/**
 * @description 导出订单列表
 */
export function exportData() {
   return httpPost('/admin/yydd/export');
}

/**
 * @description 退押金
 */
export function refundDeposit(params: { id: number }) {
   return httpPost('/admin/yydd/refundDeposit', params);
}

/**
 * @description 订单结款
 */
export function settleOrder(params: { id: number }) {
   return httpPost('/admin/yydd/settleOrder', params);
}

/**
 * @description 订单退款
 */
export function refundOrder(params: { id: number }) {
   return httpPost('/admin/yydd/refundOrder', params);
}
