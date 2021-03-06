<template>
    <Modal
        v-model="modalShow"
        :title="title || '详情'"
        width="40%"
        :closable="false"
        :mask-closable="false">
        <Form ref="formData" :model="formData" :rules="ruleValidate" :label-width="70" class="clearfix">
            <Row :gutter="40">
                <Col span="24">
                    <FormItem label="培训类别" prop="trainType" required>
                        <template v-if="isEdit">
                            <Select v-model="currentTypeIdx" :disabled="!isNew">
                                <Option :value="i" v-for="(item, i) in alltypeList" :key="i">{{item.type}}</Option>
                            </Select>
                        </template>
                        <template v-else>
                            <Input v-model="formData.trainType" disabled></Input>
                        </template>
                    </FormItem>
                </Col>
                <Col span="12">
                    <FormItem label="培训名称" prop="trainName">
                        <template v-if="isEdit && currentTypeIdx !== (alltypeList.length - 1)">
                            <Select v-model="formData.trainName" :disabled="!isNew && !isUserDefined">
                                <Option :value="item" v-for="item in names" :key="item">{{item}}</Option>
                            </Select>
                        </template>
                        <template v-else>
                            <Input v-model="formData.trainName" :disabled="!isNew && !isUserDefined" :maxlength="200"></Input>
                        </template>
                    </FormItem>
                </Col>
                <Col span="12">
                    <FormItem label="培训时间" prop="trainTime">
                        <DatePicker
                            type="datetime"
                            v-model="formData.trainTime"
                            placement="bottom-end"
                            placeholder="请选择培训时间"
                            style="float: left; width: 100%;">
                        </DatePicker>
                    </FormItem>
                </Col>
                <Col span="24">
                    <FormItem label="申请单位" prop="company">
                        <Select v-model="formData.company">
                            <Option :value="item.name" v-for="item in companyList" :key="item.id">{{item.name}}</Option>
                        </Select>
                    </FormItem>
                </Col>
                <Col span="24">
                    <FormItem label="培训地址" prop="trainAddress">
                        <Input v-model="formData.trainAddress" :maxlength="200"></Input>
                    </FormItem>
                </Col>
                <Col span="24">
                    <FormItem label="培训内容" :label-width="72" prop="trainInfo" required>
                        <Input
                            v-model="formData.trainInfo"
                            type="textarea"
                            :autosize="{minRows: 5,maxRows: 5}"
                            style="resize:none;"
                            :disabled="!isNew && !isUserDefined"
                            :maxlength="1000">
                        </Input>
                    </FormItem>
                </Col>
            </Row>
        </Form>
        <div slot="footer">
            <Button class="modalBtn" type="default" @click="closeModal" size="large">取消</Button>
            <Button class="modalBtn" type="primary" @click="handleSubmit" size="large">确定</Button>
        </div>
    </Modal>
</template>

<script>
import api from '@/api/axiosApi'
import apiList from '@/api/comApiList'

export default {
    data() {
        const validRules = {
            trainTime (rule, value, callback) {
                if (value.length === 0) {
                    callback(new Error('请选择日期！'))
                } else {
                    callback()
                }
            }
        }
        const vm = this
        return {
            // 当前选择的类型的序号
            currentTypeIdx: 0,
            modalShow: false,
            companyList: [],
            formData: {
                trainType: '',
                trainName: '',
                trainTime: '',
                trainAddress: '',
                trainInfo: '',
                company: ''
            },
            ruleValidate: {
                trainType: [{
                    validator: (rule, value, cb) => {
                        if (!vm.formData.trainType) {
                            cb(new Error('不可为空'))
                        } else {
                            cb()
                        }
                    }
                }],
                trainName: [{required: true, message: '不可为空', trigger: 'blur'}],
                trainTime: [{required: true, validator: validRules.trainTime, trigger: 'blur'}],
                trainAddress: [{required: true, message: '不可为空', trigger: 'blur'}],
                trainInfo: [{
                    trigger: 'blur',
                    validator: (rule, value, cb) => {
                        if (!value) {
                            cb(new Error('不可为空'))
                            return
                        }
                        cb()
                    }
                }],
                company: [{required: true, message: '不可为空', trigger: 'change'}]
            },
            // 自定义
            isNew: false,
            // 修改
            isEdit: false,
            // 表单信息
            detailData: {}
        }
    },
    props: {
        // 弹窗title
        title: String,
        // 类型列表
        typeList: {
            type: Array,
            default: () => {
                return []
            }
        }
    },
    computed: {
        alltypeList () {
            return this.typeList
        },
        names () {
            if (this.currentTypeIdx === this.alltypeList.length - 1 || this.alltypeList.length === 0) {
                return []
            }
            return this.alltypeList[this.currentTypeIdx].names
        },
        isUserDefined () {
            return this.detailData.type == '自定义培训'
        }
    },
    methods: {
        // 获取所有单位
        getMyOrgList() {
            const vm = this
            api(apiList.getMyOrgList)
            .then(res => {
                if (res.data.errcode === 0) {
                    vm.companyList = res.data.data.orgList
                }
            }, error => {console.log(error)})
        },
        handleSubmit() {
            const vm = this
            console.log("验证表单")
            vm.$refs['formData'].validate((valid) => {
                if(valid) {
                    vm.$emit('on-ok', vm.formData)
                    // vm.closeModal()
                } else {
                    vm.$Message.error('表单验证失败!')
                }
            })
        },
        validForm() {
            this.$refs['formData'].resetFields()
            this.$refs['formData'].validate()
        },
        open (isNew, isEdit, detailData) {
            this.detailData = detailData
            this.isNew = isNew
            this.isEdit = isEdit
            this.modalShow = true
            this.$refs['formData'].resetFields()
            this.initDetail()
            this.getMyOrgList()
        },
        closeModal() {
            this.modalShow = false
        },
        initDetail() {
            const vm = this
            vm.formData = {
                trainType: vm.detailData.type || '',
                trainName: vm.detailData.name || '',
                trainTime: vm.detailData.trainTime || '',
                trainAddress: vm.detailData.trainAddress || '',
                trainInfo: vm.detailData.context || ''
            }
            // 修改
            if (vm.isEdit) {
                const detailType = vm.detailData.type
                for (var i = 0; i < vm.typeList.length; i++) {
                    if (vm.typeList[i].type === detailType) {
                        vm.currentTypeIdx = i
                        break
                    }
                }
            }
            // 新建自定义
            if(vm.isNew) {
                vm.formData.trainType = '自定义培训'
            }
        }
    }
}
</script>

<style scoped>
.ivu-form-item >>> .ivu-form-item-content {
    height:auto;
}
.ivu-input-wrapper >>> textarea {
    resize: none;
}
.modalBtn{
    margin:0 20px;
}
</style>
