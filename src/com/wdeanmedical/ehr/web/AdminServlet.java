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
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.service.AdminService;
import com.google.gson.Gson;

import org.apache.log4j.Logger;

public class AdminServlet extends AppServlet  {
  
  private static final Logger log = Logger.getLogger(AdminServlet.class);
  
  private AdminService adminService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    try{
      adminService = new AdminService();
    }
    catch(MalformedURLException e){
      e.printStackTrace();
    }
  }
    
  @Override
  public void doPost( HttpServletRequest request, HttpServletResponse response) {
    String returnString = "";
    String pathInfo = request.getPathInfo();
    String servletPath = request.getServletPath();
    boolean isBinaryResponse = false;
     
    try { 
      if (isValidSession(request, response) == false) {
        returnString = logout(request, response);  
      }
      else { 
        if (pathInfo.equals("/activateClinician")) {
          returnString = getAdminData(request, "/activateClinician");  
        }
        else if (pathInfo.equals("/deactivateClinician")) {
          returnString = getAdminData(request, "/deactivateClinician");  
        }
        else if (pathInfo.equals("/purgeClinician")) {
          returnString = getAdminData(request, "/purgeClinician");  
        }
        else if (pathInfo.equals("/saveNewClinician")) {
          returnString = getAdminData(request, "/saveNewClinician");  
        }
        else if (pathInfo.equals("/updateClinician")) {
          returnString = getAdminData(request, "/updateClinician");  
        }
      }
     
      ServletOutputStream  out = null;
      response.setContentType("text/plain");
     
      if (isBinaryResponse == true) { 
        out = response.getOutputStream();
        out.println(returnString);
        out.close();
      }
      else { 
    	  if(returnString != null){
    		  PrintWriter ajaxOut = response.getWriter();
    		  ajaxOut.write(returnString);
    		  ajaxOut.close();
    	  }
      }
    
    }  
    catch( IOException ioe ) {
      ioe.printStackTrace();
    } 
    catch( Exception e ) {
      e.printStackTrace();
    }
  }  

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    doPost(request, response);  
  }
  
  public String getAdminData(HttpServletRequest request, String pathAction) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AdminDTO dto = gson.fromJson(data, AdminDTO.class); 
    if(pathAction.equals("/saveNewClinician")) {
      adminService.saveNewClinician(dto);
    } 
    else if(pathAction.equals("/updateClinician")) {
      adminService.updateClinician(dto);
    } 
    else if(pathAction.equals("/activateClinician")) {
      adminService.activateClinician(dto);
    }
    else if(pathAction.equals("/deactivateClinician")) {
      adminService.deactivateClinician(dto);
    }
    else if(pathAction.equals("/purgeClinician")) {
      adminService.purgeClinician(dto);
    }
    String json = gson.toJson(dto);
    return json;
  }

}
