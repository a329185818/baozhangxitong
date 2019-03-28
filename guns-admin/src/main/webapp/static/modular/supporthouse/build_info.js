var floorUnitRoomNum = [];
var chooseId = "";
var buildInfo = {

}


buildInfo.showHouseInfo = function (floornum,unitcount) {
    var count = 0;
    var floorHouse = [];
    var juge = true;
    var $toFloor = $("td[floornum='"+floornum+"'][unitcount='"+unitcount+"']");
    $.each(house,function (i,item) {
        if(item.floorNum == floornum && item.unitNum == unitcount ){
            count ++;
            floorHouse.push(item);
            juge = false;
        }
    });
    if(juge){
        return ;
    }
    var roomWidth = 1/count*100;     //每个房间的宽度
    var $floorUl = $('<ul></ul>');
    $.each(floorHouse,function (i,item) {
        var colorClass = showColor(item.houseCode);
        var $li = $('<li id="'+item.houseId+'" class="' + colorClass + '" housRoom="houseRoom" style="width:' + roomWidth + '%;">'+ item.roomNum + '<input type="hidden" name="roomNum" value="'+item.roomNum+'"/></li>');
        //$("input[name = 'roomNum']").data(item.houseId, item);
        $floorUl.append($li);
    });
    $toFloor.html($floorUl);
}

buildInfo.createBuild = function () {
    $("#buildId").val(buildId);
    $("#unitCount").val(unitCount);
    $("#floorCount").val(floorCount);

    var $table = $('#houseTable');
    /**
     * 生成head
     */
    for( var x=0;x<=unitCount;x++ ){
        if( x == 0 ){
            $table.find('thead').append('<th>*</th>')
        }else{
            $table.find('thead').append('<th>第' + x + '单元</th>')
        }
    }

    /**
     * 生成body
     */
    for( var y=floorCount;y>0;y-- ){
        var $tr = $('<tr></tr>');

        for(var z=0;z<=unitCount;z++){
            var housenum = z;
            var floorNum = y;
            if(z == 0){
                $tr.append('<td>'+ floorNum + '</td>');
            }else{
                $tr.append('<td floorNum="'+floorNum+'" unitCount="'+housenum+'"></td>');
            }
        }
        $table.find('tbody').append($tr);
        for(var z=0;z<=unitCount;z++) {
            var housenum = z;
            var floorNum = y;
            buildInfo.showHouseInfo(floorNum,housenum);
        }

    }
    $("#houseForm").html($table);

     $table.find('li[housroom]').click(function () {
         if($(this).hasClass('floor_selected')){
             chooseId = chooseId.replace($(this).attr('id')+",","");
             $(this).removeClass('floor_selected');
         }else{
             chooseId += $(this).attr('id') + ',';
             $(this).addClass('floor_selected');
         }
     })

}

/**
 * 将给楼层添加房间数
 */
buildInfo.addRoomToFloor = function () {

    var unit = $("#unit").val();
    var floor = $("#floor").val();
    var roomCount = $('#house_count').val();  //添加的房屋数
    var $toFloor = $("td[floornum='"+floor+"'][unitcount='"+unit+"']");

    if($toFloor.children().length){
        Feng.info("该楼层已添加房屋，请勿重复操作!");
        return ;
    }

    var roomWidth = 1/roomCount*100;     //每个房间的宽度
    var $floorUl = $('<ul></ul>');

    for(var i=1;i<=roomCount;i++){
        var roomNum;
        if(i<10){
            roomNum = $toFloor.attr('floornum') + '0' + i;      //房号
        }else{
            roomNum = $toFloor.attr('floornum')  + i;      //房号
        }

        var $li = $('<li style="width:' + roomWidth + '%">'+ roomNum +'<input type="hidden" name="roomNum" value="'+roomNum+'"/></li>');
        var floorUnitRoom = {};
        floorUnitRoom.roomNum = roomNum;
        floorUnitRoom.floorNum = floor;
        floorUnitRoom.unitNum = unit;
        floorUnitRoom.buildId = buildId;
        floorUnitRoomNum.push(floorUnitRoom);
        $floorUl.append($li);
    }

    $toFloor.html($floorUl);
}

/**
 * 显示添加房屋数编辑框
 */
