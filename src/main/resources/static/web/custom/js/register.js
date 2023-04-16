$('#register-btn').on('click', function(e){
    e.preventDefault();
    if ($('#register-form').valid()){
        registerClient();
    }
})
function registerClient(){
    let baseUrl = window.location.origin;
    $.ajax({
        url: '/ecommerce/api/v1/client/auth/register',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'name' : $('#register-fullname').val(),
            'phone' : $('#register-phone').val(),
            'email' : $('#register-email').val(),
            'password' : $('#register-password').val(),
            'baseUrl' : baseUrl
        }),
        success: function(res){
            toastr.success("Link kích hoạt đã được gửi tới email của bạn.");
            setTimeout(function(){
                window.location.href = "/shop/login";
            }, 5000);
        },
        error: function(e){
            console.log(e);
            toastr.error(e.responseJSON.message);
        }
    })
}