/**
 * @description 视频宽高初始化
 *
 */

import { VideoInfo } from '@/api/type';
const maxWidth = 300;
const maxHeight = 250;
export function initVideoWH(obj: VideoInfo): VideoInfo {
   const { width, height } = obj;

   let newWidth: number;
   let newHeight: number;

   if (height > width) {
      // 高度更大 → 以 maxHeight 为基准，等比缩放宽度
      const ratio = maxHeight / height;
      newWidth = width * ratio;
      newHeight = maxHeight;
   } else {
      // 宽度更大或相等 → 以 maxWidth 为基准，等比缩放高度
      const ratio = maxWidth / width;
      newWidth = maxWidth;
      newHeight = height * ratio;
   }
   return {
      ...obj,
      width: newWidth,
      height: newHeight
   };
}
