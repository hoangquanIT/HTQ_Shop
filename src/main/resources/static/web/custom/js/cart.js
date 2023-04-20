function changeQtyValue (obj) {
    let $element = $(obj);
    let qtyMin = parseInt($element.attr("min"));
    let qtyMax = parseInt($element.attr("max"));
    let $minusBtn = $element.siblings(".qty-count--minus");
    let $addBtn = $element.siblings(".qty-count--add");
    let qty = parseInt($element.val());
    let id = $element.attr("id");
    let quantityDifference = qty - parseInt($element.attr('data-oldValue'));

    if (isNaN(qty) || qty <= qtyMin) {
        $element.val(qtyMin);
        quantityDifference = qtyMin - parseInt($element.attr('data-oldValue'));
        $minusBtn.attr("disabled", true);
        $addBtn.attr("disabled", false);
    } else {
        $minusBtn.attr("disabled", false);

        if(qty >= qtyMax){
            $element.val(qtyMax);
            quantityDifference = qtyMax - parseInt($element.attr('data-oldValue'));
            $addBtn.attr('disabled', true);
        } else {
            $element.val(qty);
            $addBtn.attr('disabled', false);
        }
    }
    updateCart(id, quantityDifference);
}

function changeBtn (obj) {
    let operator = obj.dataset.action;
    let $element = $(obj);
    let $input = $element.siblings(".product-qty");
    let qtyMin = parseInt($input.attr("min"));
    let qtyMax = parseInt($input.attr("max"));
    let qty = parseInt($input.val());
    let id = $input.attr("id");

    if (operator == "add") {
        qty += 1;
        updateCart(id, 1);
        if (qty >= qtyMin + 1) {
            $element.siblings(".qty-count--minus").attr("disabled", false);
        }

        if (qty >= qtyMax) {
            $element.attr("disabled", true);
        }
    } else {
        qty = qty <= qtyMin ? qtyMin : (qty -= 1);
        updateCart(id, -1);
        if (qty == qtyMin) {
            $element.attr("disabled", true);
        }

        if (qty < qtyMax) {
            $element.siblings(".qty-count--add").attr("disabled", false);
        }
    }

    $input.val(qty);
}

