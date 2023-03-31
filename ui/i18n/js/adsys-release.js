function initReleaseTable(){
	var language = getCookie('language');
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
		locale:language =='zh'?'zh-CN':'en-US',
                queryParams: relQueryParams,
    	        queryParamsType: 'limit',
		ajax: relAjaxRequest,
		detailView: true,
		detailFormatter: "relDetailFormatter",
                columns: [{
                    title: $.i18n.prop('dnameTitle'),
                    field: 'dname',
                    align: 'center'
                },{
                    title: $.i18n.prop('dstatusTitle'),
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
	var language = getCookie('language');
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
		locale:language =='zh'?'zh-CN':'en-US',
    	        queryParamsType: 'limit',
		detailView: false,
                columns: [{
                    title: $.i18n.prop('fnameTitle'),
                    field: 'fname',
                    align: 'center'
                },{
                    title: $.i18n.prop('fstateTitle'),
                    field: 'state',
                    align: 'center',
		    formatter:function(value,row,index){  
    	            	return releaseItemStatusFormat(row.state);
    	            }
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
		}else{
			if (result.status == 404)
				alert($.i18n.prop('noAccessMsg'));
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
		return "<p>"+$.i18n.prop('noprogramMsg')+"</p>";
	else{
		for(var td = 0; td < row.plist.length; td++){
			var shcstr = "";
			if (row.plist[td].ptype == 2){
				shcstr += " (";
				shcstr += row.plist[td].schedule.replace(";", "-");
				shcstr += ")";
			}
			tds += '<tr><td width="40%" align="center">'+row.plist[td].pname+shcstr+'</td><td width="20%" align="center">'+releaseTypeStatusFormat(row.plist[td].ptype)+'</td><td width="20%" align="center">'+releaseDevStatusFormat(row.plist[td].state)+'</td><td width="20%" align="center"><button type="button" title="'+$.i18n.prop('flistTitle')+'" class="btn btn-primary btn-xs btn-table" onclick="pushProgramItem(\'' + row.plist[td].did + '\',\'' + row.plist[td].sid + '\');"><i class="fa fa-list"></i></button>  <button type="button" title="'+$.i18n.prop('delTitle')+'" class="btn btn-danger btn-xs btn-table" onclick="delReleaseItem(\'' + row.plist[td].sid + '\',\'' + row.plist[td].did + '\');"><i class="fa fa-trash-o"></i></button></td></tr>';
		}
	}

	var html = '<div class="col-md-12"><div class="col-md-12" style="border-right: 1px solid #e6e6e6;"><div class="col-md-12"><table class="table table-info mb30"><thead><tr><th align="center">'+$.i18n.prop('pnameTitle')+'</th><th align="center">'+$.i18n.prop('ptypeTitle')+'</th><th align="center">'+$.i18n.prop('pushstateTitle')+'</th><th align="center">'+$.i18n.prop('operationTile')+'</th></tr></thead><tbody>'+tds+'</tbody></table></div></div></div>';
	return html;
}


function showAddReleaseModal(){
	jQuery('#starttime').timepicker({showMeridian: false});
	jQuery('#endtime').timepicker({showMeridian: false});
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
				OKDialog($.i18n.prop('pushTitle'),$.i18n.prop('pushMsg'));
				$('.addReleaseModal').modal('hide');
				$("#timeset_plan").hide();
				clearForm($(formname));
				$("#mutidid").select2("val", ""); 
				releaseTablerefresh();
			}
			else
			{
				FailDialog($.i18n.prop('failTitle'),result.msg);
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
			OKDialog($.i18n.prop('pushreviewTitle'),$.i18n.prop('pushreviewMsg'));
			releaseTablerefresh();
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);
}

function pushProgram(sid){
	var params_t = {};
	params_t['oper'] = "push";
	ajaxSubmitByData("release/program/push/"+sid, params_t,function(result){
		if(result.success)
		{
			OKDialog($.i18n.prop('repushTitle'),$.i18n.prop('repushMsg'));
			releaseTablerefresh();
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
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
				FailDialog($.i18n.prop('failTitle'),result.msg);
			}
	}, null);
}
function delRelease(uuid) {
	bootbox.confirm($.i18n.prop('deltipMsg') + "[" + uuid + "]", function(result) {
		if (result) {
			ajaxDel("release/program/" + uuid,null,function(result){
				if (result.success){
					releaseTablerefresh();
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
				
			},
			function(result){
				var retJ = JSON.parse(result.responseText);
				FailDialog($.i18n.prop('failTitle'),retJ.msg);
			});
		}
	});
}

function delReleaseItem(sid, did) {
	bootbox.confirm($.i18n.prop('deltipMsg') + "[" + did + "]", function(result) {
		if (result) {
			ajaxDel("release/program/item/" + sid + "/" + did,null,function(result){
				if (result.success){
					releaseTablerefresh();
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			},
			function(result){
				var retJ = JSON.parse(result.responseText);
				FailDialog($.i18n.prop('failTitle'),retJ.msg);
			});
		}
	});
}

