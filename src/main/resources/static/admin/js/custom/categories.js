$(document).ready(function() {
    getCategories();
})

// ==================================== ADD CATEGORY =================================
const CATEGORY_URL = '/ecommerce/api/v1/category';

function addCategory() {
    $.ajax({
        url: CATEGORY_URL,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'name': $('#createName').val(),
            'description': $('#createDescription').val()
        }),
        success: function(res){
            toastr.success("Add Category successfully");
            getCategories();
        },
        error: function(e){
            //$('#createName-err').text(e.responseJSON.name);
            //toastr.error(e.responseJSON.message);
            let res = e.responseJSON;
            if (res.message !== undefined){
                toastr.error(res.message);
                $('#createName-err').text(res.message);
            } else {
                for (const index in res) {
                    toastr.error(res[index]);
                }
                $('#createName-err').text(res.name);
            }
        }
    })
}

$('#create-category').on('click', function(e){
    e.preventDefault();
    if ($('#form-category').valid()){
        addCategory();
        if ($('#createName-err').text() == '') {
            clearForm();
            $('#modal-default-catetory').modal('hide');
            $('.modal-backdrop').hide();
        }
    }
})

$('#cancel-category').on('click', function(){
    clearForm();
})

function clearForm(){
    $(':input','#form-category')
        .not(':button, :submit, :reset, :hidden')
        .val('')
        .prop('checked', false)
        .prop('selected', false);
    $('#createName-err').text('');
}

// ==================================== GET CATEGORIES =================================

function getCategories(){
    $.ajax({
        url: CATEGORY_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderCategories(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

$('#dataTable').dataTable({
    "bInfo" : false
});

function renderCategories(arr){
    let html = '';
    arr.forEach(element => {
        html += `
            <tr>
                <td>${element.id}</td>
                <td>${element.name}</td>
                <td>${element.description}</td>
                <td>${element.updatedAt}</td>
                <td>
                    <div class="row">
                        <div class="col-5 text-center">
                            <a href="javascript:void(0)" class="card-link"
                                data-toggle="modal" data-target="#modal-update-${element.id}">Update
                            </a>
                        </div>
                        <!-- Update modal-->
                        <div class="modal fade" id="modal-update-${element.id}" style="display: none;"
                            aria-hidden="true">
                            <div class="modal-dialog">
                                <form class="modal-content" id="update-cate-form">
                                    <div class="modal-header">
                                        <h4 class="modal-title">Cập nhật nhóm sản phẩm</h4>
                                        <button type="button" class="close"
                                            data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">×</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label>Mã nhóm</label>
                                            <input type="text" class="form-control updateId"
                                                value="${element.id}"
                                                disabled>
                                        </div>
                                        <div class="form-group">
                                            <label for="updateName">Tên nhóm</label>
                                            <input type="text"
                                                class="form-control updateName" name="updateName" 
                                                value="${element.name}">
                                            <span id="update-name-err"></span>
                                        </div>
                                        <div class="form-group">
                                            <label for="updateDescription">Mô tả ngắn</label>
                                            <input type="text"
                                                class="form-control updateDescription" name="update-cate-description"
                                                value="${element.description}">
                                        </div>
                                    </div>
                                    <div class="modal-footer justify-content-between">
                                        <button type="button" class="btn btn-default"
                                            data-dismiss="modal">Hủy</button>
                                        <button type="button"
                                            class="btn btn-primary" onclick="updateCategoryEvent(${element.id})">Lưu</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="col-2 text-center">|</div>
                        <div class="col-5 text-center">
                            <a href="javascript:void(0)" class="card-link"
                                data-toggle="modal"
                                data-target="#modal-delete-${element.id}"
                                style="color: red;">Delete</a>
                        </div>
                        <!--Delete modal-->
                        <div class="modal fade" id="modal-delete-${element.id}" style="display: none;"
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
                                        <p>Bạn có chắc chắn muốn xóa nhóm
                                            sản phẩm này không?</p>
                                    </div>
                                    <div class="modal-footer justify-content-between">
                                        <button type="button" class="btn btn-default"
                                            data-dismiss="modal">Hủy</button>
                                        <button type="button" id="delete-category-btn"
                                            class="btn btn-primary" onclick="deleteCategoryEvent(${element.id})">Xóa</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        `;
    })
    $('#category-list').html(html);
}

// ==================================== UPDATE CATEGORY =================================

function updateCategory(id){
    let modalUpdate = document.querySelector(`#modal-update-${id}`)
    let nameUpdateEl = modalUpdate.querySelector(".updateName").value;
    let descriptionUpdateEl = modalUpdate.querySelector(".updateDescription").value;
    if (nameUpdateEl == ''){
        toastr.error('Tên nhóm sản phẩm không được để trống');
        getCategories();
        return;
    }
    $.ajax({
        url: `${CATEGORY_URL}/${id}`,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'name': nameUpdateEl,
            'description': descriptionUpdateEl
        }),
        success: function(data){
            toastr.success("Update Category successfully");
            getCategories();
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
            getCategories();
        }
    })
}

function updateCategoryEvent(id){
    if ($('#update-cate-form').valid()){
        updateCategory(id);
        $('.modal').modal('hide');
        $('.modal-backdrop').hide();
    }
}

// ==================================== DELETE CATEGORY =================================

function deleteCategory(id){
    $.ajax({
        url: `${CATEGORY_URL}/${id}`,
        type: 'DELETE',
        dataType: 'json',
        success: function(res){
            toastr.success("Delete Category successfully");
            getCategories();
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function deleteCategoryEvent(id){
    deleteCategory(id);
    $('.modal').modal('hide');
    $('.modal-backdrop').hide();
}