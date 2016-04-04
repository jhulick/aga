(function () {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('threadsCtrl', [
            '$scope',
            'instance',
            'InstanceThreads',
            threadsCtrl
        ]);

    function threadsCtrl($scope, instance, InstanceThreads) {
        $scope.dumpThreads = function () {
            InstanceThreads.getDump(instance).success(function (dump) {
                $scope.dump = dump;

                var threadStats = {NEW: 0, RUNNABLE: 0, BLOCKED: 0, WAITING: 0, TIMED_WAITING: 0, TERMINATED: 0};
                for (var i = 0; i < dump.length; i++) {
                    threadStats[dump[i].threadState]++;
                }
                threadStats.total = dump.length;
                $scope.threadStats = threadStats;

            }).error(function (error) {
                $scope.error = error;
            });
        }
    }

})();

