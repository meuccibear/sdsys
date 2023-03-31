function KeyDown()
{
	if (event.keyCode == 13)
	{
		event.returnValue=false;
		event.cancel = true;
		authForm.login.click();
	}
}

function onLogin(){
	ajaxSubmit("login/applogin",'#authForm',function(result){
		if(result.success)
		{
			saveToken(result.data.user_type, result.data.token);
			saveName(result.data.name);
			window.location.href='index.html'; 
		}
		else
		{
			FailDialog($.i18n.prop('loginfailTitle'),result.msg);
		}
	}, null);
}

function onSysLogin(){
	ajaxSubmit("login/syslogin",'#authForm',function(result){
		if(result.success)
		{
			saveToken(result.data.user_type, result.data.token);
			saveName(result.data.name);
			window.location.href='./index.html'; 
		}
		else
		{
			FailDialog($.i18n.prop('loginfailTitle'),result.msg);
		}
	}, null);
}
