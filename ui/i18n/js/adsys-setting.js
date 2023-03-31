function updateLicense(){
	ajaxSubmit("customer/license",'#licenseForm',function(result){
		if(result.success)
		{
			OKDialog($.i18n.prop('addlicTitle'),$.i18n.prop('addlicMsg'));
		}
		else
		{
			FailDialog($.i18n.prop('failTitle'),result.msg);
		}
	}, null);
 }
