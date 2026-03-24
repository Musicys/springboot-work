export const MESSAGE_TYPE = {
   TEXT: 'text',
   IMAGE: 'image',
   VOICE: 'voice',
   AUDIO: 'audio',
   FILE: 'file',
   VIDEO: 'video'
};
export const GetMessageContent = (obj: any) => {
   try {
      const msg = JSON.parse(obj);
      if (msg.type === MESSAGE_TYPE.TEXT) {
         return msg.content;
      }
      if (msg.type === MESSAGE_TYPE.IMAGE) {
         return '[图片]';
      }
      if (msg.type === MESSAGE_TYPE.VOICE) {
         return '[语音]';
      }
      if (msg.type === MESSAGE_TYPE.VIDEO) {
         return '[视频]';
      }
      if (msg.type === MESSAGE_TYPE.AUDIO) {
         return '[音频]';
      }
      if (msg.type === MESSAGE_TYPE.FILE) {
         return '[文件]';
      }
   } catch (error) {
      return '未知消息类型';
   }
};

export const getTEXT = (str: string) => {
   return JSON.stringify({
      type: MESSAGE_TYPE.TEXT,
      content: str
   });
};
const maxWidth = 150;
const maxHeight = 250;

export const getIMAGE = (url: object) => {
   const { width, height, url: imageUrl } = url;

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

   return JSON.stringify({
      type: MESSAGE_TYPE.IMAGE,
      content: {
         width: newWidth || 150,
         height: newHeight || 150,
         url: imageUrl
      }
   });
};

export const getVOICE = (url: object) => {
   const { width, height } = url;

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
   return JSON.stringify({
      type: MESSAGE_TYPE.VIDEO,
      content: {
         url: url.filePath,
         cover: url.cover,
         width: newWidth || 150,
         height: newHeight || 150
      }
   });
};
export const getAUDIO = (url: string) => {
   return JSON.stringify({
      type: MESSAGE_TYPE.AUDIO,
      content: url
   });
};

export const getFILE = (url: string) => {
   return JSON.stringify({
      type: MESSAGE_TYPE.FILE,
      content: url
   });
};
