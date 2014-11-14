
"use strict";

modulejs.define('table', ["events", "util"], function (Events, Util) {
  var module = {};

  module.getDataTableName = function(item) {
    var value = '';
    if (item.medicalTest) {
      value = item.medicalTest.name;
    } else if (item.healthTrendReport) {
      value = item.healthTrendReport.name;
    }
    return value;
  }
  module.getColumnValue = function (column, item) {
    var value = '', field0, field1, field2, field3;
    if (item === undefined) {
      return value;
    }
    var columnFields = column.field.split('.');
    if (column.type == 'simple' || column.type == 'numeric') {
      if (item[column.field] === undefined) {
        value = '';
      } else {
        value = item[column.field];
      }
    } else if (column.type == 'html') {
      if (item[column.field] === undefined) {
        value = '';
      } else {
        value = Util.stripHtml(item[column.field]);
      }
    } else if (column.type == 'soap-note') {
      if (item[column.field] === undefined) {
        value = '';
      } else {
        value = Util.stripHtml(item[column.field]);
        value = Util.truncate(value, 50);
      }
    } else if (column.type == 'strip-html') {
      if (item[column.field] === undefined) {
        value = '';
      } else {
        value = Util.stripHtml(item[column.field]);
        value = Util.truncate(value, 100);
      }
    } else if (column.type == 'date') {
      if (item[column.field] === undefined) {
        value = '';
      } else {
        value = dateFormat(item[column.field], 'mm/dd/yyyy');
      }
    } else if (column.type == 'date-time') {
      if (item[column.field] === undefined) {
        value = '';
      } else {
        value = dateFormat(item[column.field], 'mm/dd/yyyy hh:mm');
      }
    } else if (column.type == 'double') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      if (item[field0] === undefined) {
        return value;
      }
      value = item[field0][field1];
    } else if (column.type == 'double-person') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      if (item[field0] === undefined) {
        return value;
      }
      value = Util.buildFullName(item[field0]['firstName'], item[field0][
          'middleName'
        ],
        item[field0]['lastName']);
    } else if (column.type == 'double-date') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      if (item[field0] === undefined) {
        return value;
      }
      value = dateFormat(item[field0][field1], 'mm/dd/yyyy');
    } else if (column.type == 'triple-person') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      if (item[field0] === undefined || item[field0][field1] ===
        undefined) {
        return value;
      }
      value = Util.buildFullName(item[field0][field1]['firstName'], item[
          field0][field1]['middleName'],
        item[field0][field1]['lastName']);
    } else if (column.type == 'triple') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      if (item[field0] === undefined || item[field0][field1] ===
        undefined) {
        return value;
      }
      value = item[field0][field1][field2];
    } else if (column.type == 'triple-date') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      if (item[field0] === undefined || item[field0][field1] ===
        undefined) {
        return value;
      }
      value = dateFormat(item[field0][field1][field2], 'mm/dd/yyyy');
    } else if (column.type == 'quad') {
      var field0 = columnFields[0];
      var field1 = columnFields[1];
      var field2 = columnFields[2];
      var field3 = columnFields[3];
      if (item[field0] === undefined || item[field0][field1] ===
        undefined || item[field0][field1][field2] ===
        undefined) {
        return value;
      }
      value = item[field0][field1][field2][field3];
    }
    return value;
  };

  module.handleDoubleClickedRow = function (evt) {
    var id = undefined;
    var tableId = undefined;
    var tableName = undefined;
    var attributes = evt.currentTarget.attributes;
    for (var i = 0; i < attributes.length; i++) {
      if (attributes[i].name == 'name') {
        id = attributes[i].nodeValue;
      } else if (attributes[i].name == 'id') {
        tableId = attributes[i].nodeValue;
      } else if (attributes[i].name == 'data-table-name') {
        tableName = attributes[i].nodeValue;
      }
    }
    if (id !== undefined) {}
  };
  module.handleClickableRow = function (evt) {
    var id = undefined;
    var tableId = undefined;
    var tableName = undefined;
    var attributes = evt.currentTarget.attributes;
    for (var i = 0; i < attributes.length; i++) {
      if (attributes[i].name == 'name') {
        id = attributes[i].nodeValue;
      } else if (attributes[i].name == 'id') {
        tableId = attributes[i].nodeValue;
      } else if (attributes[i].name == 'data-table-name') {
        tableName = attributes[i].nodeValue;
      }
    }
    if (id !== undefined) {
      if (tableName == 'patient-search-results') {
        Events.trigger("update-patient-search-results", id);
      } else if (tableName == 'messages-inbox') {
        Events.trigger("update-messages-inbox", id);
      } else if (tableName == 'chart-encounters-list') {
        Events.trigger("update-chart-encounters-list", id);
      } else if (tableName == 'soap-notes-list') {
        Events.trigger("update-soap-notes-list", id);
      } else if (tableName == 'chief-complaints-list') {
        Events.trigger("update-chief-complaints-list", id);
      }
    }
  };
  return module;
});