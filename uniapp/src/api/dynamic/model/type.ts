export interface Dynamic {
   email: string;
   id: number;
   context: string;
   lat: number;
   lng: number;
   imgarr: string[]; // 用户相册
   distance: number;
   formatted: string;
   count: number; // 评论数
   love: number; // 点赞数
   userId: number;
   userAccount: string;
   username: string; // 用户名
   gender: number; // 1 表示男性，0 表示女性，或其他约定
   age: number; // 年龄
   avatarUrl: string; // 头像 URL
   isLove: number; // 0 或 1，表示是否点赞
   updatetime: string; // ISO 8601 格式的时间字符串
   createTime: string; // ISO 8601 格式的时间字符串
   login: number; // 登录状态，0 表示未登录，1 表示已登录
   province: string | null;
   district: string | null;
   isFocus: '0' | '1'; // 是否关注
   isPictures: number; // 0 表示文本动态，1 表示视频动态
   video: string;
}
