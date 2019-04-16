/**
 * Created by zhaozijun on 2017/11/28.
 */
var config = {
//  url: ''
};

/*
 * 公共的数据获取接口
 */
function getResultMethods(url, prams, fn, errors) {
    $.ajax({
        url:  url,
        data: prams,
        type: 'get',
        dataType: 'json',
        success: function(d) { if('function' === typeof fn) fn(d)},
		error:function (d) {if( errors && 'function' === typeof errors) errors(d)}
    });
};


//判断设备及平台
function judClient() {
    var userAgent = navigator.userAgent;
    var ua = userAgent.toLowerCase();
    var clientType = '';
    if(ua.match(/IOSAPP/i) == "iosapp") {
        if (screen.height == 812 && screen.width == 375){
            clientType = 'app-iphoneX';
        }else{
            clientType = 'app-ios';
        }

    }else if(ua.match(/ANDROIDAPP/i) == "androidapp"){
        clientType = 'app-android';
    }else if(ua.match(/MicroMessenger/i) == "micromessenger"){
        clientType = 'weixin';
    }else{
        if (ua.match(/iPhone/i) == "iphone") {
            if (screen.height == 812 && screen.width == 375){
                clientType = 'html-iphoneX';
            }else{
                clientType = 'html-ios';
            }
        }else if(ua.match(/Android/i)=="android") {
            clientType = 'html-android';
        }else{
            clientType = 'html';
        }

    }
    return clientType
}
//设备识别
function clientType(type) {
    var u = navigator.userAgent;
    var ua = u.toLowerCase();
    if(type == 'weixin'){
        if(ua.match(/MicroMessenger/i) == "micromessenger"){
            return true
        }else{
            return false
        }
    }else if(type == 'iphoneX'){
        if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)){
            if (screen.height == 812 && screen.width == 375){
                return true
            }else{
                return false
            }
        }else{
            return false
        }
    }else if(type == 'ios'){
        if(!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)){
            return true
        }else{
            return false
        }
    }
}

//获取url中的参数
function getThePrameValue(keys) {
    var thsValue='',output='';
    var urls=location.href.split("?")[1];
    if(urls){
        if(urls.split("&")[1]){
            var kvArr=urls.split("&");
            for(var i=0;i<kvArr.length;i++){
                if(kvArr[i].split("=")[0]==keys){
                    thsValue=kvArr[i].split("=")[1];
                }
            }
        }else if(urls.split("=")[1]){
            var getK=urls.split("=")[0];
            if(getK==keys){
                thsValue=urls.split("=")[1];
            }
        }
    }
    // if(Number(thsValue) !=0 && Number(thsValue) != NaN){
    //     output=Number(thsValue)
    // }
    return thsValue;
}

/*复选框*/

function checkGroupInit(checkGroupId,permitCancel,exeFun){
	var idStr="#"+checkGroupId;
	var check = false;
	$(idStr).on("click",".choose",function () {
		if($(this).hasClass('choose-after')){
			//被选中的不做任何操作
			if(permitCancel && permitCancel==true){
				$(this).removeClass('choose-after');
				if($(this).data("target")){
	    			 $($(this).data("target")).attr("checked",false);
	    		}
			}
			
			check = false;
		}else {
			//没有被选中的，就选中，并且取消其他选中
			$(idStr).find('.choose').each(function(i){
				$(this).removeClass('choose-after');
				if($(this).data("target")){
					$($(this).data("target")).attr("checked", false);
				}
		    });
			$(this).addClass("choose-after");
			if($(this).data("target")){
				$($(this).data("target")).attr("checked",true);
			}
			
			check = true;
		}
		
		if(exeFun && typeof exeFun === 'function'){
			exeFun($(this), check);
		}
	});
};
//加载动画
function loadingBox() {
    this.creat = function () {
        var divs = document.createElement('div');
        divs.className = 'onloadingSport'
        divs.innerHTML='<div><img style="width:0.6rem;height:0.6rem" src="../img/loading.gif" /></div>';
        document.body.appendChild(divs)
    }
    this.close = function () {
        var divs= document.getElementsByClassName('onloadingSport')[0];
        divs.style.display = 'none';
    }
    this.show = function () {
        var divs= document.getElementsByClassName('onloadingSport')[0];
        divs.style.display = 'block';
    }
}
//文本提示框
function loadingTips(tip,timeout) {
	this.tips = tip;
	this.timeout = timeout?timeout:'';
	this.setTimeFun = null;
    this.creat = function () {
        if($('.onloadingTips')){
            $('.onloadingTips').hide()
            $('.onloadingTips').remove()
        }
        var divs = document.createElement('div');
        divs.className = 'onloadingTips'
        divs.innerHTML='<div>'+this.tips+'</div>';
        document.body.appendChild(divs)
        if(this.timeout){
            var _this = this;
            this.setTimeFun = setTimeout(function () {
                divs.style.display = 'none';
                clearTimeout(this.setTimeFun)
                $(divs).remove()
            },this.timeout)
        }
    }
    this.close = function () {
        $('.onloadingTips').hide()
        if(this.setTimeFun){
            clearTimeout(this.setTimeFun)
        }
        $('.onloadingTips').remove()



    }
    // this.show = function () {
    //     var divs= document.getElementsByClassName('onloadingTips')[0];
    //     divs.style.display = 'block';
    // }
}
//内容为空时
function emptyWorn(obj,tip,next_top) {
    this.objs = obj;
    this.text = tip;
    this.next = next_top;
    this.show = function () {
        if($('.emptyWornStyle')[0]){
            $('.emptyWornStyle').hide()
            $('.emptyWornStyle').remove
        }
        var doms = document.createElement('div');
        doms.className = 'emptyWornStyle';
        var wornText = this.text?this.text:'暂无成绩';
        var wornNextText = this.next?this.next:'～快来参赛，获取您的专属证书～';
        doms.innerHTML = '<div><img src="../img/img_no.png"/>' +
            '<div>' +
            '<p class="pfirst">'+wornText+'</p>' +
            '<p class="plast">'+wornNextText+'</p></div>' +
            '</div>';
        $(obj).append(doms);
    };
    this.hide = function () {
        if($('.emptyWornStyle')[0]){
            $('.emptyWornStyle').hide()
            $('.emptyWornStyle').remove
        }

    }
}

