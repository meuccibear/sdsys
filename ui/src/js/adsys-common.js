$(function(){ 
	var p = window.document.location.href;
	if (p.match("/admin/")){
		if (getType()=="appuser")
			window.location.href='../login.html'; 
	}
	$("#adsys_name").html(getName());
	$("#adsys_name_l").html(getName());
});

function clearForm(form) {
	$(':input', form).each(function () {
        	var type = this.type;
		var tag = this.tagName.toLowerCase(); // normalize case
        	if (type == 'text' || type == 'password' || tag == 'textarea' || type == 'hidden')
			this.value = "";
        	else if (tag == 'select')
			this.selectedIndex = -1;
	});
};

function deviceStatusFormat(status){
	switch(status){
		case 'online':
			return "在线";
		break;
		case 'offline':
			return "离线";
		break;
		default:
			return status;
	}
}

function releaseStatusFormat(status){
	switch(status){
		case 'new':
			return "待审核";
		break;
		case 'reviewed':
			return "已审核";
		break;
		default:
			return status;
	}
}

function releaseTypeStatusFormat(status){
	switch(status){
		case 2:
			return "定时插播";
		break;
		default:
			return "顺序播放";
	}
}

function templateTypeFormat(type){
	switch(type){
		case 'private':
			return "自定义模板";
		break;
		case 'share':
			return "公共模板";
		break;
		default:
			return type;
	}
}

function releaseDevStatusFormat(status){
	switch(status){
		case 'pushing':
			return "推送中";
		break;
		case 'pushed':
		case 'older':
			return "已发布";
		break;
		case 'fail':
			return "失败";
		break;
		case 'deleted':
			return "已删除";
		break;
		case 'playing':
			return "播放中";
		break;
		default:
			return status;
	}
}

function releaseItemStatusFormat(status){
	switch(status){
		case 'push':
			return "传输中";
		break;
		case 'finished':
			return "完成";
		break;
		case 'fail':
			return "失败";
		break;
		default:
			return status;
	}
}

function userTypeFormat(usertype){
	switch(usertype){
		case 'admin':
			return "管理员";
		break;
		case 'appuser':
			return "客户";
		break;
		default:
			return usertype;
	}
}

function networkTypeFormat(nettype){
	switch(nettype){
		case '0':
			return '有线网络';
		break;
		case '1':
			return 'WIFI';
		break;
		case '2':
			return '移动网络';
		break;
		default:
			return '未知';
	}
}

function openNewWindow(url,name){  
	var fulls = "left=0,screenX=0,top=0,screenY=0,scrollbars=1";
	if (window.screen) {  
     		var ah = screen.availHeight - 30;  
     		var aw = screen.availWidth - 10;  
     		fulls += ",height=" + ah;  
     		fulls += ",innerHeight=" + ah;  
     		fulls += ",width=" + aw;  
     		fulls += ",innerWidth=" + aw;  
     		fulls += ",resizable"  
	} else {  
     		fulls += ",resizable";
 	}  
  	window.open(url,name,fulls);  
}  

function openPlayerWindow(did,name){  
	layer.open({
	  type: 2,
	  title: name,
	  shadeClose: true,
	  shade: 0.8,
	  area: ['665px', '420px'],
	  content: './liveplayer.html?did=' + did
	});   
}

function saveToken(type,token){
	if(window.localStorage){
		window.localStorage.setItem("ad_sys_ty",type);
		window.localStorage.setItem("ad_sys_tk",token);
	}
}

function getToken(){
	if(window.localStorage){
		var tk = window.localStorage.getItem("ad_sys_tk");
		if (tk == null)
			return '';
		return tk;
	}
	return '';
}

function getType(){
	if(window.localStorage){
		var ty = window.localStorage.getItem("ad_sys_ty");
		if (ty == null)
			return '';
		return ty;
	}
	return '';
}

function saveName(name){
	if(window.localStorage){
		window.localStorage.setItem("ad_name",name);
	}
}

function getName(){
	if(window.localStorage){
		var name = window.localStorage.getItem("ad_name");
		if (name == null)
			return '未知';
		return name;
	}
	return '未知';
}

function appLogout(){
	if(window.localStorage){
		window.localStorage.removeItem("ad_sys_ty");
		window.localStorage.removeItem("ad_sys_tk");
	}
	window.location.href='login.html'; 
}

