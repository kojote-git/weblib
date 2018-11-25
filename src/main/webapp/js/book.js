const bookModule = angular.module("bookPage", []);

bookModule.controller("BookController", ["$http", "$scope", function ($http, $scope) {
    const logout = document.getElementById("logout");
    $http
        .get(URL + "rest/extbooks/" + getBookId())
        .then(function (resp) {
            $scope.book = resp.data;
            for (let i = 0; i < $scope.book.authors.length - 1; i++) {
                let lastName = $scope.book.authors[i].lastName;
                $scope.book.authors[i].lastName = lastName + ",";
            }
            for (let j = 0; j < $scope.book.subjects.length - 1; j++) {
                $scope.book.subjects[j] += ",";
            }
            $http
                .get($scope.book.description)
                .then(function (resp) {
                    $scope.book.description = resp.data.description;
                });
        });
    $scope.getClassNameForRating = function (rating) {
        if (rating === -1)
            return "not-rated";
        if (rating < 4)
            return "red";
        if (rating < 8)
            return "orange";
        return "green"
    };
    $scope.getRatingValue = function (rating) {
        if (rating < 0)
            return "not rated yet";
        return rating;
    };
    $scope.download = function (e) {
        let target = e.currentTarget,
            instanceId = parseInt(target.getAttribute("data-instance-id")),
            email = getEmail(),
            accessToken = getAccessToken();
        window.open(target.getAttribute("data-file-url"));
        if (!email && !accessToken)
            return false;
        $http
            .put(URL + "rest/readers/downloading", {
                    instanceId: instanceId
                }, {
                    headers: {
                        "Email": email,
                        "Access-token": accessToken
                }
            });
    };
    if (logout) {
        logout.addEventListener("click", function (e) {
            let link = logout.getAttribute("data-ref"),
                xhr = new XMLHttpRequest();
            xhr.open("POST", URL + "rest/readers/logout");
            xhr.setRequestHeader("Email", getEmail());
            xhr.setRequestHeader("Access-token", getAccessToken());
            xhr.addEventListener("load", function (e) {
                window.open(URL, "_self", true);
            });
            xhr.send();
        })
    }
}]);
function getBookId() {
    return parseInt(document.getElementById("book-id").innerText);
}