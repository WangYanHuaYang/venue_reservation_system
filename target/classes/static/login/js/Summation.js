const carousel = layui.carousel;
const layer = layui.layer;

$(document).ready(function(){
	$('#login-btn').click(function(){
		loginSystem($('#usercode').val(),hex_md5($('#password').val()))
	})
})
//建造实例
carousel.render({
	elem: '#test',
	interval: '3000',
	width: '100%', //设置容器宽度
	arrow: 'none', //始终显示箭头
	height: '97vh',
	indicator:'none',
	anim: 'fade' //切换动画方式
})
function loginSystem(usercode,pwd){
	$.ajax({
		url: '/sys-user/login',
		type: 'post',
		async:false,
		data: {
			usercode:usercode,
			pwd:pwd
		},
		success: function (result) {
			if (result.code!=0){
				layer.msg(result.message, {
					icon: 2, //1 成功 2 失败 3 异常
					time: 1000 //2秒关闭（如果不配置，默认是3秒）
				})
			}else {
				sessionStorage.setItem("login-bean",JSON.stringify(result.context.loginBean));
				window.location.href = "/index.html";
			}
		}
	})
}

window.onload = function() {
	//主页
	//游客登录
	// $("#touristBooking").click(function() {
	//
	// 	alert("暂没有该页面!");
	// });
	//
	// // 团队登录
	// $("#teamLoginMethod").click(function() {
	// 	window.location.href = "/login/team_Signin.html"
	//
	// });

	// 学校登录
	$("#schoolLoginMethod").click(function() {
		alert("暂没有该页面!");
		// window.location.href = "/login/school_Signin.html"
	});

	// 教育局登录
	$("#educationBureaLoginMethod").click(function() {
		window.location.href = "/login/school_Signin.html"
		// window.location.href = "/login/BureauofEducation_Signin.html"
	});
	
	//注册跳转///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 学校注册
	$("#schoolToRegister").click(function() {

		window.location.href = "/login/school_register.html"
	});
	// 团队注册
	// $("#teamToRegister").click(function() {
	//
	// 	window.location.href = "/login/team_register.html"
	// });
	
	
	//返回跳转///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 学校注册取消回到学校登录
	$("#back").click(function() {
		window.location.href = "/login/school_Signin.html"
	});
	// 团队注册取消回到团队登录
	// $("#retunTeamSignin").click(function() {
	//
	// 	window.location.href = "/login/team_Signin.html"
	// });
	//logo回到主页
	$(".logo").click(function() {
	
		window.location.href = "/login/index.html"
	});
	
	
}
