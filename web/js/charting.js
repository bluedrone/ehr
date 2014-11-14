
"use strict";

modulejs.define('charting', ["dto"], function (Dto) {
  var module = {};

  module.renderLineChart = function (labels, data) {
    var lineChartData = {
      labels: labels,
      datasets: [{
        fillColor: "rgba(151,187,205,0.5)",
        strokeColor: "rgba(151,187,205,1)",
        pointColor: "rgba(151,187,205,1)",
        pointStrokeColor: "#fff",
        data: data
      }]
    };
    var options = {
      scaleFontSize: 14,
      bezierCurve: false,
      animation: false
    };
    var myLine = new Dto.chart(document.getElementById(
      "health-trends-chart-canvas").getContext("2d")).Line(
      lineChartData,
      options);
  };
  return module;
});