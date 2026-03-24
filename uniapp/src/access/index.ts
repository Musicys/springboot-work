import router from '@/router';
import { UserIsLogin } from '@/api/user';
import { useStore } from '@/store/user';
import router from '@/router';
router.beforeEach((to, from, next) => {
   // next入参 false 以取消导航

   next(true);
});
router.afterEach((to, from) => {
   if (
      to.fullPath == '/pages/login/index' ||
      to.fullPath == '/pages/tabar/mine/userlst/index' ||
      to.fullPath == '/'
   ) {
      return;
   }
   UserIsLogin().then(res => {
      if (res.code == 0) {
         //
      } else {
         router.replaceAll({
            path: '/pages/login/index'
         });
      }
   });

   // console.log(to);
   // console.log(from);
});
