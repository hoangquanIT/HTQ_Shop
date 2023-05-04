// let $qtyInputs = $(".qty-input");
// let $inputs = $qtyInputs.find(".product-qty");
// let $countBtn = $qtyInputs.find(".qty-count");
// let qtyMin = parseInt($inputs.attr("min"));
// let qtyMax = parseInt($inputs.attr("max"));
// function getQtyBox(){
//     qtyMin = parseInt($inputs.attr("min"));
//     qtyMax = parseInt($inputs.attr("max"));
// }
//
// let QtyInput = (function () {
//     // var $qtyInputs = $(".qty-input");
//
//     if (!$qtyInputs.length) {
//         return;
//     }
//
//     // var $inputs = $qtyInputs.find(".product-qty");
//     // var $countBtn = $qtyInputs.find(".qty-count");
//     // var qtyMin = parseInt($inputs.attr("min"));
//     // var qtyMax = parseInt($inputs.attr("max"));
//
//     $inputs.change(function () {
//         var $this = $(this);
//         var $minusBtn = $this.siblings(".qty-count--minus");
//         var $addBtn = $this.siblings(".qty-count--add");
//         var qty = parseInt($this.val());
//
//         if (isNaN(qty) || qty <= qtyMin) {
//             $this.val(qtyMin);
//             $minusBtn.attr("disabled", true);
//         } else {
//             $minusBtn.attr("disabled", false);
//
//             if(qty >= qtyMax){
//                 $this.val(qtyMax);
//                 $addBtn.attr('disabled', true);
//             } else {
//                 $this.val(qty);
//                 $addBtn.attr('disabled', false);
//             }
//         }
//     });
//
//     $countBtn.click(function () {
//         var operator = this.dataset.action;
//         var $this = $(this);
//         var $input = $this.siblings(".product-qty");
//         var qty = parseInt($input.val());
//
//         if (operator == "add") {
//             qty += 1;
//             if (qty >= qtyMin + 1) {
//                 $this.siblings(".qty-count--minus").attr("disabled", false);
//             }
//
//             if (qty >= qtyMax) {
//                 $this.attr("disabled", true);
//             }
//         } else {
//             qty = qty <= qtyMin ? qtyMin : (qty -= 1);
//
//             if (qty == qtyMin) {
//                 $this.attr("disabled", true);
//             }
//
//             if (qty < qtyMax) {
//                 $this.siblings(".qty-count--add").attr("disabled", false);
//             }
//         }
//
//         $input.val(qty);
//     });
// })();

function changeQtyValue (obj) {
    let $element = $(obj);
    let qtyMin = parseInt($element.attr("min"));
    let qtyMax = parseInt($element.attr("max"));
    let $minusBtn = $element.siblings(".qty-count--minus");
    let $addBtn = $element.siblings(".qty-count--add");
    let qty = parseInt($element.val());

    if (isNaN(qty) || qty <= qtyMin) {
        $element.val(qtyMin);
        $minusBtn.attr("disabled", true);
        $addBtn.attr("disabled", false);
    } else {
        $minusBtn.attr("disabled", false);

        if(qty >= qtyMax){
            $element.val(qtyMax);
            $addBtn.attr('disabled', true);
        } else {
            $element.val(qty);
            $addBtn.attr('disabled', false);
        }
    }
}

function changeBtn (obj) {
    let operator = obj.dataset.action;
    let $element = $(obj);
    let $input = $element.siblings(".product-qty");
    let qtyMin = parseInt($input.attr("min"));
    let qtyMax = parseInt($input.attr("max"));
    let qty = parseInt($input.val());

    if (operator == "add") {
        qty += 1;
        if (qty >= qtyMin + 1) {
            $element.siblings(".qty-count--minus").attr("disabled", false);
        }

        if (qty >= qtyMax) {
            $element.attr("disabled", true);
        }
    } else {
        qty = qty <= qtyMin ? qtyMin : (qty -= 1);

        if (qty == qtyMin) {
            $element.attr("disabled", true);
        }

        if (qty < qtyMax) {
            $element.siblings(".qty-count--add").attr("disabled", false);
        }
    }

    $input.val(qty);
}

$(document).ready(async function() {
    await getProduct();
    // await getQtyBox();
    await getRelatedProducts();
})

// =========================== GET PRODUCT ===========================
const pathname = window.location.pathname.split("/");
const productId = pathname[pathname.length-1];
let product_data;
let variant_id;
let product_name;

// format sang tiền Việt
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}

function decodeId() {
    return parseInt(productId.slice(-6), 16);
}

