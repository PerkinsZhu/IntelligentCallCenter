var ws_address = "127.0.0.1" , ws_port = "5066" , fs_domain="aaa.com";
//ws_address 如果不是localhost 和 127.0.0.1的话，需要走 wss才可以连通
// fs_domain 是fs为了支持多租户配置的domain
$(document).ready(function(){

	$(document).on("click" , '[data-toggle="soft-function"]' , function(e){
		if($(this).closest(".disabled").length == 0){
			var name = $(this).data("action");
			if(name == "login"){
				uKeFuSoftPhone.input();
			}else if(name == "logout"){
				uKeFuSoftPhone.logout();
			}else if(name == "ready"){
				uKeFuSoftPhone.ready();
			}else if(name == "notready"){
				uKeFuSoftPhone.notready();
			}else if(name == "answer"){
				uKeFuSoftPhone.answer();
			}else if(name == "hungup"){
				uKeFuSoftPhone.hungup();
			}else if(name == "hold"){
				uKeFuSoftPhone.hold();
			}else if(name == "unhold"){
				uKeFuSoftPhone.unhold();
			}
		}
	});
	var ondial = false ;
	$('#softphone-makecall').click(function(){
		if($(this).closest(".disabled").length == 0){
			$('#dialpad').show();
		}
	}) ;
	$('#dialpad .number').on("mousedown" , function(e){
		$(this).css("background-color" , "#1E90FF") ;
	}).on("mouseup" , function(e){
		$(this).css("background-color" , "#FFFFFF") ;
	}).on("click" , function(e){
		$("#dialpad-input").val($("#dialpad-input").val() + $(this).attr("id"));
	});
	$("#makecall").on("click" , function(){
		//这里可以验证手机号是否正确
		/*if(new RegExp(/^(0\d{2,3}-{0,1}\d{5,8}(-{0,1}\d{3,5}){0,1})|(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8})|(1[0-9]{3})$/).test($('#dialpad-input').val())){
			uKeFuSoftPhone.invite($('#dialpad-input').val());
			$('#dialpad-input').val("") ;
			$('#dialpad').hide();
		}else{
			layer.msg("无效的号码，请重新输入");
		}*/

		uKeFuSoftPhone.invite($('#dialpad-input').val());
		$('#dialpad-input').val("") ;
		$('#dialpad').hide();
	});
	$('#dialpad').hover(function(){
		ondial = true ;
	}, function(){
		ondial = false ;
		setTimeout(function(){
			if(ondial == false){
				$('#dialpad').hide();
			}
		} , 1000);
	});
});
var softPhoneUA , currentSession , mediaStream;

