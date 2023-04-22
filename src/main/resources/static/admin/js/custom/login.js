const AUTH_URL = '/ecommerce/api/v1/auth/login';

function login(){
    $.ajax({
        url: AUTH_URL,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            'email' : $('#inputEmail').val(),
            'password' : $('#inputPassword').val()
        }),
        success: function(res){
            window.location.href = "/admin/dashboard";
        },
        error: function(e){
            console.log(e);
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#login-btn').on('click', function(e){
    e.preventDefault();
    if ($('#login-form').valid()){
        login();
    }
})

$('#switch-to-forgot').on('click', function(){
    document.getElementById("login-block").style.display = "none";
    document.getElementById("forgot-block").style.display = "block";
})

$('#switch-to-login').on('click', function(){
    document.getElementById("forgot-block").style.display = "none";
    document.getElementById("login-block").style.display = "block";
})

// ================================ RESET PASSWORD =============================
function resetPassword() {
    let email = $('#forgot-password-email').val();
    $.ajax({
        url: `/ecommerce/api/v1/client/auth/reset-password/${email}`,
        type: 'GET',
        dataType: 'json',
        success: function(res) {
            toastr.success("Mật khẩu mới đã được gửi về mail cho bạn");
        },
        error: function(e) {
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#forgot-password-btn').on('click', function(e){
    e.preventDefault();
    if ($('#forgot-form').valid()){
        resetPassword();
    }
})