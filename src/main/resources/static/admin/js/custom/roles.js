$(document).ready(function() {
    getRoles();
})

// ====================================== GET ROLES =================================
const URL = '/ecommerce/api/v1/roles';

function getRoles(){
    $.ajax({
        url: URL,
        type: 'GET',
        dataType: 'json',
        success: function(res){
            console.log(res);
            if ($.fn.DataTable.isDataTable('#dataTable')) {
                $('#dataTable').DataTable().clear().destroy();
            }
            renderRoles(res);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderRoles(data){
    $('#dataTable').DataTable({
        "columns": [
            {"data": "id"},
            {"data": "name"},
            {"data": "createdAt"},
        ]
    }).rows.add(data).draw();
}