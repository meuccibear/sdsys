/**
 * 界面设计器的菜单栏脚本
 */
		//节目属性
		var d_global_data = {
        	    Rows: [
			{"ID": "ptype","pname": "类型","pvalue": "节目"},
			{"ID": "pname","pname": "名称","pvalue": "节目设置"},
        	    	{"ID": "ptime","pname": "节目时长","pvalue": "60"},
			{"ID": "ppic","pname": "背景图","pvalue": "null"}
        	    ],
        	    Total: 4
        	};
		//图片属性
		var d_pic_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "图片"},
        	        {"ID": "pname","pname": "名称","pvalue": "图片"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
        	        {"ID": "pheight","pname": "高","pvalue": "300"},
			{"ID": "pptime","pname": "切换间隔","pvalue": "10"}
        	    ],
        	    Total: 7
        	};
        	//视频属性
        	var d_video_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "视频"},
            	        {"ID": "pname","pname": "名称","pvalue": "视频"},
            	        {"ID": "pxpos","pname": "左","pvalue": "0"},
            	        {"ID": "pypos","pname": "上","pvalue": "0"},
            	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
            	        {"ID": "pheight","pname": "高","pvalue": "300"},
			{"ID": "pvoice","pname": "音量","pvalue": "50"}
            	    ],
            	    Total: 7
             };
            //音频属性
            var d_voice_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "音频"},
            	        {"ID": "pname","pname": "名称","pvalue": "音频"},
			{"ID": "pxpos","pname": "左","pvalue": "0"},
            	        {"ID": "pypos","pname": "上","pvalue": "0"},
            	        {"ID": "pwidth","pname": "宽","pvalue": "100"},
            	        {"ID": "pheight","pname": "高","pvalue": "100"},
			{"ID": "pvoice","pname": "音量","pvalue": "50"}
            	    ],
            	    Total: 7
             };
            //字幕属性
            var d_sub_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "字幕"},
            	        {"ID": "pname","pname": "名称","pvalue": "字幕"},
            	        {"ID": "pxpos","pname": "左","pvalue": "0"},
            	        {"ID": "pypos","pname": "上","pvalue": "0"},
            	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
            	        {"ID": "pheight","pname": "高","pvalue": "80"},
            	        {"ID": "pcontent","pname": "内容","pvalue": "我是字幕"},
            	        {"ID": "pcolor","pname": "字体颜色","pvalue": "#C4B16F"},
            	        {"ID": "pfontsize","pname": "字体大小","pvalue": "60"}
            	    ],
            	    Total: 9
             };
            //日期属性
            var d_calender_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "日期"},
        	        {"ID": "pname","pname": "名称","pvalue": "日期"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
        	        {"ID": "pheight","pname": "高","pvalue": "300"}
        	    ],
        	    Total: 6
        	};
            //时间属性
        	var d_clock_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "时间"},
        	        {"ID": "pname","pname": "名称","pvalue": "时间"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
        	        {"ID": "pheight","pname": "高","pvalue": "100"}
        	    ],
        	    Total: 6
        	};
	    //天气属性
        	var d_weather_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "天气"},
        	        {"ID": "pname","pname": "城市","pvalue": "深圳"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "500"},
        	        {"ID": "pheight","pname": "高","pvalue": "90"}
        	    ],
        	    Total: 6
        	};
	    //网页属性
        	var d_web_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "网页"},
        	        {"ID": "pname","pname": "网址","pvalue": "http://www.baidu.com"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "400"},
        	        {"ID": "pheight","pname": "高","pvalue": "400"}
        	    ],
        	    Total: 6
        	};
	    //声控属性
        	var d_voiceCtr_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "声控"},
        	        {"ID": "pname","pname": "名称","pvalue": "声控"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "180"},
        	        {"ID": "pheight","pname": "高","pvalue": "180"}
        	    ],
        	    Total: 6
        	};
		//文本属性
            	var d_txt_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "文本"},
            	        {"ID": "pname","pname": "名称","pvalue": "文本"},
            	        {"ID": "pxpos","pname": "左","pvalue": "0"},
            	        {"ID": "pypos","pname": "上","pvalue": "0"},
            	        {"ID": "pwidth","pname": "宽","pvalue": "350"},
            	        {"ID": "pheight","pname": "高","pvalue": "400"},
            	        {"ID": "pcontent","pname": "内容","pvalue": "我是段落"},
            	        {"ID": "pcolor","pname": "字体颜色","pvalue": "#000000"},
            	        {"ID": "pfontsize","pname": "字体大小","pvalue": "60"}
            	    ],
            	    Total: 9
             	};


		//节目属性
		var p_global_data = {
        	    Rows: [
			{"ID": "ptype","pname": "类型","pvalue": "节目"},
			{"ID": "pname","pname": "名称","pvalue": "节目设置"},
        	    	{"ID": "ptime","pname": "节目时长","pvalue": "60"},
			{"ID": "ppic","pname": "背景图","pvalue": "null"}
        	    ],
        	    Total: 4
        	};
        	var p_pic_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "图片"},
        	        {"ID": "pname","pname": "名称","pvalue": "图片"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
        	        {"ID": "pheight","pname": "高","pvalue": "300"},
			{"ID": "pptime","pname": "切换间隔","pvalue": "10"}
        	    ],
        	    Total: 7
        	};
        	//视频属性
        	var p_video_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "视频"},
            	        {"ID": "pname","pname": "名称","pvalue": "视频"},
            	        {"ID": "pxpos","pname": "左","pvalue": "0"},
            	        {"ID": "pypos","pname": "上","pvalue": "0"},
            	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
            	        {"ID": "pheight","pname": "高","pvalue": "300"},
			{"ID": "pvoice","pname": "音量","pvalue": "50"}
            	    ],
            	    Total: 7
             };
            //音频属性
            var p_voice_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "音频"},
            	        {"ID": "pname","pname": "名称","pvalue": "音频"},
            	        {"ID": "pvoice","pname": "音量","pvalue": "50"}
            	    ],
            	    Total: 3
             };
            //字幕属性
            var p_sub_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "字幕"},
            	        {"ID": "pname","pname": "名称","pvalue": "字幕"},
            	        {"ID": "pxpos","pname": "左","pvalue": "0"},
            	        {"ID": "pypos","pname": "上","pvalue": "0"},
            	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
            	        {"ID": "pheight","pname": "高","pvalue": "80"},
            	        {"ID": "pcontent","pname": "内容","pvalue": "我是字幕"},
            	        {"ID": "pcolor","pname": "字体颜色","pvalue": "#eeeeee"},
            	        {"ID": "pfontsize","pname": "字体大小","pvalue": "30"}
            	    ],
            	    Total: 10
             };
            //日期属性
            var p_calender_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "日期"},
        	        {"ID": "pname","pname": "名称","pvalue": "日期"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "100"},
        	        {"ID": "pheight","pname": "高","pvalue": "100"}
        	    ],
        	    Total: 6
        	};
            //时间属性
        	var p_clock_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "时间"},
        	        {"ID": "pname","pname": "名称","pvalue": "时间"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "300"},
        	        {"ID": "pheight","pname": "高","pvalue": "100"}
        	    ],
        	    Total: 6
        	};
	    //天气属性
        	var p_weather_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "天气"},
        	        {"ID": "pname","pname": "城市","pvalue": "深圳"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "500"},
        	        {"ID": "pheight","pname": "高","pvalue": "90"}
        	    ],
        	    Total: 6
        	};
	    //网页属性
        	var p_web_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "网页"},
        	        {"ID": "pname","pname": "网址","pvalue": "http://www.baidu.com"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "500"},
        	        {"ID": "pheight","pname": "高","pvalue": "500"}
        	    ],
        	    Total: 6
        	};
	    //声控属性
        	var p_voiceCtr_data = {
        	    Rows: [
        	    	{"ID": "ptype","pname": "类型","pvalue": "声控"},
        	        {"ID": "pname","pname": "名称","pvalue": "声控"},
        	        {"ID": "pxpos","pname": "左","pvalue": "0"},
        	        {"ID": "pypos","pname": "上","pvalue": "0"},
        	        {"ID": "pwidth","pname": "宽","pvalue": "180"},
        	        {"ID": "pheight","pname": "高","pvalue": "180"}
        	    ],
        	    Total: 6
        	};

		//文本属性
            	var p_txt_data = {
            	    Rows: [
            	    	{"ID": "ptype","pname": "类型","pvalue": "文本"},
            	        {"ID": "pname","pname": "名称","pvalue": "文本"},
            	        {"ID": "pxpos","pname": "左","pvalue": "0"},
            	        {"ID": "pypos","pname": "上","pvalue": "0"},
            	        {"ID": "pwidth","pname": "宽","pvalue": "350"},
            	        {"ID": "pheight","pname": "高","pvalue": "400"},
            	        {"ID": "pcontent","pname": "内容","pvalue": "我是段落"},
            	        {"ID": "pcolor","pname": "字体颜色","pvalue": "#000000"},
            	        {"ID": "pfontsize","pname": "字体大小","pvalue": "30"}
            	    ],
            	    Total: 9
             	};
