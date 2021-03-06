/**
 * 配房管理人员初始化
 */
var allotRoom = {
    id: "allotRoomTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
allotRoom.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},

        {title: '申请人姓名', field: 'OWNERNAME', align: 'center', valign: 'middle'},
        {title: '申请人身份证号', field: 'OWNERCERTNUM', align: 'center', valign: 'middle'},
        {title: '配偶姓名', field: 'OWNERNAME_PEIOU', align: 'center', valign: 'middle'},
        {title: '配偶身份证号', field: 'OWNERCERTNUM_PEIOU', align: 'center', valign: 'middle'},
        {title: '状态', field: 'PEOPLE_STATUS', align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                if(value== '3'){
                   return '<a onclick="allotRoom.searchHouse('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">已配房</a>';
                }else if(value=='4'){
                    return '<a onclick="allotRoom.lookHousingSubsidy('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">已补贴</a>';
                }else if(value=='2'){
                    return '轮候';
                }else {
                    return '';
                }
            }},
        {title: '登记编号', field: 'RECNUMGATHER', align: 'center', valign: 'middle',visible: false},
        {title: 'OPTYPENUM', field: 'OPTYPENUM', align: 'center', valign: 'middle',visible: false},
        {title: 'RECYEAR', field: 'RECYEAR', align: 'center', valign: 'middle',visible: false},
        {title: 'RECNUM', field: 'RECNUM', align: 'center', valign: 'middle',visible: false},

        {title: '操作',field: 'PEOPLE_STATUS', align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                if(value=='2'){
                    return '<a onclick="allotRoom.detail('+'\'' +row.RECNUMGATHER+'\',\''+ row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">详细</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="allotRoom.peifang('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">配房</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="allotRoom.housingSubsidy('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\',\''+ row.RECNUMGATHER +'\')">住房补贴</a>'
                }else if(value=='3'){
                    return '<a onclick="allotRoom.detail('+'\'' +row.RECNUMGATHER+'\',\''+ row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">详细</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="allotRoom.relieve('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">解除配房</a>'
                }else if(value=='4'){
                    return '<a onclick="allotRoom.detail('+'\'' +row.RECNUMGATHER+'\',\''+ row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">详细</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                        '<a onclick="allotRoom.relieveHousingSubsidy('+'\'' + row.OPTYPENUM + '\',\'' +row.RECYEAR + '\',\''+ row.RECNUM +'\')">解除补贴</a>'
                }
            }

        }
    ];
};


/**
 * 检查是否选中
 */
allotRoom.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');

    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        allotRoom.seItem = selected[0];
        return true;
    }
};


/**
 * 查询
 */
allotRoom.query = function(){

    var param = {
        "condition":$("#condition").val(),
        "status":$("#status").val(),

    };
    allotRoom.table.refresh({query: param});
}



