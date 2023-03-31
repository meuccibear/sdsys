function initSysUserTable(){
	    var language = getCookie('language');
            $('#sysusers-table').bootstrapTable({
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
                queryParams: userQueryParams,
    	        queryParamsType: 'limit',
		ajax: ajaxUsersRequest,
                columns: [{
                    title: $.i18n.prop('usernameTitle'),
                    field: 'username',
                    align: 'center'
                },{
                    title: $.i18n.prop('nameTitle'),
                    field: 'name',
                    align: 'center'
                },{
                    title: $.i18n.prop('telTitle'),
                    field: 'tel',
                    align: 'center'
                },{
                    title: $.i18n.prop('emailTitle'),
                    field: 'email',
                    align: 'center'
                },{
                    title: $.i18n.prop('logindateTitle'),
                    field: 'logindate',
                    align: 'center',
		    formatter:function(value,row,index){  
    	            	return timestampFormat(row.logindate);
    	            }
                },{
	            title: $.i18n.prop('operationTile'),
	            field: 'id',
	            align: 'center',
	            formatter:function(value,row,index){  
			var html = '';
	                html = html + '<button title="'+$.i18n.prop('editTtile')+'" class="btn btn-warning btn-xs" onclick="showEditUserModal(\'' + row.uid + '\');"><i class="fa fa-edit"></i></button>';
                    	html = html + ' <button title="'+$.i18n.prop('delTtile')+'" class="btn btn-danger btn-xs" onclick="delUser(\'' + row.uid + '\');"><i class="fa fa-trash-o"></i></button>';
	                
	                return html;  
		    }
	        }],
                pagination:true
            });
        }

function initAppUserTable() {
	var language = getCookie('language');
	$('#appusers-table').bootstrapTable({
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
                queryParams: appUserqueryParams,
    	        queryParamsType: 'limit',
		ajax: ajaxAppusersRequest,
                columns: [{
                    title: $.i18n.prop('usernameTitle'),
                    field: 'username',
                    align: 'center'
                },{
                    title: $.i18n.prop('nameTitle'),
                    field: 'name',
                    align: 'center'
                },{
                    title: $.i18n.prop('telTitle'),
                    field: 'tel',
                    align: 'center'
                },{
                    title: $.i18n.prop('emailTitle'),
                    field: 'email',
                    align: 'center'
                },{
                    title: $.i18n.prop('usertypeTitle'),
                    field: 'rolename',
                    align: 'center'
                },{
                    title: $.i18n.prop('logindateTitle'),
                    field: 'logindate',
                    align: 'center',
		    formatter:function(value,row,index){  
			if (row.logindate)
    	            		return timestampFormat(row.logindate);
			else
				return null;
    	            }
                },{
	            title: $.i18n.prop('operationTile'),
	            field: 'id',
	            align: 'center',
	            formatter:function(value,row,index){  
			var html = '';
	                html = html + '<button title="'+$.i18n.prop('editTtile')+'" class="btn btn-warning btn-xs" onclick="showEditAppuserModal(\'' + row.uid + '\');"><i class="fa fa-edit"></i></button>';
                    	html = html + '  <button title="'+$.i18n.prop('delTtile')+'" class="btn btn-danger btn-xs" onclick="delAppuser(\'' + row.uid + '\');"><i class="fa fa-trash-o"></i></button>';
	                
	                return html;  
		    }
	        }],
                pagination:true
});
}

function ajaxUsersRequest(params) {
		ajaxGet("sysusers/sysusers", params.data, function(result){
			if(result.success)
			{
				params.success({
					total: result.data.total,
					rows: result.data.sysuser_items
				});  
			}else{
				if (result.status == 404)
					alert($.i18n.prop('noAccessMsg'));
			} 
		}, null);
}

function ajaxAppusersRequest(params) {
		ajaxGet("appusers/appusers", params.data, function(result){
			if(result.success)
			{
				params.success({
					total: result.data.total,
					rows: result.data.appuser_items
				});  
			}else{
				if (result.status == 404)
					alert($.i18n.prop('noAccessMsg'));
			} 
		}, null);
}

