
        
function setCookie(name,value)
{
	var Days = 30;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}

function getCookie(name)
{
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)){
		return unescape(arr[2]);
	}
}

function EnloadProperties() {
     $.i18n.properties({
        name:'strings',
        path:'/i18n/',
        mode:'map',
        language:'en',
        callback:function () {
           $('[data-locale]').each(function(){
               $(this).html($.i18n.prop($(this).data("locale")));
           })
        }
    })
}
function ZhloadProperties() {
	$.i18n.properties({
	    name:'strings',
	    path:'/i18n/',
	    mode:'map',
	    language:'zh',
	    callback:function () {
		$('[data-locale]').each(function(){
			$(this).html($.i18n.prop($(this).data("locale")));
		})
	    }
	})
}
jQuery(document).ready(function() {
	var language = getCookie('language')
	if(language=="En"){
		 EnloadProperties();
		 setCookie('language','En');
	}else{
		setCookie('language','Zh');
		ZhloadProperties();
	} 

       $('.radio1').click(function () {
       		setCookie('language','En')
           	EnloadProperties()
           
       })

       $('.radio0').click(function () {
       		setCookie('language','Zh')
           	ZhloadProperties()
       })

	$('#toggleLanguage').click(function(){
		var language = getCookie('language')
		if(language==undefined){
			language = 'En'
		}
		if(language=="Zh"){ 
			 EnloadProperties()
			 setCookie('language','En')
		}else if(language=="En"){
			ZhloadProperties()
			setCookie('language','Zh')
		} 
		location.reload();
	})
 });
