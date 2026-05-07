import { HttpGet } from '@/util/http';
// 前缀url
const url = '/user';

export const getJobList = (params: {
   pageNum?: number;
   pageSize?: number;
   jobType?: number;
   regionName?: string;
   workTimeType?: number;
}) => {
   return HttpGet(`${url}/job/list`, params);
};

export const getJobDetail = (id: number) => {
   return HttpGet(`${url}/job/detail/${id}`);
};

export const getOrderList = (params: {
   pageNum?: number;
   pageSize?: number;
   orderStatus?: number;
   keyword?: string;
}) => {
   return HttpGet('/job/order/list', params);
};

export const getOrderDetail = (id: number) => {
   return HttpGet(`/job/order/detail/${id}`);
};
