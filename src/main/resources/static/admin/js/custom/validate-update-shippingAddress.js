jQuery.validator.addMethod("phone", function (value, element) {
    return this.optional(element) || /^(0|\+84)[1-9][0-9]{8}$/.test(value);
}, "Điện thoại phải đủ 10 số và bắt đầu bằng 0 hoặc +84");

jQuery.validator.addMethod("email", function (value, element) {
    return this.optional(element) || /^[a-zA-Z][\w]+@([\w]+\.[\w]{2,}|[\w]+\.[\w]{2,}\.[\w]{2,})$/.test(value);
}, "Email không đúng định dạng");

$('form').validate({
    rules: {
        "updateName" : {
            required: true,
        },
        "updatePhone" : {
            required: true,
            phone: true,
        },
        "updateEmail" : {
            required: true,
            email: true,
        },
        "updateAddress" : {
            required: true,
        }
    },
    messages: {
        "updateName" : {
            required: "Tên không được để trống",
        },
        "updatePhone" : {
            required: "Số điện thoại không được để trống",
        },
        "updateEmail" : {
            required: "Email không được để trống",
        },
        "updateAddress" : {
            required: "Địa chỉ không được để trống",
        }
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})