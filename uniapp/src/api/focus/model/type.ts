/**
 * 关注用户信息视图对象 (VO)
 */
export interface FocusUserVo {
   /**
    * 关注表ID
    */
   id: number | null;

   /**
    * 关注表的创建时间
    */
   createTime: string | null; // ISO 8601 格式字符串，如 "2025-10-22T19:30:00Z"

   /**
    * 被关注的用户ID（或当前查询的关注者ID）
    */
   userId: number | null;

   /**
    * 用户昵称
    */
   username: string | null;

   /**
    * 用户头像 URL
    */
   avatarUrl: string | null;

   /**
    * 用户年龄
    */
   age: number | null;

   /**
    * 用户标签（JSON 字符串数组）
    */
   tags: string | null;

   /**
    * 在线状态：0 - 不在线，1 - 在线
    */
   login: number | null;

   /**
    * 性别：0 - 女，1 - 男，其他可扩展
    */
   gender: number | null;

   /**
    * 个人简介
    */
   introductory: string | null;
}
