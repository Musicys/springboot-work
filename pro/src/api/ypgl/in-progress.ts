import { httpPost, httpGet } from '@/util/http';

export const getInProgressOrders = () => {
   return httpGet('/pro/job/orders/in-progress', {});
};

export const settleOrder = (data: { orderId: number }) => {
   return httpPost('/pro/job/orders/settle', data);
};

export const refundDeposit = (data: { orderId: number }) => {
   return httpPost('/pro/job/orders/refund', data);
};

export const applyDispute = (data: { orderId: number }) => {
   return httpPost('/pro/job/orders/dispute', data);
};