var uKeFuSoftPhone = {
	getOptions : function(){
		var options = {
			media: {
				constraints: {
					audio: true,
					video: false
				},
				render: {
					remote: document.getElementById('remoteAudio'),
					local: document.getElementById('localAudio')
				}
			}
		}
		return options ;
	},
	input : function(){
		layer.msg($('#ukefu-login-html').html(), {
		  time: 0 //不自动关闭
		  ,btn: ['登陆', '关闭']
		  ,yes: function(index){
			var extno = $("#extno").val();
			var extpass = $("#extpass").val();
			layer.close(index);
			if(extno!='' && extpass != ''){
				uKeFuSoftPhone.login(extno , extpass);
			}
		  }
		});
		$("#extno").focus();
	},
	login(extno , extpass){
		var config = {
			uri: extno+'@'+fs_domain,
			wsServers: 'ws://'+ws_address+':'+ws_port,
			authorizationUser: extno,
			password: extpass,
			allowLegacyNotifications:true,
			autostart:true,
			register: false
		};
		softPhoneUA = new SIP.UA(config);
		softPhoneUA.on('invite', function (session) {
			uKeFuSoftPhone.status.callIn();
			currentSession = session ;

			uKeFuSoftPhone.sessionEvent(session);

		})
		softPhoneUA.on('connecting', function (args) {
			console.log("connecting");
		});
		softPhoneUA.on('connected', function () {
			if(softPhoneUA.isRegistered()){
				uKeFuSoftPhone.status.ready();
			}else{
				uKeFuSoftPhone.status.notready();
			}
		});
		softPhoneUA.on('unregistered', function (response, cause) {
			uKeFuSoftPhone.status.notready();
		});
		softPhoneUA.on('registered', function () {
			uKeFuSoftPhone.status.ready();
		})
		softPhoneUA.on('disconnected', function () {
			uKeFuSoftPhone.status.logout();
		})
		uKeFuSoftPhone.status.login();
		var mediaConstraints = {
			audio: true,
			video: true
		};
	},
	ready:function(){
		console.log("===ready===")
		softPhoneUA.register({register:true});
	},
	invite:function(pnumber){
		console.log("===invite===")
		currentSession = softPhoneUA.invite(pnumber+"@"+ws_address , uKeFuSoftPhone.getOptions());
		uKeFuSoftPhone.sessionEvent(currentSession);
		uKeFuSoftPhone.status.callOut();
	},
	logout:function(){
		console.log("===logout===")
		softPhoneUA.stop({register:true});
	},
	answer:function(){
		console.log("===answer===")
		if(currentSession){			
			currentSession.accept(uKeFuSoftPhone.getOptions());
		}
	},
	hungup:function(){
		console.log("===hungup===")
		if(currentSession){
			if(currentSession.hasAnswer){
				currentSession.bye();
			}else if(currentSession.isCanceled == false){
				currentSession.cancel();
			}else{
				currentSession.reject();
			}
		}
	},
	hold:function(){
		if(currentSession && currentSession.hasAnswer){
			currentSession.hold();
		}
	},
	unhold:function(){
		if(currentSession && currentSession.hasAnswer){
			currentSession.unhold();
		}
	},
	notready:function(){
		console.log("===notready===")
		softPhoneUA.unregister();
	},
	sessionEvent:function(session){
		session.on("rejected" , function (response, cause){
			uKeFuSoftPhone.status.hungup();
		});
		session.on("bye" , function (response, cause){
			uKeFuSoftPhone.status.hungup();
		});
		session.on("hold" , function (response, cause){
			uKeFuSoftPhone.status.hold();
		});
		session.on("unhold" , function (response, cause){
			uKeFuSoftPhone.status.unhold();
		});
		session.on("accepted" , function (response, cause){
			uKeFuSoftPhone.status.accepted();
		});
		session.on("cancel" , function (response, cause){
			uKeFuSoftPhone.status.hungup();
		});
		uKeFuSoftPhone.status.initCallStatus(session) ;
	},
	status : {
		login:function(){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').addClass("disabled");
			
			$('#softphone-status .hold').addClass("disabled").show();
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').addClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();

			$('#softphone-status .ready').removeClass("disabled").show();
			$('#softphone-status .notready').addClass("disabled").hide();
		},
		ready : function(){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').addClass("disabled");
			
			$('#softphone-status .hold').addClass("disabled").show();
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').removeClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();
			$('#softphone-status .ready').addClass("disabled").hide();
			$('#softphone-status .notready').removeClass("disabled").show();
		},
		notready : function(){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').addClass("disabled");

			$('#softphone-status .hold').addClass("disabled").show();
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').addClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();

			$('#softphone-status .ready').removeClass("disabled").show();
			$('#softphone-status .notready').addClass("disabled").hide();

		},
		callIn : function(){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').removeClass("disabled");
			$('#softphone-hungup').removeClass("disabled");
			
			$('#softphone-status .hold').addClass("disabled").show();;
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').addClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();

			$('#softphone-status .ready').addClass("disabled").hide();
			$('#softphone-status .notready').addClass("disabled").show();
		},
		callOut : function(){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').removeClass("disabled");
			
			$('#softphone-status .hold').addClass("disabled").show();;
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').addClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();

			$('#softphone-status .ready').addClass("disabled").hide();
			$('#softphone-status .notready').addClass("disabled").show();
		},
		hungup : function(){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').addClass("disabled");
			
			$('#softphone-status .hold').addClass("disabled").show();;
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').removeClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();
			
			$('#softphone-status .ready').addClass("disabled").hide();
			$('#softphone-status .notready').removeClass("disabled").show();
		},
		accepted : function (response, cause){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').removeClass("disabled");
			
			$('#softphone-status .hold').removeClass("disabled").show();
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').removeClass("disabled");
			$('#softphone-makecall').addClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();

			$('#softphone-status .ready').addClass("disabled").hide();
			$('#softphone-status .notready').addClass("disabled").show();
		},
		hold : function (){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').addClass("disabled");
			
			$('#softphone-status .hold').addClass("disabled").hide();
			$('#softphone-status .unhold').removeClass("disabled").show();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').removeClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();

			$('#softphone-status .ready').addClass("disabled").hide();
			$('#softphone-status .notready').addClass("disabled").show();
		},
		unhold : function (){
			$('.soft-function,.status').removeClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').removeClass("disabled");
			
			$('#softphone-status .hold').removeClass("disabled").show();
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').removeClass("disabled");
			$('#softphone-makecall').addClass("disabled");

			$('#ukefu_account .login').hide();
			$('#ukefu_account .logout').show();

			$('#softphone-status .ready').addClass("disabled").hide();
			$('#softphone-status .notready').addClass("disabled").show();
		},
		logout : function (){	
			$('.status').addClass("disabled");	
			$('#softphone-answer').addClass("disabled");
			$('#softphone-hungup').addClass("disabled");
			
			$('#softphone-status .hold').addClass("disabled").show();
			$('#softphone-status .unhold').addClass("disabled").hide();

			$('#softphone-trans').addClass("disabled");
			$('#softphone-makecall').addClass("disabled");

			$('#softphone-status .ready').addClass("disabled").show();
			$('#softphone-status .notready').addClass("disabled").hide();

			$('#ukefu_account .login').removeClass("disabled").show();
			$('#ukefu_account .logout').addClass("disabled").hide();
		},
		initCallStatus:function(session , called){
			$('#caller .number').text(session.request.from.uri.user);
			if(called){
				$('#called .number').text(called);
			}
		}
	}
	
}
function getUserMediaSuccess (stream) {
	console.log('getUserMedia succeeded', stream)
	mediaStream = stream;
}

function getUserMediaFailure (e) {
	console.error('getUserMedia failed:', e);
}

