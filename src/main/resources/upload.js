document.querySelector('#file').onchange = function(e) {
	files = this.files;
	var data = new FormData();
	
	jQuery.each(jQuery('#file')[0].files, function(i, file) {
	    data.append('file-'+i, file);
	});
	
	//console.log("data vorm senden");
	//console.log(data);
	
	for (var a = 0; a < files.length; a++)
		
		$.ajax({
			url : 'upload',
			type : "POST",
			data : data,
			processData : false,
			contentType : false,
			success : function(data) {
				$('#imageName').val(data);

				//console.log($('#imageName').val(data));
				//console.log("dataaa:");
				//console.log(data);
			}
		});
}