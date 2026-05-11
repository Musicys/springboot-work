import { HttpGet, HttpPost } from '@/util/http';
// 前缀url
const url = '/user';

export const getJobList = (params: {
   pageNum?: number;
   pageSize?: number;
   jobType?: number;
   regionName?: string;
   workTimeType?: number;
   keyword?: string;
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
}) => {
   return HttpGet(`${url}/orders/list`, params);
};

export const getOrderDetail = (id: number) => {
   return HttpGet(`/job/order/detail/${id}`);
};

export const applyJob = data => {
   return HttpPost('/user/orders/apply', data);
};

export const cancelOrder = (id: number) => {
   return HttpPost(`/user/orders/cancel/${id}`, {});
};

export const confirmOrder = (orderId: number) => {
   return HttpPost('/user/orders/confirm', { orderId });
};

export const applyArbitration = (data: {
   orderId: number;
   userEvidenceSummary: string;
   userEvidenceImages?: string[];
}) => {
   return HttpPost('/user/arbitration/apply', data);
};

export const uploadFile = (
   filePath: string
): Promise<{ code: number; data: { url: string } }> => {
   return new Promise((resolve, reject) => {
      uni.uploadFile({
         url: `${import.meta.env.VITE_APP_BASE_API}/file/upload`,
         filePath: filePath,
         name: 'file',
         success: res => {
            try {
               const data = JSON.parse(res.data);
               resolve(data);
            } catch (e) {
               reject(e);
            }
         },
         fail: reject
      });
   });
};