function H5DownloadImg(imgdata,names) {
    var type ='png';//图片格式
    //2.0 将mime-type改为image/octet-stream,强制让浏览器下载
    var fixtype=function(type){
        type=type.toLocaleLowerCase().replace(/jpg/i,'jpeg');
        var r=type.match(/png|jpeg|bmp|gif/)[0];
        return 'image/'+r;
    };
    imgdata=imgdata.replace(fixtype(type),'image/octet-stream');
    //3.0 将图片保存到本地
    var savaFile=function(data,filename)
    {
        var save_link=document.createElement( 'a');
        save_link.href=data;
        save_link.download=filename;
        var event=document.createEvent('MouseEvents');
        event.initMouseEvent('click',true,false,window,0,0,0,0,0,false,false,true,false,0,null);
        save_link.dispatchEvent(event);
    };
    var filename=names+new Date().getSeconds()+'.'+type;
    //用当前秒是可以解决重名的问题了 不行就换成毫秒
    savaFile(imgdata,filename);
}

/*单选框*/
function checkBoxGroupInit(checkBoxGroupId,exeFun,type){
	var idStr="#"+checkBoxGroupId;
	var check = false;
		if(type){
			$(idStr).off('click','.choose')
		}
	$(idStr).on("click",".choose",function () {
		var canApply = $(this).attr('canapply');
		var isValid = $(this).attr('isvalid');
		console.log(canApply, isValid)
		if(canApply == 'true' && isValid == 'true') {
			if($(this).hasClass('choose-after')){
				//是删除
				$(this).removeClass('choose-after');
				var check = false;
			}else{
				$(this).addClass('choose-after');
				var check = true;
			}
			if(typeof exeFun == "function"){
				exeFun($(this), check);
			}
		}else {
			console.log('禁用，不能点击');
			
		}
	});
}


