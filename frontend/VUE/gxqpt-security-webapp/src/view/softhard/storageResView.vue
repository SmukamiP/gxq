<!-- 存储资源面板 -->
<template>
  <Layout>
    <Content>
      <Breadcrumb>
        <BreadcrumbItem>存储资源面板</BreadcrumbItem>
      </Breadcrumb>
      <Card>
        <div class="card-container">
          <div class="card-list">
            <Card style="height:160px;background: #fff;color:#495060;">
              <div class="card-content" style="width:100%;">
                <p style="line-height: 42px;"><span style="color:#0099CC">内存：</span>{{memData.memSize}}GB</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>本月新增：</span>{{memData.monthSave}}GB</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>新增占比：</span>{{memData.saveOccupationRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>新增环比：</span>{{memData.saveRingRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>净增：</span>{{memData.netGrowth}}GB</p>
              </div> 
              <div class="card-content">
                <p style="line-height: 42px;"><span>本月回收：</span>{{memData.monthRemove}}GB</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>回收占比：</span>{{memData.removeOccupationRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>回收环比：</span>{{memData.removeRingRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>净增占比：</span>{{memData.netGrowthOccupationRatio}}%</p>
              </div>
            </Card>
            <Card style="height:160px;margin-top:15px;background: #fff;color:#495060;">
              <div class="card-content" style="width:100%;">
                <p style="line-height: 42px;"><span style="color:#0099CC">磁盘：</span>{{diskData.memSize}}GB</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>本月新增：</span>{{diskData.monthSave}}GB</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>新增占比：</span>{{diskData.saveOccupationRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>新增环比：</span>{{diskData.saveRingRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>净增：</span>{{diskData.netGrowth}}GB</p>
              </div> 
              <div class="card-content">
                <p style="line-height: 42px;"><span>本月回收：</span>{{diskData.monthRemove}}GB</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>回收占比：</span>{{diskData.removeOccupationRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>回收环比：</span>{{diskData.removeRingRatio}}%</p>
              </div>
              <div class="card-content">
                <p style="line-height: 42px;"><span>净增占比：</span>{{diskData.netGrowthOccupationRatio}}%</p>
              </div>
            </Card>
          </div>
        </div>
         <Row type="flex" justify="space-between">
          <Col style="width: 49%">
            <chart-card title="各部门内存资源容量统计">
              <barChart ref="memoryCount">
                <div id="memoryCount" style="height: 540px;"></div>
              </barChart>
            </chart-card>
          </Col>
          <Col style="width: 49%">
            <chart-card title="各部门磁盘资源容量统计">
              <barChart ref="diskCount">
                <div id="diskCount" style="height: 540px;"></div>
              </barChart>
            </chart-card>
          </Col>
          <Col style="width: 49%">
            <chart-card title="各部门内存资源容量变动统计">
              <chartLine ref="memCountLastYear" el="memCountLastYear" :lineOption="memCountLastYear">
                <div id="memCountLastYear" style="height: 400px;"></div>
              </chartLine>
            </chart-card>
          </Col>
          <Col style="width: 49%">
            <chart-card title="各部门磁盘资源容量变动统计">
              <chartLine ref="diskCountLastYear" el="diskCountLastYear" :lineOption="diskCountLastYear">
                <div id="diskCountLastYear" style="height: 400px;"></div>
              </chartLine>
            </chart-card>
          </Col>
        </Row>
      </Card>
    </Content>
  </Layout>
</template>

<script>
// echart图外层容器，包括卡片样式
import chartCard from '@/view/home/chartCard.vue'
import lineChart from '@/view/home/lineChart.vue'
import barChart from '@/view/home/barChart.vue'
import chartLine from './common/ChartLine.vue'
import api from '@/api/axiosApi'
import softhardApiList from '@/api/softhardApiList'
import {dataPie,multipleLine} from '@/assets/js/echartOption'
export default {
  components:{
    chartCard,
    lineChart,
    barChart,
    chartLine
  },
  data() {
    return {
      memData:{
        memSize :0,//数据库数量
        monthSave: 0,//新增
        monthRemove: 0,//回收
        saveOccupationRatio: 0,//新增占比
        removeOccupationRatio:0,//回收占比
        saveRingRatio: 0,//新增环比
        removeRingRatio:0,//回收环比
        netGrowth: 0,//净增
        netGrowthOccupationRatio: 0,//净增占比
      },
      diskData:{
        memSize :0,//数据库数量
        monthSave: 0,//新增
        monthRemove: 0,//回收
        saveOccupationRatio: 0,//新增占比
        removeOccupationRatio:0,//回收占比
        saveRingRatio: 0,//新增环比
        removeRingRatio:0,//回收环比
        netGrowth: 0,//净增
        netGrowthOccupationRatio: 0,//净增占比
      },
      databaseType: dataPie(),
      memCountLastYear: multipleLine(),
      diskCountLastYear: multipleLine(),
    }
  },
  mounted () {
    this.init();
  },
  methods: {
    init(){//初始化
      this.getStorageMemData();
      this.getStorageDiskData();
      this.findMemByDpm();
      this.findDiskByDpm();
      this.findDiskByDpmAndLastYare();
      this.findMemByDpmAndLastYare();
    },
    getStorageMemData() {//存储面板内存信息统计
      api(softhardApiList.getStorageMemData).then((res) => {
        if(res.status == 200 && res.data.data) {
          let data = res.data.data;
          this.memData.memSize = data.memSize ;
          this.memData.monthSave = data.monthSave;
          this.memData.monthRemove = data.monthRemove;
          this.memData.saveOccupationRatio = data.saveOccupationRatio.toFixed(2);
          this.memData.removeOccupationRatio = data.removeOccupationRatio.toFixed(2);
          this.memData.saveRingRatio  = data.saveRingRatio.toFixed(2);
          this.memData.removeRingRatio  = data.removeRingRatio.toFixed(2);
          this.memData.netGrowth  = data.netGrowth;
          this.memData.netGrowthOccupationRatio  = data.netGrowthOccupationRatio.toFixed(2);
        }
      }, (err) => {})
    },
    getStorageDiskData() {//存储面板磁盘信息统计
      api(softhardApiList.getStorageDiskData).then((res) => {
        if(res.status == 200 && res.data.data) {
          let data = res.data.data;
          this.diskData.memSize = data.memSize ;
          this.diskData.monthSave = data.monthSave;
          this.diskData.monthRemove = data.monthRemove;
          this.diskData.saveOccupationRatio = data.saveOccupationRatio.toFixed(2);
          this.diskData.removeOccupationRatio = data.removeOccupationRatio.toFixed(2);
          this.diskData.saveRingRatio  = data.saveRingRatio.toFixed(2);
          this.diskData.removeRingRatio  = data.removeRingRatio.toFixed(2);
          this.diskData.netGrowth  = data.netGrowth;
          this.diskData.netGrowthOccupationRatio  = data.netGrowthOccupationRatio.toFixed(2);
        }
      }, (err) => {})
    },
    findMemByDpm() {//存储面板部门内存统计图
      api(softhardApiList.findMemByDpm).then((res) => {
        if(res.status == 200 && res.data.data) {
          this.memoryCount(res.data.data.x,res.data.data.y);
        }
      }, (err) => {})
    },
    findDiskByDpm() {//存储面板部门磁盘统计图
      api(softhardApiList.findDiskByDpm).then((res) => {
        if(res.status == 200 && res.data.data) {
          this.diskCount(res.data.data.x,res.data.data.y);
        }
      }, (err) => {})
    },
    findMemByDpmAndLastYare() {//存储面板部门近一年内存统计图
      api(softhardApiList.findMemByDpmAndLastYare).then((res) => {
        if(res.status == 200 && res.data.data) {
          console.log(res.data.data.series);
          res.data.data.series.forEach((item,index) => {
            item.type =  'line';
            item.stack =  '总量';
          });
          this.getMemCountLastYear(res.data.data.legend,res.data.data.xAxis,res.data.data.series)
        }
      }, (err) => {})
    },
    findDiskByDpmAndLastYare() {//存储面板部门近一年磁盘统计图
      api(softhardApiList.findDiskByDpmAndLastYare).then((res) => {
        if(res.status == 200 && res.data.data) {
          console.log(res.data.data.series);
          res.data.data.series.forEach((item,index) => {
            item.type =  'line';
            item.stack =  '总量';
          });
          this.getDiskCountLastYear(res.data.data.legend,res.data.data.xAxis,res.data.data.series)
        }
      }, (err) => {})
    },
    memoryCount (name,arr) {
      const vm = this
      const opts = {
        el: 'memoryCount',
        legend: {
          left: 'center',
          bottom: 10,
          textStyle: {
            fontSize: '16px'
          },
          data: name
        },
        xAxis: {
          data: name,
          axisLabel: {
            interval:0,
            rotate:40
          }
        },
        yAxis: {
          name: '容量/GB',
          min: 0,
          max: Math.max.apply(null, arr) + (Math.max.apply(null, arr)*0.2),
        },
        series: [{
          name: '内存资源容量',
          data: arr,
          barWidth : (arr.length > 12) ? 20 : 40,
          itemStyle:{
              normal:{//柱子颜色
                  color:'#4F81BD'
              }
          },
        }]
      }
      vm.$refs.memoryCount.refresh(opts)
    },
    diskCount (name,arr) {
      const vm = this
      const opts = {
        el: 'diskCount',
        legend: {
          left: 'center',
          bottom: 10,
          textStyle: {
            fontSize: '16px'
          },
          data: name
        },
        xAxis: {
          data: name,
          axisLabel: {
            interval:0,
            rotate:40
          }
        },
        yAxis: {
          name: '容量/GB',
          min: 0,
          max: Math.max.apply(null, arr) + (Math.max.apply(null, arr)*0.2),
        },
        series: [{
          name: '磁盘资源容量',
          data: arr,
          barWidth : (arr.length > 12) ? 20 : 40,
          itemStyle:{
              normal:{//柱子颜色
                  color:'#4F81BD'
              }
          },
        }]
      }
      vm.$refs.diskCount.refresh(opts)
    },
    // getDatabaseType (legendName,dataVal) {
    //   this.databaseType.legend.data = legendName;
    //   this.databaseType.series[0].data = dataVal;
    //   this.databaseType.title.text = '各部门计算资源占比统计';
    //   this.$refs.databaseType.refresh()
    // },
    getMemCountLastYear (legendName,timeData,dataVal) {
      this.memCountLastYear.legend.data = legendName;
      this.memCountLastYear.xAxis.data = timeData;
      this.memCountLastYear.series = dataVal;
      // this.memCountLastYear.title.text = '各部门近12个月计算资源容量统计';
      this.$refs.memCountLastYear.refresh()
    },
    getDiskCountLastYear (legendName,timeData,dataVal) {
      this.diskCountLastYear.legend.data = legendName;
      this.diskCountLastYear.xAxis.data = timeData;
      this.diskCountLastYear.series = dataVal;
      // this.memCountLastYear.title.text = '各部门近12个月计算资源容量统计';
      this.$refs.diskCountLastYear.refresh()
    },
  }
}
</script>

<style lang="less" scoped>
.card-container{
  &:after{
    content: '';
    clear: both;
    display: block;
  }
  .card-list{
    float: left;
    width: 100%;
    margin-left: 4%;
    text-align: center;
    &:nth-child(1){
      margin-left: 0;
    }
    .card-content{
      width:25%;
      float:left;
      display: inline-block;
      text-align: center;
      font-size: 18px;
      font-weight: bold;
      height: 42px;
    }
  }
}
</style>

