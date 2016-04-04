(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .config(function ($stateProvider) {
            $stateProvider
                .state('apps', {
                    url: '/apps/:id',
                    data: {
                        authorities: []
                    },
                    views: {
                        'content@': {
                            templateUrl: 'scripts/app/dashboard/apps/apps.html',
                            controller: 'appsCtrl'
                        }
                    },
                    resolve: {
                        instance: ['$stateParams', 'Instance', function ($stateParams, Instance) {
                            return Instance.query({id: $stateParams.id}).$promise;
                        }]
                    }
                })
                .state('history', {
                    url: '/history',
                    templateUrl: 'scripts/app/dashboard/apps/history.html',
                    controller: 'appsHistoryCtrl'
                })
                .state('apps.details', {
                    url: '/details',
                    templateUrl: 'scripts/app/dashboard/apps/details.html',
                    controller: 'detailsCtrl'
                })
                .state('apps.details.metrics', {
                    url: '/metrics',
                    templateUrl: 'scripts/app/dashboard/apps/details/metrics.html',
                    controller: 'detailsMetricsCtrl'
                })
                .state('apps.details.env', {
                    url: '/env',
                    templateUrl: 'scripts/app/dashboard/apps/details/env.html',
                    controller: 'detailsEnvCtrl'
                })
                .state('apps.details.props', {
                    url: '/props',
                    templateUrl: 'scripts/app/dashboard/apps/details/props.html',
                    controller: 'detailsPropsCtrl'
                })
                .state('apps.details.classpath', {
                    url: '/classpath',
                    templateUrl: 'scripts/app/dashboard/apps/details/classpath.html',
                    controller: 'detailsClasspathCtrl'
                })
                .state('apps.logging', {
                    url: '/logging',
                    templateUrl: 'scripts/app/dashboard/apps/logging.html',
                    controller: 'loggingCtrl'
                })
                .state('apps.jmx', {
                    url: '/jmx',
                    templateUrl: 'scripts/app/dashboard/apps/jmx.html',
                    controller: 'jmxCtrl'
                })
                .state('apps.threads', {
                    url: '/threads',
                    templateUrl: 'scripts/app/dashboard/apps/threads.html',
                    controller: 'threadsCtrl'
                });
        });
})();
