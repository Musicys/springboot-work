import { Rote } from '@/access/config';

export default {
   path: 'glygl',
   name: 'glygl',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Management',
      setup: true, //是否是菜单
      tabConfig: {
         hideClose: false, // 隐藏关闭按钮
         keepAlive: true, // 启用缓存
         name: '管理员管理中心',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'glygllist',
         name: 'glyglList',
         component: () => import('@/view/glygl/glygl/index.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false, // 隐藏关闭按钮
               keepAlive: true, // 启用缓存
               name: '管理员列表'
            }
         }
      },
      {
         path: 'glygladd',
         name: 'glyglAdd',
         component: () => import('@/view/glygl/glygl/form.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false, // 隐藏关闭按钮
               keepAlive: true, // 启用缓存
               name: '添加管理员账号'
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
