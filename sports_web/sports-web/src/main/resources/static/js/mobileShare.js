function mobileShare(path,shareData) {
    if(clientType('weixin')){
        $.get(path+"/share/info",{"shareUrl":window.location.href},function(data,status){
            wx.config({
                debug: false,
                appId: data.result.appId,
                timestamp:data.result.timestamp,
                nonceStr:data.result.noncestr,
                signature:data.result.signature,
                jsApiList: [
                    // "checkJsApi",
                    'onMenuShareQZone',
                    'onMenuShareQQ',
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage'
                    // 'hideAllNonBaseMenuItem',
                    // 'showMenuItems',
                    // 'showOptionMenu'

                    // "hideOptionMenu"
                ]
            });
            wx.ready(function(){
                wx.onMenuShareAppMessage({
                    title: shareData.title,
                    desc: shareData.desc,
                    link: shareData.link,
                    imgUrl: shareData.imgUrl,
                    success: function() {
                        var tips = new loadingTips('分享成功',2500);
                        tips.creat();
                    },
                    cancel: function() {
                        var tips = new loadingTips('取消分享',2500);
                        tips.creat();
                    }
                });
                //分享到朋友圈
                wx.onMenuShareTimeline({
                    title: shareData.title,
                    link: shareData.link, // 分享链接
                    imgUrl: shareData.imgUrl, // 分享图标
                    success: function () {
                        var tips = new loadingTips('分享成功',2500);
                        tips.creat();
                    },
                    cancel: function () {
                        var tips = new loadingTips('取消分享',2500);
                        tips.creat();
                    }
                });
                //分享到QQ
                wx.onMenuShareQQ({
                    title: shareData.title, // 分享标题
                    desc: shareData.desc, // 分享描述
                    link: shareData.link, // 分享链接
                    imgUrl:shareData.imgUrl, // 分享图标
                    success: function () {
                        var tips = new loadingTips('分享成功',2500);
                        tips.creat();
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                        var tips = new loadingTips('取消分享',2500);
                        tips.creat();
                    }
                });
                wx.onMenuShareQZone({
                    title: shareData.title, // 分享标题
                    desc: shareData.desc, // 分享描述
                    link: shareData.link, // 分享链接
                    imgUrl: shareData.imgUrl, // 分享图标
                    success: function () {
                        var tips = new loadingTips('分享成功',2500);
                        tips.creat();
                    },
                    cancel: function () {
                        var tips = new loadingTips('取消分享',2500);
                        tips.creat();
                    }
                });

                wx.error(function(res){
                    var tips = new loadingTips(res,2500);
                    tips.creat();
                });
                // wx.hideAllNonBaseMenuItem();
                // wx.showMenuItems({
                //     menuList: [
                //         "menuItem:share:appMessage",
                //         "menuItem:share:timeline",
                //         "menuItem:share:qq",
                //         "menuItem:share:QZone"
                //     ], // 要显示的菜单项，所有menu项见附录3
                //     success:function () {
                //         console.log( 'yes' );
                //     },
                //     fail:function (res) {
                //         console.log( 'no' );
                //     }
                // });
                // wx.showOptionMenu()
                // wx.hideOptionMenu();/***隐藏分享菜单****/
            });
        });


        // wx.hideMenuItems({
        //     menuList: [] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
        // });

    }else{
        soshm('#nativeShare', {
            // 分享的链接，默认使用location.href
            url: shareData.link,
            // 分享的标题，默认使用document.title
            title: shareData.title,
            // 分享的摘要，默认使用<meta name="description" content="">content的值
            digest: shareData.desc,
            // 分享的图片，默认获取本页面第一个img元素的src
            pic: shareData.imgUrl,
            // 默认显示的网站为以下六个个,支持设置的网站有
            // weixin,weixintimeline,qq,qzone,yixin,weibo,tqq,renren,douban,tieba
            sites:['weibo', 'qq', 'qzone']
        });
        // 'weixin', 'weixintimeline',

        $('.nativeShareCancel').click(function () {
            $("#nativeShare").css('display','none')
            $('.share_mask_box').hide()
        })
        $(".share_mask_box").click(function () {
            $("#nativeShare").css('display','none')
            $('.share_mask_box').hide()
        })

    }

}