(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('appsCtrl', ['$scope', 'instance', function ($scope, instance) {
            $scope.instance = instance;
        }])
})();

