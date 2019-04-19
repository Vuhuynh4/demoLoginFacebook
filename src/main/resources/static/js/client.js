/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global Stomp */

var stompClient = null;

function connect() {
    var hostAddress = window.location.host.toString();
    url = "https://" + hostAddress + "/gw_payment/waiting";
    var socket = new SockJS(url);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        var clientId = $("#clientId").val();
        console.log('Connected: ' + frame);
        stompClient.subscribe('/result/'+ clientId +'/mapping', function (messageOutput) {
            console.log("SOME THING CALL ME");
            var receivedId = messageOutput.body;
            if (clientId === receivedId) {
                disconnect();
                window.location = '/gw_payment/result/data?clientId=' + receivedId;
            }
            console.log(messageOutput);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

var remainingSeconds = 240; // 4 mins

setInterval(function () {
    remainingSeconds--;
    if (remainingSeconds == 0) {
        disconnect();
        redirectToResultData();
    }
}, 1000);

function redirectToResultData() {
    var clientId = $("#clientId").val();
    window.location = '/gw_payment/result/data?clientId=' + clientId;
}