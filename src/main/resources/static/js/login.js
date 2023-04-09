$(function(){
	    $("#btn-login").click(function(){
	        $.ajax({
		        url:"/users/login",
		        type:"POST",
		        data:$("#form-login").serialize(),
		        dtaType:"JSON",
		        success:function(json){
		            if(json.state==200){
		            location.href="index.html";
		            }
		        },
		        error:function(xhr){

		        }
		    });
	    });
})