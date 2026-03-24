import { defineStore } from 'pinia';
import { ref } from 'vue';
import { User } from '@/api/user/model/type';
import { UserLogin } from '@/api/user';
import { UserUpdateLongitude } from '@/api/user';
import router from '@/router';

export const useStore = defineStore(
   'user',
   () => {
      const userInfo = ref<User>(null);
      const Tokens = ref<User[]>([]);
      const location = ref<{
         lat: number;
         lng: number;
      }>({
         lat: 0,
         lng: 0
      }); //经纬度
      //设置用户位置
      const setLocation = () => {
         uni.getLocation({
            type: 'wgs84',
            success: function (res) {
               location.value.lat = res.latitude;
               location.value.lng = res.longitude;

               UserUpdateLongitude({
                  lat: res.latitude,
                  lng: res.longitude
               }).then(res => {
                  if (res.code == 0) {
                     //更新成功
                     setUserInfo(res.data);
                  }
               });
            },
            fail: function (res) {
               console.log('获取位置失败', res);
            }
         });
      };
      //设置用户信息
      const setUserInfo = (val: User) => {
         console.log('设置用户信息', val);

         userInfo.value = val;
         location.value.lat = userInfo.value.lat;
         location.value.lnt = userInfo.value.lnt;
      };
      //保持用户密码
      const setTokens = (val: User) => {
         let def = true;
         Tokens.value = Tokens.value.map(item => {
            if (item.id === val.id) {
               console.log('YONGH,', 'GENX1');

               item = {
                  ...item,
                  ...val,
                  is_default: 1
               };
               def = false;
            } else {
               item.is_default = 0;
            }

            return item;
         });
         if (def) {
            Tokens.value.push(val);
         }
      };

      const ColseToken = () => {
         Tokens.value = [];
      };
      //自动登录
      const autoLogin = (
         fun: () => Void = () => {},
         err: () => Void = () => {}
      ) => {
         const defaultToken = Tokens.value.find(item => item.is_default === 1);
         if (defaultToken) {
            UserLogin(defaultToken.tokens).then(res => {
               // setUserInfo(res.data);
               console.log(res);

               if (res.code == 0) {
                  fun();
               } else {
                  err();
               }
            });
         } else {
            err();
         }
      };

      const ColseTokenDefale = () => {
         Tokens.value = Tokens.value.map(item => {
            item.is_default = 0;
            console.log(item);

            return item;
         });
      };

      //返回修改用户信息字段
      const getEiditUser = (data: {
         age?: number;
         avatarUrl?: string;
         gender?: number;
         imagsarr?: string;
         introductory?: string;
         phone?: string;
         tags?: string;
         username?: string;
      }) => {
         const obj = {
            age: userInfo.value.age,
            avatarUrl: userInfo.value.avatarUrl,
            gender: userInfo.value.gender,
            id: userInfo.value.id,
            imagsarr: userInfo.value.imagsarr,
            introductory: userInfo.value.introductory,
            phone: userInfo.value.phone,
            tags: userInfo.value.tags,
            username: userInfo.value.username
         };
         Object.keys(data).forEach(key => {
            obj[key] = data[key];
         });
         return obj;
      };

      return {
         userInfo,
         setUserInfo,
         Tokens,
         setTokens,
         autoLogin,
         location,
         setLocation,
         ColseToken,
         getEiditUser,
         ColseTokenDefale
      };
   },
   {
      unistorage: true // 开启后对 state 的数据读写都将持久化
   }
);
