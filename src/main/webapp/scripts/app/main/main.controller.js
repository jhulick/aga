(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .controller('MainController', function ($scope, Principal) {
            Principal.identity().then(function (account) {
                $scope.account = account;
                $scope.isAuthenticated = Principal.isAuthenticated;
            });
        });
})();
