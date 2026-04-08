<template>
   <div class="dashboard">
      <!-- 顶部标题栏 -->
      <div class="dashboard-header">
         <h1>📊 数据运营看板</h1>
         <div class="header-date">{{ formattedDate }}</div>
      </div>

      <!-- 指标卡片区 (4个) -->
      <div class="cards-grid">
         <div class="stat-card" v-for="card in dashboardData.cards" :key="card.label">
            <div class="card-label">{{ card.label }}</div>
            <div class="card-value">
               {{ formatCardValue(card.label, card.value) }}
            </div>
            <div class="card-trend"></div>
         </div>
      </div>

      <!-- 双饼图区域 -->
      <div class="charts-row">
         <!-- 订单状态占比饼图 -->
         <div class="chart-card">
            <div class="chart-header">
               <span class="chart-title">{{ dashboardData.pie_order_status.title }}</span>
            </div>
            <div ref="pieOrderRef" class="chart-container"></div>
         </div>

         <!-- 商家商誉分构成饼图 + 总分展示 -->
         <div class="chart-card">
            <div class="chart-header">
               <span class="chart-title">{{ dashboardData.pie_merchant_score.title }}</span>
               <div class="total-score-badge">总分 {{ dashboardData.pie_merchant_score.total }}</div>
            </div>
            <div ref="pieMerchantRef" class="chart-container"></div>
         </div>
      </div>

      <!-- 月度趋势复合图表 (柱状图+折线图) -->
      <div class="chart-card full-width">
         <div class="chart-header">
            <span class="chart-title">📈 月度订单 & 交易金额趋势</span>
            <span class="sub-title">(完成订单/故障订单 | 支付金额)</span>
         </div>
         <div ref="mixedChartRef" class="chart-container large-chart"></div>
      </div>
   </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, computed } from 'vue';
import * as echarts from 'echarts';

// ---------- 数据定义 ----------
const chartData = ref({
   dashboard: {
      date: '2026-03-26',
      cards: [
         { label: '订单结算金额', value: 1258000.5 },
         { label: '订单冻结金额', value: 45000.0 },
         { label: '已经完成订单', value: 3420 },
         { label: '订单进行中', value: 158 }
      ],
      pie_order_status: {
         title: '当前年份订单完成占比',
         data: [
            { name: '已完成订单', value: 3420 },
            { name: '故障订单', value: 125 },
            { name: '未完成订单', value: 158 }
         ]
      },
      pie_merchant_score: {
         title: '商家商誉分构成',
         total: 92,
         data: [
            { name: '正常基础分', value: 80 },
            { name: '兼职加分', value: 22 },
            { name: '违规扣分', value: -10 }
         ]
      },
      tree_monthly_stats: [
         { month: '2026-01', completed_count: 280, fault_count: 10, payment_amount: 150000.0 },
         { month: '2026-02', completed_count: 310, fault_count: 8, payment_amount: 185000.0 },
         { month: '2026-03', completed_count: 295, fault_count: 12, payment_amount: 172000.0 },
         { month: '2026-04', completed_count: 320, fault_count: 5, payment_amount: 198000.0 },
         { month: '2026-05', completed_count: 350, fault_count: 15, payment_amount: 210000.0 },
         { month: '2026-06', completed_count: 330, fault_count: 9, payment_amount: 195000.0 },
         { month: '2026-07', completed_count: 360, fault_count: 11, payment_amount: 225000.0 },
         { month: '2026-08', completed_count: 345, fault_count: 7, payment_amount: 205000.0 },
         { month: '2026-09', completed_count: 315, fault_count: 13, payment_amount: 188000.0 },
         { month: '2026-10', completed_count: 380, fault_count: 6, payment_amount: 240000.0 },
         { month: '2026-11', completed_count: 390, fault_count: 14, payment_amount: 255000.0 },
         { month: '2026-12', completed_count: 405, fault_count: 15, payment_amount: 268000.0 }
      ]
   }
});

// 解构数据方便模板使用
const dashboardData = computed(() => chartData.value.dashboard);
const formattedDate = computed(() => {
   const dateStr = dashboardData.value.date;
   if (dateStr) return `📅 ${dateStr.replace(/-/g, '/')}`;
   return '';
});

