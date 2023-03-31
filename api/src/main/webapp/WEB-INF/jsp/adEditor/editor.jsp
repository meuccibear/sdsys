<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 

<html>
<head>
    <title>节目制作</title>
    <link href="../../lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="../../lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
    <link href="../../lib/zTree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="../../lib/colorpicker.css" />

 	<style type="text/css">
    </style> 

        
    <style type="text/css">
        body{ padding:1px; margin:0;}
        #mainLayout{  width:100%; margin:40px;  height:400px;margin:0; padding:0;} 
        #accordion1 { height:270px;}
         h4{ margin:20px;}
        .toolbar-bt ul li{display: inline;width: 300px;}
        .toolbar-bt ul li div{border: 1px solid #ffffff;text-align: center;width:60px;float: left;margin-top: 5px;}
        .toolbar-bt ul li div:hover{border: 1px solid #89a8e3;}
        .toolbar-bt ul li div img{width:50px;height: 50px;}
        
        div#rMenu {padding-left:1px;padding-right:1px;z-index:100;position:absolute; visibility:hidden; top:0; background-color: #BED5F3;text-align: left;}
		div#rMenu ul li{
			margin: 1px 0;
			padding: 0px 20px;
			cursor: pointer;
			list-style: none outside none;
			background-color: #f9f9f9;
			width: 100px;
    		height: 30px;
    		line-height: 30px;
		}
		div#rMenu ul li:hover {background-color:#ecd775}
		
		div#pMenu {padding-left:1px;padding-right:1px;z-index:100;position:absolute; visibility:hidden; top:0; background-color: #BED5F3;text-align: left;}
		div#pMenu ul li{
			margin: 1px 0;
			padding: 0px 20px;
			cursor: pointer;
			list-style: none outside none;
			background-color: #f9f9f9;
			width: 100px;
    		height: 30px;
    		line-height: 30px;
		}
		div#pMenu ul li:hover {background-color:#ecd775}
    </style>
</head>
<body style="padding:10px">
        <div id="mainLayout"  >
             <div position="left" id="reslist" title="资源列表" id="leftMenuDiv">
             	<div id="resLayout" >
             		<div position="top" title="资源列表" style="height:200px;overflow:auto;">
				<a class="l-button" onclick="removeTreeItem()" style="float:right;margin-right:10px;display:none;"><lable data-locale="delTitle">删除</lable></a>
		               <ul id="resTree" class="ztree"></ul>
		            </div>
		            <div position="center" id="prolist" title="节目列表" style="height:300px;overflow:auto;">
		               <ul id="proTree" class="ztree"></ul>
		            </div>
             	</div>     
             </div>
            <div position="center" >
            	<div id="contentLayout" style="width: 100%;height: 100%;">
            		<div position="top">
            			<div  id="toolbar"></div>
            			<div class="toolbar-bt">
	            			<ul>
	            				<li><div id="picMenuBt" tag="pic" name="menuBt"><img src="../../lib/img/editor/fun/pic.png" alt="" /></br><lable data-locale="picTitle">图片</lable></div></li>
	            				<li><div id="videoMenuBt" tag="video" name="menuBt"><img src="../../lib/img/editor/fun/video.png" alt="" /></br><lable data-locale="videoTitle">视频</lable></div></li>
	            				<li><div id="musicMenuBt" tag="music" name="menuBt"><img src="../../lib/img/editor/fun/music.png" alt="" /></br><lable data-locale="musicTitle">音频</lable></div></li>
	            				<li><div id="subMenuBt" tag="sub" name="menuBt"><img src="../../lib/img/editor/fun/subtitles.png" alt="" /></br><lable data-locale="subTitle">字幕</lable></div></li>
	            				<li><div id="calendarMenuBt" tag="calendar" name="menuBt"><img src="../../lib/img/editor/fun/calendar.png" alt="" /></br><lable data-locale="dateTitle">日期</lable></div></li>
	            				<li><div id="clockMenuBt" tag="clock" name="menuBt"><img src="../../lib/img/editor/fun/clock.png" alt="" /></br><lable data-locale="clockTitle">时钟</lable></div></li>
	            				<li><div id="weatherMenuBt" tag="weather" name="menuBt"><img src="../../lib/img/editor/fun/weather.png" alt="" /></br><lable data-locale="weatherTitle">天气</lable></div></li>
	            				<li><div id="webMenuBt" tag="web" name="menuBt"><img src="../../lib/img/editor/fun/web.png" alt="" /></br><lable data-locale="webTitle">网页</lable></div></li>
