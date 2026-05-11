import { httpPost, httpGet } from '@/util/http';

export interface TransactionLogsQuery {
   pageNum: number;
   pageSize: number;
   keyword?: string;
   userId?: number;
   type?: string;
   relatedOrderId?: number;
}

export function getTransactionList(params: TransactionLogsQuery) {
   return httpPost('/pro/transaction/list', params);
}

export function getTransactionDetail(params: { id: number }) {
   return httpGet('/pro/transaction/get/' + params.id, null);
}
