(function() {
    'use strict';

    angular
        .module('maxgatewayApp')
        .config(function (uibPagerConfig) {
            uibPagerConfig.itemsPerPage = 20;
            uibPagerConfig.previousText = '«';
            uibPagerConfig.nextText = '»';
        });
})();
