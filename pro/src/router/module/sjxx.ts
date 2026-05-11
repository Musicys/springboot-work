import { Rote } from '@/access/config';

export default {
   path: 'sjxx',
   name: 'sjxx',
   component: () => import('@/components/router.vue'),
   meta: {
      icon: 'UserFilled',
      setup: true,
      tabConfig: {
         hideClose: false,
         keepAlive: true,
         name: '商家详情管理',
         access: Rote.admin
      }
   },
   children: [
      {
         path: 'detail',
         name: 'sjxxDetail',
         component: () => import('@/view/sjxx/detail.vue'),
         meta: {
            icon: 'Collection',
            tabConfig: {
               hideClose: true,
               keepAlive: false,
               name: '商家详情查看'
            }
         }
      }
   ]
};
