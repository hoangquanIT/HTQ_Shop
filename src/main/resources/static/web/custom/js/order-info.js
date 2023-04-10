$(document).ready(function() {
    getOrderInfo();
    checkOnlinePayment();
    if (localStorage.getItem('firstLogin') !== null) {
        updateAccountInfo();
    }
})

// ======================== GET ORDER INFO ========================
const pathname = window.location.pathname.split("/");
const orderCode = pathname[pathname.length-1];
let totalPrice = 0;
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}
function getOrderInfo(){
    $.ajax({
        url: `/ecommerce/api/v1/client/payment/order/${orderCode}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
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
                  <tr>
                    <td>
                      <!-- Cart Item-->
                      <div class="row mx-0 g-0">
                        <div class="col-2 position-relative">
                          <picture class="d-block border">
                            <img class="img-fluid"
                                 src="${obj.imageUrl}"
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
                        </div>
                      </div> <!-- / Cart Item-->
                    </td>
                    <td>${obj.quantity}</td>
                    <td>${formatVND(obj.total / obj.quantity)}</td>
                    <td>${formatVND(obj.total)}</td>
                  </tr>
            `;
        })
    }
    $('#order-items').html(html);
    $('#order-total').text(formatVND(totalPrice));
}

function renderShippingInfo(data) {
    let shipping = data.shippingAddress;
    $('#order-code').text(orderCode);
    $('#name-order').text(shipping.name);
    $('#email-order').text(shipping.email);
    $('#phone-order').text(shipping.phone);
    let deliverAddress = shipping.address + ", " + shipping.city;
    $('#deliver-order').text(deliverAddress);
}

function checkOnlinePayment() {
    let url = window.location.href;

    let url_parts = url.split("?");

    if (url_parts !== null && url_parts !== undefined) {
        let query_params = url_parts[1].split("&");

        let vnp_ResponseCode;
        for (let i = 0; i < query_params.length; i++) {
            let param_parts = query_params[i].split("=");
            if (param_parts[0] === "vnp_ResponseCode") {
                vnp_ResponseCode = param_parts[1];
                break;
            }
        }

        if (vnp_ResponseCode === "00") {
            let myNumber = 0;
            localStorage.setItem('numberOfItems', myNumber.toString());
            localStorage.removeItem("cart_id");
            updateOrderPayment();
        }
    }
}

function updateOrderPayment() {
    $.ajax({
        url: `/ecommerce/api/v1/client/order/${orderCode}`,
        type: 'PUT',
        dataType: 'json',
        success: function(res) {
            console.log("success");
        },
        error: function(e) {
            console.log(e);
        }
    })
}

function updateAccountInfo() {
    $.ajax({
        url : '/ecommerce/api/v1/client/account/cart',
        type : 'PUT',
        dataType: 'json',
        success: function(res) {
            console.log("success");
        },
        error: function(e) {
            console.log(e);
        }
    })
}