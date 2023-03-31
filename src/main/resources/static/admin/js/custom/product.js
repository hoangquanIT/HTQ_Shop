$(document).ready(function(){
    getCategories();
    getProduct();
})

// ==================================== GET CATEGORIES =================================
const CATEGORY_URL = '/ecommerce/api/v1/category';
let categoryArr;

function getCategories(){
    $.ajax({
        url: CATEGORY_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            categoryArr = data;
            //renderCategoriesToSelectBox(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

// function renderCategoriesToSelectBox(data){
//     let html = '';
//     data.forEach(element => {
//         html += `
//             <option value="${element.id}">${element.name}</option>
//         `;
//     })
//     $('#category').html(html);
// }

// ======================================== GET PRODUCT ========================================
const PRODUCT_URL = '/ecommerce/api/v1/product';
const pathname = window.location.pathname.split("/");
const productId = pathname[pathname.length-1];
let productObj;

const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}

function getProduct(){
    $.ajax({
        url: `${PRODUCT_URL}/${productId}`,
        type: "GET",
        dataType: "json",
        success: function(data){
            productObj = data;
            renderProduct(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderProduct(data){
    $('#name').val(data.name);
    $('#content').val(data.content);
    $('#description').val(data.description);
    renderChosenCategories(data.categories);
    renderVariants(data.variants);
    renderImg(data.images)
}

function renderChosenCategories(data){
    let html = "";
    categoryArr.forEach(el => {
        if(data.find(ele => ele.id === el.id)){
            html += `<option value="${el.id}" selected>${el.name}</option>`;
        } else {
            html += `<option value="${el.id}">${el.name}</option>`;
        }
    })
    $('#category').html(html);
}

// ===== VARIANTS =====
function renderVariants(data){
    let variantsList = document.getElementById("variants");
    let html = "";
    data.forEach(el => {
        html += `
        <div class="row pt-3 variant-obj" id="${el.id}">
            <div class="col-3">
                <label>Sku</label>
                <input type="text" class="form-control sku" value="${el.sku}" disabled/>
            </div>
            <div class="col-3">
                <label>Màu sắc</label>
                <input type="text" class="form-control color" value="${el.color}" disabled/>
            </div>
            <div class="col-2">
                <label>Kích thước</label>
                <input type="text" class="form-control size" value="${el.size}" disabled/>
            </div>
            <div class="col-3">
                <label>Giá bán</label>
                <input type="text" class="form-control price" value="${formatVND(el.price)}" disabled/>
            </div>
            <div class="col-1 d-flex align-items-end">
                <a href="javascript:void(0)" 
                    class="btn btn-danger btn-flat btn-delete-variant" 
                    data-toggle="modal"
                    data-target="#modal-delete-${el.id}"
                    style="height: 60%; border-radius: 0.25rem;">Xóa</a>
                <!--Delete modal-->
                <div class="modal fade" id="modal-delete-${el.id}" style="display: none;"
                    aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">Delete</h4>
                                <button type="button" class="close"
                                    data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p>Bạn có chắc chắn muốn xóa phiên bản
                                    sản phẩm này không?</p>
                            </div>
                            <div class="modal-footer justify-content-between">
                                <button type="button" class="btn btn-default"
                                    data-dismiss="modal">Hủy</button>
                                <button type="button" id="delete-variant-btn"
                                    class="btn btn-primary" onclick="removeVariant(${el.id})">Xóa</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>`;
    })
    variantsList.innerHTML = html;
}

//------- DELETE VARIANT -------
function removeVariant(id){
    $.ajax({
        url: `/ecommerce/api/v1/variant/${id}`,
        type: 'DELETE',
        dataType: 'json',
        success: function(res){
            productObj.variants = productObj.variants.filter(el => el.id !== id);
            const removeEl = document.getElementById(`${id}`);
            removeEl.remove();
            toastr.success("Delete Variant successfully");
            //$('.modal').modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').hide();
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

// ======================================== ADD VARIANT ========================================

function createVariant(){
    $.ajax({
        url: `/ecommerce/api/v1/product/${productId}/variant`,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'sku' : $('#createSku').val(),
            'size' : $('#createSize').val(),
            'color' : $('#createColor option:selected').val(),
            'price' : $('#createPrice').val(),
        }),
        success: function(res){
            console.log(res);
            toastr.success("Add Variant successfully");
            productObj.variants.push(res);
            console.log(productObj);
            renderVariants(productObj.variants);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#create-variant').on('click', function(e){
    e.preventDefault();
    if ($('#create-variant-form').valid()){
        createVariant();
        clearForm();
        $('.modal').modal('hide');
        $('.modal-backdrop').hide();
    }
})

function clearForm(){
    $(':input','#create-variant-form')
        .not(':button, :submit, :reset, :hidden')
        .val('')
        .prop('checked', false)
        .prop('selected', false);
}

// ===== IMAGE =====
function renderImg(images){
    let html = "";
    images.forEach(img => {
        html += `
            <div class="col-2 m-2" id="${img.id}">
                    <div class="row">
                        <div class="image-item col-12">
                            <img src="${img.url}" alt="">
                        </div>
                        <div class="col-12 mt-2">
                            <button class="btn-delete-img" onclick="removeImgProduct(${img.id})">Delete</button>
                        </div>
                    </div>
            </div>
        `
    })
    $('#result').html(html);
}

// ----- REMOVE IMAGE -----
function removeImgProduct(id){
    $.ajax({
        url: `/ecommerce/api/v1/image/${id}`,
        type: 'DELETE',
        dataType: 'json',
        success: function(res){
            productObj.images = productObj.images.filter(el => el.id !== id)
            renderImg(productObj.images)
            toastr.success("Delete Image successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

// ----- ADD IMAGE -----

$('#input-file').on('change', function(e){
    let files = e.target.files;
    let formData = new FormData();
    for (let i = 0; i < files.length; i++) {
        formData.append("file[]" ,files[i]);
    }
    addImage(formData);
})

function addImage(formData){
    $.ajax({
        url: `/ecommerce/api/v1/product/${productId}/image`,
        type: 'POST',
        dataType: 'json',
        contentType: false,
        processData: false,
        data: formData,
        success: function(res){
            res.forEach(newImg => {
                productObj.images.push(newImg);
            })
            renderImg(productObj.images);
            toastr.success("Add Image successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

// ===================================== UPDATE PRODUCT =====================================

$('#update-product').on('click', function(e){
    e.preventDefault();
    if ($('#update-basic-form').valid()){
        updateProduct();
    }
})

function updateProduct(){
    $.ajax({
        url: `${PRODUCT_URL}/${productId}`,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'name' : $('#name').val(),
            'content' : $('#content').val(),
            'description' : $('#description').val(),
            'categoryIds' : getChosenCategories()
        }),
        success: function(res){
            toastr.success("Update Product successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function getChosenCategories(){
    let categoryArr = [];
    let categoryOptionEl = document.getElementById("category").querySelectorAll("option");
    categoryOptionEl.forEach(el => {
        if (el.selected) {
            categoryArr.push(+el.value)
        }
    })
    return categoryArr;
}

// ===================================== DELETE PRODUCT =====================================

$('#btn-delete-product').on('click', function(e){
    deleteProduct();
    $('.modal').modal('hide');
    $('.modal-backdrop').hide();
})

function deleteProduct(){
    $.ajax({
        url: `${PRODUCT_URL}/${productId}`,
        type: 'DELETE',
        dataType: 'json',
        success: function(res){
            window.location.href = "/admin/products";
            toastr.success("Delete Product successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}