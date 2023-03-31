function addapk(){
    var form = new FormData();
    form.append("version", $('#version').val())
    form.append("md5sum", $('#md5sum').val())
    form.append("fileAPK", $('#fileAPK')[0].files[0])
    $.ajax({
        url: "http://127.0.0.1:8080/ADSYS/addAPK",
        type: "post",
        headers: {'X-Access-Token': getToken()},
        data: form,
        dataType: "json",
        cache: false,
        processData: false,
        contentType: false,
        success:function (data) {
            if(data.status == 01){
                alert(data.msg)
                $('.addAPKModal').modal('hide')
                document.location.reload();
            }
        }
    })
}
function initAPKTable(){
    $('#apkTable').bootstrapTable({
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
        queryParams: appUserqueryParams,
        queryParamsType: 'limit',
        ajax: ajaxAPKRequest,
        columns: [{
            title: 'ID',
            field: 'id',
            align: 'center'
        },{
            title: '版本',
            field: 'version',
            align: 'center'
        },{
            title: 'md5sum',
            field: 'md5sum',
            align: 'center'
        },{
            title: '上传时间',
            field: 'createdate',
            align: 'center',
            formatter:function(value,row,index){
                return timestampFormat(row.createdate);
            }
        },{
            title: '操作',
            field: 'id',
            align: 'center',
            formatter:function(value,row,index){
                var html = '';
                html = html + ' <button title="推送升级" class="btn btn-danger btn-xs" onclick="upAPK(\'' + row.id + '\');"><i class="fa fa-edit"></i></button>';

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
    $.ajax({
        url:'http://127.0.0.1:8080/ADSYS/getAPKList',
        type: "get",
        headers: {'X-Access-Token': getToken()},
        success:function (date) {
            if(date.success){
                params.success({
                    total: date.status,
                    rows: date.data
                });
            }
        }

    })

}
function upAPK(id) {
    $.ajax({
        url:'http://127.0.0.1:8080/ADSYS/upAPK',
        type: "post",
        data:"id="+id,
        headers: {'X-Access-Token': getToken()},
        success:function (date) {
            if(date.success){
                alert(date.msg)
            }
        }
    })
}
function downloadAPK(md5sum) {
   $.ajax({
       url:'http://127.0.0.1:8080/ADSYS/downloadAPK',
       type: "post",
       data:"md5sum="+md5sum,
       headers: {'X-Access-Token': getToken()},
       success:function (date) {
           if(date.success){

           }
       }
   })
}
function refreshAPKTable() {
    $('#apkTable').bootstrapTable('refresh');
}

