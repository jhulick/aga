(function() {
    'use strict';

    angular.module('maxGatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('account', {
                    abstract: true,
                    parent: 'site'
                });
        });
})();
