$(function(){
	    $("#btn-change-password").click(function(){
	        $.ajax({
		        url:"/users/change_password",
		        type:"POST",
		        data:$("#form-change-password").serialize(),
		        dtaType:"JSON",
		        success:function(json){
		            if(json.state==200){
		            location.href="login.html";
		            } else{

		            }
		        },
		        error:function(xhr){
//		            alert("セッションが切れましたので、再度ログインしてください！:" + xhr.status);
		            location.href = "login.html";
		        }
		    });
	    });
})