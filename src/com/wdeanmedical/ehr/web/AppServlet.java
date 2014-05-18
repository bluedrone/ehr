/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.ClinicianDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.dto.TerminologyDTO;
import com.wdeanmedical.ehr.entity.CPT;
import com.wdeanmedical.ehr.entity.FHIRPatient;
import com.wdeanmedical.ehr.entity.ICD10;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientHealthIssue;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.dto.MessageDTO;
import com.google.gson.Gson;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class AppServlet extends HttpServlet  {
  
  private static final long serialVersionUID = 5141268230082988870L;
  private static final Logger logger = Logger.getLogger(AppServlet.class);
  
  private AppService appService;

  @Override
public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    Core.servletContext = context;
    Core.timeZone = context.getInitParameter("timeZone");
    Core.sendMail = context.getInitParameter("mail.send");
    Core.mailFrom = context.getInitParameter("mail.from");
    Core.smtphost = context.getInitParameter("mail.smtp.host");
    Core.smtpport = context.getInitParameter("mail.smtp.port");
    Core.appBaseDir = context.getRealPath("/");
    Core.appDefaultHeadshot = context.getInitParameter("appDefaultHeadshot");
    Core.patientDirPath = context.getInitParameter("patientDirPath");
    Core.appSessionTimeout = Integer.parseInt(context.getInitParameter("appSessionTimeout"));
    Core.imageMagickHome = context.getInitParameter("IMAGE_MAGICK_HOME");
    Core.imagesDir = context.getInitParameter("imagesDir");
    Core.buildUserPermissionsMap();
    try{
      appService = new AppService();
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
     
    try { 

      if (pathInfo.equals("/login")) {
        returnString = login(request, response);  
      }
      else if (pathInfo.equals("/logout")) {
        returnString = logout(request, response);  
      }
      else { 
        if (isValidSession(request, response) == false) {
          returnString = logout(request, response);  
        }
        else { 
          if (pathInfo.equals("/getClinicianDashboard")) {
            returnString = getClinicianDashboard(request, response);  
          }
          else if (pathInfo.equals("/getClinicianMessage")) {
            returnString = getClinicianMessage(request, response);  
          }
          else if (pathInfo.equals("/getClinicianMessages")) {
            returnString = getClinicianMessages(request, response);  
          }
          else if (pathInfo.equals("/getClinicians")) {
            returnString = getClinicians(request, response);  
          }
          else if (pathInfo.equals("/patientSearch")) {
            returnString = patientSearch(request, response);  
          }
          else if (pathInfo.equals("/getRecentPatients")) {
            returnString = getRecentPatients(request, response);  
          }
          else if (pathInfo.equals("/getPatientChart")) {
            returnString = getPatientChart(request, response);  
          }
          else if (pathInfo.equals("/getPatientChartSummary")) {
            returnString = getPatientChartSummary(request, response);  
          }
          else if (pathInfo.equals("/getPatientHealthIssues")) {
            returnString = getPatientHealthIssues(request, response);  
          }
          else if (pathInfo.equals("/getPatientSearchTypeAheads")) {
            returnString = getPatientSearchTypeAheads(request, response);  
          }
          else if (pathInfo.equals("/park")) {
            returnString = park(request, response);  
          }
          else if (pathInfo.equals("/unpark")) {
            returnString = unpark(request, response);  
          }
          else if (pathInfo.equals("/searchICD10")) {
            returnString = searchICD10(request, response);  
          }
          else if (pathInfo.equals("/searchCPT")) {
            returnString = searchCPT(request, response);  
          }
        }
      }
     
      ServletOutputStream  out = null;
      response.setContentType("text/plain");
      out = response.getOutputStream();
      out.println(returnString);
      logger.debug(returnString);
      out.close();
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
  
  
  protected  boolean isValidSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String ipAddress = request.getRemoteHost();
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AuthorizedDTO dto = gson.fromJson(data, AuthorizedDTO.class);  
    if (dto == null){
      dto = new AuthorizedDTO();
      dto.setSessionId(request.getParameter("sessionId"));
    }
    return appService.isValidSession(dto, ipAddress, request.getPathInfo());
  }
  
  
  public String searchICD10(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String searchText = request.getParameter("searchText");
    Gson gson = new Gson();
    TerminologyDTO dto = new TerminologyDTO(); 
    dto.setSearchText(searchText);
    List<ICD10> icd10List = appService.searchICD10(dto); 
    dto.setIcd10List(icd10List);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String searchCPT(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String searchText = request.getParameter("searchText");
    Gson gson = new Gson();
    TerminologyDTO dto = new TerminologyDTO(); 
    dto.setSearchText(searchText);
    List<CPT> cptList = appService.searchCPT(dto); 
    dto.setCptList(cptList);
    String json = gson.toJson(dto);
    return json;
  }
  
    
  public String getClinicianDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ClinicianDTO dto = gson.fromJson(data, ClinicianDTO.class); 
    appService.getClinicianDashboard(dto); 
    String json = gson.toJson(dto);
    return json;
  }
  
  
  public String getPatientSearchTypeAheads(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ClinicianDTO dto = gson.fromJson(data, ClinicianDTO.class); 
    appService.getPatientSearchTypeAheads(dto); 
    String json = gson.toJson(dto);
    return json;
  }
 
 
  public String getClinicianMessages(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ClinicianDTO dto = gson.fromJson(data, ClinicianDTO.class); 
    List<PatientMessage> clinicianMessages = appService.getClinicianMessages(dto, false); 
    dto.setPatientMessages(clinicianMessages);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String getClinicians(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ClinicianDTO dto = gson.fromJson(data, ClinicianDTO.class); 
    List<Clinician> clinicians = appService.getClinicians(dto); 
    dto.setClinicians(clinicians);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String getClinicianMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    MessageDTO dto = gson.fromJson(data, MessageDTO.class); 
    boolean result = appService.getClinicianMessage(dto); 
    String json = gson.toJson(dto);
    return (json);
  }
  
  public String patientSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<Patient> patients = appService.getFilteredPatients(dto); 
    dto.setPatients(patients);
    String json = gson.toJson(dto);
    System.out.println(json);
    return json;
  }
  
 
  
  public String getRecentPatients(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<Patient> patients = appService.getRecentPatients(dto); 
    dto.setPatients(patients);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String getPatientChart(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    appService.getPatientChart(dto); 
    String json = gson.toJson(dto);
    return json;
  }
  
  public String getPatientChartSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    appService.getPatientChartSummary(dto); 
    String json = gson.toJson(dto);
    return json;
  }
  
  public String getPatientHealthIssues(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<PatientHealthIssue> patientHealthIssues = appService.getPatientHealthIssues(dto); 
    dto.setPatientHealthIssues(patientHealthIssues);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    LoginDTO loginDTO = gson.fromJson(data, LoginDTO.class);  
    String ipAddress = request.getRemoteHost();
    Clinician clinician = appService.login(loginDTO, ipAddress); 
    String json = gson.toJson(clinician);
    return json;
  }
  
  
  public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AuthorizedDTO dto = gson.fromJson(data, AuthorizedDTO.class);  
    appService.logout(dto);
    dto.setAuthenticated(false);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String park(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AuthorizedDTO dto = gson.fromJson(data, AuthorizedDTO.class);  
    appService.park(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String unpark(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AuthorizedDTO dto = gson.fromJson(data, AuthorizedDTO.class);  
    appService.unpark(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  public String checkSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AuthorizedDTO dto = gson.fromJson(data, AuthorizedDTO.class);  
    if (dto == null) {
      dto = new AuthorizedDTO();
    }
    dto.setAuthenticated(isValidSession(request, response));
    String json = gson.toJson(dto);
    return json;
  }
  
}
 
 
