function initDeviceTable(){
	var language = getCookie('language');
	$('#device-table').bootstrapTable({
                method: 'get',
                striped: true, 
                cache: false, 
                pagination: true, 
                sortable: false, 
                sortOrder: "asc",
                pageNumber:1,
                pageSize: 10,
                sidePagination: "server",
                strictSearch: true,
                minimumCountColumns: 2, 
                clickToSelect: true, 
                searchOnEnterKey: true,
                detailView: false,
                toolbar: '#toolbar',
		locale:language =='zh'?'zh-CN':'en-US',
                queryParams: devQueryParams,
    	        queryParamsType: 'limit',
		ajax: devAjaxRequest,
		detailView: true,
		detailFormatter: "devDetailFormatter",
                columns: [{
                    title: $.i18n.prop('didTitle'),
                    field: 'did',
                    align: 'center'
                },{
                    title: $.i18n.prop('dnameTitle'),
                    field: 'dname',
                    align: 'center'
                },{
                    title: $.i18n.prop('dstatusTitle'),
                    field: 'dstatus',
                    align: 'center',
		    width: '150px',
		    formatter:function(value,row,index){  
    	            	return deviceStatusFormat(row.dstatus);
    	            }
                },{
                    title: $.i18n.prop('beatdateTitle'),
                    field: 'beatdate',
                    align: 'center',
		    width: '280px',
		    formatter:function(value,row,index){  
    	            	return timestampFormat(row.beatdate);
    	            }
                }],
                pagination:true
	});
	$('#device-table').bootstrapTable("expandAllRows");
	$('#device-table').on('page-change.bs.table', function (e, number, size) {
        	$('#device-table').bootstrapTable("expandAllRows");
        })
}

function initAdminDeviceTable(){
	var language = getCookie('language');
	$('#device-admin-table').bootstrapTable({
                method: 'get',
                striped: true, 
                cache: false, 
                pagination: true, 
                sortable: false, 
                sortOrder: "asc",
                pageNumber:1,
                pageSize: 10,
                sidePagination: "server",
                strictSearch: true,
                minimumCountColumns: 2, 
                clickToSelect: true, 
                searchOnEnterKey: true,
                detailView: false,
                toolbar: '#toolbar',
		locale:language =='zh'?'zh-CN':'en-US',
                queryParams: devQueryParams,
    	        queryParamsType: 'limit',
		ajax: devAjaxRequest,
                columns: [{
                    title: $.i18n.prop('cnameTitle'),
                    field: 'cname',
                    align: 'center'
                },{
                    title: $.i18n.prop('didTitle'),
                    field: 'did',
                    align: 'center',
		    width: '200px',
                },{
                    title: $.i18n.prop('dnameTitle'),
                    field: 'dname',
                    align: 'center'
                },{
                    title: $.i18n.prop('dstatusTitle'),
                    field: 'dstatus',
                    align: 'center',
		    width: '150px',
		    formatter:function(value,row,index){  
    	            	return deviceStatusFormat(row.dstatus);
    	            }
                },{
                    title: $.i18n.prop('beatdateTitle'),
                    field: 'beatdate',
                    align: 'center',
		    width: '280px',
		    formatter:function(value,row,index){  
    	            	return timestampFormat(row.beatdate);
    	            }
                },{
	            title: $.i18n.prop('moreTitle'),
	            field: 'id',
	            align: 'center',
	            formatter:function(value,row,index){  
			var html = '';
	                html = html + '<button title="'+$.i18n.prop('moreTitle')+'" class="btn btn-warning btn-xs" onclick="showDevDetailPage(\'' + row.did + '\');"><i class="fa fa-list-alt"></i></button>';
	                
	                return html;  
		    }
	        }],
                pagination:true
	});
}

function adminDeviceTablerefresh(){
	$('#device-admin-table').bootstrapTable('refresh');
}

