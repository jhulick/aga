'use strict';

angular.module('maxGatewayApp')
    .controller('circuitBreakerCtrl', ['$scope', '$stateParams', 'Instance',
        function ($scope, $stateParams, Instance) {

            if ($stateParams.type == 'app') {
                var stream = "/circuitBreaker.stream?appName=" + $stateParams.id;
                $scope.subtitle = $stateParams.id;
            } else if ($stateParams.type == 'instance') {
                var stream = "/circuitBreaker.stream?instanceId=" + $stateParams.id;
                var instance = Instance.query({id: $stateParams.id}, function (instance) {
                    $scope.subtitle = instance.name;
                });
            }

            // commands
            $scope.hystrixMonitor = new HystrixCommandMonitor('dependencies', {includeDetailIcon: false});

            // sort by error+volume by default
            $scope.hystrixMonitor.sortByErrorThenVolume();

            // start the EventSource which will open a streaming connection to the server
            $scope.source = new EventSource(stream);

            // add the listener that will process incoming events
            $scope.source.addEventListener('message', $scope.hystrixMonitor.eventSourceMessageListener, false);

            $scope.source.addEventListener('error', function (e) {
                if (e.eventPhase == EventSource.CLOSED) {
                    // Connection was closed.
                    console.log("Connection was closed on error: " + JSON.stringify(e));
                } else {
                    console.log("Error occurred while streaming: " + JSON.stringify(e));
                }
            }, false);

            // thread pool
            $scope.dependencyThreadPoolMonitor = new HystrixThreadPoolMonitor('dependencyThreadPools');

            $scope.dependencyThreadPoolMonitor.sortByVolume();

            // start the EventSource which will open a streaming connection to the server
            $scope.threadSource = new EventSource(stream);

            // add the listener that will process incoming events
            $scope.threadSource.addEventListener('message', $scope.dependencyThreadPoolMonitor.eventSourceMessageListener, false);

            $scope.threadSource.addEventListener('error', function (e) {
                if (e.eventPhase == EventSource.CLOSED) {
                    // Connection was closed.
                    console.log("Connection was closed on error: " + e);
                } else {
                    console.log("Error occurred while streaming: " + e);
                }
            }, false);

            $scope.$on('$destroy', function clearEventSource() {
                if ($scope.source) {
                    $scope.source.close();
                    delete $scope.source;
                }
                if ($scope.threadSource) {
                    $scope.threadSource.close();
                    delete $scope.threadSource;
                }
            })
        }]);
