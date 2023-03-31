/**
 * 界面设计器的资源列表脚本
 */
var treeManager;
var zTree;
var rMenu;
var pMenu; 

function loadResources(){
	  $("#removeTreeItem").hide();
	  $.ajax({
           type: "GET",
           url: pageCxt+"/adEditor/resources/list",
	   headers:{'X-Access-Token':getToken()},
           data: null,
           dataType: "json",
           success: function(data){
				if(data.data != null){
					var items = data.data;
					var treeData = [];
					for(var i=0;i<items.length;i++){
						var tmp = items[i];
						var item = {};
						item.id = tmp.id;
						item.pId = tmp.pid;
						item.name = tmp.resName;
						item.isParent = tmp.resType == 0 ? true : false;
						item.fileType = tmp.fileType;
						item.resPath = tmp.resPath;
						treeData.push(item);
					}
					zTree = $.fn.zTree.init($("#resTree"), setting, treeData);
					var rootNode = zTree.getNodeByParam("id", 0, null);
					zTree.expandNode(rootNode);
				}
					
				                       
           }
       });
	}

function dropTrees(e, treeId, treeNodes, targetNode, moveType){
	for(var i = 0; i < treeNodes.length; i++){
	var domId = "dom_" + treeNodes[i].getParentNode().id;//treeNode 节点的父节点 JSON 数据对象,如果 treeNode 是根节点，返回 null 。
	if (moveType == null ) {
		var zTree = $.fn.zTree.getZTreeObj("resTree");//zTree 对象，提供操作 zTree 的各种方法，对于通过 js 操作 zTree 来说必须通过此对象
		var nodes = zTree.getSelectedNodes();
		var selNode = nodes[i];
		if(selNode.isParent){
			return;
		}
			var update = 0;
			//暂时只支持图片和视频
			var typeTag = $(e.target).attr('typeTag');
			//拖动替换背景图
			var typeBgi =  $(e.target).attr('name');
			if(typeBgi == 'adDesiger' && selNode.fileType== 'pic'){
					var newBgi = nodes[0].resPath
					var tab_id = designerTab.getSelectedTabItemID();
					var tabtabtag = tab_id.replace('tab_','')
					$("[tabtag='"+tabtabtag+"']").css("background-image","url('"+pageCxt+'/'+newBgi+"')")
					p_global_data.Rows[3].pvalue = '/'+newBgi;
			}else if(typeBgi == 'adDesiger' && selNode.fileType != 'pic'){
					alert($.i18n.prop('bgtipMsg'))
			}
			//不是约定的组件不能拖放
			if($(e.target).attr("class") != 'domBtn'){
				if(selNode.fileType == 'video' && $(e.target).parent().attr('typeTag') == 'video'){
					update = 1;
					typeTag = $(e.target).parent().attr('typeTag');
					if($(e.target).parent().attr("class") != 'domBtn'){
						return;
					}
				}else{
					return;
				}
				
			}
			if(selNode.fileType == 'pic' && typeTag == 'pic'){//图片
				$(e.target).css('background-image','url('+pageCxt+'/'+selNode.resPath+')');
				$(e.target).css('background-size','100% 100%');
				$(e.target).css('-moz-background-size','100% 100%');
				var curRes = $(e.target).attr("resref");
				if(curRes != null && curRes != ''){
					curRes += ";"
				}else{
					curRes = '';
				}
				$(e.target).attr("resref",curRes+pageCxt+'/'+selNode.resPath);
				//新增组件资源树节点
				var pNode = prozTree.getNodeByParam("id", $(e.target).attr("id"), null);
				pNode.isParent = true;
				prozTree.updateNode(pNode);
				var newNode = {};
				newNode.id = guid();
				newNode.pId = $(e.target).attr("id");
				newNode.name = selNode.resPath.substring(selNode.resPath.lastIndexOf("/")+1);
				newNode.res = pageCxt+"/"+selNode.resPath;
				newNode.isParent = false;
				newNode.isContent = 1;
				prozTree.addNodes(pNode,newNode);
				
			}else if(selNode.fileType == 'video' && typeTag == 'video'){//视频
				//取消自动播放和循环播放，在预览的时候才播放就ok了
				var videoObj = $("<video src='"+pageCxt+'/'+selNode.resPath+"' width='"+$(e.target).css("width")+"' height='"+$(e.target).css("height")+"'></video>");
				var refObj = null;
				if(update == 1){
					refObj = $(e.target).parent();
					$(e.target).parent().html(videoObj);
				}else{
					refObj = $(e.target);
					$(e.target).html(videoObj);
				}
				var curRes = refObj.attr("resref");
				if(curRes != null && curRes != ''){
					curRes += ";"
				}else{
					curRes = '';
				}
				refObj.attr("resref",curRes+pageCxt+'/'+selNode.resPath);
				//新增组件资源树节点
				var pNode = prozTree.getNodeByParam("id", refObj.attr("id"), null);
				pNode.isParent = true;
				prozTree.updateNode(pNode);
				var newNode = {};
				newNode.id = guid();
				newNode.pId = refObj.attr("id");
				newNode.name = selNode.resPath.substring(selNode.resPath.lastIndexOf("/")+1);
				newNode.res = pageCxt+"/"+selNode.resPath;
				newNode.isParent = false;
				newNode.isContent = 1;
				prozTree.addNodes(pNode,newNode);
			}else if(selNode.fileType == 'music' && typeTag == 'music'){//音频
				//取消自动播放和循环播放，在预览的时候才播放就ok了
				var audioObj = $("<audio src='"+pageCxt+'/'+selNode.resPath+"' width='"+$(e.target).css("width")+"' height='"+$(e.target).css("height")+"' controls='controls'></audio>");
				var refObj = null;
				if(update == 1){
					refObj = $(e.target).parent();
					$(e.target).parent().html(audioObj);
				}else{
					refObj = $(e.target);
					$(e.target).html(audioObj);
				}
				var curRes = refObj.attr("resref");
				if(curRes != null && curRes != ''){
					curRes += ";"
				}else{
					curRes = '';
				}
				refObj.attr("resref",curRes+pageCxt+'/'+selNode.resPath);
				//新增组件资源树节点
				var pNode = prozTree.getNodeByParam("id", refObj.attr("id"), null);
				pNode.isParent = true;
				prozTree.updateNode(pNode);
				var newNode = {};
				newNode.id = guid();
				newNode.pId = refObj.attr("id");
				newNode.name = selNode.resPath.substring(selNode.resPath.lastIndexOf("/")+1);
				newNode.res = pageCxt+"/"+selNode.resPath;
				newNode.isParent = false;
				newNode.isContent = 1;
				prozTree.addNodes(pNode,newNode);
			}

		} else if ( $(e.target).parents(".domBtnDiv").length > 0) {
			alert(MoveTest.errorMsg);
		}
	}
}	

