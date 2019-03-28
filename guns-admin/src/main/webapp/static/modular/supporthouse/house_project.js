var inqueryData = [15];
/**
 * 保障房项目初始化
 */
var houseProject = {
    id: "HouseProjectTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
houseProject.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '序号', field: 'IROW', visible: true, align: 'center', valign: 'middle'},
        {title: '收件日期', field: '收件日期', align: 'center', valign: 'middle'},
        {title: '提交日期', field: '提交日期', align: 'center', valign: 'middle'},
        {title: '登记编号', field: '登记编号', align: 'center', valign: 'middle'},
        // {title: '条形码', field: '条形码', align: 'center', valign: 'middle'},
        {title: '登记类别', field: '登记类别', visible: true, align: 'center', valign: 'middle'},
        {title: '申请人', field: '申请人', align: 'center', valign: 'middle'},
        {title: '锁定人', field: '锁定人', align: 'center', valign: 'middle'},
        {title: '受理坐落', field: '受理坐落', align: 'center', valign: 'middle'},
        {title: '阶段', field: '阶段', align: 'center', valign: 'middle'},

        {title: 'OPTYPENUM', field: 'OPTYPENUM', align: 'center', valign: 'middle',visible: false},
        {title: 'RECYEAR', field: 'RECYEAR', align: 'center', valign: 'middle',visible: false},
        {title: 'RECNUM', field: 'RECNUM', align: 'center', valign: 'middle',visible: false},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                if(row.OPTYPENUM == '80'){
                    return '<a onclick="houseProject.detail('+'\'' +row.登记编号+'\',\''+ row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">详细</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="houseProject.nodeList('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">节点</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="houseProject.relationBuildingTable('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">楼盘表</a>'

                }else{
                    inqueryData[index] = row;
                    return '<a onclick="houseProject.detail('+'\'' +row.登记编号+'\',\''+ row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">详细</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="houseProject.nodeList('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">节点</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="houseProject.unlock('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">解锁</a>'

                }
            }

        }
    ];
};

/**
 * 关联楼盘表
 * @param OPTYPENUM
 * @param RECYEAR
 * @param RECNUM
 */
houseProject.relationBuildingTable = function (OPTYPENUM,RECYEAR,RECNUM) {
    
}

/**
 * 点击查询按钮
 */
houseProject.query = function(){
    var IsEspacialElm = $("input[name=transfer_case]:checked");
    var IsEspacialVal = "";
    if(IsEspacialElm.get(0)){
        IsEspacialVal = IsEspacialElm.get(0).value;
    }
    var param = {
        "sOPTYPENUM":$("#business_first").val(),
        "sOPPARTNUM":$("#business_second").val(),
        "sRecNumGather":$("#condition").val(),
        "iBATCHNUM":$("#num").val(),
        "StartDate":$("#beginTime").val(),
        "EndDate":$("#endTime").val(),
        "iFilterMode":$("input[name=fileType]:checked").val(),
        "iRecUserNum":$("#recipients").val(),
        "IsEspacial":IsEspacialVal
    }
    houseProject.table.refresh({query: param});
}

/**
 * 选择二级下拉框
 * @param pNum
 */
houseProject.initSecondType = function(pNum){
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

/**
 * 显示详情
 * @param sRecNumGather
 * @param OPTYPENUM
 * @param RECYEAR
 * @param RECNUM
 */
houseProject.detail = function (sRecNumGather,OPTYPENUM,RECYEAR,RECNUM) {
    var data = {sRecNumGather:sRecNumGather,
                iOpTypeNum:OPTYPENUM,
                iRecYear:RECYEAR,
                iRecNum:RECNUM
                };
    $.ajax({
        url:Feng.ctxPath + '/support/get_page_info',
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
                    area: ['80%', '90%'], //宽高
                    fix: false, //不固定
                    maxmin: true,
                    content: Feng.ctxPath + '/support/detail?page='+result.data.sPage+'&OpTypeNum=' + result.data.iOpTypeNum +'&RecYear=' + result.data.iRecYear +'&RecNum=' + result.data.iRecNum + "&readonly=" + result.data.readonly
                });
                houseProject.layerIndex = index;
                layer.full(index);
            }
        },
        error:function (result) {
            Feng.info("系统异常！");
        }
    })
}

/**
 * 新建项目
 */
