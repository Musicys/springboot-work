<template>
   <view class="btn">
      <view class="input-area">
         <view class="input-left">
            <input
               id="inputType"
               v-if="inputType"
               v-model="inputValue"
               @click="emojo = false"
               placeholder="请输入消息..."
               auto-height="false"
               type="text"
               confirm-type="send"
               @confirm="sendMsg"
               :adjust-position="false"
               class="text-input" />
            <view v-else class="voice-holder"> 按住说话 </view>
            <wot-icon
               name="smile"
               color="#999"
               size="28"
               @click="sendFileMusic"></wot-icon>
         </view>
         <view class="input-right" v-if="inputType">
            <wd-button
               type="success"
               class="send-btn"
               @click="sendMsg"
               @touchend.prevent="sendMsg"
               >发送</wd-button
            >
         </view>
      </view>
      <!-- 底部功能区域 -->

      <view class="bottom-func">
         <view class="func-item" @touchend.prevent="openMusic">
            <tn-icon :name="inputType ? 'sound' : 'keyboard'" />
         </view>
         <view class="func-item" @touchend.prevent="openEmjo">
            <tn-icon name="../../static/imgs/smile.png" />
         </view>
         <view class="func-item" @touchend.prevent="sendFile">
            <tn-icon name="image" />
         </view>
         <view class="func-item" @touchend.prevent="openCircle"
            ><tn-icon name="add-circle" />
         </view>
      </view>
   </view>

   <EmjoSelect
      v-if="emojo"
      :height="heght"
      @swiperChange="selectEmoji"></EmjoSelect>
</template>

<script setup lang="ts">
import EmjoSelect from '@/components/emjo-select/index.vue';
import { onBackPress } from '@dcloudio/uni-app';
import { h, nextTick, onMounted } from 'vue';
import {
   getTEXT,
   getVOICE,
   getIMAGE,
   getAUDIO,
   MESSAGE_TYPE
} from '@/util/mssageType';
import { ChatupdateOssFile, updateFileVideo } from '@/api/file';

// 切换输入类型（文字/语音）c

const heght = ref();
const inputValue = ref('');
const emojo = ref(false);
const inputType = ref(true);
const IsFocus = ref(true);
onMounted(() => {
   // #ifndef APP-PLUS
   uni.onKeyboardHeightChange(res => {
      height.value = res.height;
   });
   // #endif
});
const onTap = e => {
   e.stopPropagation();
};

const emit = defineEmits(['sumbitValue']);
const selectEmoji = (emoji: string) => {
   inputValue.value += emoji;
};
const inputRef = ref(null);

const sendFile = async () => {
   uni.showLoading({
      title: '发送中',
      mask: true
   });
   //   #ifdef APP-PLUS
   uni.chooseMedia({
      count: 1, // 最多选择1个文件
      mediaType: ['video', 'image'], // 仅选择视频
      sourceType: ['album'], // 从相册选择
      maxDuration: 15, // 视频最大时长（秒）
      success: res => {
         if (res.type === 'video') {
            console.log('视频路径:', res.tempFiles[0]);

            ChatupdateOssFile(res.tempFiles[0].thumbTempFilePath).then(
               result => {
                  try {
                     const data = JSON.parse((result.data as string) || '[]');

                     if (data.code === 0) {
                        // uni.hideLoading();

                        updateFileVideo({
                           file: res.tempFiles[0].tempFilePath,
                           tagId: 9,
                           width: res.tempFiles[0].width,
                           height: res.tempFiles[0].height,
                           durationSeconds: Math.ceil(
                              res.tempFiles[0].duration
                           ),
                           cover: data.data.url
                        }).then(result2 => {
                           const data2 = JSON.parse(
                              (result2.data as string) || '[]'
                           );

                           if (data2.code === 0) {
                              uni.hideLoading();

                              emit('sumbitValue', getVOICE(data2.data));
                           }
                        });
                     } else {
                        uni.hideLoading();
                     }
                  } catch (e) {
                     uni.showToast({
                        title: '图片上传失败',
                        icon: 'none'
                     });
                  }
               }
            );

            // const result = await updateFileVideo(res.tempFiles[0].tempFilePath);

            //获取路径提交给服务器
         } else if (res.type === 'image') {
            ChatupdateOssFile(res.tempFiles[0].tempFilePath).then(result => {
               const data = JSON.parse((result.data as string) || '[]');

               if (data.code === 0) {
                  uni.hideLoading();

                  emit('sumbitValue', getIMAGE(data.data));
               } else {
                  uni.hideLoading();
               }
            });
         }
      },
      fail: err => {
         console.error('选择媒体失败:', err);
         uni.hideLoading();
      }
   });
   // #endif

   // #ifdef H5

   uni.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album'],
      success: res => {
         // 将新选择的图片添加到数组中

         ChatupdateOssFile(res.tempFilePaths[0]).then(result => {
            try {
               const data = JSON.parse((result.data as string) || '[]');
               console.log(data.code);

               if (data.code === 0) {
                  uni.hideLoading();
                  emit('sumbitValue', getIMAGE(data.data));
               } else {
                  uni.hideLoading();
               }
            } catch (e) {
               uni.showToast({
                  title: '图片上传失败',
                  icon: 'none'
               });
               return uni.hideLoading();
            }
         });
      },
      fail: err => {
         console.error('打开相册失败:', err);
         uni.hideLoading();
      }
   });
   // #endif
};
const openCircle = () => {};

