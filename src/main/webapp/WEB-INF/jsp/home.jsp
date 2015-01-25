<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Auth</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/insta.css">
	<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.3.js"></script>
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<h2>Here you are, ${name}</h2>
			<img src="${image}" alt="some_text">
		</div>
		<div class="page-header">
			<h1>Mutual Friends</h1>
		</div>	
		<div class="row">
			<form id="mutual">
				<div class="form-group col-sm-6">
					<input class="form-control" type="text" name="firstUser" id="firstUser">
				</div>
				<div class="form-group col-sm-6">
					<input class="form-control" type="text" name="secondUser" id="secondUser">
				</div>
				<div class="form-group col-sm-12">
					<button type="button" class="btn btn-success" id="submitMutual" >Get mutual</button>
					<button type="button" class="btn btn-info" id="clearMutual" >Clear</button>
				</div>
			</form>				
		</div>
		<div class="progress">
			<div class="progress-bar progress-bar-striped" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
				<span class="sr-only">Please wait</span>
			</div>
		</div>
		<div class="row mutual-result-container">				
		</div>
		<p>
		<hr>
		<div class="alert alert-danger" role="alert">
        <strong>Danger!</strong>Features below are not working. Please stay away
      	</div>

		<div class="page-header">
			<h1>Build graps</h1>
		</div>
		<div class="row">

			<div class="col-sm-12">
				<a class="btn btn-lg btn-info" href="${pageContext.request.contextPath}/graph?access_token=${access_token}&id=${id}">build simple
					graph</a>
				</div>

			</div>

			<div class="page-header">
				<h1>Build flashmob three</h1>
			</div>	
			<div class="row">
				<div class="col-sm-12">
					<form action="${pageContext.request.contextPath}/mob">
						<div class="form-group">
							<input class="form-control" type="text" name="target_id" value="" />
						</div>
						<div class="form-group">
							<input class="btn btn-success" type="submit" />
						</div>
					</form>
				</div>
			</div>

			<hr>
			<footer>
       			<p>&copy; kalpas 2015</p>
      		</footer>


			<div class='col-xs-3' id="mutual-template">	
				<table>
					<tr>
						<td >
							<span id="left-left" class='glyphicon glyphicon-chevron-left' ></span>
							<span id="left-right" class='glyphicon glyphicon-chevron-right'></span>
						</td>
						<td >
							<img class='img-thumbnail mutual-picture' />						
						</td>
						<td >
							<span id="right-left" class='glyphicon glyphicon-chevron-left'></span>
							<span id="right-right" class='glyphicon glyphicon-chevron-right'></span>
						</td>
					</tr>					
				</table>
				<p><a></a></p>
			</div>

			<script type="text/javascript">
				$(function(){
					$('#submitMutual').click(function(){
						$('.mutual-result-container').empty();
						$('.progress').show().addClass('active');
						$.ajax({
							type: "POST",
							url: "${pageContext.request.contextPath}/friends",
							data: $("#mutual").serialize(),
							success:  function(data){
								console.log(data);

								$.each(data,function(index,value){
									var elem = $('#mutual-template').clone().removeAttr('id').appendTo($('.mutual-result-container'));
									$(elem).find('img').attr('src',value.user.profile_picture);
									$(elem).find('a').attr('href','http://instagram.com/'+ value.user.username + '/').text(value.user.username);	
									if(value.flags[0]){
										$(elem).find('#left-left').css('color','green');
									}
									if(value.flags[1]){
										$(elem).find('#left-right').css('color','green');	
									}							
									if(value.flags[2]){
										$(elem).find('#right-right').css('color','green');
									}							
									if(value.flags[3]){
										$(elem).find('#right-left').css('color','green');
									}														
								});
								$('.progress').hide().removeClass('active');
							},
							error: function( jqXHR ,  textStatus,  errorThrown ){
								console.log(textStatus);
								$('.progress').hide().removeClass('active');
							}
						});
				});
				$('#clearMutual').click(function(){
					$('.mutual-result-container').empty();
				});

				});
			</script>
</body>
</html>