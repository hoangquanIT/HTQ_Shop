$(document).ready(function() {
    getCategories();
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
