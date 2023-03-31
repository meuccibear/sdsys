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
	
	    
    	var v ;
        $(function ()
        {
	    var lang = getCookie('language');
	    if(lang=="En"){
	        EnloadProperties();
	    }else{
	        ZhloadProperties();
	    } 
            $("#folderForm").ligerForm();
        });

        function save(dialog){
        	var resName = $("#resName").val();
    	    if($.trim(resName) == ''){
    	    	alert($.i18n.prop('inputfoldernameMsg'));
    	    	return;
        	}
        	
           var param = $("#folderForm").serialize();
           $.ajax({
				type : "POST",
				url : "${pageContext.request.contextPath}/adEditor/resources/forlder/add",
				headers:{'X-Access-Token':getToken()},
				data: param,
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
//						$.ligerDialog.error('system error');
				}
			});
        }
    </script>
	
	</head> 
	<body>
		<div align="center">
			<form id="folderForm" name="folderForm" method="post">
				<input type="hidden" name="pid" id="pid" value="${pid}"/>
				<input type="hidden" name="id" id="id" value="${id}"/>

				<table width="90%" border="0" cellpadding="0" cellspacing="0" style="margin-top: 10px;" class="tabg">
					<tr>
						<td align="right" class="td1"><lable data-locale="parenfolderTitle">上级目录</lable>：</td>
						<td align="left">${pResName}</td>
					</tr>
					<tr>
						<td align="right" class="td1"><lable data-locale="foldernameTitle">文件夹名称</lable>：</td>
						<td align="left">
						     <input type="text" id="resName" name="resName"/>
						</td>  
					</tr>
				</table>
			</form>
		</div>
	</body> 
</html> 