async function getProduct(){
    let id = decodeId();
    await $.ajax({
        url: `/ecommerce/api/v1/client/product/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderProduct(data);
            product_data = data;
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderProduct(data){
    $('#product-name').text(data.name);
    product_name = data.name;
    $('#product-price').text(renderPrice(data));
    $('#product-content').text(data.content);
    $('#product-description').text(data.description);
    $('#variant-sku').text(data.variants[0].sku);
    variant_id = data.variants[0].id;
    renderSize(data.variants[0].color, data);
    renderColor(data);
    renderImage(data);
    renderQuantity(data.variants[0].quantity);
    deactiveButton(data.variants[0].quantity);
}

function renderPrice(data){
    let arr = data.variants;
    let price = arr[0].price;
    return formatVND(price);
}

function renderSize(color, data){
    let varArr = data.variants;
    let sizeArr = [];
    let html = '';

    let sku = $('#variant-sku').text();
    let checkedSize;
    varArr.forEach(el => {
        if(!sizeArr.includes(el.size) && el.color === color){
            sizeArr.push(el.size);
        }
        if (el.sku === sku){
            checkedSize = el.size;
        }
    })

    sizeArr.sort();

    // html = `
    //      <div class="form-check-option form-check-rounded">
    //         <input type="radio" name="product-option-sizes" value="${sizeArr[0]}"
    //         id="option-sizes-${sizeArr[0]}" onclick="getVariantBySize('${sizeArr[0]}')">
    //         <label for="option-sizes-${sizeArr[0]}">
    //             <small>${sizeArr[0]}</small>
    //         </label>
    //     </div>
    // `;


    for (let i = 0; i < sizeArr.length; i++) {
        html += `
            <div class="form-check-option form-check-rounded">
                <input type="radio" name="product-option-sizes" value="${sizeArr[i]}" 
                id="option-sizes-${sizeArr[i]}" onclick="getVariantBySize('${sizeArr[i]}')" ${checkedSize === sizeArr[i] ? 'checked' : ''}>
                <label for="option-sizes-${sizeArr[i]}">
                    <small>${sizeArr[i]}</small>
                </label>
            </div>
            `;
    }


    $('#product-size').html(html);
}

function getVariantBySize(size){
    let varArr = product_data.variants;
    let variant;

    varArr.forEach(el => {
        if(el.size === size){
            $('#variant-sku').text(el.sku);
            variant_id = el.id;
            variant = el;
        }
    })

    renderQuantity(variant.quantity);
    deactiveButton(variant.quantity);
}

function renderColor(data){
    let varArr = data.variants;
    let colorArr = [];
    let html = '';

    varArr.forEach(el => {
        if(!colorArr.includes(el.color)){
            colorArr.push(el.color);
        }
    })

    html = `
        <div
         class="form-group d-inline-block mr-1 mb-1 form-check-solid-bg-checkmark form-check-custom form-check-${parseColorToClassName(colorArr[0])}">
            <input type="radio" name="color-filter" class="form-check-color-input"
                   id="filter-colours-modal-${colorArr[0]}" value="${colorArr[0]}" checked onclick="getVariant('${colorArr[0]}')">
            <label class="form-check-label" for="filter-colours-modal-${colorArr[0]}" ${colorArr[0] === 'white' ? 'id="white-box"' : ''}></label>
        </div>
    `;

    if(colorArr.length > 1){
        for(let i = 1; i < colorArr.length; i++){
            html += `
                <div
                 class="form-group d-inline-block mr-1 mb-1 form-check-solid-bg-checkmark form-check-custom form-check-${parseColorToClassName(colorArr[i])}">
                    <input type="radio" name="color-filter" class="form-check-color-input"
                           id="filter-colours-modal-${colorArr[i]}" value="${colorArr[i]}" onclick="getVariant('${colorArr[i]}')">
                    <label class="form-check-label" for="filter-colours-modal-${colorArr[i]}" ${colorArr[i] === 'white' ? 'id="white-box"' : ''}></label>
                </div>
            `;
        }
    }

    $('#product-color').html(html);
}

function parseColorToClassName(color){
    switch (color) {
        case "purple":
            return "primary";
        case "green":
            return "success";
        case "red":
            return "danger";
        case "blue":
            return "info";
        case "yellow":
            return "warning";
        case "black":
            return "dark";
        case "gray":
            return "secondary";
        default:
            return "white";
    }
}

function renderQuantity(quantity){
    if (quantity === 0){
        $('#qty-minus').attr('disabled', true);
        $('#qty-plus').attr('disabled', true);
        $('#product-qty').val(0).attr('min', 0);
        $('#out-of-stock').text("Hết hàng");
    } else {
        $('#qty-minus').attr('disabled', false);
        $('#qty-plus').attr('disabled', false);
        $('#product-qty').val(1).attr('min', 1);
        $('#out-of-stock').text("");
    }
    $('#product-qty').attr('max', quantity);
    // getQtyBox();
}

function getVariant(color){
    let varArr = product_data.variants;
    let variant;

    varArr.forEach(el => {
        if(el.color === color){
            $('#variant-sku').text(el.sku);
            variant_id = el.id;
            variant = el;
        }
    })

    renderSize(color, product_data);
    renderQuantity(variant.quantity);
    deactiveButton(variant.quantity);
}

function deactiveButton(quantity){
    if (quantity === 0){
        $('#add-to-cart-btn').attr('disabled', true);
        $('#buy-now-btn').attr('disabled', true);
    } else {
        $('#add-to-cart-btn').attr('disabled', false);
        $('#buy-now-btn').attr('disabled', false);
    }
}

function renderImage(data){
    let images = data.images;
    let html = '';

    images.forEach(el => {
        html += `
             <div class="col-12">
                <picture>
                    <img class="img-fluid" data-zoomable src="${el.url}"
                        alt="">
                </picture>
            </div>
        `;
    })

    $('#product-image').html(html);
}


// =============================== ADD TO CART ===============================
const addToCart = async () => {
    let cart_id = localStorage.getItem("cart_id");
    await $.ajax({
        url: "/ecommerce/api/v1/client/cart",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            cartId: +cart_id,
            note: "",
            variantId: variant_id,
            quantity: $("#product-qty").val()
        }),
        success: function (res){
            toastr.success("Sản phẩm đã được thêm vào giỏ");
            localStorage.setItem("cart_id", res.cartId);
            renderCartItemsNumber(res);
            getProduct();
        },
        error: function (e){
            console.log(e);
            toastr.error("Thêm sản phẩm thất bại");
        }
    })
}

