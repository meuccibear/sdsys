
        
function setCookie(name,value)
{
	if(window.localStorage){
		window.localStorage.setItem("ad_lang",value);
	}
}

function getCookie(name)
{
	if(window.localStorage){
		var val = window.localStorage.getItem("ad_lang");
		if (val == null)
			return "Zh";

		return val;
	}
	return "Zh";
}

function EnloadProperties() {
     $.i18n.properties({
        name:'strings',
        path:'../../lib/i18n/',
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
	    path:'../../lib/i18n/',
	    mode:'map',
	    language:'zh',
	    callback:function () {
		$('[data-locale]').each(function(){
			$(this).html($.i18n.prop($(this).data("locale")));
		})
	    }
	})
}
