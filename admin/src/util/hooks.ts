import { reactive } from 'vue';

/**
 * 分页对象及indexMethod
 * @returns
 */
export function usePagination(pageSize = 10) {
   // 分页对象
   const pagination = reactive({
      pageSize,
      pageNum: 1,
      total: 0
   });
   // 序号函数
   const indexMethod = index => {
      return (pagination.pageNum - 1) * pagination.pageSize + index + 1;
   };

   const paginationReset = (pageSize = 10) => {
      pagination.pageNum = 1;
      pagination.pageSize = pageSize;
      pagination.total = 0;
   };

   return {
      pagination,
      indexMethod,
      paginationReset
   };
}
