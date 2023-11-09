let soupsSelect = document.getElementById("soups");
let mainSelect = document.getElementById("main-food");
let dessertSelect = document.getElementById("dessert-food");

let soupsSelectEdit = document.getElementById("first-food-edit");
let mainSelectEdit = document.getElementById("main-food-edit");
let dessertSelectEdit = document.getElementById("dessert-food-edit");
let ageGroupSelectEdit = document.getElementById("edit-menu-age-group");
let dateSelectEdit = document.getElementById("select-date")

let ageGroupSelect = document.getElementById("add-menu-age-group");

let soups = []
let purees = []
let smallMain = []
let bigMain = []
let smallDessert = []
let bigDessert = []



async function fillFoods() {
    soupsSelect.innerHTML = '';
    mainSelect.innerHTML = '';
    dessertSelect.innerHTML = ''


    let response = await fetch(`http://localhost:8080/api/get-foods`);
    let data = await response.json();
    let info = Object.entries(data);

    info.forEach(array => {
        if (array[0] === "soups") {
            soups = array[1].sort();
        } else if (array[0] === "puree") {
            purees = array[1].sort();
        } else if (array[0] === "smallMain") {
            smallMain = array[1].sort();
        } else if (array[0] === "bigMain") {
            bigMain = array[1].sort();
        } else if (array[0] === "smallDessert") {
            smallDessert = array[1].sort();
        } else if (array[0] === "bigDessert") {
            bigDessert = array[1].sort();
        }
    })

   generateOptions(soupsSelect, mainSelect, dessertSelect, ageGroupSelect.value, null);

}

async function fillEditForm(){
    soupsSelectEdit.innerHTML = '';
    mainSelectEdit.innerHTML = '';
    dessertSelectEdit.innerHTML = ''
    await fillFoods()

    let response = await fetch(`http://localhost:8080/api/get-menu-by-date?date=${dateSelectEdit.value}&&ageGroup=${ageGroupSelectEdit.value}`);
    let existingMenu = await response.json();

    generateOptions(soupsSelectEdit, mainSelectEdit, dessertSelectEdit, ageGroupSelectEdit.value, existingMenu)

}

function generateOptions(soupSrc, mainSrc, dessertSrc, ageGroup, existingMenu){
    let existingSoup;
    let existingMain;
    let existingDessert;

    if (existingMenu != null){
        existingSoup = existingMenu.soup.name;
        existingMain = existingMenu.main.name;
        existingDessert = existingMenu.dessert.name;
    }

    if (ageGroup !== "") {
    soups.forEach(s => {
        let option = document.createElement("option");
        option.value = s;
        option.text = s;
        if (s === existingSoup){
            option.selected = true;
        }
        soupSrc.appendChild(option);
    })
}

    if (ageGroup === "МАЛКИ") {
        purees.forEach(p => {
            let option = document.createElement("option");
            option.value = p;
            option.text = p;
            if (p === existingMain){
                option.selected = true;
            }
            mainSrc.appendChild(option);
        })

        smallDessert.forEach(d => {
            let option = document.createElement("option");
            option.value = d;
            option.text = d;

            if (d === existingDessert){
                option.selected = true;
            }
            dessertSrc.appendChild(option);
        })


    } else if (ageGroup === "ГОЛЕМИ") {
        bigMain.forEach(m => {
            let option = document.createElement("option");
            option.value = m;
            option.text = m;

            if (m === existingMain){
                option.selected = true;
            }
            mainSrc.appendChild(option);
        })

        bigDessert.forEach(d => {
            let option = document.createElement("option");
            option.value = d;
            option.text = d;

            if (d === existingDessert){
                option.selected = true;
            }
            dessertSrc.appendChild(option);
        })
    }

}

function closeSuccess(){
    let successDiv = document.getElementsByClassName("success")[0];
    successDiv.setAttribute("style", "display: none");
}


