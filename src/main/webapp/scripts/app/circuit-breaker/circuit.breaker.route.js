(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('circuit-breaker', {
                    url: '/circuit-breaker/:type/:id',
                    templateUrl: 'scripts/app/circuit-breaker/index.html',
                    controller: 'circuitBreakerCtrl'
                });
        });
})();
