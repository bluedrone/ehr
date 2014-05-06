/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.service.PatientService;
import com.wdeanmedical.ehr.core.Core;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class PatientServlet extends AppServlet  {
  
  private static final long serialVersionUID = 1196033626033964617L;
  private static final Logger log = Logger.getLogger(PatientServlet.class);
  
  private PatientService patientService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    try{
      patientService = new PatientService();
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
    boolean isUploadResponse = false;
     
    try { 
      if (isValidSession(request, response) == false) {
        returnString = logout(request, response);  
      }
      else { 
        if (pathInfo.equals("/acquirePatient")) {
          returnString = acquirePatient(request, response);  
        }
        else if (pathInfo.equals("/overridePatient")) {
          returnString = overridePatient(request, response);  
        }
        else if (pathInfo.equals("/addIntakeMedication")) {
          returnString = addIntakeMedication(request, response);  
        }
        else if (pathInfo.equals("/addIntakeQuestion")) {
          returnString = addIntakeQuestion(request, response);  
        }
        else if (pathInfo.equals("/closeEncounter")) {
          returnString = closeEncounter(request, response);  
        }
        else if (pathInfo.equals("/getEncounter")) {
          returnString = getEncounter(request, response);  
        }
        else if (pathInfo.equals("/closeProgressNote")) {
          returnString = closeProgressNote(request, response);  
        }
        else if (pathInfo.equals("/newEncounter")) {
          returnString = newEncounter(request, response);  
        }
        else if (pathInfo.equals("/newProgressNote")) {
          returnString = newProgressNote(request, response);  
        }
        else if (pathInfo.equals("/getPatientEncounters")) {
          returnString = getPatientEncounters(request, response);  
        }
        else if (pathInfo.equals("/getProgressNotes")) {
          returnString = getProgressNotes(request, response);  
        }
        else if (pathInfo.equals("/createPatientAndEncounter")) {
          returnString = createPatientAndEncounter(request, response);  
        }
        else if (pathInfo.equals("/getCurrentPatientEncounter")) {
          returnString = getCurrentPatientEncounter(request, response);  
        }
        else if (pathInfo.equals("/deletePatient")) {
          returnString = deletePatient(request, response);  
        }
        if (pathInfo.equals("/releasePatient")) {
          returnString = releasePatient(request, response);  
        }
        if (pathInfo.equals("/createBasicInfo")) {
          returnString = createBasicInfo(request, response);  
        }
        else if (pathInfo.equals("/createCC")) {
          returnString = createCC(request, response);  
        }
        else if (pathInfo.equals("/createExam")) {
          returnString = createExam(request, response);  
        }
        else if (pathInfo.equals("/createFollowUp")) {
          returnString = createFollowUp(request, response);  
        }
        else if (pathInfo.equals("/createHist")) {
          returnString = createHist(request, response);  
        }
        else if (pathInfo.equals("/updateIntakeMedication")) {
          returnString = updateIntakeMedication(request, response);  
        }
        else if (pathInfo.equals("/updateIntakeQuestion")) {
          returnString = updateIntakeQuestion(request, response);  
        }
        else if (pathInfo.equals("/updatePatient")) {
          returnString = updatePatient(request, response);  
        }
        else if (pathInfo.equals("/createVitals")) {
          returnString = createVitals(request, response);  
        }
        else if (pathInfo.equals("/createFamily")) {
          returnString = createFamily(request, response);  
        }
        else if (pathInfo.equals("/createOBGYN")) {
          returnString = createOBGYN(request, response);  
        }
        else if (pathInfo.equals("/createPFSH")) {
          returnString = createPFSH(request, response);  
        }
        else if (pathInfo.equals("/createSupp")) {
          returnString = createSupp(request, response);  
        }
        else if (pathInfo.equals("/uploadProfileImage")) {
          isUploadResponse = true;
          returnString = uploadProfileImage(request, response);  
        }
      }
     
      ServletOutputStream  out = null;
      response.setContentType("text/plain");
     
      if (isUploadResponse == false) { 
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
    
  public String createPatientAndEncounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createPatientAndEncounter(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  public String getCurrentPatientEncounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.getCurrentPatientEncounter(dto);
    String json = gson.toJson(dto);
    return json;
  }

  
  public String deletePatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.deactivatePatient(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String updatePatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.updatePatient(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  public String updateIntakeMedication(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.updateIntakeMedication(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String updateIntakeQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.updateIntakeQuestion(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createBasicInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createBasicInfo(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createVitals(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createVitals(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createFamily(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createFamily(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createCC(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createCC(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createOBGYN(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createOBGYN(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createPFSH(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createPFSH(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createExam(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createExam(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createFollowUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createFollowUp(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createHist(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createHist(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String createSupp(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createSupp(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String addIntakeMedication(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.addIntakeMedication(dto.getPatientId());
    String json = gson.toJson(dto);
    return json;
  }
  
  public String addIntakeQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.addIntakeQuestion(dto.getEncounterId());
    String json = gson.toJson(dto);
    return json;
  }
  
  public String newEncounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    Encounter encounter =  patientService.newEncounter(dto);
    dto.setEncounter(encounter);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String newProgressNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    ProgressNote note =  patientService.newProgressNote(dto);
    dto.setProgressNote(note);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String updateProgressNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    ProgressNote note =  patientService.updateProgressNote(dto);
    dto.setProgressNote(note);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String getEncounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    Encounter encounter =  patientService.getEncounter(dto);
    dto.setEncounter(encounter);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  public String getPatientEncounters(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<Encounter> encounters =  patientService.getPatientEncounters(dto);
    dto.setPatientEncounters(encounters);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  public String getProgressNotes(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<ProgressNote> notes =  patientService.getProgressNotes(dto);
    dto.setProgressNotes(notes);
    String json = gson.toJson(dto);
    return json;
  }

  public String acquirePatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.acquirePatient(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String overridePatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.overridePatient(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String releasePatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.releasePatient(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String closeEncounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.closeEncounter(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String closeProgressNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.closeProgressNote(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String uploadProfileImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
    return patientService.uploadProfileImage(request, response);
  }

 
}
 
 