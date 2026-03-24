<template>
   <view class="wrapper" :style="colorStyle">
      <view class="page-msg">
         <view class="title">
            {{ currentTabIndex === 0 ? '密码登录' : '验证码注册' }}
         </view>
         <view class="tip"> 首次登录会自动注册 </view>
      </view>
      <view class="page-form">
         <view class="animate-fadeIn">
            <!-- 使用TnSwitchTab组件实现登录方式切换 -->
            <tn-switch-tab
               v-model="currentTabIndex"
               active-bg-color
               active-text-color="#0AD9EE"
               inactive-bg-color="#D1E7FE"
               :tabs="tabs">
               <view class="centext">
                  <!-- 密码登录表单 -->
                  <view v-if="currentTabIndex === 0" class="temp">
                     <!-- 登录表单 -->
                     <tn-form-item
                        label="账号/邮箱"
                        label-position="top"
                        class="item">
                        <tn-input
                           type="text"
                           placeholder="请输入账号或邮箱"
                           placeholder-class="placeholder"
                           v-model="loginForm.userAccount"
                           :adjust-position="false" />
                     </tn-form-item>
                     <tn-form-item
                        label="密码"
                        label-position="top"
                        class="item">
                        <tn-input
                           type="password"
                           placeholder="请输入密码"
                           placeholder-class="placeholder"
                           v-model="loginForm.userPassword"
                           :adjust-position="false" />
                     </tn-form-item>
                  </view>

                  <!-- 验证码注册表单 -->
                  <view v-if="currentTabIndex === 1" class="temp">
                     <tn-form-item
                        label="邮箱"
                        label-position="top"
                        class="item">
                        <tn-input
                           type="text"
                           placeholder="请输入邮箱"
                           placeholder-class="placeholder"
                           v-model="registerForm.userAccount"
                           :adjust-position="false" />
                     </tn-form-item>
                     <!-- 密码输入区域 -->
                     <tn-form-item
                        label="密码"
                        label-position="top"
                        class="item">
                        <tn-input
                           type="password"
                           placeholder="请登录密码"
                           placeholder-class="placeholder"
                           v-model="registerForm.userPassword"
                           :adjust-position="false" />
                     </tn-form-item>
                     <tn-form-item
                        label="确认密码"
                        label-position="top"
                        class="item">
                        <tn-input
                           type="password"
                           placeholder="请确认密码"
                           placeholder-class="placeholder"
                           v-model="registerForm.checkPassword"
                           :adjust-position="false" />
                     </tn-form-item>
                     <tn-form-item
                        label="验证码"
                        label-position="top"
                        class="item acea-row row-between-wrapper">
                        <tn-input
                           type="number"
                           placeholder="请输入验证码"
                           placeholder-class="placeholder"
                           :maxlength="6"
                           class="codetn-input"
                           v-model="registerForm.planetCode"
                           :adjust-position="false" />
                        <view class="line"></view>
                        <button
                           class="code !text-[0.8em]"
                           :class="countdown > 0 ? 'on' : ''"
                           :disabled="countdown > 0"
                           @click="sendVerificationCode()">
                           {{
                              countdown > 0
                                 ? `${countdown}秒后重发`
                                 : '获取验证码'
                           }}
                        </button>
                     </tn-form-item>
                  </view>

                  <!-- <view class="btn" @click="handleLogin"> 登录 </view> -->
                  <wd-button
                     @click="submitForm"
                     class="w-full"
                     type="success"
                     round="15">
                     {{ currentTabIndex === 0 ? '登录' : '注册' }}
                  </wd-button>
                  <wd-button
                     v-if="tokens.length > 0"
                     @click="router.push({ name: 'userlst' })"
                     class="w-full"
                     round="15"
                     style="margin-top: 20rpx"
                     type="error">
                     快捷登录
                  </wd-button>
               </view>
            </tn-switch-tab>
         </view>
      </view>
   </view>
</template>

<script setup lang="ts">
import bg from '@/static/imgs/bg.png';
import { useRouter } from 'uni-mini-router';
import { useStore } from '@/store/user';
import { UserLogin, UserGetVerifyCode, UserRegister } from '@/api/user';
import { sockeStore } from '@/store/socke';
import { throttle } from '@/util';
const webstore = sockeStore();
const router = useRouter();
const store = useStore();
const tokens = ref(store.Tokens);
// 登录表单数据
const loginForm = reactive({
   userAccount: '',
   userPassword: ''
});
// 验证码倒计时
const countdown = ref(0);
let countdownTimer: number | null = null;

// 注册表单数据
const registerForm = reactive({
   checkPassword: '',
   planetCode: '',
   userAccount: '',
   userPassword: ''
});
// 新增的辅助变量和方法
const current = ref(false);
const pageTitle = ref('账号登录');
const isHome = ref(false);
const colorStyle = ref({
   '--view-theme': '#0BD9EE'
});
// 发送验证码
type SendType = 'login' | 'register';
// 当前选中的标签索引
const currentTabIndex = ref(0);
type LoginType = 'password' | 'verificationCode';
const loginMethod = ref<LoginType>('password');
// 标签列表
const tabs = ['密码登录', '验证码注册'];

