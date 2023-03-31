function initCustomerTable() {
	var language = getCookie('language');
	$('#customer-table').bootstrapTable({
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
                queryParams: CustomerqueryParams,
    	        queryParamsType: 'limit',
		ajax: ajaxCustomersRequest,
                columns: [{
                    title: $.i18n.prop('cnameTitle'),
                    field: 'cname',
                    align: 'center'
                },{
                    title: $.i18n.prop('ctelTitle'),
                    field: 'tel',
                    align: 'center'
                },{
                    title: $.i18n.prop('caddrTitle'),
                    field: 'caddr',
                    align: 'center'
                },{
                    title: $.i18n.prop('installnumTitle'),
                    field: 'installnum',
                    align: 'center'
                },{
                    title: $.i18n.prop('limitnumTitle'),
                    field: 'limitnum',
                    align: 'center'
                },{
                    title: $.i18n.prop('expriedateTitle'),
                    field: 'expriedate',
                    align: 'center'
                },{
                    title: $.i18n.prop('adddateTitle'),
                    field: 'adddate',
                    align: 'center',
		    formatter:function(value,row,index){  
			if (row.adddate)
    	            		return timestampFormat(row.adddate);
			else
				return null;
    	            }
                },{
	            title: $.i18n.prop('operationTile'),
	            field: 'id',
	            align: 'center',
	            formatter:function(value,row,index){  
			var html = '';
			html = html + '<button title="'+$.i18n.prop('moreTtile')+'" class="btn btn-default btn-xs" onclick="showDevListPage(\'' + row.uuid + '\');"><i class="fa  fa-th-list"></i></button>';
	                html = html + ' <button title="'+$.i18n.prop('editTtile')+'" class="btn btn-warning btn-xs" onclick="showEditCustomerModal(\'' + row.uuid + '\');"><i class="fa fa-edit"></i></button>';
                    	html = html + '  <button title="'+$.i18n.prop('delTtile')+'" class="btn btn-danger btn-xs" onclick="delCustomer(\'' + row.uuid + '\');"><i class="fa fa-trash-o"></i></button>';
	                
	                return html;  
		    }
	        }],
                pagination:true
});
}

function showDevListPage(cid){
		window.location.href="./devices.html?cid=" + cid;
}

function ajaxCustomersRequest(params) {
		ajaxGet("customer/customer", params.data, function(result){
			if(result.success)
			{
				params.success({
					total: result.data.total,
					rows: result.data.customer_items
				});  
			}else{
				if (result.status == 404)
					alert("无访问权限，请联系管理员!");
			} 
		}, null);
}

function initCustomersSelect(selectId){
	ajaxGet("customer/list",null,function(result){
		if (result.success)
		{
			var retJ = result.data;
			if (retJ)
			{
				var sel_options = '<option value="">全部</option>';
				for(var item in retJ)
				{
					sel_options += '<option value="'+retJ[item].uuid+'">'+retJ[item].cname+'</option>';
				}
				selectId.html(sel_options);
			}
		}	
	}, null);
}

function delCustomer(uuid) {
		bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
			if (result) {
				ajaxDel("customer/customer/" + uuid,null,function(result){
					if(result.success)
					{
						refreshCustomerTable();
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


function addAppuser_validform(){
	return $("#addCustomersForm").validate();
}

function addCustomer(){
			ajaxSubmit("appusers/reg",'#addCustomersForm',function(result){
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
						title: "添加会员用户信息",  
					}); 
					$('.addCustomerModal').modal('hide');
					refreshCustomerTable();
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

function showEditCustomerModal(id){
		ajaxGet("customer/customer/" + id,null,function(result){
			if (result.success)
			{
				var retJ = result.data;
				if (retJ)
				{
					for(var item in retJ)
					{
						$("#editCustomersForm").contents().find("#"+item).val(retJ[item]);
					}
					
					$('.editCustomerModal').modal('show');
				}
			}	
		}, null);
}

function editAppuser_validform(){
	return $(".editCustomersForm").validate();
}

function editCustomer(){

			var id = $("#editCustomersForm").contents().find("#uuid").val();
			ajaxSubmit("customer/customer/" + id,'#editCustomersForm',function(result){
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
						title: "编辑会员用户信息",  
					}); 
					$('.editCustomerModal').modal('hide');
					refreshCustomerTable();
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

function refreshCustomerTable() {
	$('#customer-table').bootstrapTable('refresh');
}



function showAddCustomerModal(){
	$('.addCustomerModal').modal('show');
}

	
function CustomerqueryParams(params) { 
		var params_t = {};
		params_t['offset'] = params.offset;
		$('#CustomerSearchForm').find('input[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		$('#CustomerSearchForm').find('select[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		return params_t;
}
