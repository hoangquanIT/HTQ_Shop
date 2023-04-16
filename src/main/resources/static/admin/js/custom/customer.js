$(document).ready(async function() {
    await getCity();
    await getCustomer();
    getOrders();
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

// ============================== GET CUSTOMER ==============================
const pathname = window.location.pathname.split("/");
const cusId = pathname[pathname.length-1];
const CUSTOMER_URL = '/ecommerce/api/v1/customer';

function getCustomer(){
    $.ajax({
        url: `${CUSTOMER_URL}/${cusId}`,
        type: "GET",
        dataType: "json",
        success: function(data){
            renderCustomer(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderCustomer(data){
    $('#cus-name').val(data.name);
    $('#cus-email').val(data.email);
    $('#cus-phone').val(data.phone);
    $('#cus-address').val(data.address);
    renderSelectedCity(data.cityCode);
}

function renderSelectedCity(value){
    const opt = document.querySelectorAll("#select-province option");
    $('#select-province option').removeAttr("selected");
    opt.forEach(element => {
        if (element.value == value) {
            element.selected = true;
        }
    })
}

// ============================== UPDATE CUSTOMER ==============================

function updateCustomer(){
    $.ajax({
        url: CUSTOMER_URL,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'id' : cusId,
            'name' : $('#cus-name').val(),
            'phone' : $('#cus-phone').val(),
            'address' : $('#cus-address').val(),
            'cityCode' : getCitySelected(),
        }),
        success: function(res){
            toastr.success("Update Customer successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function getCitySelected(){
    return $('#select-province option:selected').val();
}

$('#btn-update-customer').on('click', function(e){
    e.preventDefault();
    if ($('#customer-info').valid()){
        updateCustomer();
    }
})

// ======================== GET ORDERS ========================
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}

function getOrders() {
    $.ajax({
        url: `/ecommerce/api/v1/order/customer/${cusId}`,
        type: "GET",
        dataType: "json",
        success: function(data){
            if ($.fn.DataTable.isDataTable('#dataTable')) {
                $('#dataTable').DataTable().clear().destroy();
            }
            renderOrders(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderOrders(data) {
    $('#dataTable').DataTable({
        "columns": [
            {"data": "id"},
            {"data": "createdAt"},
            {
                "data": "total",
                "render": function(data, type, row, meta) {
                    return formatVND(data);
                }
            }
        ]
    }).rows.add(data).draw();
}