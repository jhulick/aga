(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .factory('MonitoringService', MonitoringService);

    function MonitoringService($rootScope, $http) {

        var service = {
            getMetrics: getMetrics,
            checkHealth: checkHealth,
            threadDump: threadDump
        };

        return service;

        ///////////////////////

        function getMetrics() {
            return $http.get('metrics/metrics').then(function (response) {
                return response.data;
            });
        }

        function checkHealth() {
            return $http.get('health').then(function (response) {
                return response.data;
            });
        }

        function threadDump() {
            return $http.get('dump').then(function (response) {
                return response.data;
            });
        }
    }

})();
