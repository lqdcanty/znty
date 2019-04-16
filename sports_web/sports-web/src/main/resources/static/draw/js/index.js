
//转盘抽奖  /cjqh5/act/bigTurntable/doPrize.action   loginName
//中奖用户 /cjqh5/act/bigTurntable/getPrizeWall.action
//兑奖记录/cjqh5/act/bigTurntable/getPrizeRecord.action   loginName
// 倒计时
var interval = 1000;

$(".cjgz-c").on('click', function() {
	$(".zz").hide();
	$(".zj").hide();
});
//无次数弹框
$(".cjgz-c").on('click', function() {
	$(".wcs").hide();
	$(".zz").hide();
});



jp = {
	'1': ["0", "0.1%加息券"],
	'2': ["1", "0.2%加息券"],
	'3': ["2", "0.3%加息券"],
	'4': ["3", "谢谢参与"],
	'5': ["4", "Iphone8"],
	'6': ["5", "0.5元"],
	'7': ["6", "0.1元"],
	'8': ["7", "10元"],
};
var s,canCj=false,falseMsg=null;
$.ajax({
	url:realPath+'/draw/prize/status',
	type:"POST",
	data:{
		activeCode:activityCode,
	},
	success:function(data){
		if(data.resultCode==200){
			canCj=true
		}else{
			falseMsg=data.resultMsg
		}
	}
})
//抽奖代码
$(function() {
	var $btn = $('.g-lottery-img'); // 旋转的div
	var cishu = 1; //初始次数，由后台传入
	$('#cishu').html(cishu); //显示还剩下多少次抽奖机会
	var isture = 0; //是否正在抽奖
	var clickfunc = function() {
		$.ajax({
			url:realPath+'/draw/prize',
			type:"POST",
			data:{
				activeCode:activityCode,
			},
			success:function(data){
				if(data.resultCode==200 && data.result.status){
					cishu=0;
					var num=data.result.prizeType;
					switch (num) {
						case 1:
							rotateFunc(1, 0, '恭喜您，已获得<br>一等奖');
							break;
						case -1:
							rotateFunc(2, 60, '谢谢参与');
							break;
						case 5:
							rotateFunc(3, 120, '恭喜您，已获得<br>五等奖');
							break;
						case 4:
							rotateFunc(4, 180, '恭喜您，已获得<br>四等奖');
							break;
						case 3:
							rotateFunc(5, 240, '恭喜您，已获得<br>三等奖');
							break;
						case 2:
							rotateFunc(6, 300, '恭喜您，已获得<br>二等奖');
							break;
					}
				}else if(data.resultCode==200 && !data.result.status){
					clearInterval(s)
					$(".texts").html(data.result.reason);
					$(".zz").show();
					$(".jl-tk").show();
				}

			}
		})
	}
	$(".zhizhen").click(function() {
		if(cishu===0){
			$(".texts").html('抱歉<br>你已经抽过奖了！');
			$(".zz").show();
			$(".jl-tk").show();
			return
		}
		if (isture) return; // 如果在执行就退出
		isture = true; // 标志为 在执行
        var angle = 0;
        if(canCj){
        	  s=setInterval(function(){
                  angle +=99;
                  $btn.rotate(angle);
              }, 20);
              clickfunc()
        }else{
        	$(".texts").html(falseMsg);
			// console.log(text)
			$(".zz").show();
			$(".jl-tk").show();
			
        }
	});
	var rotateFunc = function(awards, angle, text) {
		console.log('开始摇奖')
		isture = true;
		$btn.stopRotate();
		clearInterval(s)
		$btn.rotate({
			angle: 0, //旋转的角度数
			duration: 4000, //旋转时间
			animateTo: angle + 1440, //给定的角度,让它根据得出来的结果加上1440度旋转
			callback: function() {
				$(".texts").html(text);
					// console.log(text)
				$(".zz").show();
				$(".jl-tk").show();
				$(".cjgz-c").on('click', function() {
					$(".zz").hide();
					$(".jl-tk").hide();
				});
				$(".ok-img").on('click', function() {
					$(".zz").hide();
					$(".jl-tk").hide();
				});
			}
		});
	};
	$(".ok-img").on('click', function() {
		$(".zz").hide();
		$(".jl-tk").hide();
	});
});