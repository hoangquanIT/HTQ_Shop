$(document).ready(function() {
    getInfo();
})

// =============================== GET EMP INFO =============================
const DETAIL_URL = '/ecommerce/api/v1/employee/detail';

function getInfo(){
    $.ajax({
        url: DETAIL_URL,
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
    $('#info-name').val(data.name);
    $('#info-email').val(data.email);
    $('#info-phone').val(data.phone);
}

// =============================== UPDATE EMP INFO =============================
