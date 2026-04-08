import { Rote } from '@/access/config';

export default {
   path: 'sjjs',
   name: 'sjjs',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'Money',
      setup: true,
      tabConfig: {
         hideClose: false,
         keepAlive: true,
         name: '商家结算管理',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'list',
         name: 'sjjsList',
         component: () => import('@/view/sjjs/list.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '结算订单列表'
            }
         }
      },
      {
         path: 'detail',
         name: 'sjjsDetail',
         component: () => import('@/view/sjjs/detail.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true,
               keepAlive: false,
               name: '结算详情'
            }
         }
      },
      {
         path: 'credit',
         name: 'sjjsCredit',
         component: () => import('@/view/sjjs/credit.vue'),
         meta: {
            icon: 'Star',
            tabConfig: {
               hideClose: false,
               keepAlive: true,
               name: '信誉分记录'
            }
         }
      }
   ]
};
