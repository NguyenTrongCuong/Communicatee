const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

let stompClient = null;
let notificationClient = null;
let messageNotificationClient = null;
let messageCoverClient = [];

let messageFrom = [];
let joinedRoom = [];
let loadedRoom = [];

let isSubscribingToNotificationQueue = false;

let isSubscribingToMessageNotificationQueue = false;

let communicatorEmail = document.getElementById("communicator-email").value;
let communicatorFirstName = document.getElementById("communicator-first-name").value;
let communicatorLastName = document.getElementById("communicator-last-name").value;

function connect() {
	const webSocketHandshakeProcessingUrl = "/process-web-socket-handshake";
	const sockJSClient = new SockJS(webSocketHandshakeProcessingUrl);
	stompClient = Stomp.over(sockJSClient);
	stompClient.connect({}, function() {
		subscribeToNotificationCountQueue();
		subscribeToMessageCountQueue();
	});
}

function subscribeToMessageCountQueue() {
	$("input:hidden[name=communicator-room]").each(function() {
		let roomId = $(this).val();
		performSubscribeToMessageCountQueue("/topic/message-count-" + roomId, roomId.toString());
		joinedRoom.push(roomId.toString());
	});
}

function performSubscribeToMessageCountQueue(destination, id) {
	stompClient.subscribe(destination, function(response) {
		const message = JSON.parse(response.body);
		const roomId = message.roomId;
		if(!isSubscribingToMessageNotificationQueue && !messageFrom.includes(roomId)) {
			const numberOfMessages = 1 + parseInt($("#badge1").text());
			$("#badge1").text(numberOfMessages);
			if($("#badge1").css("visibility") == "hidden") {
				$("#badge1").css("visibility", "visible");
			}
			messageFrom.push(roomId);
		}
	}, 
	{
		"id": communicatorEmail + "-" + id + "-" + "count",
		"auto-delete": false,
		"durable": true
	});
}

function subscribeToNotificationQueue() {
	isSubscribingToNotificationQueue = true;
	notificationClient = stompClient.subscribe("/queue/" + communicatorEmail + "-notification", function (data) {
		const notification = JSON.parse(data.body);
		const notificationId = notification.notificationId;
		$("#notification-table-cover").prepend("<div class='notification-element' id='notification-cover-id-" + notificationId + "'>" + notification.notificationContent + "</div>");
		if(notification.notificationType == "Friend Request") {
			$("#notification-cover-id-" + notificationId).append("<button id='accept-button-id-" + notificationId + "'" + " onclick='acceptOrReject(" + notificationId + ", \"accepted\")'>Accept</button>   <button id='reject-button-id-" + notificationId + "'" + " onclick='acceptOrReject(" + notificationId + ", \"rejected\")'>Reject</button>");
		}
	}, {});
}

function subscribeToNotificationCountQueue() {
	stompClient.subscribe("/queue/" + communicatorEmail + "-notification-count", function(response) {
		const notification = JSON.parse(response.body);
		if(notification.notificationType == "Member Adding" && !joinedRoom.includes(notification.from)) {
			performSubscribeToMessageCountQueue("/topic/message-count-" + notification.from, notification.from);
			joinedRoom.push(notification.from);
		}
		else {
			if(!isSubscribingToNotificationQueue) {
				const numberOfNotifications = 1 + parseInt($("#badge2").text());
				$("#badge2").text(numberOfNotifications);
				if($("#badge2").css("visibility") == "hidden") {
					$("#badge2").css("visibility", "visible");
				}
			}
		}
	}, {});
}

