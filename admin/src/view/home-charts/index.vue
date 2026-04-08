<template>
   <div class="dashboard">
      <!-- KPI 卡片区 -->
      <div class="kpi-grid">
         <div class="kpi-card">
            <div class="kpi-label">🚨 异常商家 (累计)</div>
            <div class="kpi-value">{{ formatNumber(kpiData.totalAbnormalMerchants) }}</div>
            <div class="kpi-unit">家次</div>
         </div>
         <div class="kpi-card">
            <div class="kpi-label">⚠️ 异常订单 (累计)</div>
            <div class="kpi-value">{{ formatNumber(kpiData.totalAbnormalOrders) }}</div>
            <div class="kpi-unit">单</div>
         </div>
         <div class="kpi-card">
            <div class="kpi-label">👥 异常用户 (累计)</div>
            <div class="kpi-value">{{ formatNumber(kpiData.totalAbnormalUsers) }}</div>
            <div class="kpi-unit">人次</div>
         </div>
         <div class="kpi-card">
            <div class="kpi-label">⚡ 冲突金额 (累计)</div>
            <div class="kpi-value">{{ formatMoney(kpiData.totalConflictAmount) }}</div>
            <div class="kpi-unit">万元</div>
         </div>
         <div class="kpi-card">
            <div class="kpi-label">💰 交易金额 (累计)</div>
            <div class="kpi-value">{{ formatMoney(kpiData.totalTradeAmount) }}</div>
            <div class="kpi-unit">万元</div>
         </div>
         <div class="kpi-card">
            <div class="kpi-label">✅ 交易完成金额 (累计)</div>
            <div class="kpi-value">{{ formatMoney(kpiData.totalCompletedAmount) }}</div>
            <div class="kpi-unit">万元</div>
         </div>
      </div>

      <!-- 第一行：兼职订单数 + 交易流水 -->
      <div class="row-2cols">
         <div class="chart-card">
            <div class="chart-title">📊 兼职订单数 · 月度趋势</div>
            <div ref="parttimeChartRef" class="chart-container"></div>
         </div>
         <div class="chart-card">
            <div class="chart-title">💸 交易流水 · 月度对比 (交易金额 vs 完成金额)</div>
            <div ref="transactionChartRef" class="chart-container"></div>
         </div>
      </div>

      <!-- 第二行：异常商家 / 异常用户 / 异常订单 -->
      <div class="row-3cols">
         <div class="chart-card">
            <div class="chart-title">🏭 异常商家 · 月统计</div>
            <div ref="abMerchantChartRef" class="small-chart"></div>
         </div>
         <div class="chart-card">
            <div class="chart-title">👤 异常用户 · 月统计</div>
            <div ref="abUserChartRef" class="small-chart"></div>
         </div>
         <div class="chart-card">
            <div class="chart-title">📦 异常订单 · 月统计</div>
            <div ref="abOrderChartRef" class="small-chart"></div>
         </div>
      </div>

      <!-- 第三行：饼图 (冲突金额 vs 交易完成金额) -->
      <div class="pie-section">
         <div class="chart-card pie-card">
            <div class="chart-title">🥧 冲突金额 & 交易完成金额 占比 (累计)</div>
            <div ref="pieChartRef" style="height: 360px; width: 100%"></div>
         </div>
      </div>
      <div class="footer-note">⏱️ 数据基于近12个月模拟统计 | 冲突金额为争议/纠纷金额，交易完成金额为实际履约金额</div>
   </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, reactive } from 'vue';
import * as echarts from 'echarts';

// ---------- 模拟数据：最近12个月 (2025年1月 ~ 2025年12月) ----------
const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];

// 兼职订单数 (笔)
const parttimeOrders = [142, 158, 173, 195, 238, 272, 305, 318, 296, 284, 261, 247];

