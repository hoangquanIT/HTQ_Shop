$(document).ready(async function() {
    await getCity();
    await getOrder();
})

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
    $('#select-province').html(html);
}

// ========================== GET ORDER ==========================
const pathname = window.location.pathname.split("/");
const orderId = pathname[pathname.length-1];
let totalPrice = 0;
let shippingId;

const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}

function getOrder() {
    $.ajax({
        url: `/ecommerce/api/v1/order/${orderId}`,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            renderOrderInfo(data);
        },
        error: function(e) {
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderOrderInfo(data) {
    if (data.account != null) {
        renderAccountInfo(data.account);
    }
    renderShippingAddress(data.shippingAddress);
    renderOrderItems(data.orderItems);
    renderOrderState(data);
    $('#note').val(data.note);
}

function renderAccountInfo(data) {
    $('#account-name').text(data.name);
    $('#account-email').text(data.email);
    $('#account-phone').text(data.phone);
    $('#account-city').text(getCityName(data.cityCode));
    $('#account-address').text(data.address);
}

function getCityName(data) {
    let cityName = '';
    cityArr.forEach(el => {
        if (el.code == data){
            cityName = el.name;
        }
    })

    return cityName;
}

function renderShippingAddress(data) {
    shippingId = data.id;
    $('#shipping-name').text(data.name);
    $('#shipping-email').text(data.email);
    $('#shipping-phone').text(data.phone);
    $('#shipping-city').text(data.city);
    $('#shipping-address').text(data.address);

    $('#updateName').val(data.name);
    $('#updateEmail').val(data.email);
    $('#updatePhone').val(data.phone);
    renderSelectedCity(data.city);
    $('#updateAddress').val(data.address);
}

function renderSelectedCity(value){
    const opt = document.querySelectorAll("#select-province option");
    $('#select-province option').removeAttr("selected");
    opt.forEach(element => {
        if (element.text == value) {
            element.selected = true;
        }
    })
}


function renderOrderItems(data) {
    let html = '';

    data.forEach(el => {
        totalPrice += el.quantity * el.price;
        html += `
            <tr>
                <td>${el.productName}</td>
                <td>${el.quantity}</td>
                <td>${formatVND(el.price)}</td>
                <td>${formatVND(el.quantity * el.price)}</td>
            </tr>
        `;
    })

    $('#list-items').html(html);
    $('#order-total').text(formatVND(totalPrice));
}

function renderOrderState(data) {
    let statusEl = document.getElementById("select-status");
    let paymentEl = document.getElementById("select-payment");
    let fulfillmentEl = document.getElementById("select-fulfillment");

    renderSelect(statusEl, data.status);
    renderSelect(paymentEl, data.payment);
    renderSelect(fulfillmentEl, data.fulfillment);
}

function renderSelect(obj, data) {
    let options = obj.querySelectorAll("option");
    options.forEach(el => {
        if (el.value === data) {
            el.selected = true;
        }
    })
}


// ======================= UPDATE SHIPPING ADDRESS =======================
$('#btn-update-shipping').on('click', function(e) {
    e.preventDefault();
    if ($('#shipping-address-form').valid()) {
        updateShippingAddress();
        clearForm();
    }
})
function updateShippingAddress() {
    $.ajax({
        url: `/ecommerce/api/v1/order/shipping-address/${shippingId}`,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'name' : $('#updateName').val(),
            'email' : $('#updateEmail').val(),
            'phone' : $('#updatePhone').val(),
            'address' : $('#updateAddress').val(),
            'city' : getCityName($('#select-province option:selected').val()),
        }),
        success: function (data) {
            toastr.success("Update Shipping address successfully");
            renderShippingAddress(data);
            $('body').removeClass('modal-open');
            $('.modal').modal('hide');
            $('.modal-backdrop').hide();
        },
        error: function (e) {
            toastr.error(e.responseJSON.message);
        }
    })
}

function clearForm(){
    $(':input','#create-variant-form')
        .not(':button, :submit, :reset, :hidden')
        .val('')
        .prop('checked', false)
        .prop('selected', false);
}

// ========================= UPDATE ORDER STATE =========================
$('#btn-update-order').on('click', function() {
    updateOrder();
})
function updateOrder() {
    $.ajax({
        url: `/ecommerce/api/v1/order/${orderId}`,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'note' : $('#note').val(),
            'status' : $('#select-status option:selected').val(),
            'payment' : $('#select-payment option:selected').val(),
            'fulfillment' : $('#select-fulfillment option:selected').val(),
        }),
        success: function(res) {
            toastr.success("Update Order successfully");
        },
        error: function(e) {
            toastr.error(e.responseJSON.message);
        }
    })
}