function setGroupTreeView(tvname,tvform) {
	ajaxGet("groups/tree",null,function(result){
		if(result.success)
		{
			var treeObj = JSON.parse(result.data.tree);
			treeObj[0].state = {checked: true, selected: true}
			treeviewSelectHandler(tvform,treeObj[0]);
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
			OKDialog($.i18n.prop('addgroupTitle'),$.i18n.prop('addgroupMsg')); 
			$('.addGroupModal').modal('hide');
			clearForm($('#addGroupModalForm'));
			setGroupTreeView(treename);
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);
}



function delGroup(treename, uuid) {
		bootbox.confirm($.i18n.prop('deltipMsg') + "[" + uuid + "]", function(result) {
			if (result) {
				ajaxDel("groups/group/" + uuid,null,function(result){
					if(result.success)
					{
						setGroupTreeView(treename);
					}
					else
					{
						FailDialog($.i18n.prop('failTitle'),result.msg);
					}
				},null);
			}
		});
}
