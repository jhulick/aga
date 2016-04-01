(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .factory('notificationInterceptor', function ($q, AlertService) {
            return {
                response: function (response) {
                    var alertKey = response.headers('X-maxgatewayApp-alert');
                    if (angular.isString(alertKey)) {
                        AlertService.success(alertKey, {param: response.headers('X-maxgatewayApp-params')});
                    }
                    return response;
                }
            };
        });
})();
