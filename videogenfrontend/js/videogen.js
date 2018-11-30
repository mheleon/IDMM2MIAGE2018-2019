var VideoGenApp = angular.module('videogenDirective', ['ngResource']);

// Declare directive videogenDirective
// VideoGenApp.directive('videogenDirective', function () {
//     return {
//         templateUrl: 'template.html'
//     };
// });

// Injection of myURL url
VideoGenApp.constant('myURL', 'http://localhost:8080/');

VideoGenApp.config(['$resourceProvider', function ($resourceProvider) {
    $resourceProvider.defaults.stripTrailingSlashes = false;
}]);

// Service to connect (future) the controllers
VideoGenApp.service('serveData', [function () {
    return { myValue: 1 };
}]);

VideoGenApp.controller('myCtrlSearch', ['$scope', '$log', '$http', 'serveData', 'myURL', '$window', function ($scope, $log, $http, serveData, myURL, $window) {

    // Creation of scopes
    $scope.videos = [{}];
    $scope.alternativeVideo = [{}];
    $scope.obj = serveData;
    $scope.videoOutput = ({ video: "video/output/output.mp4" });

    // $http to get all video sequence (JSON)
    $http({
        method: "GET",
        url: myURL + 'api/v1/VideoGenList'
    }).success(function (data, status, headers, config) {
        // Una vez recuperado el objeto json que contiene todos los videos, se obtiene solo la parte del objeto que interesa
        var json = data;

        // Se coloca el id y la imagen de todos los videos en un nuevo json para mostralos
        for (var i in json) {
            if (json[i].type == "mandatory" || json[i].type == "optional") {
                $scope.videos.push({ id: json[i].id, image: myURL + json[i].image, type: json[i].type });
            } else {
                jsonA = json[i];
                for (var j in jsonA) {
                    $scope.alternativeVideo.push({ id: jsonA[j].id, image: myURL + jsonA[j].image, type: jsonA[j].type });
                }
                $scope.videos.push({ type: "alternative", obt: $scope.alternativeVideo });
            }

        };
        console.log($scope.videos);
    }).error(function (data, status, headers, config) {
        alert("Error: " + status);
    });

    $scope.generateRandom = function () {
        $http.post(myURL + 'api/v1/random', $scope.formData)
            .success(function (data) {
                if (data.message == "successful") {
                    console.log(data);
                    $window.location.href = 'videoOutput.html';
                    $scope.show = true;
                }
            })
            .error(function (data) {
                console.log('Error:' + data);
            });
    };

    $scope.generateConf = function () {
        var datosForm = document.getElementById("myForm").getElementsByTagName('input');
        var videoTrue = [];

        for (var i = 0; i < datosForm.length; i++) {
            if (datosForm[i].checked) {
                videoTrue.push(datosForm[i].id);
            }
        }
        console.log(videoTrue);
        $http({
            url: myURL + 'api/v1/conf',
            method: "POST",
            data: angular.toJson(videoTrue),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            }
        }, $scope.formData).success(function (data) {
            console.log(data);
            $scope.show = true;
            $window.location.href = 'videoOutput.html';
        }).error(function (data) {
            console.log('Error:' + data);
        });
    };

}]);