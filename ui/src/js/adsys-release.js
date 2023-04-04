function initReleaseTable(){
	$('#release-table').bootstrapTable({
                method: 'get',
                striped: true,
                cache: false,
                pagination: true,
                //sortable: true,
		sortName: "id",
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
                queryParams: relQueryParams,
    	        queryParamsType: 'limit',
		ajax: relAjaxRequest,
		detailView: true,
		detailFormatter: "relDetailFormatter",
                columns: [{
                    title: '设备名称',
                    field: 'dname',
                    align: 'center'
                },{
                    title: '设备状态',
                    field: 'dstatus',
                    align: 'center',
		    formatter:function(value,row,index){
    	            	return deviceStatusFormat(row.dstatus);
    	            }
                }],
                pagination:true
	});
	$('#release-table').bootstrapTable("expandAllRows");
	$('#release-table').on('page-change.bs.table', function (e, number, size) {
        	$('#release-table').bootstrapTable("expandAllRows");
        })
}

function initReleasePushItemTable(){
	$('#release-push-item-table').bootstrapTable({
                striped: true,
                cache: false,
                pagination: true,
                sortable: false,
                sortOrder: "asc",
                pageNumber:1,
                pageSize: 10,
                sidePagination: "client",
                strictSearch: true,
                minimumCountColumns: 2,
                clickToSelect: true,
                searchOnEnterKey: true,
                detailView: false,
                toolbar: '#toolbar',
		locale:"zh-CN",
    	        queryParamsType: 'limit',
		detailView: false,
                columns: [{
                    title: '资源名称',
                    field: 'fname',
                    align: 'center'
                },{
                    title: '资源状态',
                    field: 'state',
                    align: 'center',
		    formatter:function(value,row,index){
    	            	return releaseItemStatusFormat(row.state);
    	            }
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

function releaseTablerefresh(){
	$('#release-table').bootstrapTable('refresh');
	$('#release-table').bootstrapTable("expandAllRows");
}


function relAjaxRequest(params) {
	ajaxGet("release/programs",params.data,function(result){
		if(result.success)
		{
			params.success({
				total: result.data.total,
				rows: result.data.release_items
			});
		}
	}, null);
}


function relQueryParams(params) {
	var params_t = {};
	params_t['offset'] = params.offset;
	params_t['rn'] = new Date().getTime();
	$('#searchRelForm').find('input[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	$('#searchRelForm').find('select[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	return params_t;
}

function showDevDetailPage(did){
		window.location.href="./releases_ext.html?did=" + did;
}

function relDetailFormatter(index, row) {
	var tds = "";

	if (row.plist == "")
		return "<p>本设备未发现符合条件的节目！</p>";
	else{
		for(var td = 0; td < row.plist.length; td++){
			var shcstr = "";
			if (row.plist[td].ptype == 2){
				shcstr += " (";
				shcstr += row.plist[td].schedule.replace(";", "-");
				shcstr += ")";
			}
			tds += '<tr><td width="30%" align="center">'+row.plist[td].pid+'</td><td width="30%" align="center">'+row.plist[td].pname+shcstr+'</td><td width="10%" align="center">'+releaseTypeStatusFormat(row.plist[td].ptype)+'</td><td width="10%" align="center">'+releaseDevStatusFormat(row.plist[td].state)+'</td><td width="20%" align="center"><button type="button" title="资源列表" class="btn btn-primary btn-xs btn-table" onclick="pushProgramItem(\'' + row.plist[td].did + '\',\'' + row.plist[td].sid + '\');"><i class="fa fa-list"></i></button>  <button type="button" title="删除" class="btn btn-danger btn-xs btn-table" onclick="delReleaseItem(\'' + row.plist[td].sid + '\',\'' + row.plist[td].did + '\');"><i class="fa fa-trash-o"></i></button></td></tr>';
		}
	}

	var html = '<div class="col-md-12"><div class="col-md-12" style="border-right: 1px solid #e6e6e6;"><div class="col-md-12"><table class="table table-info mb30"><thead><tr><th align="center">Hash</th><th align="center">节目名称</th><th align="center">节目类型</th><th align="center">发布状态</th><th align="center">操作</th></tr></thead><tbody>'+tds+'</tbody></table></div></div></div>';
	return html;
}


function showAddReleaseModal(){
	if(window.initEle){
		window.initEle()
	}
	// jQuery('#starttime').timepicker({showMeridian: false});
	// jQuery('#endtime').timepicker({showMeridian: false});
	initProgramSelect($("#pid"));
	initDevicesSelect($("#mutidid"));
	$('.addReleaseModal').modal('show');

}

function addRelease(formname){
	$("#did").val($("#mutidid").val());
	if($(formname).validate().form()) {
		ajaxSubmit("release/program", formname,function(result){
			if(result.success)
			{
				bootbox.alert({
				    	buttons: {
						ok: {
							label: '确认',
							className: 'btn btn-success'
						}
					},
					message: '提交节目单成功',
					title: "发布节目单",
				});
				$('.addReleaseModal').modal('hide');
				$("#timeset_plan").hide();
				clearForm($(formname));
				$("#mutidid").select2("val", "");
				releaseTablerefresh();
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


function addReleasev2(formname){
	$("#did").val($("#mutidid").multipleSelect('getSelects'));
	// $("#did").val($("#mutidid").val());
	if($(formname).validate().form()) {
		ajaxSubmit("release/program", formname,function(result){
			if(result.success)
			{
				window['dialog'] = bootbox.dialog({
					message: '发送成功',
					closeButton: false
					// title: "发布",
				});
				setTimeout(function () {
					window['dialog'].modal('hide');
				}, 1000)
				// $('.addReleaseModal').modal('hide');
				// $("#timeset_plan").hide();
				// clearForm($(formname));
				// $("#mutidid").select2("val", "");
				// releaseTablerefresh();
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

function reviewProgram(sid){
	var params_t = {};
	params_t['oper'] = "reviewed";
	ajaxSubmitByData("release/program/review/"+sid, params_t,function(result){
		if(result.success)
		{
			bootbox.alert({
			    	buttons: {
					ok: {
						label: '确认',
						className: 'btn btn-success'
					}
				},
				message: '节目单审核成功！',
				title: "节目单审核",
			});
			releaseTablerefresh();
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

function pushProgram(sid){
	var params_t = {};
	params_t['oper'] = "push";
	ajaxSubmitByData("release/program/push/"+sid, params_t,function(result){
		if(result.success)
		{
			bootbox.alert({
			    	buttons: {
					ok: {
						label: '确认',
						className: 'btn btn-success'
					}
				},
				message: '节目单重发布成功！',
				title: "节目单发布",
			});
			releaseTablerefresh();
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

var mTimer;
function pushProgramItem(did,sid){
	$('.releasePushItemModal').modal('show');
	getPushProgramItem(did,sid);
	mTimer = window.setInterval(function(){
		getPushProgramItem(did,sid);
	}, 5000);

}

function closeReleaseModal(){
	if (mTimer != null)
		clearInterval(mTimer);
	$('.releasePushItemModal').modal('hide');
}

function getPushProgramItem(did, sid){
	ajaxGet("release/programs/item?did="+did+"&sid="+sid, null,function(result){
			if(result.success)
			{
				$('#release-push-item-table').bootstrapTable('load', result.data);
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
function delRelease(uuid) {
	bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
		if (result) {
			ajaxDel("release/program/" + uuid,null,function(result){
				if (result.success){
					releaseTablerefresh();
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

function delReleaseItem(sid, did) {
	bootbox.confirm("确定要删除[" + did + "]吗?", function(result) {
		if (result) {
			ajaxDel("release/program/item/" + sid + "/" + did,null,function(result){
				if (result.success){
					releaseTablerefresh();
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

