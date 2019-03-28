/**
 * 保障房项目初始化
 */
var houseTable = {
    id: "houseTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    url:"",
    secondLayerIndex:-1
};

/**
 * 初始化表格的列
 */
houseTable.initColumn = function () {
    return [
        {field: '', radio: false,formatter:function(value,row,index){
                return '<input type="hidden" value="'+row.projectId+'">';
            }
        },
        {title: '项目名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '项目地址', field: 'sitnum', align: 'center', valign: 'middle', sortable: true},
        {title: '所在区域', align: 'center', valign: 'middle', sortable: true,
            formatter:function(value,row,index){
                for(var i = 0;i<regionList.length;i++){
                    if(regionList[i].code == row.roadCode){
                        return regionList[i].value;
                    }
                }
                return "";
            }
        },
        {title: '操作',  align: 'center', valign: 'middle',
            formatter:function(value,row,index){
                var str = '';
                str +=  '<a onclick="houseTable.chooseProject('+'\'' + row.projectId + '\',\''+row.name+'\')">绑定此项目</a>' ;
                return str;
            }
        }
    ];
};

houseTable.query = function(){
    var param = {
        "name":$("#condition").val()
    };
    houseTable.table.refresh({query: param});
};

/**
 *  选择楼盘表
 * @param id
 * @param num
 */
houseTable.chooseProject = function (id,name) {
    layer.open({
        content: '确定选择此楼盘表<'+name+'>？'
        ,btn: ['确定', '关闭']
        ,yes: function(index, layero){
            var data = {
                projectId:projectId,
                buildReceive:id
            };
            $.ajax({
                url:'/project/choose',
                type:'POST',
                // contentType: 'application/json; charset=UTF-8',
                async:false,
                // dataType:'json',
                data:data,
                success: function (response) {
                    if(response == 'Error'){
                        Feng.info('绑定失败');
                    }else{
                        //关闭当前窗口
                        houseTable.close();
                        Feng.success('绑定成功');
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
houseTable.close = function () {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
};

$(function () {
    houseTable.url = "/project/table_list";
    var defaultColunms = houseTable.initColumn();
    var table = new BSTable(houseTable.id, houseTable.url, defaultColunms);
    houseTable.table = table.init();
});
