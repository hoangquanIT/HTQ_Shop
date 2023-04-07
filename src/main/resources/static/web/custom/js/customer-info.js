$(document).ready(async function() {
    await getCity();
    await getCustomerInfo();
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

// ====================== GET CUSTOMER INFO ======================
const CUSTOMER_INFO_URL = '/ecommerce/api/v1/client/account';
let customer_id = 0;
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
    customer_id = data.id;
    $('#account-fullname').val(data.name);
    $('#account-phone').val(data.phone);
    $('#account-email').val(data.email);
    $('#account-address').val(data.address);
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

// =============================== UPDATE CUSTOMER INFO ===============================
$('#account-update-btn').on('click', function(e){
    e.preventDefault();
    if ($('#account-info').valid()){
        updateCustomerInfo();
    }
})
function updateCustomerInfo(){
    $.ajax({
        url: CUSTOMER_INFO_URL,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'id' : customer_id,
            'name' : $('#account-fullname').val(),
            'phone' : $('#account-phone').val(),
            'address' : $('#account-address').val(),
            'cityCode' : getCitySelected(),
        }),
        success: function(res) {
            toastr.success("Cập nhật thông tin thành công");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function getCitySelected(){
    return $('#select-province option:selected').val();
}

// =============================== UPDATE PASSWORD ===============================
function updateCustomerPassword(){
    $.ajax({
        url: `${CUSTOMER_INFO_URL}/password`,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'oldPassword' : $('#old-password').val(),
            'newPassword' : $('#new-password').val()
        }),
        success: function(res){
            toastr.success("Cập nhật mật khẩu thành công");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#btn-change-password').on('click', function(e){
    e.preventDefault();
    if ($('#change-pass-form').valid()){
        updateCustomerPassword();
        clearForm();
        // $('#modal-change-password').removeClass('show')
        //     .removeAttr('aria-modal')
        //     .removeAttr('style')
        //     .attr('style', 'display:none')
        //     .attr('aria-hidden', 'true');
        document.querySelector('.modal').style.display = 'none';
        $('body').removeClass('modal-open').removeAttr('style');
        $('.modal-backdrop').hide();
    }
})

function clearForm(){
    $(':input','#change-pass-form')
        .not(':button, :submit, :reset, :hidden')
        .val('')
        .prop('checked', false)
        .prop('selected', false);
}