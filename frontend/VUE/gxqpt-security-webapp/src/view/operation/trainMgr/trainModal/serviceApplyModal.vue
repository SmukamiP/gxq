<template>
    <Modal @on-visible-change="openModal" v-model="evalModal" title="服务申请" width="20%" :mask-closable="false">
        <div style="text-align:center">
            <Form ref="evalData" :model="evalData" :rules="ruleValidate" :label-width="70" class="clearfix">
                <Row :gutter="40">
                    <Col span="13">
                        <FormItem label="服务类别" prop="results">
                            <Select v-model="evalData.type">
                                <Option value="同意">同意</Option>
                                <Option value="不同意">不同意</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="13">
                        <FormItem label="服务对象" prop="classify">
                            <Select v-model="evalData.classify">
                                <Option value="同意">同意</Option>
                                <Option value="不同意">不同意</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="13">
                        <FormItem label="服务分类" prop="obj">
                            <Select v-model="evalData.obj">
                                <Option value="同意">同意</Option>
                                <Option value="不同意">不同意</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="13">
                        <FormItem label="服务名称" prop="name">
                            <Select v-model="evalData.name">
                                <Option value="同意">同意</Option>
                                <Option value="不同意">不同意</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="24">
                        <FormItem label="服务简介" label-position="left" prop="Introduction">
                            <Input v-model="evalData.Introduction" type="textarea" :autosize="{minRows: 2,maxRows: 5}"></Input>
                        </FormItem>
                    </Col>
                    <Col span="13">
                        <FormItem label="涉及系统" prop="systemList">
                            <Select v-model="evalData.systemList" multiple>
                                <Option value="同意">同意</Option>
                                <Option value="不同意">不同意</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span="24">
                        <FormItem label="服务时间" prop="serviceDate">
                            <DatePicker type="daterange" v-model="evalData.serviceDate" placement="bottom-end" placeholder="请选择服务时间" style="float: left"></DatePicker>
                        </FormItem>
                    </Col>
                </Row>
            </Form>
        </div>
    </Modal>
</template>

<script>
export default {
    data(){
        return{
            evalData: {
                safetyLevel: '3',
            },
            ruleValidate: {
            }
        }
    },
    props:{
        evalModal:{
            type: Boolean,
            default: false
        },
        closeModal: Function,
    },
    methods:{
        openModal(status) {
            if(!status){
                this.$refs['evalData'].resetFields();
            }
        },
        handleSubmit() {
            this.$refs['evalData'].validate((valid) => {
                if(valid) {
                    this.save();
                } else {
                    this.$Message.error('表单验证失败!');
                }
            })
        },
        validForm() {
            this.$refs['evalData'].validate();
        },
    }
}
</script>

<style>

</style>
