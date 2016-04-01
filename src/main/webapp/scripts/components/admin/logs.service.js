(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .factory('LogsService', LogsService);

    function LogsService($resource) {
        return $resource('api/logs', {}, {
            'findAll': {method: 'GET', isArray: true},
            'changeLevel': {method: 'PUT'}
        });
    }

})();
