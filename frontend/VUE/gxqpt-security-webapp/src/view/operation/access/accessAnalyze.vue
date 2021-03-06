<template>
  <Layout>
    <Content>
      <Breadcrumb>
        <BreadcrumbItem>接入管理</BreadcrumbItem>
        <BreadcrumbItem>应用分析</BreadcrumbItem>
      </Breadcrumb>
      <Card>
        <Form
          ref="formValidate"
          inline
          :label-width="100"
          :model="searchCondition">
          <FormItem label="时间：">
            <Select v-model="searchCondition.timeType" style="width:200px">
              <Option v-for="item in timeList" :value="item.value" :key="item.value">{{item.label}}</Option>
            </Select>
          </FormItem>
          <FormItem :label-width="20">
            <Button type="primary" @click="goBack">返回</Button>
          </FormItem>
        </Form>
        <Row>
          <Col span="6" :offset="1">
            <Gauge el="appRate" ref="appRate" :gaugeOption="appScoreOption" title="应用评分">
              <div id="appRate"></div>
            </Gauge>
          </Col>
          <Col span="6" :offset="1">
            <Gauge el="bugDealTime" ref="bugDealTime" :gaugeOption="faultTimeAvgOption" title="故障处理平均时长(小时)">
              <div id="bugDealTime"></div>
            </Gauge>
          </Col>
          <Col span="6" :offset="1">
            <Gauge el="bugNum" ref="bugNum" :gaugeOption="untreatedFaultSumOption" title="未处理故障数(个)">
              <div id="bugNum"></div>
            </Gauge>
          </Col>
        </Row>
        <Row style="margin-top: 90px;">
          <Col span="10" :offset="1">
            <h3>故障产生数量分析</h3>
            <ChartLine el="bugNumAna" ref="bugNumAna" :lineOption="bugNumOption">
              <div id="bugNumAna"></div>
            </ChartLine>
          </Col>
          <Col span="10" :offset="1">
            <h3>故障级别分析</h3>
            <pieChart ref="bugTypeAna" v-show="hasBugs">
              <div id="bugTypeAna"></div>
            </pieChart>
            <div class="no-bugs" v-show="!hasBugs">
              <span>暂无故障</span>
            </div>
          </Col>
        </Row>
      </Card>
    </Content>
  </Layout>
</template>

<script>
// 3d饼图插件
// import * as d3 from 'd3'
// import Donut3D from '@static/echart/3dpie/3dpie1.js'
// import '@static/echart/3dpie/3dpie2.js'

import api from '@/api/axiosApi'
import operationApiList from '@/api/operationApiList'
// 仪表盘
import Gauge from './../echarts/Gauge.vue'
// 折线
import ChartLine from './../echarts/ChartLine.vue'
// 饼图
import pieChart from '@/view/home/pieChart'
import {dataPie} from '@/assets/js/echartOption'

const timeList = [{
  label: '最近一天',
  value: 1,
  unit: '日期(小时)'
}, {
  label: '最近一周',
  value: 2,
  unit: '日期(天)'
}, {
  label: '最近一月',
  value: 3,
  unit: '日期(天)'
}, {
  label: '最近一年',
  value: 4,
  unit: '日期(月)'
}]

const level = [{
  key: '级别1',
  class: 'legend-1'
}, {
  key: '级别2',
  class: 'legend-2'
}, {
  key: '级别3',
  class: 'legend-3'
}]

function getGaugeOption () {
  return {
    tooltip: {
      formatter: ''
    },
    series: [{
      name: '',
      type: 'gauge',
      detail: {formatter:'{value}'},
      min: 0,
      max: 100,
      // data: [{value: 50, name: '完成率'}],
      axisLine: {
        show: true,
        lineStyle:{
          // width: '10',
          color: [[0.6, '#C23531'], [0.8, '#63869E'], [1, '#91C7AE']]
        }
      }
    }]
  }
}

const lineOption = {
  grid: {
    width: '76%',
    left: 40
  },
  xAxis: {
    type: 'category',
    axisTick: {
      show: false
    }
    // data: ['1', '2', '3', '4', '5', '6', '7']
  },
  yAxis: {
    name: '故障数量(个)',
    axisTick: {
      show: false
    }
    // data: ['1', '2', '3', '4', '5', '6', '7']
  },
  series: [{
    // data: [820, 600, 570, 409, 322, 310, 200],
    type: 'line'
  }]
}

