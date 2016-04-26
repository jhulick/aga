(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('overview', {
                    parent: 'site',
                    url: '/overview',
                    data: {
                        authorities: []
                    },
                    views: {
                        'content@': {
                            templateUrl: 'scripts/app/dashboard/overview/overview.html',
                            controller: 'overviewCtrl'
                        }
                    }
                });
        });
})();
