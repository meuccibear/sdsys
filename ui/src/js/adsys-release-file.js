function initReleaseFileTable(){
	$('#release-file-table').bootstrapTable({
                method: 'get',
                striped: true, 
                cache: false, 
                pagination: true, 
                sortable: false, 
                sortOrder: "asc",
                pageNumber:1,
                pageSize: 10,
                pageList: [10, 20, 50], 
                sidePagination: "server",
                strictSearch: true,
                minimumCountColumns: 2, 
                clickToSelect: true, 
                searchOnEnterKey: true,
                detailView: false,
                toolbar: '#toolbar',
                queryParams: relFileQueryParams,
    	        queryParamsType: 'limit',
		ajax: relFileAjaxRequest,
		detailView: true,
		detailFormatter: "relFileDetailFormatter",
                columns: [{
                    title: '发布编号',
                    field: 'sid',
                    align: 'center'
                },{
                    title: '发布名称',
                    field: 'sname',
                    align: 'center'
                },{
                    title: '发布状态',
                    field: 'sstate',
                    align: 'center',
		    width: '150px',
		    formatter:function(value,row,index){  
    	            	return deviceStatusFormat(row.sstate);
    	            }
                },{
                    title: '请求时间',
                    field: 'adddate',
                    align: 'center',
		    width: '280px',
		    formatter:function(value,row,index){  
    	            	return timestampFormat(row.adddate);
    	            }
                }],
                pagination:true
	});
	$('#release-file-table').bootstrapTable("expandAllRows");
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
                pageList: [10, 20, 50], 
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
                    title: '资源名称',
                    field: 'fname',
                    align: 'center'
                },{
                    title: '资源状态',
                    field: 'state',
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


function releaseFileTablerefresh(){
	$('#release-file-table').bootstrapTable('refresh');
	$('#release-file-table').bootstrapTable("expandAllRows");
}


function relFileAjaxRequest(params) {
	ajaxGet("release/files",params.data,function(result){
		if(result.success)
		{
			params.success({
				total: result.data.total,
				rows: result.data.release_items
			});  
		} 
	}, null);
}


function relFileQueryParams(params) {
	var params_t = {};
	params_t['offset'] = params.offset;
	params_t['rn'] = new Date().getTime();
	$('#searchRelFileForm').find('input[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	$('#searchRelFileForm').find('select[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	return params_t;
}

function showDevDetailPage(did){
		window.location.href="./releases_ext.html?did=" + did;
}
	
function relFileDetailFormatter(index, row) {
	var tds = "";
	var btn = '<button type="button" class="btn btn-primary btn-sm  center-block" onclick="pushFile(\'' + row.sid + '\');">发布</button>';
	
	if (row.sstate == "new")
		btn = '<button type="button" class="btn btn-warning btn-sm  center-block" onclick="reviewPushFile(\'' + row.sid + '\');">审核</button>';

	if (row.devlist == "")
		return "<p>本设备未发布文件！</p>";
	else{
		for(var td = 0; td < row.devlist.length; td++){
			tds += '<tr><td width="60%" align="center">'+row.devlist[td].dname+'</td><td width="20%" align="center">'+row.devlist[td].dstatus+'</td><td width="20%" align="center"><button type="button" title="资源列表" class="btn btn-primary btn-xs btn-table" onclick="pushProgramItem(\'' + row.devlist[td].did + '\',\'' + row.devlist[td].sid + '\');"><i class="fa fa-list"></i></button>  <button type="button" title="删除" class="btn btn-danger btn-xs btn-table" onclick="delPushFile(\'' + row.devlist[td].id + '\');"><i class="fa fa-trash-o"></i></button></td></tr>';
		}
	}

	var html = '<div class="col-md-12"><div class="col-md-10" style="border-right: 1px solid #e6e6e6;"><div class="col-md-12"><table class="table table-info mb30"><thead><tr><th align="center">设备名称</th><th align="center">设备状态</th><th align="center">操作</th></tr></thead><tbody>'+tds+'</tbody></table></div></div><div class="col-md-2"><div class="col-md-12 bio-full">'+btn+'</div><div class="col-md-12 bio-full"><button type="button" class="btn btn-danger  center-block btn-sm" onclick="delRelease(\'' + row.sid + '\');">删除</button></div></div></div>';
	return html;
}



function addReleaseFile(formname){
	$("#did").val($("#mutidid").val());
	ajaxSubmit("release/file", formname,function(result){
		if(result.success)
		{
			bootbox.alert({  
			    	buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-success'  
					}  
				},  
				message: '发布文件成功', 
				title: "发布文件",  
			}); 
			$('.addReleaseFileModal').modal('hide');
			releaseFileTablerefresh();
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

function reviewPushFile(sid){
	var params_t = {};
	params_t['oper'] = "reviewed";
	ajaxSubmitByData("release/file/review/"+sid, params_t,function(result){
		if(result.success)
		{
			bootbox.alert({  
			    	buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-success'  
					}  
				},  
				message: '发布审核成功！', 
				title: "发布审核",  
			}); 
			releaseFileTablerefresh();
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

function pushFile(sid){
	var params_t = {};
	params_t['oper'] = "push";
	ajaxSubmitByData("release/file/push/"+sid, params_t,function(result){
		if(result.success)
		{
			bootbox.alert({  
			    	buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-success'  
					}  
				},  
				message: '文件发布成功！', 
				title: "文件发布",  
			}); 
			releaseFileTablerefresh();
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

function pushProgramItem(did,sid){
	$('.releasePushItemModal').modal('show');
	ajaxGet("release/file/item?did="+did+"&sid="+sid, null,function(result){
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

function delReleaseFile(uuid) {
	bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
		if (result) {
			ajaxDel("release/file/" + uuid,null,function(result){
				releaseFileTablerefresh();
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