const sendVerificationCode = throttle(async () => {
   if (!registerForm.userAccount) {
      uni.showToast({
         title: '请先输入账号',
         icon: 'none'
      });
      //判断是否是邮箱格式

      return;
   }
   //判断是否是邮箱格式
   if (
      !/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(
         registerForm.userAccount
      )
   ) {
      uni.showToast({
         title: '请输入正确的邮箱格式',
         icon: 'none'
      });
      return;
   }

   console.log('发送种什么');

   // 在实际应用中，这里应该调用API发送验证码
   uni.showLoading({
      title: '发送中',
      mask: true
   });
   let res = await UserGetVerifyCode({ email: registerForm.userAccount });

   if (res.code === 0) {
      uni.hideLoading();
      uni.showToast({
         title: '验证码已发送',
         icon: 'none'
      });
      startCountdown();
   }
}, 500);
// 开始倒计时
const startCountdown = () => {
   countdown.value = 60;
   if (countdownTimer) clearInterval(countdownTimer);
   countdownTimer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
         if (countdownTimer) clearInterval(countdownTimer);
         countdownTimer = null;
      }
   }, 1000) as unknown as number;
};

// 表单验证
const validateForm = (type: 'login' | 'register'): boolean => {
   if (type === 'login') {
      if (!loginForm.userAccount) {
         uni.showToast({
            title: '请输入账号',
            icon: 'none'
         });
         return false;
      }

      // 根据登录方式进行不同的验证
      if (loginMethod.value === 'password') {
         if (!loginForm.userPassword) {
            uni.showToast({
               title: '请输入密码',
               icon: 'none'
            });
            return false;
         }
      } else {
         if (!loginForm.planetCode) {
            uni.showToast({
               title: '请输入验证码',
               icon: 'none'
            });
            return false;
         }
      }
   } else {
      if (!registerForm.userAccount) {
         uni.showToast({
            title: '请设置账号',
            icon: 'none'
         });
         return false;
      }
      if (!registerForm.userPassword) {
         uni.showToast({
            title: '请设置密码',
            icon: 'none'
         });
         return false;
      }
      if (registerForm.userPassword.length < 8) {
         uni.showToast({
            title: '密码长度不能小于8位',
            icon: 'none'
         });
         return false;
      }
      if (registerForm.userPassword !== registerForm.checkPassword) {
         uni.showToast({
            title: '两次密码输入不一致',
            icon: 'none'
         });
         return false;
      }
      if (!registerForm.planetCode) {
         uni.showToast({
            title: '请输入验证码',
            icon: 'none'
         });
         return false;
      }
   }
   return true;
};

// 处理登录
const handleLogin = () => {
   if (!validateForm('login')) return;

   uni.showLoading({
      title: '登录中',
      mask: true
   });
   try {
      UserLogin(loginForm).then(res => {
         if (res.code == 0) {
            store.setUserInfo(res.data);
            store.setLocation();
            store.setTokens({ ...res.data, tokens: loginForm, is_default: 1 });
            if (webstore.$state.SocketTask) {
               webstore.$state.SocketTask.close(); //关闭连接
            }
            setTimeout(() => {
               webstore.websocke(res.data.id);
            }, 1000);

            uni.hideLoading();
            uni.showToast({
               title: '登录成功',
               icon: 'none',
               duration: 1000,
               success: () => {
                  // 延迟跳转防止提示被覆盖
                  setTimeout(() => {
                     router.pushTab({ name: 'tabar' });
                  }, 1500);
               }
            });
         } else {
            uni.hideLoading();
         }
      });
   } catch (error) {
      uni.hideLoading();
      uni.showToast({
         title: '登录失败,系统错误',
         icon: 'none'
      });
   }
};

// 处理注册
const handleRegister = async () => {
   if (!validateForm('register')) return;

   // 模拟注册成功

   let res = await UserRegister(registerForm);

   if (res.code == 0) {
      uni.showToast({
         title: '注册成功',
         icon: 'none',
         duration: 1500,
         success: () => {
            // 延迟跳转防止提示被覆盖
            store.setUserInfo(res.data);
            store.setTokens({
               ...res.data,
               tokens: {
                  userAccount: registerForm.userAccount,
                  userPassword: registerForm.userPassword
               },
               is_default: 1
            });
            if (webstore.$state.SocketTask) {
               webstore.$state.SocketTask.close(); //关闭连接
            }
            setTimeout(() => {
               webstore.websocke(res.data.id);
            }, 1000);
            setTimeout(() => {
               router.push({ name: 'personal' });
            }, 1500);
         }
      });
   } else {
      uni.showToast({
         title: res.message,
         icon: 'none'
      });
   }
   // store.setUserInfo(registerForm.userAccount);

   // // 显示注册成功提示
};