<!-- 	            				<li><div id="buttonMenuBt" tag="button" name="menuBt"><img src="../../lib/img/editor/fun/button.png" alt="" /></br>按钮</div></li> -->
 	            				<li><div id="voiceControlMenuBt" tag="voiceControl" name="menuBt"><img src="../../lib/img/editor/fun/voiceControl.png" alt="" /></br><lable data-locale="voiceTitle">声控</lable></div></li>
						<li><div id="txtMenuBt" tag="txt" name="menuBt"><img src="../../lib/img/editor/fun/timg.jpg" alt="" /></br><lable data-locale="txtTitle">文本</lable></div></li>
	            			</ul>
            			</div>
            		</div>
            		<div position="center" id="adTab" style="width: 100%;height: 100%;background-color:#000000;">
            		</div>
            	</div>
            	
            </div>
	    
            <div position="right" id= "atrrplan" title="属性面板">
            	<div id="propertyRegion"></div>
		<div id="colorPanel" style="display: none; height:210px; width:170px;overflow:auto"">
                  <div class="clearfix"></div><br />
                  <span id="colorpickerholder"></span>
            	</div>
		<div id="voicePanel" style="display: none;">
			<div id="propertyVoice"></div>
			<select name="selprog" id="selprog"  style="display: none;">
		        </select>
			<input type="hidden" name="selprog_id" id="selprog_id" data-ligerid="selprog" value="">
			<a class="l-button" style="width:50px;float:left; margin-left:30px;margin-top:5px;" onclick="addNewRow()"><lable data-locale="addTitle">添加</lable></a>
			<a class="l-button" style="width:50px;float:left; margin-left:10px;margin-top:5px;" onclick="deleteRow()"><lable data-locale="delTitle">删除</lable></a>
		</div>
            	<div id='preDiv' style="display:none;margin-top: 30px;">
            		<div><lable data-locale="previewTitle">资源预览</lable>(<a href="javascript:void(0)" onclick="delBg();"><lable data-locale="delTitle">删除</lable></a>)</div>
            		<div>
            			<img id="preImg" alt="" width="170px" height="120px" style="background-size:100% 100%;-moz-background-size:100% 100%;"/>
            		</div>
            	</div>
            	<div id='preVideoDiv' style="display:none;margin-top: 30px;">
            		<div><lable data-locale="previewTitle">资源预览</lable>(<a href="javascript:void(0)" onclick="delBg();"><lable data-locale="delTitle">删除</lable></a>)</div>
            		<div>
            			<video id="preVideo" alt="" width="170px" src="" height="120px" style="background-size:100% 100%;-moz-background-size:100% 100%;"/>
            		</div>
            	</div>
		<div id='preAudioDiv' style="display:none;margin-top: 30px;">
            		<div><lable data-locale="previewTitle">资源预览</lable></div>
            		<div>
            			<audio id="preAudio" alt="" width="170px" src="" height="120px" style="background-size:100% 100%;-moz-background-size:100% 100%;" controls="controls"/>
            		</div>
            	</div>
            </div>
        </div> 
           
     <div id="rMenu">
		<ul>
			<li id="m_add" onclick="addResource();"><lable data-locale="uploadfileTitle">上传素材</lable></li>
			<li id="m_del" onclick="addFolder();"><lable data-locale="addfolderTitle">新建文件夹</lable></li>
			<li id="m_check" onclick="deleteFolder();"><lable data-locale="delfileTitle">删除文件</lable></li>
			<li id="m_bgc" onclick="addBgi();"><lable data-locale="setbgTitle">设为背景图</lable></li>
			<li id="m_reset" onclick="resetTree();"><lable data-locale="refleshTitle">刷新</lable></li>
		</ul>
	</div>
	<div id="pMenu">
		<ul>
			<li id="m_pcopy" onclick="copyProgram();"><lable data-locale="copyprogramTitle">复制节目</lable></li>
			<li id="m_pdel" onclick="delProgram();"><lable data-locale="delprogramTitle">删除节目</lable></li>
			<li id="m_pdeleteBgi" onclick="deleteBgi();"><lable data-locale="cancelbgTitle">取消背景图</lable></li>
			<li id="m_moveup" onclick="moveupRes();"><lable data-locale="moveupTitle">上移</lable></li>
			<li id="m_movedown" onclick="movedownRes();"><lable data-locale="movedownTitle">下移</lable></li>
		</ul>
	</div>
     <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
     <script src="../../lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
     <script src="../../lib/jquery-plugin/jquery.i18n.properties-min.js"></script>
     <script src="../../lib/language.js"></script>
     <script src="../../lib/zTree/jquery.ztree.core.js" type="text/javascript"></script>
     <script src="../../lib/zTree/jquery.ztree.exedit.js" type="text/javascript"></script>
     <script src="../../lib/colorpicker.js"></script>
     <script type="text/javascript">
			var propertyGridObj;
			var propertyVoiceObj ;
			var dragObj;
			var resizeObj;
			var selObj;//当前选中的组件,jquery对象
			var pdata;
			var voicedata;
			var selprogObj;
			var pageCxt = '${pageContext.request.contextPath}';
			var designerTab;
			var w_width = $(window).width();
			var w_height = $(window).height();
			var c_width = $(".l-layout-center").css("width");
			var c_height = $(".l-layout-center").css("height");
			var scale = 0.457;//1  0.95  0.9   0.85   0.8   
			if(w_width >= 1900 ){
				scale = 0.9;
			}else if(w_width > 1600){
				scale = 0.8;
			}else if(w_width > 1366){
				scale = 0.6;
			}

			jQuery('#colorpickerholder').ColorPicker({
				flat: true,
				onChange: function (hsb, hex, rgb) {
					pdata.Rows[7].pvalue = '#'+hex;
					propertyGridObj.set({data:pdata});
				}
			});
	    function getRequests(){
		var url=location.search;
		var Request = new Object();
		if(url.indexOf("?")!=-1)
		{
			var str = url.substr(1)
			strs = str.split("&");
			for(var i=0;i<strs.length;i++)
			{
				Request[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
			}
		}
		return Request;
	    }

	    function checkNumber(theObj) {
		  var reg = /^[0-9]+.?[0-9]*$/;
		  if (reg.test(theObj)) {
		    return true;
		  }
		  return false;
	    }

            $(function ()
            { 
		var Request = getRequests();
		var tk = Request["tk"];
		if (tk != undefined){
			if(window.localStorage){
				window.localStorage.setItem("ad_sys_ty","appuser");
				window.localStorage.setItem("ad_sys_tk",tk);
			}
		}
		var lang = Request["lang"];
		if (lang != undefined){
			if(lang=="En"){
				 EnloadProperties();
				 setCookie('language','En');
				 $("#reslist").attr("title","Resources List");
				 $("#prolist").attr("title","Programs List");
				 $("#atrrplan").attr("title","Property");
			}else{
				setCookie('language','Zh');
				ZhloadProperties();
				$("#reslist").attr("title","资源列表");
				$("#prolist").attr("title","节目列表");
				$("#atrrplan").attr("title","属性面板");
			} 
		}

                $("#mainLayout").ligerLayout({leftWidth:220,allowRightCollapse:false,onEndResize:function(parm,e){
                	var newLeft = $('.l-layout-drophandle-left').css('left').replace('px','');
                	$('#leftMenuDiv').width(newLeft);
                	$('#resLayout').width(newLeft);
                	$('.l-layout-center').eq(0).width(newLeft);
                }}); 
                $("#contentLayout").ligerLayout({allowTopResize:false,topHeight :100}); 
                $("#resLayout").ligerLayout({topHeight :200});
                $("#toolbar").ligerToolBar({ items: [
                    { text: $.i18n.prop('creatprogramTitle'), click: itemclick_add, icon:'add'},
                    { line:true },
                    { text: $.i18n.prop('delitemTitle'), click: itemclick_del , icon:'delete'},
                    { line:true },
                    { text: $.i18n.prop('saveTitle'), click: itemclick_save , icon:'save'},
                    { line:true },
                    { text: $.i18n.prop('savetotempTitle'), click: itemclick_saveTemplate , icon:'save'},
                    { line:true },
                    { text: $.i18n.prop('preprogramTitle'), click: itemclick_pre , icon:'communication'},
		    { line:true },
                    { text: $.i18n.prop('publishTitle'), click: itemclick_sent , icon:'up'}
                ]
                });
                designerTab = $("#adTab").ligerTab({ 
                	//height:800,
                    	changeHeightOnResize:true, 
			onAfterSelectTabItem : function (tabid){
				if(designerTab.isTabItemExist(tabid)){
					var setting = $("div[tabid='"+tabid+"']").attr("setting");
					if (setting != undefined){
						$.extend(p_global_data.Rows,JSON.parse(setting).Rows);
						propertyGridObj.set({data:p_global_data});
					}
				}
			},
			onBeforeRemoveTabItem : function(tabid){
                		var isok = confirm($.i18n.prop('savetipMsg'));
        			if(isok){
        			    	saveCurTab();
        			 }
                	},
                    	onClose: function(tabid){//关闭的时候提示是否保存
		            	var isok = confirm($.i18n.prop('savetipMsg'));
				if(isok){
					saveCurTab();
				}
                    	}
                     
                });
		propertyGridObj = $("#propertyRegion").ligerGrid({
                    columns: [
                    { display: $.i18n.prop('itemnameTitle'), name: 'pname', width: 60, type: 'string',align:'left' },
                    { display: $.i18n.prop('itemvalTitle'), name: 'pvalue',align:'left',width:110,
                        editor: { type: 'text' }
                    },
                    { name: 'ID', width: 0, type: 'string',hide:true,minWidth:0 }
                    ],
                    enabledSort:false,enabledEdit: true, isScroll: false,usePager:false,allowAdjustColWidth:false,
                    onBeforeEdit: f_onBeforeEdit,
                    onBeforeSubmitEdit: f_onBeforeSubmitEdit,
                    onAfterEdit: f_onAfterEdit,
                    data: pdata,
                    width: '100%'
                });

		propertyVoiceObj = $("#itemkeyTitle").ligerGrid({
		        columns: [
		        { display: $.i18n.prop('itemvalTitle'), name: 'keyname',
		        	editor: { type: 'text' }, width: 50
		        },
		        { display: $.i18n.prop('itemeventTitle'), name: 'event',
		             align: 'left', width: 120,
			     render: function (item)
				    {
					var renderrst = "";
					var eventstr = item.event.split(",");
					if (eventstr.length == 3){
						renderrst = eventstr[2];
					}
				        return renderrst;
				    }   
		        }
		        ],
		        enabledEdit: true,   isScroll: false, usePager:false,allowAdjustColWidth:false,
		        data: voicedata,
			onAfterEdit: f_onVoiceAfterEdit,
		        width: '100%'
		});

		selprogObj = $("#selprog").ligerComboBox({
			width:170,
			valueFieldID: 'selprog_id',
			autocomplete: true,   
                	keySupport: true});

                loadResources();
                rMenu = $("#rMenu");
                pMenu = $("#pMenu");
                loadPrograms(); 

		
            });

	    function deleteRow()
            {
		    var manager = $("#propertyVoice").ligerGetGridManager();
		    var sdl = manager.getSelectedRow();
		    if (sdl != undefined)
			manager.deleteRow(sdl);

		    var data = manager.getData();
		    selObj.attr('exttag',JSON.stringify(data));
            }

            function addNewRow()
            {
		    var selprog = selprogObj.getSelected();
		    var selprog_txt = $("#selprog_txt").val();
		    var manager = $("#propertyVoice").ligerGetGridManager();
		    var setdata = '';
		    if (selprog.text != selprog_txt){
			setdata = '1,' + selprog_txt + ',' + selprog_txt;//网页
		    }
		    else
			setdata = '0,' + selprog.id + ',' + selprog.text;//节目
		    manager.addRow({
		        keyname: $.i18n.prop('userdefTitle'),
		        event: setdata
		    });

		    var data = manager.getData();
		    selObj.attr('exttag',JSON.stringify(data));
            } 

	    function f_onVoiceAfterEdit(e)
            {
		var manager = $("#propertyVoice").ligerGetGridManager();
		var data = manager.getData();
		selObj.attr('exttag',JSON.stringify(data));

            }


            function f_onBeforeEdit(e)
            { 
		if (e.record.ID == "pcolor"){
			$("#colorPanel").show();
		}
                if(e.rowindex ==0) return false;
                return true;
            }
            
            function f_onBeforeSubmitEdit(e)
            { 
                
            	var rowindex = e.rowindex;
		$("#colorPanel").hide();
		if (e.record.ID == "ptime"){
			if (checkNumber(e.value))
				p_global_data.Rows[2].pvalue = e.value;
			else{
				alert($.i18n.prop('inputtimeMsg'));
				propertyGridObj.set({data:p_global_data});
			}	
			return true;
		}
		if (e.record.ID == "pptime"){
			if (!checkNumber(e.value) || e.value < 1 || e.value > 3600){
				alert($.i18n.prop('inputpicintMsg'));
				return true;
			}	
		}
		if (e.record.ID == "pvoice"){
			if (!checkNumber(e.value) || e.value < 0 || e.value > 100){
				alert($.i18n.prop('inputvolMsg'));
				return true;
			}	
		}

            	if(rowindex == 5 || rowindex == 2 || rowindex == 3 ||rowindex == 4){
					if(!/^\d+$/.test(e.value)){
						return false;
					}
                }
            	//处理手工输入
            	if(rowindex == 1){//名称
            		selObj.attr("nametag",e.value);
            		//同步到节目树
            		if(selObj != null){
            			var opNode = prozTree.getNodeByParam("id", selObj.attr("id"), null);
                		opNode.name = e.value;
                		prozTree.updateNode(opNode);
            		}
            	}else if(rowindex == 2){//左
					selObj.css('left',(parseInt(e.value*scale)+17+parseInt($("#mainLayout").find(".l-layout-left").css("width").replace("px",""))+selObj.parents('div[name="adDesiger"]')[0].offsetLeft)+'px');
				}else if(rowindex == 3){//上
					selObj.css('top',(parseInt(e.value*scale)+145+selObj.parents('div[name="adDesiger"]')[0].offsetTop)+'px');
				}else if(rowindex == 4){//宽
					selObj.css('width',e.value*scale+'px');
				}else if(rowindex == 5){//高
					selObj.css('height',e.value*scale+'px');
				}
		    	var typetag = selObj.attr("typetag");
		    	if(typetag == 'sub'){//字幕
					var subHtml = '';

		    			if(rowindex == 6){//字幕内容
	    					pdata.Rows[6].pvalue = e.value;
	    				}else if(rowindex == 7){//字体颜色
	    					selObj.css('color',pdata.Rows[7].pvalue);
	    				}else if(rowindex == 8){//字体大小
						pdata.Rows[8].pvalue = e.value;
	    					selObj.css('font-size',e.value+'px');
	    				}
					var margintop = 0;
					if (pdata.Rows[5].pvalue > pdata.Rows[8].pvalue)
						margintop = (pdata.Rows[5].pvalue - pdata.Rows[8].pvalue)/2*scale;

					subHtml = "<marquee behavior='scroll' direction='left' align='middle' style='text-align:center;margin-top:"+margintop+"px;'><span style='font-weight: bolder;font-size: "+pdata.Rows[8].pvalue*scale+"px;color: "+pdata.Rows[7].pvalue+";'>"+pdata.Rows[6].pvalue+"</span></marquee>"; 
					selObj.html(subHtml);
		    	}
            		if(typetag == 'txt'){//文本
					var txtHtml = '';

		    			if(rowindex == 6){//文本内容
	    					pdata.Rows[6].pvalue = e.value;
	    				}else if(rowindex == 7){//字体颜色
	    					selObj.css('color',pdata.Rows[7].pvalue);
	    				}else if(rowindex == 8){//字体大小
						pdata.Rows[8].pvalue = e.value;
	    					selObj.css('font-size',e.value+'px');
	    				}
					var margintop = 0;
					if (pdata.Rows[5].pvalue > pdata.Rows[8].pvalue)
						margintop = scale;

					txtHtml = "<p style='margin-top:"+margintop+"px;'><span style='word-wrap:break-word;font-weight: bolder;font-size: "+pdata.Rows[8].pvalue*scale+"px;color: "+pdata.Rows[7].pvalue+";'>"+pdata.Rows[6].pvalue+"</span></p>"; 
					selObj.html(txtHtml);
		    	}
				//存储对象
			if (e.record.ID != "pcolor"){
				pdata.Rows[rowindex].pvalue = e.value;
			}
			else
				propertyGridObj.set({data:pdata});

			selObj.attr('tag',JSON.stringify(pdata));
			if(rowindex == 1){//名字
				selObj.attr('nametab',e.value);
			}
                return true;
            }
            //编辑后事件 
            function f_onAfterEdit(e)
            {
		if (e.record.ID == "pcolor"){
			propertyGridObj.updateCell('pvalue', pdata.Rows[7].pvalue, 7);
		}
            }

            
			var addCount = 1;
			var actDialog;
			
			//删除背景图片，视频
			function delBg(){
				var res = selObj.attr("resref");
				var typetag = selObj.attr("typetag");
				var rts = res.split(";");
				var isok = confirm($.i18n.prop('delresourceMsg'));
			    if(isok){
			    	if(typetag == 'pic'){
			    		if(rts.length > 1){
							selObj.css("background-image",'url('+rts[rts.length-2]+')');
							selObj.attr("resref",res.substring(0,res.lastIndexOf(";")));
							$("#preImg").css('background-image','url('+rts[rts.length-2]+')');
						}else if(rts.length == 1){
							selObj.css("background-image",'url()');
							selObj.attr("resref","");
							$("#preDiv").hide();
						}
			    		$("#preDiv").hide();
			    	}else if(typetag == 'video'){
			    		if(rts.length > 1){
							selObj.find("video").attr("src",rts[rts.length-2]);
							selObj.attr("resref",res.substring(0,res.lastIndexOf(";")));
							$("#preVideoDiv").find("video").attr('src',rts[rts.length-2]);
						}else if(rts.length == 1){
							selObj.find("video").css("src",'');
							selObj.attr("resref","");
							$("#preVideoDiv").hide();
						}
			    		$("#preVideoDiv").hide();
			    	}
			    	
			    	//删除对应树节点 
			    	var opNode = prozTree.getSelectedNodes();
			    	prozTree.removeNode(opNode[0]);
			    	//选中背景资源或者组件
			    	var opNode1 = prozTree.getNodeByParam("id", selObj.attr("id"), null);
			    	prozTree.selectNode(opNode1);
			    	
			    }
				
			}
			
			function moveupRes() {
		            var nodes = prozTree.getSelectedNodes();
		            for(var i = 0; i < nodes.length; i++) {
		                var nodesPre = nodes[i].getPreNode();
		                if(!nodes[i].isFirstNode){
					var resref = '';
		                    	prozTree.moveNode(nodesPre,nodes[i],"prev");
					var parenNodes = nodes[i].getParentNode().children;
					for(var p = 0; p < parenNodes.length; p++) {
						resref += parenNodes[p].res;
						if (p+1 < parenNodes.length)
							resref += ";";
					}
					selObj.attr("resref",resref);
		                }else{
		                    alert($.i18n.prop('cannotmoveupMsg'));
		                };
		            };
		        }

			function movedownRes() {
			        var nodes = prozTree.getSelectedNodes();
			        for(var i = 0; i < nodes.length; i++) {
			            var nodesNext = nodes[i].getNextNode();
			            if(!nodes[i].isLastNode){
					var resref = '';
			                prozTree.moveNode(nodesNext,nodes[i],"next");
					var parenNodes = nodes[i].getParentNode().children;
					for(var p = 0; p < parenNodes.length; p++) {
						resref += parenNodes[p].res;
						if (p+1 < parenNodes.length)
							resref += ";";
					}
					selObj.attr("resref",resref);
			            }else{
			                alert($.i18n.prop('cannotmovedownMsg'));
			            };
			        };
			}
     </script> 
    
    
    <script src="../../lib/editor/js/editor_ad_list.js?_v_=20181031" type="text/javascript"></script>
    <script src="../../lib/editor/js/editor_resource_list.js?_v_=20181031" type="text/javascript"></script>
    <script src="../../lib/editor/js/editor_menu.js?_v_=20181031" type="text/javascript"></script>
    
</body>
</html>
