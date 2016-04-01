(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .factory('AuditsService', AuditsService);

    function AuditsService($http) {

        var service = {
            findByDates: findByDates,
            findAll: findAll
        };

        return service;

        /////////////////////

        function findAll() {
            return $http.get('api/audits/').then(function (response) {
                return response.data;
            });
        }

        function findByDates(fromDate, toDate) {

            var formatDate = function (dateToFormat) {
                if (dateToFormat !== undefined && !angular.isString(dateToFormat)) {
                    return dateToFormat.getYear() + '-' + dateToFormat.getMonth() + '-' + dateToFormat.getDay();
                }
                return dateToFormat;
            };

            return $http.get('api/audits/', {
                params: {
                    fromDate: formatDate(fromDate),
                    toDate: formatDate(toDate)
                }
            }).then(function (response) {
                return response.data;
            });
        }
    }
})();
