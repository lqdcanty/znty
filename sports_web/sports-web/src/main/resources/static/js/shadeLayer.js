/**
 * Created by EFIDA on 2016/11/22.
 */
(function($) {
    var notice=function (poster) {
        var $this=this;
        this.modle_box=$('<div class="modle_box">');
        this.modle_container=$('<div class="modle_container" style="background:rgba(0, 0, 0, 0.6) none repeat scroll 0 0 !important;">');
        this.modle_content=$('<div class="modle_content">');
        this.modle_content.html(
            '<p class="ly-tc">提示</p>'+
            '<div class="ly-tc mt70 text-big" style="color: #666666;font-size: 0.26rem;margin-bottom: 0.4rem;margin-top: 0.1rem;text-align: center;">'+poster.content+'</div>'+
            '<div class="ly-tc mt55 btn-wrap"><a class="mr10 no-btn">取消</a><a class="ml10 yes-btn">确定</a></div>'
        );
        this.initialize(poster);
    };

    notice.prototype= {
        initialize:function (poster){
            var _this=this;
            $(this.modle_box).css({"position":"fixed","top":0, "right":0, "bottom":0, "left":0, "z-index":1040, "width":"100%", "height":"100%"});
            $(this.modle_container).css({"position":"relative", "width":"100%", "height": "100%"});
            $(this.modle_content).css({"position":"absolute", "top":"50%", "left":"50%", "width":"4.9rem", "background":"#fff", "height":"2.4rem", "margin-top":"-1.2rem", "margin-left":"-2.45rem","font-size": "16px","border-radius": "8px"});
            $(this.modle_content).find("p").css({"margin":"0 auto","color":"#333","text-align":"center","font-size":"0.3rem","margin-top":"0.4rem"});
            $(this.modle_content).find("a").css({"display":"inline-block","color":"#FF8F00","width":"48%","height":"0.7rem","cursor":"pointer","text-align":"center","line-height":"0.7rem"});
            $(this.modle_content).find(".no-btn").css({"border-right":"1px solid #E2E2E2"})
            $(this.modle_content).find(".btn-wrap").css({"border-top":"1px solid #E2E2E2","position": "absolute","bottom": "0","width": "100%"})
            $(this.modle_content).find(".no-btn").click(function () {
                _this.closeModle();
            });
            $(this.modle_content).find("button").click(function () {
                _this.closeModle();
            });
            $(this.modle_content).find(".yes-btn").click(function () {
                poster.click();
            })
        },
        openMdole:function () {
            $(this.modle_box).append(this.modle_container);
            $(this.modle_container).append(this.modle_content);
            $("body").append(this.modle_box)
        },
        closeModle:function () {
            $(this.modle_box).remove();
        }
    };
    var modle;
    notice.open=function (poster) {
        if(modle){
            modle.closeModle()
        }
        modle=new notice(poster);
        modle.openMdole();
    };
    notice.close=function () {
        modle.closeModle()
    };
    window["$notice"]=notice;
})(jQuery);