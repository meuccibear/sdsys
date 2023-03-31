<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html> 
	<head> 
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
		<title></title>
		<link href="../../lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/zTree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/select2/select2.min.css?t=3434" rel="stylesheet" type="text/css" />
	    <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
	    <script src="../../lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	    <script src="../../lib/select2/select2.min.js?t=3434" type="text/javascript"></script>
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
	function selecttemplate(state) {
		if (!state.id) {
			return state.text;
		}
		var $state = $('<span><img style="width: 60px;height: 60px;" src="' + state.element.getAttribute("imgPath") + '" class="img-flag"> ' + state.text + '</span>');
		return $state;
	}

        $(function ()
        {
		var lang = getCookie('language');
		if(lang=="En"){
	        	EnloadProperties();
		}else{
	        	ZhloadProperties();
		} 
        	userTemplate();
		$("#dp").change(function(){
			var selval = $(this).val();
			var valsplit = selval.split("x");
			$("#wd").val(valsplit[0]);
			$("#hd").val(valsplit[1]);
		});

		$('#tid').select2({
			templateResult: selecttemplate
       		});
        });

        function save(dialog){
            var name = $("#name").val();
	    var wd = $("#wd").val();
	    var hd = $("#hd").val();
    	    if($.trim(name) == ''){
    	    	alert($.i18n.prop('inputprogramnameMsg'));
    	    	return;
            }
            if($.trim(wd) == ''){
    	    	alert($.i18n.prop('inputwidthMsg'));
    	    	return;
            }
	    if($.trim(hd) == ''){
    	    	alert($.i18n.prop('inputheightMsg'));
    	    	return;
            }
           var param = $("#programForm").serialize();
           var ischeck = $("input[name='useTemplate']").prop('checked');
           if(ischeck == 'checked' || ischeck == true){
        	   ischeck = '1';
           }
           param += "&ischeck="+ischeck;
           $.ajax({
				type : "POST",
				url : "${pageContext.request.contextPath}/adEditor/programs/add",
				headers:{'X-Access-Token':getToken()},
				data: param,
				dataType : "json",
				success:function(obj){
					if(obj.success){
						//alert(obj.msg);
						parent.loadPrograms();
						//todo:打开新的tab
						parent.designerTab.addTabItem({tabid:"tab_"+obj.data.nid,text:obj.data.name});
						var p_treeNodeId = obj.data.nid;
						var p_width = obj.data.width;
						var p_height = obj.data.height;
						var c_width = parent.designerTab.element.clientWidth-60;
						var c_height = parent.designerTab.element.clientHeight-102;
						if (c_width < p_width || c_height < p_height){
							var w_scale = c_width/p_width;
							var h_scale = c_height/p_height;
							if (w_scale < h_scale)
								parent.scale = w_scale;
							else
								parent.scale = h_scale;
						}
						else
							scale = 1;
						var p_scale = parent.scale;
						parent.reloadTab(p_treeNodeId,p_scale,p_width,p_height,JSON.stringify(d_global_data));
						dialog.close();
					} else {
						alert(obj.msg);
					}
				},error:function(){
//						$.ligerDialog.error('system error');
				}
			});
        }
        
        function userTemplate(){
        	$.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/adEditor/programs/listTemplate",
		headers:{'X-Access-Token':getToken()},
                data: null,
                dataType: "json",
                success: function(data){
       				if(data.data != null){
       					var items = data.data;
       					for(var i=0;i<items.length;i++){
						var imgpath = "../../lib/img/template-private.jpg";
						if (items[i].previewImg != "none")
							imgpath = items[i].previewImg;
       						$("#tid").append('<option value="'+items[i].tid+'" imgPath="'+imgpath+'">'+items[i].tempname+'</option>');
       					}
       				}
       					
       				                       
                }
            });
        }
        
        
    </script>
	
	</head> 
	<body>
		<div align="center">
			<form id="programForm" name="programForm" method="post">
<%-- 				<input type="hidden" name="id" id="id" value="${id}"/> --%>

				<table width="90%" border="0" cellpadding="0" cellspacing="0" style="margin-top: 10px;" class="tabg">
					<tr>
						<td align="right" class="td1"><lable data-locale="programnameTitle">节目名称</lable>：</td>
						<td align="left">
							<input type="text" id="name" name="name" style="width:245px;height:20px;"/>
						</td>
					</tr>
					<tr>
						<td align="right" class="td1"><lable data-locale="programresTitle">常用分辨率</lable>：</td>
						<td align="left">
						     <select id="dp" name="dp" style="width:250px;height:25px;">
						     	<option value="680x480">680x480</option>
						     	<option value="800x600">800x600</option>
						     	<option value="1024x768">1024x768</option>
						     	<option value="1280x1024">1280x1024</option>
						     	<option value="1366x768">1366x768</option>
						     	<option value="1920x1080">1920x1080</option>
						     	<option value="480x680">480x680</option>
						     	<option value="600x800">600x800</option>
						     	<option value="768x1024">768x1024</option>
						     	<option value="1024x1280">1024x1280</option>
						     	<option value="768x1366">768x1366</option>
						     	<option value="1080x1920">1080x1920</option>
						     </select>
						</td>  
					</tr>
					<tr>
						<td align="right" class="td1"><lable data-locale="userdefTitle">节目分辨率</lable>：</td>
						<td align="left">
							<input type="text" id="wd" name="wd" value="640" style="width:115px;height:20px;"/> x <input type="text" id="hd" name="hd" value="480" style="width:115px;height:20px;"/>
						</td>
					</tr>
					<tr>
						<td align="right" class="td1"><input type="checkbox" name="useTemplate" value="1"/><lable data-locale="usetempTitle">使用模板</lable>：</td>
						<td align="left">
						     <select id="tid" name="tid" style="width:250px;height:20px;" class="form-control">
						     </select>
						</td> 
					</tr>
				</table>
			</form>
		</div>
		<script>
		</script>
<!-- 		<script src="../../lib/editor/js/editor_ad_list.js?_v_=20180722" type="text/javascript"></script> -->
	    	<script src="../../lib/editor/js/editor_menu.js?_v_=20181017" type="text/javascript"></script>
	</body> 
</html> 
