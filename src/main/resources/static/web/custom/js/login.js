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