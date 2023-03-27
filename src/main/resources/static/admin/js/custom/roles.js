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
            renderRoles(res);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderRoles(arr){
    let html = '';
    arr.forEach(element => {
        html += `
            <tr>
                <td>${element.id}</td>
                <td>${element.name}</td>
                <td>${element.createdAt}</td>
            </tr>
        `;
    })
    $('#list-role').html(html);
}