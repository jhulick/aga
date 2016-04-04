(function () {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('loggingCtrl', ['$scope', 'instance', 'InstanceLogging', loggingCtrl]);

    function loggingCtrl($scope, instance, InstanceLogging) {
        $scope.loggers = [];
        $scope.filteredLoggers = [];
        $scope.limit = 10;

        function findLogger(loggers, name) {
            for (var i in loggers) {
                if (loggers[i].name === name) {
                    return loggers[i];
                }
            }
        }

        $scope.setLogLevel = function (name, level) {
            InstanceLogging.setLoglevel(instance, name, level).then(function (response) {
                $scope.reload(name);
            }).catch(function (response) {
                $scope.error = response.error;
                console.log(response.stacktrace)
                $scope.reload(name);
            })
        };

        $scope.reload = function (prefix) {
            for (var i in $scope.loggers) {
                if (prefix == null || prefix === 'ROOT' || $scope.loggers[i].name.indexOf(prefix) == 0) {
                    $scope.loggers[i].level = null;
                }
            }
            $scope.refreshLevels();
        };

        $scope.refreshLevels = function () {
            var toLoad = [];
            var slice = $scope.filteredLoggers.slice(0, $scope.limit);
            for (var i in slice) {
                if (slice[i].level === null) {
                    toLoad.push(slice[i]);
                }
            }

            if (toLoad.length == 0) return;

            InstanceLogging.getLoglevel(instance, toLoad).then(
                function (responses) {
                    for (var i in responses) {
                        var name = responses[i].request.arguments[0];
                        var level = responses[i].value;
                        findLogger($scope.loggers, name).level = level;
                    }
                }).catch(function (responses) {
                for (var i in responses) {
                    if (responses[i].error != null) {
                        $scope.error = responses[i].error;
                        console.log(responses[i].stacktrace);
                        break;
                    }
                }
            });
        };

        InstanceLogging.getAllLoggers(instance).then(function (response) {
            $scope.loggers = [];
            for (var i in response.value) {
                $scope.loggers.push({name: response.value[i], level: null});
            }

            $scope.$watchCollection('filteredLoggers', function () {
                $scope.refreshLevels();
            });

            $scope.$watch('limit', function () {
                $scope.refreshLevels();
            });
        }).catch(function (response) {
            $scope.error = response.error;
            console.log(response.stacktrace);
        })
    }

})();

