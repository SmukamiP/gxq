<template>
  <Layout>
    <Content id="serviceApply">
      <Breadcrumb>
        <BreadcrumbItem>运维服务管理</BreadcrumbItem>
        <BreadcrumbItem>服务申请</BreadcrumbItem>
      </Breadcrumb>
      <Card>
        <Form :model="searchForm" ref="searchForm" inline :label-width="80" id="serviceApplyForm">
          <FormItem label="服务类别" style="width: 220px;">
            <Select v-model="searchForm.serviceType">
              <Option value="">全部</Option>
              <Option v-for="item in typeList" :value="item.id" :key="item.id">{{item.type}}</Option>
            </Select>
          </FormItem>
          <FormItem label="服务时间">
            <DatePicker
              type="daterange"
              v-model="serviceTimeRange"
              placement="bottom-end"
              placeholder="请选择服务时间"
              :editable="false"
              style="width: 100%"></DatePicker>
          </FormItem>
          <FormItem label="评分" style="width: 280px;">
            <Input type="number" v-model="searchForm.startScore" placeholder="最小评分" style="width: 40%;"></Input>
            至 <Input type="number" v-model="searchForm.endScore" placeholder="最大评分" style="width: 40%;"></Input>
          </FormItem>
          <FormItem label="状态" style="width: 180px;">
            <Select v-model="searchForm.status">
              <Option v-for="item in allStatus" :value="item.value" :key="item.value">{{item.label}}</Option>
            </Select>
          </FormItem>
          <FormItem label="任务编号" style="width: 200px">
            <Input v-model="searchForm.applyCode"></Input>
          </FormItem>
          <FormItem :label-width="20">
            <Button type="primary" @click="preSearch">查询</Button>
            <Button
              type="primary"
              @click="addService"
              style="margin-left: 15px;"
              v-if="authButton.includes('ops_server_apply_add')">新建申请</Button>
          </FormItem>
        </Form>
        <hy-table
          highlight-row
          border
          :current="pageInfo.pageNo"
          :columns="tableList.columns"
          :data="tableList.data"
          :total="Number(pageInfo.total)"
          :pageSize="Number(pageInfo.pageSize)"
          pageType="small"
          show-elevator
          show-sizer
          class="access-list"
          ref="safetyEvalTable"
          @on-page-change="pageChange" />
      </Card>
    </Content>
    <serviceDocModal ref="serviceDocModal"></serviceDocModal>
    <pointModal ref="pointModal" @on-ok="search"></pointModal>
    <serviceApplyModal ref="serviceModal" @on-ok="addSuccess"></serviceApplyModal>
  </Layout>
</template>
<script>
import {mapState} from 'vuex'
import serviceDocModal from './serviceApplyModal/serviceDocModal.vue'
import pointModal from './serviceApplyModal/pointModal.vue'
import serviceApplyModal from './serviceApplyModal/serviceApplyModal.vue'
import api from '@/api/axiosApi'
import operationApiList from '@/api/operationApiList'

const allStatus = [{
  value: '',
  label: '全部'
}, {
  value: 1,
  label: '未处理'
}, {
  value: 2,
  label: '已驳回'
}, {
  value: 3,
  label: '已同意'
}, {
  value: 4,
  label: '已完结'
}]

// function getDateRange(time) {
//   if (!time) {
//     return ''
//   }
//   // 结束日期
//   const endDate = new Date(time)
//   // 当前日期往前推30天
//   const startDate = new Date(time - 30 * 24 * 60 * 60 * 1000)
//   return {
//     start: `${startDate.getFullYear()}-${startDate.getMonth() + 1}-${startDate.getDate()}`,
//     end: `${endDate.getFullYear()}-${endDate.getMonth() + 1}-${endDate.getDate()}`
//   }
// }

function convertDateTime(date) {
  if (!date) return ''
  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
}

function defaultDisplay(h, params, key) {
  return h('span', params.row[key] || '--')
}