const submitForm = () => {
   if (currentTabIndex.value === 0) {
      handleLogin();
   } else {
      handleRegister();
   }
};

const back = () => {
   router.back();
};
</script>

<style lang="scss" scoped>
// 淡入动画
@keyframes fadeIn {
   from {
      opacity: 0;
      transform: translateY(10px);
   }
   to {
      opacity: 1;
      transform: translateY(0);
   }
}

// 抖动动画（补充原缺失的动画定义）
@keyframes shake {
   0%,
   100% {
      transform: translateX(0);
   }
   20% {
      transform: translateX(-6rpx);
   }
   40% {
      transform: translateX(6rpx);
   }
   60% {
      transform: translateX(-4rpx);
   }
   80% {
      transform: translateX(4rpx);
   }
}

// 复选框样式（修复深度选择器语法）
::v-deep checkbox .wx-checkbox-tn-input.wx-checkbox-tn-input-checked {
   border: 1px solid var(--view-theme) !important;
   background-color: var(--view-theme) !important;
   width: 28rpx;
   height: 28rpx;
   font-size: 24rpx;
   color: #fff !important;
   display: flex;
   align-items: center;
   justify-content: center;
}

// 页面容器
.wrapper {
   background: linear-gradient(140deg, #9dd3ff, #ecf1f9);
   height: 100vh;
   width: 100vw;
   position: relative;
   display: flex;
   flex-direction: column;
   justify-content: flex-end;
}

// 背景图区域
.bag {
   position: absolute;
   top: var(--status-bar-height);
   left: 0;
   width: 100%;
   bottom: 0;
   height: 100vw;
   z-index: 0;
}

// 页面信息区
.page-msg {
   padding-top: 160rpx;
   margin-left: 72rpx;
   z-index: 2;
   position: relative;
   max-width: calc(100% - 144rpx);

   .title {
      font-size: 48rpx;
      font-weight: 500;
      color: #333;
      line-height: 68rpx;
      font-weight: bold;
      margin: 0 0 16rpx 0;
   }

   .tip {
      font-size: 28rpx;
      font-weight: 400;
      color: #666;
      line-height: 40rpx;
      margin: 0;
   }
}

// TnSwitchTab 组件样式定制
:deep(.tn-switch-tab) {
   margin-bottom: 32rpx;
}

// 定制选中和未选中状态的颜色
:deep(.tn-switch-tab__tab) {
   &.tn-switch-tab__tab--active {
      background-color: var(--view-theme);
      color: #fff;
   }
}

:deep() {
   .wd-button {
      border-radius: 0 !important;
   }
   .tn-form-item__label {
      color: #9c9c9c;
   }
}
.tn-input--input {
   background: #f3f9fe !important;
}
// 表单区域
.page-form {
   box-sizing: border-box;
   width: 90%;

   margin: 100rpx auto 0;
   z-index: 2;
   position: relative;

   .item {
      margin: 20px 0;
   }

   .line {
      width: 2rpx;
      height: 28rpx;
      background: #ccc;
      margin: 0 16rpx;
   }

   .code {
      color: #1aad19;
      background: transparent;
      border: none;
      outline: none;
      cursor: pointer;
      padding: 0 8rpx;

      &.on {
         color: #bbb !important;
         cursor: not-allowed;
      }
   }
}

// 标题栏（修复选择器，若为组件建议用类名）
.title-bar {
   position: relative;
   display: flex;
   align-items: center;
   justify-content: center;
   height: 80rpx;
   font-size: 34rpx;
   font-weight: 500;
   color: #333;
   line-height: 48rpx;

   .icon {
      position: absolute;
      left: 30rpx;
      top: 50%;
      transform: translateY(-50%);
      display: flex;
      align-items: center;
      justify-content: center;
      width: 80rpx;
      height: 80rpx;
      cursor: pointer;

      image {
         width: 35rpx;
         height: 35rpx;
         object-fit: contain;
      }
   }
}

// 底部协议
.protocol {
   position: fixed;
   bottom: 52rpx;
   left: 0;
   width: 100%;
   margin: 0 auto;
   color: #999;
   font-size: 24rpx;
   line-height: 36rpx;
   text-align: center;
   padding: 0 40rpx;
   bottom: calc(52rpx + constant(safe-area-inset-bottom));
   bottom: calc(52rpx + env(safe-area-inset-bottom));
}

// 主题色类
.main-color {
   color: var(--view-theme) !important;
}

// 抖动动画类
.trembling {
   animation: shake 0.6s ease-in-out;
}

// 淡入动画类
.animate-fadeIn {
   animation: fadeIn 0.3s ease-out forwards;
}
.centext {
   width: 90%;
   margin: auto;
   min-height: 70vh;
}
</style>
