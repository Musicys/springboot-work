import { Rote } from '@/access/config';

export default {
   path: 'jzfb',
   name: 'jzfb',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Management',
      setup: true, //是否是菜单
      tabConfig: {
         hideClose: false, // 隐藏关闭按钮
         keepAlive: true, // 启用缓存
         name: '兼职发布管理',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'jzfblist',
         name: 'jzfblist',
         component: () => import('@/view/jzfb/jzfb/index.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false, // 隐藏关闭按钮
               keepAlive: true, // 启用缓存
               name: '兼职发布列表'
            }
         }
      },
      {
         path: 'jzfbform',
         name: 'jzfbform',
         component: () => import('@/view/jzfb/jzfb/form.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true, // 隐藏关闭按钮
               keepAlive: false, // 启用缓存
               name: '添加兼职发布'
            }
         }
      }
      // {
      //    path: 'system',
      //    name: 'sytem',
      //    // 是否是展开子菜单
      //    component: () => import('@/components/router.vue'),
      //    meta: {
      //       icon: 'Collection',
      //       tabConfig: {
      //          hideClose: false, // 隐藏关闭按钮
      //          keepAlive: true, // 启用缓存
      //          name: '系统配置',
      //          isExpand: true
      //       }
      //    },
      //    children: [
      //       {
      //          path: 'config',
      //          name: 'config',
      //          component: () => import('@/view/admin-list/index.vue'),
      //          meta: {
      //             icon: 'Collection',
      //             tabConfig: {
      //                hideClose: false, // 隐藏关闭按钮
      //                keepAlive: true, // 启用缓存
      //                name: '系统配置'
      //             }
      //          }
      //       }
      //    ]
      // }
   ]
};
