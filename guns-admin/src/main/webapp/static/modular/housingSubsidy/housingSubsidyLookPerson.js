/**
 * 住房补贴个人查看初始化
 */
var SubsidyLookPersonManage = {
    id: "HousingSubsidyLookTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
SubsidyLookPersonManage.initColumn = function () {
    return [
        {field: '', radio: false,formatter:function(value,row,index){
            return '<input type="hidden" value="'+row.id+'">';
        }
        },
        {title: '申请人姓名', field: 'name', align: 'center', valign: 'middle'},
        {title: '住房补贴金额', field: 'rent', align: 'center', valign: 'middle'},
        {title: '住房补贴开始时间', field: 'startTime', align: 'center', valign: 'middle'},
        {title: '住房补贴结束时间', field: 'endTime', align: 'center', valign: 'middle'},
        {title: '操作人姓名', field: 'oprationName', align: 'center', valign: 'middle'},
        {title: '解除人姓名', field: 'relieveOprationName', align: 'center', valign: 'middle'},
        {title: '解除时间', field: 'relieveTime', align: 'center', valign: 'middle'},
        {title: '备注', field: 'remark', align: 'center', valign: 'middle', visible: false},
        {title: '身份证号码', field: 'idCard', align: 'center', valign: 'middle', visible: false},
        {title: '电话号码', field: 'telphone', align: 'center', valign: 'middle', visible: false},
        {title: 'OPTYPENUM', field: 'optypenum', align: 'center', valign: 'middle',visible: false},
        {title: 'RECYEAR', field: 'recyear', align: 'center', valign: 'middle',visible: false},
        {title: 'RECNUM', field: 'recnum', align: 'center', valign: 'middle',visible: false}
    ];
};


/**
 * 关闭此对话框
 */
SubsidyLookPersonManage.close = function () {
    parent.layer.close(window.parent.SubsidyLookPersonManage.layerIndex);
};


$(function () {
    SubsidyLookPersonManage.url = "/housingSubsidy/person_list";
    var data = {
        optypenum:subsidyOptypenum,
        recyear:subsidyRecyear,
        recnum:subsidyRecnum
    };
    var defaultColunms = SubsidyLookPersonManage.initColumn();
    var table = new BSTable(SubsidyLookPersonManage.id, SubsidyLookPersonManage.url, defaultColunms);
    table.setQueryParams(data);
    SubsidyLookPersonManage.table = table.init();
});

