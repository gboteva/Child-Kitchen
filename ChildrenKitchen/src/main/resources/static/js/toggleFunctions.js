function toggle() {
  let nav = document.querySelector(".responsive-wrapper nav.nav-ctn");

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

function closeModal(){
  let modal = document.querySelector(".custom-modal");

  modal.setAttribute("style", "display: none")

}






