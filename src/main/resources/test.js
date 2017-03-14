

//if (window.ConfluenceMobile) {

$(document).ready(function() {
     ConfluenceMobile.contentEventAggregator.on("displayed", function() {
    	 console.log('+++++++++++++++++++++++++++++++++++++++++');	
        
		Zepto(function($){
			$('#file').on('submit', function(e){
		        e.preventDefault();
		        var form = e.target;
		        var data = new FormData(form);
		        $.ajax({
		          url: form.action,
		          method: form.method,
		          processData: false,
		          contentType: false,
		          data: data,
		          success: function(data){
		            $('#imageName').val(data)
		          }
		        })
		      })
		});

	});
});
//}

//(window.ConfluenceMobile ? Zepto : AJS.$);