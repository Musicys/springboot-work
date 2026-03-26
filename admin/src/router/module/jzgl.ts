import { Rote } from '@/access/config';

export default {
   path: 'jzgl',
   name: 'jzgl',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Briefcase',
      setup: true, //是否是菜单
      tabConfig: {
         hideClose: false, // 隐藏关闭按钮
         keepAlive: true, // 启用缓存
         name: '兼职岗位管理中心',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'jzgllist',
         name: 'jzglList',
         component: () => import('@/view/jzgl/jzgl/index.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false, // 隐藏关闭按钮
               keepAlive: true, // 启用缓存
               name: '兼职岗位列表'
            }
         }
      },
      {
         path: 'jzgladd',
         name: 'jzglAdd',
         component: () => import('@/view/jzgl/jzgl/form.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false, // 隐藏关闭按钮
               keepAlive: true, // 启用缓存
               name: '添加兼职岗位'
            }
         }
      }
   ]
};
