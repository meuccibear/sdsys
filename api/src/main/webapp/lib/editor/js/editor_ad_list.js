/**
 * 界面设计器的节目列表脚本
 */
function getToken(){
    if(window.localStorage){
        var tk = window.localStorage.getItem("ad_sys_tk");
        if (tk == null)
            return '';
        return tk;
    }
    return '';
}

function addBgi(){
	var nodes = zTree.getSelectedNodes();
	var newBgi = nodes[0].resPath
	var tab_id = designerTab.getSelectedTabItemID();
	if (nodes[0].children && nodes[0].children.length>0) {
		alert($.i18n.prop('cannotsetbgMsg'));
		return; 
	}
	if(nodes[0].fileType!='pic'){
		alert($.i18n.prop('bgtipMsg'))
	}
	if(nodes[0].fileType=='pic'){
		var tabtabtag = tab_id.replace('tab_','')
		$("[tabtag='"+tabtabtag+"']").css("background-image","url('"+pageCxt+'/'+newBgi+"')")
		p_global_data.Rows[3].pvalue = '/'+newBgi;
	}
}
//取消背景图
function deleteBgi(){
	var nodes = zTree.getSelectedNodes();
	var tab_id = designerTab.getSelectedTabItemID();
	var tabtabtag = tab_id.replace('tab_','')
	$("[tabtag='"+tabtabtag+"']").css("background-image",'')
	p_global_data.Rows[3].pvalue = '';
}
function ElementDrag(elementFather,elementOne){
    var n_type_tag = elementOne.attr('typetag');
    var leftFather = $(elementFather).offset().left
    var topFather = $(elementFather).offset().top
    var widthFather = $(elementFather).width()
    var heightFather = $(elementFather).height()
    var leftOne = elementOne.offset().left
    
    if (leftOne == "auto")
        leftOne = 0;
    var topOne = elementOne.offset().top
    if (topOne == "auto")
        topOne = 0;
    var widthOne = $(elementOne).width()
    var heightOne = $(elementOne).height()
    if(n_type_tag != 'music' && n_type_tag != 'voiceControl'){
        //左移动判断
        if(leftOne - leftFather-1<0){           
            $(elementOne).css({'left':leftFather+'px'})        
        }
        //上移动判断
        if(topOne - topFather-1<0){        
            $(elementOne).css({'top':topFather+'px'})
        }
        //右移动判断
        if(leftOne+widthOne - leftFather-widthFather-1> 0){            
            $(elementOne).css({'left':leftFather+widthFather-widthOne+'px'})
        };
        //下移动判断
        if(topOne+heightOne - topFather-heightFather> 0){           
            $(elementOne).css({'top':topFather+heightFather-heightOne+'px'})
        }
        pdata.Rows[2].pvalue = parseInt((elementOne.offset().left - $(elementFather).offset().left)/scale);
        pdata.Rows[3].pvalue = parseInt((elementOne.offset().top - $(elementFather).offset().top)/scale);
        propertyGridObj.set({data:pdata});
        selObj.attr('tag',JSON.stringify(pdata));
    }
}

function ElementResizable(elementFather,elementOne){
	var leftFather = $(elementFather).offset().left
	var topFather = $(elementFather).offset().top
    var widthFather = $(elementFather).width()
    var heightFather = $(elementFather).height()
    var leftOne = elementOne.offset().left
    if (leftOne == "auto")
        leftOne = 0;
    var topOne = elementOne.offset().top
    if (topOne == "auto")
        topOne = 0;
    var widthOne = $(elementOne).width()
    var heightOne = $(elementOne).height()
    //左拉
    if(leftOne+widthOne>leftFather+widthFather+1){
        $(elementOne).css({'left':leftFather+widthFather+1-widthOne+'px'});
        if(widthOne>widthFather){
            $(elementOne).width(widthFather-2)
            $(elementOne).css({'left':leftFather+2+'px'});
        };
    };
    //下拉
    if(topOne+heightOne>topFather+heightFather+1){
        $(elementOne).css({'top':topFather+heightFather+1-heightOne+'px'})
        if(heightOne>heightFather){
            $(elementOne).height(heightFather-2)
            $(elementOne).css({'top':topFather+2+'px'});
        };
    };
    //右拉
    if(leftOne<leftFather+1){
        $(elementOne).css({'left':leftFather+2+'px'})
        if(widthOne>widthFather){
            $(elementOne).width(widthFather-2)
            $(elementOne).css({'left':leftFather+2+'px'});
        };
    };
    //上拉
    if(topOne<topFather+1){
        $(elementOne).css({'top':topFather+2+'px'})
        if(heightOne>heightFather){
            $(elementOne).height(heightFather-2)
            $(elementOne).css({'top':topFather+2+'px'});
        };
    };
    pdata.Rows[4].pvalue = parseInt($(elementOne).width()/scale);
    pdata.Rows[5].pvalue = parseInt($(elementOne).height()/scale);
    propertyGridObj.set({data:pdata});
    selObj.attr('tag',JSON.stringify(pdata));
}

