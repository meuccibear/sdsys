function updateLicense(){
	ajaxSubmit("customer/license",'#licenseForm',function(result){
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
				title: "授权管理",  
			}); 
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