$('#add-to-cart-btn').on('click', function (){
    addToCart();
})

function renderCartItemsNumber(res){
    let cart_items_number = parseInt(localStorage.getItem('numberOfItems'));
    cart_items_number = cart_items_number + parseInt(res.quantity);
    localStorage.setItem('numberOfItems', cart_items_number.toString());
    renderCartNumber();
}

// =============================== BUY NOW ===============================
async function buyNow() {
    await addToCart();
    await redirectToCheckout();
}

function redirectToCheckout() {
    window.location.href = "/shop/checkout";
}

$('#buy-now-btn').on('click', function() {
    buyNow();
})


// =========================== RENDER SIMILAR PRODUCTS =========================

function getRelatedProducts() {
    let arr = product_name.split(' ');
    let keyword = arr[0] + ' ' + arr[1];
    $.ajax({
        url: `/ecommerce/api/v1/client/product/similar/${keyword}`,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            renderRelatedProducts(data);
        },
        error: function(e) {
            console.log(e)
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

function renderRelatedProducts(data) {
    let html = '';
    if (data != null) {
        data.forEach(el => {
            if (el.name !== product_name) {
                let img = el.images[0];
                html += `
                    <div class="swiper-slide col-12 col-sm-6 col-lg-3">
                        <!-- Card Product-->
                        <div
                            class="card border border-transparent position-relative overflow-hidden h-100 transparent">
                            <div class="card-img position-relative">
                                <div class="card-badges">
                                    <span class="badge badge-card"><span
                                            class="f-w-2 f-h-2 bg-success rounded-circle d-block me-1"></span>
                                        New In</span>
                                </div>
    <!--                            <span class="position-absolute top-0 end-0 p-2 z-index-20 text-muted"><i-->
    <!--                                    class="ri-heart-line"></i></span>-->
                                <picture class="position-relative overflow-hidden d-block bg-light">
                                    <img class="w-100 img-fluid position-relative z-index-10" title="" src="${img.url}" alt="">
                                </picture>
    <!--                            <div class="position-absolute start-0 bottom-0 end-0 z-index-20 p-2">-->
    <!--                                <button class="btn btn-quick-add"><i class="ri-add-line me-2"></i> Quick-->
    <!--                                    Add</button>-->
    <!--                            </div>-->
                            </div>
                            <div class="card-body px-0">
                                <a class="text-decoration-none link-cover" href="/shop/product/${encodeId(el.id)}">${el.name}</a>
                                <p class="mt-2 mb-0 small">${renderProductPrice(el)}</p>
                            </div>
                        </div>
                        <!--/ Card Product-->
                    </div>
                `;
            }
        });
    }
    if (html === '') {
        $('#related-area').css('display', 'none');
    } else {
        $('#related-products').append(html);
        let mySwiper = new Swiper('.swiper-container', {
            spaceBetween: 10,
            loop: true,
            autoplay: {
                delay: 5000,
                disableOnInteraction: false
            },
            navigation: {
                nextEl: '.swiper-next',
                prevEl: '.swiper-prev'
            },
            breakpoints: {
                600: {
                    slidesPerView: 2
                },
                1024: {
                    slidesPerView: 3
                },
                1400: {
                    slidesPerView: 4
                }
            }
        });
    }
}

function renderProductPrice(data){
    let arr = data.variants;
    let price = arr[0].price;
    return formatVND(price);
}