function subscribeToMessageNotificationQueue() {
	isSubscribingToMessageNotificationQueue = true;
	messageNotificationClient = stompClient.subscribe("/queue/" + communicatorEmail + "-message-notification", function(response) {
		const conversation = JSON.parse(response.body);
		if(!$("#" + conversation.roomId).length) {
			let temp = " ";
			if(conversation.from != "" || conversation.latestMessage != "") {
				temp = ": ";
			}
			$("#conversation-cover").prepend("<div onclick='loadMessages(\"" + conversation.roomId + "\", \"" + conversation.roomType + "\", \"" + conversation.roomName + "\")' class='conversation-layout' id='" + conversation.roomId + "'><h3>" + conversation.roomName + "</h3><span id='message-show-" + conversation.roomId + "'>"+ conversation.from + temp + conversation.latestMessage + "</span><input id='type-property-" + conversation.roomId + "' type='hidden' value='" + conversation.roomType + "'/><input id='name-property-" + conversation.roomId + "' type='hidden' value='" + conversation.roomName + "'/></div>");
			subscribeToMessageCoverQueue(conversation.roomId);
		}
	}, {});
}

function subscribeToMessageCoverQueue(roomId) {
	messageCoverClient.push(stompClient.subscribe("/topic/message-cover-" + roomId, function(response) {
		const message = JSON.parse(response.body);
		const roomType = $("#type-property-" + message.roomId).val();
		const roomName = $("#name-property-" + message.roomId).val();
		$("#" + message.roomId).hide();
		$("#conversation-cover").prepend("<div onclick='loadMessages(\"" + message.roomId + "\", \"" + roomType + "\", \"" + roomName + "\")' class='conversation-layout' id='" + message.roomId + "'><h3>" + roomName + "</h3><span id='message-show-" + message.roomId + "'>" + message.senderName + ": " + message.messageContent + "</span><input id='type-property-" + message.roomId + "' type='hidden' value='" + roomType + "'/><input id='name-property-" + message.roomId + "' type='hidden' value='" + roomName + "'/></div>");
		if(!$("#message-content-cover-id-" + message.roomId).length) {
			$("#message-show-" + message.roomId).css("color", "green");
		}
		else {
			if(!$("#message-id-" + message.messageId).length) {
				if(message.from == communicatorEmail) {
					$("#message-content-cover-id-" + message.roomId).append("<div class='message-content-from-me' id='message-id-" + message.messageId + "'>" + message.senderName + ": " + message.messageContent + "</div>");
				}
				else $("#message-content-cover-id-" + message.roomId).append("<div class='message-content-from-others' id='message-id-" + message.messageId + "'>" + message.senderName + ": " + message.messageContent + "</div>");
			}
		}
	}, 
	{
		"id": communicatorEmail + "-" + roomId + "-" + "cover",
		"durable": true,
		"auto-delete": false
	}));
}

function unsubscribeToNotificationQueue() {
	if(isSubscribingToNotificationQueue) {
		isSubscribingToNotificationQueue = false;
		notificationClient.unsubscribe();
		notificationClient = null;
	}
}

function unsubscribeToMessageNotificationQueue() {
	if(isSubscribingToMessageNotificationQueue) {
		isSubscribingToMessageNotificationQueue = false;
		messageNotificationClient.unsubscribe();
		messageNotificationClient = null;
		loadedRoom = [];
	}
}

function unsubscribeToMessageCoverQueue() {
	if(isSubscribingToMessageNotificationQueue) {
		const messageCoverClientLength = messageCoverClient.length;
		for(let i = 0; i < messageCoverClientLength; ++i) {
			messageCoverClient[i].unsubscribe();
		}
		messageCoverClient = [];
	}
}

connect();


