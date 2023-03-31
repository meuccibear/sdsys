function initLogTable(){
	var language = getCookie('language');
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
		locale:language =='zh'?'zh-CN':'en-US',
                queryParams: logQueryParams,  
    	        queryParamsType: 'limit',
		ajax: ajaxLogRequest,
		detailView: false,
                columns: [{
                    title: $.i18n.prop('ltypeTitle'),
                    field: 'ltype',
                    align: 'center',
		    width: '100px',
                },{
                    title: $.i18n.prop('lactionTitle'),
                    field: 'laction',
                    align: 'center'
    	        },{
                    title: $.i18n.prop('luserTitle'),
                    field: 'luser',
                    align: 'center',
		    width: '100px',
    	        },{
                    title: $.i18n.prop('adddateTitle'),
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
		}else{
			if (result.status == 404)
				alert($.i18n.prop('noAccessMsg'));
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
