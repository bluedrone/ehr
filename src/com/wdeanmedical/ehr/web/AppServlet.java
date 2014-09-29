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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.core.Permissions;
import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.ClinicianDTO;
import com.wdeanmedical.ehr.dto.DTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.dto.TerminologyDTO;
import com.wdeanmedical.ehr.entity.CPT;
import com.wdeanmedical.ehr.entity.CPTModifier;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.ICD10;
import com.wdeanmedical.ehr.entity.ICD9;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientHealthIssue;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.util.DataEncryptor;
import com.wdeanmedical.ehr.dto.MessageDTO;
import com.wdeanmedical.ehr.dto.AppointmentDTO;
import com.wdeanmedical.ehr.entity.Appointment;
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
    try { DataEncryptor.setEncryptionKey(context.getInitParameter("encryptionKey")); } catch (Exception e1) { e1.printStackTrace();}
    Core.timeZone = context.getInitParameter("timeZone");
    Core.sendMail = context.getInitParameter("mail.send");
    Core.mailFrom = context.getInitParameter("mail.from");
    Core.smtphost = context.getInitParameter("mail.smtp.host");
    Core.smtpport = context.getInitParameter("mail.smtp.port");
    Core.appBaseDir = context.getRealPath("/");
    Core.appDefaultHeadshot = context.getInitParameter("appDefaultHeadshot");
    Core.filesHome = context.getInitParameter("filesHome");
    Core.patientDirPath = context.getInitParameter("patientDirPath");
    Core.appSessionTimeout = Integer.parseInt(context.getInitParameter("appSessionTimeout"));
    Core.imageMagickHome = context.getInitParameter("IMAGE_MAGICK_HOME");
    Core.imagesDir = context.getInitParameter("imagesDir");
    Core.pmHome = context.getInitParameter("pmHome");
    Permissions.buildClinicianPermissionsMap();
    try{ appService = new AppService(); } catch(MalformedURLException e){ e.printStackTrace(); }
  }


  @Override
  public void doPost( HttpServletRequest request, HttpServletResponse response) {
    String returnString = "";
    String pathInfo = request.getPathInfo();
    String servletPath = request.getServletPath();
    boolean isBinaryResponse = false;

    try { 
      if (pathInfo.equals("/getStaticLists")) {
        returnString = getStaticLists(request, response);  
      }
      else if (pathInfo.equals("/login")) {
        returnString = login(request, response);  
      }
      else if (pathInfo.equals("/logout")) {
        returnString = logout(request, response);  
      }
      else if (pathInfo.equals("/updateSession")) {
        returnString = updateSession(request, response);  
      }
      else { 
        if (isValidSession(request, response) == false) {
          returnString = logout(request, response);  
        }
        else { 
          if (pathInfo.equals("/getAppointment")) {
            returnString = getAppointment(request, response);  
          }
          else if (pathInfo.equals("/getAppointments")) {
            returnString = getAppointments(request, response);  
          }
          else if (pathInfo.equals("/getAppointmentsByClinician")) {
            returnString = getAppointmentsByClinician(request, response);  
          }
          else if (pathInfo.equals("/getClinicianDashboard")) {
            returnString = getClinicianData(request, "/getClinicianDashboard");
          }
          else if (pathInfo.equals("/getClinicianMessage")) {
            returnString = getClinicianMessage(request, response);  
          }
          else if (pathInfo.equals("/getClinicianMessages")) {
            returnString = getClinicianData(request, "/getClinicianMessages");
          }
          else if (pathInfo.equals("/getClinicians")) {
            returnString = getClinicianData(request, "/getClinicians");
          }
          else if (pathInfo.equals("/getCPTModifiers")) {
            returnString = getCPTModifiers(request, response);  
          }
          else if (pathInfo.equals("/getPatientChart")) {
            returnString = getPatientData(request, "/getPatientChart");
          }
          else if (pathInfo.equals("/getPatientChartSummary")) {
            returnString = getPatientData(request, "/getPatientChartSummary");  
          }
          else if (pathInfo.equals("/getPatientHealthIssues")) {
            returnString = getPatientData(request, "/getPatientHealthIssues");  
          }
          else if (pathInfo.equals("/getPatientSearchTypeAheads")) {
            returnString = getClinicianData(request, "/getPatientSearchTypeAheads");
          }
          else if (pathInfo.equals("/getRecentPatients")) {
            returnString = getPatientData(request, "/getRecentPatients");  
          }
          else if (pathInfo.equals("/park")) {
            returnString = getAuthorizedData(request, "/park");  
          }
          else if (pathInfo.equals("/patientSearch")) {
            returnString = getPatientData(request, "/patientSearch");
          }
          else if (pathInfo.equals("/searchCPT")) {
            returnString = searchCPT(request, response);  
          }
          else if (pathInfo.equals("/searchICD9")) {
            returnString = searchICD9(request, response);  
          }
          else if (pathInfo.equals("/searchICD10")) {
            returnString = searchICD10(request, response);  
          }
          else if (pathInfo.equals("/unpark")) {
            returnString = getAuthorizedData(request, "/unpark");  
          }
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
        PrintWriter ajaxOut = response.getWriter();
        ajaxOut.write(returnString);
        ajaxOut.close();
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


  protected  boolean isValidSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String ipAddress = request.getRemoteHost();
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AuthorizedDTO dto = null;
    try{
      dto = gson.fromJson(data, AuthorizedDTO.class);  
    }catch (Exception e){}
    if (dto == null || dto.getSessionId() == null) {
      dto = new AuthorizedDTO();
      dto.setSessionId(request.getParameter("sessionId"));
    }
    String path = request.getPathInfo();
    if(path.substring(1).split("/").length > 1) {
      path = path.substring(1).split("/")[1];
    } 
    path = request.getServletPath() + path;
    return appService.isValidSession(dto, ipAddress, path);
  }


  public String updateSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ClinicianSession dto = gson.fromJson(data, ClinicianSession.class);  
    Clinician clinician = appService.updateSession(dto, request); 
    String json = gson.toJson(clinician);
    return json;
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



  public String searchICD9(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String searchText = request.getParameter("searchText");
    Gson gson = new Gson();
    TerminologyDTO dto = new TerminologyDTO(); 
    dto.setSearchText(searchText);
    List<ICD9> icd9List = appService.searchICD9(dto); 
    dto.setIcd9List(icd9List);
    String json = gson.toJson(dto);
    return json;
  }



  public String getCPTModifiers(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Gson gson = new Gson();
    TerminologyDTO dto = new TerminologyDTO(); 
    List<CPTModifier> cptModifiers = appService.getCPTModifiers(dto); 
    dto.setCptModifierList(cptModifiers);
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



  public String getClinicianData(HttpServletRequest request, String pathAction) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    ClinicianDTO dto = gson.fromJson(data, ClinicianDTO.class); 
    if(pathAction.equals("/getClinicianDashboard")) {
      appService.getClinicianDashboard(dto);
    } 
    else if(pathAction.equals("/getPatientSearchTypeAheads")) {
      appService.getPatientSearchTypeAheads(dto);
    }
    else if(pathAction.equals("/getClinicianMessages")) {
      List<PatientMessage> clinicianMessages = appService.getClinicianMessages(dto, false); 
      dto.setPatientMessages(clinicianMessages);
    } 
    else if(pathAction.equals("/getClinicians")) {
      List<Clinician> clinicians = appService.getClinicians(dto); 
      dto.setClinicians(clinicians);
    } 
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

  public String getPatientData(HttpServletRequest request, String pathAction) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    if(pathAction.equals("/patientSearch")) {
      List<Patient> patients = appService.getFilteredPatients(dto); 
      dto.setPatients(patients);
    } 
    else if(pathAction.equals("/getRecentPatients")) {
      List<Patient> patients = appService.getRecentPatients(dto); 
      dto.setPatients(patients);
    } 
    else if(pathAction.equals("/getPatientChart")) {
      appService.getPatientChart(dto);
    }
    else if(pathAction.equals("/getPatientChartSummary")) {
      appService.getPatientChartSummary(dto); 
    }
    else if(pathAction.equals("/getPatientHealthIssues")) {
      List<PatientHealthIssue> patientHealthIssues = appService.getPatientHealthIssues(dto); 
      dto.setPatientHealthIssues(patientHealthIssues);
    }
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

  public String getAuthorizedData(HttpServletRequest request, String pathAction) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AuthorizedDTO dto = gson.fromJson(data, AuthorizedDTO.class);  
    if(pathAction.equals("/park")) {
      appService.park(dto);
    }
    else if(pathAction.equals("/unpark")) {
      appService.unpark(dto);
    }
    String json = gson.toJson(dto);
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



  public String getAppointment(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    AppointmentDTO dto = gson.fromJson(data, AppointmentDTO.class); 
    boolean result = appService.getAppointment(dto);
    String json = gson.toJson(dto);
    return json;
  }



  public String getAppointments(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Gson gson = new Gson();
    List<Appointment> bookedAppts = null;
    bookedAppts = appService.getAllAppointments();

    ArrayList<Map<String, Object>> visitsList = new ArrayList<Map<String, Object>>();
    Map<String, Object> visitInstance = null;
    if(bookedAppts != null) {
      for(Appointment event : bookedAppts) {
        visitInstance = new HashMap<String, Object>();
        visitInstance.put("id", event.getId());
        visitInstance.put("title", event.getTitle());
        visitInstance.put("start", formatDate(event.getStartTime()));
        visitInstance.put("end", formatDate(event.getEndTime()));
        visitInstance.put("desc", event.getDesc());
        visitInstance.put("allDay", Boolean.FALSE);
        visitsList.add(visitInstance);
      }
    }
    return gson.toJson(visitsList);
  }



  public String getAppointmentsByClinician(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Gson gson = new Gson();
    List<Appointment> bookedAppts = null;
    Clinician clinician = appService.getClinicianBySessionId(request.getParameter("sessionId"));
    bookedAppts = appService.getAllAppointmentsByClinician(clinician);

    ArrayList<Map<String, Object>> visitsList = new ArrayList<Map<String, Object>>();
    Map<String, Object> visitInstance = null;
    if(bookedAppts != null) {
      for(Appointment event : bookedAppts) {
        visitInstance = new HashMap<String, Object>();
        visitInstance.put("id", event.getId());
        visitInstance.put("title", event.getTitle());
        visitInstance.put("start", formatDate(event.getStartTime()));
        visitInstance.put("end", formatDate(event.getEndTime()));
        visitInstance.put("desc", event.getDesc());
        visitInstance.put("allDay", Boolean.FALSE);
        visitsList.add(visitInstance);
      }
    }
    return gson.toJson(visitsList);
  }


  public static String formatDate(Date date){
    String value = null;
    if (date != null){
      SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      value = dateformat.format(date);
    }
    return value;
  }


  public String getStaticLists(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String json = appService.getStaticLists(); 
    return json;
  }


}
