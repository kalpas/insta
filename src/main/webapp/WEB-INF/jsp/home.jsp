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
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/insta.js"></script>
</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">   
				<img src="${image}" alt="${name}" id="userimage" class="img-thumbnail">       
				<a class="navbar-brand">Welcome, ${name}</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">				
				<form class="navbar-form navbar-left" id="mutual">
					<div class="form-group">
						<input class="form-control" type="text" name="firstUser"
						id="firstUser" placeholder="First user">
					</div>
					<div class="form-group">
						<input class="form-control" type="text" name="secondUser"
						id="secondUser" placeholder="Second user">
					</div>
					<div class="form-group">
						<button type="button" class="btn btn-success" id="submitMutual">Get
							mutual</button>
							<button type="button" class="btn btn-info" id="clearMutual">Clear</button>
						</div>
					</form>					
				</div><!--/.nav-collapse -->
			</div>
		</nav>
		<div class="container">
			<div class="col-lg-3">
				<div class="panel panel-default">				
					<div class="panel-heading">
						<h4>Your friends</h4>
					</div>
					<div class="panel-body">
						<div class="row friends" id="your-friends-container"></div>
					</div>
				</div>	
			</div>
			<div class="col-lg-6 main-column">				
				<div class="page-header">
					<h3>Mutual Friends</h3>
				</div>
				<div class="row">

				</div>
				<div class="progress">
					<div class="progress-bar progress-bar-striped" role="progressbar"
					aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"
					style="width: 100%">
					<span class="sr-only">Please wait</span>
				</div>
			</div>
			<div class="row mutual-result-container"></div>			
		</div>


		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-heading" id="drag-target">
					<h4>Drop user here</h4>
				</div>
				<div class="panel-body">
					<div class="row friends" id="friends-container"></div>
				</div>
			</div>
		</div>
	</div>


	<!-- Footer -->
	<hr>
	<footer>
		<p>&copy; kalpas 2015</p>
	</footer>

	<!-- Templates-->
	<div class='col-xs-3' id="mutual-template">
		<table>
			<tr>
				<td><span id="left-left"
					class='glyphicon glyphicon-chevron-left'></span> <span
					id="left-right" class='glyphicon glyphicon-chevron-right'></span></td>
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


		<div class='col-sm-4' id="friend-template">
			<table>
				<tr>
					<td><span id="left-left"
						class='glyphicon glyphicon-chevron-left'></span><p><span
						id="left-right" class='glyphicon glyphicon-chevron-right'></span></td>
						<td><img class='img-thumbnail friend-picture' /></td>					
					</tr>
				</table>
				<a></a>

			</div>




			<!-- Scripts -->
			<script type="text/javascript">
				$(function() {
					registerHandlers();
					preloadData("${name}");
					$('#userimage').on('dragstart',function(event){
						ondrag(event,'${name}');
					});
				});				
			</script>

			<!-- Analytics -->
			<script>
				(function(i, s, o, g, r, a, m) {
					i['GoogleAnalyticsObject'] = r;
					i[r] = i[r] || function() {
						(i[r].q = i[r].q || []).push(arguments)
					}, i[r].l = 1 * new Date();
					a = s.createElement(o), m = s.getElementsByTagName(o)[0];
					a.async = 1;
					a.src = g;
					m.parentNode.insertBefore(a, m)
				})(window, document, 'script',
				'//www.google-analytics.com/analytics.js', 'ga');

				ga('create', 'UA-58948245-1', 'auto');
				ga('send', 'pageview');
			</script>
		</body>
		</html>