/*function setPosEvent(lm, source){
    var tab_id = designerTab.getSelectedTabItemID();
    var n_type_tag = source.attr('typeTag');
    var to = $("div[tabid='"+tab_id+"']").find("div[name='adDesiger']");
    var parentObj = $(source[0]).parents('div[name="adDesiger"]')[0];
    var tog = $("#"+source[0].id);
    //console.log("parent --> tpos: "+parentObj.offsetTop+"    lpos："+parentObj.offsetLeft);
    var t1 = tog.css("top").replace("px","");
    if (t1 == "auto")
        t1 = 0;

    var t2 = parentObj.offsetTop;
    var t3 = lm.css("top").replace("px","");

    var l1 = tog.css("left").replace("px","");
    if (l1 == "auto")
        l1 = 0;
    var l2 = parentObj.offsetLeft;
    var l3 = lm.css("width").replace("px","");

    if(n_type_tag != 'music' && n_type_tag != 'voiceControl'){
        var tpos = parseInt((t1 - t2 - 145)/scale);
        var lpos = parseInt((l1 - l2 - l3 -17)/scale);
        //console.log("otpos:"+parseInt((t1 - t2 - 145))+"tpos: "+tpos+"olpos:" +parseInt((l1 - l2 - l3 -17)) + "    lpos："+lpos);
        if(tpos<0){
            pdata.Rows[3].pvalue = 0;
            tog.css("top",(145+t2)+"px");
            //console.log("ttttt   "+tog.css("top"));
        }else{
            pdata.Rows[3].pvalue =  tpos;
        }
        if((tpos*scale) > (parentObj.clientHeight)){
            pdata.Rows[3].pvalue = $("div[tabid='"+tab_id+"']").attr("dp_h");
            tog.css("top",(145+t2+parentObj.clientHeight)+"px");
        }
        if(lpos<0){
            pdata.Rows[2].pvalue =  0;
            //console.log("================change left : "+(parseInt(17)+parseInt(l2)+parseInt(l3)) +"             ===="+tog.css("left"));
            tog.css("left",(parseInt(17)+parseInt(l2)+parseInt(l3))+"px");
            ///console.log("lllllll   "+tog.css("left"));
            return false;
        }else{
            pdata.Rows[2].pvalue = lpos;
        }
        if((lpos*scale) > (parentObj.clientWidth)){
            pdata.Rows[2].pvalue =  $("div[tabid='"+tab_id+"']").attr("dp_w");
            tog.css("left",(parseInt(17)+parseInt(l2)+parseInt(l3)+parentObj.clientWidth)+"px");
        }
        propertyGridObj.set({data:pdata});
        selObj.attr('tag',JSON.stringify(pdata));
    }
}*/

