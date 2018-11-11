const URL = "http://localhost:8080/weblib";
const mainPage = angular.module("main-page", []);
mainPage.controller("BooksController", function BooksController($http, $scope) {
    $scope.getClassNameForRating = function (rating) {
        let tRating = parseFloat(rating);
        if (tRating < 0)
            return "not-rated";
        if (tRating < 4)
            return "red";
        if (tRating < 7)
            return "orange";
        return "green";
    };
    $scope.getRatingValue = function(rating) {
        let tRating = parseFloat(rating);
        if (tRating < 0)
            return "not rated yet";
        return tRating;
    };
    $http.get(URL + "/rest/books").then(function(response) {
        $scope.books = response.data.books;
    })
});