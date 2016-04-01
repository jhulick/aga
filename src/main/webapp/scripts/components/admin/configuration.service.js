(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .factory('ConfigurationService', ConfigurationService);

    function ConfigurationService($rootScope, $filter, $http) {
        return {
            get: function () {
                return $http.get('configprops').then(function (response) {
                    var properties = [];
                    angular.forEach(response.data, function (data) {
                        properties.push(data);
                    });
                    var orderBy = $filter('orderBy');
                    return orderBy(properties, 'prefix');
                });
            }
        };
    }

})();