//点击元件事件，切换绑定
function selObjevent(tarObj,ctype){
    if(selObj != undefined && selObj.attr("id") != $(tarObj).attr("id")){
        selObj.css('border','1px solid rgb(99, 98, 96)');
        var tag = JSON.stringify(pdata);
        selObj.attr('tag',tag);
    }
    selObj = $(tarObj);
    selObj.css('border','2px solid #e21010');//选中后的边框
    //选择资源树对应的节点，并展开
    if(ctype != 1){//等于1的时候表明是点击树出发的事件，不需要重新选择树
        var opNode = prozTree.getNodeByParam("id", selObj.attr("id"), null);
        prozTree.expandNode(opNode, true, true, true);
        prozTree.selectNode(opNode);
    }

    var ntag = selObj.attr('tag');
    var n_type_tag = selObj.attr('typeTag');
    if(ntag != undefined && ntag != ''){
        pdata = JSON.parse(ntag);
    }else{//首次点击
        if(n_type_tag == 'pic'){
            pdata = d_pic_data;
        }else if(n_type_tag == 'video'){
            pdata = d_video_data;
        }else if(n_type_tag == 'music'){
            pdata = d_voice_data;
        }else if(n_type_tag == 'sub'){
            pdata = d_sub_data;
        }else if(n_type_tag == 'calendar'){
            pdata = d_calender_data;
        }else if(n_type_tag == 'clock'){
            pdata = d_clock_data;
        }else if(n_type_tag == 'weather'){
            pdata = d_weather_data;
        }else if(n_type_tag == 'web'){
            pdata = d_web_data;
        }else if(n_type_tag == 'voiceControl'){
            pdata = d_voiceCtr_data;
        }else if(n_type_tag == 'txt'){
            pdata = d_txt_data;
        }
        selObj.attr('nametag',pdata.Rows[1].pvalue);
        selObj.attr('tag',JSON.stringify(pdata));
    }

    if(n_type_tag == 'voiceControl'){
        var exttag = selObj.attr('exttag');
        var manager = $("#propertyVoice").ligerGetGridManager();
        var datasize = manager.getData().length;
        for(var d = 0; d < datasize; d++)
            manager.deleteRow(0);

        if(exttag != undefined && exttag != ''){
            var items = JSON.parse(exttag);

            for(var i = 0; i < items.length; i++){
                manager.addRow(items[i]);
            }
        }
        $("#voicePanel").show();
    }else{
        $("#voicePanel").hide();
    }
    propertyGridObj.set({data:pdata});

    if(dragObj != undefined && dragObj != null){
        dragObj.unbind('Drop');
        dragObj.unbind('DragEnter');
        dragObj.unbind('DragOver');
    }
    dragObj = selObj.ligerDrag({ proxy: false,receive:'#mainLayout' });
    /*var lm = $("#mainLayout").find(".l-layout-left");*/
    dragObj.bind('DragOver', function (receive, source, e)
    {
        /*setPosEvent(lm, source);*/
        var elementFather = $(source[0]).parents('div[name="adDesiger"]')[0]
        var elementOne = $(source[0])
        ElementDrag(elementFather,elementOne);
    });
    dragObj.bind('Drop', function (receive, source, e)
    {
        /*setPosEvent(lm, source);*/
        var elementFather = $(source[0]).parents('div[name="adDesiger"]')[0]
        var elementOne = $(source[0])
        ElementDrag(elementFather,elementOne);
    });

    if(resizeObj != undefined && resizeObj != null){
        resizeObj.unbind('EndResize');
    }
    resizeObj = selObj.ligerResizable({
        scope:10
    });
    resizeObj.bind('EndResize',function(e){

        if(n_type_tag != 'music' && n_type_tag != 'voiceControl'){
            var elementFather = $(resizeObj.target[0]).parents('div[name="adDesiger"]')[0]
            var elementOne = $(resizeObj.target[0])
            
            ElementResizable(elementFather,elementOne);
            ElementDrag(elementFather,elementOne);
            /*setPosEvent(lm, resizeObj.target);*/
        }
    });



}

//工具方法
//用于生成uuid
function S4() {
    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}

function guid() {
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}

//加载节目列表
var prozTree;

var prosetting = {
    data: {
        keep: {
            parent: true,
            leaf: true
        },
        simpleData: {
            enable: true
        }
    },
    callback: {
        onDblClick: openProgramTab,
        onClick : selectProgramTab,
        onRightClick: OnRightClickProgram
    },
    view: {
        selectedMulti: false,
        dblClickExpand: false
    }
};

//右键菜单事件
function OnRightClickProgram(event, treeId, treeNode) {
    if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
        zTree.cancelSelectedNode();
        showPMenu("root", event.clientX, event.clientY);
    } else if (treeNode && !treeNode.noR) {
        if(treeNode.pId == '0'){//根目录下直接挂节目数据
            zTree.selectNode(treeNode);
            showPMenu("node", event.clientX, event.clientY);
        }else if (treeNode.res != undefined && treeNode.level > 2){
            zTree.selectNode(treeNode);
            showPMenu("res", event.clientX, event.clientY);
        }
    }
}

