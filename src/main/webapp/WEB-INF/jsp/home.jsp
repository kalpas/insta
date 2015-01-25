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
			<div class="row">
				<div class="col-xs-12 mutual-result list-group">
					
				</div>
					
			</div>
			<script type="text/javascript">
			$(function(){
				$('#submitMutual').click(function(){
					$('.mutual-result').empty();
					$.ajax({
					  type: "POST",
					  url: "${pageContext.request.contextPath}/friends",
					  data: $("#mutual").serialize(),
					  success:  function(data){
						console.log(data);
						$.each(data,function(index,value){
							$('.mutual-result').append(
								"<div class='list-group-item'>"+
									"<p>"+
										value.username +
									"</p>"+
									"<img src='"+ value.profile_picture +"' />"+
								"</div>"
								);
						});
						},
						error: function( jqXHR ,  textStatus,  errorThrown ){
						console.log(textStatus);
						}
					});
				});
				$('#clearMutual').click(function(){
					$('.mutual-result').empty();
				});
				
			});

			
			</script>
			

		</body>
		</html>