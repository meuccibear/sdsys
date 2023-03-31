function initTemplateTable() {
	var language = getCookie('language');
	$('#template-table').bootstrapTable({
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
                queryParams: TemplateQueryParams,
    	        queryParamsType: 'limit',
		ajax: ajaxTemplateRequest,
                columns: [{
                    title: $.i18n.prop('previewImgTitle'),
                    field: 'previewImg',
                    align: 'center',
		    width: '155px',
		    formatter:function(value,row,index){  
			var picpath = "images/template-private.jpg";
			var imghtml = '<img src="' +picpath + '" class="img-polaroid"  width="150px" height="auto"></img>';
			return imghtml;
		    }
                },{
                    title: $.i18n.prop('tempnameTitle'),
                    field: 'tempname',
                    align: 'center'
                },{
                    title: $.i18n.prop('propertyTitle'),
                    field: 'property',
                    align: 'center',
		    formatter:function(value,row,index){  
			return templateTypeFormat(row.property);
	 	    }
                },{
	            title: $.i18n.prop('operationTile'),
	            field: 'tid',
	            align: 'center',
	            formatter:function(value,row,index){  
			var html = '';
                    	html = html + '  <button title="'+$.i18n.prop('delTtile')+'" class="btn btn-danger btn-xs" onclick="delTemplate(\'' + row.tid + '\');"><i class="fa fa-trash-o"></i></button>';
	                
	                return html;  
		    }
	        }],
                pagination:true
});
}


function ajaxTemplateRequest(params) {
		ajaxGet("templates/template", params.data, function(result){
			if(result.success)
			{
				params.success({
					total: result.data.total,
					rows: result.data.template_items
				});  
			}else{
				if (result.status == 404)
					alert($.i18n.prop('noAccessMsg'));
			} 
		}, null);
}

function TemplateQueryParams(params) { 
		var params_t = {};
		params_t['offset'] = params.offset;
		$('#templateSearchForm').find('input[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		$('#templateSearchForm').find('select[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		return params_t;
}

function refreshTemplateTable() {
	$('#template-table').bootstrapTable('refresh');
}

function delTemplate(uuid) {
		bootbox.confirm($.i18n.prop('deltipMsg') + "[" + uuid + "]", function(result) {
			if (result) {
				ajaxDel("templates/template/" + uuid,null,function(result){
					if(result.success)
					{
						refreshTemplateTable();
					}
					else
					{
						FailDialog($.i18n.prop('failTitle'),result.msg);
					}
				},null);
			}
		});
}

