$('#search-btn').on('click', function(e){
    e.preventDefault();
    console.log("hello");
    let search = $('#search-input').val();
    window.location.href = `/shop/category?search=${search}`;
})