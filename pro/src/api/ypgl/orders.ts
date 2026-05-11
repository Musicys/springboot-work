import { httpGet } from '@/util/http';

export const getOrders = (params?: any) => httpGet('/pro/job/orders/list', params);
