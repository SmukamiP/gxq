<template>
	<Layout>
		<Content>
			<Breadcrumb>
				<BreadcrumbItem>服务器监控</BreadcrumbItem>
				<BreadcrumbItem>服务器访问监控</BreadcrumbItem>
			</Breadcrumb>
			<Card>
				<Form ref="formValidate" inline :label-width="76" :model="searchFormData">
					<FormItem label="服务器/账号">
						<Input type="text" v-model="searchFormData.serverIpOrAccount">
						</Input>
					</FormItem>
					<FormItem label="访问日期">
						<DatePicker type="daterange" :value="searchDateArray" :editable="false" @on-change="begDateChange"></DatePicker>
					</FormItem>
					<!--<FormItem label="至" :label-width="20">
						<DatePicker type="date" :value="searchFormData.endDate" :editable="false" @on-change="endDateChange"></DatePicker>
					</FormItem>-->
					<FormItem :label-width="30">
						<Button type="primary" @click="search" v-if="checkButton('security_server_monitor_access_search')">查询</Button>
					</FormItem>
				</Form>
				<hy-table border ref="table" :data="data" :columns="columns" :current="pageOption.current" :total="pageOption.total" :page-size="pageOption.pageSize" @on-change="pageChange" @on-page-size-change="changePageSize" show-sizer show-elevator/>
			</Card>
		</Content>
	</Layout>
</template>

<script>
	import api from '@/api/axiosApi'
	import apiList from '@/api/securityApiList'
	import { mapState } from 'vuex'
	export default {
		data() {
			return {
			  searchDateArray: [],
				searchFormData: {
					begDate:'',
					endDate:''
				},
				pageOption: { //分页配置参数
					current: 1,
					total: 0,
					pageSize: 10
				},
				columns: [{
						type: 'index',
						title: '序号',
						width: 60,
						align: 'center'
					},
					{
						title: '服务器地址',
						key: 'toServerIp'
					},
					// {
					// 	title: '目标服务器',
					// 	key: 'toServerIp'
					// },
					{
						title: '登录账户',
						key: 'account'
					},
					{
						title: '登录日期',
						key: 'loginTime'
					},
					{
						title: '退出日期',
						key: 'logoutTime'
					},
					{
						title: '登录时长（分钟）',
						key: 'loginDuration'
					}
				],
				data: []
			}
		},
		computed:{
			...mapState([
				'authButton'
			])
		},
		methods: {
			init(){
				//设置日期控件默认值
        let d = new Date();
        let day = d.getDate()
        if (day < 10) day = '0' + day
				this.searchFormData.begDate = d.getFullYear()+'-'+d.getMonth()+'-'+day;
				this.searchFormData.endDate = d.getFullYear()+'-'+(d.getMonth()+1)+'-'+day;
				this.searchDateArray = [this.searchFormData.begDate, this.searchFormData.endDate]
				this.getServerAccess(0);
      },
      search() {
				this.pageOption.current=1;
				this.getServerAccess(1);
      },
			getServerAccess(type){
				let params = {
					data: JSON.parse(JSON.stringify(this.searchFormData)),
					pageNo: this.pageOption.current,
					pageSize: this.pageOption.pageSize
        }
        let endDay = (parseInt(params.data.endDate.substring(params.data.endDate.length-2)) + 1).toString()
        if (parseInt(endDay) < 10) endDay = '0' + endDay
        params.data.endDate = params.data.endDate.substring(0, 8) + endDay
				api(apiList.getServerAccess,params).then((res)=>{
					if(res.status == 200){
						this.data =  res.data.data.list;
						this.pageOption.total = res.data.data.total;
					}
				},(err)=>{
					//do something...
				})
			},
			begDateChange(fdate,type){
			  if(fdate.length > 0) {
			    this.searchFormData.begDate = fdate[0];
			    this.searchFormData.endDate = fdate[1];
			    this.searchDateArray = fdate
			  }else{
			    this.searchFormData.begDate = '';
          this.searchFormData.endDate = '';
          this.searchDateArray = []
			  }
			},
			endDateChange(fdate,type){
				this.searchFormData.endDate = fdate;
			},
			pageChange(num) { //页码改变的回调
				this.pageOption.current = num;
				this.getServerAccess();
			},
			changePageSize(num) { //切换每页条数时的回调
				this.pageOption.pageSize = num;
				this.getServerAccess();
			},
			checkButton(code){
				if(this.authButton.indexOf(code) >= 0){
					return true;
				}else{
					return false;
				}
			}
		},
		created(){
			this.init();
		}
	}
</script>

<style lang="less" scoped>
	.page-split {
		margin-top: 10px;
	}
</style>