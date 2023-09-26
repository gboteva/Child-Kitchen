
function loadPoints() {
        let parentUl = document.getElementById("points-list");
        parentUl.innerHTML = '';

        fetch(`http://localhost:8080/api/points`)
        .then(response => response.json())
        .then(data => data.forEach( point => {
                const li = document.createElement("li")
                li.classList.add("point-item")
                parentUl.appendChild(li)

                const article = document.createElement("article");
                article.classList.add("card");
                li.appendChild(article)

                const imgCtn = document.createElement("div")
                imgCtn.classList.add("img-ctn")
                article.appendChild(imgCtn);

                const img = document.createElement("img")
                img.setAttribute("src", point.pictureUrl);
                img.setAttribute("alt", point.name);
                imgCtn.appendChild(img);

                const contentCtn = document.createElement("div");
                contentCtn.classList.add("card-content");
                article.appendChild(contentCtn);

                const nameP = document.createElement("p");
                nameP.classList.add("title")
                nameP.textContent = point.name;
                contentCtn.appendChild(nameP);

                const address = document.createElement("address");
                address.textContent = point.address;
                contentCtn.appendChild(address);

                const timeP = document.createElement("p");
                timeP.classList.add("working-tim")
                timeP.textContent = point.workingTime;
                contentCtn.appendChild(timeP);
        }))
            .catch(error => console.log(error))
}