function showPMenu(type, x, y) {
    $("#pMenu ul").show();
    if (type=="root") {
	$("#m_pcopy").hide();
        $("#m_pdel").hide();
        $("#m_moveup").hide();
        $("#m_movedown").hide();
    } else if (type=="node"){
	$("#m_pcopy").show();
        $("#m_pdel").show();
        $("#m_moveup").hide();
        $("#m_movedown").hide();
    } else if (type=="res"){
	$("#m_pcopy").hide();
        $("#m_pdel").hide();
        $("#m_moveup").show();
        $("#m_movedown").show();
    }
    y += document.body.scrollTop;
    x += document.body.scrollLeft;
    pMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

    $("body").bind("mousedown", onPMenuMouseDown);
}

function hidePMenu() {
    if (pMenu) pMenu.css({"visibility": "hidden"});
    $("body").unbind("mousedown", onPMenuMouseDown);
}

function onPMenuMouseDown(event){
    if (!(event.target.id == "pMenu" || $(event.target).parents("#pMenu").length>0)) {
        pMenu.css({"visibility" : "hidden"});
    }
}

//加载节目树
function loadPrograms(){
    $.ajax({
        type: "GET",
        url: pageCxt+"/adEditor/programs/list",
        headers:{'X-Access-Token':getToken()},
        data: null,
        dataType: "json",
        success: function(data){
            if(data.data != null){
                var items = data.data;
                prozTree = $.fn.zTree.init($("#proTree"), prosetting, items);
                var rootNode = prozTree.getNodeByParam("id", 0, null);
                prozTree.expandNode(rootNode);

                var selprog_html = '';
                selprogObj.clear();
                selprogObj.addItem({id:"website", text:"http://"});
                for(var i = 0; i < items.length; i++){
                    if (items[i].pId == "0"){
                        var item = {id:items[i].id, text:items[i].name};
                        selprogObj.addItem(item);
                    }
                }
                selprogObj.setText($.i18n.prop('voiceseltipMsg'));
            }


        }
    });
}

//删除节目
function delProgram() {
    hidePMenu();
    var nodes = zTree.getSelectedNodes();

    $.ajax({
        type : "POST",
        url : pageCxt+"/adEditor/programs/delete?id="+nodes[0].id,
        headers:{'X-Access-Token':getToken()},
        data: null,
        dataType : "json",
        success:function(obj){
            if(obj.success){
                alert(obj.msg);
                parent.loadPrograms();
            } else {
                alert(obj.msg);
            }
        },error:function(){
            alert($.i18n.prop('accesserrorMsg'));
        }
    });
}

function copyProgram(item){
       var nodes = zTree.getSelectedNodes();
       var tab_id = designerTab.getSelectedTabItemID();
       $("#m_pcopy").hide();
       $("#m_pdel").hide();
       var name = prompt($.i18n.prop('inputcopynameMsg'),"");
       if(name == null){
       alert($.i18n.prop('cancelcopyMsg'))
    }else{
               $.ajax({
               type : "POST",
               url:pageCxt+"/adEditor/programs/copy?id="+nodes[0].id+"&name="+name,
               headers:{'X-Access-Token':getToken()},
               success:function(obj){
                    if(obj.success){
			alert($.i18n.prop('copyokMsg'));
		        parent.loadPrograms();
		    } else {
		        alert(obj.msg);
		    }   
               },error:function(data){
			alert($.i18n.prop('accesserrorMsg'));
               }
           });
    }
}

//点击上方的新增按钮
function itemclick_add(item)
{
    actDialog = $.ligerDialog.open({
        url:pageCxt+'/ad/editor/editProgram',
        width:400,height:430,isResize:true,
        title:$.i18n.prop('creatprogramTitle'),
        allowClose:false,
        buttons : [
            {
                text : $.i18n.prop('submitTitle'),onclick : function(item,dialog){
                    dialog.frame.save(dialog);
                }
            },{
                text : $.i18n.prop('cancelTitle'),onclick : function(item,dialog){
                    dialog.close();
                }
            }
        ]
    });
}

//点击上方的删除按钮
function itemclick_del(item)
{
    if(selObj == undefined || selObj == null){
        alert($.i18n.prop('delobjtipMsg'));return;
    }
    var isok = confirm($.i18n.prop('delobjMsg'));
    if(isok){
        var oid = selObj.attr("id");
        selObj.remove();
        //todo:同步节目树
        var opNode = prozTree.getNodeByParam("id", oid, null);
        prozTree.removeNode(opNode);
        selObj = null;
    }
}