// 交易金额 (万元)
const tradeAmount = [186.5, 192.3, 210.8, 235.6, 278.4, 312.5, 348.9, 365.2, 342.7, 328.4, 305.9, 296.3];
// 交易完成金额 (万元)  完成率约87%~94%
const completedAmount = tradeAmount.map((val, idx) => {
   let rate = 0.88 + idx * 0.003;
   if (rate > 0.94) rate = 0.94;
   return parseFloat((val * rate).toFixed(1));
});
// 冲突金额 (万元)
const conflictAmount = tradeAmount.map((val, idx) => {
   let base = val * (0.05 + (idx % 3) * 0.02);
   if (idx > 5) base = val * 0.09;
   return parseFloat(base.toFixed(1));
});

// 异常商家 月度数据 (家)
const abnormalMerchants = [3, 4, 5, 6, 8, 7, 9, 11, 12, 13, 14, 15];
// 异常用户 月度数据 (人)
const abnormalUsers = [6, 7, 8, 9, 11, 13, 14, 16, 18, 19, 21, 23];
// 异常订单 月度数据 (单)
const abnormalOrders = [28, 32, 37, 42, 48, 53, 59, 66, 72, 78, 83, 89];

// 累计值
const totalAbnormalMerchants = abnormalMerchants.reduce((a, b) => a + b, 0);
const totalAbnormalUsers = abnormalUsers.reduce((a, b) => a + b, 0);
const totalAbnormalOrders = abnormalOrders.reduce((a, b) => a + b, 0);
const totalConflictAmount = conflictAmount.reduce((a, b) => a + b, 0);
const totalTradeAmount = tradeAmount.reduce((a, b) => a + b, 0);
const totalCompletedAmount = completedAmount.reduce((a, b) => a + b, 0);

const kpiData = reactive({
   totalAbnormalMerchants,
   totalAbnormalUsers,
   totalAbnormalOrders,
   totalConflictAmount: parseFloat(totalConflictAmount.toFixed(1)),
   totalTradeAmount: parseFloat(totalTradeAmount.toFixed(1)),
   totalCompletedAmount: parseFloat(totalCompletedAmount.toFixed(1))
});

// 格式化函数
const formatNumber = (num: number) => num.toLocaleString();
const formatMoney = (num: number) => num.toFixed(1);

// 图表 DOM 引用
const parttimeChartRef = ref<HTMLDivElement>();
const transactionChartRef = ref<HTMLDivElement>();
const abMerchantChartRef = ref<HTMLDivElement>();
const abUserChartRef = ref<HTMLDivElement>();
const abOrderChartRef = ref<HTMLDivElement>();
const pieChartRef = ref<HTMLDivElement>();

// 存储 ECharts 实例，用于 resize 和销毁
let chartInstances: echarts.ECharts[] = [];

// 渲染兼职订单柱状图
const renderParttimeOrders = () => {
   if (!parttimeChartRef.value) return;
   const chart = echarts.init(parttimeChartRef.value);
   chart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, valueFormatter: (value: any) => value + ' 单' },
      grid: { top: 40, left: 50, right: 20, bottom: 30, containLabel: true },
      xAxis: { type: 'category', data: months, axisLabel: { rotate: 30, fontWeight: 500 } },
      yAxis: { type: 'value', name: '订单数 (单)', nameLocation: 'middle', nameGap: 40 },
      series: [
         {
            name: '兼职订单数',
            type: 'bar',
            data: parttimeOrders,
            itemStyle: { borderRadius: [8, 8, 0, 0], color: '#3b82f6', shadowColor: 'rgba(59,130,246,0.2)' },
            barWidth: '55%',
            label: { show: true, position: 'top', fontWeight: 'bold' }
         }
      ]
   });
   return chart;
};

