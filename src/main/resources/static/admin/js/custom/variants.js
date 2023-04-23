$(document).ready(function() {
    getVariants();
})

// =============================== GET VARIANTS ===============================
const VARIANT_URL = '/ecommerce/api/v1/variant';

const formatVND = (obj) => {
    obj = obj.toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
    return obj;
}

function getVariants(){
    $.ajax({
        url: VARIANT_URL,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            if ($.fn.DataTable.isDataTable('#dataTable')) {
                $('#dataTable').DataTable().clear().destroy();
            }
            renderVariants(data);
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderVariants(data){
    $('#dataTable').DataTable({
        "columns": [
            {"data": "name"},
            {"data": "sku"},
            {"data": "color"},
            {"data": "size"},
            {"data": "quantity"},
            {
                "data": "price",
                "render": function (data, type, row) {
                    return formatVND(data);
                }
            },
            {
                "data": "id",
                "className": "text-center",
                "render": function (data, type, row) {
                    return `
                        <a href="javascript:void(0)" class="card-link" data-toggle="modal"
                            data-target="#modal-update-${data}" onclick="getOneVariant(${data})">Update
                        </a>
                        <!-- Update modal-->
                        <div class="modal fade" id="modal-update-${data}" style="display: none;"
                            aria-hidden="true">
                            <div class="modal-dialog">
                                <form class="modal-content" id="update-variant-form-${data}">
                                    <div class="modal-header">
                                        <h4 class="modal-title">Cập nhật phiên bản sản phẩm</h4>
                                        <button type="button" class="close" data-dismiss="modal"
                                            aria-label="Close">
                                            <span aria-hidden="true">×</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label>Sku</label>
                                            <input type="text" class="form-control updateSku" name="updateSku" 
                                            id="updateSku-${data}">
                                            <span id="sku-err-${data}" style="color: red;"></span>
                                        </div>
                                        <div class="form-group">
                                            <label>Màu sắc</label>
                                            <input type="text" class="form-control updateColor" id="updateColor" disabled>
                                        </div>
                                        <div class="form-group">
                                            <label>Kích thước</label>
                                            <input type="text" class="form-control updateSize" id="updateSize" disabled>
                                        </div>
                                        <div class="form-group">
                                            <label>Giá</label>
                                            <input type="number" min="0"
                                                class="form-control updatePrice" name="updatePrice"
                                                id="updatePrice-${data}">
                                            <span id="price-err-${data}" style="color: red;"></span>
                                        </div>
                                        <div class="form-group">
                                            <label>Tồn kho</label>
                                            <input type="number" min="0"
                                                class="form-control updateQuantity" name="updateQuantity"
                                                id="updateQuantity-${data}">
                                            <span id="quantity-err-${data}" style="color: red;"></span>
                                        </div>
                                    </div>
                                    <div class="modal-footer justify-content-between">
                                        <button type="button" class="btn btn-default"
                                            data-dismiss="modal">Hủy</button>
                                        <button type="button" class="btn btn-primary"
                                            onclick="updateVariant(${data})">Cập nhật phiên bản</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        &ensp; | &ensp;
                        <a href="javascript:void(0)" class="card-link" data-toggle="modal"
                            data-target="#modal-delete-${data}" style="color: red;">Delete</a>
                        <!--Delete modal-->
                        <div class="modal fade" id="modal-delete-${data}" style="display: none;"
                            aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h4 class="modal-title">Delete</h4>
                                        <button type="button" class="close" data-dismiss="modal"
                                            aria-label="Close">
                                            <span aria-hidden="true">×</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <p>Bạn có chắc chắn muốn xóa phiên bản sản phẩm này
                                            không?</p>
                                    </div>
                                    <div class="modal-footer justify-content-between">
                                        <button type="button" class="btn btn-default"
                                            data-dismiss="modal">Hủy</button>
                                        <button type="button"
                                            class="btn btn-primary" onclick="deleteVariant(${data})">Xóa</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    `;
                }
            }
        ]
    }).rows.add(data).draw();
}

// ------- GET ONE VARIANT -------
function getOneVariant(id){
    $.ajax({
        url: `${VARIANT_URL}/${id}`,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            renderUpdateVariant(data);
            $(`#sku-err-${id}`).text('');
            $(`#price-err-${id}`).text('');
            $(`#quantity-err-${id}`).text('');
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}

function renderUpdateVariant(data){
    $('.updateSku').val(data.sku);
    $('.updateColor').val(data.color);
    $('.updateSize').val(data.size);
    $('.updatePrice').val(data.price);
    $('.updateQuantity').val(data.quantity);
}

// ------------ UPDATE VARIANT ------------
function updateVariant(id){
    $.ajax({
        url: `${VARIANT_URL}/${id}`,
        type: 'PUT',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            'sku' : $(`#updateSku-${id}`).val(),
            'price' : $(`#updatePrice-${id}`).val(),
            'quantity' : $(`#updateQuantity-${id}`).val(),
        }),
        success: function(res){
            toastr.success("Cập nhật phiên bản sản phẩm thành công");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').hide();
            getVariants();
        },
        error: function(e){
            let res = e.responseJSON;
            if (res.message !== undefined){
                toastr.error(e.responseJSON.message);
            } else {
                for (const index in res) {
                    toastr.error(res[index]);
                }
                $(`#sku-err-${id}`).text(e.responseJSON.sku);
                $(`#price-err-${id}`).text(e.responseJSON.price);
                $(`#quantity-err-${id}`).text(e.responseJSON.quantity);
            }
            // toastr.error(e.responseJSON.message);
            // $(`#sku-err-${id}`).text(e.responseJSON.sku);
            // $(`#price-err-${id}`).text(e.responseJSON.price);
            // $(`#quantity-err-${id}`).text(e.responseJSON.quantity);
        }
    })
}

// ------------ DELETE VARIANT ------------
function deleteVariant(id){
    $.ajax({
        url: `${VARIANT_URL}/${id}`,
        type: 'DELETE',
        dataType: 'json',
        success: function(res){
            toastr.success("Xóa phiên bản sản phẩm thành công");
            $('body').removeClass('modal-open');
            $('.modal-backdrop').hide();
            getVariants();
        },
        error: function(e){
            toastr.error(e.responseJSON.message);
        }
    })
}