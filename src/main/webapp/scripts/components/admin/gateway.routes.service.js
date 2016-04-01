(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .factory('GatewayRoutesService', GatewayRoutesService);

    function GatewayRoutesService($resource) {
        return $resource('api/gateway/routes/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }

})();
