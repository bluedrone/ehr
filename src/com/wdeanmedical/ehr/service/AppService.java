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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wdeanmedical.ehr.persistence.AppDAO;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.core.ExcludedFields;
import com.wdeanmedical.ehr.core.ExcludedObjects;
import com.wdeanmedical.ehr.core.Permissions;
import com.wdeanmedical.ehr.core.Statics;
import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.ClinicianDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.dto.TerminologyDTO;
import com.wdeanmedical.ehr.entity.Appointment;
import com.wdeanmedical.ehr.entity.BaseEntity;
import com.wdeanmedical.ehr.entity.CPT;
import com.wdeanmedical.ehr.entity.ClinicianSchedule;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.EncounterQuestion;
import com.wdeanmedical.ehr.entity.ICD10;
import com.wdeanmedical.ehr.entity.LabReview;
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
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.ToDoNote;
import com.wdeanmedical.ehr.util.ClinicianSessionData;
import com.wdeanmedical.ehr.util.DataEncryptor;
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
  private PatientService patientService;



  public AppService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    appDAO = (AppDAO) wac.getBean("appDAO");
    activityLogService = new ActivityLogService();
    patientService = new PatientService();
  }
  
  
  
  public String capitalize(String s) {
    return Character.toUpperCase(s.charAt(0)) + s.substring(1); 
  }
  
  
  
  public List<Patient> getFilteredPatients(PatientDTO dto) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Date dobFilter = null; 
    if (dto.getDobFilter() != null) {
      try { dobFilter = sdf.parse(dto.getDobFilter()); } catch (ParseException pe) {dobFilter = null;}
    }
    String firstNameFilter = dto.getFirstNameFilter(); 
    String middleNameFilter = dto.getMiddleNameFilter(); 
    String lastNameFilter = dto.getLastNameFilter(); 
    String cityFilter = dto.getCityFilter(); 
    String encryptedFirstNameFilter = firstNameFilter.length() > 0 ? DataEncryptor.encrypt(capitalize(firstNameFilter)) : firstNameFilter;
    String encryptedMiddleNameFilter = middleNameFilter.length() > 0 ? DataEncryptor.encrypt(capitalize(middleNameFilter)) : middleNameFilter;
    String encryptedLastNameFilter = lastNameFilter.length() > 0 ? DataEncryptor.encrypt(capitalize(lastNameFilter)) : lastNameFilter;
    String encryptedCityFilter = cityFilter.length() > 0 ? DataEncryptor.encrypt(capitalize(cityFilter)) : cityFilter;
    
    
    List<Patient> patients = appDAO.getFilteredPatients(
      encryptedFirstNameFilter, 
      encryptedMiddleNameFilter, 
      encryptedLastNameFilter,
      encryptedCityFilter,
      dto.getGenderFilter(),
      dobFilter
    );
    for (Patient p : patients) { 
      patientService.decrypt(p); 
      ExcludedFields.excludeFields(p);
      ExcludedObjects.excludeObjects(p);
    }
    return patients;
  }
  
  
  
  public  List<Patient> getPatients(PatientDTO dto) throws Exception {
    List<Patient> patients = appDAO.getPatients();
    for (Patient p : patients) { 
      patientService.decrypt(p); 
      ExcludedFields.excludeFields(p);
      ExcludedObjects.excludeObjects(p);
    }
    return patients;
  }
  
  
  
  public  List<Patient> getRecentPatients(PatientDTO dto) throws Exception {
    List<Patient> patients = appDAO.getRecentPatients(RECENT_PATIENT_SIZE);
    for (Patient p : patients) { 
      patientService.decrypt(p); 
      ExcludedFields.excludeFields(p);
      ExcludedObjects.excludeObjects(p);
    }
    return patients;
  }
  
  
  
  public  List<Patient> getRecentPatientsByClinician(PatientDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianById(dto.getClinicianId());
    List<Patient> patients = appDAO.getRecentPatientsByClinician(clinician, RECENT_PATIENT_SIZE);
    for (Patient p : patients) { 
      patientService.decrypt(p); 
      ExcludedFields.excludeFields(p);
      ExcludedObjects.excludeObjects(p);
    }
    return patients;
  }
  
  
  
  public  List<PatientAllergen> getPatientAllergens(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    List<PatientAllergen> items = appDAO.getPatientAllergens(patient);
    for (PatientAllergen item : items) {
      patientService.decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
    }
    return items;
  }
  
  
  
  public  List<PatientMedication> getPatientMedications(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    List<PatientMedication> items = appDAO.getPatientMedications(patient);
    for (PatientMedication item : items) {
      patientService.decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
      ExcludedFields.excludeFields(item.getPrescriber());
      ExcludedObjects.excludeObjects(item.getPrescriber());
    }
    return items;
  }
  
  
  
  public  List<PatientImmunization> getPatientImmunizations(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientImmunizations(patient);
  }
  
  
  
  public List<PatientHealthIssue> getPatientHealthIssues(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    List<PatientHealthIssue> items = appDAO.getPatientHealthIssues(patient);
    for (PatientHealthIssue item : items) {
      patientService.decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
    }
    return items;
  }
  
  
  
  public List<PatientMedicalTest> getiPatientMedicalTests(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    return appDAO.getPatientMedicalTests(patient);
  }
  
  
  
  public List<PatientMedicalTest> getPatientMedicalTests(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    List<PatientMedicalTest> items = appDAO.getPatientMedicalTests(patient);
    for (PatientMedicalTest item : items) {
      ExcludedFields.excludeFields(item.getClinician());
      ExcludedObjects.excludeObjects(item.getClinician());
    }
    return items;
  }
  
  
  
  public  List<PatientMedicalProcedure> getPatientMedicalProcedures(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    List<PatientMedicalProcedure> items = appDAO.getPatientMedicalProcedures(patient);
    for (PatientMedicalProcedure item : items) {
      patientService.decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
    }
    return items;
  }
  
  
  
  public List<Clinician> getClinicians(ClinicianDTO dto) throws Exception {
    List<Clinician> items = appDAO.getClinicians();
    for (Clinician item : items) {
      ExcludedFields.excludeFields(item);
      ExcludedObjects.excludeObjects(item);
    }
    return items;
  }
  
  
  
  public List<ICD10> searchICD10(TerminologyDTO dto) throws Exception {
    return appDAO.searchICD10(dto.getSearchText());
  }
  
  
  
  public List<CPT> searchCPT(TerminologyDTO dto) throws Exception {
    return appDAO.searchCPT(dto.getSearchText());
  }
  
  
  
  public  boolean getPatientSearchTypeAheads(ClinicianDTO dto) throws Exception {
    List<Patient> patients = appDAO.getPatients();
    Set<String> firstNames = new TreeSet<String>();
    Set<String> middleNames = new TreeSet<String>();
    Set<String> lastNames = new TreeSet<String>();
    Set<String> cities = new TreeSet<String>();
    
    for (Patient patient : patients) {
      patientService.decrypt(patient);
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

  
  
  public boolean getClinicianDashboard(ClinicianDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianById(dto.getId());
    
    List<PatientMessage> messages = appDAO.getPatientMessagesByClinician(clinician); 
    for (PatientMessage m : messages) { 
      patientService.decrypt(m.getPatient()); 
      ExcludedObjects.excludeObjects(m);
      ExcludedFields.excludeFields(m);
      ExcludedFields.excludeFields(m.getPatient());
      ExcludedObjects.excludeObjects(m.getPatient());
    }
    dto.dashboard.put("messages", messages);
    
    List<ProgressNote> progressNotes = appDAO.getProgressNotes(clinician); 
    for (ProgressNote note : progressNotes) { 
      patientService.decrypt(note.getPatient()); 
      ExcludedObjects.excludeObjects(note);
      ExcludedFields.excludeFields(note);
      ExcludedFields.excludeFields(note.getPatient());
      ExcludedObjects.excludeObjects(note.getPatient());
    }
    dto.dashboard.put("progressNotes", progressNotes);
    
    List<ToDoNote> toDoNotes = appDAO.getToDoNotes(clinician); 
    for (ToDoNote note : toDoNotes) { 
      patientService.decrypt(note.getPatient()); 
      ExcludedObjects.excludeObjects(note);
      ExcludedFields.excludeFields(note);
      ExcludedFields.excludeFields(note.getPatient());
      ExcludedObjects.excludeObjects(note.getPatient());
    }
    dto.dashboard.put("toDoNotes", toDoNotes);
    
    List<LabReview> labReviews = appDAO.getLabReview(clinician); 
    for (LabReview lr : labReviews) { patientService.decrypt(lr.getPatient()); }
    for (LabReview lr : labReviews) { 
      patientService.decrypt(lr.getPatient()); 
      ExcludedObjects.excludeObjects(lr);
      ExcludedFields.excludeFields(lr);
      ExcludedFields.excludeFields(lr.getPatient());
      ExcludedObjects.excludeObjects(lr.getPatient());
    }
    dto.dashboard.put("labReview", labReviews);
    
    List<ClinicianSchedule> schedule = appDAO.getClinicianSchedule(clinician); 
    for (ClinicianSchedule item : schedule) { 
      patientService.decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item);
      ExcludedObjects.excludeObjects(item);
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
    }
    dto.dashboard.put("clinicianSchedule", schedule);
    
    return true;
  }
  
  
  
  public  boolean getPatientChartSummary(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    Clinician clinician = appDAO.findClinicianById(dto.getClinicianId());
    
    List<Encounter> patientEncounters = appDAO.getEncountersByPatient(patient, clinician);
    for (Encounter item : patientEncounters) {
      patientService.decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
      ExcludedFields.excludeFields(item.getClinician());
      ExcludedObjects.excludeObjects(item.getClinician());
    }
    dto.patientChartSummary.put("patientEncounters", patientEncounters);
    dto.patientChartSummary.put("patientVitalSigns", appDAO.getPatientVitalSigns(patient.getId()));
    dto.patientChartSummary.put("patientHealthIssues", getPatientHealthIssues(dto));
    dto.patientChartSummary.put("patientAllergens", getPatientAllergens(dto));
    dto.patientChartSummary.put("patientMedications", getPatientMedications(dto));
    dto.patientChartSummary.put("patientMedicalProcedures", getPatientMedicalProcedures(dto));
    activityLogService.logViewPatient(dto.getId(), patient.getId(), dto.getClinicianId());
    return true;
  }
  
  
  
  public List<PatientLetter> getPatientLetters(PatientDTO dto) throws Exception {
    Patient patient = appDAO.findPatientById(dto.getId());
    List<PatientLetter> items =  appDAO.getPatientLetters(patient);
    for (PatientLetter item : items) {
      patientService.decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item);
      ExcludedObjects.excludeObjects(item);
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
    }
    return items;
  }
  
  
  
  public List<PatientMessage> getClinicianMessages(ClinicianDTO dto, Boolean fromClinician) throws Exception {
    Clinician clinician = appDAO.findClinicianById(dto.getId());
    List<PatientMessage> items = appDAO.getClinicianMessages(clinician, fromClinician);
    for (PatientMessage item : items) { 
      patientService.decrypt(item.getPatient()); 
      ExcludedObjects.excludeObjects(item);
      ExcludedFields.excludeFields(item);
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient()); 
    }
    return items;
  }
  
  
  
  public boolean getClinicianMessage(MessageDTO dto) throws Exception {
    PatientMessage patientMessage = appDAO.findClinicianMessageById(dto.getId());
    dto.setContent(patientMessage.getContent());
    dto.setPatient(patientMessage.getPatient());
    patientService.decrypt(patientMessage.getPatient()); 
    ExcludedObjects.excludeObjects(patientMessage);
    ExcludedFields.excludeFields(patientMessage);
    ExcludedFields.excludeFields(patientMessage.getPatient());
    ExcludedObjects.excludeObjects(patientMessage.getPatient()); 
    return true;
  }
  

  
  public  boolean getPatientChart(PatientDTO dto) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Patient patient = appDAO.findPatientById(dto.getId());
    patientService.decrypt(patient); 
    dto.setFirstName(patient.getCred().getFirstName());
    dto.setMiddleName(patient.getCred().getMiddleName());
    dto.setLastName(patient.getCred().getLastName());
    dto.setDob(sdf.format(patient.getDemo().getDob()));
    dto.setCity(patient.getDemo().getCity());
    dto.setStreetAddress1(patient.getDemo().getCity());
    dto.setUSState(patient.getDemo().getUsState().getCode());
    dto.setPostalCode(patient.getDemo().getPostalCode());
    dto.setGender(patient.getDemo().getGender().getCode());
    dto.setMrn(patient.getCred().getMrn());
    dto.setEmail(patient.getCred().getEmail());
    dto.setPrimaryPhone(patient.getDemo().getPrimaryPhone());
    dto.setSecondaryPhone(patient.getDemo().getSecondaryPhone());
    dto.setProfileImagePath(patient.getDemo().getProfileImagePath());
    patient.setLastAccessed(new Date());
    Encounter encounter = patientService.getCurrentEncounter(patient, dto);
    dto.setLastApptDate(sdf.format(encounter.getDate()));
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
      ExcludedFields.excludeFields(clinician);
      ExcludedObjects.excludeObjects(clinician);
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
  
  
  public Clinician updateSession(ClinicianSession dto, HttpServletRequest request) throws Exception{
    Clinician clinician = null;
    if (dto != null) {
      clinician = appDAO.findClinicianById(dto.getClinician().getId());
      ClinicianSession clinicianSession = appDAO.findClinicianSessionBySessionId(dto.getSessionId());
      if (clinicianSession != null) {
        clinicianSession.setLastAccessTime(new Date());
        ClinicianSessionData clinicianSessionData = new ClinicianSessionData();
        clinicianSessionData.setClinicianSession(clinicianSession);
       request.getSession().setAttribute(Statics.AUTHENTICATED_CLINICIAN, clinicianSession);
      }
    }
    return clinician;
  }
  
  
  
  public  boolean isValidSession(AuthorizedDTO dto, String ipAddress, String path) throws Exception {
    String clinicianName = "";
   
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
    if (Permissions.clinicianPermissionsMap.get(path) != null) {
      clinicianName = clinicianSession.getClinician().getUsername(); 
      logger.info("======= isValidSession() checking " + path + " for clinician " + clinicianName + " with a permissions level of " + accessLevel); 
      if (Permissions.clinicianPermissionsMap.get(path)[accessLevel] == false) {
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
    List<Appointment> items = appDAO.getAppointments(patient, isPast);
    for (Appointment item : items) { 
      patientService.decrypt(item.getPatient()); 
      ExcludedObjects.excludeObjects(item);
      ExcludedFields.excludeFields(item);
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient()); 
    }
    return items;
  }
  
  
  
  public boolean getAppointment(AppointmentDTO dto) throws Exception {
    Appointment item = appDAO.findAppointmentById(dto.getId());
    patientService.decrypt(item.getPatient()); 
    ExcludedObjects.excludeObjects(item);
    ExcludedFields.excludeFields(item);
    ExcludedFields.excludeFields(item.getPatient());
    ExcludedObjects.excludeObjects(item.getPatient()); 
    dto.setAppointment(item);
    return true;
  }
  


  public List<Appointment> getAllAppointments() throws Exception {
    List<Appointment> items = appDAO.getAllAppointments();
    for (Appointment item : items) { 
      patientService.decrypt(item.getPatient()); 
      ExcludedObjects.excludeObjects(item);
      ExcludedFields.excludeFields(item);
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient()); 
    }
    return items;
  }
  
  
  public Clinician getClinicianBySessionId(String sessionId) throws Exception {
    return appDAO.findClinicianBySessionId(sessionId);
  }
  
  
    
  public List<Appointment> getAllAppointmentsByClinician(Clinician clinician) throws Exception {
    List<Appointment> items = appDAO.getAllAppointmentsByClinician(clinician);
    for (Appointment item : items) { 
      patientService.decrypt(item.getPatient()); 
      ExcludedObjects.excludeObjects(item);
      ExcludedFields.excludeFields(item);
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient()); 
    }
    return items;
  }
  
  
  
  public String getStaticLists() throws Exception{
    Map<String,List> map = new HashMap<String,List>();
    Gson gson = new Gson();
    map.put("usStates", appDAO.getUSStates());
    return gson.toJson(map);
  }
  

}
