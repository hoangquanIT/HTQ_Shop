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