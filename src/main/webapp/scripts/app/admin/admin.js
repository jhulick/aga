(function() {
    'use strict';

    angular.module('maxgatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('admin', {
                    abstract: true,
                    parent: 'site'
                });
        });
})();
