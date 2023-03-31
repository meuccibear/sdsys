<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html> 
	<head> 
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
		<title></title>
		<link href="../../lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/zTree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
	    <link rel="stylesheet" type="text/css" href="../../lib/webuploader/webuploader.css" />
	    <link rel="stylesheet" type="text/css" href="../../lib/webuploader/style.css" />
	    <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
	    <script src="../../lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	    <script src="../../lib/comm.js" type="text/javascript"></script>
	    <script src="../../lib/jquery-plugin/jquery.i18n.properties-min.js"></script>
            <script src="../../lib/language.js"></script>
	    <script src="../../lib/webuploader/webuploader.js?_v_=20180926"></script>
		<script src="../../lib/webuploader/upload.js?_v_=20180926"></script>
		
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
            //$("#folderForm").ligerForm();
        });

        function save(dialog){
        	var resName = $("#resName").val();
    	    if($.trim(resName) == ''){
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
			<div id="uploader">
				<div class="queueList">
					<div id="dndArea" class="placeholder">
						<div id="filePicker"></div>
							<p>或将照片拖到这里，单次最多可选300张</p>
						</div>
					</div>
					<div class="statusBar" style="display:none;">
						<div class="progress">
							<span class="text">0%</span>
							<span class="percentage"></span>
						</div><div class="info"></div>
						<div class="btns">
							<div id="filePicker2"></div><div class="uploadBtn"><lable data-locale="startuploadTitle">开始上传</lable></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body> 
	<script>
		/*jQuery(document).ready(function() {
			console.log(WebUploader.Uploader.options.formData.pid);
			WebUploader.Uploader.options.formData.pid = "${pid}";
		});*/
		function getPid(){
			return "${pid}";
		}
	</script>
</html> 