// 渲染交易流水 (双柱)
const renderTransactionFlow = () => {
   if (!transactionChartRef.value) return;
   const chart = echarts.init(transactionChartRef.value);
   chart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, valueFormatter: (value: any) => value + ' 万元' },
      legend: { data: ['交易金额', '交易完成金额'], left: 'left', itemWidth: 30 },
      grid: { top: 50, left: 60, right: 30, bottom: 30 },
      xAxis: { type: 'category', data: months, axisLabel: { rotate: 30 } },
      yAxis: { type: 'value', name: '金额 (万元)' },
      series: [
         {
            name: '交易金额',
            type: 'bar',
            data: tradeAmount,
            barWidth: '35%',
            itemStyle: { borderRadius: [6, 6, 0, 0], color: '#f97316' },
            label: { show: true, position: 'top', formatter: (p: any) => p.value.toFixed(0) }
         },
         {
            name: '交易完成金额',
            type: 'bar',
            data: completedAmount,
            barWidth: '35%',
            itemStyle: { borderRadius: [6, 6, 0, 0], color: '#10b981' },
            label: { show: true, position: 'top', formatter: (p: any) => p.value.toFixed(0) }
         }
      ]
   });
   return chart;
};

// 异常商家
const renderAbnormalMerchants = () => {
   if (!abMerchantChartRef.value) return;
   const chart = echarts.init(abMerchantChartRef.value);
   chart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, valueFormatter: (value: any) => value + ' 家' },
      grid: { top: 35, left: 45, right: 15, bottom: 25, containLabel: true },
      xAxis: { type: 'category', data: months, axisLabel: { rotate: 25, fontSize: 11 } },
      yAxis: { type: 'value', name: '异常商家数', nameLocation: 'middle', nameGap: 35 },
      series: [
         {
            type: 'bar',
            data: abnormalMerchants,
            name: '异常商家',
            itemStyle: { color: '#ef4444', borderRadius: [6, 6, 0, 0] },
            barWidth: '60%',
            label: { show: true, position: 'top' }
         }
      ]
   });
   return chart;
};

// 异常用户
const renderAbnormalUsers = () => {
   if (!abUserChartRef.value) return;
   const chart = echarts.init(abUserChartRef.value);
   chart.setOption({
      tooltip: { trigger: 'axis', valueFormatter: (value: any) => value + ' 人' },
      grid: { top: 35, left: 45, right: 15, bottom: 25 },
      xAxis: { type: 'category', data: months, axisLabel: { rotate: 25, fontSize: 11 } },
      yAxis: { type: 'value', name: '异常用户数' },
      series: [
         {
            type: 'bar',
            data: abnormalUsers,
            name: '异常用户',
            itemStyle: { color: '#f59e0b', borderRadius: [6, 6, 0, 0] },
            barWidth: '60%',
            label: { show: true, position: 'top' }
         }
      ]
   });
   return chart;
};

// 异常订单
const renderAbnormalOrders = () => {
   if (!abOrderChartRef.value) return;
   const chart = echarts.init(abOrderChartRef.value);
   chart.setOption({
      tooltip: { trigger: 'axis', valueFormatter: (value: any) => value + ' 单' },
      grid: { top: 35, left: 45, right: 15, bottom: 25 },
      xAxis: { type: 'category', data: months, axisLabel: { rotate: 25, fontSize: 11 } },
      yAxis: { type: 'value', name: '异常订单数' },
      series: [
         {
            type: 'bar',
            data: abnormalOrders,
            name: '异常订单',
            itemStyle: { color: '#8b5cf6', borderRadius: [6, 6, 0, 0] },
            barWidth: '60%',
            label: { show: true, position: 'top' }
         }
      ]
   });
   return chart;
};

// 饼图: 冲突金额 vs 交易完成金额
const renderPieChart = () => {
   if (!pieChartRef.value) return;
   const chart = echarts.init(pieChartRef.value);
   chart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c}万元 ({d}%)' },
      legend: { orient: 'vertical', left: 'left', data: ['冲突金额', '交易完成金额'], textStyle: { fontWeight: 500 } },
      series: [
         {
            type: 'pie',
            radius: '55%',
            center: ['50%', '55%'],
            data: [
               { value: totalConflictAmount, name: '冲突金额', itemStyle: { color: '#f43f5e' } },
               { value: totalCompletedAmount, name: '交易完成金额', itemStyle: { color: '#22c55e' } }
            ],
            emphasis: { scale: true, label: { show: true, fontWeight: 'bold' } },
            label: { show: true, formatter: '{b}: {d}%', fontWeight: 'normal' },
            labelLine: { length: 12, length2: 8 }
         }
      ]
   });
   return chart;
};

