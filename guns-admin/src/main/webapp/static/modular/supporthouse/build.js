var build = {
    id:"managerTable",
    curId:0,
    curPid:0,
    layerIndex:null,
    table: null,
    buildNum:"",
    unitCount:"",
    floorCount:"",
    projectName:"",
    sitnumgather:"",
    buildId :""
}

/**
 * 初始化表格的列
 */
build.initColumn = function () {
    return [
        {field: 'selectItem', radio: false,formatter:function(value,row,index) {
            return '<input data-index="0" name="btSelectItemBuildId" type="hidden" value="'+row.buildId+'">';
        }},
        {title: '栋号', field: 'buildNum', align: 'center', valign: 'middle'},
        {title: '总单元数', field: 'unitCount', align: 'center', valign: 'middle'},
        {title: '总层数', field: 'floorCount', align: 'center', valign: 'middle'},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                var str = '';
                if(num == 1){
                    str += '<a onclick="build.createBuildPage( '+ '\''+ row.buildId + '\',\'' + row.buildNum + '\',\'' +row.unitCount + '\',\''+ row.floorCount +'\')">编辑</a>&nbsp;&nbsp;|&nbsp;&nbsp;';
                }
                str += '<a onclick="build.detail( '+ '\''+ row.buildId + '\',\'' + row.buildNum + '\',\'' +row.unitCount + '\',\''+ row.floorCount +'\')">查看</a>';
                return str;
            }

        }
    ];
};

/**
 * 显示创建项目编辑框
 */
build.showEditProject = function () {
    $("#projectName").val("");
    $("#sitnumgather").val("");
    $('#createPorjectModal').modal("show");
}

/**
 * 显示创建栋编辑框
 */
build.showEditRidgepole = function () {
    $("#buildNum").val("");
    $("#unitCount").val("");
    $("#floorCount").val("");
    $('#createRidgepoleModal').modal("show");
}

/**
 * 更新项目信息
 */
build.updateProject = function () {
    $("#projectName").val(build.projectName);
    $("#sitnumgather").val(build.sitnumgather);
    $("#projectId").val(build.curId);
    $('#createPorjectModal').modal("show");
}

/**
 * 删除项目
 */
build.deleteProject = function () {
    if(window.confirm('确定要删除该项目吗？该操作不可逆！PS：不能删除还存在栋的项目')) {
        var data = {projectId:build.curId};
        $.ajax({
            url:'/build/delete_project',
            type:'POST',
            //contentType: 'application/json; charset=UTF-8',
            async:false,
            //dataType:'json',
            data:data,
            success: function (response) {
                if(response == 'ERROR'){
                    Feng.info("选择的项目存在栋，删除失败！!");
                }else{
                    Feng.success("删除成功!");
                }

                window.location.reload();
            }
        })
    }

}

/**
 * 提交项目信息
 */
build.createProject = function () {
    if(!$("#projectName").val()){
        $("#projectName").focus();
        return;
    }
    if(!$("#sitnumgather").val()){
        $("#sitnumgather").focus();
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/build/create_project", function (data) {
        if(data == 'CREATE'){
            Feng.success("新建成功!");
        }else if(data == 'UPDATE'){
            Feng.success("更新成功!");
        }
        window.location.reload();
    }, function (data) {
        Feng.error("创建或失败!" + data.responseJSON.message + "!");
    });
    ajax.set("projectName", $("#projectName").val());
    ajax.set("sitnumgather", $("#sitnumgather").val());
    ajax.set("projectId", $("#projectId").val());
    ajax.set("region", $("#region").val());
    ajax.start();
}

/**
 * 提交栋信息
 */
build.createRidgepole = function () {
    if(!$("#buildNum").val()){
        $("#buildNum").focus();
        return;
    }
    if(!$("#unitCount").val()){
        $("#unitCount").focus();
        return;
    }
    if(!$("#floorCount").val()){
        $("#floorCount").focus();
        return;
    }
    var ajax = new $ax(Feng.ctxPath + "/build/create_ridgepole", function (data) {
        Feng.success("新建成功!");
        window.location.reload();
    }, function (data) {
        Feng.error("创建失败!" + data.responseJSON.message + "!");
    });
    ajax.set("buildNum", $("#buildNum").val());
    ajax.set("unitCount", $("#unitCount").val());
    ajax.set("floorCount", $("#floorCount").val());
    ajax.set("projectId", build.curId);
    ajax.start();
}

