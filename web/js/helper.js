
"use strict";

modulejs.define('helper', ["dto"], function (Dto) {
  var module = {};

  module.createGender = function (code) {
    var gender = new Dto.gender();
    gender.code = code;
    if (!code) {
      gender.id = 5;
      gender.name = 'Unreported';
      gender.code = 'U';
    } else if (code == 'M') {
      gender.id = 1;
      gender.name = 'Male';
    } else if (code == 'F') {
      gender.id = 2;
      gender.name = 'Female';
    }
    return gender;
  };
  return module;
});