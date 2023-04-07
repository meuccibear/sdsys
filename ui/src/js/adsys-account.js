function initSysUserTable() {
    $('#sysusers-table').bootstrapTable({
        method: 'get',
        striped: true,
        cache: false,
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber: 1,
        pageSize: 10,
        sidePagination: "server",
        strictSearch: true,
        minimumCountColumns: 2,
        clickToSelect: true,
        searchOnEnterKey: true,
        detailView: false,
        toolbar: '#toolbar',
        locale: "zh-CN",
        queryParams: userQueryParams,
        queryParamsType: 'limit',
        ajax: ajaxUsersRequest,
        columns: [{
            title: '编号',
            field: 'aid',
            align: 'center'
        }, {
            title: '账号',
            field: 'account',
            align: 'center'
        }, {
            title: '用户名',
            field: 'username',
            align: 'center'
        }, {
            title: '账号类型',
            field: 'accounttype',
            align: 'center'
        }, {
            title: '添加时间',
            field: 'addDate',
            align: 'center',
            formatter: function (value, row, index) {
                return timestampFormat(row.logindate);
            }
        }, {
            title: '操作',
            field: 'id',
            align: 'center',
            formatter: function (value, row, index) {
                var html = '';
                html = html + '<button title="修改" class="btn btn-warning btn-xs" onclick="showEditUserModal(\'' + row.uid + '\');"><i class="fa fa-edit"></i></button>';
                html = html + ' <button title="删除" class="btn btn-danger btn-xs" onclick="delUser(\'' + row.uid + '\');"><i class="fa fa-trash-o"></i></button>';

                return html;
            }
        }],
        pagination: true
    });
}

function initAppUserTable() {
    $('#appusers-table').bootstrapTable({
        method: 'get',
        striped: true,
        cache: false,
        pagination: true,
        sortable: false,
        sortOrder: "asc",
        pageNumber: 1,
        pageSize: 10,
        sidePagination: "server",
        strictSearch: true,
        minimumCountColumns: 2,
        clickToSelect: true,
        searchOnEnterKey: true,
        detailView: false,
        toolbar: '#toolbar',
        locale: "zh-CN",
        queryParams: appUserqueryParams,
        queryParamsType: 'limit',
        ajax: ajaxAppusersRequest,
        columns: [{
            title: '编号',
            field: 'aid',
            align: 'center'
        }, {
            title: '账号',
            field: 'account',
            align: 'center'
        }, {
            title: '用户名',
            field: 'username',
            align: 'center'
        }, {
            title: '账号类型',
            field: 'accounttype',
            align: 'center'
        }, {
            title: '状态',
            field: 'status',
            align: 'center'
        }, {
            title: '添加时间',
            field: 'addDate',
            align: 'center',
            formatter: function (value, row, index) {
                return timestampFormat(row.adddate);
            }
        }, {
            title: '操作',
            field: 'aid',
            align: 'center',
            formatter: function (value, row, index) {
                var html = '';
                html = html + '<button title="修改" class="btn btn-warning btn-xs" onclick="showEditAppuserModal(\'' + row.aid + '\');"><i class="fa fa-edit"></i></button>';
                // html = html + '  <button title="换账号" class="btn btn-info btn-xs" onclick="changeAccount(\'' + row.aid + '\');"><i class="fa fa-exchange"></i></button>';
                html = html + '  <button title="删除" class="btn btn-danger btn-xs" onclick="delAppuser(\'' + row.aid + '\');"><i class="fa fa-trash-o"></i></button>';

                return html;
            }
        }],
        pagination: true
    });
}

function ajaxUsersRequest(params) {
    ajaxGet("sysusers/sysusers", params.data, function (result) {
        if (result.success) {
            params.success({
                total: result.data.total,
                rows: result.data.sysuser_items
            });
        }
    }, null);
}

function ajaxAppusersRequest(params) {
    ajaxGet("account/list", params.data, function (result) {
        if (result.success) {
            params.success({
                total: result.data.total,
                rows: result.data.appuser_items
            });
        }
    }, null);
}

