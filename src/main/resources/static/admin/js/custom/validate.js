jQuery.validator.addMethod("phone", function (value, element) {
    return this.optional(element) || /^(0|\+84)[1-9][0-9]{8}$/.test(value);
}, "Điện thoại phải đủ 10 số và bắt đầu bằng 0 hoặc +84");

jQuery.validator.addMethod("email", function (value, element) {
    return this.optional(element) || /^[a-zA-Z][\w]+@([\w]+\.[\w]{2,}|[\w]+\.[\w]{2,}\.[\w]{2,})$/.test(value);
}, "Email không đúng định dạng");

jQuery.validator.addMethod("password", function (value, element) {
    return this.optional(element) || /^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,})$/.test(value);
}, "Password phải tối thiểu 6 ký tự và chứa 1 chữ cái thường, 1 chữ cái hoa và 1 chữ số");

$('form').validate({
    rules: {
        "createName" : {
            required: true,
        },
        "create-emp-mame" : {
            required: true,
        },
        "create-emp-phone" : {
            required: true,
            phone: true,
        },
        "create-emp-email" : {
            required: true,
            email: true,
        },
        "create-emp-password" : {
            required: true,
            password: true,
        },
        "select-roles" : {
            required: true,
        },
    },
    messages: {
        "createName": {
            required: "Please enter category name",
        },
        "create-emp-mame" : {
            required: "Tên không được bỏ trống",
        },
        "create-emp-phone" : {
            required: "Số điện thoại không được bỏ trống",
        },
        "create-emp-email" : {
            required: "Email không được bỏ trống",
        },
        "create-emp-password" : {
            required: "Password không được bỏ trống",
        },
        "select-roles" : {
            required: "Role không được bỏ trống",
        },
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
});

$('#reset-add-cert').click(function () {
    $('#cert-form').validate().resetForm();
});