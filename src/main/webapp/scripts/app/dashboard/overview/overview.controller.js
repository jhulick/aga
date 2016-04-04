(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('overviewCtrl', [
            '$scope',
            '$location',
            '$interval',
            '$q',
            '$stateParams',
            '$timeout',
            '$state',
            'Applications',
            'ApplicationOverview',
            'InstanceOverview',
            OverviewCtrl]);

    function OverviewCtrl($scope, $location, $interval, $q, $stateParams,
                          $timeout, $state, Applications,
                          ApplicationOverview, InstanceOverview) {

        $scope.findApp = function (name) {
            for (var j = 0; $scope.applications != null && j < $scope.applications.length; j++) {
                if (name === $scope.applications[j].name) {
                    return $scope.applications[j];
                }
            }
        };

        $scope.selectApp = function (name) {
            $scope.selectedAppName = name;
            $scope.selectedApp = $scope.findApp(name);
            if (angular.isDefined($scope.selectedApp)) {
                ApplicationOverview.getCircuitBreakerInfo($scope.selectedApp);
                $scope.selectedApp.active = true;
                $scope.selectedApp.instances.forEach(function (instance) {
                    InstanceOverview.getInfo(instance)
                });
            }
        };

        $scope.updateApStatus = function (app) {
            var instanceUp = 0, instanceCount = 0;
            app.instances.forEach(function (instance) {
                instanceCount++;
                if (instance.health == 'UP') {
                    instanceUp++;
                }
            });

            var appState = instanceUp / instanceCount;
            if (appState > 0.8) {
                app.badge = 'success';
            } else if (app.instanceUp == 0) {
                app.badge = 'danger';
            } else {
                app.badge = 'warning';
            }

            app.instanceUp = instanceUp;
            app.instanceCount = instanceCount;
        };

        $scope.loadData = function () {

            return Applications.query(function (applications) {
                applications.forEach(function (app) {
                    app.instances.forEach(function (instance) {
                        InstanceOverview.getHealth(instance).finally(function () {
                            $scope.updateApStatus(app);
                        });
                    });
                });

                $scope.applications = applications;

                //Refresh current selected App
                if ($scope.selectedAppName) {
                    $scope.selectApp($scope.selectedAppName);
                } else {
                    $timeout(function () {
                        if (applications.length > 0) $state.go('overview.select', {id: applications[0].name});
                    }, 10);
                }
            });
        };

        $scope.loadData();

        // reload site every 30 seconds
        var task = $interval(function () {
            $scope.loadData();
        }, 30000);
    }
})();

