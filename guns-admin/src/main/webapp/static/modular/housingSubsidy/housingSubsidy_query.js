/**
 * 住房补贴查询初始化
 */
var HousingSubsidyManage = {
    id: "HousingSubsidyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
HousingSubsidyManage.initColumn = function () {
    return [
        {field: '', radio: false,formatter:function(value,row,index){
            return '<input type="hidden" value="'+row.id+'">';
        }
        },
        {title: '申请人姓名', field: 'name', align: 'center', valign: 'middle'},
        {title: '身份证号码', field: 'idCard', align: 'center', valign: 'middle'},
        {title: '电话号码', field: 'telphone', align: 'center', valign: 'middle'},
        {title: 'OPTYPENUM', field: 'optypenum', align: 'center', valign: 'middle',visible: false},
        {title: 'RECYEAR', field: 'recyear', align: 'center', valign: 'middle',visible: false},
        {title: 'RECNUM', field: 'recnum', align: 'center', valign: 'middle',visible: false},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                var str = '<a onclick="HousingSubsidyManage.lookPersonInfo('+'\'' + row.optypenum + '\',\'' +row.recyear + '\',\''+ row.recnum +'\')">查看</a>';
                return str;
            }
        }
    ];
};

/**
 * 查看，个人可能会有多条数据
 * @param OPTYPENUM
 * @param RECYEAR
 * @param RECNUM
 */
HousingSubsidyManage.lookPersonInfo = function (optypenum,recyear,recnum) {
    var index = layer.open({
        type: 2,
        title: '个人住房补贴详情',
        area: ['80%', '95%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/housingSubsidy/look_person?optypenum=' + optypenum +'&recyear=' + recyear +'&recnum=' + recnum
    });
    HousingSubsidyManage.layerIndex = index;
}

HousingSubsidyManage.query = function(){
    var param = {
        "name":$("#condition").val()
    };
    HousingSubsidyManage.table.refresh({query: param});
};

/**
 * 关闭此对话框
 */
HousingSubsidyManage.close = function () {
    parent.layer.close(window.parent.contractManage.layerIndex);
};

$(function () {
    HousingSubsidyManage.url = "/housingSubsidy/list";
    var defaultColunms = HousingSubsidyManage.initColumn();
    var table = new BSTable(HousingSubsidyManage.id, HousingSubsidyManage.url, defaultColunms);
    HousingSubsidyManage.table = table.init();
});

