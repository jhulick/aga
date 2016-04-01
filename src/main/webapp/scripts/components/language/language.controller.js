(function() {
    'use strict';

    angular.module('maxgatewayApp')
        .controller('LanguageController', function ($scope, $translate, Language, tmhDynamicLocale) {
            $scope.changeLanguage = function (languageKey) {
                $translate.use(languageKey);
                tmhDynamicLocale.set(languageKey);
            };

            Language.getAll().then(function (languages) {
                $scope.languages = languages;
            });
        })
        .filter('findLanguageFromKey', function () {
            return function (lang) {
                return {
                    "en": "English",
                    "es": "Español",
                    "fr": "Français",
                    "it": "Italiano"
                }[lang];
            }
        });
})();
