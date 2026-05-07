export const agecolumns = [
   10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
   29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
   48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66,
   67, 68
];
// 生成的100个符合现代审美的网络名称数组
const nicknames = [
   '星坠入梦',
   '雾隐青川',
   '云端漫步者',
   '风与诗的边界',
   '月光邮差',
   '半盏茶时',
   '银河便利店',
   '晚风告白',
   '心跳延迟0.5秒',
   '第7种情绪',
   '雪落无声境',
   '像素旅人',
   '时间褶皱里',
   '静音模式ON',
   '深海回声',
   '橘子汽水味',
   '凌晨三点灵感',
   '追光废片集',
   '情绪回收站',
   '玻璃晴朗',
   '蝉鸣未完待续',
   '浮世绘梦境',
   '呼吸暂停术',
   '星轨解码员',
   '虚焦浪漫',
   '沉默信号灯',
   '纸间宇宙',
   '逆向生长',
   '记忆缓存区',
   '低饱和度人生',
   '午夜放映厅',
   '情绪颗粒度',
   '无人知晓岛',
   '碎钻与静谧',
   '时间琥珀',
   '心跳采样器',
   '雾中楼宇',
   '静止旋转门',
   '透明存在感',
   '第25小时',
   '未命名情绪',
   '微光漫游者',
   '意识流沙',
   '数字幽灵',
   '冷色调叙事',
   '平行切片',
   '空白帧之间',
   '呼吸留白处',
   '镜面独白',
   '星屑收集者',
   '静音震动模式',
   '情绪滤镜',
   '城市倒影',
   '时间褶子',
   '低电量浪漫',
   '虚构现实主义',
   '透明心事',
   '渐变黄昏',
   '意识漂移',
   '无重力思绪',
   '静默潮汐',
   '像素心跳',
   '记忆显影液',
   '虚构岛屿志',
   '情绪断点续传',
   '光年外的晚安',
   '未读消息99+',
   '心跳波形图',
   '静止动画帧',
   '星语转译中',
   '空气质感',
   '意识缓存',
   '梦境缓加载',
   '情绪灰度值',
   '城市呼吸阀',
   '时间切片师',
   '虚拟共感体',
   '低频振动源',
   '静音播放中',
   '透明轨迹',
   '意识碎片整理',
   '心跳同步率',
   '星图未完成',
   '情绪渐变中',
   '记忆云备份',
   '虚构共鸣腔',
   '数字冥想室',
   '静止光年',
   '呼吸频率',
   '意识漫游模式'
];

/**
 * 返回一个随机的网络名称
 * @returns {string} 随机选取的昵称
 */
export function getRandomNickname() {
   const randomIndex = Math.floor(Math.random() * nicknames.length);
   return nicknames[randomIndex];
}

/**
 * 将base64编码的字符串转换为File对象
 * @param base64 base64编码的字符串
 * @param filename 文件名
 * @param mimeType 文件类型
 * @returns {File} File对象
 */
export function base64ToFile(base64, filename, mimeType) {
   // 去除 base64 头部信息（如 data:image/png;base64,）
   const arr = base64.split(',');
   const matchResult = arr[0].match(/:(.*?);/);
   const mime = (matchResult && matchResult[1]) || mimeType;

   // 解码 base64 内容
   const bstr = atob(arr[1]);

   // 创建二进制数组
   let n = bstr.length;
   const u8arr = new Uint8Array(n);
   while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
   }

   // 创建 Blob 或 File
   const file = new File([u8arr], filename, { type: mime });
   return file;
}
//随机头像
export function getRandomAvatar() {
   return new Promise((resolve, reject) => {
      uni.request({
         url: 'https://v2.xxapi.cn/api/head',
         method: 'GET',
         success: res => {
            resolve(res.data);
         }
      });
   });
}
