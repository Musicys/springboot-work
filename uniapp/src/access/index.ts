import router from '@/router';
import { useUserStore } from '@/store/user';
import { getLoginStatus } from '@/api/user/index';

const whiteList = [
   '/pages/login/index',
   '/pages/register/index',
   '/pages/init/index'
];

router.beforeEach((to, from, next) => {
   next(true);
});

router.afterEach((to, from) => {
   const userStore = useUserStore();

   if (whiteList.includes(to.path)) {
      return;
   }

   if (userStore.isLoggedIn && userStore.userInfo) {
      return;
   }
   console.log('蓝降价');
   uni.showLoading({
      title: '加载中...',
      mask: true
   });

   getLoginStatus()
      .then((res: any) => {
         console.log(res);

         uni.hideLoading();

         if (res.code === 0 && res.data) {
            userStore.setUserInfo(res.data);
            return;
         } else {
            uni.reLaunch({
               url: '/pages/login/index'
            });
         }
      })
      .catch(() => {
         uni.hideLoading();
         uni.reLaunch({
            url: '/pages/login/index'
         });
      });
});
