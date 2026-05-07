import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface WlWallets {
   userId: string;
   balance: number;
   frozenBalance: number;
   totalIncome: number;
   version: number;
   updatedAt: string;
}

export interface UserInfo {
   userId: number;
   username: string;
   token: string | null;
   userType: number;
   avatarUrl: string;
   userRote: number | null;
   studentId: string;
   realName: string;
   age: number;
   gender: number;
   phone: string;
   profession: string;
   introduction: string;
   creditScore: number;
   tagName: string;
   wlWallets: WlWallets | null;
}

export const useUserStore = defineStore(
   'user',
   () => {
      const userInfo = ref<UserInfo | null>(null);
      const isLoggedIn = ref(false);

      const setUserInfo = (info: UserInfo) => {
         userInfo.value = info;
         isLoggedIn.value = true;
      };

      const clearUserInfo = () => {
         userInfo.value = null;
         isLoggedIn.value = false;
      };

      return {
         userInfo,
         isLoggedIn,
         setUserInfo,
         clearUserInfo
      };
   },
   {
      unistorage: true
   }
);
