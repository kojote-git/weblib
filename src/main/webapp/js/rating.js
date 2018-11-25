const ratings = document.querySelectorAll(".rating");
attachListener(ratings, "click", function (e) {
    let target = e.target,
        rating = parseInt(target.getAttribute("data-rating-value")),
        requestBody = {
            bookId: getBookId(),
            rating: rating
        },
        xhr = new XMLHttpRequest();
    xhr.open("PUT", URL + "rest/readers/rating", true);
    xhr.setRequestHeader("Email", getEmail());
    xhr.setRequestHeader("Access-token", getAccessToken());
    xhr.addEventListener("load", function (e) {
        alert("Thank you for feedback!");
    });
    xhr.send(JSON.stringify(requestBody));
});
