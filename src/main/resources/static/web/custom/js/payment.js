$(document).ready(function() {
    getOrderInfo();
})

// ======================== GET ORDER INFO ========================
let totalPrice = 0;
let orderCode;
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}
function getOrderInfo(){
    let cartId = localStorage.getItem('cart_id');
    $.ajax({
        url: `/ecommerce/api/v1/client/order/${cartId}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            orderCode = data.orderCode;
            renderCartItem(data);
            renderShippingInfo(data);
        },
        error: function(e){
            console.log(e);
        }
    })
}

function renderCartItem(data){
    let html = '';

    if (data != null && data.items.length !== 0){
        let arr = data.items;
        arr.forEach(obj => {
            totalPrice += obj.total;
            html += `
                <!-- Cart Item-->
                <div class="row mx-0 py-4 g-0 border-bottom">
                    <div class="col-2 position-relative">
                        <span class="checkout-item-qty">${obj.quantity}</span>
                        <picture class="d-block border">
                            <img class="img-fluid" src="${obj.imageUrl}"
                                 alt="HTML Bootstrap Template by Pixel Rocket">
                        </picture>
                    </div>
                    <div class="col-9 offset-1">
                        <div>
                            <h6 class="justify-content-between d-flex align-items-start mb-2">
                                ${obj.productName}
                            </h6>
                            <div class="d-block text-muted fw-bolder text-uppercase fs-9">
                                Size: <span id="product-size">${obj.variant.size}</span> /
                                Màu: <span id="product-color">${obj.variant.color}</span>
                            </div>
                        </div>
                        <p class="fw-bolder text-end text-muted m-0">${formatVND(obj.total)}</p>
                    </div>
                </div> <!-- / Cart Item-->
            `;
        })
    } else {
        html = '<h6">Giỏ hàng của bạn đang trống</h6>';
    }
    $('#cart-items').html(html);
    $('#total-price').text(formatVND(totalPrice));
}

function renderShippingInfo(data) {
    let shipping = data.shippingAddress;
    $('#name-payment').text(shipping.name);
    $('#email-payment').text(shipping.email);
    $('#phone-payment').text(shipping.phone);
    let deliverAddress = shipping.address + ", " + shipping.city;
    $('#deliver-payment').text(deliverAddress);
}


$('#online-payment-btn').on('click', function() {
    createPayment();
})

function createPayment() {
    $.ajax({
        url: '/ecommerce/api/v1/client/payment',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'orderId' : orderCode,
            'total' : totalPrice
        }),
        success: function(data) {
            window.location.href = data.url;
        },
        error: function(e) {
            console.log(e);
        }
    })
}

$('#order-btn').on('click', function() {
    toastr.success("Đặt hàng thành công");
    setTimeout(function(){
        let myNumber = 0;
        localStorage.setItem('numberOfItems', myNumber.toString());
        localStorage.removeItem("cart_id");
        window.location.href = "/shop/checkout/order/" + orderCode;
    }, 3000);
})