function footerAll(flag) {
    this.showFooter = function () {
    	 var first_si = path+'/img/icon_baoming@2x.png'
         var second_si = path+'/img/icon_chengji@2x.png'
         var three_si = path+'/img/icon_geren@2x.png'
         var four_si = path+'/img/icon_bar_fx@2x.png'
         var textColor1 = 'onloadColorBlack'
         var textColor2 = 'onloadColorBlack'
         var textColor3 = 'onloadColorBlack'
     	 var textColor4='onloadColorBlack'
        switch(flag){
        case 1:
            first_si = path+'/img/icon_baoming_a@2x.png'
            second_si = path+'/img/icon_chengji@2x.png'
            three_si = path+'/img/icon_geren@2x.png'
            four_si = path+'/img/icon_bar_fx@2x.png'
            textColor1 = 'onloadColor'
            textColor2 = 'onloadColorBlack'
            textColor3 = 'onloadColorBlack'
        	textColor4='onloadColorBlack'	
            break;
        case 2:
            first_si = path+'/img/icon_baoming@2x.png'
            second_si = path+'/img/icon_chengji_a@2x.png'
            three_si = path+'/img/icon_geren@2x.png'
            four_si = path+'/img/icon_bar_fx@2x.png'
            textColor1 = 'onloadColorBlack'
            textColor2 = 'onloadColor'
            textColor3 = 'onloadColorBlack'
        	textColor4='onloadColorBlack'
            break;
        case 3:
            first_si = path+'/img/icon_baoming@2x.png'
            second_si = path+'/img/icon_chengji@2x.png'
            three_si = path+'/img/icon_geren_a@2x.png'
            four_si = path+'/img/icon_bar_fx@2x.png'
            textColor1 = 'onloadColorBlack'
            textColor2 = 'onloadColorBlack'
            textColor3 = 'onloadColor'
        	textColor4='onloadColorBlack'
            break;
        case 4:
            first_si = path+'/img/icon_baoming@2x.png'
            second_si = path+'/img/icon_chengji@2x.png'
            three_si = path+'/img/icon_geren@2x.png'
            four_si = path+'/img/icon_bar_fx@2x.png'
            textColor1 = 'onloadColorBlack'
            textColor2 = 'onloadColorBlack'
            textColor3 = 'onloadColorBlack'
        	textColor4='onloadColor'
            break;
        default:
            first_si = path+'/img/icon_baoming@2x.png'
            second_si = path+'/img/icon_chengji@2x.png'
            three_si = path+'/img/icon_geren@2x.png'
            four_si = path+'/img/icon_bar_fxa@2x.png'
            textColor1 = 'onloadColorBlack'
            textColor2 = 'onloadColorBlack'
            textColor3 = 'onloadColorBlack'
        	textColor4='onloadColorBlack'
            break;
    }



        var foots = document.createElement('div');
        foots.className = 'onloadingFooter'
    	foots.innerHTML='<div data-index="1" style="width:25%;margin-left:0"><span class="onloadingFooter_si"><img class="onloadingFooter_si" src="'+first_si+'" /></span><span class="'+textColor1+'">大赛报名</span></div>'+
        '<div data-index="2" style="width:25%;margin-left:0"><span class="onloadingFooter_si"><img class="onloadingFooter_si" src="'+second_si+'" /></span><span class="'+textColor2+'">比赛成绩</span></div>'+
      /*  '<div data-index="4"style="width:25%;margin-left:0"><span class="onloadingFooter_si"><img class="onloadingFooter_si" src="'+four_si+'" /></span><span class="'+textColor4+'">发现</span></div>'+*/
        '<div data-index="3" style="width:25%;margin-left:0"><span class="onloadingFooter_si"><img class="onloadingFooter_si" src="'+three_si+'" /></span><span class="'+textColor3+'">个人中心</span></div>'
        document.body.appendChild(foots)
        $('.onloadingFooter>div').click(function (ev) {
            var index = $(this).attr('data-index');
            switch(index){
                case "1":
                    window.location.href=path+ '/game/type'
                    break;
                case "2":
                    window.location.href=path+ '/competitionResults'
                    break;
                case "3":
                    window.location.href=path+ '/ucenter'
                    break;
                case "4":
               	 window.location.href=socialDomain+ '/find'
                   break;   
                default:
                    break;
            }
        })
        // foots.onclick=function (ev) {
        //     var target = ev.target;
        //     while(target !== foots){
        //         if(target.tagName.toLowerCase() == 'div'){
        //             break;
        //         }
        //         target = target.parentNode;
        //
        //         switch(target.innerText.trim()){
        //             case "大赛报名":
        //                 window.location.href=path+ '/game/type'
        //                 break;
        //             case "比赛成绩":
        //                 window.location.href=path+ '/competitionResults'
        //                 break;
        //             case "个人中心":
        //                 window.location.href=path+ '/ucenter'
        //                 break;
        //             default:
        //                 break;
        //         }
        //     }
        // }
    }
    this.hideFooter = function () {
        var foots= document.getElementsByClassName('onloadingFooter')[0];
        foots.style.display = 'none';
        document.body.removeChild(foots)
    }
}



(function (doc, win) {
    var docEl = doc.documentElement, 
    	resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
    	recalc = function () {  
      		var clientWidth = docEl.clientWidth;  
      		if (!clientWidth) return;  
	      	docEl.style.fontSize = 100 * (clientWidth / 750) + 'px';  
	    };  
  
  	if (!doc.addEventListener) return;  
  	win.addEventListener(resizeEvt, recalc, false);  
  	doc.addEventListener('DOMContentLoaded', recalc, false);
  	//设置head
	$(window).load(function () {
		var paddngNum= 0;
        if(getThePrameValue('platform')) {
            if(getThePrameValue('platform') == 'iOS') {
                if (clientType('iphoneX')) {
                    //是iphoneX
                    paddngNum= 0.88+'rem';
                    $('.h5_header').css(
                        'padding-top',paddngNum
                    );
                }
            }else if(getThePrameValue('platform') == 'Android') {
                paddngNum= 0.4+'rem';
                $('.h5_header').css(
                    'padding-top',paddngNum
                );
			}
        }else{
            paddngNum= 0+'rem';
            $('.h5_header').css(
                'padding-top',paddngNum
            );
		}
		if(clientType('weixin')&&clientType('ios')){
            //重写alert方法，alert()方法重写，不能传多余参数
            window.alert = function(name){
                var iframe = document.createElement("IFRAME");
                iframe.style.display="none";
                iframe.setAttribute("src", 'data:text/plain');
                document.documentElement.appendChild(iframe);
                window.frames[0].window.alert(name);
                iframe.parentNode.removeChild(iframe);
            }
        }
        // if(clientType('ios')&&!getThePrameValue('platform')){
        //     $('body').css(
        //         'position','fixed'
        //     );
        // }
    })
})(document, window); 