export interface DynamicLoveVo {
   /**
    * 用户id
    */
   userId: number | null;

   /**
    * 用户名
    */
   username: string | null;

   /**
    * 用户头像
    */
   avatarUrl: string | null;

   /**
    * 动态id
    */
   dynamicId: number | null;

   /**
    * 动态内容
    */
   content: string | null;

   /**
    * 动态图片数组 (注: Java字段名为imgarr, 可能为图片URL数组)
    */
   imgarr: string | null;

   /**
    * 点赞时间
    */
   createTime: string | null;
}

export interface DynamicCommentVo {
   /**
    * 用户id
    */
   userId: number | null;

   /**
    * 用户名
    */
   username: string | null;

   /**
    * 用户头像
    */
   avatarUrl: string | null;

   /**
    * 评论创建时间
    */
   createTime: string | null;

   /**
    * 动态id (被评论的动态)
    */
   dynamicId: number | null;

   /**
    * 动态内容 (被评论的动态原文)
    */
   dynamicContext: string | null;

   /**
    * 动态图片数组 (被评论的动态的图片，JSON字符串)
    */
   dynamicImgarr: string | null;

   /**
    * 评论内容
    */
   commentsContext: string | null;

   /**
    * 评论图片数组 (评论者上传的图片，JSON字符串，可能为空)
    */
   commentsImgarr: string | null;
}
