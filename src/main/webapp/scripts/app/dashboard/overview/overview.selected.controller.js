(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('overviewSelectedCtrl', [
            '$scope',
            '$location',
            '$interval',
            '$q',
            '$stateParams',
            'Applications',
            'InstanceOverview',
            'Instance', overviewSelectedCtrl
        ]);

    function overviewSelectedCtrl($scope, $location, $interval, $q, $stateParams, Applications, InstanceOverview, Instance) {
        $scope.selectApp($stateParams.id); // parent function
    }

})();

