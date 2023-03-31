function getToken(){
	if(window.localStorage){
		var tk = window.localStorage.getItem("ad_sys_tk");
		if (tk == null)
			return '';
		return tk;
	}
	return '';
}

