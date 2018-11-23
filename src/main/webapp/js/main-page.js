const URL = "http://localhost:8080/weblib/";
const mainPage = angular.module("main-page", []);
mainPage.controller("BooksController", function BooksController($http, $scope) {
    const orderBySelect = document.getElementById("order-by-select"),
          sortOrderSelect = document.getElementById("sort-order-select"),
          applyFilters = document.getElementById("apply-filters"),
          showMoreAuthors = document.getElementById("show-more-authors"),
          showMoreSubjects = document.getElementById("show-more-subjects"),
          search = document.getElementById("search");
    $scope.currentPage = 1;
    $scope.pageSize = 8;
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
    $scope.getNext = function() {
        if ($scope.fetched < $scope.pageSize)
            return;
        $scope.currentPage++;
        $scope.loadBooks($scope.currentPage, $scope.pageSize, $scope.sort, $scope.filters);
        let a = document.createElement("a");
        a.href = "#";
        a.click();
    };
    $scope.getPrevious = function() {
        if ($scope.currentPage === 1)
            return;
        $scope.currentPage--;
        $scope.loadBooks($scope.currentPage, $scope.pageSize, $scope.sort, $scope.filters);
        let a = document.createElement("a");
        a.href = "#";
        a.click();
    };
    $scope.loadBooks = function(page, pageSize, sort, filters) {
        $http.get(buildUrl(page, pageSize, sort, filters)).then(function(response) {
            $scope.books = response.data.books;
            $scope.fetched = response.data.books.length;
        });
    };
    $scope.getAuthorId = function(id) {
        return "author-" + id;
    };
    $scope.getSubjectId = function(id) {
        return "subject-" + id;
    };
    applyFilters.addEventListener("click", function (e) {
        let filters = document.querySelectorAll("[data-filter]");
        $scope.filters = [];
        for (let i = 0; i < filters.length; i++) {
            let filterElement = filters[i],
                name = filterElement.getAttribute("data-filter-name"),
                values = [];
            let valuesElement = document.querySelectorAll("[data-filter-binding='"+name+"']")
            for (let j = 0; j < valuesElement.length; j++) {
                let valueElement = valuesElement[j];
                switch (valueElement.type) {
                    case "checkbox":
                        if (valueElement.checked)
                            values.push(valueElement.getAttribute("data-filter-value"));
                        break;
                    case "text":
                        values.push(valueElement.value);
                        break;
                    default:
                        break;
                }
            }
            let filter = {};
            filter.name = name;
            filter.values = values;
            $scope.filters.push(filter);
        }
        $scope.currentPage = 1;
        $scope.loadBooks($scope.currentPage, $scope.pageSize, $scope.sort, $scope.filters);
        let a = document.createElement("a");
        a.href = "#";
        a.click();
    });
    orderBySelect.addEventListener("change", function (e) {
        if (orderBySelect.value === "none") {
            $scope.sort = undefined;
            sortOrderSelect.value = "none";
        } else {
            let sortOrder = sortOrderSelect.value;
            if (sortOrder === "none") {
                sortOrderSelect.value = "ASC";
                sortOrder = "ASC";
            }
            $scope.sort = {
                value: e.target.value,
                order: sortOrder
            };
        }
        $scope.currentPage = 1;
        $scope.loadBooks($scope.currentPage, $scope.pageSize, $scope.sort, $scope.filters)
    });
    sortOrderSelect.addEventListener("change", function (e) {
        if (orderBySelect.value === "none")
            return;
        if (sortOrderSelect.value === "none")
            return;
        if (!$scope.sort)
            return;
        $scope.sort.order = sortOrderSelect.value;
        $scope.currentPage = 1;
        $scope.loadBooks($scope.currentPage, $scope.pageSize, $scope.sort, $scope.filters);
    });
    showMoreAuthors.addEventListener("click", function (e) {
        let target = e.target,
            url;
        if (target.getAttribute("shown") === "true") {
            target.innerText = "Show More";
            url = URL + "rest/authors?page=1&pageSize=5";
            target.setAttribute("shown", "false");
        } else {
            target.setAttribute("shown", "true");
            target.innerText = "Hide";
            url = URL + "rest/authors";
        }
        $http
            .get(url)
            .then(function (response) {
                $scope.authors = response.data.authors;
            });
    });
    showMoreSubjects.addEventListener("click", function (e) {
       let target = e.target,
           url;
       if (target.getAttribute("shown") === "true") {
           target.innerText = "Show More";
           url = URL + "rest/subjects?page=1&pageSize=5";
           target.setAttribute("shown", "false");
       } else {
           target.setAttribute("shown", "true");
           target.innerText = "Hide";
           url = URL + "rest/subjects";
       }
       $http
           .get(url)
           .then(function (response) {
               $scope.subjects = response.data.subjects;
           })
    });
    search.addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
            if (!$scope.filters) {
                $scope.filters = [];
            }
            if (e.target.value.length === 0) {
                let i = firstIndexOf($scope.filters, function (filter) {
                    return filter.name === "title";
                });
                if (i === -1)
                    return;
                $scope.filters.splice(i, 1);
            }
            let idx = firstIndexOf($scope.filters, function (filter) {
               return filter.name === "title";
            });
            if (idx == -1) {
                $scope.filters.push({
                    name: "title",
                    values: [
                        e.target.value
                    ]
                });
            } else {
                $scope.filters[idx].values = [e.target.value];
            }
            $scope.currentPage = 1;
            $scope.loadBooks($scope.currentPage, $scope.pageSize, $scope.sort, $scope.filters);
        }
    });
    function buildUrl(page, pageSize, sort, filters) {
        let res = URL + "rest/books?page=" + page + "&pageSize=" + pageSize;
        if (sort !== undefined)
            res += "&order=" + sort.value + "," + sort.order;
        if (filters === undefined)
            return res;
        for (let i = 0; i < filters.length; i++) {
            let filter = filters[i];
            if (filter.values.length !== 0) {
                res += "&" + filter.name;
                if (filter.values.length > 1) {
                    res += "=" + filter.values[0];
                    for (let j = 1; j < filter.values.length; j++) {
                        res += "," + filter.values[j];
                    }
                } else {
                    res += "=" + filter.values[0];
                }
            }
        }
        return res;
    }

    // --------------------------- INITIALIZING ---------------------------------

    $http
        .get(URL + "rest/authors")
        .then(function (response) {
            $scope.authors = shuffle(response.data.authors).slice(0, 5);
        });
    $http
        .get(URL + "rest/subjects")
        .then(function (response) {
           $scope.subjects = shuffle(response.data.subjects).slice(0, 5);
        });
    $scope.loadBooks($scope.currentPage, $scope.pageSize);
    function addEventListener(array, event, callback) {
        for (let i = 0; i < array.length; i++) {
            array[i].addEventListener(event, callback);
        }
    }
});
