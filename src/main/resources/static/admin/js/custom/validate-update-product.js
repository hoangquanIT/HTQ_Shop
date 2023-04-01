$('#update-basic-form').validate({
    rules: {
        "product-name" : {
            required: true,
        },
    },
    messages: {
        "product-name" : {
            required: "Tên sản phẩm không được để trống",
        },
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})

$('#create-variant-form').validate({
    rules: {
        "createSku" : {
            required: true,
        },
        "createColor" : {
            required: true,
        },
        "createSize" : {
            required: true,
        },
        "createPrice" : {
            required: true,
        },
    },
    messages: {
        "createSku" : {
            required: "Mã SKU không được bỏ trống",
        },
        "createColor" : {
            required: "Màu không được bỏ trống",
        },
        "createSize" : {
            required: "Size không được bỏ trống",
        },
        "createPrice" : {
            required: "Giá tiền không được bỏ trống",
        },
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})