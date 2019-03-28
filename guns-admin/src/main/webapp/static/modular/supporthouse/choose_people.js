/**
 * 初始化
 */
var peopleChoose = {
    id: "PeopleTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
peopleChoose.initColumn = function () {
    return [
        {field: '', radio: false,formatter:function(value,row,index){
                return '';
            }
        },
        {title: '姓名', field: 'applicantName', align: 'center', valign: 'middle', sortable: true},
        {title: '身份证', field: 'applicantCard', align: 'center', valign: 'middle', sortable: true},
        {title: '配偶姓名', field: 'OWNERNAME_PEIOU', align: 'center', valign: 'middle', sortable: true},
        {title: '配偶身份证', field: 'OWNERCERTNUM_PEIOU', align: 'center', valign: 'middle', sortable: true},

        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                var str = '';
                str += '<a onclick="peopleChoose.chooseHouse(\'' + row.oPTYPENUM + '\',\'' + row.rECYEAR + '\',\'' + row.rECNUM + '\',\''+row.applicantName+'\')">配房</a>';
                return str;
            }
        }
    ];
};

peopleChoose.query = function(){
    var param = {
        "name":$("#condition").val()
    };
    peopleChoose.table.refresh({query: param});
};

/**
 * 选择房屋
 */
peopleChoose.chooseHouse = function (OPTYPENUM,RECYEAR,RECNUM,name) {
    layer.open({
        content: '确定给<'+name+'>配此房吗？'
        ,btn: ['确定', '关闭']
        ,yes: function(index, layero){
            var data = {
                OPTYPENUM:OPTYPENUM,
                RECYEAR:RECYEAR,
                RECNUM:RECNUM,
                checkHouseId:houseId,
                houseCode:'2'
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
                        //关闭当前窗口
                        peopleChoose.close();
                        parent.location.reload(); // 父页面刷新
                        Feng.success("分配房屋成功!");
                    }else if(response == "EXIT"){
                        Feng.info("此人已有分配房屋，不能再分配");
                    }else if(response == 'NOEXIT'){
                        Feng.info("查无此人，不能分配房屋");
                    }else{
                        Feng.info("分配失败");
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

/**
 * 关闭此对话框
 */
peopleChoose.close = function () {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
};

//保存项目信息
peopleChoose.save = function () {
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
                peopleChoose.close();
                parent.location.reload();
                Feng.success('保存成功');
            }
        }
    })

};

$(function () {
    //初始化表格
    var data = {
        "status":'2'
    };
    peopleChoose.url = "/support/people_list";
    var defaultColunms = peopleChoose.initColumn();
    var table = new BSTable(peopleChoose.id, peopleChoose.url, defaultColunms);
    table.setQueryParams(data);
    peopleChoose.table = table.init();
});
