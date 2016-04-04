(function () {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('detailsEnvCtrl', ['$scope', 'instance', 'InstanceDetails', detailsEnvCtrl]);

    function detailsEnvCtrl($scope, instance, InstanceDetails) {
        $scope.instance = instance;
        InstanceDetails.getEnv(instance).success(function (env) {
            $scope.env = env;
        }).error(function (error) {
            $scope.error = error;
        });
    }

})();

