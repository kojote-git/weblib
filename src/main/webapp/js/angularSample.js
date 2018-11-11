let sampleData = angular.module("sampleData", []);
sampleData.controller("SampleDataController", function SampleDataController($scope) {
    $scope.books = [
        {
            url: "#",
            imageUrl: "https://images-eu.ssl-images-amazon.com/images/I/517R971LWaL.jpg",
            title: "The God Delusion",
            averageRating: 9.1,
            authors : [
                {
                    url: "#",
                    firstName: "Richard",
                    lastName: "Dawkins"
                }
            ]
        }, {
            url: "#",
            imageUrl: "https://images-na.ssl-images-amazon.com/images/I/51%2BGySc8ExL._SX333_BO1,204,203,200_.jpg",
            title: "A Brief History of Time",
            averageRating: 9.2,
            authors : [
                {
                    url: "#",
                    firstName: "Stephen",
                    lastName: "Hawking"
                }
            ]
        }, {
            url: "#",
            imageUrl: "https://images.penguinrandomhouse.com/cover/9780553802023",
            title: "The Universe in a Nutshell",
            averageRating: 8.9,
            authors : [
                {
                    url: "#",
                    firstName: "Stephen",
                    lastName: "Hawking"
                }
            ]
        }, {
            url: "#",
            imageUrl: "https://images-na.ssl-images-amazon.com/images/I/51UVVOhk1sL._SX302_BO1,204,203,200_.jpg",
            title: "All Quiet on the Western Front",
            averageRating: 8.1,
            authors : [
                {
                    url: "#",
                    firstName: "Erich",
                    lastName: "Remarque"
                }
            ]
        }, {
            url: "#",
            imageUrl: "https://images-na.ssl-images-amazon.com/images/I/41CTXHtO9fL._SX330_BO1,204,203,200_.jpg",
            title: "Three Comrades",
            averageRating: 8.2,
            authors : [
                {
                    url: "#",
                    firstName: "Erich",
                    lastName: "Remarque"
                }
            ]
        }, {
            url: "#",
            imageUrl: "https://images-na.ssl-images-amazon.com/images/I/51xPIEYPWWL._SX326_BO1,204,203,200_.jpg",
            title: "It",
            averageRating: 8.7,
            authors : [
                {
                    firstName: "Stephen",
                    lastName: "King"
                }
            ]
        }
    ];
    $scope.getClassNameForRating = function(rating) {
        let tRating = parseFloat(rating);
        if (tRating < 4)
            return "red";
        if (tRating < 7)
            return "orange";
        return "green";
    }
});