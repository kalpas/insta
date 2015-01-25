<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Login page</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/insta.css">
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<h1>Welcome</h1>
			<p>Please login with your instagram account</p>
			<a class="btn btn-lg btn-success" href="https://api.instagram.com/oauth/authorize/?client_id=${insta_client_id}&redirect_uri=${insta_redirect_uri}&response_type=${insta_response_type}">Sign
				In</a>
		</div>
	</div>
</body>
</html>