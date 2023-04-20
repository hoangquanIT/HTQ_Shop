$(document).ready(function() {
    getProducts();
    renderSearch();
})

// ==================================== GET PRODUCTS BY CATEGORY ====================================

// format sang tiền Việt
const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}

function getProducts(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    $.ajax({
        url: `/ecommerce/api/v1/client/category/products?${urlParams}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderProductList(data);
            renderTitle(data);
            renderPagination(data);
        },
        error: function(e){
            console.log(e);
        }
    })
}

// ------ RENDER TITLE OF CATEGORY ------
function renderTitle(data){
    let text = data.categoryName + " (" + data.productPage.totalElements + ")";
    $('#category-title').text(text);
}

// ------ RENDER PRODUCTS ------
function renderProductList(data){
    let items = data.productPage.content;
    let html = "";
    items.forEach(el => {
        let img = el.images[0];
        html += `
            <div class="col-12 col-sm-6 col-lg-4">
                <!-- Card Product-->
                <div class="card border border-transparent position-relative overflow-hidden h-100 transparent">
                    <div class="card-img position-relative">
                        <div class="card-badges">
                                <span class="badge badge-card"><span
                                        class="f-w-2 f-h-2 bg-success rounded-circle d-block me-1"></span> New In</span>
                        </div>
<!--                        <span class="position-absolute top-0 end-0 p-2 z-index-20 text-muted"><i-->
<!--                                class="ri-heart-line"></i></span>-->
                        <picture class="position-relative overflow-hidden d-block bg-light">
                            <img class="w-100 img-fluid position-relative z-index-10" title=""
                                 src="${img.url}" alt="">
                        </picture>
<!--                        <div class="position-absolute start-0 bottom-0 end-0 z-index-20 p-2">-->
<!--                            <a href="/shop" class="btn btn-quick-add"><i class="ri-add-line me-2"></i> Thêm vào giỏ</a>-->
<!--                        </div>-->
                    </div>
                    <div class="card-body px-0">
                        <a class="text-decoration-none link-cover" href="/shop/product/${encodeId(el.id)}">${el.name}</a>
                        <p class="mt-2 mb-0 small">${renderProductPrice(el)}</p>
                    </div>
                </div>
                <!--/ Card Product-->
            </div>
        `;
    })
    $('#product-list').html(html);
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

function renderProductPrice(data){
    let arr = data.variants;
    let price = arr[0].price;
    return formatVND(price);
}

// ------ RENDER PAGINATION ------
function renderPagination(data){
    let pageInfo = data.productPage;
    let currentPage = data.currentPage;
    let totalPage = pageInfo.totalPages;
    let html = `<li class="page-item ${currentPage === 1 ? 'disabled' : ''}" id="prev-page">
                    <a class="page-link" href="javascript:void(0)" aria-label="Previous" onclick="changePage(${currentPage - 1})">
<!--                      <span aria-hidden="true"><i class="bi bi-chevron-left"></i></span>-->
<!--                       <span class="sr-only">Prev</span> -->
                        Prev
                    </a>
                </li>`;
    for (let i = 1; i <= totalPage; i++) {
        html += `<li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="javascript:void(0)" onclick="changePage(${i})">${i}</a>
                 </li>`;
    }
    html += `<li class="page-item ${currentPage === totalPage ? 'disabled' : ''}" id="next-page">
                <a class="page-link" href="javascript:void(0)" aria-label="Next" onclick="changePage(${currentPage + 1})">
<!--                  <span aria-hidden="true"><i class="bi bi-chevron-right"></i></span>-->
<!--                   <span class="sr-only">Next</span>-->
                    Next
                </a>
             </li>`;
    $("#product-pagination").html(html);
}

function changePage(page) {
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set("page", page);
    window.history.replaceState(page, "page", `?${urlParams}`);
    getProducts();
}

// =================================== FILTER =================================
$('#filter-btn').on('click', function(){
    getFilter();
})

function getFilter(){
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);

    // get price filter
    let price = $('input[name="price"]:checked');
    let min = price.attr("data-minval");
    let max = price.attr("data-maxval");
    if (min !== undefined && max !== undefined){
        if(min === "all" && max === "all"){
            urlParams.delete("min");
            urlParams.delete("max");
        } else {
            urlParams.set("min", min);
            urlParams.set("max", max);
        }
        window.history.replaceState(min, "min", `?${urlParams}`)
        window.history.replaceState(max, "max", `?${urlParams}`)
    }


    // get size filter
    let size = $('input[name="size-filter"]:checked').val();
    if (size !== undefined) {
        if(size === "all"){
            urlParams.delete("size")
        } else {
            urlParams.set("size", size);
        }
        window.history.replaceState(size, "size", `?${urlParams}`)
    }


    // get color filter
    let color = $('input[name="color-filter"]:checked').val();
    if (color !== undefined) {
        if(color === "all"){
            urlParams.delete("color");
        }
        else {
            urlParams.set("color", color);
        }
        window.history.replaceState(color, "color", `?${urlParams}`)
    }


    getProducts();
}

$('#sort-price').on('change', function(){
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);

    let sort = $('#sort-price option:selected').val();
    urlParams.set('sort', sort);
    window.history.replaceState(sort, "sort", `?${urlParams}`)

    getProducts();
})

// =================================== RENDER SEARCH PRODUCTS ===================================

function renderSearch(){
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    let search = urlParams.get('search');
    if (search !== undefined && search !== null){
        $('#search-title').text(`Kết quả tìm kiếm: "${search}"`);
    }
}