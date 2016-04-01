(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .controller('ConfigurationController', function ($scope, ConfigurationService) {
            ConfigurationService.get().then(function (configuration) {
                $scope.configuration = configuration;
            });
        });
})();
