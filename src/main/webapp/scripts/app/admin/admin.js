(function() {
    'use strict';

    angular.module('maxGatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('admin', {
                    abstract: true,
                    parent: 'site'
                });
        });
})();