/**
 * 打开创建楼盘编辑页
 */
build.createBuildPage = function (buildId,buildNum,unitCount,floorCount) {
    build.buildNum = buildNum;
    build.unitCount = unitCount;
    build.floorCount = floorCount;
    build.buildId = buildId;
    var index = layer.open({
        type: 2,
        title: '楼盘表创建',
        area: ['80%', '90%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/build/open_add?buildId='+buildId
    });
    build.layerIndex = index;
    //layer.full(index);
}

/**
 * 打开查看楼盘页
 */
build.detail = function (buildId,buildNum,unitCount,floorCount) {
    build.buildNum = buildNum;
    build.unitCount = unitCount;
    build.floorCount = floorCount;
    build.buildId = buildId;
    var index = layer.open({
        type: 2,
        title: '楼盘表查看',
        area: ['80%', '90%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/build/open_detail?buildId='+buildId
    });
    build.layerIndex = index;
    //layer.full(index);
}

/**
 * 删除楼盘
 */
build.deleteBuild = function () {
    var buildId = "";
    $.each($(".selected"),function (i,item) {
        //获取选择的栋数
        var input = $(item).find("input")[1];
        //获取栋唯一号
        var val = $(input).val();
        buildId += val + ',';
    });
    if(buildId == ""){
        Feng.info("请选择需要删除的栋数!");
    }else{
        if(window.confirm('确定要删除已选栋吗？该操作不可逆！PS：不能删除还存在房屋的栋')) {
            var data = {buildId:buildId};
            $.ajax({
                url:'/build/delete_build',
                type:'POST',
                //contentType: 'application/json; charset=UTF-8',
                async:false,
                //dataType:'json',
                data:data,
                success: function (response) {
                    if(response == 'ERROR'){
                        Feng.info("选择的栋存在房屋，删除失败！!");
                    }else{
                        Feng.success("删除成功!");
                    }

                    window.location.reload();
                }
            })
        }
    }
}


/***********zTtree click start***********/

build.treeClick = function (e, treeId, treeNode) {
    build.curId = treeNode.id;
    if(treeNode.pId){
        return;
    }
    build.search();
}

//显示右键菜单
function showRMenu(type, x, y) {
    $("#rMenu ul").show();
    $("#rMenu").css({"top":y+"px", "left":x+"px", "visibility":"visible"}); //设置右键菜单的位置、可见
    $("body").bind("mousedown", onBodyMouseDown);
}
//隐藏右键菜单
function hideRMenu() {
    if ($("#rMenu")) $("#rMenu").css({"visibility": "hidden"}); //设置右键菜单不可见
    $("body").unbind("mousedown", onBodyMouseDown);
}
//鼠标按下事件
function onBodyMouseDown(event){
    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
        $("#rMenu").css({"visibility" : "hidden"});
    }
}

build.treeRightClick = function (event, treeId, treeNode) {
    build.curPid =treeNode.pId;
    build.curId = treeNode.id;
    build.projectName = treeNode.name;
    build.sitnumgather = treeNode.value;
    if(treeNode.pId){
        return;
    }
    if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
        showRMenu("root", event.clientX, event.clientY);
    } else if (treeNode && !treeNode.noR) {
        showRMenu("node", event.clientX, event.clientY);
    }
}

/***********zTtree click end***********/

build.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['projectId'] = build.curId;
    build.table.refresh({query: queryData});
};

$(function () {
    //去掉默认的contextmenu事件，否则会和右键事件同时出现。
    document.oncontextmenu = function(e){
        e.preventDefault();
    };

    var buildTree = new $ZTree("buildTree", "/build/tree");
    buildTree.bindOnClick(build.treeClick);
    buildTree.bindRightClick(build.treeRightClick);
    buildTree.init();
    if(buildId==0){
        buildId="none"
    }
    var data = {
        projectId:buildId
    };
    var defaultColunms = build.initColumn();
    var table = new BSTable(build.id, "/build/build_list", defaultColunms);
    table.setQueryParams(data);
    build.table = table.init();

})