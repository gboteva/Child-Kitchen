

let countCouponsSelect = document.getElementById("buy_coupons-count");

countCouponsSelect.addEventListener("click", populatePrice)

let priceInput = document.getElementById("buy_coupons-price");

function populatePrice(e){
    e.preventDefault();
    let price =  (Number(countCouponsSelect.value) * 0.8).toFixed(2);

    priceInput.value = price + " лв."

}

