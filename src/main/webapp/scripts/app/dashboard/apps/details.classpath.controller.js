(function () {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('detailsClasspathCtrl', [
            '$scope',
            'instance',
            'InstanceDetails',
            'Abbreviator',
            detailsClasspathCtrl
        ]);

    function detailsClasspathCtrl($scope, instance, InstanceDetails, Abbreviator) {
        $scope.instance = instance;
        InstanceDetails.getEnv(instance).success(function (env) {
            var separator = env['systemProperties']['path.separator'];
            $scope.classpath = env['systemProperties']['java.class.path'].split(separator);
        }).error(function (error) {
            $scope.error = error;
        });
    }

})();