// 统一初始化所有图表
const initCharts = () => {
   const charts = [
      renderParttimeOrders(),
      renderTransactionFlow(),
      renderAbnormalMerchants(),
      renderAbnormalUsers(),
      renderAbnormalOrders(),
      renderPieChart()
   ];
   chartInstances = charts.filter((c): c is echarts.ECharts => c !== undefined);
};

// 窗口 resize 处理
const handleResize = () => {
   chartInstances.forEach(chart => chart.resize());
};

onMounted(() => {
   initCharts();
   window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
   window.removeEventListener('resize', handleResize);
   chartInstances.forEach(chart => chart.dispose());
   chartInstances = [];
});
</script>

<style lang="scss" scoped>
.dashboard {
   max-width: 1600px;
   margin: 0 auto;
   padding: 24px;
   background: #f0f2f6;
   font-family: 'Inter', 'Segoe UI', 'PingFang SC', Roboto, 'Helvetica Neue', sans-serif;
}

.kpi-grid {
   display: grid;
   grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
   gap: 20px;
   margin-bottom: 28px;
}

.kpi-card {
   background: white;
   border-radius: 24px;
   padding: 20px 16px;
   box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
   transition: all 0.2s ease;
   border: 1px solid rgba(0, 0, 0, 0.05);
   backdrop-filter: blur(2px);
   &:hover {
      transform: translateY(-2px);
      box-shadow: 0 12px 20px rgba(0, 0, 0, 0.08);
   }
   .kpi-label {
      font-size: 14px;
      color: #5b6e8c;
      letter-spacing: 0.5px;
      margin-bottom: 12px;
      font-weight: 500;
   }
   .kpi-value {
      font-size: 32px;
      font-weight: 700;
      color: #1e293b;
      line-height: 1.2;
   }
   .kpi-unit {
      font-size: 14px;
      font-weight: 500;
      color: #7c8ea0;
      margin-left: 4px;
   }
}

.chart-card {
   background: white;
   border-radius: 28px;
   padding: 18px 16px 12px 16px;
   box-shadow: 0 8px 20px rgba(0, 0, 0, 0.02);
   border: 1px solid #eef2f6;
   transition: all 0.2s;
   .chart-title {
      font-size: 18px;
      font-weight: 600;
      color: #0f172a;
      margin-bottom: 16px;
      padding-left: 8px;
      border-left: 4px solid #3b82f6;
      letter-spacing: -0.2px;
   }
   .chart-container {
      width: 100%;
      height: 320px;
   }
   .small-chart {
      width: 100%;
      height: 280px;
   }
}

.row-2cols {
   display: grid;
   grid-template-columns: repeat(2, 1fr);
   gap: 24px;
   margin-bottom: 28px;
}

.row-3cols {
   display: grid;
   grid-template-columns: repeat(3, 1fr);
   gap: 24px;
   margin-bottom: 28px;
}

.pie-section {
   margin-top: 8px;
   margin-bottom: 20px;
   .pie-card {
      max-width: 550px;
      margin: 0 auto;
   }
}

.footer-note {
   text-align: center;
   font-size: 12px;
   color: #8ba0bc;
   margin-top: 32px;
   padding: 16px;
   border-top: 1px solid #e2e8f0;
}

@media (max-width: 1000px) {
   .row-2cols,
   .row-3cols {
      grid-template-columns: 1fr;
      gap: 20px;
   }
   .pie-card {
      max-width: 100%;
   }
   .dashboard {
      padding: 16px;
   }
}
</style>
