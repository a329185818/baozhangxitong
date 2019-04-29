var buildInfo = {

}


buildInfo.showHouseInfo = function (floornum,unitcount,num) {
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
    var $floorUl = $('<ul style="cursor:pointer;"></ul>');
    $.each(floorHouse,function (i,item) {
        var date = showDate(item,num);
        var colorClass = showColor(item.houseCode);
        var $li = $('<li id="'+item.houseId+'" class="' + colorClass + '" housRoom="houseRoom" style="width:' + roomWidth + '%;">'+ date + '<input type="hidden" name="'+num+'" value="'+date+'"/></li>');
        //$("input[name = 'roomNum']").data(item.houseId, item);
        $floorUl.append($li);
    });
    $toFloor.html($floorUl);
}

buildInfo.showOtherDate = function (num,type) {
    var $tableTbody = $('#houseTableTbody');
    $tableTbody.html("");
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
        $tableTbody.append($tr);
        for(var z=0;z<=unitCount;z++) {
            var housenum = z;
            var floorNum = y;
            buildInfo.showHouseInfo(floorNum,housenum,num);
        }

    }
    if(type == 1){
        $tableTbody.find('li[housroom]').click(function () {
            var houseId = $(this).attr('id');
            $.each(house,function (i,item) {
                if(item.houseId == houseId){
                    $("#roomNum").html(item.roomNum);
                    $("#architStructcode").html(houseTypeExchange(isNull(item.architStructcode),buildingStructureListJson));
                    $("#usage").html(houseTypeExchange(isNull(item.usage),houseuSageListJson));
                    $("#houseType").html(houseTypeExchange(isNull(item.houseType),houseTypeListJson));
                    $("#sitnumGather").html(isNull(item.sitnumGather));
                    $("#layout").html(isNull(item.layout));
                    $("#otherprop").html(isNull(item.otherprop));
                    $("#architArea").html(isNull(item.architArea));
                    $("#roomArea").html(isNull(item.roomArea));
                    $("#apportArea").html(isNull(item.apportArea));
                    $("#houseProp").html(isNull(item.houseProp));
                    debugger;
                    $("#bargainTotalprice").html(isNull(item.bargainTotalprice));
                    if(item.houseCode != 0){
                        for(var i =0;i<holder.length;i++){
                            var a = item.houseId;
                            var b = holder[i].HOUSEID;
                            if(item.houseId == holder[i].HOUSEID){
                                $("#holderName").html(isNull(holder[i].HOLDERNAME));
                                $("#holderCard").html(isNull(holder[i].HOLDERCARD));
                                $("#holderSpouseName").html(isNull(holder[i].HOLDERSPOUSENAME));
                                $("#holderSpouseCard").html(isNull(holder[i].HOLDERSPOUSECARD));
                                $("#holderDiv").show();
                            }
                        }
                    }else{
                        $("#holderDiv").hide();
                    }
                    return false;
                }
            });
            $("#editHouseModal").modal("show");
        })
    }else{
        $tableTbody.find('li[housroom]').click(function () {
            var houseId = $(this).attr('id');
            $.each(house,function (i,item) {
                if(item.houseId == houseId){
                    if(item.houseCode == 0){
                        $("#checkHouseId").val(item.houseId);
                        $("#roomNum").html(item.roomNum);
                        $("#architStructcode").html(houseTypeExchange(isNull(item.architStructcode),buildingStructureListJson));
                        $("#usage").html(houseTypeExchange(isNull(item.usage),houseuSageListJson));
                        $("#houseType").html(houseTypeExchange(isNull(item.houseType),houseTypeListJson));
                        $("#sitnumGather").html(isNull(item.sitnumGather));
                        $("#layout").html(isNull(item.layout));
                        $("#otherprop").html(isNull(item.otherprop));
                        $("#architArea").html(isNull(item.architArea));
                        $("#roomArea").html(isNull(item.roomArea));
                        $("#apportArea").html(isNull(item.apportArea));
                        $("#houseProp").html(isNull(item.houseProp));
                        $("#bargainTotalprice").html(isNull(item.bargainTotalprice));
                        $("#chooseHouse").show();
                        $("#editHouseModal").modal("show");
                    }else{
                        Feng.info("该房屋不处再空置状态！");
                    }
                    return false;
                }
            });
        })
    }
}

buildInfo.createBuild = function (num,type) {
    $("#buildId").val(buildId);
    $("#unitCount").val(unitCount);
    $("#floorCount").val(floorCount);

    var $table = $('#houseTable');
    $table.find('thead').html("");
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
    this.showOtherDate(num,type);
}

//空则显示空字符
function isNull (o) {
    if(o == null){
        return "";
    }else{
        return o;
    }
}

//展示数据
function showDate(item,num){
    if(num == 'usage'){
        return houseTypeExchange(isNull(item[num]),houseuSageListJson);
    }else if(num == 'architStructcode'){
        return houseTypeExchange(isNull(item[num]),buildingStructureListJson);
    }else if(num == 'houseType') {
        return houseTypeExchange(isNull(item[num]),houseTypeListJson);
    }else{
        return isNull(item[num]);
    }
}

//显示的颜色样式
function showColor(num){
    var color = 'vacantColor';
    if(num != 0){
        color = 'noVancantColor';
    }
    return color;
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

$(function () {

})