function initDevicesSelect(selectId){
	ajaxGet("devices/list",null,function(result){
		if (result.success)
		{
			var retJ = result.data;
			if (retJ)
			{
				var sel_options = '';
				for(var item in retJ)
				{
					sel_options += '<option value="'+retJ[item].did+'">'+retJ[item].dname+'</option>';
				}
				selectId.html(sel_options);
			}
		}else{
			if (result.status == 404)
				alert($.i18n.prop('noAccessMsg'));
		}	
	}, null);
}

function deviceTablerefresh(){
	$('#device-table').bootstrapTable('refresh');
	$('#device-table').bootstrapTable("expandAllRows");
}

function devAjaxRequest(params) {
	ajaxGet("devices/device",params.data,function(result){
		if(result.success)
		{
			params.success({
				total: result.data.total,
				rows: result.data.device_items
			});  
		}else{
			if (result.status == 404)
				alert($.i18n.prop('noAccessMsg'));
		} 
	}, null);
}

function getDevDetailed(did){
	ajaxGet("devices/device/" + did,null,function(result){
		if (result.success)
		{
			var retJ = result.data;
			if (retJ)
			{
				$("#devExtname").html(retJ.dname);		
				$("#devExtState").html($.i18n.prop('dstatusTitle')+'："' + deviceStatusFormat(retJ.dstatus));
				$("#devExtAddr").html($.i18n.prop('addrTitle')+'：' + retJ.daddr);
				$("#devExtid").html($.i18n.prop('didTitle')+'：' + retJ.did);	
				$("#mac").html('<span>'+$.i18n.prop('macTitle')+' </span>：' + retJ.ext.mac);
				$("#ipaddr").html('<span>'+$.i18n.prop('ipaddrTitle')+' </span>：' + retJ.ext.ipaddr);
				$("#ipdns").html('<span>'+$.i18n.prop('ipdnsTitle')+' </span>：' + retJ.ext.ipdns);
				$("#gateway").html('<span>'+$.i18n.prop('gatewayTitle')+' </span>：' + retJ.ext.gateway);
				$("#nettype").html('<span>'+$.i18n.prop('nettypeTitle')+' </span>：' + networkTypeFormat(retJ.ext.nettype));
				$("#ssid").html('<span>'+$.i18n.prop('ssidTitle')+' </span>：' + retJ.ext.ssid);	
				$("#space").html('<span>'+$.i18n.prop('spaceTitle')+' </span>：' + retJ.ext.space + "MB");	
				$("#volume").html('<span>'+$.i18n.prop('volumeTitle')+' </span>：<input type="range" value="'+ retJ.ext.volume+'" min="0"  max="100" >');	
				$("#disptype").html('<span>'+$.i18n.prop('disptypeTitle')+' </span>：' + retJ.ext.disptype);
				$("#brightness").html('<span>'+$.i18n.prop('brightnessTitle')+' </span>：<input type="range" value="'+ retJ.ext.brightness+'" min="0"  max="100" >');
				$("#width").html('<span>'+$.i18n.prop('widthTitle')+' </span>：' + retJ.ext.width);
				$("#height").html('<span>'+$.i18n.prop('heightTitle')+' </span>：' + retJ.ext.height);
				$("#sysvision").html('<span>'+$.i18n.prop('sysvisionTitle')+' </span>：' + retJ.ext.sysvision);	
				if (retJ.dlive == "start"){
					$("#btnstartlive").hide();
					$("#btnstoplive").show();
					$("#btnplaylive").show();
				}
				else
				{
					$("#btnstartlive").show();
					$("#btnstoplive").hide();
					$("#btnplaylive").hide();
				}
			}
		}	
	}, null);
}


