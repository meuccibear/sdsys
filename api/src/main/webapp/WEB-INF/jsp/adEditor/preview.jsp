<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<html> 
	<head> 
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
		<title></title>
		<link href="../../lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
	    <link href="../../lib/zTree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
	    <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
	    <script src="../../lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	    <script src="../../lib/comm.js" type="text/javascript"></script>
		<style>
		body,td,select,input{font-family: "微软雅黑", "宋体", Arial, sans-serif; font-size: 12px; }
		.td1{
			background:#F7F7FF;
			text-align:right;
			height:22px;
			text-align:right;
			border:1px solid #C4D8ED;
			padding-right: 4px; 
		}
		.td2{background:#FFFFFF;
		     height:30px;
			 text-align:left;
			 border:1px solid #C4D8ED;
			 padding-left:2px;
		}
		</style>
    <script type="text/javascript">
	$(function ()
        { 
	    	var w_width = ${width};
		var w_height = ${height};
		var o_width = ${orgwidth};
		var o_height = ${orgheight};
		var scale = 0.6;//1  0.95  0.9   0.85   0.8   
		if (o_width < w_width || o_height < w_height){
			var w_scale = o_width/w_width;
			var h_scale = o_height/w_height;
			if (w_scale < h_scale)
				scale = w_scale;
			else
				scale = h_scale;
			}
		else
			scale = 1;
		    
	    	
		load('${pid}',scale);
	});

        function load(pid,scale){
        	$.ajax({
		           type: "GET",
		           url: "${pageContext.request.contextPath}/adEditor/programs/items?pid="+pid,
			   headers:{'X-Access-Token':getToken()},
		           data: null,
		           dataType: "json",
		           success: function(data){
						if(data.data != null){
							var tmpData = null;
							var desigerObj = $("div[name='adDesiger']");
							var tmpres = null;
							var tmpSetting = null;
							var tmpbgsize = null;
							for(var i=0;i<data.data.length;i++){
								tmpData = data.data[i];
								tmpSetting = JSON.parse(tmpData.setting).Rows;
								if(tmpData.type == 'pic'){//初始化图片
									tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/pic.png' : tmpData.res.split(";")[tmpData.res.split(";").length-1];
									tmpbgsize = (tmpData.res == null || tmpData.res == '') ? '45px 45px' : '100% 100%'; 
									desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' typeTag='pic' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: fixed; cursor: move;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '></div>");
								}else if(tmpData.type == 'video'){//初始化视频
									tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/video.png' : tmpData.res.split(";")[tmpData.res.split(";").length-1];
									var videoHtml = "";
									if(tmpData.res != null && tmpData.res != ''){
										videoHtml = "<video autoplay loop src='"+tmpres+"' width='"+tmpSetting[4].pvalue*scale+"px' height='"+tmpSetting[5].pvalue*scale+"px '></video>"; 
									}
									desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' typeTag='video' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute; cursor: move;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '>"+videoHtml+"</div>");
								}else if(tmpData.type == 'sub'){//初始化字幕
									var margintop = 0;
									if (tmpSetting[5].pvalue > tmpSetting[8].pvalue)
										margintop = (tmpSetting[5].pvalue - tmpSetting[8].pvalue)/2*scale;
									var subHtml = "<marquee behavior='scroll' direction='left' align='center' style='text-align:center;margin-top:"+margintop+"px;'><span style='font-weight: bolder;font-size: "+tmpSetting[8].pvalue*scale+"px;color: "+tmpSetting[7].pvalue+";'>"+tmpSetting[6].pvalue+"</span></marquee>";
									desigerObj.append("<div typeTag='sub' id='"+tmpData.id+"' class='domBtn' style='width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '>"+subHtml+"</div>");
								}else if(tmpData.type == 'music'){//音频
									tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/music.png' : tmpData.res.split(";")[tmpData.res.split(";").length-1];
									var audioHtml = "";
									if(tmpData.res != null && tmpData.res != ''){
										audioHtml = "<audio autoplay loop src='"+tmpres+"' width='300px' height='90px' controls='controls'></audio>"; 
									}
									desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' typeTag='music' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute; cursor: move;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '>"+audioHtml+"</div>");
								}else if(tmpData.type == 'web'){//网页
									tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/web.png' : tmpData.res;
									var webHtml = "";
									if(tmpSetting[1].pvalue != null && tmpSetting[1].pvalue != ''){
										webHtml = "<iframe src='"+tmpSetting[1].pvalue+"' width='"+tmpSetting[4].pvalue*scale+"px' height='"+tmpSetting[5].pvalue*scale+"px' frameborder=0 scrolling=auto marginwidth=0 style='background-color: white'></iframe>"; 
									}
									desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' typeTag='web' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute; cursor: move;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '>"+webHtml+"</div>");
								}else if(tmpData.type == 'txt'){//初始化文本
									var margintop = 0;
									if (tmpSetting[5].pvalue > tmpSetting[8].pvalue)
										margintop = scale;
									var txtHtml = "<p  style='margin-top:"+margintop+"px;'><span style='word-wrap:break-word;font-weight: bolder;font-size: "+tmpSetting[8].pvalue*scale+"px;color: "+tmpSetting[7].pvalue+";'>"+tmpSetting[6].pvalue+"</span></p>";
									desigerObj.append("<div typeTag='txt' id='"+tmpData.id+"' class='domBtn' style='overflow: hidden;width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '>"+txtHtml+"</div>");
								}else if(tmpData.type == 'calendar'){//calendar
									tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/calendar_pre.jpg' : tmpData.res;
									desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' typeTag='calendar' id='"+tmpData.id+"' class='domBtn' style='overflow: hidden;background-image:url("+tmpres+");width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute; cursor: move;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '></div>");
								}else if(tmpData.type == 'clock'){//clock
									var nowTime=new Date();
									var month = nowTime.getMonth() + 1;
								        var strNowTime = nowTime.getFullYear() + '-' +
										month + '-' +
										nowTime.getDate() + ' ' +
										nowTime.getHours() + ':' +
										nowTime.getMinutes() + ':' +
										nowTime.getSeconds();

									var clockHtml = "<p><span style='font-weight: bolder;font-size: "+80*scale+"px;color: #C4B16F;'>"+strNowTime+"</span></p>"; 
									
									desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' typeTag='clock' id='"+tmpData.id+"' class='domBtn' style='width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute; cursor: move;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '>"+clockHtml+"</div>");
								}else if(tmpData.type == 'weather'){//weather
									tmpres = (tmpData.res == null || tmpData.res == '') ? '../../lib/img/editor/fun/weather_pre.jpg' : tmpData.res;
									desigerObj.append("<div resref='"+tmpData.res+"' nametag='"+tmpData.name+"' typeTag='weather' id='"+tmpData.id+"' class='domBtn' style='background-image:url("+tmpres+");width:"+tmpSetting[4].pvalue*scale+"px;height:"+tmpSetting[5].pvalue*scale+"px;background-repeat:no-repeat;background-size:"+tmpbgsize+";-moz-background-size:"+tmpbgsize+";position: absolute; cursor: move;top:"+(parseInt(tmpSetting[3].pvalue*scale)+20)+"px ;left:"+(parseInt(tmpSetting[2].pvalue*scale)+20)+"px '></div>");
								}else if(tmpData.type == 'program'){
									var pageCxt = '${pageContext.request.contextPath}';
									$('#adDesiger').css('background-image',"url('"+pageCxt+'/'+tmpSetting[3].pvalue+"')");
								}
							}
						}
							
						                       
		           }
		       });
        }

    </script>
	
	</head> 
	<body>
		<div align="center" style="width: ${wwidth}px;height: ${wheight}px;background-color:#000000;">
			    <div id="adDesiger" name="adDesiger" style="background-image:url();display:inline-block;border: 1px solid #f5850c;width:${orgwidth}px;height: ${orgheight}px;margin: 20px 20px;">
            	
		        </div>
		</div>
	</body> 
</html> 
