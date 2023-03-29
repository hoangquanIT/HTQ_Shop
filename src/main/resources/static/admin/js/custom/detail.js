$(document).ready(function() {
    getInfo();
})

// =============================== GET EMP INFO =============================
const DETAIL_URL = '/ecommerce/api/v1/employee/detail';
let infoId = 0;
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
    infoId = data.id;
    $('#info-name').val(data.name);
    $('#info-email').val(data.email);
    $('#info-phone').val(data.phone);
}

// =============================== UPDATE EMP INFO =============================

function updateInfo(){
    $.ajax({
        url: DETAIL_URL,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'id' : infoId,
            'name' : $('#info-name').val(),
            'phone' : $('#info-phone').val()
        }),
        success: function(res){
            toastr.success("Update Information successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#btn-update').on('click', function(e){
    e.preventDefault();
    if ($('#account-info').valid()){
        updateInfo();
    }
})

// =============================== UPDATE PASSWORD =============================
function updatePassword(){
    $.ajax({
        url: `${DETAIL_URL}/password`,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'oldPassword' : $('#old-password').val(),
            'newPassword' : $('#new-password').val()
        }),
        success: function(res){
            toastr.success("Update Password successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#btn-change-password').on('click', function(e){
    e.preventDefault();
    if ($('#change-pass-form').valid()){
        updatePassword();
        clearForm();
        $('#modal-change-password').modal('hide');
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