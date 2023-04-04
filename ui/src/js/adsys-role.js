var zAppRolesNodes =[
            { id:1, pId:0, name:"全部", open:true},
            { id:100, pId:1, name:"用户角色管理", open:true},
		{ id:103, pId:100, name:"角色写权限"},
		{ id:104, pId:100, name:"角色读权限"},
		{ id:105, pId:100, name:"用户写权限"},
		{ id:106, pId:100, name:"用户读权限"},
	    { id:300, pId:1, name:"设备管理", open:true},
		{ id:301, pId:300, name:"设备查看"},
		{ id:302, pId:300, name:"设备编辑删除"},
	    { id:400, pId:1, name:"群组管理", open:true},
		{ id:401, pId:400, name:"群组编辑删除"},
	    { id:700, pId:1, name:"发布管理", open:true},
		{ id:701, pId:700, name:"节目审核"},
		{ id:703, pId:700, name:"节目发布"},
		{ id:702, pId:700, name:"节目编辑删除"},
	    { id:800, pId:1, name:"系统设置", open:true},
		{ id:801, pId:800, name:"系统设置权限"},
];

function initRoleTable(){
        $('#roles-table').bootstrapTable({
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
                queryParams: roleQueryParams,
    	        queryParamsType: 'limit',
		ajax: ajaxRolesRequest,
		detailView: false,
                columns: [{
                    title: '角色名称',
                    field: 'rolename',
                    align: 'center'
                },{
                    title: '角色权限',
                    field: 'permissionname',
                    align: 'center'
    	        },{
                    title: '操作',
                    field: 'id',
                    align: 'center',
		    formatter:function(value,row,index){
			var html = '';
                    	html = html + '<button type="button" title="删除" class="btn btn-danger btn-xs btn-table" onclick="delRoles(\'' + row.id + '\');"><i class="fa fa-trash-o"></i></button>';

	                return html;
		    }
                }],
                pagination:true
      });
}

function ajaxRolesRequest(params) {
	ajaxGet("roles/roles", params.data, function(result){
		if(result.success)
		{
			params.success({
				total: result.data.total,
				rows: result.data.role_items
			});
		}
	}, null);

}

function delRoles(uuid) {
	bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
		if (result) {
			ajaxDel("roles/role/" + uuid,null,function(result){
				if(result.success)
				{
					refreshRoleTable();
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

function showAddRolesModal(){

	$('.addRolesModal').modal('show');
}


function addRoles(){
	ajaxSubmit("roles/role",'#addRolesForm',function(result){
		if(result.success)
		{
			bootbox.alert({
			    	buttons: {
					ok: {
						label: '确认',
						className: 'btn btn-success'
					}
				},
				message: '提交成功',
				title: "新增角色",
			});
			$('.addRolesModal').modal('hide');
			clearForm($(".addRolesForm"));
			refreshRoleTable();
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


function roleQueryParams(params) {
	var params_t = {};
	params_t['offset'] = params.offset;
	$('#rolesSearchForm').find('input[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	$('#rolesSearchForm').find('select[name]').each(function () {
		params_t[$(this).attr('name')] = $(this).val();
	});
	return params_t;
}

function refreshRoleTable(){
	$('#roles-table').bootstrapTable('refresh');
}

