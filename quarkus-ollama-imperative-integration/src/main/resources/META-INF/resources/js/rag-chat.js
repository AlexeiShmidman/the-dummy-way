let scrollToBottom = function () {
  let chat = $('#chat');
  chat.scrollTop(chat[0].scrollHeight);
};

let successfulResponseHandler = function (data) {
  let chat = $("#chat");
  chat.append(`<span class='system-message'><span class='system-user'>Bot response</span> : <span class='user'>${data} </span><br>`);
  scrollToBottom();
};

let errorResponseHandler = function (error) {
  let chat = $("#chat");
  chat.append(`<span class='system-message error'><span class='system-user'>Error</span> : <span class='user'>${error} </span><br>`);
  scrollToBottom();
};

let send = function () {
  let msg = $("#prompt-msg");
  var prompt = msg.val();


  let chat = $("#chat");
  chat.append(`<span class='message'></span><span class='system-user'>User prompt</span> : <span class='user'>${prompt} </span><br>`);

  $.post("/rag/chat", prompt, function (response) {
    console.log("Message sent to server");
    })
    .done(successfulResponseHandler)
    .fail(errorResponseHandler);

  msg.val("");
};

$(document).ready(function () {

    $("#prompt-send").click(send);

    $("#prompt-msg").keypress(function (event) {
        if (event.keyCode == 13 || event.which == 13) {
            send();
        }
    });

    $("#chat").change(function () {
        scrollToBottom();
    });
});
