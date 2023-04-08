jQuery.validator.addMethod("phone", function (value, element) {
    return this.optional(element) || /^(0|\+84)[1-9][0-9]{8}$/.test(value);
}, "Điện thoại phải đủ 10 số và bắt đầu bằng 0 hoặc +84");

jQuery.validator.addMethod("email", function (value, element) {
    return this.optional(element) || /^[a-zA-Z][\w]+@([\w]+\.[\w]{2,}|[\w]+\.[\w]{2,}\.[\w]{2,})$/.test(value);
}, "Email không đúng định dạng");

$('#checkout-form').validate({
    rules: {
        "name-billing" : {
            required: true,
        },
        "phone-billing" : {
            required: true,
            phone: true,
        },
        "email-billing" : {
            required: true,
            email: true,
        },
        "address-billing" : {
            required: true,
        },
        "country-billing" : {
            required: true,
        }
    },
    messages: {
        "name-billing" : {
            required: "Tên không được để trống",
        },
        "phone-billing" : {
            required: "Số điện thoại không đuợc để trống",
        },
        "email-billing" : {
            required: "Email không được để trống",
        },
        "address-billing" : {
            required: "Địa chỉ không được để trống",
        },
        "country-billing" : {
            required: "Phải chọn một tỉnh/thành phố",
        }
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})
