(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .controller('HealthModalController', function ($scope, $uibModalInstance, currentHealth, baseName, subSystemName) {

            $scope.currentHealth = currentHealth;
            $scope.baseName = baseName;
            $scope.subSystemName = subSystemName;

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        });
})();