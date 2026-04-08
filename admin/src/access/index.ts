// import { getAdminInfo } from '@/api/user';
import router from '@/router/index';

// 检查cookie是否存在
function checkCookie(name: string): boolean {
   const cookieValue = document.cookie
      .split('; ')
      .find(row => row.startsWith(name + '='))
      ?.split('=')[1];

   return !!cookieValue;
}
// 拦截器
router.beforeEach(async (to, from, next) => {
   return next();
   if (to.path == '/login') {
      return next();
   }

   // 检查Authorization cookie是否存在
   console.log('Authorization cookie:', checkCookie('Authorization'));
   if (!checkCookie('Authorization')) {
      return next({ path: '/login' });
   }

   // 如果cookie存在，直接放行
   return next();
});

// 后置设置标题
router.afterEach(to => {
   document.title = (to.meta.tabConfig as any)?.name || '登录管理系统';
});
