function browseFilesForPid(pid,ftype){
	var param = "pid=" + pid;	
	if (ftype != "")
		param = "fileType=" + ftype;
	else
		$("#addNewFolderForm").contents().find("#pid").val(pid);

	ajaxGet("adEditor/resources/browse?" + param,null,function(result){
		if (result.success)
		{
			var files = result.data.files;
			var folder = result.data.folder;
			var files_html = "";
			var folder_html = '<li><a href="javascript:history.back();"><i class="fa fa-folder-o"></i> 返回上层</a></li>';
			if (files)
			{
				for(var i = 0; i < files.length; i++){
					var pic_h = '<img src="images/photos/media-doc.png" class="img-responsive" alt="" />';
					var filetype_h = "document";
					if (files[i].fileType == "pic"){
						filetype_h = "image";
						pic_h = '<a href="'+getRootUrl()+ files[i].resPath+'" data-rel="prettyPhoto"><img src="'+getRootUrl()+ files[i].resPath+'" class="img-responsive" alt="" /></a>';
					}else if (files[i].fileType == "video"){
						filetype_h = "video";
						pic_h = '<img src="images/photos/media4.png" class="img-responsive" alt="" />';
					}else if (files[i].fileType == "music"){
						filetype_h = "audio";
						pic_h = '<img src="images/photos/media-audio.png" class="img-responsive" alt="" />';
					}
					files_html += '<div class="col-xs-6 col-sm-4 col-md-3 '+filetype_h+'"><div class="thmb"><div class="ckbox ckbox-default"><input type="checkbox" id="check'+files[i].id+'" value="'+files[i].id+'" /><label for="check'+files[i].id+'"></label></div><div class="btn-group fm-group"><button type="button" class="btn btn-default dropdown-toggle fm-toggle" data-toggle="dropdown"><span class="caret"></span></button><ul class="dropdown-menu fm-menu" role="menu"><li><a href="#"><i class="fa fa-download"></i> 下载</a></li><li><a href="#"><i class="fa fa-trash-o"></i> 删除</a></li></ul></div><div class="thmb-prev">'+pic_h+'</div><h5 class="fm-title"><a href="">'+files[i].resName+'</a></h5><small class="text-muted">Added: Jan 03, 2014</small></div></div>';
				}
				$("#browselist").html(files_html);
			}
			if (folder)
			{
				for(var i = 0; i < folder.length; i++){
					folder_html += '<li><a href="./files.html?pid='+folder[i].id+'"><i class="fa fa-folder-o"></i> '+folder[i].resName+'</a></li>';
				}
				$("#browsefolderlist").html(folder_html);
			}
		}	
	}, null);
}



function showAddFolderModal(){
	$(".addFolderModal").html();
}


function addFolder(formname){
	ajaxSubmit("adEditor/resources/forlder/add", formname,function(result){
		if(result.success)
		{
			bootbox.alert({  
			    	buttons: {  
					ok: {  
						label: '确认',  
						className: 'btn btn-success'  
					}  
				},  
				message: '新建文件夹成功', 
				title: "新建文件夹",  
			}); 
			$('.addFolderModal').modal('hide');
			location.reload();
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


function initRemoteFileTable(){
            $('#remote-file-table').bootstrapTable({
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
                toolbar: '#toolbar',
                queryParams: remoteFilequeryParams, 
    	        queryParamsType: 'limit',
		ajax: remoteFileAjaxRequest,
		detailView: false,
                columns: [{
                    title: '文件名',
                    field: 'fname',
                    align: 'center',
		    width: '250px'
                },{
                    title: '文件类型',
                    field: 'ftype',
                    align: 'center',
		    width: '120px'
                },{
                    title: '文件大小',
                    field: 'fsize',
                    align: 'center',
		    width: '120px',
		    formatter:function(value,row,index){  
			return row.fsize + "B";
		    }
                },{
                    title: '存储地址',
                    field: 'fpath',
                    align: 'center',
		    formatter:function(value,row,index){  
			return row.fpath;
		    }
                }],
                pagination:true
            });
}

function remoteFileAjaxRequest(params) {
		ajaxGet("files/files",params.data,function(result){
			if(result.success)
			{
				params.success({
					total: result.data.total,
					rows: result.data.file_items
				});  
			} 
		}, null);
}

function remoteFilequeryParams(params) {
		var params_t = {};
		params_t['offset'] = params.offset;
		params_t['rn'] = new Date().getTime();

		return params_t;
}
	
function refeshRemoteFileTable(){
	$('#remote-file-table').bootstrapTable('refresh');
}

function initRemoteFileSelect(selectId){
	ajaxGet("files/list",null,function(result){
		if (result.success)
		{
			var retJ = result.data;
			if (retJ)
			{
				var sel_options = '';
				for(var item in retJ)
				{
					sel_options += '<option value="'+retJ[item].fid+'">'+retJ[item].fname+'</option>';
				}
				selectId.html(sel_options);
			}
		}	
	}, null);
}
