<template>
	<Layout>
		<Content>
			<Breadcrumb>
				<!-- <BreadcrumbItem>管理员（大数据办）</BreadcrumbItem> -->
				<BreadcrumbItem>{{title}}</BreadcrumbItem>
			</Breadcrumb>
			<Card>
				<Form :model="serverData" ref="searchformData" inline :label-width="100">
					<FormItem label="主机IP：">
						<Input v-model="serverData.ip"></Input>
					</FormItem>
					<FormItem label="型号：">
						<Input v-model="serverData.model"></Input>
					</FormItem>
					<FormItem label="处理器数量：">
						<Input v-model="serverData.cpuType"></Input>
					</FormItem>
					<FormItem label="内存大小：">
						<Input v-model="serverData.memory"></Input>
					</FormItem>
					<FormItem label="网卡数量：">
						<Input v-model="serverData.netcardCount"></Input>
					</FormItem>
					<FormItem :label-width="20">
						<Button type="primary" @click="search">查询</Button>
					</FormItem>
				</Form>
				<hy-table ref="selection" :data="data" :columns="columns" :current="pageOption.current" :total="pageOption.total" :page-size="pageOption.pageSize" @on-change="pageChange" @on-page-size-change="changePageSize" show-sizer show-elevator/>
			</Card>
		</Content>
	</Layout>
</template>
<script>
	import api from '@/api/axiosApi'
	import softhardApiList from '@/api/softhardApiList'
	export default {
		data() {
			return {
				title:this.$store.state.title,
				serverData: { //查询参数
					ip: '',
					model: '',
					cpuCount: null,
					memory: null,
					netcardCount: null,
				},
				pageOption: { //分页参数
					current: 1,
					total: 0,
					pageSize: 10
				},
				columns: [{
					title: '序号',
					type: 'index',
					width: 60,
					align: 'center'
				}, {
					title: '主机IP',
					key: 'ip'
				}, {
					title: '所属集群',
					key: 'cluster'
				}, {
					title: '型号',
					key: 'model'
				}, {
					title: '处理器类型',
					key: 'cpuType'
				}, {
					title: '处理器数量',
					key: 'cpuNum',
					width: 110
				}, {
					title: '网卡数量',
					key: 'netcardCount',
					width: 90
				}, {
					title: '内存',
					key: 'memory',
					width: 60
				}, {
					title: '磁盘大小',
					key: 'diskSize',
					width: 90
				}],
				data: [], //表格数据
			}
		},
		mounted() {
			this.findMachineList(); //查询主机列表
		},
		methods: {
			search() { // 点击查询按钮
				this.pageOption.current=1;
				this.findMachineList();
			},
			findMachineList() { // 查询主机列表
				this.data = [];
				var formData = {
					"data": { ...this.searchCondition
					}, //Object.assign({}, this.searchCondition),
					"pageNo": this.pageOption.current,
					"pageSize": this.pageOption.pageSize
				};
				api(softhardApiList.findMachineList, formData).then((res) => {
					if(res.status == 200 && res.data.data) {
						this.data = res.data.data.list;
						if(this.data.length > 0) {
							this.pageOption.pageSize = res.data.data.pageSize;
							this.pageOption.total = res.data.data.total;
							this.pageOption.current = res.data.data.pageNum;
						};
					}
				}, (err) => {
					//dong something...
				})
			},
			pageChange(num) { //页码改变的回调
				this.pageOption.current = num;
				this.findMachineList();
			},
			changePageSize(num) { //切换每页条数时的回调
				this.pageOption.pageSize = num;
				this.findMachineList();
			},
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
	
	.access-list {
		span.handle {
			margin: 0 5px;
			display: inline-block;
			cursor: pointer;
			&:hover {
				color: #57a3f3;
			}
		}
	}
</style>