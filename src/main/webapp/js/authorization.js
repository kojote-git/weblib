const
    email = document.getElementById("email"),
    password = document.getElementById("password"),
    submit = document.getElementById("submit");

submit.addEventListener("click", function (e) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", URL + "rest/readers/authorization");
    xhr.setRequestHeader("Email", email.value);
    xhr.setRequestHeader("Password", password.value);
    xhr.addEventListener("load", function (e) {
        if (xhr.status === 200) {
            window.open(URL, "_self", false);
        }
        else {
            let resp = JSON.parse(xhr.response);
            alert(resp.error);
        }
    });
    xhr.send();
});
