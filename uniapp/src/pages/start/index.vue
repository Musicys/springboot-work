<template>
   <view class="page">
      <view class="page-top">
         <image :src="LoginIMG" mode="widthFix"></image>
         <h1>趣友</h1>
         <text>这是个兴趣爱号的交友平台</text>
      </view>

      <view class="page-butom">
         <wd-button
            type="success"
            class="green"
            @click="goLoginform('/pages/login/index')">
            <wd-icon name="mail" class="!mr-1" size="16px"></wd-icon
            ><text class="text-[1.2em]">邮箱注册</text>
         </wd-button>

         <wd-button type="warning" class="word" @click="goLoginform(2)">
            <wd-icon name="mail" class="!mr-1" size="16px"></wd-icon
            ><text class="text-[1.2em]">账号登录</text>
         </wd-button>

         <view class="box">
            <label>
               <checkbox class="box-check" :checked="Isyuedu" @click="change" />
               <text>
                  我已经阅读趣友的
                  <text>《用户协议》</text>
                  和
                  <text>《隐私策略》</text>
               </text>
            </label>
         </view>
      </view>
   </view>
   <wd-action-sheet v-model="show" title="提示" @close="close">
      <view class="cart">
         <h1>请阅读以下条款</h1>
         <text>
            我已经阅读趣友的

            <text>《用户协议》</text>
            和
            <text>《隐私策略》</text>
         </text>

         <view class="but" @click="gohede"> 同意并继续 </view>
      </view>
   </wd-action-sheet>
</template>

<script lang="ts" setup>
import LoginIMG from '@/static/imgs/login.png';
import { useRouter } from 'uni-mini-router';
const router = useRouter();
const IslDing = ref(false);
const Isyuedu = ref(false);
const show = ref(false);
let selectpath = '';
onMounted(() => {});
const change = () => {
   Isyuedu.value = !Isyuedu.value;
};
const goLoginform = e => {
   if (Isyuedu.value) {
      router.push({ path: e });
   } else {
      show.value = true;
      selectpath = e;
   }
};

const gohede = () => {
   router.push({ path: selectpath });
};
</script>

<style lang="scss" scoped>
.cart {
   padding: 1me;
   width: 80%;
   margin: auto;

   display: flex;
   flex-direction: column;
   justify-content: space-between;
   padding-bottom: env(safe-area-inset-bottom); /* 底部安全区 */
   margin-bottom: 1em;
   h1 {
      font-size: 1.5em;
   }

   text {
      font-size: 0.8em;
   }

   .but {
      width: 100%;
      margin: auto;
      margin-top: 3em;
      border-radius: 15rpx;
      font-size: 0.8em;
      height: 3em;
      display: flex;
      justify-content: center;
      align-items: center;
      background: blue;
      border: 1rpx solid black;
      color: white;
   }
}

.page {
   display: flex;
   flex-direction: column;
   padding-top: var(--status-bar-height); /* 状态栏 */
   padding-bottom: env(safe-area-inset-bottom); /* 底部安全区 */
   width: 100vw;
   height: 100vh;
   justify-content: space-between;

   .page-top {
      display: flex;
      flex-direction: column;

      justify-content: start;
      align-self: center;

      image {
         width: 5em;

         text-align: center;
         margin: auto;
         margin-top: 5em;
      }

      h1 {
         margin-top: 0.5em;
         font-size: 1.8em;
         text-align: center;
      }

      text {
         margin-top: 1em;
         font-size: 0.8em;
         text-align: center;
         color: rgba(0, 0, 0, 0.6);
      }
   }

   .page-butom {
      .green {
         background: green;
         border: 1rpx solid black;
         width: 80%;
         margin: 1em auto;
         border-radius: 15rpx;
         font-size: 0.8em;
         height: 3em;
         width: 80%;

         display: flex;
         justify-content: center;

         align-items: center;

         & > text {
            margin-left: 0.5em;
         }
      }

      .word {
         border: 1rpx solid black;
         background: yellow;
         width: 80%;
         margin: 1em auto;
         border-radius: 15rpx;
         font-size: 0.8em;
         height: 3em;
         width: 80%;
         color: black;
         display: flex;
         justify-content: center;

         align-items: center;

         & > text {
            margin-left: 0.5em;
         }
      }

      .box {
         display: flex;
         justify-content: center;

         align-items: center;
         font-size: 0.6em;
         width: 80%;

         .box-check {
            transform: scale(0.7);

            & > text {
               color: rgba(0, 0, 0, 0.6);
            }
         }
      }
   }
}
</style>
