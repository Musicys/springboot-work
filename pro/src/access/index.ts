// import { getAdminInfo } from '@/api/user';
import router from '@/router/index';

// 拦截器
router.beforeEach(async (to, from, next) => {
   return next();
   if (to.path == '/login' || to.path == '/register') {
      return next();
   }
});

// 后置设置标题
router.afterEach(to => {
   document.title = (to.meta.tabConfig as any)?.name || '登录管理系统';
});
