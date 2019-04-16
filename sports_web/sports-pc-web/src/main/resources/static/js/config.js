/**
 * Created by zhaozijun on 2017/11/28.
 */

var config = {
//  url: ''
};

/*
 * 公共的数据获取接口 GET
 */
function getResultMethods(url, prams, fn) {
    $.ajax({
        url:  url,
        data: prams,
        type: 'get',
        dataType: 'json',
        success: function(d) {
        	if(d.resultCode == 200) {
        		if('function' === typeof fn) fn(d.result);
        	}else {
        		$("#animationTipBox").show();
        		$("#animationTipBox .dec_txt").text(d.resultMsg)
        		 setTimeout(function(){
        			$("#animationTipBox").css("display","none");
        			},
        			3000)

        		/*alert(d.resultMsg);*/
        	}
        }
/*        error: function(err){  console.log(err.response) }*/
    });
};



function postResultMethods(url, prams, fn) {
	$.ajax({
        url:  url,
        data: prams,
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        dataType: 'json',
        success: function(d) {
        	if(d.resultCode == 204){//用户未登陆
        		/*  alert("用户未登陆"); */
         		$("#animationTipBox").show();
        		$("#animationTipBox .dec_txt").text('用户未登陆')
        		 setTimeout(function(){
        			$("#animationTipBox").css("display","none");
        			},
        			3000)
        	 
        	 
        		 window.location.href = "${ctx}/user/login/index?" + createTimestamp();
        		 return 0;
        	}
        	
        	if(d.resultCode == 200) {
        		if('function' === typeof fn) fn(d.result);
        	}else if(d.resultCode == 202){
        		$("#animationTipBox").show();
        		$("#animationTipBox .dec_txt").text(d.resultMsg)
        		 setTimeout(function(){
        			$("#animationTipBox").css("display","none");
        			},
        		3000)
        	}
        }
/*        error: function(err){  console.log(err.response) }*/
    });
}



function getResultMethods1(url, prams, fn) {
    $.ajax({
        url:  url,
        data: prams,
        type: 'get',
        dataType: 'json',
        success: function(d) {
        	if(d.resultCode == 200) {
        		if('function' === typeof fn) fn(d);
        	}else {
        		alert(d.resultMsg);
        	}
        }
/*        error: function(err){  console.log(err.response) }*/
    });
};

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



