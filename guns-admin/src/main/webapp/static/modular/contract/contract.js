/**
 * 合同初始化
 */
var contractManage = {
    id: "ContractTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
contractManage.initColumn = function () {
    return [
        {field: '', radio: false,formatter:function(value,row,index){
            return '<input type="hidden" value="'+row.id+'">';
        }
        },
        {title: '申请人姓名', field: 'name', align: 'center', valign: 'middle'},
        {title: '身份证号码', field: 'idCard', align: 'center', valign: 'middle'},
        {title: '电话号码', field: 'telphone', align: 'center', valign: 'middle'},
        {title: '登记编号', field: 'recnumgather', align: 'center', valign: 'middle'},
        {title: '登记类型', field: 'oppartnum', align: 'center', valign: 'middle'},
        {title: 'OPTYPENUM', field: 'optypenum', align: 'center', valign: 'middle',visible: false},
        {title: 'RECYEAR', field: 'recyear', align: 'center', valign: 'middle',visible: false},
        {title: 'RECNUM', field: 'recnum', align: 'center', valign: 'middle',visible: false},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                var str = '';
                str +=  '<a onclick="contractManage.detail('+'\'' + row.optypenum + '\',\'' +row.recyear + '\',\''+ row.recnum +'\')">设置合同</a>&nbsp;&nbsp;|&nbsp;&nbsp;';
                str += '<a onclick="contractManage.lookAndPrint('+'\'' + row.optypenum + '\',\'' +row.recyear + '\',\''+ row.recnum +'\')">查看/打印</a>';
                return str;
            }
        }
    ];
};

/**
 * 设置合同
 * @param optypenum
 * @param recyear
 * @param recnum
 */
contractManage.detail = function (optypenum,recyear,recnum) {
    var index = layer.open({
        type: 2,
        title: '合同设置详情',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/contract/contract_detail?optypenum=' + optypenum +'&recyear=' + recyear +'&recnum=' + recnum
    });
    contractManage.layerIndex = index;
    layer.full(index);
};

/**
 * 查看/打印
 * @param OPTYPENUM
 * @param RECYEAR
 * @param RECNUM
 */
contractManage.lookAndPrint = function (optypenum,recyear,recnum) {
    var index = layer.open({
        type: 2,
        title: '查看/打印',
        area: ['80%', '95%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/contract/lookAndPrint?optypenum=' + optypenum +'&recyear=' + recyear +'&recnum=' + recnum
    });
    contractManage.layerIndex = index;
}

//保存合同信息
contractManage.save = function () {
    var data = $("#contractInfoForm").serializeJSON();
    if(data.price == null || data.price == "" || data.price == undefined || data.price == 0){
        Feng.info("请填写每平米租金/月！");
        return ;
    }
    if(data.startTime == null || data.startTime == "" || data.startTime == undefined) {
        Feng.info("请选择合同开始时间！");
        return ;
    }
    if(data.endTime == null || data.endTime == "" || data.endTime == undefined){
        Feng.info("请选择合同截止时间！");
        return ;
    }
    if (data.endTime <= data.startTime){
        Feng.info("截止时间小于等于开始时间！");
        return ;
    }

    data = {json:JSON.stringify(data)};
    $.ajax({
        url:'/contract/save',
        type:'POST',
        // contentType: 'application/json; charset=UTF-8',
        async:false,
        // dataType:'json',
        data:data,
        success: function (response) {
            if(response == 'Error'){
                Feng.info('保存失败');
            }else{
                contractManage.close();
                parent.location.reload();
                Feng.success('保存成功');
            }
        }
    })

};

contractManage.query = function(){
    var param = {
        "name":$("#condition").val()
    };
    contractManage.table.refresh({query: param});
};

/**
 * 关闭此对话框
 */
contractManage.close = function () {
    parent.layer.close(window.parent.contractManage.layerIndex);
};


$(function () {
    contractManage.url = "/contract/list";
    var defaultColunms = contractManage.initColumn();
    var table = new BSTable(contractManage.id, contractManage.url, defaultColunms);
    contractManage.table = table.init();

    //填写每平米月租金，自动填写其他相关值
    var num = $("#rent").val();
    if(num>0){
        checkField(num);
    }


});
function checkField(val)
{
    var monthRent = $("#architArea").val() * $("#rent").val();
    $("#monthRent").val(monthRent);
    $("#sumMonthRent").val(monthRent*6);
}