houseProject.createProject = function(){
    if($("#business_second").val() == ""){
        Feng.info("请选择业务细项！");
        return;
    }
    var param = {
        "iOpTypeNum":$("#business_first").val(),
        "iOpPartNum":$("#business_second").val()
    }
    var ajax = new $ax(Feng.ctxPath + "/support/add", function (result) {
        if(result.code == '502' || result.code == '505'){
            Feng.info(result.msg);
            return;
        }

        var index = layer.open({
            type: 2,
            title: '新建',
            area: ['80%', '90%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/support/detail?page='+result.data.sPage+'&OpTypeNum=' + result.data.iOpTypeNum +'&RecYear=' + result.data.iYear +'&RecNum=' + result.data.iMaxRecNum
        });
        houseProject.layerIndex = index;

    }, function (data) {
        Feng.info("系统异常！");
    });
    ajax.set(param);
    ajax.start();
}

/**
 * 导出
 */
houseProject.exportProject = function () {
    window.location.href=Feng.ctxPath+"/excel/apply_approval";
}

/**
 * 保存项目细项
 */
houseProject.saveRecOwner = function () {
    var data = $("#projectInfoForm").serializeJSON();
    var ajax = new $ax(Feng.ctxPath + "/support/save_recowner", function (result) {
        if(result.code == '200'){
            Feng.info(result.msg);
            return;
        }else {
            Feng.info("操作失败！");
        }
    }, function (result) {
        Feng.info("系统异常！");
    });
    ajax.set(data);
    ajax.start();
}

/**
 * 关闭此对话框
 */
houseProject.close = function () {
    parent.layer.close(window.parent.houseProject.layerIndex);
}
/**
 * 关闭二级对话框
 */
houseProject.closeSecond = function () {
    parent.layer.close(window.parent.houseProject.secondLayerIndex);
}

/**
 * 解锁
 * @param OPTYPENUM
 * @param RECYEAR
 * @param RECNUM
 */
houseProject.unlock = function (OPTYPENUM,RECYEAR,RECNUM) {
    var data = {iOptypenum:OPTYPENUM,iRecyear:RECYEAR,iRecnum:RECNUM};
    var ajax = new $ax(Feng.ctxPath + "/support/unlock", function (result) {
        if(result == '1'){
            Feng.success('解锁成功');
            houseProject.query();
        }else {
            Feng.info("解锁失败！");
        }
    }, function (result) {
        Feng.info("系统异常！");
    });
    ajax.set(data);
    ajax.start();
}

/**
 * 节点列表
 * @param OPTYPENUM
 * @param RECYEAR
 * @param RECNUM
 */
houseProject.nodeList = function (OPTYPENUM,RECYEAR,RECNUM) {
    var index = layer.open({
        type: 2,
        title: '办理节点列表',
        area: ['80%', '90%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/support/node_list?iOptypenum=' + OPTYPENUM +'&iRecyear=' + RECYEAR +'&iRecnum=' + RECNUM +'&num=' + 0
    });
    houseProject.layerIndex = index;
}


$(function () {
    var now = new Date();
    var month = now.getMonth();
    var year = now.getFullYear();
    if(month < 4){
        year --;
        month = month + 12;
    }
    laydate.render({
        elem: '#beginTime',
        value:year + '-' + lay.digit(month-4 ) + '-' + lay.digit(now.getDate())
    });
    laydate.render({
        elem: '#endTime',
        value:now
    });

    $("#create_project").attr("disabled","disabled");

    //项目细项初始化
    $("#business_first").change(function () {
        houseProject.initSecondType($(this).children('option:selected').val());
        if($(this).val() != ''){
            $("#create_project").removeAttr("disabled");
        }else{
            $("#create_project").attr("disabled","disabled");
        }
    })

    //项目或申请参数
    if(location.pathname.match("/support/project/30")){
        houseProject.url = "/support/list?sOPGROUPCODE=30";
    }else if(location.pathname.match("/support/project/31")){
        houseProject.url = "/support/list?sOPGROUPCODE=31";
    }else if(location.pathname.match("/pie/index")){
        houseProject.url = "/support/list?sOPGROUPCODE=31";
    }
    //初始化表格
    var data = {
        "StartDate":year + '-' + lay.digit(month-4 ) + '-' + lay.digit(now.getDate()),
        "EndDate":now.getFullYear() + '-' + lay.digit(now.getMonth()+1 ) + '-' + lay.digit(now.getDate())
    };
    var defaultColunms = houseProject.initColumn();
    var table = new BSTable(houseProject.id, houseProject.url, defaultColunms);
    table.setQueryParams(data);
    houseProject.table = table.init();
    //初始化数据后增加选中记录
    $("#"+houseProject.id).on("load-success.bs.table",function(){
        $(".pagination-detail").append(
            '<span style="margin-left:5px;">共选中</span>' +
            '<input style="padding: 6px 12px;border:1px solid #e5e6e7;width:50px;text-align: center;"  type="text" readonly id="selecteds" value="">'
            );
    })

    //选择或反选触发事件
    $("#"+houseProject.id).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table",function () {
        var selectedCount = $("#"+houseProject.id).bootstrapTable('getAllSelections').length;
        $("#selecteds").val(selectedCount);
    })
});