var MoveTest = {
		errorMsg: $.i18n.prop('dragtipMsg'),
		curTarget: null,
		curTmpTarget: null,
		noSel: function() {
			try {
				window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
			} catch(e){}
		},
		dragTree2Dom: function(treeId, treeNodes) {
			return !treeNodes[0].isParent;
		},
		prevTree: function(treeId, treeNodes, targetNode) {
			return !targetNode.isParent && targetNode.parentTId == treeNodes[0].parentTId;
		},
		nextTree: function(treeId, treeNodes, targetNode) {
			return !targetNode.isParent && targetNode.parentTId == treeNodes[0].parentTId;
		},
		innerTree: function(treeId, treeNodes, targetNode) {
			return targetNode!=null && targetNode.isParent && targetNode.tId == treeNodes[0].parentTId;
		},
		dropTree2Dom: function(e, treeId, treeNodes, targetNode, moveType) {
			var domId = "dom_" + treeNodes[0].getParentNode().id;
			if (moveType == null ) {
				var zTree = $.fn.zTree.getZTreeObj("resTree");
				var nodes = zTree.getSelectedNodes();
				var selNode = nodes[0];
				if(selNode.isParent){
					return;
				}
				var update = 0;
				//暂时只支持图片和视频
				var typeTag = $(e.target).attr('typeTag');
				//拖动替换背景图
				var typeBgi =  $(e.target).attr('name');
				if(typeBgi == 'adDesiger' && selNode.fileType== 'pic'){
						var newBgi = nodes[0].resPath
						var tab_id = designerTab.getSelectedTabItemID();
						var tabtabtag = tab_id.replace('tab_','')
						$("[tabtag='"+tabtabtag+"']").css("background-image","url('"+pageCxt+'/'+newBgi+"')")
						p_global_data.Rows[3].pvalue = '/'+newBgi;
				}else if(typeBgi == 'adDesiger' && selNode.fileType != 'pic'){
						alert($.i18n.prop('bgtipMsg'))
				}
				//不是约定的组件不能拖放
				if($(e.target).attr("class") != 'domBtn'){
					if(selNode.fileType == 'video' && $(e.target).parent().attr('typeTag') == 'video'){
						update = 1;
						typeTag = $(e.target).parent().attr('typeTag');
						if($(e.target).parent().attr("class") != 'domBtn'){
							return;
						}
					}else{
						return;
					}
					
				}
				if(selNode.fileType == 'pic' && typeTag == 'pic'){//图片
					$(e.target).css('background-image','url('+pageCxt+'/'+selNode.resPath+')');
					$(e.target).css('background-size','100% 100%');
					$(e.target).css('-moz-background-size','100% 100%');
					var curRes = $(e.target).attr("resref");
					if(curRes != null && curRes != ''){
						curRes += ";"
					}else{
						curRes = '';
					}
					$(e.target).attr("resref",curRes+pageCxt+'/'+selNode.resPath);
					//新增组件资源树节点
					var pNode = prozTree.getNodeByParam("id", $(e.target).attr("id"), null);
					pNode.isParent = true;
					prozTree.updateNode(pNode);
					var newNode = {};
					newNode.id = guid();
					newNode.pId = $(e.target).attr("id");
					newNode.name = selNode.resPath.substring(selNode.resPath.lastIndexOf("/")+1);
					newNode.res = pageCxt+"/"+selNode.resPath;
					newNode.isParent = false;
					newNode.isContent = 1;
					prozTree.addNodes(pNode,newNode);
					
				}else if(selNode.fileType == 'video' && typeTag == 'video'){//视频
					//取消自动播放和循环播放，在预览的时候才播放就ok了
					var videoObj = $("<video src='"+pageCxt+'/'+selNode.resPath+"' width='"+$(e.target).css("width")+"' height='"+$(e.target).css("height")+"'></video>");
					var refObj = null;
					if(update == 1){
						refObj = $(e.target).parent();
						$(e.target).parent().html(videoObj);
					}else{
						refObj = $(e.target);
						$(e.target).html(videoObj);
					}
					var curRes = refObj.attr("resref");
					if(curRes != null && curRes != ''){
						curRes += ";"
					}else{
						curRes = '';
					}
					refObj.attr("resref",curRes+pageCxt+'/'+selNode.resPath);
					//新增组件资源树节点
					var pNode = prozTree.getNodeByParam("id", refObj.attr("id"), null);
					pNode.isParent = true;
					prozTree.updateNode(pNode);
					var newNode = {};
					newNode.id = guid();
					newNode.pId = refObj.attr("id");
					newNode.name = selNode.resPath.substring(selNode.resPath.lastIndexOf("/")+1);
					newNode.res = pageCxt+"/"+selNode.resPath;
					newNode.isParent = false;
					newNode.isContent = 1;
					prozTree.addNodes(pNode,newNode);
				}else if(selNode.fileType == 'music' && typeTag == 'music'){//音频
					//取消自动播放和循环播放，在预览的时候才播放就ok了
					var audioObj = $("<audio src='"+pageCxt+'/'+selNode.resPath+"' width='"+$(e.target).css("width")+"' height='"+$(e.target).css("height")+"' controls='controls'></audio>");
					var refObj = null;
					if(update == 1){
						refObj = $(e.target).parent();
						$(e.target).parent().html(audioObj);
					}else{
						refObj = $(e.target);
						$(e.target).html(audioObj);
					}
					var curRes = refObj.attr("resref");
					if(curRes != null && curRes != ''){
						curRes += ";"
					}else{
						curRes = '';
					}
					refObj.attr("resref",curRes+pageCxt+'/'+selNode.resPath);
					//新增组件资源树节点
					var pNode = prozTree.getNodeByParam("id", refObj.attr("id"), null);
					pNode.isParent = true;
					prozTree.updateNode(pNode);
					var newNode = {};
					newNode.id = guid();
					newNode.pId = refObj.attr("id");
					newNode.name = selNode.resPath.substring(selNode.resPath.lastIndexOf("/")+1);
					newNode.res = pageCxt+"/"+selNode.resPath;
					newNode.isParent = false;
					newNode.isContent = 1;
					prozTree.addNodes(pNode,newNode);
				}

			} else if ( $(e.target).parents(".domBtnDiv").length > 0) {
				alert(MoveTest.errorMsg);
			}
		}
	};

