$(document).ready(function() {
    countNewOrders();
    countTotalOrdersCurMonth();
})

// ======================= COUNT NEW ORDERS =======================
function countNewOrders() {
    $.ajax({
        url: '/ecommerce/api/v1/order/new',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#new-orders').text(data);
        },
        error: function(e) {
            console.log(e);
        }
    })
}

function countTotalOrdersCurMonth() {
    $.ajax({
        url: '/ecommerce/api/v1/order/month-total',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#total-orders').text(data);
        },
        error: function(e) {
            console.log(e);
        }
    })
}