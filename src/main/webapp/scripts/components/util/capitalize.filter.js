(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .filter('capitalize', capitalize);

    function capitalize() {
        return function (input, scope) {
            if (input != null)
                input = input.toLowerCase();
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
    }
})();
