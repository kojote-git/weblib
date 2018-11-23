const bookModule = angular.module("bookPage", []);

bookModule.controller("BookController", ["$http", "$scope", function ($http, $scope) {
    $http
        .get(URL + "rest/extbooks/" + getBookId())
        .then(function (resp) {
            $scope.book = resp.data;
            $scope.subjectsCount = 0;
            $scope.authorsCount = 0;
            $http
                .get($scope.book.description)
                .then(function (resp) {
                    $scope.book.description = resp.data.description;
                    $scope.subjectsCount = 0;
                    $scope.authorsCount = 0;
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
    $scope.getSplit = function(count, array) {
        if (count < array.length - 1)
            return ",";
        return "";
    };
    $scope.getSplitAuthors = function() {
        return $scope.getSplit($scope.authorsCount++, $scope.book.authors);
    };
    $scope.getSplitSubjects = function() {
        return $scope.getSplit($scope.subjectsCount++, $scope.book.subjects)
    };
    $scope.getRatingValue = function (rating) {
        if (rating < 0)
            return "not rated yet";
        return rating;
    };
    function getBookId() {
        return parseInt(document.getElementById("book-id").innerText);
    }
}]);