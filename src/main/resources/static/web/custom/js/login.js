function login(){
    $.ajax({
        url: '/ecommerce/api/v1/client/auth/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            'email' : $('#login-email').val(),
            'password' : $('#login-password').val()
        }),
        success: function(res){
            localStorage.setItem('firstLogin', 'true');
            window.location.href = "/shop";
        },
        error: function(e){
            if (e.responseJSON.message === "Tài khoản của bạn chưa được xác thực") {
                $('#resend').removeAttr('style');
            }
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#login-btn').on('click', function(e){
    e.preventDefault();
    if ($('#login-form').valid()){
        $('#resend').attr('style', 'display: none');
        login();
    }
})

$('#resend-token').on('click', function() {
    $.ajax({
        type: 'POST',
        url: '/ecommerce/api/v1/client/auth/resend-token',
        contentType: 'application/json',
        data: JSON.stringify({
            'email' : $('#login-email').val(),
            'password' : $('#login-password').val()
        }),
        success: function(res) {
            toastr.success("Gửi lại mail xác nhận thành công");
        },
        error: function(e) {
            toastr.error(e.responseJSON.message);
        }
    })
})