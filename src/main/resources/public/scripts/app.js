
var app = angular.module('shapeapp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/list.html',
        controller: 'ListCtrl'
    }).when('/create', {
        templateUrl: 'views/create.html',
        controller: 'CreateCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.controller('ListCtrl', function ($scope, $http) {
    $http.get('/api/v1/shapes').success(function (data) {
        $scope.shapes = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })

    $scope.shapestatusChanged = function (shape) {
        console.log(shape);
        $http.put('/api/v1/shapes/' + shape.id, shape).success(function (data) {
            console.log('status changed');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.shape = {
        done: false
    };

    $scope.createShape = function () {
        console.log($scope.shape);
        $http.post('/api/v1/shapes', $scope.shape).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});