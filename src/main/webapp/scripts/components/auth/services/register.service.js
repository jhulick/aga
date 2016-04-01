'use strict';

angular.module('maxGatewayApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


