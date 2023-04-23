$(document).ready(async function() {
    await getCity();
    await getCustomers();
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


// ================================= ADD CUSTOMER =================================
const CUSTOMER_URL = '/ecommerce/api/v1/customer';

function addCustomer(){
    $.ajax({
        url: CUSTOMER_URL,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'name': $('#create-cus-mame').val(),
            'email': $('#create-cus-email').val(),
            'phone': $('#create-cus-phone').val(),
            'password': $('#create-cus-password').val(),
            'address': $('#create-cus-address').val(),
            'cityCode': getChoseCityCode(),
        }),
        success: function(res){
            toastr.success("Thêm khách hàng thành công");
            getCustomers();
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function getChoseCityCode(){
    return $('#select-province option:selected').val();
}

$('#create-customer').on('click', function(e){
    e.preventDefault();
    if ($('#create-cus-form').valid()){
        addCustomer();
        clearForm();
        $('#modal-default-cus').modal('hide');
        $('.modal-backdrop').hide();
    }
})

function clearForm(){
    $(':input','#create-cus-form')
        .not(':button, :submit, :reset, :hidden')
        .val('')
        .prop('checked', false)
        .prop('selected', false);
}

// ================================= GET CUSTOMERS =================================

function getCustomers(){
    $.ajax({
        url: CUSTOMER_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            if ($.fn.DataTable.isDataTable('#dataTable')) {
                $('#dataTable').DataTable().clear().destroy();
            }
            renderCustomers(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderCustomers(data){
    $('#dataTable').DataTable({
        "columns": [
            {
                "data": "id",
                "render": function(data, type, row, meta) {
                    data = '<a href="/admin/customer/' + data + '">' + row.name + '</a>';
                    return data;
                }
            },
            {"data": "email"},
            {"data": "phone"},
            {
                "data": "cityCode",
                "render": function(data, type, row, meta) {
                    data = getCityName(data);
                    return data;
                }
            },
            {"data": "createdAt"},
        ]
    }).rows.add(data).draw();
}

function getCityName(cityCode){
    let cityName = '';
    cityArr.forEach(el => {
        if (el.code == cityCode){
            cityName = el.name;
        }
    })

    return cityName;
}