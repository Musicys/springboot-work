import service from './request';

/**
 *
 * @param url 请求地址
 * @param params 请求参数
 * @returns Promise
 */
export const HttpGet = (url: string, params: any) => {
   return service.get(url, { params });
};
/**
 *
 * @param url 请求地址
 * @param data 请求数据
 * @param File 是否为文件上传
 * @returns Promise
 */

export const HttpPost = (url: string, data: any, File: boolean = false) => {
   if (File) {
      return service.post(url, data, {
         header: {
            'Content-Type': 'multipart/form-data'
            // ces: 123
         }
      });
   }

   return service.post(url, data);
};
/**
 *
 * @param url 请求地址
 * @param data 请求数据
 * @returns Promise
 */

export const HttpPut = (url: string, data: any) => {
   return service.put(url, data);
};
/**
 *
 * @param url 请求地址
 * @param data 请求数据
 * @returns Promise
 */

export const HttpDelete = (url: string, params: any) => {
   return service.delete(url, { params });
};
