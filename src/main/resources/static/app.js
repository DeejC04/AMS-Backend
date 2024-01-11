document.addEventListener('DOMContentLoaded', function () {
    var socket = null;
    var stompClient = null;
    var connectButton = document.getElementById('connect');
    var disconnectButton = document.getElementById('disconnect');
    var sendPingButton = document.getElementById('sendPing');
    var statusDiv = document.getElementById('status');
    var csrfToken = '';

    getCsrfToken().then(csrfData => {
        if (csrfData) {
            csrfToken = csrfData;
        }
    });

    console.log('CSRF token: ' + csrfToken);

    connectButton.addEventListener('click', function () {
        socket = new SockJS('/esp');
        stompClient = Stomp.over(socket);

        stompClient.connect({['X-CSRF-TOKEN']: csrfToken}, function (frame) {
            console.log('Connected: ' + frame);
            connectButton.disabled = true;
            disconnectButton.disabled = false;
            sendPingButton.disabled = false;
            statusDiv.innerHTML = 'Connected';

            stompClient.subscribe('/topic/readers', function (message) {
                console.log('Message: ' + message.body);
                // handle incoming messages here
            });
        });
    });

    disconnectButton.addEventListener('click', function () {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        connectButton.disabled = false;
        disconnectButton.disabled = true;
        sendPingButton.disabled = true;
        statusDiv.innerHTML = 'Disconnected';
        console.log("Disconnected");
    });

    sendPingButton.addEventListener('click', function () {
        // Replace with actual readerId
        var readerId = 'some-reader-id';
        fetch('/readers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'readerId=' + encodeURIComponent(readerId)
        }).then(response => {
            console.log('Ping sent. Status: ' + response.status);
        });
    });

    function getCsrfToken() {
        return fetch('/csrf', {
            credentials: 'include'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text(); 
        })
        .catch(error => {
            console.error('Fetching CSRF token failed:', error);
        });
    }    
});
