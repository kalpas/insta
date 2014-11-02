<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Auth</title>
</head>
<body>
	<h2>Here you are, ${name}</h2>
	<img src="${image}" alt="some_text">

	<a href="/insta/graph?access_token=${access_token}&id=${id}">build
		graph</a>

	<form action="/insta/mob">
		<input type="text" name="target_id" value="" /> 
		<input type="submit" />
	</form>
</body>
</html>