$(document).ready(function() {
    $("#friend-requests-icon").click(function() {
    	unsubscribeToMessageCoverQueue();
    	unsubscribeToMessageNotificationQueue();
    	unsubscribeToNotificationQueue();
        $("#header1").empty();
        $("#header1").append("<h2>Friend request form</h2>");
        $("#header2").empty();
        $("#header2").append("<h2>Header</h2>");
        if(!$("#friend-request-form-cover").length) {
            $("#header1").append("<div id='friend-request-form-cover'><form id='friend-request-form'><label>Email: </label><input type='text' onkeyup='showHints(this.value)' id='email-of-friend'/></form><button id='friend-request-sending-button' onclick='sendFriendRequest()'>Send</button></div>");
        }
    });
    
    $("#notifications-icon").click(function() {
    	unsubscribeToMessageCoverQueue();
    	unsubscribeToMessageNotificationQueue();
    	$("#badge2").css("visibility", "hidden");
    	$("#badge2").text("0");
		$("#header1").empty();
        $("#header1").append("<h2 id='notification-table'>Notifications</h2>");
        $("#header1").append("<div id='notification-table-cover'></div>");
        $("#header2").empty();
        $("#header2").append("<h2>Header</h2>");
        subscribeToNotificationQueue();
    });
    
    $("#message-creating-icon").click(function() {
    	unsubscribeToMessageCoverQueue();
    	unsubscribeToMessageNotificationQueue();
    	unsubscribeToNotificationQueue();
    	$("#header2").empty();
        $("#header2").append("<h2>Header</h2>");
    	$("#header1").empty();
        $("#header1").append("<h2>Room creating form</h2>");
        $("#header1").append("<div id='room-cover'><form id='room-creating-form'><label>Name: </label><input type='text' id='room-name'/><br/><br/><input type='radio' name='room-type' value='Group'/> <label>Group</label>  <input type='radio' name='room-type' value='Single'/> <label>Single</label><br/><br/><label>Friend list</label><br/><br/></form></div>");
        $.ajax({
        	url: "/get-friend-list/" + communicatorEmail,
        	type: "GET",
        	success: function(friendList) {
        		const friendListLength = friendList.length;
        		if(friendListLength == 0) {
        			$("#room-cover").append("<p>Empty</p>");
        		}
        		else {
        			for(let i = 0; i < friendListLength; ++i) {
            			$("#room-creating-form").append("<input type='checkbox' id='member-email' name='member-email' value='" + friendList[i].communicatorEmail + "'/> <label>" + friendList[i].communicatorFirstName + " " + friendList[i].communicatorLastName + " (" + friendList[i].communicatorEmail + ")</label><br/><br/>");
            		}
        		}
        		$("#header1").append("<button onclick='createRoom()' id='room-creating-button'>Create</button>");
        	},
        	error: function() {
        		alert("Something went wrong");
        	}
        });
    });
    
    $("#conversations-icon").click(function() {
    	unsubscribeToNotificationQueue();
    	$("#badge1").css("visibility", "hidden");
    	$("#badge1").text("0");
    	messageFrom = [];
    	$("#header1").empty();
        $("#header1").append("<h2>Conversations</h2>");
        $("#header1").append("<div id='conversation-cover'></div>");
        $("#header2").empty();
        $("#header2").append("<h2>Header</h2>");
        loadConversations();
    });
});

function loadConversations() {
	$.ajax({
		url: "/load-conversations",
		type: "GET",
		success: function(conversationList) {
			console.log(conversationList);
			const conversationListLength = conversationList.length;
			for(let i = 0; i < conversationListLength; ++i) {
				let temp = " ";
				if(conversationList[i].from != "" || conversationList[i].latestMessage != "") {
					temp = ": "
				}
				$("#conversation-cover").append("<div onclick='loadMessages(\"" + conversationList[i].roomId + "\", \"" + conversationList[i].roomType + "\", \"" + conversationList[i].roomName + "\")' class='conversation-layout' id='" + conversationList[i].roomId + "'><h3>" + conversationList[i].roomName + "</h3><span id='message-show-" + conversationList[i].roomId + "'>" + conversationList[i].from + temp + conversationList[i].latestMessage + "</span><input id='type-property-" + conversationList[i].roomId + "' type='hidden' value='" + conversationList[i].roomType + "'/><input id='name-property-" + conversationList[i].roomId + "' type='hidden' value='" + conversationList[i].roomName + "'/></div>");
				loadedRoom.push(conversationList[i].roomId);
			}
			const loadedRoomLength = loadedRoom.length;
			for(let i = 0; i < loadedRoomLength; ++i) {
				subscribeToMessageCoverQueue(loadedRoom[i]);
			}
			subscribeToMessageNotificationQueue();
		},
		error: function() {
			alert("Something went wrong");
		}
	});
}