const openMusic = () => {
   if (emojo.value) {
      emojo.value = false;
   }
   inputType.value = !inputType.value;
};
const openEmjo = () => {
   if (!emojo.value) {
      setTimeout(() => {
         uni.hideKeyboard();
      });
   }
   setTimeout(() => {
      emojo.value = !emojo.value;
   }, 50);
};

const sendMsg = () => {
   if (inputValue.value.trim() === '') {
      return;
   }
   if (inputValue.value.trim() === '') return; // 如果消息为空，则不发送
   // 在实际应用中，这里应该调用API发送消息，然后等待friendInfo.value.sendList更新
   // 1. 替换所有换行符为标准 \n
   let message = inputValue.value.replace(/\r\n|\r|\n/g, '\n');

   // 2. 可选：将多个连续空白（空格、制表符等）压缩为一个空格
   message = message.replace(/[^\S\n]+/g, ' '); // 只压缩非换行的空白

   // 3. 去除首尾空白（包括换行和空格）
   message = message.trim();

   emit('sumbitValue', getTEXT(message));
   inputValue.value = '';
};
const sendFileMusic = () => {};
</script>

<style lang="scss" scoped>
$theme-color: #ff6b6b;
$light-gray: #f5f5f5;
$border-color: #eee;
$online-green: #52c41a;
.btn {
   background: #fff;
}
.input-area {
   display: flex;
   align-items: center;
   padding: 10px;
   border-top: 1px solid $border-color;

   .input-left {
      flex: 1;
      display: flex;
      align-items: center;
      .text-input {
         flex: 1;
         height: 36px;
         line-height: 36px;
         padding: 0 8px;
         border: 1px solid #ddd;
         border-radius: 18px;
         font-size: 24rpx;
         margin: 0 8px;
      }
      .voice-holder {
         flex: 1;
         height: 36px;
         line-height: 36px;
         text-align: center;
         background-color: $light-gray;
         border-radius: 18px;
         margin: 0 8px;
      }
   }
   .input-right {
      .send-btn {
         width: 70px;
         height: 36px;
         line-height: 36px;
         background-color: $theme-color;
         color: #fff;
         border-radius: 18px;
         border: none;
      }
   }
}

.bottom-func {
   display: flex;
   justify-content: space-around;
   padding: 10px 0;
   border-top: 1px solid $border-color;
   .func-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      font-size: 18px;
      text {
         font-size: 12px;
         color: #666;
         margin-top: 5px;
      }
   }
}

.emoji-panel {
   position: fixed;
   bottom: 120px;
   left: 0;
   right: 0;

   padding: 10px;
   display: flex;
   flex-wrap: wrap;
   .emoji-item {
      width: 30px;
      height: 30px;
      text-align: center;
      line-height: 30px;
      font-size: 20px;
      margin: 5px;
   }
}
</style>