var setting = {
		edit: {
			enable: true,
			showRemoveBtn: false,
			showRenameBtn: false,
			drag: {
				prev: MoveTest.prevTree,
				next: MoveTest.nextTree,
				inner: MoveTest.innerTree
			}
		},
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
//			beforeDrag: MoveTest.dragTree2Dom,
			onClick : selectResourceTab,
			onDrop: dropTrees,
			onRightClick: OnRightClick
		},
		view: {
			selectedMulti: true,
			dblClickExpand: false
		}
};

//单击资源树
function selectResourceTab(event, treeId, treeNode){
	$("#removeTreeItem").show();
	if(treeNode.fileType != undefined){
		var type = treeNode.fileType;
		//预览图片或视频 preImg
		if(type == 'pic'){
			$("#preImg").css('background-image','url('+'/ADSYS/'+treeNode.resPath+')');
			$("#preDiv").show();
			$("#preVideoDiv").hide();
			$("#preAudioDiv").hide();
		}else if(type == 'video'){
			$("#preVideo").attr('src','/ADSYS/'+treeNode.resPath);
			$("#preVideoDiv").show();
			$("#preDiv").hide();
			$("#preAudioDiv").hide();
		}else if(type == 'music'){
			$("#preAudio").attr('src','/ADSYS/'+treeNode.resPath);
			$("#preAudioDiv").show();
			$("#preVideoDiv").hide();
			$("#preDiv").hide();
		}
		
	}else{
		$("#preDiv").hide();
		$("#preVideoDiv").hide();
	}
}

