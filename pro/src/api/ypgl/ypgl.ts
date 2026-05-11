import { httpPost, httpGet } from '@/util/http';

/**
 * @description 获取所有进行中兼职的应聘列表
 */
export function getAllApplications() {
   return httpGet('/pro/job/applications/all', {});
}

/**
 * @description 获取待入职的应聘列表
 */
export function getPendingEntryApplications() {
   return httpGet('/pro/job/applications/pending-entry', {});
}

/**
 * @description 获取进行中的兼职岗位列表
 */
export function getActiveJobs(params: any) {
   return httpPost('/pro/job/list/active', params);
}

/**
 * @description 获取兼职的应聘列表
 */
export function getJobApplications(jobId: number) {
   return httpGet('/pro/job/applications/' + jobId, {});
}

/**
 * @description 同意应聘
 */
export function approveApplication(params: { orderId: number; freezeAmount: number }) {
   return httpPost('/pro/job/approveApplication', params);
}
