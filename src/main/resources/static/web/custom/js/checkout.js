$(document).ready(async function() {
    getCart();
    await getCity();
    let isLogin = localStorage.getItem('firstLogin');
    if (isLogin !== null) {
        await getCustomerInfo();
    }
})
// ======================== GET CART ITEMS ========================
let cartDto;
let totalPrice = 0;
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}
function getCart(){
    let cartId = localStorage.getItem('cart_id');
    $.ajax({
        url: `/ecommerce/api/v1/client/cart/${cartId}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            cartDto = data;
            renderCartItem(data);
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
    $('#note').text(data.note);
}


// ================================= GET CITY =================================
const CITY_API_URL = 'https://provinces.open-api.vn/api/p/';
let cityArr = [];
async function getCity(){
    await $.ajax({
        url: CITY_API_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderCity(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderCity(arr){
    let html = '';
    arr.forEach(element => {
        cityArr.push({code: element.code, name: element.name});
        html += `<option value="${element.code}">${element.name}</option>`;
    })
    $('#country').html(html);
}

// ====================== GET CUSTOMER INFO ======================
const CUSTOMER_INFO_URL = '/ecommerce/api/v1/client/account';
function getCustomerInfo(){
    $.ajax({
        url: CUSTOMER_INFO_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderInfo(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderInfo(data){
    $('#fullNameBilling').val(data.name);
    $('#phoneBilling').val(data.phone);
    $('#emailBilling').val(data.email);
    $('#addressBilling').val(data.address);
    renderSelectedCity(data.cityCode);
}

function renderSelectedCity(value){
    const opt = document.querySelectorAll("#country option");
    $('#country option').removeAttr("selected");
    opt.forEach(element => {
        if (element.value == value) {
            element.selected = true;
        }
    })
}

// =================================== CREATE ORDER ===================================
let orderCode;
$('#next-to-payment').on('click', async function(e){
    e.preventDefault();
    if ($('#checkout-form').valid()){
        await createOrder();
        setTimeout(await function() {
            localStorage.removeItem("cart_id");
            let myNumber = 0;
            localStorage.setItem('numberOfItems', myNumber.toString());
            window.location.href = "/shop/checkout/order/" + orderCode;
        }, 3000);
    }
})
async function createOrder(){
    let cart_id = localStorage.getItem("cart_id");
    await $.ajax({
        url: '/ecommerce/api/v1/client/checkout',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'note' : $('#note').val(),
            'total' : totalPrice,
            'cartId' : cart_id,
            'orderItems' : getOrderItems(),
            'shippingAddress' : getShippingAddress()
        }),
        success: function(res) {
            toastr.success("Đơn hàng được tạo thành công");
            orderCode = res.id;
        },
        error: function(e){
            toastr.error("Đã xảy ra lỗi, vui lòng thử lại");
            console.log(e);
        }
    })
}

function getOrderItems(){
    let items = cartDto.items;
    let orderItems = [];

    items.forEach(el => {
        let obj = {
            productId: el.productId,
            productName: el.productName,
            variantId: el.variant.id,
            variantSku: el.variant.sku,
            variantColor: el.variant.color,
            variantSize: el.variant.size,
            price: el.variant.price,
            quantity: el.quantity
        }
        orderItems.push(obj);
    })
    return orderItems;
}

function getShippingAddress(){
    return {
        name: $("#fullNameBilling").val(),
        phone: $("#phoneBilling").val(),
        email: $("#emailBilling").val(),
        address: $("#addressBilling").val(),
        city: getMyProvince()
    };
}

const getMyProvince = () => {
    const provinceElement = document.querySelector("#country");
    let pCode;
    let pValue;
    let provinceOptionEl = provinceElement.querySelectorAll("option");
    provinceOptionEl.forEach(el => {
        if(el.selected){
            pCode = +el.value;
            pValue = cityArr.find(proEl => proEl.code === pCode);
        }
    })
    if(pValue === undefined){
        return null;
    }
    return pValue.name;
}

$('#online-payment-btn').on('click', async function(e) {
    e.preventDefault();
    if ($('#checkout-form').valid()){
        await createOrder();
        await createPayment();
    }
})

function createPayment() {
    let baseUrl = window.location.origin;
    $.ajax({
        url: '/ecommerce/api/v1/client/payment',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'orderId' : orderCode,
            'total' : totalPrice,
            'baseUrl' : baseUrl
        }),
        success: function(data) {
            window.location.href = data.url;
        },
        error: function(e) {
            console.log(e);
        }
    })
}