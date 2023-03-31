function setGroupTreeView(tvname,tvform) {
	ajaxGet("groups/tree",null,function(result){
		if(result.success)
		{
			var treeObj = JSON.parse(result.data.tree);
			$(tvname).treeview({
					data: treeObj,
					showIcon: false,	
					levels:3
					}); 
			$(tvname).on('nodeSelected', function(event, data) {
				treeviewSelectHandler(tvform,data);
			});  

			$(tvname).on('nodeUnselected', function(event, data) {
				treeviewunSelectHandler(tvform,data);
			});  
			//$(tvname).treeview('expandAll', [ { silent: true } ]);
		} 
	}, null);
}

function setGroupTreeViewSelecter(tvname,onNodeSelected_f) {
	ajaxGet("groups/tree",null,function(result){
		if(result.success)
		{
			var treeObj = JSON.parse(result.data.tree);
			$(tvname).treeview({
					data: treeObj,
					showIcon: false,	
					levels:3
					}); 
			$(tvname).on('nodeSelected', onNodeSelected_f);  
		} 
	}, null);
}

function showAddGroupModal(){
	$('.addGroupModal').modal('show');
}

function addGroup(treename){
	ajaxSubmit("groups/group",'#addGroupModalForm',function(result){
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
				title: "添加群组信息",  
			}); 
			$('.addGroupModal').modal('hide');
			clearForm($('#addGroupModalForm'));
			setGroupTreeView(treename);
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



function delGroup(treename, uuid) {
		bootbox.confirm("确定要删除[" + uuid + "]吗?", function(result) {
			if (result) {
				ajaxDel("groups/group/" + uuid,null,function(result){
					if(result.success)
					{
						setGroupTreeView(treename);
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