//点击上方的保存按钮
function itemclick_save(item)
{
    saveCurTab();
}

function saveCurTab(){
    var tab_id = designerTab.getSelectedTabItemID();
    var parr = [];

    $("div[tabid='"+tab_id+"']").find("div[name='adDesiger']").find("div[class='domBtn']").each(function(){
        var obj = {};
        obj.name= $(this).attr('nametag');
        obj.type = $(this).attr('typeTag');
        obj.res = $(this).attr('resref');
        if (obj.type == "voiceControl")
            obj.setting = $(this).attr('exttag');
        else
            obj.setting = $(this).attr('tag');
        obj.pid = tab_id.replace("tab_","");
        parr.push(obj);
    });

    var pobj = {};
    pobj.name= '节目';
    pobj.type = 'program';
    pobj.res = '';
    pobj.setting = JSON.stringify(p_global_data);
    pobj.pid = tab_id.replace("tab_","");
    parr.push(pobj);

    $.ajax({
        type : "POST",
        url : pageCxt+"/adEditor/programs/additem",
        headers:{'X-Access-Token':getToken()},
        data: JSON.stringify(parr),
        dataType : "json",
        contentType:'application/json',
        success:function(obj){
            if(obj.success){
                alert(obj.msg);
                loadPrograms();//加载节目树
                reloadTab(tab_id.replace("tab_",""),
                		scale,
                		$("div[tabid='"+tab_id+"']").attr("dp_w"),
                		$("div[tabid='"+tab_id+"']").attr("dp_h"), 
                		JSON.stringify(p_global_data));//重新刷新创作栏
            } else {
                alert(obj.msg);
            }
        },error:function(){
//				$.ligerDialog.error('system error');
        }
    });
}
//预览和发送调用的保存方法
function saveCurTabByOther(){
    var tab_id = designerTab.getSelectedTabItemID();
    var parr = [];

    $("div[tabid='"+tab_id+"']").find("div[name='adDesiger']").find("div[class='domBtn']").each(function(){
        var obj = {};
        obj.name= $(this).attr('nametag');
        obj.type = $(this).attr('typeTag');
        obj.res = $(this).attr('resref');
        if (obj.type == "voiceControl")
            obj.setting = $(this).attr('exttag');
        else
            obj.setting = $(this).attr('tag');
        obj.pid = tab_id.replace("tab_","");
        parr.push(obj);
    });

    var pobj = {};
    pobj.name= '节目';
    pobj.type = 'program';
    pobj.res = '';
    pobj.setting = JSON.stringify(p_global_data);
    pobj.pid = tab_id.replace("tab_","");
    parr.push(pobj);

    $.ajax({
        type : "POST",
        url : pageCxt+"/adEditor/programs/additem",
        headers:{'X-Access-Token':getToken()},
        data: JSON.stringify(parr),
        dataType : "json",
        contentType:'application/json',
        success:function(obj){
            if(obj.success){
                /*alert(obj.msg);*/
                loadPrograms();//加载节目树
                reloadTab(tab_id.replace("tab_",""),
                		scale,
                		$("div[tabid='"+tab_id+"']").attr("dp_w"),
                		$("div[tabid='"+tab_id+"']").attr("dp_h"), 
                		JSON.stringify(p_global_data));//重新刷新创作栏
            } else {
                alert(obj.msg);
            }
        },error:function(){
//				$.ligerDialog.error('system error');
        }
    });
}


//点击上方的保存为模板按钮
function itemclick_saveTemplate(item){
    var name = prompt($.i18n.prop('inputtempnameMsg'),"");
    if(name == null){
        return;
    }
    var tab_id = designerTab.getSelectedTabItemID();
    $.ajax({
        type : "POST",
        url:pageCxt+"/adEditor/programs/tempetes/add/"+tab_id.replace("tab_","")+"?name="+name,
        headers:{'X-Access-Token':getToken()},
        data: null,
        dataType : "json",
        contentType:'application/json',
        success:function(obj){
            if(obj.success){
                alert(obj.msg);
            } else {
                alert(obj.msg);
            }
        },error:function(){
//				$.ligerDialog.error('system error');
        }
    });
}

