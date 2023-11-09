let resultCollection = {};
let resultSelect = document.getElementById("user-result");
let dateSelect = document.getElementById("delete-order-date");
function searchUser(e){

    let keyWord = document.getElementById("add-applicant-name");

    if(keyWord.value === ''){
        return;
    }

    let resultSelect = document.getElementById("user-result");
    resultSelect.innerHTML = '';
    let optAdd = document.createElement("option");
    optAdd.text = "Виж"
    resultSelect.appendChild(optAdd)


    fetch(`http://localhost:8080/api/admin/add-delete-order/search?addApplicantName=${keyWord.value}`)
        .then(response => response.json())
        .then(data =>{data.forEach(user => {
                    let option = document.createElement("option");
                    option.value = user.userEmail;
                    option.text = user.userEmail;
                    resultSelect.appendChild(option);
                    resultCollection[user.userEmail] = user;
                }
            )
        })
        .catch(e => console.log(e))

}

function populateFields(){
    let childrenSelect = document.getElementById("add-applicant-child");

    let deleteOrderChildren = document.getElementById("delete-applicant-child");

    let hiddenMail = document.getElementById("hiddenMail");

    childrenSelect.innerHTML = '';

    deleteOrderChildren.innerHTML = '';
    let optAdd = document.createElement("option");
    optAdd.text = "Виж"
    deleteOrderChildren.appendChild(optAdd)

    dateSelect.innerHTML = '';

    let servicePoint = document.getElementById("add-order-point");

    let object = resultCollection[resultSelect.value];

    for(let child of object.childrenNames){
        let optionAdd = document.createElement("option");
        optionAdd.value = child;
        optionAdd.text = child;
        childrenSelect.appendChild(optionAdd);

        let optionDel = document.createElement("option");
        optionDel.value = child;
        optionDel.text = child;
        deleteOrderChildren.appendChild(optionDel);
    }

    for(op of servicePoint.options){
        if (op.text === object.servicePointName){
            op.selected = "true";
        }
    }

}

function populateOrders(){
    let childName = document.getElementById("delete-applicant-child").value;
    let userEmail = resultSelect.value;

    dateSelect.innerHTML = '';

    fetch(`http://localhost:8080/api/admin/add-delete-order/allOrders?childName=${childName}&&userEmail=${userEmail}`)
        .then(response => response.json())
        .then(data => data.forEach(date => {
            let option = document.createElement("option");
            option.value = date;
            option.text = date;
            dateSelect.appendChild(option);
        }))
        .catch(e => console.log(e))
}

function populateUserData(){
    let nameInput = document.getElementById("edit-name");
    let citySelect = document.getElementById("edit-city")
    let servicePointSelect = document.getElementById("edit-service-point");
    let phoneInput = document.getElementById("info-tel-edit");
    let emailInput = document.getElementById("edit-email");
    let deleteEmailInput = document.getElementById("hiddenMail");

    let userData = resultCollection[resultSelect.value];

    for(let option of servicePointSelect.options){
        if (option.value === userData.servicePointName){
            option.selected = true;
        }
    }

    for(let op of citySelect.options){
        if (op.value === userData.cityName){
            op.selected = "true";
        }
    }

    nameInput.value = userData.userNames;
    phoneInput.value = userData.phoneNumber;
    emailInput.value = userData.userEmail;
    deleteEmailInput.value = userData.userEmail;
}