function delUser(uuid) {
		bootbox.confirm($.i18n.prop('deltipMsg') + "[" + uuid + "]", function(result) {
			if (result) {
				ajaxDel("sysusers/sysuser/" + uuid,null,function(result){
					if(result.success)
					{
						refreshSysUserTable();
					}
					else
					{
						FailDialog($.i18n.prop('failTitle'),result.msg);
					}
				},null);
			}
		});
}

function delAppuser(uuid) {
		bootbox.confirm($.i18n.prop('deltipMsg') + "[" + uuid + "]", function(result) {
			if (result) {
				ajaxDel("appusers/appuser/" + uuid,null,function(result){
					if(result.success)
					{
						refreshAppUserTable();
					}
					else
					{
						FailDialog($.i18n.prop('failTitle'),result.msg);
					}
				},null);
			}
		});
}

function showAddSysuserModal(){
	$('.addSysUserModal').modal('show');
}

function addSysuser(){
	if(user_validform("#addSysUsersForm").form()) {
			ajaxSubmit("sysusers/sysuser",'#addSysUsersForm',function(result){
				if(result.success)
				{
					OKDialog($.i18n.prop('addsysuserTitle'),$.i18n.prop('addsysuserMsg'));
					$('.addSysUserModal').modal('hide');
					clearForm($('#addSysUsersForm'));
					refreshSysUserTable()
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
	}
 }


function showEditUserModal(id){
		ajaxGet("sysusers/sysuser/" + id,null,function(result){
			if (result.success)
			{
				var retJ = result.data;
				if (retJ)
				{
					for(var item in retJ)
					{
						$("#editSysUsersForm").contents().find("#"+item).val(retJ[item]);
					}
					$('.editSysUserModal').modal('show');
				}
			}	
		}, null);
}



function editSysuser(){
	if(user_validform("#editSysUsersForm").form()) {
			var id = $("#editSysUsersForm").contents().find("#uid").val();
			ajaxSubmit("sysusers/sysuser/" + id,'#editSysUsersForm',function(result){
				if(result.success)
				{
					OKDialog($.i18n.prop('editsysuserTitle'),$.i18n.prop('editsysuserMsg'));
					$('.editSysUserModal').modal('hide');
					refreshSysUserTable();
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
	}
}

function telnum_valid(){
	jQuery.validator.addMethod("isMobile", function(value, element) {
		var length = value.length;
		var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
		return this.optional(element) || (length == 11 && mobile.test(value));
	}, $.i18n.prop('inputphonenoMsg'));
}

function user_validform(formname){
	telnum_valid();
	return $(formname).validate({
	    rules: {
	    	tel : {  
	            required : true,  
	            minlength : 11, 
	            isMobile : true  
	        		}, 
	          },
	   messages: {
		   tel : {  
		       required : $.i18n.prop('inputphoneMsg'),  
		       minlength : $.i18n.prop('inputphone11Msg'),  
		       isMobile : $.i18n.prop('inputphonenoMsg')  
		  	}
  		 },
	});
}

function addAppuser(){
	if(user_validform("#addAppUsersForm").form()) {
			ajaxSubmit("appusers/member",'#addAppUsersForm',function(result){
				if(result.success)
				{
					OKDialog($.i18n.prop('adduserTitle'),$.i18n.prop('adduserMsg'));
					$('.addAppUserModal').modal('hide');
					clearForm($('#addAppUsersForm'));
					refreshAppUserTable();
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
	}
}

function showEditAppuserModal(id){
		addRoleSelect("#editAppUsersForm", "#roleid");
		ajaxGet("appusers/appuser/" + id,null,function(result){
			if (result.success)
			{
				var retJ = result.data;
				if (retJ)
				{
					for(var item in retJ)
					{
						$("#editAppUsersForm").contents().find("#"+item).val(retJ[item]);
					}
					
					$('.editAppUserModal').modal('show');
				}
			}	
		}, null);
}


function editAppuser(){
	if(user_validform("#editAppUsersForm").form()) {
			var id = $("#editAppUsersForm").contents().find("#uid").val();
			ajaxSubmit("appusers/appuser/" + id,'#editAppUsersForm',function(result){
				if(result.success)
				{
					OKDialog($.i18n.prop('edituserTitle'),$.i18n.prop('edituserMsg'));
					$('.editAppUserModal').modal('hide');
					refreshAppUserTable();
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
	}
}

function refreshAppUserTable() {
	$('#appusers-table').bootstrapTable('refresh');
}

function refreshSysUserTable() {
	$('#sysusers-table').bootstrapTable('refresh');
}

function addRoleSelect(form, selecter){
		ajaxGet("roles/alllist",null,function(result){
			if (result.success)
			{
				var retJ = result.data;
				if (retJ)
				{
					//console.log(item);
					var optionstring = "<option value=''>"+$.i18n.prop('selroletipMsg')+"</option>";
					for (var j = 0; j < retJ.length; j++) {  
						optionstring += "<option value=\"" + retJ[j].id + "\" >" + retJ[j].rolename + "</option>";  
					}  
					$(form).contents().find(selecter).html(optionstring);
				}
			}	
		}, null);
}

function showAddAppuserModal(){
	addRoleSelect("#addAppUsersForm", "#roleid");
	$('.addAppUserModal').modal('show');
}

function userQueryParams(params) {
		var params_t = {};
		params_t['offset'] = params.offset;
		$('#userSearchForm').find('input[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		$('#userSearchForm').find('select[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		return params_t;
}
	
function appUserqueryParams(params) { 
		var params_t = {};
		params_t['offset'] = params.offset;
		$('#appUserSearchForm').find('input[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		$('#appUserSearchForm').find('select[name]').each(function () {
			params_t[$(this).attr('name')] = $(this).val();
		});
		return params_t;
}


function showAppUserProfile() {
		ajaxGet("appusers/profile", null,function(result){
			if(result.success)
			{
				$("#username").val(result.data.username);
				$("#name").val(result.data.name);
				$("#tel").val(result.data.tel);
				$("#email").val(result.data.email);
			} 
		}, null);
}

function appProfile_validform(){
		return $("#editProfileForm").validate({
			rules:{
		            password:{
		                required:false,
		                rangelength:[5,12]
		            },
		            confirm_password:{
				required:false,
		                equalTo:"#password"
		            } 
			}                   
                });
	}

function updateAppProfile(){
		if(appProfile_validform().form()) {
			ajaxSubmit("appusers/profile",'#editProfileForm',function(result){
				if(result.success)
				{
					bootbox.dialog({  
			    			buttons: {  
							ok: {  
								label: 'OK',  
								className: 'btn btn-success',
								callback: function () {  
									window.location.reload();
								} 
							}  
						},  
						message: $.i18n.prop('editprofileMsg'), 
						title: $.i18n.prop('editprofileTitle'),  
					}); 
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
		}
}


function showSysUserProfile() {
		ajaxGet("sysusers/profile", null,function(result){
			if(result.success)
			{
				$("#username").val(result.data.username);
				$("#name").val(result.data.name);
				$("#tel").val(result.data.tel);
				$("#email").val(result.data.email);
			} 
		}, null);
}


function updateSysProfile(){
		if(appProfile_validform().form()) {
			ajaxSubmit("sysusers/profile",'#editProfileForm',function(result){
				if(result.success)
				{
					bootbox.dialog({  
			    			buttons: {  
							ok: {  
								label: 'OK',  
								className: 'btn btn-success',
								callback: function () {  
									window.location.reload();
								} 
							}  
						},  
						message: $.i18n.prop('editprofileMsg'), 
						title: $.i18n.prop('editprofileTitle'),  
					}); 
				}
				else
				{
					FailDialog($.i18n.prop('failTitle'),result.msg);
				}
			}, null);
		}
}