buildInfo.showAddRoomNum = function () {
    $("#roomCount").val("");
    $('#addHouseToFloorModal').modal("show");
}

buildInfo.createAllHouse = function () {

    $.ajax({
        url:'/build/create_house',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        async:false,
        dataType:'json',
        data:JSON.stringify(floorUnitRoomNum),
        success: function (response) {
            Feng.success("新建成功!");
            window.location.reload();
        }
    })
}

/**
 * 编辑选中按钮
 */
buildInfo.editHouse = function(){
    if(chooseId != ""){
        var n=(chooseId.split(',')).length-1;
        if(n == 1){
            var str = chooseId.substr(0,chooseId.length-1);
            $.each(house,function (i,item) {
                if(item.houseId == str){
                    $("#roomNum").val(item.roomNum);
                    $("#architStructcode").val(item.architStructcode);
                    $("#usage").val(item.usage);
                    $("#houseType").val(item.houseType);
                    $("#houseProp").val(item.houseProp);
                    $("#layout").val(item.layout);
                    $("#otherprop").val(item.otherprop);
                    $("#architArea").val(item.architArea);
                    $("#roomArea").val(item.roomArea);
                    $("#apportArea").val(item.apportArea);
                    $("#bargainTotalprice").val(item.bargainTotalprice);
                }
            });
            $("#roomNum").attr("disabled",false);
        }else{
            $("#roomNum").val("");
            $("#roomNum").attr("disabled",true);
            $("#architStructcode").val("");
            $("#usage").val("");
            $("#houseType").val("");
            $("#houseProp").val("");
            $("#layout").val("");
            $("#otherprop").val("");
            $("#architArea").val("");
            $("#roomArea").val("");
            $("#apportArea").val("");
            $("#bargainTotalprice").val("");
        }

        $("#editHouseModal").modal("show");
    }else{
        Feng.info("请选择需要编辑的房屋!");
    }

}

/**
 * 保存按钮
 */
buildInfo.commitHouseInfo = function(){
    var architStructcode = $("#architStructcode").val();
    if(architStructcode == "" || architStructcode == null){
        Feng.info("请选择房屋建筑结构!");
        return ;
    }
    var houseType = $("#houseType").val();
    if(houseType == "" || houseType == null || houseType == "0"){
        Feng.info("请选择房屋户型!");
        return ;
    }
    var usage = $("#usage").val();
    if(usage == "" || usage == null || usage == "0"){
        Feng.info("请选择房屋用途!");
        return ;
    }

    var data = $("#saveHouseForm").serializeJSON();
    data = {json:JSON.stringify(data),
        chooseId:chooseId};
    $.ajax({
        url:'/build/update_house',
        type:'POST',
        //contentType: 'application/json; charset=UTF-8',
        async:false,
        //dataType:'json',
        data:data,
        success: function (response) {
            Feng.success("编辑成功!");
            window.location.reload();
        }
    })
}

/**
 * 删除按钮
 */
buildInfo.deleteHouse = function(){
    if(window.confirm('确定要删除已选房屋吗？')){
        var date = {chooseId:chooseId};
        $.ajax({
            url:'/build/delete_house',
            type:'POST',
            //contentType: 'application/json; charset=UTF-8',
            async:false,
            //dataType:'json',
            data:date,
            success: function (response) {
                Feng.success("删除成功!");
                window.location.reload();
            }
        })
    }
}

/**
 * 配房按钮
 */
buildInfo.checkHouse = function () {
    var houseId = chooseId.split(',');
    var n=houseId.length-1;
    if(n == 1){
        var classNmae = $("#"+houseId[0]).attr("class");
        if(classNmae.indexOf("noVancantColor") >= 0 ) {
            Feng.info("该房不处于空置状态");
            return ;
        }
        var index = layer.open({
            type: 2,
            title: '配房',
            area: ['80%', '90%'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/build/choose_people?id=' + houseId[0]
        });
        buildInfo.layerIndex = index;
    }else{
        Feng.info("不能同时配多个房，请选择一个房间进行配房");
    }
};

//显示的颜色样式
function showColor(num){
    var color = 'vacantColor';
    if(num != 0){
        color = 'noVancantColor';
    }
    return color;
}

$(function () {
    buildInfo.createBuild();
})