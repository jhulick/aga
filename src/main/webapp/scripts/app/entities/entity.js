(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('entity', {
                    abstract: true,
                    parent: 'site'
                });
        });
})();
