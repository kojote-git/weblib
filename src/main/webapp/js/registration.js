const
    email = document.getElementById("email"),
    password = document.getElementById("password"),
    submit = document.getElementById("submit");
submit.addEventListener("click", function (e) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", URL + "rest/readers/creation");
    xhr.setRequestHeader("Email", email.value);
    xhr.setRequestHeader("Password", password.value);
    xhr.addEventListener("load", function (e) {
        if (xhr.status === 201)
            window.open(URL + "authorization", "_self", false);
        let resp = JSON.parse(xhr.response);
        alert(resp.error);
    });
    xhr.send();
});