var createPicCount=0,createVideoCount=0,createMusicCount=0,createSubCount=0,createCalenderCount=0,createClockCount=0;        	
$(function (){
	initDragMenu();
});

var picCreateCount = 0;
var isDrag = 0;

//addType:新增类型，pic等,dp:分辨率
function getInitBound(addType,dp){
	var bound = {};
	bound.width = 300;
	bound.height = 300;
	
	return bound;
}

function initDragMenu(){
	$('div[name="menuBt"]').each(function(){
//		   console.log('xxx==============');
		   var dmb = $(this).ligerDrag({ proxy: 'clone', revert: true , receive: 'div[name="adDesiger"]'});
		   dmb.unbind('DragOver');
		   dmb.unbind('DragEnter');
		   /*
		   dmb.bind('DragEnter', function (receive, source, e){
			   this.cursor = "pointer";
			   
			   $(source).css("z-index", 999);
			   
		   });
		   dmb.bind('DragOver', function (receive, source, e){
			   this.cursor = "pointer";
			   
			   $(source).css("z-index", 999);
			   
		   });
		   */
		   dmb.bind('StartDrag', function (){
			   isDrag = 1;
			   dmb.bind('DragEnter', function (receive, source, e){
				   	  console.log("DragEnter======================");
					  dmb.unbind('DragEnter');
					  if(isDrag == 0){
				   		  return;
				   	  }
					  isDrag = 0;
					  var menuBtId = $(source).attr("id");
					  var uuid = guid();
					  //获取当前的tab
					  var tab_id = designerTab.getSelectedTabItemID();
					  var tid = tab_id.replace("tab_","");
					  var tmpNodeName = null;
					  var tmpNodeType = null;
					  var bound = getInitBound();
					  var addesiger_cont = $("#adDesiger"+tid).html();
					if('calendarMenuBt' == menuBtId){
						if (addesiger_cont.indexOf("calendar") > 0)
						{
							alert($.i18n.prop('dateobjtipMsg'));
							return;
						}
					}else if('weatherMenuBt' == menuBtId){
						if (addesiger_cont.indexOf("weather") > 0)
						{
							alert($.i18n.prop('weatherobjtipMsg'));
							return;
						}
					}else if('clockMenuBt' == menuBtId){
						if (addesiger_cont.indexOf("clock") > 0)
						{
							alert($.i18n.prop('clockobjtipMsg'));
							return;
						}
					}else if('voiceControlMenuBt' == menuBtId){
						if (addesiger_cont.indexOf("voiceControl") > 0)
						{
							alert($.i18n.prop('voiceobjtipMsg'));
							return;
						}
					}
				      if('picMenuBt' == menuBtId){//图片
				    	  $("#adDesiger"+tid).append("<div nametag='图片' tag='"+JSON.stringify(d_pic_data)+"' onmousedown='selObjevent(this)' typeTag='pic' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/pic.png);background-color: #fbf1cd42;width:"+300*scale+"px;height:"+300*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('picTitle');
				    	  tmpNodeType = 'pic';	  
				      }else if('videoMenuBt' == menuBtId){//视频
				    	  $("#adDesiger"+tid).append("<div nametag='视频' tag='"+JSON.stringify(d_video_data)+"' onmousedown='selObjevent(this)' typeTag='video' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/video.png);background-color: #fbf1cd42;width:"+300*scale+"px;height:"+300*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('videoTitle');
				    	  tmpNodeType = 'video';
				      }else if('musicMenuBt' == menuBtId){//音频
				    	  $("#adDesiger"+tid).append("<div nametag='音频' tag='"+JSON.stringify(d_voice_data)+"' onmousedown='selObjevent(this)' typeTag='music' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/music.png);background-color: #fbf1cd42;width:45px;height:45px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('musicTitle');
				    	  tmpNodeType = 'music';
				      }else if('subMenuBt' == menuBtId){//字幕
				    	  $("#adDesiger"+tid).append("<div nametag='字幕' tag='"+JSON.stringify(d_sub_data)+"' onmousedown='selObjevent(this)' typeTag='sub' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/subtitles.png);background-color: #fbf1cd42;width:"+300*scale+"px;height:"+80*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);white-space:nowrap; overflow:hidden; text-overflow:ellipsis;color:#C4B16F;font-size:"+60*scale+"px;'>我是字幕</div>");
				    	  tmpNodeName = $.i18n.prop('subTitle');
				    	  tmpNodeType = 'sub';
				      }else if('calendarMenuBt' == menuBtId){//日期
				    	  $("#adDesiger"+tid).append("<div nametag='日期' tag='"+JSON.stringify(d_calender_data)+"' onmousedown='selObjevent(this)' typeTag='calendar' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/calendar.png);background-color: #fbf1cd42;width:"+300*scale+"px;height:"+300*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('dateTitle');
				    	  tmpNodeType = 'calendar';
				      }else if('clockMenuBt' == menuBtId){//时钟
				    	  $("#adDesiger"+tid).append("<div nametag='时钟' tag='"+JSON.stringify(d_clock_data)+"' onmousedown='selObjevent(this)' typeTag='clock' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/clock.png);background-color: #fbf1cd42;width:"+300*scale+"px;height:"+100*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('clockTitle');
				    	  tmpNodeType = 'clock';
				      }else if('weatherMenuBt' == menuBtId){//天气
				    	  $("#adDesiger"+tid).append("<div nametag='天气' tag='"+JSON.stringify(d_weather_data)+"' onmousedown='selObjevent(this)' typeTag='weather' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/weather.png);background-color: #fbf1cd42;width:"+500*scale+"px;height:"+90*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('weatherTitle');
				    	  tmpNodeType = 'weather';
				      }else if('webMenuBt' == menuBtId){//网页
				    	  $("#adDesiger"+tid).append("<div nametag='网页' tag='"+JSON.stringify(d_web_data)+"' onmousedown='selObjevent(this)' typeTag='web' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/web.png);background-color: #fbf1cd42;width:"+400*scale+"px;height:"+400*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('webTitle');
				    	  tmpNodeType = 'web';
				      }else if('buttonMenuBt' == menuBtId){//按钮
				    	  $("#adDesiger"+tid).append("<div nametag='按钮' onmousedown='selObjevent(this)' typeTag='button' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/button.png);background-color: #fbf1cd42;width:"+100*scale+"px;height:"+100*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = '按钮';
				    	  tmpNodeType = 'button';
				      }else if('voiceControlMenuBt' == menuBtId){//声控
				    	  $("#adDesiger"+tid).append("<div nametag='声控' tag='"+JSON.stringify(d_voiceCtr_data)+"' onmousedown='selObjevent(this)' typeTag='voiceControl' id='"+uuid+"' class='domBtn' style='background-image:url(../../lib/img/editor/fun/voiceControl.png);background-color: #fbf1cd42;width:"+100*scale+"px;height:"+100*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed; cursor: move;border:1px solid rgb(99, 98, 96);'></div>");
				    	  tmpNodeName = $.i18n.prop('voiceTitle');
				    	  tmpNodeType = 'voiceControl';
				      }else if('txtMenuBt' == menuBtId){//文本
				    	  $("#adDesiger"+tid).append("<div nametag='文本' tag='"+JSON.stringify(d_txt_data)+"' onmousedown='selObjevent(this)' typeTag='txt' id='"+uuid+"' class='domBtn' style='overflow: hidden;background-color: #fbf1cd42;width:"+350*scale+"px;height:"+400*scale+"px;background-repeat:no-repeat;background-size:45px 45px;-moz-background-size:45px 45px;position: fixed;;border:1px solid rgb(99, 98, 96);color:#000000;font-size:"+60*scale+"px;'>我是段落</div>");
				    	  tmpNodeName = $.i18n.prop('txtTitle');
				    	  tmpNodeType = 'txt';
				      }
				      
				      if('voiceControlMenuBt' == menuBtId){
						$("#voicePanel").show();
					}else{
						$("#voicePanel").hide();
					}
				      $("#"+uuid).ligerResizable();
				      selObjevent($("#"+uuid));
				      var dragObj1 = $("#"+uuid).ligerDrag({ proxy: false,receive:'div[name="adDesiger"]' });
				      var lm = $("#mainLayout").find(".l-layout-left");
				      dragObj1.bind('DragOver', function (receive, source, e){
				    	  console.log('==============DragOver===========');
			    			var n_type_tag = source.attr('typeTag');
			    			var parentObj = $(source[0]).parents('div[name="adDesiger"]')[0];
			    			var tog = $("#"+source[0].id);
			    			
//			    			console.log("parent --> tpos: "+parentObj.offsetTop+"    lpos："+parentObj.offsetLeft);
			    			var t1 = tog.css("top").replace("px","");
			    			var t2 = parentObj.offsetTop;
			    			var t3 = lm.css("top").replace("px","");
			    			var l1 = tog.css("left").replace("px","");
			    			var l2 = parentObj.offsetLeft;
			    			var l3 = lm.css("width").replace("px","");
			    			
			    			if(n_type_tag != 'music' && n_type_tag != 'voiceControl'){
			    				var tpos = t1 - t2 - 145;
			    				var lpos = l1 - l2 - l3 -17;
//			    				console.log("tpos: "+tpos+"    lpos："+lpos);
			    				if(tpos<0){
			    					pdata.Rows[3].pvalue = 0;
			    					tog.css("top",(145+t2)+"px");
			    				}else{
			    					pdata.Rows[3].pvalue =  tpos;
			    				}
			    				if(lpos<0){
			    					pdata.Rows[2].pvalue =  0;
			    					$("#"+source[0].id).css("left",(parseInt(17)+parseInt(l2)+parseInt(l3))+"px");
			    					return false;
			    				}else{
			    					pdata.Rows[2].pvalue = lpos;
			    				}
			    				propertyGridObj.set({data:pdata});		
			    			}
			    		});
				    //动态添加到节目树里面，但是还没保存到数据库的
			    	  var parentNode = prozTree.getNodeByParam("id", tid, null);
			    	  /*if(parentNode == null){
			    		  var pnode = prozTree.getNodeByParam("id", tid, null);
			    		  var npnode = {id:tid+"_"+tmpNodeType,name:tmpNodeName,isParent:true};
			    		  prozTree.addNodes(pnode,npnode);
			    		  parentNode = prozTree.getNodeByParam("id", tid+"_"+tmpNodeType, null);
			    	  }*/
			    	  var newNode = {id:uuid,name:tmpNodeName};
			    	  prozTree.addNodes(parentNode,newNode);
				      
					});
		   });
		   
	   });
}
