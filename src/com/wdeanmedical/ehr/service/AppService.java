/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wdeanmedical.ehr.persistence.AppDAO;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.ClinicianDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.dto.TerminologyDTO;
import com.wdeanmedical.ehr.entity.Appointment;
import com.wdeanmedical.ehr.entity.CPT;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.EncounterQuestion;
import com.wdeanmedical.ehr.entity.ICD10;
import com.wdeanmedical.ehr.entity.MedicalHistory;
import com.wdeanmedical.ehr.entity.PFSH;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientAllergen;
import com.wdeanmedical.ehr.entity.PatientClinician;
import com.wdeanmedical.ehr.entity.PatientHealthIssue;
import com.wdeanmedical.ehr.entity.PatientHealthTrendReport;
import com.wdeanmedical.ehr.entity.PatientImmunization;
import com.wdeanmedical.ehr.entity.PatientLetter;
import com.wdeanmedical.ehr.entity.PatientMedicalProcedure;
import com.wdeanmedical.ehr.entity.PatientMedicalTest;
import com.wdeanmedical.ehr.entity.PatientMedication;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.util.ClinicianSessionData;
import com.wdeanmedical.ehr.dto.MessageDTO;
import com.wdeanmedical.ehr.dto.AppointmentDTO;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppService {

  private static Log logger = LogFactory.getLog(AppService.class);
  private static int RECENT_PATIENT_SIZE = 5;
  
  private ServletContext context;
  private WebApplicationContext wac;
  private AppDAO appDAO;
  private ActivityLogService activityLogService;


  public AppService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    appDAO = (AppDAO) wac.getBean("appDAO");
    activityLogService = new ActivityLogService();
  }
  
  public  List<Patient> getFilteredPatients(PatientDTO dto) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Date dobFilter = null; 
    if (dto.getDobFilter() != null) {
      try { dobFilter = sdf.parse(dto.getDobFilter()); } catch (ParseException pe) {dobFilter = null;}
    }
    return appDAO.getFilteredPatients(
      dto.getFirstNameFilter(), 
      dto.getMiddleNameFilter(), 
      dto.getLastNameFilter(),
      dto.getCityFilter(),
      dto.getGenderFilter(),
      dobFilter
    );
  }
  
  public  List<Patient> getPatients(PatientDTO dto) throws Exception {
    return appDAO.getPatients();
  }
  
  public  List<Patient> getRecentPatients(PatientDTO dto) throws Exception {
    return appDAO.getRecentPatients(RECENT_PATIENT_SIZE);
  }
  
  public  List<Patient> getRecentPatientsByClinician(PatientDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianById(dto.getClinicianId());
    return appDAO.getRecentPatientsByClinician(clinician, RECENT_PATIENT_SIZE);
  }
  
  public  List<PatientAllergen> getPatientAllergens(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientAllergens(patient);
  }
  
  public  List<PatientMedication> getPatientMedications(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientMedications(patient);
  }
  
  public  List<PatientImmunization> getPatientImmunizations(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientImmunizations(patient);
  }
  
  public  List<PatientHealthIssue> getPatientHealthIssues(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientHealthIssues(patient);
  }
  
  public  List<PatientMedicalTest> getPatientMedicalTests(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientMedicalTests(patient);
  }
  
  public  List<PatientMedicalProcedure> getPatientMedicalProcedures(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientMedicalProcedures(patient);
  }
  
  public  List<PatientHealthTrendReport> getPatientHealthTrendReports(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientHealthTrendReports(patient);
  }
  
  public List<Clinician> getClinicians(ClinicianDTO dto) throws Exception {
    return appDAO.getClinicians();
  }
  
  public List<ICD10> searchICD10(TerminologyDTO dto) throws Exception {
    return appDAO.searchICD10(dto.getSearchText());
  }
  
  public List<CPT> searchCPT(TerminologyDTO dto) throws Exception {
    return appDAO.searchCPT(dto.getSearchText());
  }
  
  public  boolean getClinicianDashboard(ClinicianDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianById(dto.getId());
    dto.dashboard.put("messages", appDAO.getPatientMessagesByClinician(clinician));
    dto.dashboard.put("progressNotes", appDAO.getProgressNotes(clinician));
    dto.dashboard.put("toDoNotes", appDAO.getToDoNotes(clinician));
    dto.dashboard.put("labReview", appDAO.getLabReview(clinician));
    dto.dashboard.put("clinicianSchedule", appDAO.getClinicianSchedule(clinician));
    return true;
  }
  
  
  public  boolean getPatientSearchTypeAheads(ClinicianDTO dto) throws Exception {
    List<Patient> patients = appDAO.getPatients();
    Set<String> firstNames = new TreeSet<String>();
    Set<String> middleNames = new TreeSet<String>();
    Set<String> lastNames = new TreeSet<String>();
    Set<String> cities = new TreeSet<String>();
    
    for (Patient patient : patients) {
      firstNames.add(patient.getCred().getFirstName());
      if (patient.getCred().getMiddleName() != null) {
        middleNames.add(patient.getCred().getMiddleName());
      }
      lastNames.add(patient.getCred().getLastName());
      cities.add(patient.getDemo().getCity());
    }
    
    dto.patientSearchTypeAheads.put("firstNames", firstNames);
    dto.patientSearchTypeAheads.put("middleNames", middleNames);
    dto.patientSearchTypeAheads.put("lastNames", lastNames);
    dto.patientSearchTypeAheads.put("cities", cities);
    return true;
  }
  
  
  public  boolean getPatientChartSummary(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    Clinician clinician = appDAO.findClinicianById(dto.getClinicianId());
    dto.patientChartSummary.put("patientEncounters", appDAO.getEncountersByPatient(patient, clinician));
    dto.patientChartSummary.put("patientVitalSigns", appDAO.getPatientVitalSigns(patient));
    dto.patientChartSummary.put("patientHealthIssues", appDAO.getPatientHealthIssues(patient));
    dto.patientChartSummary.put("patientAllergens", appDAO.getPatientAllergens(patient));
    dto.patientChartSummary.put("patientMedications", appDAO.getPatientMedications(patient));
    dto.patientChartSummary.put("patientMedicalProcedures", appDAO.getPatientMedicalProcedures(patient));
    activityLogService.logViewPatient(dto.getId(), patient.getId(), dto.getClinicianId());
    return true;
  }
  
  public  List<PatientLetter> getPatientLetters(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientLetters(patient);
  }
  
  public  List<PatientMessage> getClinicianMessages(ClinicianDTO dto, Boolean fromClinician) throws Exception {
    Clinician clinician = appDAO.findClinicianById(dto.getId());
    return appDAO.getClinicianMessages(clinician, fromClinician);
  }
  
  public boolean getClinicianMessage(MessageDTO dto) throws Exception {
    PatientMessage patientMessage = appDAO.findClinicianMessageById(dto.getId());
    dto.setContent(patientMessage.getContent());
    dto.setPatient(patientMessage.getPatient());
    return true;
  }
  

  
  public  boolean processMessage(PatientDTO dto) throws Exception {
    return true;
  }
  
  public  boolean getPatientChart(PatientDTO dto) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Patient patient = appDAO.findPatientById(dto.getId());
    dto.setFirstName(patient.getCred().getFirstName());
    dto.setMiddleName(patient.getCred().getMiddleName());
    dto.setLastName(patient.getCred().getLastName());
    dto.setDob(sdf.format(patient.getDemo().getDob()));
    dto.setGender(patient.getDemo().getGender().getCode());
    dto.setMrn(patient.getCred().getMrn());
    dto.setPrimaryPhone(patient.getDemo().getPrimaryPhone());
    dto.setSecondaryPhone(patient.getDemo().getSecondaryPhone());
    dto.setProfileImagePath(patient.getDemo().getProfileImagePath());
    patient.setLastAccessed(new Date());
    appDAO.update(patient);
    activityLogService.logViewPatient(dto.getId(), patient.getId(), dto.getClinicianId());
    return true;
  }
  
  public  void logout(AuthorizedDTO dto) throws Exception {
    ClinicianSession clinicianSession = appDAO.findClinicianSessionBySessionId(dto.getSessionId());
    String clinicianName = clinicianSession.getClinician().getUsername(); 
    logger.info("======= logout() of clinician: " + clinicianName); 
    appDAO.unparkClinicianSession(dto.getSessionId());
    appDAO.deleteClinicianSession(dto.getSessionId());
    Integer userId = clinicianSession.getClinician().getId();
    activityLogService.logLogout(userId);
  }
  
  public  void park(AuthorizedDTO dto) throws Exception {
    appDAO.parkClinicianSession(dto.getSessionId());
  }
  
  public  void unpark(AuthorizedDTO dto) throws Exception {
    appDAO.unparkClinicianSession(dto.getSessionId());
  }
  
  public  List<PatientClinician> getPatientClinicians(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientClinicians(patient);
  }
  
  
  public Clinician login(LoginDTO loginDTO, String ipAddress) throws Exception {
    Clinician clinician = appDAO.authenticateClinician(loginDTO.getUsername(), loginDTO.getPassword());
    if (clinician.getAuthStatus() == Clinician.STATUS_AUTHORIZED) {
      ClinicianSession clinicianSession = new ClinicianSession();
      clinicianSession.setClinician(clinician);
      clinicianSession.setSessionId(clinician.getSessionId());
      clinicianSession.setIpAddress(ipAddress);
      clinicianSession.setLastAccessTime(new Date());
      clinicianSession.setParked(false);
      appDAO.create(clinicianSession);
      ClinicianSessionData clinicianSessionData = new ClinicianSessionData();
      clinicianSessionData.setClinicianSession(clinicianSession);
      logger.info("======= Added " + clinicianSession.toString()); 
      activityLogService.logLogin(clinician.getId());
    }
    return clinician;
  }
  
  
  
  public void getFile(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, 
                      String filePath, String fileName) throws Exception {
    String mime = servletContext.getMimeType(fileName);
    if (mime == null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }
    response.setContentType(mime);  
    File file = new File(filePath + fileName);
    response.setContentLength((int) file.length());
    FileInputStream in = new FileInputStream(file);
    OutputStream out = response.getOutputStream();
    byte[] buf = new byte[1024];
    int count = 0;
    while ((count = in.read(buf)) >= 0) {
      out.write(buf, 0, count);
    }
    out.close();
    in.close();
  }
  
  
  
  public  boolean isValidSession(AuthorizedDTO dto, String ipAddress, String path) throws Exception {
    String clinicianName = "";
   
    if(path.substring(1).split("/").length > 1) {
      path = path.substring(1).split("/")[1];
    }          
    
    appDAO.deleteExpiredClinicianSessions();
    
    if (dto == null || dto.getSessionId() == null) {
      logger.info("======= isValidSession() no session id submitted by clinician at ip address of " + ipAddress); 
      return false;
    }
    
    ClinicianSession clinicianSession = appDAO.findClinicianSessionBySessionId(dto.getSessionId());
    
    if (clinicianSession == null) {
      logger.info("======= isValidSession() no session found for : " + dto.getSessionId()); 
      return false;
    }
    
    
    if (clinicianSession.getIpAddress().equals(ipAddress) == false) {
      logger.info("======= isValidSession() submitted IP address is of " + ipAddress + " does not match the one found in current session"); 
      return false;
    }
    
     // check for proper access level
    int accessLevel = clinicianSession.getClinician().getRole().getId();
    logger.info("======= isValidSession() checking " + path); 
    if (Core.clinicianPermissionsMap.get(path) != null) {
      clinicianName = clinicianSession.getClinician().getUsername(); 
      logger.info("======= isValidSession() checking " + path + " for clinician " + clinicianName + " with a permissions level of " + accessLevel); 
      if (Core.clinicianPermissionsMap.get(path)[accessLevel] == false) {
        logger.info("======= isValidSession() clinician " + clinicianName + " lacks permission level to execute " + path); 
        return false;
      }
    }
    
    // update session timestamp to current time 
    clinicianSession.setLastAccessTime(new Date());
    appDAO.update(clinicianSession);
    logger.info("======= isValidSession() clinician " + clinicianName + "'s timestamp updated to " + clinicianSession.getLastAccessTime()); 
    
    return true;
  }
  
  
  
  public  List<Appointment> getAppointments(PatientDTO dto, boolean isPast) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getAppointments(patient, isPast);
  }
  
  
  
  public boolean getAppointment(AppointmentDTO dto) throws Exception {
    Appointment appointment = appDAO.findAppointmentById(dto.getId());
    dto.setAppointment(appointment);
    return true;
  }
  


  public List<Appointment> getAllAppointments() throws Exception {
    return appDAO.getAllAppointments();
  }
  

}
