"use strict";

//Create account module
let module = angular.module('CreateAccountModule', ['ngResource']);

//service URI
//let serviceURI = 'http://localhost:8086/api';
let serviceURI = 'http://localhost:9000'

//Create account factory
module.factory('accountApi', function ($resource) {
    return $resource(serviceURI + '/messages');
});



//Create account controller
module.controller('CreateAccountController', function (accountApi) {


    //function to add account.
    this.addAccount = function (accountToAdd) {
        accountApi.save({}, accountToAdd);
    };




});


