(function() {
    'use strict';

    angular
        .module('maxGatewayApp')
        .config(function (uibPagerConfig) {
            uibPagerConfig.itemsPerPage = 20;
            uibPagerConfig.previousText = '«';
            uibPagerConfig.nextText = '»';
        });
})();