$(document).ready(function() {
    let cartId = localStorage.getItem('cart_id');
    if (cartId !== null) {
        getCart(cartId);
    } else {
        let html = '<h6 class="mt-3">Giỏ hàng của bạn đang trống</h6>';
        $('#checkout-link').removeAttr('href');
        $('#payment-link').removeAttr('href');
        $('#next-to-checkout').removeAttr('href').attr('style', 'cursor:not-allowed');
        $('#cart-items').html(html);
        $('#total-price').text(formatVND(0));
    }

})
// ========================== GET CART INFO ==========================
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}
function getCart(cartId){
    $.ajax({
        url: `/ecommerce/api/v1/client/cart/${cartId}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderCartItem(data);
        },
        error: function(e){
            console.log(e);
        }
    })
}

function encodeId(id) {
    let characters = '0123456789abcdef';
    let result = '';
    let charactersLength = characters.length;
    while (result.length < 10) {
        let randomChar = characters.charAt(Math.floor(Math.random() * charactersLength));
        result += randomChar;
    }

    return result + id.toString(16).padStart(6, '0');
}

function renderCartItem(data){
    let html = '';
    let totalPrice = 0;
    if (data != null && data.items.length !== 0){
        let arr = data.items;
        arr.forEach(obj => {
            totalPrice += obj.total;
            html += `
                                <!-- Cart Item-->
                <div class="row mx-0 py-4 g-0 border-bottom" id="cart-item-${obj.itemId}">
                    <div class="col-2 position-relative">
                        <picture class="d-block border">
                            <img class="img-fluid"
                                 src="${obj.imageUrl}"
                                 alt="HTML Bootstrap Template by Pixel Rocket">
                        </picture>
                    </div>
                    <div class="col-9 offset-1">
                        <div>
                            <h6 class="justify-content-between d-flex align-items-start mb-2">
                                <a href="/shop/product/${encodeId(obj.productId)}">${obj.productName}</a>
                                <button type="button" 
                                    data-toggle="modal" data-target="#modal-delete-${obj.itemId}">
                                    <span>×</span>
                                </button>
                                <!--Delete modal-->
                                <div class="modal fade" id="modal-delete-${obj.itemId}" style="display: none;"
                                    aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h4 class="modal-title">Xóa</h4>
                                                <button type="button" class="close"
                                                    data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <p>Bạn có chắc chắn muốn sản phẩm này khỏi giỏ hàng không?</p>
                                            </div>
                                            <div class="modal-footer justify-content-between">
                                                <button type="button" class="btn btn-default"
                                                    data-dismiss="modal">Hủy</button>
                                                <button type="button" id="delete-variant-btn"
                                                    class="btn btn-primary" onclick="deleteCartItem(${obj.itemId}, ${obj.variant.id})">Xóa</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </h6>
                            <div class="d-block text-muted fw-bolder text-uppercase fs-9">
                                Size: <span id="product-size">${obj.variant.size}</span> /
                                Màu: <span id="product-color">${obj.variant.color}</span>
                            </div>
                            <div class="qty-input">
                                <button class="qty-count qty-count--minus" data-action="minus"
                                        type="button" onclick="changeBtn(this)" ${obj.quantity === 1 ? 'disabled' : ''}>-
                                </button>
                                <input class="product-qty" type="number" name="product-qty" min="1" 
                                       id="${obj.variant.id}" data-oldValue="${obj.quantity}"
                                       max="${obj.variant.quantity + obj.quantity}" value="${obj.quantity}" onchange="changeQtyValue(this)">
                                <button class="qty-count qty-count--add" data-action="add"
                                        type="button" onclick="changeBtn(this)" ${obj.variant.quantity === 0 ? 'disabled' : ''}>+
                                </button>
                            </div>
                        </div>
                        <p class="fw-bolder text-end text-muted m-0">${formatVND(obj.total)}</p>
                    </div>
                </div> <!-- / Cart Item-->
            `;
        })
    } else {
        html = '<h6 class="mt-3">Giỏ hàng của bạn đang trống</h6>';
        $('#checkout-link').removeAttr('href');
        $('#payment-link').removeAttr('href');
        $('#next-to-checkout').removeAttr('href').attr('style', 'cursor:not-allowed');
    }
    $('#cart-items').html(html);
    $('#total-price').text(formatVND(totalPrice));
}

// ===================== UPDATE CART ITEM =====================
const updateCart = (variantId, quantity) => {
    let cart_id = localStorage.getItem("cart_id");
    $.ajax({
        url: "/ecommerce/api/v1/client/cart",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            cartId: +cart_id,
            note: "",
            variantId: variantId,
            quantity: quantity
        }),
        success: function (res){
            let currentQty = parseInt(localStorage.getItem('numberOfItems'));
            localStorage.setItem('numberOfItems', (currentQty + quantity).toString());
            getCart();
        },
        error: function (e){
            console.log(e);
        }
    })
}

// ===================== DELETE CART ITEM =====================
function deleteCartItem(id, variantId){
    let quantity = $(`#${variantId}`).val();
    $.ajax({
        url: `/ecommerce/api/v1/client/cart/item/${id}`,
        type: "DELETE",
        dataType: 'json',
        success: function (){
            $(`#cart-item-${id}`).remove();
            let currentQty = parseInt(localStorage.getItem('numberOfItems'));
            localStorage.setItem('numberOfItems', (currentQty - quantity).toString());
            toastr.success("Sản phẩm đã được xóa khỏi giỏ hàng");
            let cartId = localStorage.getItem('cart_id');
            getCart(cartId);
            $('body').removeClass('modal-open');
            $('.modal-backdrop').hide();
        },
        error: function (e) {
            toastr.error("Đã có lỗi xảy ra");
            console.log(e);
        }
    })
}