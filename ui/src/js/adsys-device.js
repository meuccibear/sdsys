function initDeviceTable(){
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
		locale:"zh-CN",
                queryParams: devQueryParams,
    	        queryParamsType: 'limit',
		ajax: devAjaxRequest,
		detailView: true,
		detailFormatter: "devDetailFormatter",
                columns: [{
                    title: '设备编号',
                    field: 'did',
                    align: 'center'
                },{
                    title: '设备名称',
                    field: 'dname',
                    align: 'center'
                },{
                    title: '设备状态',
                    field: 'dstatus',
                    align: 'center',
		    width: '150px',
		    formatter:function(value,row,index){  
    	            	return deviceStatusFormat(row.dstatus);
    	            }
                },{
                    title: '心跳时间',
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
		locale:"zh-CN",
                queryParams: devQueryParams,
    	        queryParamsType: 'limit',
		ajax: devAjaxRequest,
                columns: [{
                    title: '客户名称',
                    field: 'cname',
                    align: 'center'
                },{
                    title: '设备编号',
                    field: 'did',
                    align: 'center',
		    width: '200px',
                },{
                    title: '设备名称',
                    field: 'dname',
                    align: 'center'
                },{
                    title: '设备状态',
                    field: 'dstatus',
                    align: 'center',
		    width: '150px',
		    formatter:function(value,row,index){  
    	            	return deviceStatusFormat(row.dstatus);
    	            }
                },{
                    title: '心跳时间',
                    field: 'beatdate',
                    align: 'center',
		    width: '280px',
		    formatter:function(value,row,index){  
    	            	return timestampFormat(row.beatdate);
    	            }
                },{
	            title: '详情',
	            field: 'id',
	            align: 'center',
	            formatter:function(value,row,index){  
			var html = '';
	                html = html + '<button title="详情" class="btn btn-warning btn-xs" onclick="showDevDetailPage(\'' + row.did + '\');"><i class="fa fa-list-alt"></i></button>';
	                
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
				$("#devExtState").html("设备状态：" + deviceStatusFormat(retJ.dstatus));
				$("#devExtAddr").html("部署地址：" + retJ.daddr);
				$("#devExtid").html("设备编号：" + retJ.did);	
				$("#mac").html("<span>MAC地址 </span>：" + retJ.ext.mac);
				$("#ipaddr").html("<span>网络地址 </span>：" + retJ.ext.ipaddr);
				$("#ipdns").html("<span>网络DNS </span>：" + retJ.ext.ipdns);
				$("#gateway").html("<span>网关地址 </span>：" + retJ.ext.gateway);
				$("#nettype").html("<span>网络类型 </span>：" + networkTypeFormat(retJ.ext.nettype));
				$("#ssid").html("<span>热点名称 </span>：" + retJ.ext.ssid);	
				$("#space").html("<span>存储空间 </span>：" + retJ.ext.space + "MB");	
				$("#volume").html("<span>音量大小 </span>：<input type='range' value='"+ retJ.ext.volume+"' min='0'  max='100' >");	
				$("#disptype").html("<span>显示类型 </span>：" + retJ.ext.disptype);
				$("#brightness").html("<span>亮度大小 </span>：<input type='range' value='"+ retJ.ext.brightness+"' min='0'  max='100' >");
				$("#width").html("<span>显示宽度 </span>：" + retJ.ext.width);
				$("#height").html("<span>显示高度 </span>：" + retJ.ext.height);
				$("#sysvision").html("<span>系统版本 </span>：" + retJ.ext.sysvision);	
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
		return '未知';
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

	var html = '<div class="col-md-12"><div class="col-md-10" style="border-right: 1px solid #e6e6e6;"><div class="col-md-12"><div class="col-md-2"><img src="'+ picpath+'" class="img-polaroid"  width="100%" height="auto"></img></div><div class="col-md-10"><div class="bio-row"><p><span>网络地址 </span>： '+row.ext.ipaddr+'</p></div><div class="bio-row"><p><span>设备分组 </span>： '+checkGroupPath(row.group)+'</p></div><div class="bio-row"><p><span>网络类型 </span>： '+networkTypeFormat(row.ext.nettype)+'</p></div><div class="bio-row"><p><span>正在播放 </span>： '+row.ext.playing+'</p></div><div class="bio-row"><p><span>分辨率 </span>： '+row.ext.width+'*'+row.ext.height+'</p></div><div class="bio-row"><p><span>音量 </span>： '+row.ext.volume+'</p></div></div></div></div><div class="col-md-2"><div class="col-md-12 bio-full"><button type="button" class="btn btn-primary btn-sm  center-block" onclick="showDevDetailPage(\'' + row.did + '\');">详情</button></div><div class="col-md-12 bio-full"><button type="button" class="btn btn-warning btn-sm  center-block" onclick="editDeviceModalPage(\'' + row.did + '\');">编辑</button></div><div class="col-md-12 bio-full"><button type="button" class="btn btn-danger  center-block btn-sm" onclick="delDev(\'' + row.did + '\');">删除</button></div></div></div>';
	return html;
}


function addDevice(formname){
	if($(formname).validate().form()) {
		ajaxSubmit("devices/device", formname,function(result){
			if(result.success)
			{
				bootbox.alert({  
				    	buttons: {  
						ok: {  
							label: '确认',  
							className: 'btn btn-success'  
						}  
					},  
					message: '添加设备成功', 
					title: "添加设备",  
				}); 
				$('.addDeviceModal').modal('hide');
				clearForm($(formname));
				deviceTablerefresh();
			}
			else
			{
				bootbox.alert({  
				    	buttons: {  
						ok: {  
							label: '确认',  
							className: 'btn btn-warning'  
						}  
					},  
					message: result.msg, 
					title: "访问出错",  
				}); 
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
				bootbox.alert({  
				    	buttons: {  
						ok: {  
							label: '确认',  
							className: 'btn btn-success'  
						}  
					},  
					message: '编辑设备成功', 
					title: "编辑设备信息",  
				}); 
				$('.editDeviceModal').modal('hide');
				deviceTablerefresh();
			}
			else
			{
				bootbox.alert({  
				    	buttons: {  
						ok: {  
							label: '确认',  
							className: 'btn btn-warning'  
						}  
					},  
					message: result.msg, 
					title: "访问出错",  
				}); 
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
			bootbox.alert({  
			    	buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-warning'  
					}  
				},  
				message: result.msg, 
				title: "访问出错",  
			}); 
		}
	}, null);
}

function sendShutdownToDevice(did){
	bootbox.confirm("确定要关机吗?", function(result) {
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
					bootbox.alert({  
					    	buttons: {  
							ok: {  
								label: '确认',  
								className: 'btn btn-success'  
							}  
						},  
						message: '发送成功！', 
						title: "发送请求到设备",  
					}); 
				}
				else
				{
					bootbox.alert({  
					    	buttons: {  
							ok: {  
								label: '确认',  
								className: 'btn btn-warning'  
							}  
						},  
						message: result.msg, 
						title: "访问出错",  
					}); 
				}
			}, null);
		}
	});
}

