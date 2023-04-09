$(function(){
 $("#form-reg").validationEngine('attach', {promptPosition : "centerRight", scroll: false});
 $("#btn-reg").click(function(){
 		  $.ajax({
 		          url:"/users/reg",
 		          type:"POST",
 		          data:$("#form-reg").serialize(),
 		          dataType:"JSON",
 		          success:function(json){
 		             if(json.state==200){
 		               location.href="login.html";
 		             } else{

                      }
 		          },
 		          error:function(xhr){

 		          }
 		  });
 		});




 });