//右键菜单事件
function OnRightClick(event, treeId, treeNode) {
	if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
		zTree.cancelSelectedNode();
		showRMenu("root", event.clientX, event.clientY);
	} else if (treeNode && !treeNode.noR) {
		zTree.selectNode(treeNode);
		showRMenu("node", event.clientX, event.clientY);
	}
}

function showRMenu(type, x, y) {
	$("#rMenu ul").show();
	if (type=="root") {
		$("#m_del").hide();
		$("#m_check").hide();
		$("#m_unCheck").hide();
	} else {
		$("#m_del").show();
		$("#m_check").show();
		$("#m_unCheck").show();
	}

    y += document.body.scrollTop;
    x += document.body.scrollLeft;
    rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

	$("body").bind("mousedown", onBodyMouseDown);
}
function hideRMenu() {
	if (rMenu) rMenu.css({"visibility": "hidden"});
	$("body").unbind("mousedown", onBodyMouseDown);
}
function onBodyMouseDown(event){
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		rMenu.css({"visibility" : "hidden"});
	}
}

//新增文件夹
function addFolder() {
	hideRMenu();
	var nodes = zTree.getSelectedNodes();
	var param = "pid="+nodes[0].id+"&pResName="+nodes[0].name;
	actDialog = $.ligerDialog.open({
		url:pageCxt+'/ad/editor/editFolder?'+param,
		width:300,height:150,isResize:true,
		title:$.i18n.prop('addfolderTitle'),
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

//重命名文件夹
function renameFolder(){
	hideRMenu();
	alert("暂不支持重命名");
	return;
	var nodes = zTree.getSelectedNodes();
	if(nodes[0].children && nodes[0].children.length > 0){
		alert("该文件夹下已有节点，暂不支持重命名");
		return;
	}
	
}

//删除文件夹
function deleteFolder() {
	hideRMenu();
	var nodes = zTree.getSelectedNodes();
	if (nodes[0].children && nodes[0].children.length>0) {
		alert($.i18n.prop('delrestipMsg'));
		return; 
	}
	$.ajax({
		type : "POST",
		url : pageCxt+"/adEditor/resources/forlder/delete?id="+nodes[0].id,
		headers:{'X-Access-Token':getToken()},
		data: null,
		dataType : "json",
		success:function(obj){
			if(obj.success){
				alert(obj.msg);
				parent.loadResources();
				dialog.close();
			} else {
				alert(obj.msg);
			}
		},error:function(){
//				$.ligerDialog.error('system error');
		}
	});
}

//刷新资源树
function resetTree() {
	hideRMenu();
	loadResources();
}

//上传素材
function addResource() {
	hideRMenu();
	var nodes = zTree.getSelectedNodes();
	if(!nodes[0].isParent){//文件
		alert($.i18n.prop('uploadfiletipMsg'));
		return;
	}
	var param = "pid="+nodes[0].id;
	actDialog = $.ligerDialog.open({
		url:pageCxt+'/ad/editor/upload?'+param,
		width:550,height:550,isResize:true,
		title:$.i18n.prop('uploadfileTitle'),
		allowClose:false,
		buttons : [
			{
				text : $.i18n.prop('finishedTitle'),onclick : function(item,dialog){
					resetTree();
					dialog.close();
				}
			}
		]
	});
}

//批量删除
function removeTreeItem(){
	var nodes = zTree.getSelectedNodes();
	for(var i = 0; i < nodes.length; i++){
		if(nodes[i].name ==='根目录'){
			alert($.i18n.prop('delroottipMsg'))
			return;
		}
		if (nodes[i].children && nodes[i].children.length>0) {
			alert($.i18n.prop('delrestipMsg'));
			return; 
		}
	}
	for(var i = 0; i < nodes.length; i++){
		$.ajax({
			type : "POST",
			url : pageCxt+"/adEditor/resources/forlder/delete?id="+nodes[i].id,
			headers:{'X-Access-Token':getToken()},
			data: null,
			dataType : "json",
			success:function(obj){
				if(obj.success){
					parent.loadResources();
				} else {
					alert(obj.msg);
				}
			},error:function(){
//					$.ligerDialog.error('system error');
			}
		});
	}
	alert($.i18n.prop('delsuccessMsg'))
}
