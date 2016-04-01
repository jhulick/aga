(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .factory('notificationInterceptor', function ($q, AlertService) {
            return {
                response: function (response) {
                    var alertKey = response.headers('X-maxGatewayApp-alert');
                    if (angular.isString(alertKey)) {
                        AlertService.success(alertKey, {param: response.headers('X-maxGatewayApp-params')});
                    }
                    return response;
                }
            };
        });
})();