function delUser(uuid) {
    bootbox.confirm("确定要删除[" + uuid + "]吗?", function (result) {
        if (result) {
            ajaxDel("sysusers/sysuser/" + uuid, null, function (result) {
                if (result.success) {
                    refreshSysUserTable();
                } else {
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
    });
}

function delAppuser(uuid) {
    bootbox.confirm("确定要删除[" + uuid + "]吗?", function (result) {
        if (result) {
            ajaxDel("account/appuser/" + uuid, null, function (result) {
                if (result.success) {
                    refreshAppUserTable();
                } else {
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
    });
}

function showAddSysuserModal() {
    $('.addSysUserModal').modal('show');
}

function addSysuser() {
    if (user_validform("#addSysUsersForm").form()) {
        ajaxSubmit("sysusers/sysuser", '#addSysUsersForm', function (result) {
            if (result.success) {
                bootbox.alert({
                    buttons: {
                        ok: {
                            label: '确认',
                            className: 'btn btn-success'
                        }
                    },
                    message: '提交成功',
                    title: "添加用户信息",
                });
                $('.addSysUserModal').modal('hide');
                clearForm($('#addSysUsersForm'));
                refreshSysUserTable()
            } else {
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
}


function showEditUserModal(id) {
    ajaxGet("sysusers/sysuser/" + id, null, function (result) {
        if (result.success) {
            var retJ = result.data;
            if (retJ) {
                for (var item in retJ) {
                    $("#editSysUsersForm").contents().find("#" + item).val(retJ[item]);
                }
                $('.editSysUserModal').modal('show');
            }
        }
    }, null);
}


function editSysuser() {
    if (user_validform("#editSysUsersForm").form()) {
        var id = $("#editSysUsersForm").contents().find("#uid").val();
        ajaxSubmit("sysusers/sysuser/" + id, '#editSysUsersForm', function (result) {
            if (result.success) {
                bootbox.alert({
                    buttons: {
                        ok: {
                            label: '确认',
                            className: 'btn btn-success'
                        }
                    },
                    message: '提交成功',
                    title: "编辑用户信息",
                });
                $('.editSysUserModal').modal('hide');
                refreshSysUserTable();
            } else {
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
}

function telnum_valid() {
    jQuery.validator.addMethod("isMobile", function (value, element) {
        var length = value.length;
        var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请正确填写您的手机号码");
}

function user_validform(formname) {
    telnum_valid();
    return $(formname).validate({
        rules: {
            tel: {
                required: true,
                minlength: 11,
                isMobile: true
            },
        },
        messages: {
            tel: {
                required: "请输入手机号",
                minlength: "不能小于11个字符",
                isMobile: "请正确填写手机号码"
            }
        },
    });
}

function addAppuser() {
    if (user_validform("#addAppUsersForm").form()) {
        ajaxSubmit("account/add", '#addAppUsersForm', function (result) {
            if (result.success) {
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
                $('.addAppUserModal').modal('hide');
                clearForm($('#addAppUsersForm'));
                refreshAppUserTable();
            } else {
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
}

function showEditAppuserModal(id) {
    addRoleSelect("#editAppUsersForm", "#roleid");
    ajaxGet("account/appuser/" + id, null, function (result) {
        if (result.success) {
            var retJ = result.data;
            if (retJ) {
                for (var item in retJ) {
                    $("#editAppUsersForm").contents().find("#" + item).val(retJ[item]);
                }

                $('.editAppUserModal').modal('show');
            }
        }
    }, null);
}

function editAppuser() {
    if (user_validform("#editAppUsersForm").form()) {
        var id = $("#editAppUsersForm").contents().find("#aid").val();
        var passwordEle = $("#editAppUsersForm").contents().find("#password")
        debugger
        if(passwordEle.val() === "******"){
            passwordEle.val("");
        }
        ajaxSubmit("account/appuser/" + id, '#editAppUsersForm', function (result) {
            if (result.success) {
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
                $('.editAppUserModal').modal('hide');
                refreshAppUserTable();
            } else {
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
}

function refreshAppUserTable() {
    $('#appusers-table').bootstrapTable('refresh');
}

function refreshSysUserTable() {
    $('#sysusers-table').bootstrapTable('refresh');
}

function addRoleSelect(form, selecter) {
    ajaxGet("roles/alllist", null, function (result) {
        if (result.success) {
            var retJ = result.data;
            if (retJ) {
                //console.log(item);
                var optionstring = "<option value=''>请选择角色</option>";
                for (var j = 0; j < retJ.length; j++) {
                    optionstring += "<option value=\"" + retJ[j].id + "\" >" + retJ[j].rolename + "</option>";
                }
                $(form).contents().find(selecter).html(optionstring);
            }
        }
    }, null);
}

function showAddAppuserModal() {
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
    ajaxGet("appusers/profile", null, function (result) {
        if (result.success) {
            $("#username").val(result.data.username);
            $("#name").val(result.data.name);
            $("#tel").val(result.data.tel);
            $("#email").val(result.data.email);
        }
    }, null);
}

function appProfile_validform() {
    return $("#editProfileForm").validate({
        rules: {
            password: {
                required: false,
                rangelength: [5, 12]
            },
            confirm_password: {
                required: false,
                equalTo: "#password"
            }
        }
    });
}

function updateAppProfile() {
    if (appProfile_validform().form()) {
        ajaxSubmit("appusers/profile", '#editProfileForm', function (result) {
            if (result.success) {
                bootbox.dialog({
                    buttons: {
                        ok: {
                            label: '确认',
                            className: 'btn btn-success',
                            callback: function () {
                                window.location.reload();
                            }
                        }
                    },
                    message: '提交成功',
                    title: "更新个人信息",
                });
            } else {
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
}


function showSysUserProfile() {
    ajaxGet("sysusers/profile", null, function (result) {
        if (result.success) {
            $("#username").val(result.data.username);
            $("#name").val(result.data.name);
            $("#tel").val(result.data.tel);
            $("#email").val(result.data.email);
        }
    }, null);
}


function updateSysProfile() {
    if (appProfile_validform().form()) {
        ajaxSubmit("sysusers/profile", '#editProfileForm', function (result) {
            if (result.success) {
                bootbox.dialog({
                    buttons: {
                        ok: {
                            label: '确认',
                            className: 'btn btn-success',
                            callback: function () {
                                window.location.reload();
                            }
                        }
                    },
                    message: '提交成功',
                    title: "更新个人信息",
                });
            } else {
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
}

