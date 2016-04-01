(function() {
    'use strict';

    angular.module('maxGatewayApp')
        .filter('timeInterval', function () {
            function padZero(i, n) {
                var s = i + "";
                while (s.length < n) s = "0" + s;
                return s;
            }

            return function (input) {
                var s = input || 0;
                var d = padZero(Math.floor(s / 86400000), 2);
                var h = padZero(Math.floor(s % 86400000 / 3600000), 2);
                var m = padZero(Math.floor(s % 3600000 / 60000), 2);
                var s = padZero(Math.floor(s % 60000 / 1000), 2);
                return d + ':' + h + ':' + m + ':' + s;
            }
        })
        .filter('classNameLoggerOnly', function () {
            return function (input, active) {
                if (!active) {
                    return input;
                }
                var result = [];
                for (var j in input) {
                    var name = input[j].name;
                    var i = name.lastIndexOf('.') + 1;
                    if (name.charAt(i) === name.charAt(i).toUpperCase()) {
                        result.push(input[j]);
                    }
                }
                return result;
            }
        })
        .filter('capitalize', function () {
            return function (input, active) {
                var s = input + "";
                return s.charAt(0).toUpperCase() + s.slice(1);
            }
        });
})();
