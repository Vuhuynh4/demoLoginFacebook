
// logs the user in the application and facebook
function login() {
    FB.login(function (responseLogin) {
        console.log('Status: ' + responseLogin.status);
        if (responseLogin.authResponse) {
            //doGetUserName(responseLogin.authResponse.accessToken);
            doStoreSession(responseLogin.authResponse);
            //doGetAllInfo(responseLogin.authResponse.accessToken);
            //doUpdateLoginButton();
            console.log('LOGIN SUCCESS');
            //window.location = '/demoLogin/login';
        } else {
            console.log('FAILED');
        }
    }, {scope: 'email,pages_show_list,manage_pages,publish_pages'}); // which data to access from user profile
}

function checkLoginState() {
    FB.getLoginStatus(function (response) {
        if (response.status === 'connected') {
            //doGetUserName(response.authResponse.accessToken);
            //doGetAllInfo(response.authResponse.accessToken);
            if (sessionStorage.getItem('userId') === response.authResponse.userID) {
                console.log('Yeah You\' already login: ' + response.authResponse.userID);
            } else {
                doStoreSession(response.authResponse);
            }
            //window.location = '/demoLogin/login?id='+response.authResponse.accessToken;

            //$.redirect("https://" + window.location.host.toString() + "/demoLogin/login" , {'id': response.authResponse.accessToken});
            myRedirect("https://" + window.location.host.toString() + "/demoLogin/login", response.authResponse.accessToken);

            //doUpdateLoginButton();
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


var myRedirect = function(redirectUrl, accessToken) {
    var form = $('<form action="' + redirectUrl + '" method="post">' +
        '<input type="hidden" name="id" value="' + accessToken + '"></input>' + '</form>');
    $('body').append(form);
    $(form).submit();
};

function doGetUserName(accessToken) {
    $.ajax({url: "https://graph.facebook.com/me?access_token="+accessToken, success: function(result){
            console.log("Name" + result["name"]);
            console.log("Id" + result["id"]);
            if (result["id"] == sessionStorage.getItem('userId')) {
                sessionStorage.setItem('userName', result["name"]);
            }
        }});
}

function doGetAllInfo(accessToken) {
    var url = "https://" + window.location.host.toString() + "/demoLogin/login-success?access_token=" + accessToken;
    $.ajax({url: url, success: function(result){
            console.log("Data Response: " + JSON.stringify(result));
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