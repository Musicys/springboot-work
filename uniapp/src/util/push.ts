// #ifdef APP
import { KeepLive } from '@/uni_modules/android-keeplive'; //
try {
   const keep = new KeepLive();
   keep.setTitle('app');
   keep.setContent('app is runing');
   // keep.setLargeIcon("icon");
   keep.setSmallIcon('icon'); //图标名字  图标放在 插件下面的 res/drawable 文件夹下
   keep.onAddBackgroundCallback(function (res) {
      console.log('后台运行 ' + res);
   });
   keep.onAddScrrenListenerCallback(function (res) {
      console.log('屏幕开启状态 ' + res);
   });
   keep.onAddNotificationClickListener(function (d) {
      // d 为点击通知栏信息 的json字符
      console.log(d);
   });
   keep.setWakeLock(1, 'keeptag'); // 设置唤醒类型

   keep.setAutoStartEnable(true);
   if (!keep.checkAppNotification()) {
      keep.onOpenNotificationSetting(function (res) {
         keep.register();
         const ignoring = keep.isIgnoringBatteryOptimizations();
         if (!ignoring) {
            keep.requestIgnoreBatteryOptimizations();
         }
      });
   } else {
      keep.register();
      const ignoring = keep.isIgnoringBatteryOptimizations();
      if (!ignoring) {
         keep.requestIgnoreBatteryOptimizations();
      }
   }

   keep.onStartCSystemTimer(60, function () {
      console.log('onStartCSystemTimer ');
      keep.acquire(1000); // 唤醒一秒钟
   });
} catch (e) {
   console.log('出错');
}

// 需要插件增加程序后台运行能力的可以加下面代码 想程序始终保持活性（即稳定推送 scocket等）（体验好） 还是建议用户打开系统设置 启动项 后台运行
//keep.wifilock();//部分手机可能会断
// 定时器时间改成3秒 去掉上方的
//keep.onStartCSystemTimer(3,function(){
//  console.log("onStartCSystemTimer ");
//  keep.acquire(1000);// 唤醒一秒钟
//});

// #endif
