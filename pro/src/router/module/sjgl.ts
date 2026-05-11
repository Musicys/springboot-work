import { Rote } from '@/access/config';

export default {
   path: 'sjgl',
   name: 'sjgl',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Management',
      setup: true, //是否是菜单
      tabConfig: {
         hideClose: false, // 隐藏关闭按钮
         keepAlive: true, // 启用缓存
         name: '商家管理中心',
         access: Rote.user
      }
   },
   children: [
      {
         path: 'sjglxx',
         name: 'sjglxx',
         component: () => import('@/view/sjgl/sjglxx/index.vue'),
         meta: {
            icon: 'User',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '商家信息'
            }
         }
      },
      {
         path: 'sjgllist',
         name: 'sjgllist',
         component: () => import('@/view/sjgl/sjgl/index.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false, // 隐藏关闭按钮
               keepAlive: true, // 启用缓存
               name: '商家列表'
            }
         }
      },
      {
         path: 'sjglform',
         name: 'sjglform',
         component: () => import('@/view/sjgl/sjgl/form.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true, // 隐藏关闭按钮
               keepAlive: false, // 启用缓存
               name: '添加商家'
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
