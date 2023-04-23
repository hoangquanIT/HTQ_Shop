$(document).ready(function() {
    getEmployees();
    getRoles();
})

// ============================= GET ROLES =============================
const ROLES_URL = '/ecommerce/api/v1/roles';

function getRoles(){
    $.ajax({
        url: ROLES_URL,
        type: 'GET',
        dataType: 'json',
        success: function(res){
            renderRolesToSelectBox(res);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderRolesToSelectBox(arr){
    let html = '';
    arr.forEach(element => {
        if (element.name != 'CUSTOMER') {
            html += `
                <option value="${element.id}">${element.name}</option>
            `;
        }
    })

    $('#select-roles').html(html);
}

// ============================= ADD EMPLOYEE =============================
const EMP_URL = '/ecommerce/api/v1/employee';

function addEmp(){
    $.ajax({
        url: EMP_URL,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'name' : $('#create-emp-mame').val(),
            'email' : $('#create-emp-email').val(),
            'phone' : $('#create-emp-phone').val(),
            'password' : $('#create-emp-password').val(),
            'roles' : getRolesChosen()
        }),
        success: function (res) {
            toastr.success("Thêm nhân viên thành công");
            getEmployees();
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function getRolesChosen(){
    let roleArr = [];
    let rolesSelect = document.getElementById("select-roles");
    let roles = rolesSelect.querySelectorAll("option");
    roles.forEach(el => {
        if (el.selected) {
            roleArr.push(el.value);
        }
    })
    return roleArr;
}

$('#create-emp-btn').on('click', function(e){
    e.preventDefault();
    if ($('#create-emp-form').valid()){
        addEmp();
        clearForm();
        $('#modal-default-emp').modal('hide');
        $('.modal-backdrop').hide();
    }
})

function clearForm(){
    $(':input','#create-emp-form')
        .not(':button, :submit, :reset, :hidden')
        .val('')
        .prop('checked', false)
        .prop('selected', false);
}

// ============================= GET EMPLOYEES =============================

// $('#dataTable').dataTable({
//     "bInfo" : false
// });

function getEmployees(){
    $.ajax({
        url: EMP_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            console.log(data);
            if ($.fn.DataTable.isDataTable('#dataTable')) {
                $('#dataTable').DataTable().clear().destroy();
            }
            renderEmployees(data);
        },
        error: function (e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderEmployees(data){
    // let html = '';
    // arr.forEach(element => {
    //     //let roles = generateRoleString(element.roles);
    //     html += `
    //         <tr>
    //             <td><a href="/admin/employee/${element.id}">${element.name}</a></td>
    //             <td>${element.email}</td>
    //             <td>${element.phone}</td>
    //             <td>${element.roles}</td>
    //             <td>${element.createdAt}</td>
    //             <td>${element.updatedAt}</td>
    //         </tr>
    //     `;
    // })
    // $('#emp-list').html(html);

    $('#dataTable').DataTable({
        "columns": [
            {
                "data": "id",
                "render": function(data, type, row, meta) {
                    //if (type === 'display') {
                        data = '<a href="/admin/employee/' + data + '">' + row.name + '</a>';
                    //}
                    return data;
                }
            },
            {"data": "email"},
            {"data": "phone"},
            {"data": "roles"},
            {"data": "createdAt"},
            {"data": "updatedAt"},
        ]
    }).rows.add(data).draw();
}

// function generateRoleString(arr){
//     let roleArrString = '';
//     arr.forEach(el => {
//         roleArrString += el + ' ';
//     })
//     return roleArrString;
// }
