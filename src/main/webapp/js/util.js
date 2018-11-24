function shuffle(a) {
    var j, x, i;
    for (i = a.length - 1; i > 0; i--) {
        j = Math.floor(Math.random() * (i + 1));
        x = a[i];
        a[i] = a[j];
        a[j] = x;
    }
    return a;
}

function firstIndexOf(array, predicate) {
    for (let i = 0; i < array.length; i++) {
        if (predicate(array[i]))
            return i;
    }
    return -1;
}

function readCookie(name) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function getEmail() {
    return readCookie("email");
}

function getAccessToken() {
    return readCookie("accessToken");
}