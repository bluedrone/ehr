/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.gson.Gson;
import com.wdeanmedical.ehr.dto.ActivityLogDTO;
import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.entity.Report;
import com.wdeanmedical.ehr.service.ReportsService;

public class ReportsServlet extends AppServlet {

  private static final Logger log = Logger.getLogger(ReportsServlet.class);

  private ReportsService reportsService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    try {
      reportsService = new ReportsService();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    String returnString = "";
    String pathInfo = request.getPathInfo();
    boolean isBinaryResponse = false;

    try {
      if (isValidSession(request, response) == false) {
        returnString = logout(request, response);
      } else {
        if (pathInfo.equals("/getActivityLog")) {
          returnString = getActivityLog(request, response);
        } else if (pathInfo.equals("/exportCsv")) {
          returnString = exportCsv(request, response);
        } else if (pathInfo.equals("/getReportsList")) {
          returnString = getReportsList(request, response);
        } else if (pathInfo.equals("/getActivityLogSearchTypeAheads")) {
          returnString = getActivityLogSearchTypeAheads(request, response);  
        }else if (pathInfo.equals("/filterActivityLog")) {
          returnString = filterActivityLog(request, response);  
        }
      }

      ServletOutputStream out = null;
      response.setContentType("text/plain");

      if (isBinaryResponse == true) {
        out = response.getOutputStream();
        out.println(returnString);
        out.close();
      } else {
        PrintWriter ajaxOut = response.getWriter();
        ajaxOut.write(returnString);
        ajaxOut.close();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    doPost(request, response);
  } // getReportsList

  public String getReportsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AdminDTO dto = gson.fromJson(data, AdminDTO.class);
    List<Report> reportList = reportsService.getReportList(dto);
    String json = gson.toJson(reportList);
    return json;
  }

  public String getActivityLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AdminDTO dto = gson.fromJson(data, AdminDTO.class);
    List<ActivityLogDTO> activityLogs = reportsService.getActivityLog(dto);
    String json = gson.toJson(activityLogs);
    return json;
  }

  public String exportCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AdminDTO dto = gson.fromJson(data, AdminDTO.class);
    HSSFWorkbook workbook = reportsService.getWorkbook(dto);
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=ActivityLog.xls");
    workbook.write(response.getOutputStream());
    response.getOutputStream().close();
    return null;
  }
  
  public String getActivityLogSearchTypeAheads(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ActivityLogDTO dto = gson.fromJson(data, ActivityLogDTO.class); 
    reportsService.getActivityLogSearchTypeAheads(dto); 
    String json = gson.toJson(dto);
    return json;
  }
  
  public String filterActivityLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ActivityLogDTO dto = gson.fromJson(data, ActivityLogDTO.class); 
    List<ActivityLogDTO> activityLogs = reportsService.filterActivityLog(dto); 
    String json = gson.toJson(activityLogs);
    return json;   
  }

}
