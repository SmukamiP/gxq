<template>
  <Layout>
    <Content>
      <Breadcrumb>
        <!-- <BreadcrumbItem>管理员</BreadcrumbItem> -->
        <BreadcrumbItem>申请管理</BreadcrumbItem>
      </Breadcrumb>
      <serverCreate :disab="disab"></serverCreate>
      <!-- <specificInfo v-if="step=='MANAGE_AUTH_EXT'" :disab="disab"></specificInfo> -->
    </Content>
  </Layout>
</template>

<script>
// 新增服务的表单
import serverCreate from './../common/serverCreate'
import specificInfo from './../common/specificInfo'
export default {
  components: {
    serverCreate,
    specificInfo
  },
  data(){
    return{
      disab:true,
      step:this.$route.params.departData.stepCode
    }
  }
}
</script>