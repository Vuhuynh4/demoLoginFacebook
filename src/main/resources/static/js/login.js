
// logs the user in the application and facebook
function login() {
    FB.login(function (responseLogin) {
        console.log('Status: ' + responseLogin.status);
        console.log('' + JSON.stringify(responseLogin))
        if (responseLogin.authResponse) {
            doStoreSession(responseLogin.authResponse);
            console.log('LOGIN SUCCESS');
            //myRedirect("https://" + window.location.host.toString() + "/demoLogin/login", responseLogin.authResponse.accessToken);
        } else {
            console.log('FAILED');
        }
    }, {scope: 'pages_show_list,manage_pages,publish_pages',
        enable_profile_selector: true}); // which data to access from user profile
}

function checkLoginState() {
    FB.getLoginStatus(function (response) {
        if (response.status === 'connected') {
            if (sessionStorage.getItem('userId') === response.authResponse.userID) {
                console.log('Yeah You\' already login: ' + response.authResponse.userID);
            } else {
                doStoreSession(response.authResponse);
            }
            myRedirect("https://" + window.location.host.toString() + "/demoLogin/login", response.authResponse.accessToken);
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
}


var myRedirect = function(redirectUrl, accessToken) {
    var form = $('<form action="' + redirectUrl + '" method="post">' +
        '<input type="hidden" name="id" value="' + accessToken + '"></input>' + '</form>');
    $('body').append(form);
    $(form).submit();
};

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