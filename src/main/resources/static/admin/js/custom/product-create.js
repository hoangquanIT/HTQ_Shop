$(document).ready(function(){
    getCategories();
})

// ==================================== GET CATEGORIES =================================
const CATEGORY_URL = '/ecommerce/api/v1/category';

function getCategories(){
    $.ajax({
        url: CATEGORY_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderCategoriesToSelectBox(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderCategoriesToSelectBox(data){
    let html = '';
    data.forEach(element => {
        html += `
            <option value="${element.id}">${element.name}</option>
        `;
    })
    $('#category').html(html);
}

// ==================================== CREATE PRODUCT =================================
const PRODUCT_URL = '/ecommerce/api/v1/product';
let formData = new FormData();

// Phiên bản sp
// 1. Thêm phiên bản
let idx = 1;

$('#add-variant').on("click", function() {
    let variantsList = document.getElementById("variants")
    let newVariant = document.createElement("div")
    newVariant.classList.add("row", "pt-3", "variant-obj")
    newVariant.setAttribute("id", `${idx}`)
    newVariant.innerHTML = `
        <div class="col-3">
            <label>Sku</label>
            <input type="text" class="form-control sku" name="sku"/>
        </div>
        <div class="col-3">
            <label>Màu sắc</label>
            <select class="select2 form-control color" name="color" id="color">
                <option value="black">Đen</option>
                <option value="white">Trắng</option>
                <option value="gray">Xám</option>
                <option value="red">Đỏ</option>
                <option value="blue">Xanh da trời</option>
                <option value="green">Xanh lá cây</option>
                <option value="yellow">Vàng</option>
                <option value="purple">Tím</option>
            </select>
        </div>
        <div class="col-2">
            <label>Kích thước</label>
            <input type="text" class="form-control size" name="size"/>
        </div>
        <div class="col-3">
            <label>Giá bán</label>
            <input type="text" class="form-control price" name="price"/>
        </div>
        <div class="col-1 d-flex align-items-end">
            <button class="btn btn-danger btn-flat btn-delete-variant" onclick="removeVariant(${idx})"
            style="height: 60%; border-radius: 0.25rem;">Xóa</button>
        </div>`
    idx++;
    variantsList.appendChild(newVariant)
})


// 2. Xóa phiên bản
const removeVariant = (id) => {
    const removeEl = document.getElementById(`${id}`);
    removeEl.remove();
}


$('#create-product').on('click', function(e){
    e.preventDefault();
    if ($('#result').val() == ''){
        toastr.error("Cần phải thêm ít nhất 1 ảnh");
    }
    else if ($('#variants').children().length === 0){
        toastr.error("Cần phải thêm ít nhất 1 phiên bản sản phẩm");
    }
    else if ($('#basic-info-product').valid()){
        createProduct();
    }
})

function createProduct(){
    formData.append("product", JSON.stringify({
        'name' : $('#name').val(),
        'content' : $('#content').val(),
        'description' : $('#description').val(),
        'categories' : getCategoriesChosen(),
        'variants' : getVariantsList(),
    }));
    $.ajax({
        url: PRODUCT_URL,
        type: 'POST',
        dataType: 'json',
        contentType: false,
        processData: false,
        data: formData,
        success: function(res){
            formData.delete("file[]")
            formData.delete("product")
            window.location.href = "/admin/products";
            toastr.success("Add Product successfully");
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
            formData.delete("product")
        }
    })
}

function getCategoriesChosen(){
    let categorySelect = document.getElementById("category");
    let categoryArr = [];
    let categoryOptionEl = categorySelect.querySelectorAll("option");
    categoryOptionEl.forEach(el => {
        if (el.selected) {
            categoryArr.push(+el.value)
        }
    })
    return categoryArr;
}

function getVariantsList() {
    let variantArr = [];
    const variantList = document.querySelectorAll(".variant-obj");
    variantList.forEach(variant => {
        let varSku = variant.querySelector(".sku")
        let varColor = variant.querySelector(".color")
        let varSize = variant.querySelector(".size")
        let varPrice = variant.querySelector(".price")
        let variantObj = {
            "sku": varSku.value,
            "color": varColor.value,
            "size": varSize.value,
            "price": varPrice.value
        }
        variantArr.push(variantObj);
    })
    return variantArr;
}

// Xử lý ảnh
const output = $("#result");
$('#input-file').on('change', function(e){
    const files = e.target.files;
    console.log(files)
    for (const file of files) {
        file.preview = URL.createObjectURL(file)
        formData.append("file[]", file)
    }

    previewFiles(formData);
})

// Hiển thị ảnh preview
const previewFiles = files => {
    output.html('');

    let html = "";
    files.getAll("file[]").forEach((file, index) => {
        html += `
                <div class="col-2 m-2">
                    <div class="row">
                        <div class="image-item col-12">
                            <img src="${file.preview}" alt="">
                        </div>
                        <div class="col-12 mt-2">
                            <button class="btn-delete-img" onclick=deleteImagePreview(${index})>Delete</button>
                        </div>
                    </div>
                </div>
                `;
    });

    output.html(html);
}

// Xóa ảnh preview
const deleteImagePreview = (index) => {
    const files = formData.getAll("file[]")
        .filter((file, i) => i !== index);

    formData.delete("file[]");
    for (const file of files) {
        formData.append("file[]", file)
    }

    previewFiles(formData);
}