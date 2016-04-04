(function () {
    'use strict';

    angular
        .module('maxGatewayApp')
        .controller('detailsMetricsCtrl', [
            '$scope',
            'instance',
            'InstanceDetails',
            'Abbreviator',
            'MetricsHelper',
            detailsMetricsCtrl
        ]);

    function detailsMetricsCtrl($scope, instance, InstanceDetails, Abbreviator, MetricsHelper) {
        $scope.memoryData = [];
        $scope.heapMemoryData = [];
        $scope.counterData = [];
        $scope.gaugeData = [];

        InstanceDetails.getMetrics(instance).success(function (metrics) {
            //*** Extract data for Counter-Chart and Gauge-Chart
            $scope.counterData = [{key: "value", values: []}];
            $scope.gaugeData = [{key: "value", values: []},
                {key: "average", values: []},
                {key: "min", values: []},
                {key: "max", values: []},
                {key: "count", values: []}];

            MetricsHelper.find(metrics,
                [/counter\.(.+)/, /gauge\.(.+)\.val/, /gauge\.(.+)\.avg/, /gauge\.(.+)\.min/, /gauge\.(.+)\.max/, /gauge\.(.+)\.count/, /gauge\.(.+)\.alpha/, /gauge\.(.+)/],
                [function (metric, match, value) {
                    $scope.counterData[0].values.push([match[1], value]);
                },
                    function (metric, match, value) {
                        $scope.gaugeData[0].values.push([match[1], value]);
                    },
                    function (metric, match, value) {
                        $scope.gaugeData[1].values.push([match[1], value]);
                    },
                    function (metric, match, value) {
                        $scope.gaugeData[2].values.push([match[1], value]);
                    },
                    function (metric, match, value) {
                        $scope.gaugeData[3].values.push([match[1], value]);
                    },
                    function (metric, match, value) {
                        $scope.gaugeData[4].values.push([match[1], value]);
                    },
                    function (metric, match, value) { /*NOP*/
                    },
                    function (metric, match, value) {
                        $scope.gaugeData[0].values.push([match[1], value]);
                    }]);

            //in case no richGauges are present remove empty groups
            var i = $scope.gaugeData.length;
            while (--i) {
                if ($scope.gaugeData[i].values.length === 0) {
                    $scope.gaugeData.splice(i, 1);
                }
            }
        }).error(function (error) {
            $scope.error = error;
        });

        var colorArray = ['#6db33f', '#a5b2b9', '#34302d', '#fec600', '#4e681e'];
        $scope.colorFunction = function () {
            return function (d, i) {
                return colorArray[i % colorArray.length];
            };
        };

        $scope.abbreviateFunction = function (targetLength, preserveLast, shortenThreshold) {
            return function (s) {
                return Abbreviator.abbreviate(s, '.', targetLength, preserveLast, shortenThreshold)
            };
        };

        $scope.intFormatFunction = function () {
            return function (d) {
                return d3.format('d')(d);
            };
        };

        $scope.toolTipContentFunction = function () {
            return function (key, x, y, e, graph) {
                return '<b>' + key + '</b> ' + e.point[0] + ': ' + e.point[1];
            }
        }

    }

})();