function sendRebootToDevice(did){
	bootbox.confirm("确定要重启吗?", function(result) {
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
					bootbox.alert({  
					    	buttons: {  
							ok: {  
								label: '确认',  
								className: 'btn btn-success'  
							}  
						},  
						message: '发送成功！', 
						title: "发送请求到设备",  
					}); 
				}
				else
				{
					bootbox.alert({  
					    	buttons: {  
							ok: {  
								label: '确认',  
								className: 'btn btn-warning'  
							}  
						},  
						message: result.msg, 
						title: "访问出错",  
					}); 
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
			$("#pforliveTisModal").html("定时开关机设置中...");
			$("#liveTisModalLabel").html("定时设置");
			$('.liveTisModal').modal('show');
			mLiveTimer = window.setInterval(function(){
				getDevTimePrefList(did);
				clearInterval(mLiveTimer);
				$('.liveTisModal').modal('hide');
			}, 1000);
		}
		else
		{
			bootbox.alert({  
				buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-warning'  
					}  
				},  
				message: result.msg, 
				title: "访问出错",  
			}); 
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
	bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
		if (result) {
			ajaxDel("devices/device/" + uuid,null,function(result){
				deviceTablerefresh();
			},
			function(result){
				var retJ = JSON.parse(result.responseText);
				bootbox.alert({  
					buttons: {  
						ok: {  
							label: '确认',  
							className: 'btn btn-warning'  
						}  
					},  
					message: retJ.msg, 
					title: "访问出错",  
        			});  
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
				$("#pforliveTisModal").html("摄像头启动中...");
				$("#liveTisModalLabel").html("直播控制");
			}else{
				$("#pforliveTisModal").html("摄像头关闭中...");
				$("#liveTisModalLabel").html("直播控制");
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
			bootbox.alert({  
			    	buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-warning'  
					}  
				},  
				message: result.msg, 
				title: "访问出错",  
			}); 
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
                    title: '星期',
                    field: 'week',
                    align: 'center',
		    width: '100px',
                },{
                    title: '开机时间',
                    field: 'timestart',
                    align: 'center'
    	        },{
                    title: '关机时间',
                    field: 'timeend',
                    align: 'center',
		    width: '100px',
    	        },{
                    title: '操作',
                    field: 'id',
                    align: 'center',
		    formatter:function(value,row,index){  
			var html = '';
                    	html = html + '<button type="button" title="删除" class="btn btn-danger btn-xs btn-table" onclick="delTimePref(\'' + row.did + '\',\'' + row.id + '\');"><i class="fa fa-trash-o"></i></button>';
	                
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
			bootbox.alert({  
			    	buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-warning'  
					}  
				},  
				message: result.msg, 
				title: "访问出错",  
			}); 
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
			$("#pforliveTisModal").html("定时开关机删除中...");
			$("#liveTisModalLabel").html("定时设置");
			$('.liveTisModal').modal('show');
			mLiveTimer = window.setInterval(function(){
				getDevTimePrefList(did);
				clearInterval(mLiveTimer);
				$('.liveTisModal').modal('hide');
			}, 1000);
		}
		else
		{
			bootbox.alert({  
				buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-warning'  
					}  
				},  
				message: result.msg, 
				title: "访问出错",  
			}); 
		}
	}, null);
}


function initProgramCouterTable(){
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
		locale:"zh-CN",
                queryParams: devCouterQueryParams,
    	        queryParamsType: 'limit',
		ajax: devCouterAjaxRequest,
		detailView: false,
                columns: [{
                    title: '节目名称',
                    field: 'name',
                    align: 'center'
                },{
                    title: '播放次数',
                    field: 'couter',
                    align: 'center'
                },{
                    title: '更新时间',
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
		} 
	}, null);
}

function refreshCouterTable(){
	$('#device-p-couter-table').bootstrapTable('refresh');
}

function showlocationMap(){
	layer.open({
	  type: 2,
	  title: '设备位置',
	  shadeClose: true,
	  shade: 0.8,
	  area: ['90%', '90%'],
	  content: 'deviceLocationMap.html' //iframe的url
	}); 
}