function loadMessages(roomId, roomType, roomName) {
	$("#message-show-" + roomId).css("color", "black");
	$.ajax({
		url: "/load-messages/" + roomId,
		type: "GET",
		success: function(messages) {
			$("#header2").empty();
			$("#header2").append("<h2>" + roomName + "</h2>");
			$("#header2").append("<div id='message-cover'></div>");
			if(roomType == "Single") {
				$("#message-cover").append("<div class='message-content-cover' id='message-content-cover-id-" + roomId + "' style='height: 350px;'></div>");
			}
			else {
				$("#message-cover").append("<div id='task-bar'></div>");
				$("#task-bar").append("<div class='float-child' id='member-adding-cover'></div>");
				$("#member-adding-cover").append("<label>Add a member: </label>");
				$("#member-adding-cover").append("<input type='text'/>");
				$("#member-adding-cover").append("<button>Add</button>");
				$("#task-bar").append("<div class='float-child' id='member-kicking-cover'></div>");
				$("#member-kicking-cover").append("<label>Kick a member: </label>");
				$("#member-kicking-cover").append("<input type='text'/>");
				$("#member-kicking-cover").append("<button>Kick</button>");
				$("#task-bar").append("<div class='float-child' id='authority-adding-cover'></div>");
				$("#authority-adding-cover").append("<label>Set as admin: </label>");
				$("#authority-adding-cover").append("<input type='text'/>");
				$("#authority-adding-cover").append("<button>Set</button>");
				$("#task-bar").append("<div class='float-child' id='authority-removing-cover'></div>");
				$("#authority-removing-cover").append("<label>Remove admin authority: </label>");
				$("#authority-removing-cover").append("<input type='text'/>");
				$("#authority-removing-cover").append("<button>Remove</button>");
				$("#message-cover").append("<div class='message-content-cover' id='message-content-cover-id-" + roomId +"' style='height: 300px; margin-top: 50px;'></div>");
			}
			for(let element of messages) {
				if(element.from == communicatorEmail) {
					$("#message-content-cover-id-" + roomId).append("<div class='message-content-from-me' id='message-id-" + element.messageId + "'>" + element.senderName + ": " + element.messageContent + "</div>");
				}
				else $("#message-content-cover-id-" + roomId).append("<div class='message-content-from-others' id='message-id-" + element.messageId + "'>" + element.senderName + ": " + element.messageContent + "</div>");
			}
			$("#header2").append("<div id='message-reply-cover'></div>");
			$("#message-reply-cover").append("<textarea id='message-reply'></textarea>");
			$("#message-reply-cover").append("<i class='fas fa-paper-plane' style='color: blue;' id='sending-message-button' onclick='sendMessage(\"" + roomId + "\")'></i>");
		},
		error: function() {
			alert("Something went wrong");
		}
	});
}

function sendMessage(roomID) {
	console.log(roomID);
	const messageObj = {
		messageContent: $("#message-reply").val(),
		roomId: parseInt(roomID),
		from: communicatorEmail
	};
	$("#message-reply").val("");
	$.ajax({
		url: "/save-message",
		type: "POST",
		data: JSON.stringify(messageObj),
		contentType: "application/json; charset=utf-8",
		success: function(response) {
			messageObj.messageId = response.messageId;
			messageObj.senderName = response.senderName;
			stompClient.send("/topic/message-count-" + roomID, {}, JSON.stringify(messageObj));
			stompClient.send("/topic/message-cover-" + roomID, {}, JSON.stringify(messageObj));
		},
		error: function() {
			alert("Something went wrong");
		}
	});
}

$(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
});

