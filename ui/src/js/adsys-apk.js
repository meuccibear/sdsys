function addapk(){
    var form = new FormData();
    form.append("version", $('#version').val())
    form.append("md5sum", $('#md5sum').val())
    form.append("fileAPK", $('#fileAPK')[0].files[0])
    $.ajax({
        url: getRootUrl() + "/addAPK",
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


function getApkList() {
	ajaxGet("/getAPKList", null, function(result){
		if(result.success)
		{
			for (var i = 0; i < result.data.length; i++) {
                        $('#apktable').after(
                            '<tr style="margin-top: 60px"><th>'
                            + result.data[i].id +
                            '</th>' + '<th>'
                            + result.data[i].version +
                            '</th>' + '<th>'
                            + result.data[i].md5sum +
                            '</th>' + '<th>'
                            + formatDateTime(result.data[i].createdate)+
                            '</th></tr>')
                    	}
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

