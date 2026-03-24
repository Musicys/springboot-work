<!-- components/Popup/Popup.vue -->
<template>
   <view v-show="show" class="popup-container" @touchmove.stop.prevent>
      <!-- 遮罩层 -->
      <view
         v-if="mask"
         class="popup-mask"
         :class="{ 'popup-mask-show': showMask }"
         :style="{ backgroundColor: maskColor }"
         @tap="onMaskClick"></view>

      <!-- 弹出层主体 -->
      <view
         class="popup-content"
         :class="[
            `popup-content-${position}`,
            { 'popup-content-show': showContent }
         ]"
         :style="[contentCommonStyle, contentStyle]"
         @touchmove.stop>
         <slot></slot>
      </view>
   </view>
</template>

<script setup>
import { ref, watch, computed } from 'vue';

// ========== Props 定义 ==========
const props = defineProps({
   show: {
      type: Boolean,
      default: false
   },
   position: {
      type: String,
      default: 'bottom',
      validator: value =>
         ['center', 'top', 'bottom', 'left', 'right'].includes(value)
   },
   mask: {
      type: Boolean,
      default: true
   },
   closeOnClickMask: {
      type: Boolean,
      default: true
   },
   maskColor: {
      type: String,
      default: 'rgba(0, 0, 0, 0.5)'
   },
   contentStyle: {
      type: Object,
      default: () => ({})
   },
   duration: {
      type: Number,
      default: 300
   }
});

// ========== 事件触发 ==========
const emit = defineEmits(['update:show', 'open', 'close']);

// ========== 响应式数据 ==========
const showMask = ref(false);
const showContent = ref(false);

// ========== 计算属性 ==========
// 动态设置动画时长
const contentCommonStyle = computed(() => ({
   transitionDuration: `${props.duration}ms`
}));

// ========== 方法 ==========
const open = () => {
   showMask.value = true;
   setTimeout(() => {
      showContent.value = true;
   }, 50);
   emit('open');
};

const close = () => {
   showContent.value = false;
   setTimeout(() => {
      showMask.value = false;
      emit('close');
   }, props.duration);
};

const onMaskClick = () => {
   if (props.closeOnClickMask) {
      emit('update:show', false);
      emit('close'); // 可选：点击遮罩也触发 close 事件
   }
};

// ========== 监听器 ==========
watch(
   () => props.show,
   newVal => {
      if (newVal) {
         open();
      } else {
         close();
      }
   },
   { immediate: true } // 确保组件初始化时也能响应 props.show
);
</script>

<style lang="scss" scoped>
.popup-container {
   position: fixed;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   z-index: 999;
   pointer-events: none;
}

.popup-mask {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   opacity: 0;
   transition: opacity 0.3s ease;
   pointer-events: none;
}

.popup-mask-show {
   opacity: 1;
   pointer-events: auto;
}

.popup-content {
   position: absolute;
   box-sizing: border-box;
   background-color: #fff;
   transition-property: transform, opacity;
   transition-timing-function: ease;
   pointer-events: auto;
}

/* 位置样式 */
.popup-content-top {
   top: 0;
   left: 0;
   width: 100%;
   transform: translateY(-100%);
}
.popup-content-top.popup-content-show {
   transform: translateY(0);
}

.popup-content-bottom {
   bottom: 0;
   left: 0;
   width: 100%;
   transform: translateY(100%);
}
.popup-content-bottom.popup-content-show {
   transform: translateY(0);
}

.popup-content-left {
   top: 0;
   left: 0;
   height: 100%;
   transform: translateX(-100%);
}
.popup-content-left.popup-content-show {
   transform: translateX(0);
}

.popup-content-right {
   top: 0;
   right: 0;
   height: 100%;
   transform: translateX(100%);
}
.popup-content-right.popup-content-show {
   transform: translateX(0);
}

.popup-content-center {
   top: 50%;
   left: 50%;
   transform: translate(-50%, -50%) scale(0.8);
   opacity: 0;
   min-width: 200px;
   border-radius: 12px;
}
.popup-content-center.popup-content-show {
   transform: translate(-50%, -50%) scale(1);
   opacity: 1;
}

/* 动画时长由 JS 动态注入 */
</style>
