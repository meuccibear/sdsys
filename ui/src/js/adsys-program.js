function initProgramTable(){
        $('#program-table').bootstrapTable({
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
                queryParams: programQueryParams,  
    	        queryParamsType: 'limit',
		ajax: ajaxProgramRequest,
		detailView: false,
                columns: [{
                    title: '节目名称',
                    field: 'name',
                    align: 'center'
                },{
                    title: '分辨率',
                    field: 'dp',
                    align: 'center'
    	        },{
                    title: '操作',
                    field: 'id',
                    align: 'center',
		    formatter:function(value,row,index){  
			var html = '';
                    	html = html + '<button type="button" title="删除" class="btn btn-danger btn-xs btn-table" onclick="delProgram(\'' + row.id + '\');"><i class="fa fa-trash-o"></i></button>';
	                
	                return html;  
		    }
                }],
                pagination:true
      });
}

function ajaxProgramRequest(params) {
	ajaxGet("adEditor/programs/pagelist", params.data, function(result){
		if(result.success)
		{
			params.success({
				total: result.data.total,
				rows: result.data.program_items
			});  
		} 
	}, null);

}

function initProgramSelect(selectId){
	ajaxGet("adEditor/programs/alllist",null,function(result){
		if (result.success)
		{
			var retJ = result.data;
			if (retJ)
			{
				var sel_options = '';
				for(var item in retJ)
				{
					sel_options += '<option value="'+retJ[item].id+'">'+retJ[item].name+'</option>';
				}
				selectId.html(sel_options);
			}
		}	
	}, null);
}

function delProgram(uuid) {
	bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
		if (result) {
			ajaxSubmitByData("adEditor/programs/delete","id=" + uuid,function(result){
				if(result.success)
				{
					refreshProgramTable();
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
			},null);
		}
	});
}


function programQueryParams(params) { 
	var params_t = {};
	params_t['offset'] = params.offset;
	$('#programSearchForm').find('input[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	$('#programSearchForm').find('select[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	return params_t;
}

function refreshProgramTable(){
	$('#program-table').bootstrapTable('refresh');
}




///////////////////////////list by did

function initDeviceProgramTable(){
        $('#program-dev-table').bootstrapTable({
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
                    title: '节目单名称',
                    field: 'sname',
                    align: 'center'
                },{
                    title: '内容列表',
                    field: 'schedule',
                    align: 'center',
		    formatter:function(value,row,index){  
    	            	return '<button type="button" title="查看列表" class="btn btn-primary btn-xs btn-table" onclick="pushProgramItem(\'' + row.did + '\',\'' + row.sid + '\');"><i class="fa fa-list"></i> 查看列表</button>';
    	            }
    	        },{
                    title: '节目状态',
                    field: 'state',
                    align: 'center'
    	        },{
                    title: '发布时间',
                    field: 'adddate',
                    align: 'center',
		    width: '280px',
		    formatter:function(value,row,index){  
    	            	return timestampFormat(row.adddate);
    	            }
                }],
                pagination:true
      });
}

function getDevProgramList(did) {
	ajaxGet("/release/programs/dev/"+did, null, function(result){
		if(result.success)
		{
			$('#program-dev-table').bootstrapTable('load', result.data);
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

