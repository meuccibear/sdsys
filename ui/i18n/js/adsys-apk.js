function addapk(){
    var form = new FormData();
    form.append("version", $('#version').val())
    form.append("readme", $('#readme').val())
    form.append("md5sum", $('#md5sum').val())
    form.append("fileAPK", $('#fileAPK')[0].files[0])
    $.ajax({
        url: getRootUrl() + "/update/addAPK",
        type: "post",
        headers: {'X-Access-Token': getToken()},
        data: form,
        dataType: "json",
        cache: false,
        processData: false,
        contentType: false,
        success:function (data) {
            if(data.success){
                alert(data.msg)
                $('.addAPKModal').modal('hide')
                document.location.reload();
            }else{
		 alert(data.msg);
	    }
        }
    })
}

function initAPKTableAdmin(){
    var language = getCookie('language');
    $('#apkTable-admin').bootstrapTable({
        method: 'get',
        striped: true,
        cache: false,
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber:1,
        pageSize: 10,
        pageList: [10, 20, 50],
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
        ajax: ajaxAPKRequest,
        columns: [{
            title: $.i18n.prop('versionTitle'),
            field: 'version',
            align: 'center'
        },{
            title: $.i18n.prop('readmeTitle'),
            field: 'readme',
            align: 'center'
        },{
            title: $.i18n.prop('md5sumTitle'),
            field: 'md5sum',
            align: 'center'
        },{
            title: $.i18n.prop('createdateTitle'),
            field: 'createdate',
            align: 'center',
            formatter:function(value,row,index){
                return timestampFormat(row.createdate);
            }
        },{
            title: $.i18n.prop('operationTile'),
            field: 'id',
            align: 'center',
            formatter:function(value,row,index){
                var html = '';
                html = html + ' <button title="'+$.i18n.prop('delTtile')+'" class="btn btn-danger btn-xs" onclick="delAPK(\'' + row.version + '\');"><i class="fa fa-trash-o"></i></button>';

                return html;
            }
        }],
        pagination:true
    });
}

function initAPKTableUser(){
    var language = getCookie('language');
    $('#apkTable-user').bootstrapTable({
        method: 'get',
        striped: true,
        cache: false,
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber:1,
        pageSize: 10,
        pageList: [10, 20, 50],
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
        ajax: ajaxAPKRequest,
        columns: [{
            title: $.i18n.prop('versionTitle'),
            field: 'version',
            align: 'center'
        },{
            title: $.i18n.prop('readmeTitle'),
            field: 'readme',
            align: 'center'
        },{
            title: $.i18n.prop('md5sumTitle'),
            field: 'md5sum',
            align: 'center'
        },{
            title: $.i18n.prop('createdateTitle'),
            field: 'createdate',
            align: 'center',
            formatter:function(value,row,index){
                return timestampFormat(row.createdate);
            }
        },{
            title: $.i18n.prop('operationTile'),
            field: 'id',
            align: 'center',
            formatter:function(value,row,index){
                var html = '';
                html = html + ' <button title="'+$.i18n.prop('updateTtile')+'" class="btn btn-warning btn-xs" onclick="sendUpdateToDevices(\'' + row.md5sum + '\');"><i class="fa fa-sitemap"></i></button>';

                return html;
            }
        }],
        pagination:true
    });
}
function formatDateTime(inputTime) {
    var date = new Date(inputTime);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    var second = date.getSeconds();
    minute = minute < 10 ? ('0' + minute) : minute;
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d + ' ' + ' ' + h + ':' + minute + ':' + second;
}
function ajaxAPKRequest(params) {
    ajaxGet("update/getAPKList", params.data, function(result){
	if(result.success)
	{
		params.success({
			total: result.data.length,
			rows: result.data
		});  
	}else{
		if (result.status == 404)
			alert($.i18n.prop('noAccessMsg'));
	}
    }, null);
}

function delAPK(version) {
    bootbox.confirm($.i18n.prop('deltipMsg') + "[" + version + "]", function(result) {
        if (result) {
            ajaxDel("update/deleteByVersion/" + version,null,function(result){
                if(result.success)
                {
                    refreshAPKTable();
                }
                else
                {
                    FailDialog($.i18n.prop('failTitle'),result.msg);
                }
            },null);
        }
    });
}

function sendUpdateToDevices(id){
	var params_t = {};
	ajaxSubmitByData("update/pushUpdate/"+id, params_t,function(result){
		if(!result.success)
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
		else
		{
			OKDialog($.i18n.prop('senddevTitle'),$.i18n.prop('updatedevTitle'));
		}
	}, null);
}

function refreshAPKTable() {
    $('#apkTable-admin').bootstrapTable('refresh');
}