allotRoom.initSecondType = function(pNum){
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
 * 关闭此对话框
 */
allotRoom.close = function () {
    parent.layer.close(window.parent.allotRoom.layerIndex);
}
/**
 * 关闭二级对话框
 */
allotRoom.closeSecond = function () {
    parent.layer.close(window.parent.allotRoom.secondLayerIndex);
}

$(function () {
    //初始化表格
    var data = {
        "condition":$("#condition").val(),
        "status":$("#status").val(),
    };
    allotRoom.url = "/allotRoom/index2";
    var defaultColunms = allotRoom.initColumn();
    var table = new BSTable(allotRoom.id,allotRoom.url, defaultColunms);
    table.setQueryParams(data);
    allotRoom.table = table.init();
});

/**
 * 显示详情
 * @param sRecNumGather
 * @param OPTYPENUM
 * @param RECYEAR
 * @param RECNUM
 */
allotRoom.detail = function (RECNUMGATHER,OPTYPENUM,RECYEAR,RECNUM) {
        var data = {sRecNumGather:RECNUMGATHER,
            iOpTypeNum:OPTYPENUM,
            iRecYear:RECYEAR,
            iRecNum:RECNUM
        };
        $.ajax({
            url:Feng.ctxPath + '/info/get_page_info',
            data:data,
            type:"post",
            success:function (result) {
                debugger
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
 * 查看此人分配的房屋信息
 */
allotRoom.searchHouse = function (OPTYPENUM,RECYEAR,RECNUM) {


    var data = {
        OpTypeNum:OPTYPENUM,
        RecYear:RECYEAR,
        RecNum:RECNUM
    };
    $.ajax({
        url:Feng.ctxPath + '/allotRoom/findHouse',
        data:data,
        type:"post",
        success:function (houseJson) {
            $("#roomNum").html(houseJson.house.roomNum);
            $("#obligee").html(houseJson.obligee);
            $("#architStructcode").html(houseTypeExchange(isNull(houseJson.house.architStructcode),buildingStructureListJson));
            $("#usage").html(houseTypeExchange(isNull(houseJson.house.usage),houseuSageListJson));
            $("#houseType").html(houseTypeExchange(isNull(houseJson.house.houseType),houseTypeListJson));
            $("#sitnumGather").html(isNull(houseJson.house.sitnumGather));
            $("#layout").html(isNull(houseJson.house.layout));
            $("#otherprop").html(isNull(houseJson.otherprop));
            $("#architArea").html(isNull(houseJson.house.architArea));
            $("#roomArea").html(isNull(houseJson.house.roomArea));
            $("#apportArea").html(isNull(houseJson.house.apportArea));
            $("#bargainTotalprice").html(isNull(houseJson.house.bargainTotalprice));
            $("#houseProp").html(isNull(houseJson.house.houseProp));
            $("#sitnumGather").html(isNull(houseJson.house.sitnumGather));
            $("#chooseHouse").hide();
            $("#editHouseModal").modal("show");

        },
        error:function (result) {
            Feng.info("系统异常！");
        }
    })
}

/**
 * 住房补贴
 */
allotRoom.housingSubsidy = function (OPTYPENUM,RECYEAR,RECNUM,RECNUMGATHER) {
    var index = layer.open({
        type: 2,
        title: '住房补贴设置',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/housingSubsidy/housing_subsidy?optypenum=' + OPTYPENUM +'&recyear=' + RECYEAR +'&recnum=' + RECNUM +'&recnumgather=' + RECNUMGATHER
    });
    allotRoom.layerIndex = index;
    layer.full(index);
}

//保存补贴信息
allotRoom.save = function () {
    var data = $("#housingSubsidyInfoForm").serializeJSON();
    if(data.rent == null || data.rent == "" || data.rent == undefined || data.rent == 0){
        Feng.info("请填写住房补贴金额！");
        return ;
    }
    if(data.startTime == null || data.startTime == "" || data.startTime == undefined) {
        Feng.info("请选择开始时间！");
        return ;
    }
    if(data.endTime == null || data.endTime == "" || data.endTime == undefined){
        Feng.info("请选择截止时间！");
        return ;
    }
    if (data.endTime <= data.startTime){
        Feng.info("截止时间小于等于开始时间！");
        return ;
    }

    data = {json:JSON.stringify(data)};
    $.ajax({
        url:'/housingSubsidy/save_subsidy',
        type:'POST',
        // contentType: 'application/json; charset=UTF-8',
        async:false,
        // dataType:'json',
        data:data,
        success: function (response) {
            if(response == 'Error'){
                Feng.info('保存失败');
            }else if (response == 'Already'){
                Feng.info('已经配房');
            }
            else {
                allotRoom.close();
                parent.allotRoom.table.refresh();
                Feng.success('保存成功');
            }
        }
    })

};

/**
 * 解除补贴
 */
allotRoom.relieveHousingSubsidy = function (OPTYPENUM,RECYEAR,RECNUM) {
    var data = {
        optypenum:OPTYPENUM,
        recyear:RECYEAR,
        recnum:RECNUM
    };
    var operation = function() {
        $.ajax({
            url: Feng.ctxPath + '/housingSubsidy/relieve_subsidy',
            data: data,
            type: "post",

            success: function (result) {
                Feng.success("解除补贴成功！", allotRoom.table.refresh());
            },
            error: function (result) {
                Feng.error("解除补贴失败!" );
            }
        })
    }
    Feng.confirm("是否解除补贴？", operation);

}

/**
 * 查看此人住房补贴信息
 */
allotRoom.lookHousingSubsidy = function (OPTYPENUM,RECYEAR,RECNUM) {
    var data = {
        optypenum:OPTYPENUM,
        recyear:RECYEAR,
        recnum:RECNUM
    };

    $.ajax({
        url:Feng.ctxPath + '/housingSubsidy/look_subsidy',
        data:data,
        type:"post",
        success:function (subsidyJson) {
            $("#applicantName").html(isNull(subsidyJson.apply.applicantName));
            $("#cardType").html(isNull(subsidyJson.apply.oWNERCERTTYPECODE));
            $("#applicantCard").html(isNull(subsidyJson.apply.applicantCard));
            $("#rent").html(isNull(subsidyJson.subsidy.rent));
            $("#remark").html(isNull(subsidyJson.subsidy.remark));
            $("#startTime").html(isNull(subsidyJson.subsidy.startTime));
            $("#endTime").html(isNull(subsidyJson.subsidy.endTime));
            $("#editSubsidyModal").modal("show");

        },
        error:function (result) {
            Feng.info("系统异常！");
        }
    })
}

/**
 * 配房
 */
allotRoom.peifang = function (OPTYPENUM,RECYEAR,RECNUM) {
    var index = layer.open({
        type: 2,
        title: '分配房屋',
        area: ['80%', '90%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/allotRoom/detail?OpTypeNum=' + OPTYPENUM +'&RecYear=' + RECYEAR +'&RecNum=' + RECNUM
    });
    allotRoom.layerIndex = index;
    layer.full(index);
}

/**
 * 解除配房
 */
allotRoom.relieve = function (OPTYPENUM,RECYEAR,RECNUM) {
    var data = {
        OpTypeNum:OPTYPENUM,
        RecYear:RECYEAR,
        RecNum:RECNUM
    };
    var operation = function() {
        $.ajax({
            url: Feng.ctxPath + '/allotRoom/relieve',
            data: data,
            type: "post",

            success: function (result) {
                Feng.success("解除成功！", allotRoom.table.refresh());
            },
            error: function (result) {
                Feng.error("解除失败!" );
            }
        })
    }
    Feng.confirm("是否解除配房？", operation);

}
//字典值转换
function houseTypeExchange(num,dateList){
    if(dateList != ''){
        for(var date in dateList){
            if(dateList[date].code == num){
                return dateList[date].value;
            }
        }
    }
    return "";
}

//空则显示空字符
function isNull (o) {
    if(o == null){
        return "";
    }else{
        return o;
    }
}

