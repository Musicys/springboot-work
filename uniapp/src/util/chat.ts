/**
 * 判断是否应显示时间戳（输入为你提供的字符串格式）
 * @param {string} currentTime - 如 "Mon Nov 17 22:25:03 CST 2025"
 * @param {string | null | undefined} lastTime - 上一条时间字符串，没有则传 null/undefined
 * @returns {boolean}
 */
export function shouldShowTime(currentTime, lastTime) {
   // 第一条消息一定显示
   if (!lastTime) {
      return true;
   }

   const current = new Date(currentTime);
   const last = new Date(lastTime);

   // 跨天判断（比较年月日）
   const isSameDay =
      current.getFullYear() === last.getFullYear() &&
      current.getMonth() === last.getMonth() &&
      current.getDate() === last.getDate();

   if (!isSameDay) {
      return true;
   }

   // 时间差 ≥ 5 分钟？
   const diffMs = current.getTime() - last.getTime();
   return diffMs >= 5 * 60 * 1000; // 5分钟 = 300000 毫秒
}
