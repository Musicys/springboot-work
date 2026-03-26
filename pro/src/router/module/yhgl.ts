import { Rote } from '@/access/config';

export default {
   path: 'yhgl',
   name: 'yhgl',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'User',
      setup: true, //是否是菜单
      tabConfig: {
         hideClose: false, // 隐藏关闭按钮
         keepAlive: true, // 启用缓存
         name: '用户管理中心',
         access: Rote.user
      }
   },
   children: [
      {
         path: 'yhgllist',
         name: 'yhglList',
         component: () => import('@/view/yhgl/yhgl/index.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false, // 隐藏关闭按钮
               keepAlive: false, // 启用缓存
               name: '用户列表'
            }
         }
      },
      {
         path: 'yhglform',
         name: 'yhglAdd',
         component: () => import('@/view/yhgl/yhgl/form.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true, // 隐藏关闭按钮
               keepAlive: false, // 启用缓存
               name: '添加用户'
            }
         }
      }
   ]
};
