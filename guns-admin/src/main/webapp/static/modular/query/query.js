/**
 * 保障房项目初始化
 */
var queryProject = {
    id: "queryProjectTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
queryProject.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '登记编号', field: '登记编号', align: 'center', valign: 'middle'},
        {title: '申请人', field: '申请人', align: 'center', valign: 'middle'},
        {title: '身份证件号', field: '身份证件号', align: 'center', valign: 'middle'},
        {title: '案卷状态', field: '案卷状态', align: 'center', valign: 'middle'},

        {title: 'OPTYPENUM', field: 'OPTYPENUM', align: 'center', valign: 'middle',visible: false},
        {title: 'RECYEAR', field: 'RECYEAR', align: 'center', valign: 'middle',visible: false},
        {title: 'RECNUM', field: 'RECNUM', align: 'center', valign: 'middle',visible: false},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                return '<a onclick="queryProject.detail('+'\'' +row.登记编号+'\',\''+ row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">详细</a>';
            }

        }
    ];
};

queryProject.query = function(){
    //是否勾选本人经办
    var IsOwner_handle = $("input[name=owner_handle]:checked");
    var IsOwner_handleVal = "";
    if(IsOwner_handle.get(0)){
        IsOwner_handleVal = IsOwner_handle.get(0).value;
    }

    //是否勾选精确查找
    var IsPrecise_query = $("input[name=precise_query]:checked");
    var IsPrecise_queryVal = "";
    if(IsPrecise_query.get(0)){//勾选了
        IsPrecise_queryVal = IsPrecise_query.get(0).value;
    }
    var param = {
        "first":"2",
        "registerNum":$("#registerNum").val(),
        "application":$("#application").val(),
        "idCard":$("#idCard").val(),
        "archivesNum":$("#archivesNum").val(),
        "iIsSelfExec":IsOwner_handleVal,
        "IsPrecise_queryVal":IsPrecise_queryVal
    };
    queryProject.table.refresh({query: param});
}

queryProject.initSecondType = function(pNum){
    if(pNum == ''){
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/support/second_type", function (data) {
        $("#business_second").html('<option value="">请选择</option>');
        $.each(data,function (i,item) {
            $("#business_second").append('<option value="'+item.OPPARTNUM+'">'+item.OPPARTNAME+'</option>')
        })
    }, function (data) {

    });
    ajax.set({pNum:pNum});
    ajax.start();
}

queryProject.detail = function (sRecNumGather,OPTYPENUM,RECYEAR,RECNUM) {
    var data = {sRecNumGather:sRecNumGather,
                iOpTypeNum:OPTYPENUM,
                iRecYear:RECYEAR,
                iRecNum:RECNUM
                };
    $.ajax({
        url:Feng.ctxPath + '/info/get_page_info',
        data:data,
        type:"post",
        success:function (result) {
            if(result.code != '200'){
                Feng.info(result.msg);
                return;
            }
            if(result.data.sPage){
                var index = layer.open({
                    type: 2,
                    title: '详细',
                    area: ['80%', '80%'], //宽高
                    fix: false, //不固定
                    maxmin: true,
                    content: Feng.ctxPath + '/support/detail?page='+result.data.sPage+'&OpTypeNum=' + result.data.iOpTypeNum +'&RecYear=' + result.data.iRecYear +'&RecNum=' + result.data.iRecNum + "&info=" + '1'
                });
                queryProject.layerIndex = index;
                layer.full(index);
            }
        },
        error:function (result) {
            Feng.info("系统异常！");
        }
    })
}


/**
 * 关闭此对话框
 */
queryProject.close = function () {
    parent.layer.close(window.parent.queryProject.layerIndex);
}
/**
 * 关闭二级对话框
 */
queryProject.closeSecond = function () {
    parent.layer.close(window.parent.queryProject.secondLayerIndex);
}

$(function () {
    //初始化表格
    var data = {
        "first":"1"
    };
    queryProject.url = "/info/list";
    var defaultColunms = queryProject.initColumn();
    var table = new BSTable(queryProject.id, queryProject.url, defaultColunms);
    table.setQueryParams(data);
    queryProject.table = table.init();
});




