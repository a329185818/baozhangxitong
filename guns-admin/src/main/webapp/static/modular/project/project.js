/**
 * 保障房项目初始化
 */
var projecManage = {
    id: "ProjectTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
projecManage.initColumn = function () {
    return [
        {field: '', radio: false,formatter:function(value,row,index){
                return '<input type="hidden" value="'+row.id+'">';
            }
        },
        {title: '项目名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '项目地址', field: 'address', align: 'center', valign: 'middle', sortable: true},
        {title: '建设单位', field: 'constructionUnit', align: 'center', valign: 'middle', sortable: true},
        {title: '建设套数', field: 'houseNumber', align: 'center', valign: 'middle', sortable: true},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                var str = '';
                if(row.canChange == '1'){
                    str +=  '<a onclick="projecManage.detail('+'\'' + row.id + '\',\'1\')">编辑</a>&nbsp;&nbsp;|&nbsp;&nbsp;';
                    if(bindingHouseTablePower == 'true'){
                        str += '<a onclick="projecManage.chooseHouseTable('+'\'' + row.id + '\')">绑定楼盘表</a>&nbsp;&nbsp;|&nbsp;&nbsp;';
                    }
                }
                str += '<a onclick="projecManage.detail('+'\'' + row.id + '\',\'0\')">查看</a>';
                return str;
            }
        }
    ];
};

projecManage.query = function(){
    var param = {
        "name":$("#condition").val()
    };
    projecManage.table.refresh({query: param});
};

/**
 * 项目详情
 * @param id
 * @param num
 */
projecManage.detail = function (id,num) {

    var index = layer.open({
        type: 2,
        title: '项目详情',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/project/project_detail?id='+id + '&num=' + num
    });
    projecManage.layerIndex = index;
    layer.full(index);
};

/**
 * 选择楼盘表
 * @param id
 * @param num
 */
projecManage.chooseHouseTable = function (id) {

    var index = layer.open({
        type: 2,
        title: '选择楼盘表',
        area: ['100%', '100%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/project/choose_houseTable?id=' + id
    });
    projecManage.layerIndex = index;
    layer.full(index);
};

/**
 * 关闭此对话框
 */
projecManage.close = function () {
    parent.layer.close(window.parent.projecManage.layerIndex);
};

projecManage.openExport = function () {

}

projecManage.exportExc = function (num) {
    var formData=new FormData();
    formData.append('excelExport', $("#excelExport")[0].files[0]);  /*获取上传的excel*/
    formData.append('num', num);  /*获取上传的excel*/
    if(num==1){
        $.ajax({
            url: '/excel/import_excel',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function (args) {
                if(args == '导入成功'){
                    window.location.reload();
                    Feng.success(args);
                }else{
                    Feng.info(args);
                }
            }
        });
    }else{
        $("#numInput").val(num);
        $("#import_excel").submit();

    }
}

//保存项目信息
projecManage.save = function () {
    var data = $("#projectInfoForm").serializeJSON();
    if(data.name == null || data.name == "" || data.name == undefined){
        Feng.info("项目名称不能为空！");
        return ;
    }
    if(data.startTime == null || data.startTime == "" || data.startTime == undefined) {
        data.startTime =  new Date(1900,0,1);
    }
    if(data.endTime == null || data.endTime == "" || data.endTime == undefined){
        data.endTime =  new Date(1900,0,1);
    }
    data = {json:JSON.stringify(data)};
    $.ajax({
        url:'/project/save',
        type:'POST',
        // contentType: 'application/json; charset=UTF-8',
        async:false,
        // dataType:'json',
        data:data,
        success: function (response) {
            if(response == 'Error'){
                Feng.info('保存失败');
            }else{
                projecManage.close();
                parent.location.reload();
                Feng.success('保存成功');
            }
        }
    })

};

projecManage.deleteProject = function () {
    layer.open({
        content: '确定删除所选项目？删除后无法恢复'
        ,btn: ['确定', '关闭']
        ,yes: function(index, layero){
            var id = '';
            $.each($('input[name="btSelectItem"]:checked'),function(){
                var valId = $(this).parent().find("input[type=hidden]").val();
                id += valId + ',';
            });
            var data = {id:id};
            $.ajax({
                url:'/project/delete',
                type:'POST',
                // contentType: 'application/json; charset=UTF-8',
                async:false,
                // dataType:'json',
                data:data,
                success: function (response) {
                    if(response == 'Error'){
                        Feng.info('删除失败');
                    }else{
                        location.reload();
                        Feng.success('删除成功');
                    }
                }
            })
        }
        ,btn2: function(index, layero){
            //按钮【按钮二】的回调
            //return false 开启该代码可禁止点击该按钮关闭
        }
        ,cancel: function(){
            //右上角关闭回调
        }
    });
};

$(function () {
    projecManage.url = "/project/list";
    var defaultColunms = projecManage.initColumn();
    var table = new BSTable(projecManage.id, projecManage.url, defaultColunms);
    projecManage.table = table.init();
});
