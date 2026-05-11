import { httpPost, httpGet } from '@/util/http';

/**
 * @description 获取仲裁案件列表
 */
export function getList(params: { pageNum: number; pageSize: number; id?: number; orderId?: number; status?: number }) {
   return httpGet('/admin/arbitration/list', params);
}

/**
 * @description 获取仲裁案件详情
 */
export function getDetail(params: { id: number }) {
   return httpGet('/admin/arbitration/detail', params);
}

/**
 * @description 结案处理
 */
export function closeCase(params: {
   id: number;
   rulingResult?: number;
   faultParty?: number;
   deductedScore?: number;
   adminComment?: string;
   finalSettlementAmount?: number;
}) {
   return httpPost(`/admin/arbitration/close/${params.id}`, params);
}
