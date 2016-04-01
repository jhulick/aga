(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('ConfigurationController', function ($scope, ConfigurationService) {
            ConfigurationService.get().then(function (configuration) {
                $scope.configuration = configuration;
            });
        });
})();