function getRootUrl(){
	return "http://127.0.0.1:8080/ADSYS/";
	//return "/apis/ADSYS/";
}

function openEditor(){
	window.open('http://127.0.0.1:8080/ADSYS/ad/editor/?tk='+getToken(),"节目编辑器",'top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no');
}

function sysLogout(){
	if(window.localStorage){
		window.localStorage.removeItem("ad_sys_ty");
		window.localStorage.removeItem("ad_sys_tk");
	}
	window.location.href='../admin/login.html'; 
}

function getFormJson(frm){
	var o={};
  	var a=$(frm).serializeArray();
  	$.each(a,function(){
		if(this.name !== "editorValue"){
	   		if(o[this.name]!==undefined){
	    			if(!o[this.name].push){
	     				o[this.name]=[o[this.name]];
	    			}
	    			o[this.name].push(this.value || '');
	   		}else{
	    			o[this.name]=this.value || '';
	   		}
		}
  	});
  	return o;
 }

function accessErrorHandle(result){
	if (result.status == 401 || result.status == 403)
	{
		var curPath=window.document.location.href;
		if (curPath.match("/admin/") == null)
			window.location.href='login.html'; 
		else
			window.location.href='../admin/login.html';
	}else
		alert("访问服务器出错");	
}

function ajaxSubmit(url,frm,fns,fne){
	var dataPara=getFormJson(frm);
	var token = getToken();
	var feh = accessErrorHandle;
	if (fne != null)
		feh = fne;
	jQuery.support.cors = true;
	$.ajax({
  		url:getRootUrl() + url,
   		type:"post",
		headers:{'X-Access-Token':token},
   		data:dataPara,
   		async:false,
   		dataType:'json',
   		success:fns,
		error:feh
  	});
 }

function ajaxSubmitByData(url,dataPara,fns,fne){
	var token = getToken();
	var feh = accessErrorHandle;
	if (fne != null)
		feh = fne;
	jQuery.support.cors = true;
	$.ajax({
  		url:getRootUrl() + url,
   		type:"post",
		headers:{'X-Access-Token':token},
   		data:dataPara,
   		async:false,
   		dataType:'json',
   		success:fns,
		error:feh
  	});
}

function ajaxGet(url,dataPara,fns,fne){
	var token = getToken();
	var feh = accessErrorHandle;
	if (fne != null)
		feh = fne;
	jQuery.support.cors = true;
	$.ajax({
  		url:getRootUrl() + url,
   		type:"get",
		headers:{'X-Access-Token':token},
   		data:dataPara,
   		async:false,
   		dataType:'json',
   		success:fns,
		error:feh
  	});
 }

function ajaxDel(url,dataPara,fns,fne){
	var token = getToken();
	var feh = accessErrorHandle;
	if (fne != null)
		feh = fne;
	jQuery.support.cors = true;
	$.ajax({
  		url:getRootUrl() + url,
   		type:"delete",
		headers:{'X-Access-Token':token},
   		data:dataPara,
   		async:false,
   		dataType:'json',
   		success:fns,
		error:feh
  	});
 }

function getRequests(){
	var url=location.search;
	var Request = new Object();
	if(url.indexOf("?")!=-1)
	{
		var str = url.substr(1)
		strs = str.split("&");
		for(var i=0;i<strs.length;i++)
		{
			Request[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
		}
	}
	return Request;
}

Date.prototype.format = function(format) {
	var o = {
	       "M+": this.getMonth() + 1,
	       // month
	       "d+": this.getDate(),
	       // day
	       "h+": this.getHours(),
	       // hour
	       "m+": this.getMinutes(),
	       // minute
	       "s+": this.getSeconds(),
	       // second
	       "q+": Math.floor((this.getMonth() + 3) / 3),
	       // quarter
	       "S": this.getMilliseconds()
	       // millisecond
	   };
	   if (/(y+)/.test(format) || /(Y+)/.test(format)) {
	       format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	   }
	   for (var k in o) {
	       if (new RegExp("(" + k + ")").test(format)) {
	           format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	       }
	}
	return format;
};

function timestampFormat(timestamp)
{
	return (new Date(parseFloat(timestamp))).format("yyyy-MM-dd hh:mm:ss");
}
