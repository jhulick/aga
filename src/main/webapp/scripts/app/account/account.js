(function() {
    'use strict';

    angular.module('maxgatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('account', {
                    abstract: true,
                    parent: 'site'
                });
        });
})();
