<!--
  服务器配置项，根据hidecode来判断隐藏哪些项
  itemCode = [
    'CPU',
    '内存',
    '操作系统',
    '所属网络',
    '网络带宽',
    '网关描述',
    '数据盘',
    '申请数量'
  ]
-->
<template>
  <div id="saveConfig">
    <Form ref="apply" :label-width="100" :model="applyServer" :rules="ruleValidate">
      <FormItem label="CPU：" prop="cpuCount">
        <RadioGroup v-model="applyServer.cpuCount">
          <Radio label="2">2核</Radio>
          <Radio label="4">4核</Radio>
          <Radio label="8">8核</Radio>
          <Radio label="16">16核</Radio>
        </RadioGroup>
      </FormItem>
      <FormItem label="内存：" prop="memorySize">
        <RadioGroup v-model="applyServer.memorySize">
          <Radio label="2">2GB</Radio>
          <Radio label="4">4GB</Radio>
          <Radio label="8">8GB</Radio>
          <Radio label="16">16GB</Radio>
          <Radio label="32">32GB</Radio>
          <Radio label="64">64GB</Radio>
          <Radio label="128">128GB</Radio>
        </RadioGroup>
      </FormItem>
      <FormItem label="操作系统：" prop="system">
        <Cascader v-model="systemVal" :data="systemData" @on-change="handleChange" trigger="hover"></Cascader>
      </FormItem>
      <FormItem label="所属网络：" prop="netScope">
        <Select class="queryItem" clearable v-model="applyServer.netScope">
          <Option v-for="netsItem in netScopeArray" :key="netsItem.id" :value="netsItem.code">{{netsItem.name}}</Option>
        </Select>
      </FormItem>
      <FormItem label="网络带宽：" prop="netWide">
        <Select clearable v-model="applyServer.netWide">
          <Option v-for="(item,idx) in netWideData" :key="idx" :value="item.name">{{item.name}}M</Option>
        </Select>
      </FormItem>
      <FormItem label="网关描述：" prop="gatewayDesc">
        <Input
          type="textarea"
          v-model="applyServer.gatewayDesc"
          :maxlength="200"
          placeholder="限制输入200个字符以内"
          :rows="5">
        </Input>
      </FormItem>
      <FormItem label="系统盘大小" prop="sysDisksize">
        <Input type="text" v-model="applyServer.sysDisksize"><span slot="append">GB</span></Input>
      </FormItem>
      <FormItem label="数据盘：">
        <Button type="primary" @click="addDisk(applyServer.disks.length)">+添加</Button>
      </FormItem>
      <FormItem class="disk" v-for="(item, idx) in applyServer.disks" :key="idx" :label="item.diskName || `数据磁盘${idx + 1}`" :label-width="100" :prop="'disks.' + idx + '.diskSize'" :rules="[{required: true, message: '磁盘 ' + item.diskName +' 不能为空', trigger: 'blur'},{ type: 'number', message: '请输入数字格式', trigger: 'blur', transform(value) {return Number(value);}}]">
          <!-- <input style="width:80%" type='number' v-model="item.diskSize"><span slot="append">GB</span></input> -->
          <Input style="width:80%;" type="text" v-model="item.diskSize"><span slot="append">GB</span></Input>
          <a @click="handleRemove(idx)">删除</a>
      </FormItem>
      <FormItem label="申请数量：" prop="applyCount">
        <input class="ivu-input" style="width:50%;padding:0 5px;" type='number' v-model="applyServer.applyCount" min='1'>
        <!-- <InputNumber v-model="applyServer.applyCount" :editable="false" :min='0'></InputNumber> -->
      </FormItem>
    </Form>
  </div>
</template>

<script>
const validateCount = (rule, value, callback) => {
    console.log(value);
    if (!value) {
        return callback(new Error('该项为必填项，请填写相应数据！'));
    }
    // 模拟异步验证效果
    setTimeout(() => {
      console.log(typeof value);
      console.log(typeof parseInt(value));
        if (!Number.isInteger(parseInt(value))) {
            callback(new Error('请填写数字！'));
        } else {
            if (parseInt(value) <= 0) {
                callback(new Error('不能为负数!'));
            } else {
                callback();
            }
        }
    }, 1000);
};
export default {
  mounted(){
    console.log(this.applyEdit);
    // if(this.applyServer)this.applyServer = this.applyServer;
  },
  props:{
    applyEdit:{
      type:Object
    }
  },
  data () {
    return {
      applyServer:{
        cpuCount: '',
        memorySize: '',
        netScope: '',
        netWide: '',
        gatewayDesc: '',
        sysDisksize: '',
        applyCount:1,
        disks:[]
      },
      disksArr:[],
      systemData:this.$store.state.systemArray,//操作系统
      netScopeArray:this.$store.state.netScopeArray,//所属网络
      netWideData:this.$store.state.netWideData,
      systemVal: [],//操作系统默认值
      sysVal: "",
      ruleValidate: {
        cpuCount: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'change'
        }],
        memorySize: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'change'
        }],
        system: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'change'
        }],
        netScope: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'change'
        }],
        netWide: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'change'
        }],
        gatewayDesc: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'change'
        }],
        sysDisksize: [{
          required: true,
          validator:validateCount,
          trigger: 'blur'
        }],
        applyCount: [{
          validator:validateCount,
          trigger: 'blur'
        }],
        applyReason: [{
          required: true,
          message: '该项为必填项，请填写相应数据！',
          trigger: 'change'
        }],
      },
    }
  },
  methods: {
    handleSubmit(name) {//获取父级事件触发
      this.netScopeArray.forEach(item=>{
        if(item.code == this.applyServer.netScope){
          this.applyServer.netScopeName = item.name;//所属网络名称赋值
        }
      })
      console.log(this.applyServer);
      this.$refs[name].validate((valid) => {
        if(valid) {
          this.$emit('saveConfig', this.applyServer);
        }
      })
    },
    reload() {//初始化
      this.applyServer = {
        disks:[]
      };
      this.systemVal = [];
    },
    reloadFun() {
      this.$refs.apply.resetFields();
    },
    getEditData(editData){//获取数据时
      this.$refs.apply.resetFields();
      this.systemData.forEach(item => {
        item.children.forEach(items => {
          if(editData.system == items.value){
            this.systemVal[0] = item.value;
            this.systemVal[1] = items.value;
          }
        })
      });
      this.$set(this.systemVal);//更新
      let delNum = null;
      if(editData.disks){
        editData.disks.map((item,index) => {
          if(item.diskName === '系统盘'){
            delNum = index
          }
        })
      }
      if(delNum || delNum === 0){
        editData.disks.splice(delNum, 1)
      }
      this.applyServer = editData;
    },
    addDisk (len) {//添加磁盘数量
      console.log(len);
      let diskItem = {};
      diskItem = {diskSize:'', diskName:''};
      this.applyServer.disks.push(diskItem);
      console.log(this.applyServer.disks)
    },
    handleRemove (idx) {//删除磁盘数量
      this.applyServer.disks.splice(idx,1);
    },
    handleChange(value, selecteddata) {//级联选项
      let val = selecteddata.map(o => o.value).reverse();
      this.systemVal = selecteddata.map(o => o.value);
      this.applyServer.system = val[0];
      let sysValName = selecteddata.map(o => o.label).reverse();
      this.sysVal = sysValName[0];
      this.applyServer.systemName = sysValName[0];
    },
  },
}
</script>

<style lang="less" scoped>
#applyCount div{
  cursor: pointer;
}
.disk{
  position: relative;
  a{
    position: absolute;
    top:0;
    right:10px;
  }
}
</style>
