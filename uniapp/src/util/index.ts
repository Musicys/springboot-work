/**
 * @description 防抖
 * @param function, number
 * @returns Funtion
 */

export function debounce<T extends (...args: any[]) => any>(
   func: T,
   wait: number
): (...args: Parameters<T>) => void {
   let timeoutId: ReturnType<typeof setTimeout> | null = null;

   return function (this: ThisParameterType<T>, ...args: Parameters<T>) {
      // 清除上一次的定时器
      if (timeoutId) {
         clearTimeout(timeoutId);
      }

      // 重新设置定时器
      timeoutId = setTimeout(() => {
         func.apply(this, args);
         timeoutId = null;
      }, wait);
   };
}
/**
 * @description 节流
 * @param function
 * @param number
 * @returns Funtion
 */
export function throttle<T extends (...args: any[]) => any>(
   func: T,
   wait: number
): (...args: Parameters<T>) => void {
   let timeoutId: ReturnType<typeof setTimeout> | null = null;

   return function (this: ThisParameterType<T>, ...args: Parameters<T>) {
      if (!timeoutId) {
         timeoutId = setTimeout(() => {
            func.apply(this, args);
            timeoutId = null;
         }, wait);
      }
   };
}
/**
 * 计算两个经纬度之间的距离（单位：米）
 * @param {number} lat1 - 第一个点的纬度
 * @param {number} lng1 - 第一个点的经度
 * @param {number} lat2 - 第二个点的纬度
 * @param {number} lng2 - 第二个点的经度
 * @returns {number} 距离（米）
 */
export function getDistance(lat1, lng1, lat2, lng2) {
   const R = 6371000; // 地球半径，单位：米

   const dLat = ((lat2 - lat1) * Math.PI) / 180;
   const dLng = ((lng2 - lng1) * Math.PI) / 180;

   const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos((lat1 * Math.PI) / 180) *
         Math.cos((lat2 * Math.PI) / 180) *
         Math.sin(dLng / 2) *
         Math.sin(dLng / 2);

   const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

   return R * c; // 返回距离，单位：米
}

/**
 * 将小时转换为中文时段：凌晨、早上、上午、中午、下午、晚上
 */
function getPeriod(hour: number): string {
   if (hour >= 0 && hour <= 5) return '凌晨';
   if (hour >= 6 && hour <= 8) return '早上';
   if (hour >= 9 && hour <= 11) return '上午';
   if (hour === 12) return '中午';
   if (hour >= 13 && hour <= 17) return '下午';
   return '晚上'; // 18-23
}

/**
 * 格式化消息时间为微信风格（含历史消息的时间段）
 * - 今天：晚上 8:34
 * - 昨天：昨天 晚上 8:34
 * - 前天及以上：11-17 晚上 8:34（跨年：2024-12-01 晚上 8:34）
 */
export const formatMessageTime = (timeString?: string): string => {
   if (!timeString) return '';

   let messageTime: Date;

   try {
      // 修复 CST 时区歧义（假设为 UTC+8）
      if (typeof timeString === 'string' && /CST/.test(timeString)) {
         const corrected = timeString.replace(/\bCST\b/g, '+0800');
         messageTime = new Date(corrected);
         if (isNaN(messageTime.getTime())) {
            console.warn(`Fallback parsing for: ${timeString}`);
            messageTime = new Date(timeString);
         }
      } else {
         messageTime = new Date(timeString);
      }

      if (isNaN(messageTime.getTime())) {
         console.error('Invalid date:', timeString);
         return '';
      }
   } catch (error) {
      console.error('Parse error:', timeString, error);
      return '';
   }

   const now = new Date();

   // 获取日历日（本地时区）
   const getDateKey = (d: Date): string =>
      `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;

   const todayKey = getDateKey(now);
   const msgKey = getDateKey(messageTime);

   // 时间部分（中文时段 + 12小时制时间）
   const hour = messageTime.getHours();
   const minute = String(messageTime.getMinutes()).padStart(2, '0');
   const period = getPeriod(hour);
   const displayHour = hour % 12 === 0 ? 12 : hour % 12; // 1→1, 13→1, 0→12, 12→12
   const timePart = `${period} ${displayHour}:${minute}`;

   // 🟢 1. 今天
   if (msgKey === todayKey) {
      return timePart;
   }

   // 🟢 2. 昨天
   const yesterday = new Date(now);
   yesterday.setDate(now.getDate() - 1);
   const yesterdayKey = getDateKey(yesterday);
   if (msgKey === yesterdayKey) {
      return `昨天 ${timePart}`;
   }

   // 🟢 3. 前天及更早 → 日期 + 时间
   const month = String(messageTime.getMonth() + 1).padStart(2, '0');
   const day = String(messageTime.getDate()).padStart(2, '0');

   let datePart: string;
   if (messageTime.getFullYear() !== now.getFullYear()) {
      datePart = `${messageTime.getFullYear()}-${month}-${day}`;
   } else {
      datePart = `${month}-${day}`;
   }

   return `${datePart} ${timePart}`;
};

/**
 * 根据年龄判断所属年代群体（基于当前年份 2025）
 * @param {number} age - 当前年龄（周岁）
 * @returns {string} 所属代际，如 "80后"、"90后" 等；若不匹配则返回 "未知"
 */
export function getGeneration(age) {
   if (typeof age !== 'number' || age < 0 || !Number.isInteger(age)) {
      throw new Error('请输入一个有效的非负整数年龄');
   }

   const currentYear = 2025;
   const birthYear = currentYear - age;

   if (birthYear >= 1980 && birthYear <= 1989) {
      return '80后';
   } else if (birthYear >= 1990 && birthYear <= 1999) {
      return '90后';
   } else if (birthYear >= 2000 && birthYear <= 2004) {
      return '00后';
   } else if (birthYear >= 2005 && birthYear <= 2009) {
      return '05后';
   } else if (birthYear >= 2010 && birthYear <= 2019) {
      return '10后';
   } else {
      return '未知';
   }
}
