var inqueryData = [15];
/**
 * 保障房项目初始化
 */
var payRent = {
    id: "PayRentTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
payRent.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '序号', field: 'IROW', visible: true, align: 'center', valign: 'middle'},
        {title: '房屋编号', field: 'houseId', align: 'center', valign: 'middle'},
        {title: '租金', field: 'rent', align: 'center', valign: 'middle'},
        {title: '交租日期', field: 'payTime', visible: true, align: 'center', valign: 'middle'},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                return '<a onclick="payRent.detail('+'\'' + row.houseId + '\')">查看</a>';
            }
        }
    ];
};

payRent.query = function(){
    var param = {
        "houseId":$("#condition").val(),
        "month":$("#month").val()
    };
    payRent.table.refresh({query: param});
};

payRent.initSecondType = function(pNum){
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

payRent.detail = function (houseId) {

    var index = layer.open({
        type: 2,
        title: '详细',
        area: ['80%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pay/detail?houseId='+houseId
    });
    payRent.layerIndex = index;

}

/**
 * 导出
 */
payRent.exportProject = function () {
    window.location.href=Feng.ctxPath+"/excel/apply_approval";
}

/**
 * 关闭此对话框
 */
payRent.close = function () {
    parent.layer.close(window.parent.payRent.layerIndex);
}
/**
 * 关闭二级对话框
 */
payRent.closeSecond = function () {
    parent.layer.close(window.parent.payRent.secondLayerIndex);
}

$(function () {
    var data = {};
    payRent.url = "/pay/search";
    var defaultColunms = payRent.initColumn();
    var table = new BSTable(payRent.id, payRent.url, defaultColunms);
    table.setQueryParams(data);
    payRent.table = table.init();
    //初始化数据后增加选中记录
    $("#"+payRent.id).on("load-success.bs.table",function(){
        $(".pagination-detail").append(
            '<span style="margin-left:5px;">共选中</span>' +
            '<input style="padding: 6px 12px;border:1px solid #e5e6e7;width:50px;text-align: center;"  type="text" readonly id="selecteds" value="">'
        );
    })

    //选择或反选触发事件
    $("#"+payRent.id).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table",function () {
        var selectedCount = $("#"+payRent.id).bootstrapTable('getAllSelections').length;
        $("#selecteds").val(selectedCount);
    })
});
