const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

let stompClient = null;
let notificationClient = null;
let messageClient = null;

let isSubscribingToNotificationQueue = false;
let isSubscribingToMessageQueue = false;

let communicatorEmail = document.getElementById("communicator-email").value;
let communicatorFirstName = document.getElementById("communicator-first-name").value;
let communicatorLastName = document.getElementById("communicator-last-name").value;

function connect() {
	const webSocketHandshakeProcessingUrl = "/process-web-socket-handshake";
	const sockJSClient = new SockJS(webSocketHandshakeProcessingUrl);
	stompClient = Stomp.over(sockJSClient);
	stompClient.connect({}, function() {
		subscribeToMessageCountQueue();
		subscribeToNotificationCountQueue();
	});
}

function subscribeToMessageQueue() {
	messageClient = stompClient.subscribe("/queue/" + communicatorEmail + "-message", function() {
		
	}, {});
}

function subscribeToMessageCountQueue() {
	stompClient.subscribe("/queue/" + communicatorEmail + "-message-count", function() {
		
	}, {});
}

function subscribeToNotificationQueue() {
	notificationClient = stompClient.subscribe("/queue/" + communicatorEmail + "-notification", function (data) {
		const notification = JSON.parse(data.body);
		const notificationId = notification.notificationId;
		$("#header1").append("<div class='notification-element' id='" + notificationId + "'>" + "<p>" + notification.notificationContent + "</p>" + "</div>");
		if(notification.notificationType == "Friend Request") {
			$("#" + notificationId).append("<button onclick='acceptOrReject(" + notificationId + ", \"accepted\")'>Accept</button>   <button onclick='acceptOrReject(" + notificationId + ", \"rejected\")'>Reject</button>");
		}
	}, {});
}

function subscribeToNotificationCountQueue() {
	stompClient.subscribe("/queue/" + communicatorEmail + "-notification-count", function() {
		if(!$("#notification-table").length) {
			const numberOfNotifications = 1 + parseInt($("#badge2").text());
			$("#badge2").text(numberOfNotifications);
			if($("#badge2").css("visibility") == "hidden") {
				$("#badge2").css("visibility", "visible");
			}
		}
	}, {});
}

function unsubscribeToNotificationQueue() {
	if(isSubscribingToNotificationQueue) {
		isSubscribingToNotificationQueue = false;
		notificationClient.unsubscribe();
	}
}

function unsubscribeToMessageQueue() {
	if(isSubscribingToMessageQueue) {
		isSubscribingToMessageQueue = false;
		messageClient.unsubscribe();
	}
}

connect();


$(document).ready(function() {
    $("#friend-requests-icon").click(function() {
    	unsubscribeToNotificationQueue();
    	unsubscribeToMessageQueue();
        $("#header1").empty();
        $("#header1").append("<h2>Friend request form</h2>");
        if(!$("#friend-request-form-cover").length) {
            $("#header1").append("<div id='friend-request-form-cover'><form id='friend-request-form'><label>Email: </label><input type='text' onkeyup='showHints(this.value)' id='email-of-friend'/></form><button id='friend-request-sending-button' onclick='sendFriendRequest()'>Send</button></div>");
        }
    });
    
    $("#notifications-icon").click(function() {
    	unsubscribeToMessageQueue();
    	isSubscribingToNotificationQueue = true;
    	$("#badge2").css("visibility", "hidden");
    	$("#badge2").text("0");
		$("#header1").empty();
        $("#header1").append("<h2 id='notification-table'>Notifications</h2>");
        subscribeToNotificationQueue();
    });
});

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

function sendFriendRequest() {
	const friendEmail = $("#email-of-friend").val();
	if($("#verified-icon").length) {
		const notification = {
			notificationContent: communicatorFirstName + " " + communicatorLastName + " sent you a friend request",
			notificationType: "Friend Request",
			from: communicatorEmail,
			to: [friendEmail],
		};
		stompClient.send("/app/send-friend-request-to-communicator/" + friendEmail, {}, JSON.stringify(notification));
		$("#verified-icon").remove();
		$("#email-of-friend").val("");
		alert("Sent successfully");
	}
}









































