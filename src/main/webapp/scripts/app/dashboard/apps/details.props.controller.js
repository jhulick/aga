(function () {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('detailsPropsCtrl', ['$scope', 'instance', 'InstanceDetails', detailsPropsCtrl]);

    function detailsPropsCtrl($scope, instance, InstanceDetails) {
        $scope.instance = instance;
        InstanceDetails.getEnv(instance).success(function (env) {
            $scope.props = [];
            for (var attr in env) {
                if (attr.indexOf('[') != -1 && attr.indexOf('.properties]') != -1) {
                    $scope.props.push({key: attr, value: env[attr]});
                }
            }
        }).error(function (error) {
            $scope.error = error;
        });
    }

})();

