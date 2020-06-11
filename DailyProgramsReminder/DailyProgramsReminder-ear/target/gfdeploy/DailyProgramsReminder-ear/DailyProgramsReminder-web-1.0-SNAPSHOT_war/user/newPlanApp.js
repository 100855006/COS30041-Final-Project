"use strict";

var app = angular.module("dpr", ["angularBeans"]);

app.controller("createPlanCtrl", function ($scope, $window, createPlanAngularBean) {
    $scope.planTitle = "";
    $scope.planDescription = "";
    $scope.planRepeat = "0";
    $scope.planPriority = "0";

    $scope.createPlan = function() {
        if($scope.planRepeat === "0") {
            $scope.planEndRepeat = $scope.planDate;
        }
        
        createPlanAngularBean.planTitle = $scope.planTitle;
        createPlanAngularBean.planDescription = $scope.planDescription;
        createPlanAngularBean.planDate = $scope.planDate;
        createPlanAngularBean.planRepeat = $scope.planRepeat;
        createPlanAngularBean.planEndRepeat = $scope.planEndRepeat;
        createPlanAngularBean.planPriority = $scope.planPriority;

        createPlanAngularBean.createPlan().then(function(res){
            if (res === true) {
                $window.location.href = "newPlanSuccess.xhtml";
            } else {
                $window.location.href = "newPlanFailure.xhtml";
            }
        });
    };
});

app.directive("ngValidate", function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attributes, modelCtrl) {
            var inputType = attributes.ngValidate;
            function myValidation(value) {
                var regex = "";
                switch (inputType) {
                case "planTitle":
                    regex = /^[a-zA-Z0-9 ]*$/;
                    modelCtrl.$setValidity('noValidPlanTitle', regex.test(value));
                    break;
                case "planDescription":
                    regex = /^[a-zA-Z0-9 ]*$/;
                    modelCtrl.$setValidity('noValidPlanDescription', regex.test(value));
                    break;
                }
                return value;
            }
            modelCtrl.$parsers.push(myValidation);
        }
    };
});

$(function () {
    var today = moment();
    
    $('#planDatetimepicker').datetimepicker({
        minDate: today,
        defaultDate: today,
        format: 'DD/MM/YYYY HH:mm A'
    });
    $('#planEndRepeatDatetimepicker').datetimepicker({
        minDate: today,
        defaultDate: today,
        format: 'DD/MM/YYYY HH:mm A'
    });
    
    $("#planDatetimepicker").on("change.datetimepicker", function (e) {
        $('#planEndRepeatDatetimepicker').datetimepicker('minDate', e.date);
    });
});