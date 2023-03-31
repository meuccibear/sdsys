<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html> 
	<head> 
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
		<title></title>
		<link href="../../lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/zTree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
	    <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
	    <script src="../../lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	    <script src="../../lib/zTree/jquery.ztree.all-3.4.min.js" type="text/javascript"></script>
	    <script src="../../lib/jquery-validation/jquery.validate.min.js" type="text/javascript"></script>
	    <script src="../../lib/jquery-validation/messages_cn.js" type="text/javascript"></script>
	    <script type="text/javascript" src="../../lib/treeview/bootstrap-treeview.min.js" ></script>
	    <script src="../../lib/jquery-plugin/jquery.i18n.properties-min.js"></script>
            <script src="../../lib/language.js"></script>
	    <script src="../../lib/comm.js" type="text/javascript"></script>
		<style>
		body,td,select,input{font-family: "微软雅黑", "宋体", Arial, sans-serif; font-size: 12px; }
		.td1{
			background:#F7F7FF;
			text-align:right;
			height:22px;
			text-align:right;
			border:1px solid #C4D8ED;
			padding-right: 4px;
		} 
		.td2{background:#FFFFFF;
		     height:30px;
			 text-align:left;
			 border:1px solid #C4D8ED;
			 padding-left:2px;
		}
		</style>
    <script type="text/javascript">
	
	    
        $(function ()
        {
		var lang = getCookie('language');
		if(lang=="En"){
	        	EnloadProperties();
		}else{
	        	ZhloadProperties();
		} 
		$(".radio0").click(function(){
			$("#device_plane").show();
			$("#group_plane").hide();
		});
		$(".radio1").click(function(){
			$("#device_plane").hide();
			$("#group_plane").show();
		});
        	loadDevices();
		loadGroupTree();
        });


	function loadDevices(){
        	$.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/devices/list",
		headers:{'X-Access-Token':getToken()},
                data: null,
                dataType: "json",
                success: function(data){
       				if(data.data != null){
					var ddata = data.data;
					var pdata = '[';
       					for (var i = 0; i < ddata.length; i++) {
	    					if(i === ddata.length - 1){
	    						pdata += '{id:"'+ddata[i].did+'",text:"'+ddata[i].dname+'"}';
	    					}else{
	    						pdata += '{id:"'+ddata[i].did+'",text:"'+ddata[i].dname+'"},';
	    					}
    					}
					pdata += ']';
			    		//将字符串转换成对象作为下拉菜单参数
			    		var rst = eval('('+ pdata +')');
			    		//用ligerui制作下拉菜单
			    		$("#mutidid").ligerComboBox({
			    			isShowCheckBox: true,
						isMultiSelect: true,
						width : 250,
						height: 25,
						data:rst
			    		});
       				}      				                       
                }
            });
        }
        
	function loadGroupTree() {
       		$.ajax({
		    	type: "GET",
		    	url: "${pageContext.request.contextPath}/groups/tree",
			headers:{'X-Access-Token':getToken()},
		    	dataType: "json",
		    	success: function (result) {
				var treeObj = JSON.parse(result.data.tree);
		        	$('#grouptree').treeview({
				    	data: treeObj,         // 数据源
				    	multiSelect: false,    //多选
				    	onNodeChecked: function (event,data) {
				       		treeviewSelectHandler(data);
				    	},
				    	onNodeSelected: function (event, data) {
				        	treeviewunSelectHandler(data);
				    	}
		        	});
		    	},
		    	error: function () {
		        	alert("设备分组加载失败！")
		    	}
        	});
	}

	function treeviewSelectHandler(data){
		$("#gid").val(data.id);
	}

	function treeviewunSelectHandler(data){
		$("#gid").val(0);
	}

	function save(dialog){
		$("#did").val($("#mutidid_val").val().replace(/;/g, ","));
		if($("#programSent").validate().form()) {
			var param = $("#programSent").serialize();
           		$.ajax({
				type : "POST",
				url : "${pageContext.request.contextPath}/release/program",
				headers:{'X-Access-Token':getToken()},
				data: param,
				dataType : "json",
				success:function(obj){
					if(obj.success){
						alert($.i18n.prop('publishsuccMsg'));
						dialog.close();
					} else {
						alert(obj.msg);
					}
				},error:function(){
//						$.ligerDialog.error('system error');
				}
			});
		}

	}
    </script>
	
	</head> 
	<body>
		<body>
		<div align="center">
			<form id="programSent" name="programSent" method="post">
				<table width="90%" border="0" cellpadding="0" style="margin-top:10px ;" class="tabg">
					<tr>
						<td align="right" class="td1"><lable data-locale="programnameTitle">节目名称</lable>:</td>
						<td align="left">
							<input type="text" id="pname" name="pname" value=${pname} class="required" style="width:245px;height:20px;" disabled="true"/>
							<input type="hidden" name="pid" id="pid" value="${id}" />
						</td>
					</tr>
					<tr>
						<td align="right" class="td1"><lable data-locale="playtypeTitle">播放类型</lable>:</td>
						<td align="left">
							<lable data-locale="releaseReplace">
							<select id="ptype" name="ptype" class="required" style="width:250px;height:25px;">
								<option value="0">替换播放</option>
								<option value="1">追加播放</option>
							</select>
							</lable>
						</td>
					</tr>
					<tr>
						<td align="right" class="td1"><lable data-locale="targetTitle">发布目标</lable>:</td>
						<td align="left">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<lable data-locale="deviceTitle">设备</lable><input type="radio" name="radioTarget" id="radioTarget" value="1" checked="true" class="radio0" style="height:20px;"/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<lable data-locale="groupTitle">分组</lable><input type="radio" name="radioTarget" id="radioTarget" value="2" class="radio1" style="height:20px;"/>
						</td>
					</tr>
					<tr id="device_plane">
						<td align="right" class="td1" ><lable data-locale="devicelistTitle">设备列表</lable>:</td>
						<td align="left">
							<input type="text" id="mutidid"/>
							<input type="hidden" class="form-control" id="did" name="did"/>
						</td>
					</tr>
					<tr id="group_plane" style="display: none;" >
						<td align="right" class="td1" ><lable data-locale="grouplistTitle">分组列表</lable>:</td>
						<td align="left">
							<div id="grouptree">
								
							</div>
							<input type="hidden" class="form-control" id="gid" name="gid"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body> 
</html> 
