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
          returnString = getPatientServiceData(request, "/acquirePatient");  
        }
        else if (pathInfo.equals("/addDxCode")) {
          returnString = getPatientServiceData(request, "/addDxCode");  
        }
        else if (pathInfo.equals("/addEncounterQuestion")) {
          returnString = getPatientServiceData(request, "/addEncounterQuestion");  
        }
        else if (pathInfo.equals("/addPatientMedication")) {
          returnString = getPatientServiceData(request, "/addPatientMedication");  
        }
        else if (pathInfo.equals("/addTxCode")) {
          returnString = getPatientServiceData(request, "/addTxCode");  
        } 
        else if (pathInfo.equals("/createCC")) {
          returnString = getPatientServiceData(request, "/createCC");  
        }
        else if (pathInfo.equals("/createExam")) {
          returnString = getPatientServiceData(request, "/createExam");  
        }
        else if (pathInfo.equals("/createFollowUp")) {
          returnString = getPatientServiceData(request, "/createFollowUp");  
        }
        else if (pathInfo.equals("/createHist")) {
          returnString = getPatientServiceData(request, "/createHist");  
        }
        else if (pathInfo.equals("/closeEncounter")) {
          returnString = getPatientServiceData(request, "/closeEncounter");  
        }
        else if (pathInfo.equals("/closeProgressNote")) {
          returnString = getPatientServiceData(request, "/closeProgressNote");  
        }
        else if (pathInfo.equals("/createOBGYN")) {
          returnString = getPatientServiceData(request, "/createOBGYN");  
        }
        else if (pathInfo.equals("/createPFSH")) {
          returnString = getPatientServiceData(request, "/createPFSH");  
        }
        else if (pathInfo.equals("/createSOAPNote")) {
          returnString = getPatientServiceData(request, "/createSOAPNote");  
        }
        else if (pathInfo.equals("/createSupp")) {
          returnString = getPatientServiceData(request, "/createSupp");  
        }
        else if (pathInfo.equals("/createVitals")) {
          returnString = getPatientServiceData(request, "/createVitals");  
        }
        else if (pathInfo.equals("/deletePatient")) {
          returnString = getPatientServiceData(request, "/deletePatient");  
        }
        else if (pathInfo.equals("/deletePatientMedication")) {
          returnString = getPatientServiceData(request, "/deletePatientMedication");  
        }
        else if (pathInfo.equals("/deleteDxCode")) {
          returnString = getPatientServiceData(request, "/deleteDxCode");  
        }
        else if (pathInfo.equals("/deleteTxCode")) {
          returnString = getPatientServiceData(request, "/deleteTxCode");  
        }
        else if (pathInfo.equals("/encryptPatients")) {
          returnString = getPatientServiceData(request, "/encryptPatients");  
        }
        else if (pathInfo.equals("/getChiefComplaints")) {
          returnString = getPatientServiceData(request, "/getChiefComplaints");  
        }
        else if (pathInfo.equals("/getCurrentPatientEncounter")) {
          returnString = getPatientServiceData(request, "/getCurrentPatientEncounter");  
        }
        else if (pathInfo.equals("/getEncounter")) {
          returnString = getPatientServiceData(request, "/getEncounter");  
        }
        else if (pathInfo.equals("/getPatientEncounters")) {
          returnString = getPatientServiceData(request, "/getPatientEncounters");  
        }
        else if (pathInfo.equals("/getPatientProfileImage")) {
          isBinaryResponse = true;
          returnString = getPatientProfileImage(request, response);  
        }
        else if (pathInfo.equals("/getPatientVitalSigns")) {
          returnString = getPatientServiceData(request, "/getPatientVitalSigns");  
        }
        else if (pathInfo.equals("/getProgressNotes")) {
          returnString = getPatientServiceData(request, "/getProgressNotes");  
        }
        else if (pathInfo.equals("/getSOAPNotes")) {
          returnString = getPatientServiceData(request, "/getSOAPNotes");  
        }
        else if (pathInfo.equals("/newEncounter")) {
          returnString = getPatientServiceData(request, "/newEncounter");  
        }
        else if (pathInfo.equals("/newProgressNote")) {
          returnString = getPatientServiceData(request, "/newProgressNote");  
        }
        else if (pathInfo.equals("/overridePatient")) {
          returnString = getPatientServiceData(request, "/overridePatient");  
        } 
        else if (pathInfo.equals("/releasePatient")) {
          returnString = getPatientServiceData(request, "/releasePatient");  
        }
        else if (pathInfo.equals("/updateDxCode")) {
          returnString = getPatientServiceData(request, "/updateDxCode");  
        }
        else if (pathInfo.equals("/updateEncounterQuestion")) {
          returnString = getPatientServiceData(request, "/updateEncounterQuestion");  
        }
        else if (pathInfo.equals("/updatePatient")) {
          returnString = getPatientServiceData(request, "/updatePatient");  
        }
        else if (pathInfo.equals("/updatePatientMedication")) {
          returnString = getPatientServiceData(request, "/updatePatientMedication");  
        }
        else if (pathInfo.equals("/uploadProfileImage")) {
          returnString = uploadProfileImage(request, response);  
        }
        else if (pathInfo.equals("/updateTxCode")) {
          returnString = getPatientServiceData(request, "/updateTxCode");  
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

  public String getPatientServiceData(HttpServletRequest request, String pathAction) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    if(pathAction.equals("/getChiefComplaints")) {
      List<ChiefComplaint> chiefComplaints =  patientService.getChiefComplaints(dto);
      dto.setChiefComplaints(chiefComplaints);
    }
    else if(pathAction.equals("/getSOAPNotes")) {
      List<SOAPNote> notes =  patientService.getSOAPNotes(dto);
      dto.setSOAPNotes(notes);
    }
    else if(pathAction.equals("/getProgressNotes")) {
      List<ProgressNote> notes =  patientService.getProgressNotes(dto);
      dto.setProgressNotes(notes);
    }
    else if(pathAction.equals("/getEncounter")) {
      Encounter encounter =  patientService.getEncounter(dto);
      dto.setEncounter(encounter);
    }
    else if(pathAction.equals("/getPatientEncounters")) {
      List<Encounter> encounters =  patientService.getPatientEncounters(dto);
      dto.setPatientEncounters(encounters);
    }
    else if(pathAction.equals("/getPatientVitalSigns")) {
      List<VitalSigns> patientVitalSigns = patientService.getPatientVitalSigns(dto); 
      dto.setVitalSigns(patientVitalSigns);
    }
    else if(pathAction.equals("/overridePatient")) {
      patientService.overridePatient(dto);
    }
    else if(pathAction.equals("/acquirePatient")) {
      patientService.acquirePatient(dto);
    }
    else if(pathAction.equals("/getCurrentPatientEncounter")) {
      patientService.getCurrentPatientEncounter(dto);
    }
    else if(pathAction.equals("/deletePatient")) {
      patientService.deactivatePatient(dto);
    }
    else if(pathAction.equals("/deletePatientMedication")) {
      patientService.deletePatientMedication(dto);
    }
    else if(pathAction.equals("/deleteDxCode")) {
      patientService.deleteDxCode(dto);
    }
    else if(pathAction.equals("/deleteTxCode")) {
      patientService.deleteTxCode(dto);
    }
    else if(pathAction.equals("/updatePatient")) {
      patientService.updatePatient(dto);
    }
    else if(pathAction.equals("/updatePatientMedication")) {
      patientService.updatePatientMedication(dto);
    }
    else if(pathAction.equals("/updateEncounterQuestion")) {
      patientService.updateEncounterQuestion(dto);
    }
    else if(pathAction.equals("/updateDxCode")) {
      patientService.updateDxCode(dto);
    }
    else if(pathAction.equals("/updateTxCode")) {
      patientService.updateTxCode(dto);
    }
    else if(pathAction.equals("/createVitals")) {
      patientService.createVitals(dto);
    }
    else if(pathAction.equals("/createSOAPNote")) {
      patientService.createSOAPNote(dto);
    }
    else if(pathAction.equals("/createCC")) {
      patientService.createCC(dto);
    }
    else if(pathAction.equals("/createOBGYN")) {
      patientService.createOBGYN(dto);
    }
    else if(pathAction.equals("/createPFSH")) {
      patientService.createPFSH(dto);
    }
    else if(pathAction.equals("/createExam")) {
      patientService.createExam(dto);
    }
    else if(pathAction.equals("/createFollowUp")) {
      patientService.createFollowUp(dto);
    }
    else if(pathAction.equals("/createHist")) {
      patientService.createHist(dto);
    }
    else if(pathAction.equals("/createSupp")) {
      patientService.createSupp(dto);
    }
    else if(pathAction.equals("/addPatientMedication")) {
      Integer patientMedicationId = patientService.addPatientMedication(dto.getPatientId());
      dto.setPatientMedicationId(patientMedicationId);
    }
    else if(pathAction.equals("/addEncounterQuestion")) {
      Integer encounterQuestionId = patientService.addEncounterQuestion(dto.getEncounterId());
      dto.setEncounterQuestionId(encounterQuestionId);
    }
    else if(pathAction.equals("/addDxCode")) {
      Integer dxCodeId = patientService.addDxCode(dto.getEncounterId());
      dto.setDxCodeId(dxCodeId);
    }
    else if(pathAction.equals("/addTxCode")) {
      Integer txCodeId = patientService.addTxCode(dto.getEncounterId());
      dto.setTxCodeId(txCodeId);
    }
    else if(pathAction.equals("/newEncounter")) {
      Encounter encounter =  patientService.newEncounter(dto);
      dto.setEncounter(encounter);
    }
    else if(pathAction.equals("/newProgressNote")) {
      ProgressNote note =  patientService.newProgressNote(dto);
      dto.setProgressNote(note);
    }
    else if(pathAction.equals("/releasePatient")) {
      patientService.releasePatient(dto);
    }
    else if(pathAction.equals("/closeEncounter")) {
      patientService.closeEncounter(dto);
    }
    else if(pathAction.equals("/closeProgressNote")) {
      patientService.closeProgressNote(dto);
    }
    else if(pathAction.equals("/encryptPatients")) {
      patientService.encryptPatients(dto);
    }
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
  
  public String updateProgressNote(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    ProgressNote note =  patientService.updateProgressNote(dto);
    dto.setProgressNote(note);
    String json = gson.toJson(dto);
    return json;
  }
  
  public String uploadProfileImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
    return patientService.uploadProfileImage(request, response);
  }
}
 