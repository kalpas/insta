<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login page</title>
</head>
<body>
	<p>
		<a href="https://api.instagram.com/oauth/authorize/?client_id=${insta_client_id}&redirect_uri=${insta_redirect_uri}&response_type=${insta_response_type}">Sign
			In</a>
	</p>
</body>
</html>