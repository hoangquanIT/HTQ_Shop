$(document).ready(function() {
    getEmpById();
})

// ======================================= GET EMPLOYEE =================================
const pathname = window.location.pathname.split("/");
const empId = pathname[pathname.length-1];
const EMP_URL = '/ecommerce/api/v1/employee';

function getEmpById(){
    $.ajax({
        url: `${EMP_URL}/${empId}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderEmp(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderEmp(data){
    $('#emp-name').val(data.name);
    $('#emp-email').val(data.email);
    $('#emp-phone').val(data.phone);
}

// ======================================= UPDATE EMPLOYEE =================================

function updateEmp(){
    $.ajax({
        url: EMP_URL,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'id': empId,
            'name': $('#emp-name').val(),
            'phone': $('#emp-phone').val(),
        }),
        success: function(res){
            toastr.success("Update Employee successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#btn-update').on('click', function(e){
    e.preventDefault();
    if ($('#employee-info').valid()){
        updateEmp();
    }
})

// ======================================= DELETE EMPLOYEE =================================

function deleteEmployee(){
    $.ajax({
        url: `${EMP_URL}/${empId}`,
        type: 'DELETE',
        dataType: 'json',
        success: function(res){
            toastr.success("Delete Employee successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#btn-delete-employee').on('click', function(e){
    deleteEmployee();
    $('.modal').modal('hide');
    $('.modal-backdrop').hide();
    window.location.href = "/admin/employees";
})
