/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var proposerInfoDlg = {
    proposerInfoData: {},
    validateFields: {
        col1: {
            validators: {
                notEmpty: {
                    message: '申请人不能为空'
                }
            }
        },
        col2: {
            validators: {
                notEmpty: {
                    message: '申请人身份证号不能为空'
                }
            }
        },

    }
};

/**
 * 清除数据
 */
proposerInfoDlg.clearData = function () {
    this.proposerInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
proposerInfoDlg.set = function (key, value) {
    if(typeof value == "undefined"){
        if(typeof $("#" + key).val() =="undefined"){
            var str="";
            var ids="";
            $("input[name='"+key+"']:checkbox").each(function(){
                if(true == $(this).is(':checked')){
                    str+=$(this).val()+",";
                }
            });
            if(str){
                if(str.substr(str.length-1)== ','){
                    ids = str.substr(0,str.length-1);
                }
            }else{
                $("input[name='"+key+"']:radio").each(function(){
                    if(true == $(this).is(':checked')){
                        ids=$(this).val()
                    }
                });
            }
            this.proposerInfoData[key] = ids;
        }else{
            this.proposerInfoData[key]= $("#" + key).val();
        }
    }

    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
proposerInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
proposerInfoDlg.close = function () {
    parent.layer.close(window.parent.queryImport.layerIndex);
};

/**
 * 收集数据
 */
proposerInfoDlg.collectData = function () {
    this.set('col1').set('col2').set('col3').set('col4')
        .set('col5').set('col6').set('col7').set('col8').set('col9').set('col10')
        .set('col11').set('col12').set('col13').set('col14').set('col15').set('col16').set('col17').set('id');
};


/**
 * 验证数据是否为空
 */
proposerInfoDlg.validate = function () {
    $('#proposerInfoForm').data("bootstrapValidator").resetForm();
    $('#proposerInfoForm').bootstrapValidator('validate');
    return $("#proposerInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加申请记录
 */
proposerInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }


    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/info/addProposer", function (data) {
        Feng.success("添加成功!");
        window.parent.queryImport.table.refresh();
        proposerInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.proposerInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
proposerInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/info/proposer_alter", function (data) {
        Feng.success("修改成功!");
        if (window.parent.queryImport != undefined) {
            window.parent.queryImport.table.refresh();
            proposerInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.proposerInfoData);
    ajax.start();
};
$(function () {
    Feng.initValidator("proposerInfoForm", proposerInfoDlg.validateFields);
})