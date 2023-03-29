jQuery.validator.addMethod("phone", function (value, element) {
    return this.optional(element) || /^(0|\+84)[1-9][0-9]{8}$/.test(value);
}, "Điện thoại phải đủ 10 số và bắt đầu bằng 0 hoặc +84");

jQuery.validator.addMethod("password", function (value, element) {
    return this.optional(element) || /^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,})$/.test(value);
}, "Password phải tối thiểu 6 ký tự và chứa 1 chữ cái thường, 1 chữ cái hoa và 1 chữ số");

$('#account-info').validate({
    rules: {
        "info-name" : {
            required: true,
        },
        "info-phone" : {
            required: true,
            phone: true,
        }
    },
    messages: {
        "info-name" : {
            required: "Tên không được để trống",
        },
        "info-phone" : {
            required: "Số điện thoại không đuợc để trống",
        }
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})

$('#change-pass-form').validate({
    rules: {
        "old-password" : {
            required: true,
        },
        "new-password" : {
            required: true,
            password: true,
        },
        "re-password" : {
            required: true,
            password: true,
            equalTo: "#new-password"
        }
    },
    messages: {
        "old-password" : {
            required: "Không được để trống",
        },
        "new-password" : {
            required: "Không được để trống",
        },
        "re-password" : {
            required: "Không được để trống",
            equalTo: "Re-password phải giống password"
        }
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})
