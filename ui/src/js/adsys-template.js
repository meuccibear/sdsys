function initTemplateTable() {
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
		locale:"zh-CN",
                queryParams: TemplateQueryParams,
    	        queryParamsType: 'limit',
		ajax: ajaxTemplateRequest,
                columns: [{
                    title: '预览图',
                    field: 'previewImg',
                    align: 'center',
		    width: '155px',
		    formatter:function(value,row,index){  
			var picpath = "images/template-private.jpg";
			var imghtml = '<img src="' +picpath + '" class="img-polaroid"  width="150px" height="auto"></img>';
			return imghtml;
		    }
                },{
                    title: '模板名称',
                    field: 'tempname',
                    align: 'center'
                },{
                    title: '模板类型',
                    field: 'property',
                    align: 'center',
		    formatter:function(value,row,index){  
			return templateTypeFormat(row.property);
	 	    }
                },{
	            title: '操作',
	            field: 'tid',
	            align: 'center',
	            formatter:function(value,row,index){  
			var html = '';
                    	html = html + '  <button title="删除" class="btn btn-danger btn-xs" onclick="delTemplate(\'' + row.tid + '\');"><i class="fa fa-trash-o"></i></button>';
	                
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
		bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
			if (result) {
				ajaxDel("templates/template/" + uuid,null,function(result){
					if(result.success)
					{
						refreshTemplateTable();
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