function devQueryParams(params) {
	var params_t = {};
	var Request = getRequests();
	var cid = Request["cid"];
	if (cid != undefined){
		params_t['cid'] = cid;
	}
	params_t['offset'] = params.offset;
	params_t['rn'] = new Date().getTime();
	$('#searchDevForm').find('input[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	$('#searchDevForm').find('select[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	
	return params_t;
}


function showDevDetailPage(did){
		window.location.href="./devices_ext.html?did=" + did;
}

function checkGroupPath(group){
	if (group == undefined || group == null)
		return 'unkown';
	else if (group.gpath == "/"){
		return "/" + group.gname;
	}else{
		return group.gpath.replace("//", "/")+'/'+group.gname;
	}
}
	
function devDetailFormatter(index, row) {
	var picpath = "";
	if (row.picpath == undefined || row.picpath == "")
		picpath = "images/device.png";
	else
		picpath = getRootUrl() + row.picpath;

	var html = '<div class="col-md-12"><div class="col-md-10" style="border-right: 1px solid #e6e6e6;"><div class="col-md-12"><div class="col-md-2"><img src="'+ picpath+'" class="img-polaroid"  width="100%" height="auto"></img></div><div class="col-md-10"><div class="bio-row"><p><span>'+$.i18n.prop('ipaddrTitle')+' </span>： '+row.ext.ipaddr+'</p></div><div class="bio-row"><p><span>'+$.i18n.prop('devgroupTitle')+' </span>： '+checkGroupPath(row.group)+'</p></div><div class="bio-row"><p><span>'+$.i18n.prop('nettypeTitle')+' </span>： '+networkTypeFormat(row.ext.nettype)+'</p></div><div class="bio-row"><p><span>'+$.i18n.prop('playingTitle')+' </span>： '+row.ext.playing+'</p></div><div class="bio-row"><p><span>'+$.i18n.prop('resolutionTitle')+' </span>： '+row.ext.width+'*'+row.ext.height+'</p></div><div class="bio-row"><p><span>'+$.i18n.prop('volumeTitle')+' </span>： '+row.ext.volume+'</p></div></div></div></div><div class="col-md-2"><div class="col-md-12 bio-full"><button type="button" class="btn btn-primary btn-sm  center-block" onclick="showDevDetailPage(\'' + row.did + '\');">'+$.i18n.prop('moreTitle')+'</button></div><div class="col-md-12 bio-full"><button type="button" class="btn btn-warning btn-sm  center-block" onclick="editDeviceModalPage(\'' + row.did + '\');">'+$.i18n.prop('editTitle')+'</button></div><div class="col-md-12 bio-full"><button type="button" class="btn btn-danger  center-block btn-sm" onclick="delDev(\'' + row.did + '\');">'+$.i18n.prop('delTitle')+'</button></div></div></div>';
	return html;
}


function addDevice(formname){
	if($(formname).validate().form()) {
		ajaxSubmit("devices/device", formname,function(result){
			if(result.success)
			{
				OKDialog($.i18n.prop('adddevTitle'),$.i18n.prop('adddevMsg'));
				$('.addDeviceModal').modal('hide');
				clearForm($(formname));
				deviceTablerefresh();
			}
			else
			{
				FailDialog($.i18n.prop('failTitle'),result.msg);
			}
		}, null);
	}
}

function editDevice(formname){
	var did = $("#editDevModalForm").contents().find("#edid").val();
	if($(formname).validate().form()) {
		ajaxSubmit("devices/device/" + did, formname,function(result){
			if(result.success)
			{
				OKDialog($.i18n.prop('editdevTitle'),$.i18n.prop('editdevMsg'));
				$('.editDeviceModal').modal('hide');
				deviceTablerefresh();
			}
			else
			{
				FailDialog($.i18n.prop('failTitle'),result.msg);
			}
		}, null);
	}
}

function editDeviceModalPage(did){
	ajaxGet("devices/device/" + did,null,function(result){
			if (result.success)
			{
				var retJ = result.data;
				if (retJ)
				{
					for(var item in retJ)
					{
						$("#editDevModalForm").contents().find("#"+item).val(retJ[item]);
						if (item == "group"){
							var node = $('#grouptree_edit').treeview('search',[ retJ[item].gname , {
							  ignoreCase: true,     // case insensitive
							  exactMatch: false,    // like or equals
							  revealResults: true,  // reveal matching nodes
							}]);
							$('#grouptree_edit').treeview('selectNode', [ node[0].nodeId, { silent: true } ]);
						}
					}
					$("#editDevModalForm").contents().find("#edid").val(did);
					$('.editDeviceModal').modal('show');
				}
			}	
	}, null);
}

function sendMsgToDevice(did, cmd, data){
	var params_t = {};
	params_t['command'] = cmd;
	params_t['data'] = data;
	ajaxSubmitByData("devices/notify/"+did, params_t,function(result){
		if(!result.success)
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);
}

function sendShutdownToDevice(did){
	bootbox.confirm($.i18n.prop('shutdowntipMsg'), function(result) {
		if (result) {
			if (did == undefined){
				var Request = getRequests();
				did = Request["did"];
			}
			var params_t = {};
			params_t['command'] = "shutdown";
			params_t['data'] = "down";
			ajaxSubmitByData("devices/notify/"+did, params_t,function(result){
				if(result.success)
				{
					OKDialog($.i18n.prop('senddevTitle'),$.i18n.prop('senddevMsg'));
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
		}
	});
}

function sendRebootToDevice(did){
	bootbox.confirm($.i18n.prop('reboottipMsg'), function(result) {
		if (result) {
			if (did == undefined){
				var Request = getRequests();
				did = Request["did"];
			}
			var params_t = {};
			params_t['command'] = "reboot";
			params_t['data'] = "down";
			ajaxSubmitByData("devices/notify/"+did, params_t,function(result){
				if(result.success)
				{
					OKDialog($.i18n.prop('senddevTitle'),$.i18n.prop('senddevMsg'));
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
		}
	});
}

var mLiveTimer;
function sendTimePrefToDevice(did,data){
	var params_t = {};
	params_t['command'] = "settimepref";
	params_t['data'] = data;
	ajaxSubmitByData("devices/notify/"+did, params_t,function(result){
		if(result.success)
		{
			$("#pforliveTisModal").html($.i18n.prop('timersetMsg'));
			$("#liveTisModalLabel").html($.i18n.prop('timersetTitle'));
			$('.liveTisModal').modal('show');
			mLiveTimer = window.setInterval(function(){
				getDevTimePrefList(did);
				clearInterval(mLiveTimer);
				$('.liveTisModal').modal('hide');
			}, 1000);
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);
}

function setTimePref(){
	var Request = getRequests();
	did = Request["did"];
	var tpweek = $("#timeprefweek").val();
	var tps = $("#timeprefstart").val();
	var tpe = $("#timeprefend").val();
	var timepref_obj = {"week":tpweek, "timestart":tps, "timeend":tpe };
	sendTimePrefToDevice(did, JSON.stringify(timepref_obj));
}

function delDev(uuid) {
	bootbox.confirm($.i18n.prop('deltipMsg') + "[" + uuid + "]", function(result) {
		if (result) {
			ajaxDel("devices/device/" + uuid,null,function(result){
				deviceTablerefresh();
			},
			function(result){
				var retJ = JSON.parse(result.responseText);
				FailDialog($.i18n.prop('failTitle'),retJ.msg);
			});
		}
	});
}


function setLiveOnDevice(did, src, action){
	var params_t = {};
	params_t['src'] = src;
	params_t['action'] = action;
	ajaxSubmitByData("devices/live/"+did, params_t,function(result){
		if(result.success)
		{
			if (action == "start"){
				$("#pforliveTisModal").html($.i18n.prop('openliveMsg'));
				$("#liveTisModalLabel").html($.i18n.prop('livestreamTitle'));
			}else{
				$("#pforliveTisModal").html($.i18n.prop('closeliveMsg'));
				$("#liveTisModalLabel").html($.i18n.prop('livestreamTitle'));
			}
			$('.liveTisModal').modal('show');
			mLiveTimer = window.setInterval(function(){
				getDevDetailed(did);
				clearInterval(mLiveTimer);
				$('.liveTisModal').modal('hide');
			}, 4000);
			
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);
}

function timePrefTable(){
        $('#timepref-table').bootstrapTable({
                striped: true, 
                cache: false, 
                pagination: true, 
                sortable: false, 
                sortOrder: "asc",
                pageNumber:1,
                pageSize: 50,
                sidePagination: "client",
                strictSearch: true,
                minimumCountColumns: 2,
                clickToSelect: true, 
                searchOnEnterKey: true,
                detailView: false,
                toolbar: '#toolbar', 
    	        queryParamsType: 'limit',
		detailView: false,
                columns: [{
                    title: $.i18n.prop('weekTitle'),
                    field: 'week',
                    align: 'center',
		    width: '100px',
                },{
                    title: $.i18n.prop('timestartTitle'),
                    field: 'timestart',
                    align: 'center'
    	        },{
                    title: $.i18n.prop('timeendTitle'),
                    field: 'timeend',
                    align: 'center',
		    width: '100px',
    	        },{
                    title: $.i18n.prop('operationTile'),
                    field: 'id',
                    align: 'center',
		    formatter:function(value,row,index){  
			var html = '';
                    	html = html + '<button type="button" title="'+$.i18n.prop('delTtile')+'" class="btn btn-danger btn-xs btn-table" onclick="delTimePref(\'' + row.did + '\',\'' + row.id + '\');"><i class="fa fa-trash-o"></i></button>';
	                
	                return html;  
		    }
                }],
                pagination:true
      });
}

function getDevTimePrefList(did) {
	ajaxGet("devices/timepref/"+did, null, function(result){
		if(result.success)
		{
			$('#timepref-table').bootstrapTable('load', result.data);
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);

}

function delTimePref(did, id) {
	var params_t = {};
	params_t['command'] = "deltimepref";
	params_t['data'] = id;
	ajaxSubmitByData("devices/notify/"+did, params_t,function(result){
		if(result.success)
		{
			$("#pforliveTisModal").html($.i18n.prop('timerdelMsg'));
			$("#liveTisModalLabel").html($.i18n.prop('timersetTitle'));
			$('.liveTisModal').modal('show');
			mLiveTimer = window.setInterval(function(){
				getDevTimePrefList(did);
				clearInterval(mLiveTimer);
				$('.liveTisModal').modal('hide');
			}, 1000);
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);
}


function initProgramCouterTable(){
	var language = getCookie('language');
	$('#device-p-couter-table').bootstrapTable({
                method: 'get',
                striped: true, 
                cache: false, 
                pagination: true, 
                sortable: false, 
                sortOrder: "asc",
                pageNumber:1,
                pageSize: 10,
                sidePagination: "server",
                strictSearch: true,
                minimumCountColumns: 2, 
                clickToSelect: true, 
                searchOnEnterKey: true,
                detailView: false,
                toolbar: '#toolbar',
		locale:language =='zh'?'zh-CN':'en-US',
                queryParams: devCouterQueryParams,
    	        queryParamsType: 'limit',
		ajax: devCouterAjaxRequest,
		detailView: false,
                columns: [{
                    title: $.i18n.prop('pnameTitle'),
                    field: 'name',
                    align: 'center'
                },{
                    title: $.i18n.prop('couterTitle'),
                    field: 'couter',
                    align: 'center'
                },{
                    title: $.i18n.prop('updatedateTitle'),
                    field: 'updatedate',
                    align: 'center',
		    width: '280px',
		    formatter:function(value,row,index){  
    	            	return timestampFormat(row.updatedate);
    	            }
                }],
                pagination:true
	});
}

function devCouterQueryParams(params) {
	var params_t = {};
	var Request = getRequests();
	var did = Request["did"];
	if (did != undefined){
		params_t['did'] = did;
	}
	params_t['offset'] = params.offset;
	params_t['rn'] = new Date().getTime();
	
	return params_t;
}


function devCouterAjaxRequest(params) {
	ajaxGet("devices/program/couter",params.data,function(result){
		if(result.success)
		{
			params.success({
				total: result.data.total,
				rows: result.data.couter_items
			});  
		}else{
			if (result.status == 404)
				alert($.i18n.prop('noAccessMsg'));
		} 
	}, null);
}

function refreshCouterTable(){
	$('#device-p-couter-table').bootstrapTable('refresh');
}

function showlocationMap(){
	layer.open({
	  type: 2,
	  title: $.i18n.prop('locationTitle'),
	  shadeClose: true,
	  shade: 0.8,
	  area: ['90%', '90%'],
	  content: 'deviceLocationMap.html'
	}); 
}
