(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('overview.select', {
                    url: '/:id',
                    data: {
                        authorities: []
                    },
                    views: {
                        'content@': {
                            templateUrl: 'scripts/app/dashboard/overview/overview.selected.html',
                            controller: 'overviewSelectedCtrl'
                        }
                    }
                });
        });
})();
