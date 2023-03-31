var zAppRolesNodesZh =[  
            { id:1, pId:0, name:'全部', open:true},  
            { id:100, pId:1, name:'用户角色管理', open:true},  
		{ id:103, pId:100, name:'角色写权限'},    
		{ id:104, pId:100, name:'角色读权限'},  
		{ id:105, pId:100, name:'用户写权限'},  
		{ id:106, pId:100, name:'用户读权限'},
	    { id:300, pId:1, name:'设备管理', open:true},
		{ id:301, pId:300, name:'设备查看'}, 
		{ id:302, pId:300, name:'设备编辑删除'}, 
	    { id:400, pId:1, name:'群组管理', open:true},
		{ id:401, pId:400, name:'群组编辑删除'}, 
	    { id:700, pId:1, name:'节目管理', open:true},  
		{ id:701, pId:700, name:'节目审核'},  
		{ id:703, pId:700, name:'节目发布'}, 
		{ id:702, pId:700, name:'节目编辑删除'}, 
	    { id:800, pId:1, name:'系统设置', open:true},
		{ id:801, pId:800, name:'系统设置权限'}, 
		{ id:802, pId:800, name:'远程升级权限'}, 
];

var zAppRolesNodesEn =[  
            { id:1, pId:0, name:'All', open:true},  
            { id:100, pId:1, name:'Users And Roles', open:true},  
		{ id:103, pId:100, name:'Roles Write'},    
		{ id:104, pId:100, name:'Roles Read'},  
		{ id:105, pId:100, name:'Users Write'},  
		{ id:106, pId:100, name:'Users Read'},
	    { id:300, pId:1, name:'Devices Managerment', open:true},
		{ id:301, pId:300, name:'Device Read'}, 
		{ id:302, pId:300, name:'Device Edit/Del'}, 
	    { id:400, pId:1, name:'Group Managerment', open:true},
		{ id:401, pId:400, name:'Group Edit/Del'}, 
	    { id:700, pId:1, name:'Program Managerment', open:true},  
		{ id:701, pId:700, name:'Program Review'},  
		{ id:703, pId:700, name:'Program Publish'}, 
		{ id:702, pId:700, name:'Program Edit/Del'}, 
	    { id:800, pId:1, name:'System Setting', open:true},
		{ id:801, pId:800, name:'Setting Write'}, 
		{ id:802, pId:800, name:'Remote Upgrade'}, 
];

function initRoleTable(){
	var language = getCookie('language');
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
		locale:language =='zh'?'zh-CN':'en-US',
                queryParams: roleQueryParams,  
    	        queryParamsType: 'limit',
		ajax: ajaxRolesRequest,
		detailView: false,
                columns: [{
                    title: $.i18n.prop('rolenameTitle'),
                    field: 'rolename',
                    align: 'center'
                },{
                    title: $.i18n.prop('permissionnameTitle'),
                    field: 'permissionname',
                    align: 'center'
    	        },{
                    title: $.i18n.prop('operationTile'),
                    field: 'id',
                    align: 'center',
		    formatter:function(value,row,index){  
			var html = '';
                    	html = html + '<button type="button" title="'+$.i18n.prop('delTtile')+'" class="btn btn-danger btn-xs btn-table" onclick="delRoles(\'' + row.id + '\');"><i class="fa fa-trash-o"></i></button>';
	                
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
		}else{
			if (result.status == 404)
				alert($.i18n.prop('noAccessMsg'));
		} 
	}, null);

}

function delRoles(uuid) {
	bootbox.confirm($.i18n.prop('deltipMsg') + "[" + uuid + "]", function(result) {
		if (result) {
			ajaxDel("roles/role/" + uuid,null,function(result){
				if(result.success)
				{
					refreshRoleTable();
				}
				else
				{
					FailDialog($.i18n.prop('loginfailTitle'),result.msg);
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
			OKDialog($.i18n.prop('addroleTitle'),$.i18n.prop('addroleMsg'));
			$('.addRolesModal').modal('hide');
			clearForm($(".addRolesForm"));
			refreshRoleTable();
		}
		else
		{
			FailDialog($.i18n.prop('loginfailTitle'),result.msg);
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