export default {
  components: {
    serviceDocModal,
    pointModal,
    serviceApplyModal
  },
  data () {
    const vm = this
    return {
      allStatus,
      serviceDoc: false,
      point: false,
      serviceApply: false,
      searchForm: {
        serviceType: '',
        status: '',
        applyCode: this.$route.query.code || ''
      },
      tableList: {
        columns: [{
          title: '序号',
          type: 'index',
          width: 80,
          align: 'center'
        }, {
          title: '任务编号',
          key: 'applyCode'
        }, {
          title: '服务类别',
          key: 'serviceType'
        }, {
          title: '服务名称',
          key: 'serviceName'
        }, {
          title: '涉及系统',
          key: 'systemName',
          render: (h, params) => {
            const systemName = params.row.systemName || []
            const names = []
            systemName.map(item => {
              names.push(item.systemName)
            })
            // return (<span>{names.join(',')}</span>)
            return h('div', [
              h('span', {
                style: {
                  display: 'inline-block',
                  width: params.column._width * 0.9 + 'px',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap'
                },
                domProps: {
                  title: names.join(',')
                }
              }, names.join(','))
				// 　　　　h('Tooltip', {
				// 　　　　props: { placement: 'top' }
				// 　　　　}, [
				// 　　　　h('span', {
				// 　　　　　　style: {
				// 　　　　　　　　display: 'inline-block',
				// 　　　　　　　　width: params.column._width * 0.9 + 'px',
				// 　　　　　　　　overflow: 'hidden',
				// 　　　　　　　　textOverflow: 'ellipsis',
				// 　　　　　　　　whiteSpace: 'nowrap',
				// 　　　　　　},
				// 　　　　　　},  names.join(',')),
				// 　　　　　　h('span', {
				// 　　　　　　　　slot: 'content',
				// 　　　　　　　　style: { whiteSpace: 'normal', wordBreak: 'break-all' }
				// 　　　　　　}, names.join("，"))
				// 　　　　])
				　　])
          }
        }, {
          title: '服务时间',
          key: 'startTime',
          align: 'center',
          render: (h, params) => {
            const { startTime, endTime } = params.row
            return (
              <div>
                <p>{startTime}</p>
                <p>至</p>
                <p>{endTime}</p>
              </div>
            )
          }
        }, {
          title: '结束时间',
          key: 'endTime'
        }, {
          title: '状态',
          key: 'status',
          render: (h, params) => {
            let label = '--'
            if (params.row.status) {
              label = allStatus[params.row.status].label
            }
            return h('span', label)
          }
        }, {
          title: '评分',
          key: 'score',
          maxWidth: 80
        }, {
          title: '操作',
          type: 'act',
          minWidth: 140,
          align: 'center',
          render: (h, params) => {
            const row = params.row
            const detail = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px',
                marginTop: '5px'
              },
              on: {
                click: () => {
                  vm.$router.push({
                    name: 'serviceView',
                    params: {
                      id: row.id
                    }
                  })
                }
              }
            }, '详情')
            const report = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px',
                marginTop: '5px'
              },
              on: {
                click: () => {
                  vm.$refs.serviceDocModal.open(row.id)
                }
              }
            }, '服务报告')
            const rate = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginRight: '5px',
                marginTop: '5px'
              },
              on: {
                click: () => {
                  const rate = {
                    serviceAttitude: row.serviceAttitude,
                    serviceEfficiency: row.serviceEfficiency,
                    serviceEvaluation: row.serviceEvaluation
                  }
                  vm.$refs.pointModal.open(row.id, rate)
                }
              }
            }, '评价')
            const edit = h('Button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              style: {
                marginTop: '5px',
                marginRight: '5px'
              },
              on: {
                click: () => {
                  this.$refs.serviceModal.open(row.id)
                }
              }
            }, '修改')
            const btns = []
            switch (row.status) {
              case 1:
              case 3:
                if (vm.authButton.includes('ops_server_apply_detail')) {
                  return h('div', [detail])
                }
                break
              case 2:
                if (vm.authButton.includes('ops_server_apply_update')) {
                  btns.push(edit)
                }
                if (vm.authButton.includes('ops_server_apply_detail')) {
                  btns.push(detail)
                }
                return h('div', btns)
              case 4:
                if (vm.authButton.includes('ops_server_apply_detail')) {
                  btns.push(detail)
                }
                if (vm.authButton.includes('ops_server_apply_servicereport')) {
                  btns.push(report)
                }
                if (vm.authButton.includes('ops_server_apply_evaluate') && !(row.score || row.score == 0)) {
                  btns.push(rate)
                }
                return h('div', btns)
            }
            return h('div', '--')
          }
        }],
        data: []
      },
      // 服务类型列表
      typeList: [],
      serviceTimeRange: [],
      pageInfo: {
        pageNo: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  computed: {
    ...mapState(['authButton'])
  },
  mounted () {
    this.search()
    this.getTypeList()
  },
  methods: {
    preSearch() {
        this.pageInfo.pageNo = 1
        this.search()
    },
    pageChange(pageNo, pageSize) {
      this.pageInfo.pageNo = pageNo
      this.pageInfo.pageSize = pageSize
      this.search()
    },
    // 获取服务类型列表
    getTypeList() {
      const vm = this
      api(operationApiList.getServiceType, {
        level: 1,
        parentId: -1
      }).then(res => {
        if(res.data.errcode === 0) {
          vm.typeList = res.data.data
        }
      }, error => { console.log(error) })
    },
    search () {
      this.getList()
    },
    // 获取列表
    getList (pageNo, pageSize) {
      const vm = this
      const endTime = convertDateTime(vm.serviceTimeRange[1])
      const startTime = convertDateTime(vm.serviceTimeRange[0])
      api(operationApiList.servicePage, {
        pageNo: pageNo || vm.pageInfo.pageNo,
        pageSize: pageSize || vm.pageInfo.pageSize,
        data: {
          endTime: endTime ? `${endTime} 23:59:59` : '',
          startTime: startTime ? `${startTime} 00:00:00` : '',
          ...vm.searchForm
        }
      }).then(res => {
        if (res.data.errcode === 0) {
          const result = res.data.data
          vm.pageInfo.pageNo = result.pageNum
          vm.pageInfo.total = result.total
          vm.tableList.data = result.list
        }
      }, error => { console.log(error) })
    },
    addService () { // 新建服务申请/修改服务申请
      this.$refs.serviceModal.open()
    },
    addSuccess () {
      this.search()
    }
  }
}
</script>
<style lang="less" scoped="scoped">
  .act-btn-group {
    button {
      margin: 0 12px;
    }
  }
  .ivu-modal-footer {
    display: none;
  }
</style>

<style lang="less">
#serviceApplyForm .ivu-input-wrapper{
  width: 100%;
}
#serviceApply{
  .ivu-card-body{
    min-height: 360px;
  }
}
</style>
