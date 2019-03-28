/**
 * 初始化通知详情对话框
 */
var ProjectInfoDlg = {
    layerIndex:-1,
    projectInfoData: {},
    editor: null,
    commonParam:{
        iOptypenum:-1,
        iRecyear:-1,
        iRecnum:-1,
        iRecmatnum:-1
    },

    validateFields: {
        'CELLCOUNT': {
            validators: {
                integer: {
                    message: '请输入整数'
                }
            }
        },
        'ARCHITAREA': {
            validators: {
                integer: {
                    message: '请输入整数'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
ProjectInfoDlg.clearData = function () {
    this.projectInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ProjectInfoDlg.set = function (key, value) {
    this.projectInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ProjectInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ProjectInfoDlg.close = function () {
    parent.layer.close(window.parent.houseProject.layerIndex);
}

/**
 * 收集数据
 */
ProjectInfoDlg.collectData = function () {
    this.projectInfoData = $("#projectInfoForm").serializeJSON();
}

/**
 * 验证数据
 */
ProjectInfoDlg.validate = function () {
    $('#projectInfoForm').data("bootstrapValidator").resetForm();
    $('#projectInfoForm').bootstrapValidator('validate');
    return $("#projectInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 保存项目细项
 */
ProjectInfoDlg.saveRecOwner = function () {
    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/support/save_recowner", function (result) {
        if(result.code == '200'){
            Feng.success(result.msg);
            window.parent.houseProject.table.refresh();
            ProjectInfoDlg.close();
        }else {
            Feng.info("操作失败！");
        }
    }, function (result) {
        Feng.info("系统异常！");
    });
    ajax.set(this.projectInfoData);
    ajax.start();
}

//文件上传页面
ProjectInfoDlg.openUploadPage = function (OPTYPENUM,RECYEAR,RECNUM,RECMATNUM) {
    var index = layer.open({
        type: 2,
        title: '材料上传',
        area: ['60%', '60%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/support/open_upload?iOptypenum=' + OPTYPENUM +'&iRecyear=' + RECYEAR +'&iRecnum=' + RECNUM + "&iRecMatnum=" + RECMATNUM
    });
    ProjectInfoDlg.layerIndex = index;
}

//图片显示页面
ProjectInfoDlg.openImagePage = function (OPTYPENUM,RECYEAR,RECNUM,RECMATNUM) {
    var index = layer.open({
        type: 2,
        title: '材料查看',
        area: ['60%', '60%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/support/show_image?iOptypenum=' + OPTYPENUM +'&iRecyear=' + RECYEAR +'&iRecnum=' + RECNUM + "&iRecMatnum=" + RECMATNUM
    });
    ProjectInfoDlg.layerIndex = index;
    layer.full(index);
}

/**
 * 至下阶段或转交箱
 * @param url
 */
ProjectInfoDlg.toNext = function (url) {
    var data = $("#handle_opinions").serializeJSON();
    data = {
        json:JSON.stringify(data),
        iOptypenum:this.commonParam.iOptypenum,
        iRecyear:this.commonParam.iRecyear,
        iRecnum:this.commonParam.iRecnum,
        iRecmatnum:this.commonParam.iRecmatnum
    };

    $.ajax({
        url:url,
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {
            if(response == 'ERROR'){
                Feng.info("保存失败！");
            }else{
                Feng.success("保存成功!");
                ProjectInfoDlg.close();
            }
        }
    })

}

ProjectInfoDlg.returnNode = function () {
    var returnReason = $("textarea[name='returnReason']").val();
    if(returnReason == null || returnReason == ''){
        Feng.info("请填写退回意见！");
        return "";
    }
    var index = layer.open({
        type: 2,
        title: '办理节点列表',
        area: ['80%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/support/return_node?iOptypenum=' + this.commonParam.iOptypenum +'&iRecyear=' + this.commonParam.iRecyear +'&iRecnum=' + this.commonParam.iRecnum
    });

}

/**
 * 关闭对话框
 */
ProjectInfoDlg.closeSecond = function () {
    parent.layer.close(ProjectInfoDlg.layerIndex);
}

/**
 * 增加上传文件栏
 */
ProjectInfoDlg.addUploadTable = function (OPTYPENUM,RECYEAR,RECNUM) {
    ProjectInfoDlg.commonParam.iRecmatnum++;
    var trId = "fileTr" + ProjectInfoDlg.commonParam.iRecmatnum;
    var materialStr = "";
    if(materialListJson != ''){
        for(var material in materialListJson){
            materialStr += '<option value="'+materialListJson[material].code+'">'+materialListJson[material].value+'</option>'
        }
    }
    var str = '<tr id="'+trId+'">' +
        '           <input type="hidden" name="optypenum[]" value="'+OPTYPENUM+'">' +
        '           <input type="hidden" name="recyear[]" value="'+RECYEAR+'">' +
        '           <input type="hidden" name="recnum[]" value="'+RECNUM+'">' +
        '           <input type="hidden" name="recmatnum[]" value="'+ProjectInfoDlg.commonParam.iRecmatnum+'">' +
        '           <td>' +
        '               <input type="checkbox">' +
        '           </td>' +
        '           <td><input type="text" class="tableInputClass" name="matname[]"></td>' +
        '           <td>' +
        '           <select class="form-control" name="mattypecode[]">' +
                        materialStr +
        '           </select>' +
        '           </td>' +
        '           <td>' +
        '               <input type="text"  class="tableInputClass" value="1" name="matcount[]">' +
        '           </td>' +
        '           <td>' +
        '               <input type="text" class="tableInputClass" value="1" name="matpage[]">' +
        '           </td>' +
        '           <td><input type="text" class="tableInputClass" name="matkindcode[]"></td>' +
        '           <td>' +
        '                <input type="button" onclick="ProjectInfoDlg.openUploadPage('+OPTYPENUM+','+RECYEAR+','+RECNUM+','+ProjectInfoDlg.commonParam.iRecmatnum+')" value="上传">' +
        '                <input type="button" onclick="ProjectInfoDlg.openImagePage('+OPTYPENUM+','+RECYEAR+','+RECNUM+','+ProjectInfoDlg.commonParam.iRecmatnum+')" value="查看">' +
        '                <input type="button" onclick="ProjectInfoDlg.deleteUpload('+trId+')" value="删除">' +
        '           </td>' +
        '      </tr>';
    $("#uploadTbody").append(str);
}
/**
 * 删除多余的tr
 */
ProjectInfoDlg.deleteUpload = function (trId) {
    $(trId).remove();
}

/**
 * 保存家庭信息
 */
ProjectInfoDlg.saveFamilySurvey = function () {
    //手机号判断正则表达式
    var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
    var phone = $("#TELCODE").val();
    if (!myreg.test(phone)) {
        Feng.info("联系电话格式错误！");
        return ;
    }

    var data = $("#projectInfoForm").serializeJSON();
    data = {json:JSON.stringify(data)};
    $.ajax({
        url:'/support/save_family',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {
            Feng.success("保存成功!");
            location.reload();
        }
    })
}


/**
 * 保存文件上传
 */
ProjectInfoDlg.saveRecOpinions = function () {
    var data = structureDateType();
    $.ajax({
        url:'/support/save_file_yu',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        async:false,
        dataType:'json',
        data:JSON.stringify(data),
        success: function (response) {
            if(response == 'ERROR'){
                Feng.info("保存失败！");
            }else{
                Feng.success("保存成功!");
                location.reload();
            }
        }
    })
}

/**
 * 构建新的数据类型
 */
function structureDateType() {
    var data = $("#uploadTbodyForm").serializeJSON();
    var newData = [];
    //构建数据格式
    for(var i = 0;i<data.matname.length;i++){
        var file_yuVo = {};
        file_yuVo.optypenum = data.optypenum[i];
        file_yuVo.recyear = data.recyear[i];
        file_yuVo.recnum = data.recnum[i];
        file_yuVo.recmatnum = data.recmatnum[i];
        file_yuVo.matname = data.matname[i];
        file_yuVo.mattypecode = data.mattypecode[i];
        file_yuVo.matcount = data.matcount[i];
        file_yuVo.matpage = data.matpage[i];
        file_yuVo.matkindcode = data.matkindcode[i];
        newData.push(file_yuVo);
    }
    return newData;
}
/**
 * 保存意见
 */
ProjectInfoDlg.saveHandle = function () {
    var data = $("#handle_opinions").serializeJSON();
    data = {json:JSON.stringify(data)};

    $.ajax({
        url:'/support/save_handle',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {
            if(response == 'ERROR'){
                Feng.info("保存失败！");
            }else{
                Feng.success("保存成功!");
            }
        }
    })
}

ProjectInfoDlg.buildSearch = function () {
    var projectName = $("#projectName").val();
    var buildName = $("#buildName").val();

    if(buildName == '' || buildName == undefined){
        if(projectName == '' || projectName == undefined) {
            Feng.info("请输入查询关键字！");
            return ;
        }
    }
    var date ={
        buildName:buildName,
        projectName:projectName
    };
    $.ajax({
        url:'/support/build_list',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:date,
        success: function (response) {
            var buildDivStr = "";
            for(var i = 0;i<response.length;i++){
                buildDivStr += '<div class="col-sm-4" ><input type="radio" name="buildNameRadio" value="'+response[i].buildId+'" />'+response[i].buildNum;
                buildDivStr += '</div>';
            }
            buildDivStr += '<div class="col-sm-12" style="text-align: center;margin-top: 20px;">' +
                           '<button type="button" class="btn btn-info" onclick="ProjectInfoDlg.buildSure();" id="buildIdButton">' +
                           '    <i class="fa"></i>&nbsp;确定' +
                           '</button></div>';

            $("#buildDiv").html(buildDivStr);
            Feng.success("查询完成!");
            for(var i = 0;i<response.length;i++){
                $("input[name='buildNameRadio']").data(response[i].buildId,response[i]);//可以这样来存数据
            }
        }
    })
}

ProjectInfoDlg.buildSure = function(){
    //获取选中的值
    buildId = $("input[name='buildNameRadio']:checked").val();
    var data = $("input[name='buildNameRadio']").data(buildId);
    floorCount = data.floorCount;
    unitCount = data.unitCount;
    buildNum = data.buildNum;
    var getDate = {
        buildId:buildId
    };
    $.ajax({
        url:'/support/show_detail',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:getDate,
        success: function (response) {
            if(response.houseList != null && response.houseList != ''){
                house = response.houseList;
            }else{
                house = [];
            }
            buildInfo.createBuild('roomNum','2');
            Feng.success("楼盘表生成成功!");
        }
    })
};

ProjectInfoDlg.refund = function (OPTYPENUM,RECYEAR,RECNUM) {
    var data = {
        OPTYPENUM:OPTYPENUM,
        RECYEAR:RECYEAR,
        RECNUM:RECNUM
    };

    $.ajax({
        url:'/support/refund',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {

        }
    })
}

ProjectInfoDlg.checkHouse = function (OPTYPENUM,RECYEAR,RECNUM,houseCode) {
    var checkHouseId = $("#checkHouseId").val();
    var data = {
        OPTYPENUM:OPTYPENUM,
        RECYEAR:RECYEAR,
        RECNUM:RECNUM,
        checkHouseId:checkHouseId,
        houseCode:houseCode
    };
    $.ajax({
        url:'/support/check_house',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {
            if(response == "SUCCESS"){
                $("#editHouseModal").modal("hide");
                location.reload();
                Feng.success("分配房屋成功!");
            }else if(response == "EXIT"){
                Feng.info("此人已有分配房屋，不能再分配")
            }else if(response == 'NOEXIT'){
                Feng.info("查无此人，不能分配房屋")
            }else{
                Feng.info("分配失败")

            }
        }
    })
}

/**
 * 查看此人分配的房屋信息
 */
ProjectInfoDlg.searchHouse = function () {

    $("#roomNum").html(houseJson.roomNum);
    $("#architStructcode").html(houseTypeExchange(isNull(houseJson.architStructcode),buildingStructureListJson));
    $("#usage").html(houseTypeExchange(isNull(houseJson.usage),houseuSageListJson));
    $("#houseType").html(houseTypeExchange(isNull(houseJson.houseType),houseTypeListJson));
    $("#sitnumGather").html(isNull(houseJson.sitnumGather));
    $("#layout").html(isNull(houseJson.layout));
    $("#otherprop").html(isNull(houseJson.otherprop));
    $("#architArea").html(isNull(houseJson.architArea));
    $("#roomArea").html(isNull(houseJson.roomArea));
    $("#apportArea").html(isNull(houseJson.apportArea));
    $("#bargainTotalprice").html(isNull(houseJson.bargainTotalprice));
    $("#chooseHouse").hide();
    $("#editHouseModal").modal("show");
}

var buildId;//栋id
var floorCount;//总层数
var unitCount;//总单元
var buildNum;//栋号
var house;
$(function () {
    if(judgeInfo == 1 || readonly == 'true'){
        $(document.body).find("input").attr("readonly","readonly");
        $(document.body).find("input[type=radio]").attr("disabled",true);
        $(document.body).find("select").attr("disabled","disabled");
        $(document.body).find("button").hide();
        $("input[name='uploadButton']").hide();
    }
});
