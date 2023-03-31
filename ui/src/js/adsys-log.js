function initLogTable(){
        $('#log-table').bootstrapTable({
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
                queryParams: logQueryParams,  
    	        queryParamsType: 'limit',
		ajax: ajaxLogRequest,
		detailView: false,
                columns: [{
                    title: '日志类型',
                    field: 'ltype',
                    align: 'center',
		    width: '100px',
                },{
                    title: '日志内容',
                    field: 'laction',
                    align: 'center'
    	        },{
                    title: '操作人',
                    field: 'luser',
                    align: 'center',
		    width: '100px',
    	        },{
                    title: '操作时间',
                    field: 'adddate',
                    align: 'center',
		    width: '280px',
                }],
                pagination:true
      });
}

function ajaxLogRequest(params) {
	ajaxGet("loger/log", params.data, function(result){
		if(result.success)
		{
			params.success({
				total: result.data.total,
				rows: result.data.log_items
			});  
		} 
	}, null);

}

function logQueryParams(params) { 
	var params_t = {};
	params_t['offset'] = params.offset;
	$('#logSearchForm').find('input[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	$('#logSearchForm').find('select[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	return params_t;
}

function refreshLogTable(){
	$('#log-table').bootstrapTable('refresh');
}
