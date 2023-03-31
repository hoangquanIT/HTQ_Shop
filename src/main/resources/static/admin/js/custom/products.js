$(document).ready(function() {
    getProducts();
})

// ======================================= GET PRODUCTS =======================================
const PRODUCT_URL = '/ecommerce/api/v1/product';

function getProducts(){
    $.ajax({
        url: PRODUCT_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            if ($.fn.DataTable.isDataTable('#dataTable')) {
                $('#dataTable').DataTable().clear().destroy();
            }
            renderProducts(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderProducts(data){
    $('#dataTable').DataTable({
        "columns": [
            {"data": "id"},
            {
                "data": "name",
                "render": function(data, type, row, meta) {
                    data = '<a href="/admin/product/' + row.id + '">' + data + '</a>';
                    return data;
                }
            },
            {"data": "description"},
            {"data": "createdAt"},
        ]
    }).rows.add(data).draw();
}