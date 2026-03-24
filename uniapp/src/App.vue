<script setup lang="ts">
import { useStore } from '@/store/user';
import { sockeStore } from './store/socke';
import { UserGetInfo, UserIsLogin } from '@/api/user';
import { useRouter } from 'uni-mini-router';

const router = useRouter();
const websocket = sockeStore();
const store = useStore();

onLaunch(async () => {
   console.log('开始自动登录');

   await store.autoLogin(
      //登录成功
      async () => {
         console.log('登录成功');

         let res = await UserGetInfo();

         if (res.code == 0) {
            store.setUserInfo(res.data);
            websocket.websocke(store.userInfo.id);

            setTimeout(() => {
               router.pushTab({ name: 'tabar' });
            }, 500);
         }
      },
      //登录失败
      () => {
         console.log('登录四班');

         router.replaceAll({
            name: 'start'
         });
         store.ColseToken();
      }
   );
});
onShow(() => {
   console.log('显示');
});
onHide(() => {});
</script>
<style lang="scss">
@import './uni.scss';
@import '@tuniao/tn-style/dist/uniapp/index.css';
</style>
