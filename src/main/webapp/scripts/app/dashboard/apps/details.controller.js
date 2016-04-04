(function () {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('detailsCtrl', [
            '$scope',
            '$interval',
            'instance',
            'InstanceDetails',
            'MetricsHelper',
            detailsCtrl
        ]);

    function detailsCtrl($scope, $interval, instance, InstanceDetails, MetricsHelper) {
        $scope.instance = instance;
        InstanceDetails.getInfo(instance).success(function (info) {
            $scope.info = info;
        }).error(function (error) {
            $scope.error = error;
        });

        InstanceDetails.getHealth(instance).success(function (health) {
            $scope.health = health;
        }).error(function (health) {
            $scope.health = health;
        });

        InstanceDetails.getMetrics(instance).success(function (metrics) {
            $scope.metrics = metrics;
            $scope.metrics["mem.used"] = $scope.metrics["mem"] - $scope.metrics["mem.free"];

            $scope.metrics["systemload.averagepercent"] = $scope.metrics["systemload.average"] / $scope.metrics["processors"] * 100;

            $scope.gcInfos = {};
            $scope.datasources = {};

            function createOrGet(map, key, factory) {
                return map[key] || (map[key] = factory());
            }

            MetricsHelper.find(metrics,
                [/gc\.(.+)\.time/, /gc\.(.+)\.count/, /datasource\.(.+)\.active/, /datasource\.(.+)\.usage/],
                [function (metric, match, value) {
                    createOrGet($scope.gcInfos, match[1], function () {
                        return {time: 0, count: 0};
                    }).time = value;
                },
                    function (metric, match, value) {
                        createOrGet($scope.gcInfos, match[1], function () {
                            return {time: 0, count: 0};
                        }).count = value;
                    },
                    function (metric, match, value) {
                        $scope.hasDatasources = true;
                        createOrGet($scope.datasources, match[1], function () {
                            return {min: 0, max: 0, active: 0, usage: 0};
                        }).active = value;
                    },
                    function (metric, match, value) {
                        $scope.hasDatasources = true;
                        createOrGet($scope.datasources, match[1], function () {
                            return {min: 0, max: 0, active: 0, usage: 0};
                        }).usage = value;
                    }]);
        }).error(function (error) {
            $scope.error = error;
        });

        InstanceDetails.getCircuitBreakerInfo(instance).success(function () {
            instance.circuitBreaker = true;
        }).error(function () {
            instance.circuitBreaker = false;
        });

        var start = Date.now();
        var tick = $interval(function () {
            $scope.ticks = Date.now() - start;
        }, 1000);
    }

})();