// ---------- 格式化工具 ----------
const formatNumber = (num: number, isCurrency: boolean = false): string => {
   if (isCurrency) {
      return `¥ ${num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
   }
   return num.toLocaleString('zh-CN');
};

const formatCardValue = (label: string, value: number): string => {
   if (label.includes('金额')) {
      return formatNumber(value, true);
   }
   return formatNumber(value, false);
};

// 处理商家商誉分数据（将扣分项转为绝对值用于饼图展示，但保留原始语义）
const getMerchantScoreChartData = () => {
   const raw = dashboardData.value.pie_merchant_score.data;
   return raw.map(item => ({
      name: item.name,
      value: item.name === '违规扣分' ? Math.abs(item.value) : item.value,
      originalValue: item.value // 保留原始值用于tooltip
   }));
};

// 月度数据准备
const monthlyXAxis = computed(() => {
   return dashboardData.value.tree_monthly_stats.map(item => {
      const [year, month] = item.month.split('-');
      return `${month}月`;
   });
});

const completedData = computed(() => dashboardData.value.tree_monthly_stats.map(item => item.completed_count));
const faultData = computed(() => dashboardData.value.tree_monthly_stats.map(item => item.fault_count));
const paymentData = computed(() => dashboardData.value.tree_monthly_stats.map(item => item.payment_amount));

// ---------- ECharts 实例管理 ----------
let pieOrderChart: echarts.ECharts | null = null;
let pieMerchantChart: echarts.ECharts | null = null;
let mixedChart: echarts.ECharts | null = null;

// 1. 订单状态饼图
const initPieOrderChart = () => {
   if (!pieOrderRef.value) return;
   if (pieOrderChart) pieOrderChart.dispose();
   pieOrderChart = echarts.init(pieOrderRef.value);
   const option = {
      tooltip: { trigger: 'item', backgroundColor: 'rgba(0,0,0,0.7)', borderColor: '#333' },
      legend: { orient: 'vertical', left: 'left', textStyle: { color: '#e2e8f0' } },
      series: [
         {
            name: '订单状态',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: dashboardData.value.pie_order_status.data,
            emphasis: { scale: true },
            label: { color: '#f1f5f9', formatter: '{b}: {d}%' },
            itemStyle: {
               borderRadius: 8,
               borderColor: '#0f172a',
               borderWidth: 2
            },
            color: ['#3b82f6', '#ef4444', '#f59e0b']
         }
      ],
      backgroundColor: 'transparent'
   };
   pieOrderChart.setOption(option);
};

// 2. 商家商誉分构成饼图 (使用绝对值展示，tooltip显示真实增减分)
const initPieMerchantChart = () => {
   if (!pieMerchantRef.value) return;
   if (pieMerchantChart) pieMerchantChart.dispose();
   pieMerchantChart = echarts.init(pieMerchantRef.value);
   const chartDataValues = getMerchantScoreChartData();
   const option = {
      tooltip: {
         trigger: 'item',
         formatter: (params: any) => {
            const originalItem = dashboardData.value.pie_merchant_score.data.find(d => d.name === params.name);
            if (originalItem && originalItem.name === '违规扣分') {
               return `${params.name}<br/>分值影响: ${originalItem.value} 分 (绝对值 ${Math.abs(originalItem.value)})<br/>占比: ${params.percent}%`;
            }
            if (originalItem && (originalItem.name === '正常基础分' || originalItem.name === '兼职加分')) {
               return `${params.name}<br/>贡献分值: +${originalItem.value}<br/>占比: ${params.percent}%`;
            }
            return `${params.name}<br/>分值: ${params.value}<br/>占比: ${params.percent}%`;
         },
         backgroundColor: 'rgba(0,0,0,0.7)',
         borderColor: '#475569'
      },
      legend: { orient: 'vertical', left: 'left', textStyle: { color: '#e2e8f0' } },
      series: [
         {
            name: '商誉分构成',
            type: 'pie',
            radius: ['45%', '65%'],
            avoidLabelOverlap: false,
            itemStyle: {
               borderRadius: 8,
               borderColor: '#0f172a',
               borderWidth: 2
            },
            label: { color: '#f1f5f9', formatter: '{b}\n{d}%' },
            data: chartDataValues,
            color: ['#10b981', '#8b5cf6', '#f97316']
         }
      ],
      graphic: [
         {
            type: 'text',
            left: 'center',
            top: 'center',
            style: {
               text: `${dashboardData.value.pie_merchant_score.total}`,
               fill: '#facc15',
               fontSize: 24,
               fontWeight: 'bold',
               fontFamily: 'DIN, monospace'
            },
            z: 100
         },
         {
            type: 'text',
            left: 'center',
            top: '62%',
            style: {
               text: '总分',
               fill: '#94a3b8',
               fontSize: 12,
               fontWeight: 'normal'
            },
            z: 100
         }
      ],
      backgroundColor: 'transparent'
   };
   pieMerchantChart.setOption(option);
};

// 3. 混合图表 (完成订单柱状图 + 故障订单柱状图 + 支付金额折线图)
const initMixedChart = () => {
   if (!mixedChartRef.value) return;
   if (mixedChart) mixedChart.dispose();
   mixedChart = echarts.init(mixedChartRef.value);
   const option = {
      tooltip: {
         trigger: 'axis',
         axisPointer: { type: 'shadow' },
         formatter: (params: any) => {
            let res = `${params[0].axisValue}<br/>`;
            for (let p of params) {
               if (p.seriesName === '支付金额(元)') {
                  res += `${p.marker} ${p.seriesName}: ¥ ${p.value.toLocaleString()}<br/>`;
               } else {
                  res += `${p.marker} ${p.seriesName}: ${p.value} 笔<br/>`;
               }
            }
            return res;
         }
      },
      legend: {
         data: ['完成订单', '故障订单', '支付金额(元)'],
         textStyle: { color: '#e2e8f0' },
         left: 'center',
         top: 0,
         icon: 'circle'
      },
      grid: { left: '8%', right: '10%', top: '15%', bottom: '5%', containLabel: true },
      xAxis: {
         type: 'category',
         data: monthlyXAxis.value,
         axisLabel: { color: '#cbd5e1', fontWeight: 500, rotate: 0 },
         axisLine: { lineStyle: { color: '#334155' } },
         axisTick: { show: false }
      },
      yAxis: [
         {
            type: 'value',
            name: '订单数量 (笔)',
            nameTextStyle: { color: '#94a3b8' },
            axisLabel: { color: '#cbd5e1' },
            splitLine: { lineStyle: { color: '#1e293b', type: 'dashed' } }
         },
         {
            type: 'value',
            name: '金额 (元)',
            nameTextStyle: { color: '#94a3b8' },
            axisLabel: { color: '#facc15', formatter: (val: number) => `¥${val / 1000}k` },
            splitLine: { show: false },
            axisLine: { show: false }
         }
      ],
      series: [
         {
            name: '完成订单',
            type: 'bar',
            barWidth: '25%',
            data: completedData.value,
            itemStyle: { borderRadius: [6, 6, 0, 0], color: '#3b82f6' },
            yAxisIndex: 0,
            label: { show: false }
         },
         {
            name: '故障订单',
            type: 'bar',
            barWidth: '25%',
            data: faultData.value,
            itemStyle: { borderRadius: [6, 6, 0, 0], color: '#ef4444' },
            yAxisIndex: 0,
            label: { show: false }
         },
         {
            name: '支付金额(元)',
            type: 'line',
            smooth: true,
            data: paymentData.value,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: { width: 3, color: '#facc15', shadowBlur: 10 },
            itemStyle: { color: '#facc15', borderColor: '#0f172a', borderWidth: 1 },
            yAxisIndex: 1,
            tooltip: { valueFormatter: (value: number) => `¥ ${value.toLocaleString()}` }
         }
      ],
      backgroundColor: 'transparent'
   };
   mixedChart.setOption(option);
};

// 统一更新所有图表 (数据变化时调用)
const updateAllCharts = () => {
   if (pieOrderChart) {
      pieOrderChart.setOption({
         series: [{ data: dashboardData.value.pie_order_status.data }]
      });
   }
   if (pieMerchantChart) {
      const newMerchantData = getMerchantScoreChartData();
      pieMerchantChart.setOption({
         series: [{ data: newMerchantData }],
         graphic: [
            {
               type: 'text',
               left: 'center',
               top: 'center',
               style: { text: `${dashboardData.value.pie_merchant_score.total}` }
            }
         ]
      });
   }
   if (mixedChart) {
      mixedChart.setOption({
         xAxis: { data: monthlyXAxis.value },
         series: [{ data: completedData.value }, { data: faultData.value }, { data: paymentData.value }]
      });
   }
};

// ---------- 响应式监听 ----------
watch(
   () => dashboardData.value,
   () => {
      updateAllCharts();
   },
   { deep: true }
);

// 窗口resize适配
const handleResize = () => {
   pieOrderChart?.resize();
   pieMerchantChart?.resize();
   mixedChart?.resize();
};

// ---------- 生命周期 & DOM引用 ----------
const pieOrderRef = ref<HTMLElement | null>(null);
const pieMerchantRef = ref<HTMLElement | null>(null);
const mixedChartRef = ref<HTMLElement | null>(null);

onMounted(() => {
   // 确保DOM挂载后再初始化图表
   setTimeout(() => {
      initPieOrderChart();
      initPieMerchantChart();
      initMixedChart();
      window.addEventListener('resize', handleResize);
   }, 50);
});

onUnmounted(() => {
   window.removeEventListener('resize', handleResize);
   pieOrderChart?.dispose();
   pieMerchantChart?.dispose();
   mixedChart?.dispose();
});
</script>

<style lang="scss" scoped>
.dashboard {
   background: #0f172a;
   min-height: 100vh;
   padding: 24px 32px;
   height: 94vh;
   overflow: auto;
   font-family:
      'Inter',
      'Segoe UI',
      system-ui,
      -apple-system,
      sans-serif;
   color: #e2e8f0;

   .dashboard-header {
      display: flex;
      justify-content: space-between;
      align-items: baseline;
      margin-bottom: 28px;
      flex-wrap: wrap;

      h1 {
         font-size: 1.8rem;
         font-weight: 600;
         background: linear-gradient(135deg, #e2e8f0, #94a3b8);
         -webkit-background-clip: text;
         background-clip: text;
         color: transparent;
         letter-spacing: -0.3px;
         margin: 0;
      }

      .header-date {
         background: #1e293b;
         padding: 8px 18px;
         border-radius: 40px;
         font-size: 0.9rem;
         font-weight: 500;
         backdrop-filter: blur(4px);
         color: #cbd5e6;
         box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
      }
   }

   // 卡片网格
   .cards-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 24px;
      margin-bottom: 32px;
   }

   .stat-card {
      background: rgba(30, 41, 59, 0.7);
      backdrop-filter: blur(8px);
      border-radius: 24px;
      padding: 20px 20px;
      border: 1px solid rgba(71, 85, 105, 0.4);
      transition:
         transform 0.2s,
         box-shadow 0.2s;
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);

      &:hover {
         transform: translateY(-3px);
         border-color: #3b82f680;
         box-shadow: 0 14px 28px rgba(0, 0, 0, 0.3);
      }

      .card-label {
         font-size: 0.9rem;
         text-transform: uppercase;
         letter-spacing: 0.5px;
         font-weight: 500;
         color: #94a3b8;
         margin-bottom: 12px;
      }

      .card-value {
         font-size: 2.1rem;
         font-weight: 700;
         color: #f1f5f9;
         word-break: break-word;
         line-height: 1.2;
         font-family: 'DIN', 'Inter', monospace;
      }

      .card-trend {
         margin-top: 12px;
         height: 4px;
         background: #3b82f6;
         width: 40px;
         border-radius: 4px;
      }
   }

   // 双饼图行
   .charts-row {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 24px;
      margin-bottom: 32px;
   }

   .chart-card {
      background: rgba(15, 23, 42, 0.6);
      backdrop-filter: blur(4px);
      border-radius: 28px;
      padding: 18px 20px 20px;
      border: 1px solid rgba(71, 85, 105, 0.3);
      transition: all 0.2s;
      box-shadow: 0 6px 14px rgba(0, 0, 0, 0.2);

      &.full-width {
         grid-column: span 2;
      }

      .chart-header {
         display: flex;
         justify-content: space-between;
         align-items: center;
         margin-bottom: 16px;
         padding: 0 8px;

         .chart-title {
            font-weight: 600;
            font-size: 1.2rem;
            color: #f1f5f9;
            letter-spacing: -0.2px;
         }

         .total-score-badge {
            background: #2d3a5e;
            padding: 6px 14px;
            border-radius: 36px;
            font-size: 0.9rem;
            font-weight: 600;
            color: #facc15;
            box-shadow:
               inset 0 0 2px #00000030,
               0 2px 6px #00000020;
         }

         .sub-title {
            font-size: 0.75rem;
            color: #64748b;
            background: #0f172a80;
            padding: 4px 12px;
            border-radius: 20px;
         }
      }

      .chart-container {
         width: 100%;
         height: 320px;

         &.large-chart {
            height: 420px;
         }
      }
   }

   // 响应式
   @media (max-width: 1200px) {
      padding: 20px;
      .cards-grid {
         gap: 16px;
      }
      .stat-card .card-value {
         font-size: 1.6rem;
      }
   }

   @media (max-width: 900px) {
      .charts-row {
         grid-template-columns: 1fr;
      }
      .chart-card.full-width {
         grid-column: span 1;
      }
      .cards-grid {
         grid-template-columns: repeat(2, 1fr);
      }
   }

   @media (max-width: 560px) {
      .cards-grid {
         grid-template-columns: 1fr;
      }
      .dashboard-header {
         flex-direction: column;
         gap: 12px;
         align-items: flex-start;
      }
   }
}
</style>
