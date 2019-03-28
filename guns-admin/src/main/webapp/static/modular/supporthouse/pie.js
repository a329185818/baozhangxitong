var choose = [];    //选择的信息
//点击派件按钮,显示选择派件人界面
function choosePiePeople() {
    $("input:checkbox[name = btSelectItem]:checked").each(function(i){
        var index = $(this).data("index");
        var data = inqueryData[index];
        choose.push(data);
    });
    if(choose.length == 0){
        Feng.info("请选择需要派件的信息！");
        return ;
    }
    var index = layer.open({
        type: 2,
        title: '选择派件人',
        area: ['80%', '80%'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pie/choosePie'
    });
    houseProject.layerIndex = index;
}

//根据关键字查询选择派件人
function nameQuery() {
    var chooseName = $("#chooseName").val();
    var data = {name:chooseName};
    $.ajax({
        url:'/pie/chooseName',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {
            if(response.length != 0){
                var divStr = "";
                for(var i = 0;i<response.length;i++){
                    divStr += '<div class="col-sm-2">' +
                        '          <input type="radio" name="regional" style="margin-left:10px;" value="'+response[i].ID+'">' + response[i].ACCOUNT +
                        '      </div>';
                }
                $("#contentDiv").html(divStr);
            }
        }
    })
}

//执行派件操作
function pie(){
    var chooseRadio = $("input[name='regional']:checked").val();
    var chooseData = parent.choose; //获取选择的信息
    //重新构建数据结构
    var transformationData = [];
    for(var i = 0;i<chooseData.length;i++){
        var data = {};
        data.optypenum = chooseData[i].OPTYPENUM;
        data.recyear = chooseData[i].RECYEAR;
        data.recnum = chooseData[i].RECNUM;
        data.gxjxmmc = chooseRadio;
        transformationData.push(data);
    }
    var result=JSON.stringify(transformationData);
    $.ajax({
        type : 'POST',
        url : '/pie/pie_application',
        dataType : 'json',
        contentType:"application/json",
        data:result,
        success : function(data) {
            houseProject.close();
            parent.houseProject.query();
            Feng.success('派件成功');
        }
    });

}
