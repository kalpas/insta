<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Auth</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/insta.css">
<script type="text/javascript"
	src="http://code.jquery.com/jquery-2.1.3.js"></script>
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<h2>Here you are, ${name}</h2>
			<img src="${image}" alt="some_text">
		</div>
		<div class="alert alert-danger" role="alert">
			<strong>Danger!</strong>Features below are not working. Please stay
			away
		</div>

		<div class="page-header">
			<h1>Build graps</h1>
		</div>
		<div class="row">

			<div class="col-sm-12">
				<a class="btn btn-lg btn-info"
					href="${pageContext.request.contextPath}/graph?access_token=${access_token}&id=${id}">build
					simple graph</a>
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
					<td><span id="left-left"
						class='glyphicon glyphicon-chevron-left'></span> <span
						id="left-right" class='glyphicon glyphicon-chevron-right'></span>
					</td>
					<td><img class='img-thumbnail mutual-picture' /></td>
					<td><span id="right-left"
						class='glyphicon glyphicon-chevron-left'></span> <span
						id="right-right" class='glyphicon glyphicon-chevron-right'></span>
					</td>
				</tr>
			</table>
			<p>
				<a></a>
			</p>
		</div>
	</div>
</body>
</html>