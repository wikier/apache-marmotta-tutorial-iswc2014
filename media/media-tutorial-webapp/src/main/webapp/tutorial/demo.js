var app = angular.module('mediaDemo',[]);

app.controller('AnnotateCtrl', function ($rootScope,$scope,$http,$log) {

    //$scope.url = "http://upload.wikimedia.org/wikipedia/commons/7/73/Lion_waiting_in_Namibia.jpg";

    $scope.loadImage = function() {
        $scope.src = $scope.url;
    }

    $scope.annotate = function() {

        var url = $scope.src + "?xywh=" + $rootScope.x +","+$rootScope.y+","+$rootScope.w+","+$rootScope.h;

        if(confirm("Add annotation '" + $scope.name + "' to fragment uri " + url)) {

            var data = "@prefix oa: <http://www.w3.org/ns/oa#> . <> a oa:Annotation; oa:hasTarget \"" + $scope.name + "\"; oa:hasBody <" + url + ">.";

            $log.info($scope.src.substring(0,$scope.src.lastIndexOf(".")));

            $http({
                method: "POST",
                url:$scope.src.substring(0,$scope.src.lastIndexOf(".")),
                data: data,
                headers: {
                    "Content-Type":"text/turtle"
                }
            }).then(function(data, status, headers, config, statusText) {
                $log.info(data, headers);
                alert("Uploaded annotation successfully");
            }, function(data, status, headers, config, statusText){
                alert("Error: "+statusText);
            });
        }
    }

});

app.run(function($rootScope){
    function preview(img, selection) {
        if (!selection.width || !selection.height)
            return;

        var scale = img.width / img.naturalWidth;

        $rootScope.x = Math.round(selection.x1 / scale);
        $rootScope.y = Math.round(selection.y1 / scale);
        $rootScope.x2 = Math.round(selection.x2 / scale);
        $rootScope.y2 = Math.round(selection.y2 / scale);
        $rootScope.w = Math.round(selection.width / scale);
        $rootScope.h = Math.round(selection.height / scale);

        $rootScope.$digest();
    }

    $('#photo').imgAreaSelect({handles: true,
            fadeSpeed: 200, onSelectChange: preview })
});