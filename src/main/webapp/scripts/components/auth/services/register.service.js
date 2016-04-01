'use strict';

angular.module('maxgatewayApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