function showHints(value) {
	if(value.length != 0) {
		console.log(value);
		const emailCheckingRequest = {
			communicatorEmail: value	
		};
		$.ajax({
			url: "/check-email",
			type: "POST",
			data: JSON.stringify(emailCheckingRequest),
			contentType: "application/json; charset=utf-8",
			success: function(emailCheckingResponse) {
				if(emailCheckingResponse.isValid == "true") {
					if($("#input-error").length) {
		                $("#input-error").remove();
		            }
		            if(!$("#verified-icon").length) {
		                $("#friend-request-form").append(" <i class='fas fa-user-check' id='verified-icon' style='color: green;'></i>");
		            }
				}
				else {
					if($("#verified-icon").length) {
		                $("#verified-icon").remove();
		            }
		            if(!$("#input-error").length) {
		                $("#friend-request-form").append("<p id='input-error'>" + emailCheckingResponse.reasonForInvalidation + "</p>");
		            }
		            else $("#input-error").text(emailCheckingResponse.reasonForInvalidation);
				}
			},
			error: function() {
				alert("Something went wrong :<");
			}
		});
	}
	else {
		if($("#input-error").length) {
            $("#input-error").remove();
        }
		if($("#verified-icon").length) {
            $("#verified-icon").remove();
        }
	}
}

function createRoom() {
	let memberEmail = [];
	$("input:checkbox[name=member-email]:checked").each(function() {
	    memberEmail.push($(this).val());
	});
	const type = $("input:radio[name=room-type]:checked").val();
	const name = $("#room-name").val();
	let quantity = memberEmail.length;
	$("#room-creating-error").remove();
	if(quantity == 0) {
		$("#room-cover").prepend("<p id='room-creating-error'>Please choose the friend(s) you want to chat with</p>");
	}
	else if(name.length == 0) {
		$("#room-cover").prepend("<p id='room-creating-error'>Room name is required</p>");
	}
	else if(type == undefined) {
		$("#room-cover").prepend("<p id='room-creating-error'>Room type is required</p>");
	}
	else if(type == "Single" && quantity > 1) {
		$("#room-cover").prepend("<p id='room-creating-error'>You can just chat with one communicator in a single room</p>");
	}
	else {
		memberEmail.push(communicatorEmail);
		quantity += 1;
		const room = {
			roomName: name,
			roomType: type,
			numberOfMembers: quantity,
			memberEmails: memberEmail
		};
		$.ajax({
			url: "/check-room",
			type: "POST",
			data: JSON.stringify(room),
			contentType: "application/json; charset=utf-8",
			success: function(response) {
				if(response.isValid == "false") {
					$("#room-cover").prepend("<p id='room-creating-error'>" + response.reasonForInvalidation + "</p>");
				}
				else {
					stompClient.send("/app/create-room", {}, JSON.stringify(room));
					if($("#room-creating-error").length) {
						$("#room-creating-error").remove();
					}
					$('input:radio[name=room-type]:checked').each(function() {
				        $(this).prop("checked", false);
				    });

				    $('input:checkbox[name=member-email]:checked').each(function() {
				        $(this).prop("checked", false);
				    });
				    $("#room-name").val("");
					alert("Created successfully");
				}
			},
			error: function() {
				alert("Something went wrong");
			}
		});
	}
}

function sendFriendRequest() {
	const receiverEmail = $("#email-of-friend").val();
	if($("#verified-icon").length) {
		const notification = {
			notificationContent: "<p>" + communicatorFirstName + " " + communicatorLastName + " sent you a friend request" + "</p>",
			notificationType: "Friend Request",
			from: communicatorEmail,
			to: [receiverEmail],
		};
		stompClient.send("/app/send-friend-request-to-communicator/" + receiverEmail, {}, JSON.stringify(notification));
		$("#verified-icon").remove();
		$("#email-of-friend").val("");
		alert("Sent successfully");
	}
}

function acceptOrReject(notificationId, isAccepted) {
	let destination = "";
	$("#accept-button-id-" + notificationId).remove();
	$("#reject-button-id-" + notificationId).remove();
	if(isAccepted == "accepted") {
		$("#notification-cover-id-" + notificationId).append("<p>Friend request accepted</p>");
		destination = "/app/process-friend-request/" + notificationId + "/true";
	}
	else {
		$("#notification-cover-id-" + notificationId).append("<p> Friend request rejected</p>");
		destination = "/app/process-friend-request/" + notificationId + "/false";
	}
	stompClient.send(destination, {}, "");
}






































