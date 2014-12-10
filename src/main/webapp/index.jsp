<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	Redirecting
	<c:redirect url="/auth/login" />
</body>
</html>

<%-- <html>
<head>
<title>Insta test app</title>
</head>
<body>
	<h1>Start page</h1>
	<p>
		<a
			href="https://api.instagram.com/oauth/authorize/?client_id=cdcfc3e943a74d9e84291b97d62d5c02&redirect_uri=http://localhost:8080/insta/auth&response_type=code">Sign In</a>
	</p>
</body>
</html> --%>
