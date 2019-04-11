/**
 * 保障房项目初始化
 */
var queryImport = {
    id: "queryImportTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
queryImport.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '申请人姓名', field: 'col1', align: 'center', valign: 'middle'},
        {title: '申请人身份证件号', field: 'col2', align: 'center', valign: 'middle'},
        {title: '配偶姓名', field: 'col3', align: 'center', valign: 'middle'},
        {title: '配偶身份证件号', field: 'col4', align: 'center', valign: 'middle'},
        {title: '申报状态', field: 'col5', align: 'center', valign: 'middle',visible: false},
        {title: '保障房类型', field: 'col6', align: 'center', valign: 'middle',visible: false},
        {title: '房号', field: 'col7', align: 'center', valign: 'middle',visible: false},
        {title: '建筑面积', field: 'col8', align: 'center', valign: 'middle',visible: false},
        {title: '套内面积', field: 'col9', align: 'center', valign: 'middle',visible: false},
        {title: '合同份数', field: 'col10', align: 'center', valign: 'middle',visible: false},
        {title: '合同签订日期', field: 'col11', align: 'center', valign: 'middle',visible: false},
        {title: '联系电话', field: 'col12', align: 'center', valign: 'middle',visible: false},
        {title: '项目地址', field: 'col13', align: 'center', valign: 'middle',visible: false},
        {title: '项目名称', field: 'col14', align: 'center', valign: 'middle',visible: false},
        {title: '房源情况', field: 'col15', align: 'center', valign: 'middle',visible: false},



    ];
};

queryImport.query = function(){

    var param = {
        "application":$("#application").val(),
        "idCard":$("#idCard").val(),
        "beginTime":$("#beginTime").val(),
        "endTime":$("#endTime").val(),
    };
    queryImport.table.refresh({query: param});
}

queryImport.initSecondType = function(pNum){
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

queryImport.detail = function (sRecNumGather,OPTYPENUM,RECYEAR,RECNUM) {
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
                queryImport.layerIndex = index;
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
queryImport.close = function () {
    parent.layer.close(window.parent.queryImport.layerIndex);
}
/**
 * 关闭二级对话框
 */
queryImport.closeSecond = function () {
    parent.layer.close(window.parent.queryImport.secondLayerIndex);
}

$(function () {
    //初始化表格
    var data = {
        "first":"1"
    };
    queryImport.url = "/info/importQueryList";
    var defaultColunms = queryImport.initColumn();
    var table = new BSTable(queryImport.id, queryImport.url, defaultColunms);
    table.setQueryParams(data);
    queryImport.table = table.init();
});
/**
 * 导出
 */
function exportProject  () {
    var application=$("#application").val();
    var idCard=$("#idCard").val();
    var beginTime=$("#beginTime").val();
    var endTime=$("#endTime").val();
    window.location.href=Feng.ctxPath+"/excel/exportQueryList?application="+application+'&idCard='+idCard+'&beginTime='+beginTime+'&endTime='+endTime;
}



