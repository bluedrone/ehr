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
import com.wdeanmedical.ehr.entity.ChiefComplaint;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.SOAPNote;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.service.PatientService;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.entity.VitalSigns;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class PatientServlet extends AppServlet  {
  
  private static final long serialVersionUID = 1196033626033964617L;
  private static final Logger log = Logger.getLogger(PatientServlet.class);
  
  private PatientService patientService;
  private AppService appService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    try{
      patientService = new PatientService();
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
    boolean isBinaryResponse = false;
     
    try { 
      if (isValidSession(request, response) == false) {
        returnString = logout(request, response);  
      }
      else { 
        if (pathInfo.equals("/acquirePatient")) {
          returnString = acquirePatient(request, response);  
        }
        else if (pathInfo.equals("/addEncounterQuestion")) {
          returnString = addEncounterQuestion(request, response);  
        }
        else if (pathInfo.equals("/addEncounterMedication")) {
          returnString = addEncounterMedication(request, response);  
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
        else if (pathInfo.equals("/closeEncounter")) {
          returnString = closeEncounter(request, response);  
        }
        else if (pathInfo.equals("/closeProgressNote")) {
          returnString = closeProgressNote(request, response);  
        }
        else if (pathInfo.equals("/createOBGYN")) {
          returnString = createOBGYN(request, response);  
        }
        else if (pathInfo.equals("/createPatientAndEncounter")) {
          returnString = createPatientAndEncounter(request, response);  
        }
        else if (pathInfo.equals("/createPFSH")) {
          returnString = createPFSH(request, response);  
        }
        else if (pathInfo.equals("/createSOAPNote")) {
          returnString = createSOAPNote(request, response);  
        }
        else if (pathInfo.equals("/createSupp")) {
          returnString = createSupp(request, response);  
        }
        else if (pathInfo.equals("/createVitals")) {
          returnString = createVitals(request, response);  
        }
        else if (pathInfo.equals("/deletePatient")) {
          returnString = deletePatient(request, response);  
        }
        else if (pathInfo.equals("/encryptPatients")) {
          returnString = encryptPatients(request, response);  
        }
        else if (pathInfo.equals("/getChiefComplaints")) {
          returnString = getChiefComplaints(request, response);  
        }
        else if (pathInfo.equals("/getCurrentPatientEncounter")) {
          returnString = getCurrentPatientEncounter(request, response);  
        }
        else if (pathInfo.equals("/getEncounter")) {
          returnString = getEncounter(request, response);  
        }
        else if (pathInfo.equals("/getPatientEncounters")) {
          returnString = getPatientEncounters(request, response);  
        }
        else if (pathInfo.equals("/getPatientProfileImage")) {
          isBinaryResponse = true;
          returnString = getPatientProfileImage(request, response);  
        }
        else if (pathInfo.equals("/getPatientVitalSigns")) {
          returnString = getPatientVitalSigns(request, response);  
        }
        else if (pathInfo.equals("/getProgressNotes")) {
          returnString = getProgressNotes(request, response);  
        }
        else if (pathInfo.equals("/getSOAPNotes")) {
          returnString = getSOAPNotes(request, response);  
        }
        else if (pathInfo.equals("/newEncounter")) {
          returnString = newEncounter(request, response);  
        }
        else if (pathInfo.equals("/newProgressNote")) {
          returnString = newProgressNote(request, response);  
        }
        else if (pathInfo.equals("/overridePatient")) {
          returnString = overridePatient(request, response);  
        } 
        else if (pathInfo.equals("/releasePatient")) {
          returnString = releasePatient(request, response);  
        }
        else if (pathInfo.equals("/updateEncounterMedication")) {
          returnString = updateEncounterMedication(request, response);  
        }
        else if (pathInfo.equals("/updateEncounterQuestion")) {
          returnString = updateEncounterQuestion(request, response);  
        }
        else if (pathInfo.equals("/updatePatient")) {
          returnString = updatePatient(request, response);  
        }
        else if (pathInfo.equals("/uploadProfileImage")) {
          returnString = uploadProfileImage(request, response);  
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
    
  public String createPatientAndEncounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createPatientAndEncounter(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  public String getPatientProfileImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    String profileImagePath = request.getParameter("profileImagePath"); 
    Gson gson = new Gson();
    String patientId = request.getParameter("patientId");
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    String filesHomePatientDirPath =  Core.filesHome  + Core.patientDirPath + "/" + patientId + "/";
	appService.getFile(request, response, getServletContext(), filesHomePatientDirPath, profileImagePath);  
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
  
  
  public String updateEncounterMedication(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.updateEncounterMedication(dto);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String updateEncounterQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.updateEncounterQuestion(dto);
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
  
  
    
  public String getPatientVitalSigns(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<VitalSigns> patientVitalSigns = patientService.getPatientVitalSigns(dto); 
    dto.setVitalSigns(patientVitalSigns);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  
  public String createSOAPNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.createSOAPNote(dto);
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
  
  public String addEncounterMedication(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    Integer encounterMedicationId = patientService.addEncounterMedication(dto.getPatientId());
    dto.setEncounterMedicationId(encounterMedicationId);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String addEncounterQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    Integer encounterQuestionId = patientService.addEncounterQuestion(dto.getEncounterId());
    dto.setEncounterQuestionId(encounterQuestionId);
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
  
  
  
  
  public String getChiefComplaints(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<ChiefComplaint> chiefComplaints =  patientService.getChiefComplaints(dto);
    dto.setChiefComplaints(chiefComplaints);
    String json = gson.toJson(dto);
    return json;
  }
  
  
  
  
  public String getSOAPNotes(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<SOAPNote> notes =  patientService.getSOAPNotes(dto);
    dto.setSOAPNotes(notes);
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
  
  
  
  public String encryptPatients(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    patientService.encryptPatients(dto);
    String json = gson.toJson(dto);
    return json;
  }

 
}
 
 
