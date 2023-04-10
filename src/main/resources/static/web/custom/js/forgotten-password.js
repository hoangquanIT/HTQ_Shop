$('#forgot-password-btn').on('click', function(e) {
    e.preventDefault();
    if ($('#forgot-form').valid()) {
        resetPassword();
    }
})
function resetPassword() {
    let email = $('#forgot-password').val();
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