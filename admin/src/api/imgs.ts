import { httpGet, httpPost, httpDelete } from '@/util/http';

export interface ListImgRequest {
   current: number;
   pageSize: number;
   title?: string;
   tagId?: number;
}

export interface Img {
   id?: number;
   delname: string;
   url: string;
   title: string;
   size: number;
   createTime?: string;
}

export interface PageResult<T> {
   records: T[];
   total: number;
   current: number;
   pageSize: number;
}

export interface ListResponse {
   code: number;
   data: PageResult<Img>;
   message?: string;
}

export interface UploadResponse {
   code: number;
   data: {
      id: number;
      url: string;
      fileName: string;
      originalName: string;
      markdown: string;
   };
   message?: string;
}

export const ImgsList = async (params: ListImgRequest): Promise<ListResponse> => {
   const response = await httpGet('/file/list', { params });
   return response;
};

export const ImgUpload = async (formData: FormData): Promise<UploadResponse> => {
   const response = await httpPost('/file/upload', formData, true);
   return response;
};

export const ImgDel = async (delnames: string[]): Promise<{ code: number; message?: string }> => {
   const response = await httpDelete('/file/delete', delnames);
   return response;
};
