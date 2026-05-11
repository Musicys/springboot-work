import { Rote } from '@/access/config';

export default {
   path: 'ypgl',
   name: 'ypglParent',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'UserFilled',
      setup: true,
      tabConfig: {
         hideClose: false,
         keepAlive: true,
         name: '应聘信息管理',
         access: Rote.user
      }
   },
   children: [
      {
         path: 'ypgl',
         name: 'ypgl',
         component: () => import('@/view/ypgl/ypgl/index.vue'),
         meta: {
            icon: 'UserFilled',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '应聘信息'
            }
         }
      },
      {
         path: 'pending-entry',
         name: 'pendingEntry',
         component: () => import('@/view/ypgl/pending-entry/index.vue'),
         meta: {
            icon: 'UserFilled',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '待入职'
            }
         }
      },
      {
         path: 'jzjxz',
         name: 'jzjxz',
         component: () => import('@/view/ypgl/jzjxz/index.vue'),
         meta: {
            icon: 'UserFilled',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '进行中'
            }
         }
      },
      {
         path: 'orders',
         name: 'orders',
         component: () => import('@/view/ypgl/orders/index.vue'),
         meta: {
            icon: 'UserFilled',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '订单主表'
            }
         }
      }
   ]
};
