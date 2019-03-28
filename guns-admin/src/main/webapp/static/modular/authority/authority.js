/**
 * 保障房项目初始化
 */
var authorityAllot = {
    id: "AuthorityTable",	//表格id
    userId:"",
    authorityType:"",
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
authorityAllot.initColumn = function () {
    return [
        {field: 'selectItem', radio: false},
        {title: '姓名', field: 'NAME', align: 'center', valign: 'middle', sortable: true},
        {title: '部门', field: 'deptName', align: 'center', valign: 'middle', sortable: true},
        {title: '邮箱', field: 'EMAIL', align: 'center', valign: 'middle', sortable: true},
        {title: '电话', field: 'PHONE', align: 'center', valign: 'middle', sortable: true},

        {title: '分配权限',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                return '<a onclick="authorityAllot.detail('+'\'' + row.ID + '\',\'编辑\')">编辑</a>&nbsp;&nbsp;|&nbsp;&nbsp;' +
                    '<a onclick="authorityAllot.detail('+'\'' + row.ID + '\',\'查看\')">查看</a>';
            }
        }
    ];
};

authorityAllot.query = function(){
    var param = {
        "name":$("#condition").val()
    };
    authorityAllot.table.refresh({query: param});
};

authorityAllot.detail = function (userId,authorityType) {

    var index = layer.open({
        type: 2,
        title: authorityType + '权限分配',
        area: ['80%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/authority/authority_allot?userId='+userId +'&authorityType=' + authorityType
    });
    authorityAllot.userId = userId;
    authorityAllot.authorityType = authorityType;
    authorityAllot.layerIndex = index;

}

/**
 * 关闭此对话框
 */
authorityAllot.close = function () {
    parent.layer.close(window.parent.authorityAllot.layerIndex);
}

//根据关键字查询项目
function nameQuery() {
    var chooseName = $("#chooseName").val();
    var data = {name:chooseName};
    $.ajax({
        url:'/authority/project_list',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {
            if(response.length != 0){
                //隐藏全部
                $("div.projectClass").hide();
                //显示查询到的数据
                for(var i = 0;i<response.length;i++){
                    $("#"+response[i].id).show();
                }
            }
        }
    })
}

//保存权限
function save() {
    var chooseProject = [];
    //获取选中的项目
    $.each($('input:checkbox:checked'),function(){
        var choose = {};
        choose.projectId = $(this).val();
        choose.userId = parent.authorityAllot.userId;
        choose.authorityType = parent.authorityAllot.authorityType;
        chooseProject.push(choose);
    });
    if(chooseProject.length == 0){
        var choose = {};
        choose.userId = parent.authorityAllot.userId;
        choose.authorityType = parent.authorityAllot.authorityType;
        chooseProject.push(choose);
    }
    $.ajax({
        url:'/authority/save',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        async:false,
        dataType:'json',
        data:JSON.stringify(chooseProject),
        success: function (response) {
            authorityAllot.close();
            Feng.success('保存成功');
        }
    })

}


$(function () {
    authorityAllot.url = "/authority/list";
    var defaultColunms = authorityAllot.initColumn();
    var table = new BSTable(authorityAllot.id, authorityAllot.url, defaultColunms);
    table.setPaginationType("client");
    authorityAllot.table = table.init();

    //当点击全选/全不选框时，全选或者全不选全部
    $("#checkAll").click(function(event){
        var check_state = event.target.checked;
        $('input[name="projectName"][type="checkbox"]').each(function() {
            this.checked = check_state;
        });
    });
    //点击数据的单选框
    $(".projectName").click(function(event){
        var flage =  true;
        var check_state = event.target.checked;
        $(".projectName").each(function() {
            if(this.checked != check_state){
                flage = false;
            }
        });
        if(flage){
            $("#checkAll")[0].checked = check_state;
        }else{
            $("#checkAll")[0].checked = false;
        }
    });
});
