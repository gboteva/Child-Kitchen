function toggle() {
  let nav = document.getElementsByClassName("nav-ctn")[0];

  if (nav.getAttribute("style") === "display: none") {
    nav.setAttribute("style", "display: block");
  } else {
    nav.setAttribute("style", "display: none");
  }
}

function toggleInstruction() {
  let header = document.getElementById("instructions");

  let instructionWrapper = document.getElementsByClassName("wrapper")[0];
  let btnSee = document.querySelector(".e-kitchen .main-btn.show");


  if (instructionWrapper.getAttribute("style") === "display: none") {
    instructionWrapper.setAttribute("style", "display: block");
    
    btnSee.setAttribute("style", "display: none");
  } else {
    instructionWrapper.setAttribute("style", "display: none");
    btnSee.setAttribute("style", "display: block");
  }

}

function showInfoFromDB(event) {
  console.log("show info");
  let infoCtn = document.getElementById("reference");
  let select = document.getElementById("puncts");
  let infoFromDB = document.querySelector("#reference .info-from-db");
  let onePunctTable = document.getElementsByClassName("one-punct")[0];
  let allPunctsTable = document.getElementsByClassName("all-puncts")[0]

  infoCtn.setAttribute("style", "display: block");
  infoFromDB.setAttribute("style", "display: flex");

  if (select.options[select.selectedIndex].text !== "Всички"){
    onePunctTable.setAttribute("style", "display: block");
    allPunctsTable.setAttribute("style", "display: none");
  } else {
    onePunctTable.setAttribute("style", "display: none");
    allPunctsTable.setAttribute("style", "display: block");
  }
}





