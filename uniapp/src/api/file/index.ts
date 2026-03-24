import { baseURL } from '@/util/system/CONFIG';
import { HttpPost } from '@/util/http';
import { configs } from '@/util/request';

//条件编译
//#ifdef H5
export const updateOssFile = (File: string) => {
   console.log(baseURL);

   return uni.uploadFile({
      url: '/api/api/file',
      filePath: File,
      name: 'file',
      formData: {
         tagId: 9
      }
   });
};
export const ChatupdateOssFile = (File: string) => {
   return uni.uploadFile({
      url: '/api/api/file/chat',
      filePath: File,
      name: 'file',
      formData: {
         tagId: 9
      }
   });
};
//  @RequestParam("file") MultipartFile file,
//             @RequestParam(value = "tagId", required = false) Long tagId,
//             @RequestParam(value = "width", required = false) Integer width,
//             @RequestParam(value = "height", required = false) Integer height,
//             @RequestParam(value = "durationSeconds", required = false) Integer durationSeconds,
//             @RequestParam(value = "cover", required = false) String cover,
export const updateFileVideo = (obj: {
   file: string;
   tagId: number;
   width: number;
   height: number;
   durationSeconds: number;
   cover: string;
}) => {
   return uni.uploadFile({
      url: '/api/api/file/chatvideo',
      filePath: obj.file,
      name: 'file',
      formData: {
         tagId: obj.tagId,
         width: obj.width,
         height: obj.height,
         durationSeconds: obj.durationSeconds,
         cover: obj.cover
      }
   });
};

//#endif

//#ifdef APP-PLUS
export const updateOssFile = (File: string) => {
   console.log(baseURL);
   return uni.uploadFile({
      url: baseURL + '/api/file',
      filePath: File,
      name: 'file',
      header: {
         'Content-Type': 'multipart/form-data',
         Cookie: configs.cookie
      },
      formData: {
         tagId: 9
      }
   });
};
export const ChatupdateOssFile = (File: string) => {
   return uni.uploadFile({
      url: baseURL + '/api/file/chat',
      filePath: File,
      name: 'file',
      header: {
         'Content-Type': 'multipart/form-data',
         Cookie: configs.cookie
      },
      formData: {
         tagId: 9
      }
   });
};
export const updateFileVideo = (obj: {
   file: string;
   tagId: number;
   width: number;
   height: number;
   durationSeconds: number;
   cover: string;
}) => {
   return uni.uploadFile({
      url: baseURL + '/api/file/chatvideo',
      filePath: obj.file,
      name: 'file',
      header: {
         'Content-Type': 'multipart/form-data',
         Cookie: configs.cookie
      },
      formData: {
         tagId: obj.tagId,
         width: obj.width,
         height: obj.height,
         durationSeconds: obj.durationSeconds,
         cover: obj.cover
      }
   });
};
//#endif
