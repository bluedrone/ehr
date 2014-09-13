/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wdeanmedical.ehr.persistence.AppDAO;
import com.wdeanmedical.ehr.persistence.PatientDAO;
import com.wdeanmedical.ehr.util.DataEncryptor;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.core.ExcludedFields;
import com.wdeanmedical.ehr.core.ExcludedObjects;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.ChiefComplaint;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Exam;
import com.wdeanmedical.ehr.entity.PatientHistoryMedication;
import com.wdeanmedical.ehr.entity.EncounterQuestion;
import com.wdeanmedical.ehr.entity.Lab;
import com.wdeanmedical.ehr.entity.OBGYNEncounterData;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.EncounterType;
import com.wdeanmedical.ehr.entity.PatientFollowUp;
import com.wdeanmedical.ehr.entity.MedicalHistory;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.PFSH;
import com.wdeanmedical.ehr.entity.PatientStatus;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.SOAPNote;
import com.wdeanmedical.ehr.entity.SuppQuestions;
import com.wdeanmedical.ehr.entity.VitalSigns;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PatientService {

  private static Log log = LogFactory.getLog(AppService.class);
  
  private ServletContext context;
  private WebApplicationContext wac;
  private PatientDAO patientDAO;
  private AppDAO appDAO;
  private ActivityLogService activityLogService;



  public PatientService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    patientDAO = (PatientDAO) wac.getBean("patientDAO");
    appDAO = (AppDAO) wac.getBean("appDAO");
    activityLogService = new ActivityLogService();
  }
  
  
  
  public List<Encounter> getPatientEncounters(PatientDTO dto) throws Exception {
    List<Encounter> items = patientDAO.findEncountersByPatient(dto.getPatientId());
    for (Encounter item : items) {
      decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedFields.excludeFields(item.getClinician());
      ExcludedObjects.excludeObjects(item.getClinician());
    }
    return items;
  }
  
  
  
  public List<ProgressNote> getProgressNotes(PatientDTO dto) throws Exception {
    Patient patient = patientDAO.findPatientById(dto.getPatientId());
    List<ProgressNote> items = patientDAO.findProgressNotesByPatient(patient);
    for (ProgressNote item : items) {
      decrypt(item.getPatient()); 
      ExcludedFields.excludeFields(item.getPatient());
      ExcludedObjects.excludeObjects(item.getPatient());
      ExcludedFields.excludeFields(item.getClinician());
      ExcludedObjects.excludeObjects(item.getClinician());
    }
    return items;
  }
  
  
  
  public List<ChiefComplaint> getChiefComplaints(PatientDTO dto) throws Exception {
    return patientDAO.findChiefComplaintsByPatientId(dto.getPatientId());
  }
  
  
  
  public List<SOAPNote> getSOAPNotes(PatientDTO dto) throws Exception {
    return patientDAO.findSOAPNotesByPatientId(dto.getPatientId());
  }
  
  
  
  public List<VitalSigns> getPatientVitalSigns(PatientDTO dto) throws Exception {
    List<VitalSigns> vitals = appDAO.getPatientVitalSigns(dto.getPatientId());
    return vitals;
  }
  
  
  
  public void deletePatientMedication(PatientDTO dto) throws Exception {
    PatientHistoryMedication medication = patientDAO.findEncounterMedicationById(dto.getEncounterMedicationId());
    patientDAO.delete(medication);
  }
  
  
  
  public void deactivatePatient(PatientDTO dto) throws Exception {
    Patient patient = patientDAO.findPatientById(dto.getPatientId());
    PatientStatus status = patientDAO.findPatientStatusById(PatientStatus.INACTIVE);
    patient.getCred().setStatus(status);
    patientDAO.update(patient.getCred());
    Encounter encounter = getCurrentEncounter(patient, dto);
    patientDAO.delete(encounter);
    activityLogService.logDeletePatient(dto.getClinicianId(), patient.getId(), dto.getClinicianId(), encounter.getId()); 
  }
  
  
  
  public void createVitals(PatientDTO dto) throws Exception {
  Set<String> fieldSetVitals = activityLogService.getListOfChangedFields(dto.getEncounter().getVitals());
    patientDAO.update(dto.getEncounter().getVitals());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetVitals); 
  }
  
  
  
  public void createSOAPNote(PatientDTO dto) throws Exception {
    Set<String> fieldSetSOAPNote = activityLogService.getListOfChangedFields(dto.getEncounter().getSOAPNote());
    patientDAO.update(dto.getEncounter().getSOAPNote());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetSOAPNote); 
  }
  
  
  
  public void createCC(PatientDTO dto) throws Exception {
    Set<String> fieldSetCc = activityLogService.getListOfChangedFields(dto.getEncounter().getCc());
    patientDAO.update(dto.getEncounter().getCc());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetCc); 
  }
  
  
  
  public void createOBGYN(PatientDTO dto) throws Exception {
  Set<String> fieldSetObgyn = activityLogService.getListOfChangedFields(dto.getEncounter().getObgyn());
    patientDAO.update(dto.getEncounter().getObgyn());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetObgyn); 
  }
  
  
  
  public void createPFSH(PatientDTO dto) throws Exception {
  Set<String> fieldSetPfsh = activityLogService.getListOfChangedFields(dto.getEncounter().getPatient().getPfsh());
    patientDAO.update(dto.getEncounter().getPatient().getPfsh());
    patientDAO.update(dto.getEncounter().getPatient());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetPfsh); 
  }
  
  
  
  public void createSupp(PatientDTO dto) throws Exception {
    for (EncounterQuestion encounterQuestion : dto.getEncounter().getSupp().getEncounterQuestionList()) {
      patientDAO.updateEncounterQuestion(encounterQuestion);
    }
    Set<String> fieldSetSupp = activityLogService.getListOfChangedFields(dto.getEncounter().getSupp());
    patientDAO.update(dto.getEncounter().getSupp());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetSupp); 
  }
  
  
  
  public void createExam(PatientDTO dto) throws Exception {
    Set<String> fieldSetExam = activityLogService.getListOfChangedFields(dto.getEncounter().getExam());
    patientDAO.update(dto.getEncounter().getExam());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetExam); 
  }
  
  
  
  public void createFollowUp(PatientDTO dto) throws Exception {
    Set<String> fieldSetFollowUp = activityLogService.getListOfChangedFields(dto.getEncounter().getFollowUp());
    patientDAO.update(dto.getEncounter().getFollowUp());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetFollowUp);
  }
  
  
  
  public void createHist(PatientDTO dto) throws Exception {
    for (PatientHistoryMedication patientHistoryMedication : dto.getEncounter().getPatient().getHist().getPatientMedicationList()) {
      patientDAO.updateEncounterMedication(patientHistoryMedication);
    }
    Set<String> fieldSetHist = activityLogService.getListOfChangedFields(dto.getEncounter().getPatient().getHist());
    patientDAO.update(dto.getEncounter().getPatient().getHist());
    patientDAO.update(dto.getEncounter().getPatient());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetHist);
  }
 
  
  
  public  void updateEncounterMedication(PatientDTO dto) throws Exception {
    PatientHistoryMedication patientHistoryMedication = patientDAO.findEncounterMedicationById(dto.getEncounterMedicationId());
    String property = dto.getUpdateProperty();
    String value = dto.getUpdatePropertyValue();
    if (property.equals("medication")) {
      patientHistoryMedication.setMedication(value);
    }
    else if (property.equals("dose")) {
      patientHistoryMedication.setDose(value);
    }
    else if (property.equals("frequency")) {
      patientHistoryMedication.setFrequency(value);
    }
    Set<String> fieldSet = new HashSet<String>();
    fieldSet.add(property);
    patientDAO.update(patientHistoryMedication);
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSet);
  }
  
  
  
  public void updateEncounterQuestion(PatientDTO dto) throws Exception {
    EncounterQuestion encounterQuestion = patientDAO.findEncounterQuestionById(dto.getEncounterQuestionId());
    String property = dto.getUpdateProperty();
    String value = dto.getUpdatePropertyValue();
    if (property.equals("question")) {
      encounterQuestion.setQuestion(value);
    }
    else if (property.equals("response")) {
      encounterQuestion.setResponse(value);
    }
    Set<String> fieldSet = new HashSet<String>();
    fieldSet.add(property);
    patientDAO.update(encounterQuestion);
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSet);
  }
  
  
  
  public void getCurrentPatientEncounter(PatientDTO dto) throws Exception {
    Encounter encounter = patientDAO.findCurrentEncounterByPatientId(dto.getPatientId());
    decrypt(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getPatient());
    ExcludedObjects.excludeObjects(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getClinician());
    ExcludedObjects.excludeObjects(encounter.getClinician());
    dto.setEncounter(encounter);
  }
  
  
 
  public Integer addEncounterMedication(Integer patientId) throws Exception {
    PatientHistoryMedication patientHistoryMedication = new PatientHistoryMedication();
    patientHistoryMedication.setPatientId(patientId);
    patientDAO.create(patientHistoryMedication);
    return patientHistoryMedication.getId();
  }
  
  
  
  public Integer addEncounterQuestion(Integer encounterId) throws Exception {
    EncounterQuestion encounterQuestion = new EncounterQuestion();
    encounterQuestion.setEncounterId(encounterId);
    patientDAO.create(encounterQuestion);
    return encounterQuestion.getId();
  }
  
  
  
  public void acquirePatient(PatientDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
    Encounter encounter = patientDAO.findEncounterById(dto.getEncounterId());
    Clinician encounterClinician = encounter.getClinician();
    if (encounterClinician == null) {
      encounter.setClinician(clinician);
      encounter.setLockStatus(Encounter.LOCK_LOCKED);
      dto.setLockStatus(Encounter.LOCK_LOCKED);
      Set<String> fieldSet = activityLogService.getListOfChangedFields(encounter); 
      patientDAO.update(encounter);
      activityLogService.logEditEncounter(clinician.getId(), dto.getId(), clinician.getId(), encounter.getId(), fieldSet); 
      dto.setClinicianId(clinician.getId());
    }
  }
  
  
  
  public void overridePatient(PatientDTO dto) throws Exception {
    Encounter encounter = patientDAO.findEncounterById(dto.getEncounterId());
    Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
    encounter.setLockStatus(Encounter.LOCK_OVERRIDDEN);
    dto.setLockStatus(Encounter.LOCK_OVERRIDDEN);
    Set<String> fieldSet = activityLogService.getListOfChangedFields(encounter); 
    patientDAO.update(encounter);
    activityLogService.logEditEncounter(clinician.getId(), dto.getId(), clinician.getId(), encounter.getId(), fieldSet); 
    dto.setClinicianId(clinician.getId());
  }
  
  
  
  public void releasePatient(PatientDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
    Encounter encounter = patientDAO.findEncounterById(dto.getEncounterId());
    Clinician encounterClinician = encounter.getClinician();
    if (encounterClinician != null && encounterClinician.getId().equals(clinician.getId())) {
      encounter.setClinician(null);
      encounter.setLockStatus(Encounter.LOCK_FREE);
      dto.setLockStatus(Encounter.LOCK_FREE);
      Set<String> fieldSet = activityLogService.getListOfChangedFields(encounter);
      patientDAO.update(encounter);
      activityLogService.logEditEncounter(clinician.getId(), dto.getId(), clinician.getId(), encounter.getId(), fieldSet);  
    }
  }
  
  
  
  public void closeEncounter(PatientDTO dto) throws Exception {
    Encounter encounter = patientDAO.findEncounterById(dto.getEncounterId());
    encounter.setCompleted(true);
    Set<String> fieldSet = activityLogService.getListOfChangedFields(encounter);
    patientDAO.update(encounter);
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSet); 
  }
  
  
  
  public void closeProgressNote(PatientDTO dto) throws Exception {
    ProgressNote note = patientDAO.findProgressNoteById(dto.getProgressNoteId());
    note.setCompleted(true);
    Set<String> fieldSet = activityLogService.getListOfChangedFields(note); 
    patientDAO.update(note);
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSet); 

  }
  
  
  
  public Encounter newEncounter(PatientDTO dto) throws Exception {
    Patient patient = patientDAO.findPatientById(dto.getPatientId());
    encrypt(patient);
    Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
    Encounter encounter = patientDAO.createEncounter(patient, clinician);
    for (int i=0; i<3; i++) {
      addEncounterQuestion(encounter.getId()); // encounter.supp
      addEncounterMedication(patient.getId()); // patient.hist
    }
    activityLogService.logNewEncounter(clinician.getId(), dto.getPatientId(), clinician.getId(), encounter.getId());
    decrypt(encounter.getPatient()); 
    ExcludedFields.excludeFields(encounter.getPatient());
    ExcludedObjects.excludeObjects(encounter.getPatient());
    return encounter;
  }
  
  
  
  public ProgressNote newProgressNote(PatientDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
    Patient patient = appDAO.findPatientById(dto.getPatientId());
    ProgressNote note = patientDAO.createProgressNote(patient, clinician);
    return note;
  }
  
  
  
  public ProgressNote updateProgressNote(PatientDTO dto) throws Exception {
    ProgressNote note = patientDAO.findProgressNoteById(dto.getProgressNote().getId());
    note.setSubject(dto.getProgressNote().getSubject());
    note.setContent(dto.getProgressNote().getContent());
    Set<String> fieldSetNote = activityLogService.getListOfChangedFields(note);
    patientDAO.update(note);
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetNote);
    ExcludedFields.excludeFields(note.getPatient());
    ExcludedObjects.excludeObjects(note.getPatient());
    ExcludedFields.excludeFields(note.getClinician());
    ExcludedObjects.excludeObjects(note.getClinician());
    return note;
  }
  
  
  
  public Encounter getEncounter(PatientDTO dto) throws Exception {
    Encounter encounter = patientDAO.findEncounterById(dto.getEncounterId());
    decrypt(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getClinician());
    ExcludedObjects.excludeObjects(encounter.getClinician());
    return encounter;
  }
  
  
  
  public Encounter getEncounter(int id) throws Exception {
    Encounter encounter = patientDAO.findEncounterById(id);
    decrypt(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getPatient());
    ExcludedObjects.excludeObjects(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getClinician());
    ExcludedObjects.excludeObjects(encounter.getClinician());
    return encounter;
  }
  
  
  
  public Patient getPatient(int id) throws Exception {
    Patient patient = patientDAO.findPatientById(id);
    decrypt(patient);
    ExcludedFields.excludeFields(patient);
    ExcludedObjects.excludeObjects(patient); 
    return patient;
  }
  
  
  
  public Encounter getCurrentEncounter(Patient patient, PatientDTO dto) throws Exception {
    Encounter encounter = patientDAO.findCurrentEncounterByPatient(patient);
    Clinician clinician = null;
    if (encounter == null) {
      encounter = new Encounter();
      encounter.setPatient(patient);
      clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
      encounter.setClinician(clinician);
      encounter.setDate(new Date());
      encounter.setEncounterType(patientDAO.findEncounterTypeById(EncounterType.CHECK_UP));
      patientDAO.create(encounter); 
    }
    else{
      clinician = encounter.getClinician();
    }    
    activityLogService.logViewEncounter(clinician.getId(), patient.getId(), clinician.getId(), encounter.getId());
    decrypt(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getPatient());
    ExcludedObjects.excludeObjects(encounter.getPatient());
    ExcludedFields.excludeFields(encounter.getClinician());
    ExcludedObjects.excludeObjects(encounter.getClinician());
    return encounter;
  }
  
  
  
  public PFSH getPatientPFSH(Patient patient, PatientDTO dto) throws Exception {
    PFSH pfsh = null;
     if (patient.getPfsh() == null) {
      pfsh = new PFSH();
      pfsh.setPatientId(patient.getId());
      Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
      pfsh.setClinicianId(clinician.getId());
      pfsh.setDate(new Date());
      patientDAO.create(pfsh); 
    }
    else {
      pfsh = patient.getPfsh();
    }
    return pfsh;
  }
  
  
      
  public String uploadProfileImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String patientId = request.getParameter("patientId");
    String sessionId = request.getParameter("sessionId");  
    InputStream is = null;
    FileOutputStream fos = null;
    String returnString = "";
    
    is = request.getInputStream();
    String filename = request.getHeader("X-File-Name");
    fos = new FileOutputStream(new File(Core.appBaseDir + Core.imagesDir + "/" + filename));
    IOUtils.copy(is, fos);
    response.setStatus(HttpServletResponse.SC_OK);
    fos.close();
    is.close();
   
    String[] imageMagickArgs = {
      Core.imageMagickHome + "convert", 
      Core.appBaseDir + Core.imagesDir + "/" + filename, 
      "-resize", 
      "160x160", 
      Core.appBaseDir + Core.imagesDir + "/" + filename
    };
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(imageMagickArgs);
    
    InputStream pis = process.getInputStream();
    InputStreamReader isr = new InputStreamReader(pis);
    BufferedReader br = new BufferedReader(isr);
    String line;
    log.info("Output of running "+ Arrays.toString(imageMagickArgs) + "is: ");

    while ((line = br.readLine()) != null) {
      log.info(line);
    }
    log.info("\n" + filename + " uploaded");
    Patient patient = patientDAO.findPatientById(new Integer(patientId));
       returnString = "{\"filename\":\""+filename+"\", \"patientId\":\""+patient.getId()+"\"}";
    String patientDirPath =  Core.appBaseDir + Core.patientDirPath + "/" + patient.getId() + "/";
    log.info("Moving to " + patientDirPath);
    //String[] mvArgs = {"mv", Core.appBaseDir + Core.imagesDir + "/" + filename,  patientDirPath};
    //runtime.exec(mvArgs);
    if(filename != null){
      
      String profileImageTempPath = Core.appBaseDir + Core.imagesDir + "/" + filename;
      
      String filesHomePatientDirPath =  Core.filesHome  + Core.patientDirPath + "/" + patient.getId() + "/";
           
      new File(filesHomePatientDirPath).mkdirs(); 
      
      File profileImageTempPathFile =new File(profileImageTempPath);
      profileImageTempPathFile.renameTo(new File(filesHomePatientDirPath + "/" + filename));     
          
    } 
    Set<String> fieldSet = activityLogService.getListOfChangedFields(patient);
    patientDAO.updatePatientProfileImage(patient, filename); 
    String username = null;
    Integer clinicianId = null;
    if(sessionId != null){
      ClinicianSession clinicianSession = appDAO.findClinicianSessionBySessionId(sessionId);
      username = clinicianSession.getClinician().getUsername();   
      clinicianId = clinicianSession.getClinician().getId();
    }
    activityLogService.logEditPatient(clinicianId, patient.getId(), clinicianId, patient.getCurrentEncounterId(), fieldSet); 
    return returnString;
 }
 
 
 
  public void updatePatient(PatientDTO dto) throws Exception {
    Set<String> fieldSet = new HashSet<String>();
    String updateClass = "";
    Patient patient = patientDAO.findPatientById(dto.getPatientId());
    Encounter encounter = getCurrentEncounter(patient, dto);
    PFSH pfsh = patient.getPfsh();
    VitalSigns vitals = encounter.getVitals();
    Exam exam = encounter.getExam();
    Lab lab = encounter.getLab();
    ChiefComplaint cc = encounter.getCc();
    OBGYNEncounterData obgyn = encounter.getObgyn();
    SuppQuestions supp = encounter.getSupp();
    MedicalHistory hist = patient.getHist();
    PatientFollowUp followUp = encounter.getFollowUp();
    Credentials cred = patient.getCred();
    Demographics demo = patient.getDemo();
    SOAPNote soapNote = encounter.getSOAPNote();
    
    String property = dto.getUpdateProperty();
    String value = dto.getUpdatePropertyValue();
    if (property.equals("firstName")) {cred.setFirstName(DataEncryptor.encrypt(value));updateClass = "Credentials";} 
    else if (property.equals("middleName")) {cred.setMiddleName(DataEncryptor.encrypt(value));updateClass = "Credentials";} 
    else if (property.equals("lastName")) {cred.setLastName(DataEncryptor.encrypt(value));updateClass = "Credentials";} 
    else if (property.equals("gender")) {demo.setGender(patientDAO.findGenderByCode(value));updateClass = "Demographics";} 
    else if (property.equals("race")) {
      Integer raceId; try { raceId = new Integer(value); } catch (NumberFormatException nfe) {raceId = null;}
      demo.setRace(patientDAO.findRaceById(raceId));updateClass = "Demographics";
    } 
    else if (property.equals("ethnicity")) {
      Integer ethnicityId; try { ethnicityId = new Integer(value); } catch (NumberFormatException nfe) {ethnicityId = null;}
      demo.setEthnicity(patientDAO.findEthnicityById(ethnicityId));updateClass = "Demographics";
    } 
    else if (property.equals("usState")) {
      Integer usStateId; try { usStateId = new Integer(value); } catch (NumberFormatException nfe) {usStateId = null;}
      demo.setUsState(patientDAO.findUSStateById(usStateId));updateClass = "Demographics";
    } 
    else if (property.equals("notes")) {encounter.setNotes(value);updateClass = "Encounter";} 
    else if (property.equals("govtId")) {cred.setGovtId(DataEncryptor.encrypt(value));updateClass = "Credentials";} 
    else if (property.equals("mrn")) {cred.setMrn(DataEncryptor.encrypt(value));updateClass = "Credentials";} 
    else if (property.equals("streetAddress1")) {demo.setStreetAddress1(DataEncryptor.encrypt(value));updateClass = "Demographics";} 
    else if (property.equals("city")) {demo.setCity(DataEncryptor.encrypt(value));updateClass = "Demographics";} 
    else if (property.equals("postalCode")) {demo.setPostalCode(DataEncryptor.encrypt(value));updateClass = "Demographics";} 
    else if (property.equals("primaryPhone")) {demo.setPrimaryPhone(DataEncryptor.encrypt(value));updateClass = "Demographics";} 
    else if (property.equals("secondaryPhone")) {demo.setSecondaryPhone(DataEncryptor.encrypt(value));updateClass = "Demographics";} 
    else if (property.equals("email")) {cred.setEmail(DataEncryptor.encrypt(value));updateClass = "Credentials";} 
    else if (property.equals("occupation")) {demo.setOccupation(value);updateClass = "Demographics";} 
    else if (property.equals("employmentStatus")) {demo.setEmploymentStatus(value);updateClass = "Demographics";} 
    else if (property.equals("schoolStatus")) {demo.setSchoolStatus(value);updateClass = "Demographics";} 
    else if (property.equals("employer")) {demo.setEmployer(DataEncryptor.encrypt(value));updateClass = "Demographics";} 
    else if (property.equals("schoolName")) {demo.setSchoolName(DataEncryptor.encrypt(value));updateClass = "Demographics";} 
    
    else if (property.equals("subjective")) {soapNote.setSubjective(value);updateClass = "SOAPNote";} 
    else if (property.equals("objective")) {soapNote.setObjective(value);updateClass = "SOAPNote";} 
    else if (property.equals("assessment")) {soapNote.setAssessment(value);updateClass = "SOAPNote";} 
    else if (property.equals("plan")) {soapNote.setPlan(value);updateClass = "SOAPNote";} 
    
    else if (property.equals("dob")) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      Date dob; try { dob = sdf.parse(value); } catch (ParseException pe) {dob = null;}
      demo.setDob(dob);
      updateClass = "Demographics";
    }
    else if (property.equals("height")) {
      Float height; try { height = new Float(value); } catch (NumberFormatException nfe) {height = null;}
      vitals.setHeight(height);
      updateClass = "Vitals";
    } 
    else if (property.equals("weight")) {
      Float weight; try { weight = new Float(value); } catch (NumberFormatException nfe) {weight = null;}
      vitals.setWeight(weight);
      updateClass = "Vitals";
    } 
    else if (property.equals("systolic")) {
      Integer sys; try { sys = new Integer(value); } catch (NumberFormatException nfe) {sys = null;}
      vitals.setSystolic(sys);
      updateClass = "Vitals";
    } 
    else if (property.equals("diastolic")) {
      Integer dia; try { dia = new Integer(value); } catch (NumberFormatException nfe) {dia = null;}
      vitals.setDiastolic(dia);
      updateClass = "Vitals";
    } 
    else if (property.equals("pulse")) {
      Integer hr; try { hr = new Integer(value); } catch (NumberFormatException nfe) {hr = null;}
      vitals.setPulse(hr);
      updateClass = "Vitals";
    } 
    else if (property.equals("respiration")) {
      Integer rr; try { rr = new Integer(value); } catch (NumberFormatException nfe) {rr = null;}
      vitals.setRespiration(rr);
      updateClass = "Vitals";
    } 
    else if (property.equals("temp")) {
      Float temp; try { temp = new Float(value); } catch (NumberFormatException nfe) {temp = null;}
      vitals.setTemperature(temp);
      updateClass = "Vitals";
    } 
    else if (property.equals("motherName")) {pfsh.setMotherName(value);updateClass = "PFSH";} 
    else if (property.equals("caretakerName")) {pfsh.setCaretakerName(value);updateClass = "PFSH";} 
    else if (property.equals("patientRelationship")) {pfsh.setCaretakerRelationship(value);updateClass = "PFSH";} 
    else if (property.equals("motherDob")) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      Date motherDob; try { motherDob = sdf.parse(value); } catch (ParseException pe) {motherDob = null;}
      pfsh.setMotherDOB(motherDob);
      updateClass = "PFSH";
    }
    else if (property.equals("description")) {cc.setDescription(value);updateClass = "ChiefComplaint";} 
    else if (property.equals("specificLocation")) {cc.setSpecificLocation(value);updateClass = "ChiefComplaint";} 
    else if (property.equals("occursWhen")) {cc.setOccursWhen(value);updateClass = "ChiefComplaint";} 
    else if (property.equals("hoursSince")) {
      Integer hoursSince; try { hoursSince = new Integer(value); } catch (NumberFormatException nfe) {hoursSince = null;}
      cc.setHoursSince(hoursSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("daysSince")) {
      Integer daysSince; try { daysSince = new Integer(value); } catch (NumberFormatException nfe) {daysSince = null;}
      cc.setDaysSince(daysSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("weeksSince")) {
      Integer weeksSince; try { weeksSince = new Integer(value); } catch (NumberFormatException nfe) {weeksSince = null;}
      cc.setWeeksSince(weeksSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("monthsSince")) {
      Integer monthsSince; try { monthsSince = new Integer(value); } catch (NumberFormatException nfe) {monthsSince = null;}
      cc.setMonthsSince(monthsSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("yearsSince")) {
      Integer yearsSince; try { yearsSince = new Integer(value); } catch (NumberFormatException nfe) {yearsSince = null;}
      cc.setYearsSince(yearsSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("howLongOther")) {cc.setHowLongOther(value);} 
    else if (property.equals("painXHour")) {
      Integer painXHour; try { painXHour = new Integer(value); } catch (NumberFormatException nfe) {painXHour = null;}
      cc.setPainXHour(painXHour);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painXDay")) {
      Integer painXDay; try { painXDay = new Integer(value); } catch (NumberFormatException nfe) {painXDay = null;}
      cc.setPainXDay(painXDay);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painXWeek")) {
      Integer painXWeek; try { painXWeek = new Integer(value); } catch (NumberFormatException nfe) {painXWeek = null;}
      cc.setPainXWeek(painXWeek);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painXMonth")) {
      Integer painXMonth; try { painXMonth = new Integer(value); } catch (NumberFormatException nfe) {painXMonth = null;}
      cc.setPainXMonth(painXMonth);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painDuration")) {cc.setPainDuration(value);} 
    else if (property.equals("painScale")) {
      Integer painScale; try { painScale = new Integer(value); } catch (NumberFormatException nfe) {painScale = null;}
      cc.setPainScale(painScale);
      updateClass = "ChiefComplaint";
    }
    else if (property.equals("painType")) {cc.setPainType(value);updateClass = "ChiefComplaint";} 
    else if (property.equals("denies")) {cc.setDenies(value);cc.setDeniesOther(value);updateClass = "ChiefComplaint";} 
    else if (property.equals("deniesOther")) {cc.setDeniesOther(value);updateClass = "ChiefComplaint";} 
    else if (property.equals("obgynG")) {obgyn.setG(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynP")) {obgyn.setP(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynT")) {obgyn.setT(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynA")) {obgyn.setA(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynL")) {obgyn.setL(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("pregStatus")) {obgyn.setPregStatus(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("breastfeeding")) {obgyn.setBreastfeeding(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("breastfeedingMonths")) {obgyn.setBreastfeedingMonths(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("lastPeriod")) {obgyn.setLastPeriod(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("ageFirstPeriod")) {obgyn.setAgeFirstPeriod(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("papSmearStatus")) {obgyn.setPapSmearStatus(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("birthControlStatus")) {obgyn.setBirthControlStatus(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("birthControlType")) {obgyn.setBirthControlType(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynHistory")) {obgyn.setHistory(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynHistoryOther")) {obgyn.setHistoryOther(value);updateClass = "OBGYNEncounterData";} 
    else if (property.equals("numResidents")) {
      Integer numResidents; try { numResidents = new Integer(value); } catch (NumberFormatException nfe) {numResidents = null;}
      pfsh.setNumResidents(numResidents);
      updateClass = "PFSH";
    }
    else if (property.equals("jobType")) {pfsh.setJobType(value);updateClass = "PFSH";} 
    else if (property.equals("motherAlive")) {pfsh.setMotherAlive(new Boolean(value));updateClass = "PFSH";} 
    else if (property.equals("motherDeathReason")) {pfsh.setMotherDeathReason(value);updateClass = "PFSH";} 
    else if (property.equals("fatherAlive")) {pfsh.setFatherAlive(new Boolean(value));updateClass = "PFSH";} 
    else if (property.equals("fatherDeathReason")) {pfsh.setFatherDeathReason(value);updateClass = "PFSH";} 
    else if (property.equals("partnerAlive")) {pfsh.setPartnerAlive(new Boolean(value));updateClass = "PFSH";} 
    else if (property.equals("partnerDeathReason")) {pfsh.setPartnerDeathReason(value);updateClass = "PFSH";} 
    else if (property.equals("numSiblings")) {
      Integer numSiblings; try { numSiblings = new Integer(value); } catch (NumberFormatException nfe) {numSiblings = null;}
      pfsh.setNumSiblings(numSiblings);
      updateClass = "PFSH";
    }
    else if (property.equals("numBrothers")) {
      Integer numBrothers; try { numBrothers = new Integer(value); } catch (NumberFormatException nfe) {numBrothers = null;}
      pfsh.setNumBrothers(numBrothers);
      updateClass = "PFSH";
    }
    else if (property.equals("numSisters")) {
      Integer numSisters; try { numSisters = new Integer(value); } catch (NumberFormatException nfe) {numSisters = null;}
      pfsh.setNumSisters(numSisters);
      updateClass = "PFSH";
    }
    else if (property.equals("numChildren")) {
      Integer numChildren; try { numChildren = new Integer(value); } catch (NumberFormatException nfe) {numChildren = null;}
      pfsh.setNumChildren(numChildren);
      updateClass = "PFSH";
    }
    else if (property.equals("numSons")) {
      Integer numSons; try { numSons = new Integer(value); } catch (NumberFormatException nfe) {numSons = null;}
      pfsh.setNumSons(numSons);
      updateClass = "PFSH";
    }
    else if (property.equals("numDaughters")) {
      Integer numDaughters; try { numDaughters = new Integer(value); } catch (NumberFormatException nfe) {numDaughters = null;}
      pfsh.setNumDaughters(numDaughters);
      updateClass = "PFSH";
    }
    else if (property.equals("numCupsCoffee")) {
      Integer numCupsCoffee; try { numCupsCoffee = new Integer(value); } catch (NumberFormatException nfe) {numCupsCoffee = null;}
      supp.setNumCupsCoffee(numCupsCoffee); 
      updateClass = "SuppQuestions";
    }
    else if (property.equals("numCupsTea")) {
      Integer numCupsTea; try { numCupsTea = new Integer(value); } catch (NumberFormatException nfe) {numCupsTea = null;}
      supp.setNumCupsTea(numCupsTea); 
      updateClass = "SuppQuestions";
    }
    else if (property.equals("numCupsWater")) {
      Integer numCupsWater; try { numCupsWater = new Integer(value); } catch (NumberFormatException nfe) {numCupsWater = null;}
      supp.setNumCupsWater(numCupsWater); 
      updateClass = "SuppQuestions";
    }
    else if (property.equals("waterSource")) {supp.setWaterSource(value);updateClass = "SuppQuestions";} 
    else if (property.equals("pastSM")) {hist.setPastSM(value);updateClass = "MedicalHistory";} 
    else if (property.equals("famHist")) {hist.setFamHist(value);updateClass = "SuppQuestions";} 
    else if (property.equals("famHistOther")) {hist.setFamHistOther(value);updateClass = "SuppQuestions";} 
    else if (property.equals("famHistNotes")) {hist.setFamHistNotes(value);updateClass = "SuppQuestions";} 
    else if (property.equals("allergFood")) {hist.setAllergFood(value);updateClass = "SuppQuestions";} 
    else if (property.equals("allergDrug")) {hist.setAllergDrug(value);updateClass = "SuppQuestions";} 
    else if (property.equals("allergEnv")) {hist.setAllergEnv(value);updateClass = "SuppQuestions";} 
    else if (property.equals("vacc")) {hist.setVacc(value);updateClass = "SuppQuestions";} 
    else if (property.equals("vaccNotes")) {hist.setVaccNotes(value);updateClass = "SuppQuestions";} 
    else if (property.equals("subst")) {hist.setSubst(value);updateClass = "SuppQuestions";} 
    else if (property.equals("smokePksDay")) {
      Float smokePksDay; try { smokePksDay = new Float(value); } catch (NumberFormatException nfe) {smokePksDay = null;}
      hist.setSmokePksDay(smokePksDay);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("yearsSmoked")) {
      Float yearsSmoked; try { yearsSmoked = new Float(value); } catch (NumberFormatException nfe) {yearsSmoked = null;}
      hist.setYearsSmoked(yearsSmoked);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("smokeYearsQuit")) {
      Float smokeYearsQuit; try { smokeYearsQuit = new Float(value); } catch (NumberFormatException nfe) {smokeYearsQuit = null;}
      hist.setSmokeYearsQuit(smokeYearsQuit);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("etohUnitsWeek")) {
      Float etohUnitsWeek; try { etohUnitsWeek = new Float(value); } catch (NumberFormatException nfe) {etohUnitsWeek = null;}
      hist.setEtohUnitsWeek(etohUnitsWeek);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("currentDrugs")) {hist.setCurrentDrugs(value);updateClass = "SuppQuestions";} 
    else if (property.equals("hb")) {lab.setHb(value);updateClass = "Lab";} 
    else if (property.equals("glucose")) {lab.setGlucose(value);updateClass = "Lab";} 
    else if (property.equals("urineDIP")) {lab.setUrineDIP(value);updateClass = "Lab";} 
    else if (property.equals("hs")) {exam.setHs(value);updateClass = "Exam";} 
    else if (property.equals("heartRhythm")) {exam.setHeartRhythm(value);updateClass = "Exam";} 
    else if (property.equals("diagnosis")) {exam.setDiagnosis(value);updateClass = "Exam";} 
    else if (property.equals("dxCode")) {exam.setDxCode(value);updateClass = "Exam";} 
    else if (property.equals("treatmentPlan")) {exam.setTreatmentPlan(value);updateClass = "Exam";} 
    else if (property.equals("txCode")) {exam.setTxCode(value);updateClass = "Exam";} 
    else if (property.equals("followUpLevel")) {followUp.setLevel(value);} 
    else if (property.equals("followUpWhen")) {followUp.setWhen(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpCondition")) {followUp.setCondition(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpDispenseRx")) {followUp.setDispenseRx(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpUSS")) {followUp.setUSS(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpPregnant")) {followUp.setPregnant(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpWoundCare")) {followUp.setWoundCare(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpRefToSpecialist")) {followUp.setRefToSpecialist(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpDentalList")) {followUp.setDentalList(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpPhysiotherapy")) {followUp.setPhysiotherapy(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpBloodLabs")) {followUp.setBloodLabs(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpOther")) {followUp.setOther(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpPulmonaryFXTest")) {followUp.setPulmonaryFXTest(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpVision")) {followUp.setVision(value);updateClass = "FollowUp";} 
    else if (property.equals("followUpCompleted")) {followUp.setCompleted(new Boolean(value));updateClass = "FollowUp";} 
    else if (property.equals("followUpDate")) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      Date followUpDate; try { followUpDate = sdf.parse(value); } catch (ParseException pe) {followUpDate = null;}
      followUp.setFollowUpDate(followUpDate);
      updateClass = "FollowUp";
    }
    else if (property.equals("followUpNotes")) {followUp.setNotes(value);updateClass = "FollowUp";} 
    
    fieldSet.add(property);
    if(updateClass.equals("FollowUp")) {}
    else if(updateClass.equals("FollowUp")) {patientDAO.update(followUp);}
    else if(updateClass.equals("Encounter")) {patientDAO.update(encounter);}
    else if(updateClass.equals("PFSH")) {patientDAO.update(pfsh);}
    else if(updateClass.equals("Vitals")) {patientDAO.update(vitals);}
    else if(updateClass.equals("Exam")) {patientDAO.update(exam);}
    else if(updateClass.equals("OBGYNEncounterData")) {patientDAO.update(obgyn);}
    else if(updateClass.equals("SOAPNote")) {patientDAO.update(soapNote);}
    else if(updateClass.equals("Lab")) {patientDAO.update(lab);}
    else if(updateClass.equals("ChiefComplaint")) {patientDAO.update(cc);}
    else if(updateClass.equals("SuppQuestions")) {patientDAO.update(supp);}
    else if(updateClass.equals("MedicalHistory")) {patientDAO.update(hist);}
    else if(updateClass.equals("Credentials")) {encrypt(patient);patientDAO.update(cred);}
    else if(updateClass.equals("Demographics")) {encrypt(patient);patientDAO.update(demo);}
    else if(updateClass.equals("FollowUp")) {patientDAO.update(followUp);}
    fieldSet.add(updateClass);
    activityLogService.logEditPatient(dto.getClinicianId(), patient.getId(), dto.getClinicianId(), encounter.getId(), fieldSet); 
  }
  
  
   public void encrypt(Patient patient) throws Exception { 
     //log.info("encrypt()");
     if (patient == null || patient.isEncrypted()) {
       return;
     }
     Credentials cred = patient.getCred();
     Demographics demo = patient.getDemo();
     if (cred.getUsername() != null) { cred.setUsername(DataEncryptor.encrypt(cred.getUsername()));}
     if (cred.getMrn() != null) { cred.setMrn(DataEncryptor.encrypt(cred.getMrn()));}
     if (cred.getFirstName() != null) { cred.setFirstName(DataEncryptor.encrypt(cred.getFirstName()));}
     if (cred.getMiddleName() != null) { cred.setMiddleName(DataEncryptor.encrypt(cred.getMiddleName()));}
     if (cred.getLastName() != null) { cred.setLastName(DataEncryptor.encrypt(cred.getLastName()));}
     if (cred.getAdditionalName() != null) { cred.setAdditionalName(DataEncryptor.encrypt(cred.getAdditionalName()));}
     if (cred.getEmail() != null) { cred.setEmail(DataEncryptor.encrypt(cred.getEmail()));}
     if (cred.getGovtId() != null) { cred.setGovtId(DataEncryptor.encrypt(cred.getGovtId()));}
     if (demo.getPrimaryPhone() != null) { demo.setPrimaryPhone(DataEncryptor.encrypt(demo.getPrimaryPhone()));}
     if (demo.getSecondaryPhone() != null) { demo.setSecondaryPhone(DataEncryptor.encrypt(demo.getSecondaryPhone()));}
     if (demo.getStreetAddress1() != null) { demo.setStreetAddress1(DataEncryptor.encrypt(demo.getStreetAddress1()));}
     if (demo.getStreetAddress2() != null) { demo.setStreetAddress2(DataEncryptor.encrypt(demo.getStreetAddress2()));}
     if (demo.getCity() != null) { demo.setCity(DataEncryptor.encrypt(demo.getCity()));}
     if (demo.getPostalCode() != null) { demo.setPostalCode(DataEncryptor.encrypt(demo.getPostalCode()));}
     if (demo.getEmployer() != null) { demo.setEmployer(DataEncryptor.encrypt(demo.getEmployer()));}
     if (demo.getSchoolName() != null) { demo.setSchoolName(DataEncryptor.encrypt(demo.getSchoolName()));}
     patient.setCred(cred);
     patient.setDemo(demo);
     patient.setEncrypted(true);
   }
   
   
   public void decrypt(Patient patient) throws Exception { 
     //log.info("decrypt()");
     if (patient == null || patient.isEncrypted() == false) {
       return;
     }
     Credentials cred = patient.getCred();
     Demographics demo = patient.getDemo();
     if (cred.getUsername() != null) { cred.setUsername(DataEncryptor.decrypt(cred.getUsername()));}
     if (cred.getMrn() != null) { cred.setMrn(DataEncryptor.decrypt(cred.getMrn()));}
     if (cred.getFirstName() != null) { cred.setFirstName(DataEncryptor.decrypt(cred.getFirstName()));}
     if (cred.getMiddleName() != null) { cred.setMiddleName(DataEncryptor.decrypt(cred.getMiddleName()));}
     if (cred.getLastName() != null) { cred.setLastName(DataEncryptor.decrypt(cred.getLastName()));}
     if (cred.getAdditionalName() != null) { cred.setAdditionalName(DataEncryptor.decrypt(cred.getAdditionalName()));}
     if (cred.getEmail() != null) { cred.setEmail(DataEncryptor.decrypt(cred.getEmail()));}
     if (cred.getGovtId() != null) { cred.setGovtId(DataEncryptor.decrypt(cred.getGovtId()));}
     if (demo.getPrimaryPhone() != null) { demo.setPrimaryPhone(DataEncryptor.decrypt(demo.getPrimaryPhone()));}
     if (demo.getSecondaryPhone() != null) { demo.setSecondaryPhone(DataEncryptor.decrypt(demo.getSecondaryPhone()));}
     if (demo.getStreetAddress1() != null) { demo.setStreetAddress1(DataEncryptor.decrypt(demo.getStreetAddress1()));}
     if (demo.getStreetAddress2() != null) { demo.setStreetAddress2(DataEncryptor.decrypt(demo.getStreetAddress2()));}
     if (demo.getCity() != null) { demo.setCity(DataEncryptor.decrypt(demo.getCity()));}
     if (demo.getPostalCode() != null) { demo.setPostalCode(DataEncryptor.decrypt(demo.getPostalCode()));}
     if (demo.getEmployer() != null) { demo.setEmployer(DataEncryptor.decrypt(demo.getEmployer()));}
     if (demo.getSchoolName() != null) { demo.setSchoolName(DataEncryptor.decrypt(demo.getSchoolName()));}
     patient.setCred(cred);
     patient.setDemo(demo);
     patient.setEncrypted(false);
   }
   
   
   public void encryptPatients(PatientDTO dto) throws Exception {
     List<Patient> ps  = appDAO.getPatients();
     for (Patient p : ps) {
       encrypt(p); 
       patientDAO.update(p.getCred());
       patientDAO.update(p.getDemo());
     }
  }

}
