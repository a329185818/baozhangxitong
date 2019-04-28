/**
 * 保障房项目初始化
 */
var contractLookManage = {
    id: "ContractLookTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
contractLookManage.initColumn = function () {
    return [
        {field: '', radio: false,formatter:function(value,row,index){
            return '<input type="hidden" value="'+row.id+'">';
        }
        },
        {title: '申请人姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '身份证号码', field: 'idCard', align: 'center', valign: 'middle', sortable: true},
        {title: '房屋坐落', field: 'houseAddress', align: 'center', valign: 'middle', sortable: true},
        {title: '每平米租金/月', field: 'price', align: 'center', valign: 'middle', sortable: true},
        {title: '合同开始时间', field: 'startTime', align: 'center', valign: 'middle', sortable: true},
        {title: '合同结束时间', field: 'endTime', align: 'center', valign: 'middle', sortable: true},
        {title: 'OPTYPENUM', field: 'optypenum', align: 'center', valign: 'middle',visible: false},
        {title: 'RECYEAR', field: 'recyear', align: 'center', valign: 'middle',visible: false},
        {title: 'RECNUM', field: 'recnum', align: 'center', valign: 'middle',visible: false},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                return '<a onclick="contractLookManage.exportMaterial('+'\'' + row.optypenum + '\',\'' +row.recyear + '\',\''+ row.recnum +'\',\''+ row.endTime +'\')">打印</a>';
            }
        }
    ];
};

//打印export_pdf
contractLookManage.exportMaterial = function (optypenum,recyear,recnum,endTime) {
    window.open(Feng.ctxPath + '/contract/export_pdf?optypenum='+optypenum+'&recyear=' + recyear +'&recnum=' + recnum +'&endTime=' + endTime);
}

/**
 * 关闭此对话框
 */
contractLookManage.close = function () {
    parent.layer.close(window.parent.contractLookManage.layerIndex);
};


$(function () {
    contractLookManage.url = "/contract/all_list";
    var data = {
        optypenum:contractOptypenum,
        recyear:contractRecyear,
        recnum:contractRecnum
    };
    var defaultColunms = contractLookManage.initColumn();
    var table = new BSTable(contractLookManage.id, contractLookManage.url, defaultColunms);
    table.setQueryParams(data);
    contractLookManage.table = table.init();
});

