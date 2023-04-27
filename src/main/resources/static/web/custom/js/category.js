$(document).ready(async function() {
    getCategories();
    if (localStorage.getItem('firstLogin') === 'true'){
        await getCartNumberInfo();
        localStorage.setItem('firstLogin', 'false');
    }
    if (localStorage.getItem('numberOfItems') === null) {
        let myNumber = 0;
        localStorage.setItem('numberOfItems', myNumber.toString());
    }
    await renderCartNumber();
})

// ==================================== GET CATEGORIES =================================
const CATEGORY_URL = '/ecommerce/api/v1/client/category';

function getCategories(){
    $.ajax({
        url: CATEGORY_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderCategoriesToMenu(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderCategoriesToMenu(data){
    let html = '';
    data.forEach(element => {
        html += `
            <li class="nav-item dropdown dropdown position-static">
              <a class="nav-link" href="/shop/category?category=${element.id}" role="button">
                ${element.name}
              </a>
            </li>
        `;
    });
    html += `
        <li class="nav-item">
          <a class="nav-link" href="/shop/category" role="button">
            Tất cả sản phẩm
          </a>
        </li>
    `;
    $('#menu-category').html(html);
}

// ========== GET CUSTOMER INFO FOR CART NUMBER ==========
async function getCartNumberInfo(){
    await $.ajax({
        url: '/ecommerce/api/v1/client/account/cartNumbers',
        type: 'GET',
        dataType: 'json',
        success: function(data){
            let count = 0;
            if (data.id !== null) {
                let items = data.cartItems;
                items.forEach(el => {
                    count += el.quantity;
                })
                localStorage.setItem('cart_id', data.id);
            } else {
                let id = localStorage.getItem('cart_id');
                if (id !== null) {
                    deleteCart();
                    localStorage.removeItem("cart_id");
                }
            }
            localStorage.setItem('numberOfItems', count.toString());
        },
        error: function(e){
            console.log(e);
        }
    })
}

function renderCartNumber(){
    let cart_items_number = parseInt(localStorage.getItem('numberOfItems'));
    let cart = $('#number-items-cart');
    if (cart_items_number === 0) {
        cart.text("");
        cart.removeClass('checkout-item-qty');
    } else {
        cart.text(cart_items_number);
        cart.addClass('checkout-item-qty');
    }
}

// ===================== LOGOUT ======================
$('#btn-logout').on('click', function(){
    if (window.confirm('Bạn có chắc muốn đăng xuất chứ?')) {
        let myNumber = 0;
        localStorage.setItem('numberOfItems', myNumber.toString());
        localStorage.removeItem("cart_id");
        localStorage.removeItem("firstLogin");
        window.location.href = "/client/logout";
    }
})

function deleteCart(){
    let id = localStorage.getItem('cart_id');
    $.ajax({
        url: `/ecommerce/api/v1/client/cart/${id}`,
        type: "DELETE",
        dataType: "json",
        success: function(res){

        },
        error: function(e){
            console.log(e);
        }
    })
}