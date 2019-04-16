var share=function(){
  function initWeichatShare(path,title,link,imgUrl,desc){
	  $.ajax({
			type : "get",
			dataType : "json",
			async : false,
			data:{shareUrl:link.split('#')[0]},
			url : path+"/share/info",
			success : function(data) {
				if(data.resultCode==200){
					var res=data.result;
					var appId=res.appId;
					var timestamp=res.timestamp;
					var noncestr=res.noncestr;
					var signature=res.signature;
					initWeichatConfig(appId,timestamp,noncestr,signature);
					initWeichatWxReady(title,link,imgUrl,desc);
				}
			},
			error : function(data){
				console.log("获取分享参数错误："+data);
			}
         });
  }
  function initWeichatConfig(appId,timestamp,noncestr,signature){
	  wx.config({  
	        debug: false,  
	        appId: appId,  
	        timestamp:timestamp,  
	        nonceStr: noncestr,  
	        signature: signature,  
	        jsApiList: [  
	            // 所有要调用的 API 都要加到这个列表中  
	            'checkJsApi',  
	            'onMenuShareTimeline',  
	            'onMenuShareAppMessage',  
	            'onMenuShareQQ',  
	            'onMenuShareWeibo',  
	            'onMenuShareQZone',  
	        ]  
	    });  
  }
  
   function initWeichatWxReady(title,link,imgUrl,desc){
	    wx.ready(function () {  
	        // 在这里调用 API  
	        //获取“分享到朋友圈”按钮点击状态及自定义分享内容接口  
	        wx.onMenuShareTimeline({  
	            title: title, // 分享标题  
	            link: link, // 分享链接  
	            imgUrl: imgUrl, // 分享图标  
	            success: function () {  
	                // 用户确认分享后执行的回调函数  
	            },  
	            cancel: function () {  
	                // 用户取消分享后执行的回调函数  
	            }  
	        });  
	        //获取“分享给朋友”按钮点击状态及自定义分享内容接口  
	        wx.onMenuShareAppMessage({  
	            title: title, // 分享标题  
	            desc: desc, // 分享描述  
	            link: link, // 分享链接  
	            imgUrl: imgUrl, // 分享图标  
	            type: 'link', // 分享类型,music、video或link，不填默认为link  
	            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空  
	        });  
	        wx.onMenuShareQQ({  
	            title:title, // 分享标题  
	            desc: desc, // 分享描述  
	            link: link, // 分享链接  
	            imgUrl: imgUrl, // 分享图标  
	        });  
	        wx.onMenuShareQZone({  
	            title: title, // 分享标题  
	            desc: desc, // 分享描述  
	            link: link, // 分享链接  
	            imgUrl: imgUrl, // 分享图标  
	        });  
	    });  
  }
	
	return {
		init:function(path,title,link,imgUrl,desc){
			initWeichatShare(path,title,link,imgUrl,desc);
		}
	}
}();