export default {
  components: {
    Gauge,
    ChartLine,
    pieChart
  },
  data () {
    const vm = this
    return {
      pieOption: dataPie(),
      level,
      hasBugs: false,
      appScoreOption: {
        ...getGaugeOption()
      },
      faultTimeAvgOption: {
        ...getGaugeOption()
      },
      untreatedFaultSumOption: {
        ...getGaugeOption()
      },
      bugNumOption: {
        ...lineOption
      },
      timeList,
      searchCondition: {
        // 时间
        timeType: 1,
        // 应用id
        appId: vm.$route.query.id
      }
    }
  },
  mounted() {
    this.search()
  },
  methods: {
    search () {
      const vm = this
      api(operationApiList.faultFindAnalyse, {
        ...vm.searchCondition
      }).then(res => {
        if (res.data.errcode === 0) {
          const {appScore, faultLevel, faultSumX, faultSumY, faultTimeAvg, untreatedFaultSum} = res.data.data

          // 应用评分
          vm.appScoreOption.series[0].data = [{
            value: parseInt(appScore) || 0
          }]

          // 故障处理时间
          vm.faultTimeAvgOption.series[0].data = [{
            value: Number(faultTimeAvg) || 0
          }]
          vm.faultTimeAvgOption.series[0].axisLine = {
            show: true,
            lineStyle:{
              // width: '10',
              color: [[0.08, '#91C7AE'], [0.33, '#63869E'], [1, '#C23531']]
            }
          }
          vm.faultTimeAvgOption.series[0].min = 0
          vm.faultTimeAvgOption.series[0].max = 72

          // 未处理故障数
          vm.untreatedFaultSumOption.series[0].data = [{
            value: parseInt(untreatedFaultSum) || 0
          }]
          vm.untreatedFaultSumOption.series[0].axisLine = {
            show: true,
            lineStyle:{
              // width: '10',
              color: [[0.1, '#91C7AE'], [0.33, '#63869E'], [1, '#C23531']]
            }
          }
          vm.untreatedFaultSumOption.series[0].min = 0
          vm.untreatedFaultSumOption.series[0].max = 30

          // 故障数量分析X轴
          vm.bugNumOption.xAxis.data = faultSumX
          vm.bugNumOption.xAxis.name = timeList[vm.searchCondition.timeType - 1].unit
          // 故障数量分析Y轴
          vm.bugNumOption.series[0].data = faultSumY
          // 故障级别分析
          vm.drawPie(faultLevel)

          vm.$refs.appRate.refresh()
          vm.$refs.bugDealTime.refresh()
          vm.$refs.bugNum.refresh()
          vm.$refs.bugNumAna.refresh()
        }
      },(error) => {console.log(error)})
    },
    goBack() {
      this.$router.go(-1)
    },
    drawPie(data) {
      const vm = this
      const salesData = [{
        label: "级别1",
        // color: "#E97C30"
      }, {
        label: "级别2",
        // color: "#5A99D3"
      }, {
        label: "级别3",
        // color: "#A3A3A3"
      }];
      vm.hasBugs = false
      const datas = []
      salesData.map((item, idx) => {
        datas.push({
          name: item.label,
          value: data[idx],
          // color: item.color
        })
        if(data[idx]){
          vm.hasBugs = true
        }
      })
      if (!vm.hasBugs || datas.length == 0) {
        return
      }

      vm.$nextTick(() => {
        vm.pieOption.el = 'bugTypeAna'
        vm.pieOption.color = ['#E97C30', '#A3A3A3', '#5A99D3']
        vm.pieOption.tooltip.formatter = '{a} <br/>{b} : {c}%'
        vm.pieOption.legend.data = ['级别1', '级别2', '级别3']
        vm.pieOption.series[0].data = datas
        vm.pieOption.series[0].name = '故障级别分析'
        vm.pieOption.title.show = false
        vm.$refs.bugTypeAna.refresh(vm.pieOption)
      })
    }
  },
  watch: {
    'searchCondition.timeType' (val, oldVal) {
      this.search()
    }
  }
}
</script>

<style lang='less'>
.access-upgrade{
  span.handle{
    margin: 0 5px;
    display: inline-block;
  }
}
</style>

<style lang='less' scoped>
#appRate, #bugDealTime, #bugNum, #bugNumAna, #bugTypeAna, .no-bugs{
  height: 320px;
  text-align: center;
  line-height: 320px;
}
</style>
