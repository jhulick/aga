(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('entity', {
                    abstract: true,
                    parent: 'site'
                });
        });
})();
