function doActionLogin() {
    var buttonText = $('#login-facebook-btn').text().trim();
    if (buttonText === "Login by Facebook") {
        login();
    } else {
        logout();
    }
}

// logs the user in the application and facebook
function login() {
    FB.getLoginStatus(function (response) {
        if (response.status === 'connected') {
            console.log('Check Status accessToken: ' + response.authResponse.accessToken);
            console.log('Check Status expiresIn: ' + response.authResponse.expiresIn);
            console.log('Check Status signedRequest: ' + response.authResponse.signedRequest);
            console.log('Check Status userID: ' + response.authResponse.userID);
            doGetUserName(response.authResponse.accessToken);
            if (sessionStorage.getItem('userId') === response.authResponse.userID) {
                console.log('Yeah You\' already login: ' + response.authResponse.userID);
            } else {
                doStoreSession(response.authResponse);
            }
            doUpdateLoginButton();
        } else {
            FB.login(function (responseLogin) {
                console.log('Status: ' + responseLogin.status);
                if (responseLogin.authResponse) {
                    console.log('Login accessToken: ' + responseLogin.authResponse.accessToken);
                    console.log('Login expiresIn: ' + responseLogin.authResponse.expiresIn);
                    console.log('Login signedRequest: ' + responseLogin.authResponse.signedRequest);
                    console.log('Login userID: ' + responseLogin.authResponse.userID);
                    doGetUserName(responseLogin.authResponse.accessToken);
                    doStoreSession(responseLogin.authResponse);
                    doUpdateLoginButton();
                } else {
                    console.log('FAILED');
                }
            }, {scope: 'email'}); // which data to access from user profile
        }
    });
}

// logs the user in the application and facebook
function logout() {
    FB.logout(function(response) {
        console.log('Do logout FB:'+ response);
        doDeleteSession();
    });
}

function doStoreSession(loginResponseData, redirectUrl) {
    sessionStorage.setItem('userId', loginResponseData.userID);
    sessionStorage.setItem('accessToken', loginResponseData.accessToken);
    console.log('Set token to session: ' + loginResponseData.userID);
}

function doDeleteSession(loginResponseData, redirectUrl) {
    sessionStorage.removeItem('userId');
    sessionStorage.removeItem('accessToken');
    doUpdateLoginButton();
}

function doUpdateLoginButton(){
    var buttonText = $('#login-facebook-btn').text().trim();
    if (buttonText === "Login by Facebook") {
        $('#login-facebook-btn').text("Logout");
        $('#user-name-lbl').text(sessionStorage.getItem('userName'));
    } else {
        $('#login-facebook-btn').text("Login by Facebook");
        $('#user-name-lbl').text("");
    }
}

function doGetUserName(accessToken) {
    $.ajax({url: "https://graph.facebook.com/me?access_token="+accessToken, success: function(result){
            console.log("Name" + result["name"]);
            console.log("Id" + result["id"]);
            if (result["id"] == sessionStorage.getItem('userId')) {
                sessionStorage.setItem('userName', result["name"]);
            }
        }});
}

/*
sessionStorage.setItem('name', 'Ted Mosby');
sessionStorage.getItem('name');
sessionStorage.length;
sessionStorage.removeItem('name');
// xóa tất cả item trong sessionStorage
sessionStorage.clear();
*/