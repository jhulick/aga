(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('appsHistoryCtrl', ['$scope', 'InstancesHistory', function ($scope, InstancesHistory) {
            InstancesHistory.query(function (history) {
                $scope.registered = history.lastRegistered;
                $scope.cancelled = history.lastCancelled;
            });
        }])
})();

