jQuery.validator.addMethod("email", function (value, element) {
    return this.optional(element) || /^[a-zA-Z][\w]+@([\w]+\.[\w]{2,}|[\w]+\.[\w]{2,}\.[\w]{2,})$/.test(value);
}, "Email không đúng định dạng");

$('#login-form').validate({
    rules: {
        "login-email" : {
            required: true,
            email: true
        },
        "login-password" : {
            required: true,
        },

    },
    messages: {
        "login-email" : {
            required: "Email không được để trống",
        },
        "login-password" : {
            required: "Password không đuợc để trống",
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
        "forgot-email" : {
            required: true,
        },
    },
    messages: {
        "forgot-email" : {
            required: "Email không được để trống",
        },
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})
