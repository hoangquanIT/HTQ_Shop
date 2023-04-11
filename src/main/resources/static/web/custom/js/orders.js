$(document).ready(function() {
    getOrders();
})

// ============================ GET ORDERS ============================
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}

function getOrders() {
    $.ajax({
        url: '/ecommerce/api/v1/client/orders',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            if ($.fn.DataTable.isDataTable('#dataTable')) {
                $('#dataTable').DataTable().clear().destroy();
            }
            renderOrders(data);
        },
        error: function(e) {
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderOrders(data) {
    $('#dataTable').DataTable({
        "columns": [
            {
                "data": "id",
                "render": function(data, type, row, meta) {
                    data = '<a href="/shop/orders/' + data + '">' + data + '</a>';
                    return data;
                }
            },
            {"data": "createdAt",},
            {
                "data": "total",
                "render": function(data, type, row, meta) {
                    data = formatVND(data);
                    return data;
                }
            },
            {"data": "status"},
            {"data": "payment"},
            {"data": "fulfillment"},
        ]
    }).rows.add(data).draw();
}