//点击上方的预览按钮
function itemclick_pre(item)
{
    var tab_id = designerTab.getSelectedTabItemID();
    var tabs = $("div[tabid='"+tab_id+"']").find("div[name='adDesiger']");
    var orgwidth = parseInt(tabs.css("width").replace("px",""));
    var width = $("div[tabid='"+tab_id+"']").attr("dp_w");
    var orgheight = parseInt(tabs.css("height").replace("px",""));
    var height = $("div[tabid='"+tab_id+"']").attr("dp_h");
    var wwidth = orgwidth+40;
    var wheigth = orgheight+40;
    saveCurTabByOther();
    actDialog = $.ligerDialog.open({
        url:pageCxt+'/ad/editor/preview?pid='+tab_id.replace("tab_","")+"&width="+width+"&height="+height+"&orgheight="+orgheight+"&orgwidth="+orgwidth+"&wheight="+wheigth+"&wwidth="+wwidth,
        width:wwidth+30,height:wheigth+80,isResize:true,
        title:$.i18n.prop('previewTitle'),
        allowClose:false,
	isResize:false,
        buttons : [
            {
                text : $.i18n.prop('cancelTitle'),onclick : function(item,dialog){
                    dialog.close();
                }
            }
        ]
    });
}

//点击上方的发送按钮
function itemclick_sent(item)
{
    var tab_id = designerTab.getSelectedTabItemID();
    var pname = $("li[tabid=" + tab_id + "]").find("a").text();
    saveCurTabByOther();
    actDialog = $.ligerDialog.open({
        url:pageCxt+'/ad/editor/sent?id='+tab_id.replace("tab_","") + '&pname=' + pname,
        width:400,height:320,isResize:true,
        title:$.i18n.prop('publishTitle'),
        allowClose:true,
        showMax:true,
        buttons : [
            {
                text : $.i18n.prop('submitTitle'),onclick : function(item,dialog){
                    dialog.frame.save(dialog);
                }
            },{
                text : $.i18n.prop('cancelTitle'),onclick : function(item,dialog){
                    dialog.close();
                }
            }
        ]
    });
}

//单击节目树
function selectProgramTab(event, treeId, treeNode){
    $("#voicePanel").hide();
    if(treeNode.pId == '0'){//根目录下直接挂节目数据
        var tab_id = "tab_"+treeNode.id;
        if(designerTab.isTabItemExist(tab_id)){
            designerTab.selectTabItem(tab_id);
            $.extend(p_global_data.Rows,JSON.parse($("div[tabid='"+tab_id+"']").attr("setting")).Rows);
            propertyGridObj.set({data:p_global_data});
        }
    }
    if(treeNode.isComp == 1){//单击是选中编辑器中的组件
        selObjevent($("#"+treeNode.id),1);
        //预览图片或视频 preImg，预览当前背景
        /*
        $("#preImg").css('background-image',$("#"+treeNode.id).css('background-image'));
        $("#preDiv").show();
        */
        $("#preDiv").hide();
        $("#preVideoDiv").hide();
    }else if(treeNode.isContent == 1){//单击是内容，需要预览
        selObjevent($("#"+treeNode.pId),1);
        var type = $("#"+treeNode.pId).attr('typeTag');
        //预览图片或视频 preImg
        if(type == 'pic'){
            $("#preImg").css('background-image','url('+treeNode.res+')');
            $("#preDiv").show();
            $("#preVideoDiv").hide();
            $("#preAudioDiv").hide();
        }else if(type == 'video'){
            $("#preVideo").attr('src',treeNode.res);
            $("#preVideoDiv").show();
            $("#preDiv").hide();
            $("#preAudioDiv").hide();
        }

    }else{
        propertyGridObj.set({data:p_global_data});
        $("#preDiv").hide();
        $("#preVideoDiv").hide();
    }
}

//处理自适应分辨率
function handleScale(){

}

