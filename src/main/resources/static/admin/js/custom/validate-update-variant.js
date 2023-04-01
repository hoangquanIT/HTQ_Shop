$('form').validate({
    rules: {
        "updateSku" : {
            required: true,
        },
        "updatePrice" : {
            required: true,
            min: 0,
        },
        "updateQuantity" : {
            required: true,
            min: 0,
        },
    },
    messages: {
        "updateSku" : {
            required: "Mã SKU không được để trống",
        },
        "updatePrice" : {
            required: "Giá không được để trống",
            min: "Nhập tối thiểu là 0",
        },
        "updateQuantity" : {
            required: "Số lượng không được để trống",
            min: "Nhập tối thiểu là 0",
        },
    },
    highlight: function (element) {
        $(element).removeClass('error');
    },
    unhighlight: function (element) {
        $(element).removeClass('error');
    }
})