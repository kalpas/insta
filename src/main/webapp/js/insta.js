function registerHandlers() {
	$('#submitMutual').click(getMutual);
	$('#clearMutual').click(function() {
		$('.mutual-result-container').empty();
		$('#mutual')[0].reset();
	});

	$('#drag-target').on('drop', ondrop).on('dragover', function(event){
		event.preventDefault();
	});

}

function ondrag(event,value){
	event.originalEvent.dataTransfer.setData('text/plain', value);
}

function ondrop(event){
	event.preventDefault();
	var user = event.originalEvent.dataTransfer.getData("text");
	$('#drag-target').find('h4').text(user);
	loadFriends(user);

}

function loadFriends(user) {
	loadConnections(user,function(data){
		var parent = $('#friends-container');
		$.each(data, function(index,value){
			createFriendElement(index,value,parent);
		});
	});
}

function preloadData(user) {
	loadConnections(user,function(data){
		var parent = $('#your-friends-container');
		$.each(data, function(index,value){
			createFriendElement(index,value,parent);
		});
	});
}

function loadConnections(user, onSuccess){
	$.ajax({
		type : "POST",
		url : "/friends/user",
		data : "name=" + user,
		success : function(data) {
			console.log(data);
			onSuccess(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(textStatus);
		}
	});
}

function getMutual() {
	$('.mutual-result-container').empty();
	$('.progress').show().addClass('active');
	$.ajax({
		type : "POST",
		url : "/friends/mutual",
		data : $("#mutual").serialize(),
		success : function(data) {
			console.log(data);

			$.each(data, createMutualElement);
			$('.progress').hide().removeClass('active');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log(textStatus);
			$('.progress').hide().removeClass('active');
		}
	});
}

function createFriendElement(index, value, parent) {
	var elem = $('#friend-template').clone().removeAttr('id').appendTo(
		parent);
	createUserImgElement(elem,value);
	$(elem).find('a').attr('href',
		'http://instagram.com/' + value.user.username + '/').text(
		value.user.username);
		if (value.flags[0]) {
			$(elem).find('#left-right').css('color', 'green');
		}
		if (value.flags[1]) {
			$(elem).find('#left-left').css('color', 'green');
		}
	}

	
	function createUserImgElement(elem,value){
		$(elem).find('img').attr('src', value.user.profile_picture).hover(function(){
			elem.find('span').css('visibility','visible');
		},function(){
			elem.find('span').css('visibility','hidden');
		}).on('dragstart',function(event){
			ondrag(event,value.user.username);
		});
	}

	function createMutualElement(index, value) {
		var elem = $('#mutual-template').clone().removeAttr('id').appendTo(
			$('.mutual-result-container'));
		createUserImgElement(elem,value);
		$(elem).find('a').attr('href',
			'http://instagram.com/' + value.user.username + '/').text(
			value.user.username);
			if (value.flags[0]) {
				$(elem).find('#left-left').css('color', 'green');
			}
			if (value.flags[1]) {
				$(elem).find('#left-right').css('color', 'green');
			}
			if (value.flags[2]) {
				$(elem).find('#right-right').css('color', 'green');
			}
			if (value.flags[3]) {
				$(elem).find('#right-left').css('color', 'green');
			}
		}