//双击节目树，打开tab
function openProgramTab(event, treeId, treeNode){
    if(treeNode.pId == '0'){//根目录下直接挂节目数据
        var tab_id = "tab_"+treeNode.id;
        if(designerTab.isTabItemExist(tab_id)){
            designerTab.selectTabItem(tab_id);
        }else{
            designerTab.addTabItem({tabid:tab_id,text:treeNode.name});
            var p_treeNodeId = treeNode.id;//抽取加载节目的方法，参数1
            var p_width = treeNode.dpw;//界面编辑器的宽度,参数三
            var p_height = treeNode.dph;//界面编辑器的高度,参数四
            var c_width = $("#adTab").css("width").replace("px","")-60;
            var c_height = $("#adTab").css("height").replace("px","")-102;
            if (c_width < p_width || c_height < p_height){
                var w_scale = c_width/p_width;
                var h_scale = c_height/p_height;
                if (w_scale < h_scale)
                    scale = w_scale;
                else
                    scale = h_scale;
            }
            else
                scale = 1;
            var p_scale = scale;//缩放比例,参数二

            reloadTab(p_treeNodeId,p_scale,p_width,p_height,JSON.stringify(d_global_data));
        }
    }

}

//重新加载tab面包里面的内容
function reloadTab(p_treeNodeId,p_scale,p_width,p_height,setting){
    //==================================================
    var divHtml = '<div tabtag="'+p_treeNodeId+'" id="adDesiger'+p_treeNodeId+'" name="adDesiger" style="background-image:url('+p_global_data.Rows[3].pvalue+');display:inline-block;border: 1px solid #f5850c;width:'+p_width*p_scale+'px;height: '+p_height*p_scale+'px;margin: 30px 30px;vertical-align: middle;"></div>';
    var tab_id = "tab_"+p_treeNodeId;
    $("div[tabid='"+tab_id+"']").css("display","flex");
    $("div[tabid='"+tab_id+"']").attr("dp_w",p_width);
    $("div[tabid='"+tab_id+"']").attr("dp_h",p_height);
    $("div[tabid='"+tab_id+"']").attr("setting",setting);
    $("div[tabid='"+tab_id+"']").html(divHtml);
    $.extend(p_global_data.Rows,JSON.parse(setting).Rows);
    propertyGridObj.set({data:p_global_data});
    initDragMenu();
    $.ajax({
        type: "GET",
        url: pageCxt+"/adEditor/programs/items?pid="+p_treeNodeId,
        headers:{'X-Access-Token':getToken()},
        data: null,
        dataType: "json",
        success: function(data){
            if(data.data != null){
                var tmpData = null;
                var desigerObj = $("#adDesiger"+p_treeNodeId);
                var tmpres = null;
                var tmpSetting = null;
                var tmpbgsize = null;
                var lpos = 0;
                var tpos = 0;
                var lm = $("#mainLayout").find(".l-layout-left");
                for(var i=0;i<data.data.length;i++){
                    tmpData = data.data[i];

                    if(tmpData.type == 'pic'){//初始化图片
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_pic_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_pic_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/pic.png' : tmpData.res.split(";")[tmpData.res.split(";").length-1];
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_pic_data)+"' onmousedown='selObjevent(this)' typeTag='pic' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'></div>");
                    }else if(tmpData.type == 'video'){//初始化视频
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            tmpSetting = $.extend(p_video_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_video_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/video.png' : tmpData.res.split(";")[tmpData.res.split(";").length-1];
                        var videoHtml = "";
                        if(tmpData.res != null && tmpData.res != ''){
                            videoHtml = "<video src='"+tmpres+"' width='100%' height='100%'></video>";
                        }
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_video_data)+"' onmousedown='selObjevent(this)' typeTag='video' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px; left:"+lpos+"px ;border:2px solid rgb(99, 98, 96);'>"+videoHtml+"</div>");
                    }else if(tmpData.type == 'sub'){//初始化字幕
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_sub_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_sub_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/subtitles.png' : tmpData.res;
                        var subHtml = "";
                        if(tmpSetting[6].pvalue != null && tmpSetting[6].pvalue != ''){
                            var margintop = 0;
                            if (tmpSetting[5].pvalue > tmpSetting[8].pvalue)
                                margintop = (tmpSetting[5].pvalue - tmpSetting[8].pvalue)/2*scale;
                            subHtml = "<marquee behavior='scroll' direction='left' align='center' style='text-align:center;margin-top:"+margintop+"px;'><span style='font-weight: bolder;font-size: "+tmpSetting[8].pvalue*p_scale+"px;color: "+tmpSetting[7].pvalue+";'>"+tmpSetting[6].pvalue+"</span></marquee>";
                        }
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_sub_data)+"' onmousedown='selObjevent(this)' typeTag='sub' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'>"+subHtml+"</div>");
                    }else if(tmpData.type == 'music'){//初始化音乐
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_voice_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_voice_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/music.png' : tmpData.res.split(";")[tmpData.res.split(";").length-1];
                        var audioHtml = "";
                        if(tmpData.res != null && tmpData.res != ''){
                            audioHtml = "<audio src='"+tmpres+"' width='300px' height='90px' controls='controls'></audio>";
                        }
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_voice_data)+"' onmousedown='selObjevent(this)' typeTag='music' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'>"+audioHtml+"</div>");
                    }else if(tmpData.type == 'calendar'){//初始化日期
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_calender_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_calender_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/calendar.png' : tmpData.res;
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_calender_data)+"' onmousedown='selObjevent(this)' typeTag='calendar' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'></div>");
                    }else if(tmpData.type == 'clock'){//初始化时钟
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_clock_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_clock_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/clock.png' : tmpData.res;
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_clock_data)+"' onmousedown='selObjevent(this)' typeTag='clock' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'></div>");
                    }else if(tmpData.type == 'weather'){//初始化天气
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_weather_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_weather_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/weather.png' : tmpData.res;
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_weather_data)+"' onmousedown='selObjevent(this)' typeTag='weather' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'></div>");
                    }else if(tmpData.type == 'web'){//初始化网页
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_web_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_web_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/web.png' : tmpData.res;
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_web_data)+"' onmousedown='selObjevent(this)' typeTag='web' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'></div>");
                    }else if(tmpData.type == 'voiceControl'){//初始化声控
                        /*if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_voiceCtr_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }*/
                        tmpSetting = d_voiceCtr_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/voiceControl.png' : tmpData.res;
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(d_voiceCtr_data)+"' exttag='"+tmpData.setting+"' onmousedown='selObjevent(this)' typeTag='voiceControl' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'></div>");
                    }else if(tmpData.type == 'txt'){//初始化文本
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_txt_data.Rows,JSON.parse(tmpData.setting).Rows);
                        }
                        tmpSetting = p_txt_data.Rows;
                        tpos = parseInt(tmpSetting[3].pvalue)*p_scale+145+parseInt(desigerObj[0].offsetTop);
                        lpos = parseInt(tmpSetting[2].pvalue)*p_scale+17+parseInt(lm.css("width").replace("px",""))+parseInt(desigerObj[0].offsetLeft);
                        tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/timg.jpg' : tmpData.res;
                        var txtHtml = "";
                        if(tmpSetting[6].pvalue != null && tmpSetting[6].pvalue != ''){
                            var margintop = 0;
                            if (tmpSetting[5].pvalue > tmpSetting[8].pvalue)
                                margintop = scale;
                            	txtHtml = "<p   style='margin-top:"+margintop+"px;'><span style='word-wrap:break-word;font-weight: bolder;font-size: "+tmpSetting[8].pvalue*p_scale+"px;color: "+tmpSetting[7].pvalue+";'>"+tmpSetting[6].pvalue+"</span></p>";
                        }
                        tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%';
                        desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' tag='"+JSON.stringify(p_txt_data)+"' onmousedown='selObjevent(this)' typeTag='txt' id='"+tmpData.id+"' class='domBtn' style='overflow: hidden;background-image:url("+tmpres+");background-color: #fbf1cd42;width:"+tmpSetting[4].pvalue*p_scale+"px;height:"+tmpSetting[5].pvalue*p_scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+tpos+"px ;left:"+lpos+"px;border:2px solid rgb(99, 98, 96);'>"+txtHtml+"</div>");
		    }else if(tmpData.type == 'program'){//初始化全局
                        if(tmpData.setting != undefined && tmpData.setting != null){
                            $.extend(p_global_data.Rows,JSON.parse(tmpData.setting).Rows);
                            $("div[tabid='"+tab_id+"']").attr("setting",tmpData.setting);
                            propertyGridObj.set({data:p_global_data});
			    var disBgi = JSON.parse(tmpData.setting).Rows[3].pvalue
                            var tabtabtag = tab_id.replace('tab_','')
                    	    $("[tabtag='"+tabtabtag+"']").css("background-image","url('"+pageCxt+'/'+disBgi+"')")
                        }
                    }

                    //$("#"+tmpData.id).ligerResizable();

                }

            }
        }
    });//end ajax
}
