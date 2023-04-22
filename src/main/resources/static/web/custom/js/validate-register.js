jQuery.validator.addMethod("phone", function (value, element) {
    return this.optional(element) || /^(0|\+84)[1-9][0-9]{8}$/.test(value);
}, "Điện thoại phải đủ 10 số và bắt đầu bằng 0 hoặc +84");
jQuery.validator.addMethod("email", function (value, element) {
    return this.optional(element) || /^[a-zA-Z][\w]+@([\w]+\.[\w]{2,}|[\w]+\.[\w]{2,}\.[\w]{2,})$/.test(value);
}, "Email không đúng định dạng");
jQuery.validator.addMethod("password", function (value, element) {
    return this.optional(element) || /^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,})$/.test(value);
}, "Password phải tối thiểu 6 ký tự và chứa 1 chữ cái thường, 1 chữ cái hoa và 1 chữ số");

$('#register-form').validate({
    rules: {
        "name" : {
            required: true,
        },
        "phone" : {
            required: true,
            phone: true,
        },
        "email" : {
            required: true,
            email: true,
        },
        "password" : {
            required: true,
            password: true,
        }
    },
    messages: {
        "name" : {
            required: "Tên không được để trống",
        },
        "phone" : {
            required: "Số điện thoại không được để trống",
        },
        "email" : {
            required: "Email không được để trống",
        },
        "password" : {
            required: "Mật khẩu không đuợc để trống",
        }
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})

$('#forgot-form').validate({
    rules: {
        "email" : {
            required: true,
            email: true,
        }
    },
    messages: {
        "email" : {
            required: "Email không được để trống",
        }
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})