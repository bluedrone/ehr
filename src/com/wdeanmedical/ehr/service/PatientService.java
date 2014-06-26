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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.wdeanmedical.ehr.persistence.AppDAO;
import com.wdeanmedical.ehr.persistence.PatientDAO;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.ChiefComplaint;
import com.wdeanmedical.ehr.entity.Country;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Exam;
import com.wdeanmedical.ehr.entity.EncounterMedication;
import com.wdeanmedical.ehr.entity.EncounterQuestion;
import com.wdeanmedical.ehr.entity.Gender;
import com.wdeanmedical.ehr.entity.Lab;
import com.wdeanmedical.ehr.entity.MaritalStatus;
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
import com.wdeanmedical.ehr.entity.SuppQuestions;
import com.wdeanmedical.ehr.entity.USState;
import com.wdeanmedical.ehr.entity.VitalSigns;
import com.wdeanmedical.external.fhir.PatientsFHIR;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
    return patientDAO.findEncountersByPatient(dto.getPatientId());
  }
  
  
  public List<ProgressNote> getProgressNotes(PatientDTO dto) throws Exception {
    Patient patient = patientDAO.findPatientById(dto.getPatientId());
    return patientDAO.findProgressNotesByPatient(patient);
  }
  
  
  public  void deactivatePatient(PatientDTO dto) throws Exception {
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
  
  
  public void createFamily(PatientDTO dto) throws Exception {
  Set<String> fieldSetPfsh = activityLogService.getListOfChangedFields(dto.getEncounter().getPatient().getPfsh());
    patientDAO.update(dto.getEncounter().getPatient().getPfsh());
    patientDAO.update(dto.getEncounter().getPatient());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetPfsh);
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
    for (EncounterMedication encounterMedication : dto.getEncounter().getPatient().getHist().getEncounterMedicationList()) {
      patientDAO.updateEncounterMedication(encounterMedication);
    }
    Set<String> fieldSetHist = activityLogService.getListOfChangedFields(dto.getEncounter().getPatient().getHist());
    patientDAO.update(dto.getEncounter().getPatient().getHist());
    patientDAO.update(dto.getEncounter().getPatient());
    patientDAO.update(dto.getEncounter());
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSetHist);
  }
  
  
  public void createBasicInfo(PatientDTO dto) throws Exception {
  Set<String> fieldSetDemo = activityLogService.getListOfChangedFields(dto.getEncounter().getPatient().getDemo());
    patientDAO.update(dto.getEncounter().getPatient().getDemo());
    Set<String> fieldSetCred = activityLogService.getListOfChangedFields(dto.getEncounter().getPatient().getCred());
    patientDAO.update(dto.getEncounter().getPatient().getCred());
    patientDAO.update(dto.getEncounter().getPatient());
    patientDAO.update(dto.getEncounter());
    Set<String> fieldSet = new HashSet<String>();
    fieldSet.addAll(fieldSetDemo);
    fieldSet.addAll(fieldSetCred);
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSet); 
  }
  
  
  public  void updateEncounterMedication(PatientDTO dto) throws Exception {
    EncounterMedication encounterMedication = patientDAO.findEncounterMedicationById(dto.getEncounterMedicationId());
    String property = dto.getUpdateProperty();
    String value = dto.getUpdatePropertyValue();
    if (property.equals("medication")) {
      encounterMedication.setMedication(value);
    }
    else if (property.equals("dose")) {
      encounterMedication.setDose(value);
    }
    else if (property.equals("frequency")) {
      encounterMedication.setFrequency(value);
    }
    Set<String> fieldSet = new HashSet<String>();
    fieldSet.add(property);
    patientDAO.update(encounterMedication);
    activityLogService.logEditEncounter(dto.getClinicianId(), dto.getPatientId(), dto.getClinicianId(), dto.getEncounterId(), fieldSet);
  }
  
  public  void updateEncounterQuestion(PatientDTO dto) throws Exception {
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
    dto.setEncounter(encounter);
  }
  
  public void updatePatient(org.hl7.fhir.Patient patientFHIR) throws Exception{    
   List<org.hl7.fhir.Identifier> identifierList = patientFHIR.getIdentifier();
   String mrn = null;
     if(identifierList.size() > 0){
      org.hl7.fhir.Identifier identifier = identifierList.get(0);
      if(identifier.getLabel().getValue().equalsIgnoreCase("MRN")){
         mrn = identifier.getValue().getValue();
      }
    }
    if(mrn != null){
      Patient patient = patientDAO.findPatientByMrn(mrn);
      Demographics demo = patient.getDemo();
      String email = null;
      String primaryPhone = null;
      String secondaryPhone = null;
      List<org.hl7.fhir.Contact> telecomList = patientFHIR.getTelecom();
      if(telecomList.size() > 0){
        org.hl7.fhir.Contact telecom = telecomList.get(0);
        email = telecom.getValue().getValue();
        if(telecomList.size() > 1){
          telecom = telecomList.get(1);
          primaryPhone = telecom.getValue().getValue();
          if(telecomList.size() > 2){
            telecom = telecomList.get(2);
            secondaryPhone = telecom.getValue().getValue();
          }
        }
      }
      String streetAddress1 = null;
      String streetAddress2 = null;
      String city = null;
      USState usState = null;
      String postalCode = null;
      Country country = null;
      List<org.hl7.fhir.Address> addressList = patientFHIR.getAddress();
      if(addressList.size() > 0){
        List<org.hl7.fhir.String> lineList = addressList.get(0).getLine();
        if(lineList.size() > 0){
          streetAddress1 = lineList.get(0).getValue();
          if(lineList.size() > 1){
            streetAddress2 = lineList.get(1).getValue();
          }
        }
        city = addressList.get(0).getCity().getValue();
        usState = patientDAO.findUSStateByName(addressList.get(0).getState().getValue());
        postalCode = addressList.get(0).getZip().getValue();
        country = patientDAO.findCountryByName(addressList.get(0).getCountry().getValue());
      }
      Gender gender = patientDAO.findGenderByCode(patientFHIR.getGender().getCoding().get(0).getCode().getValue());
      MaritalStatus maritalStatus = patientDAO.findMaritalStatusByCode(patientFHIR.getMaritalStatus().getCoding().get(0).getCode().getValue());
      org.hl7.fhir.DateTime birthDate = patientFHIR.getBirthDate();
      if(StringUtils.isNotEmpty(primaryPhone)){
      demo.setPrimaryPhone(primaryPhone);
        }
      if(StringUtils.isNotEmpty(secondaryPhone)){
      demo.setSecondaryPhone(secondaryPhone);
      }
      if(StringUtils.isNotEmpty(streetAddress1)){
      demo.setStreetAddress1(streetAddress1);
      }
      if(StringUtils.isNotEmpty(streetAddress2)){
      demo.setStreetAddress2(streetAddress2);
      }
      if(StringUtils.isNotEmpty(city)){
      demo.setCity(city);
      }
      if(usState != null){
      demo.setUsState(usState);
      }
        if(StringUtils.isNotEmpty(postalCode)){
        demo.setPostalCode(postalCode);
        }
        if(country != null){
        demo.setCountry(country);
        }
        if(gender != null){
        demo.setGender(gender);
        }
        if(maritalStatus != null){
        demo.setMaritalStatus(maritalStatus);
        }
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date dob = dateFormat.parse(birthDate.getValue());
      if(dob != null){
        demo.setDob(dob);
      }
      patientDAO.update(demo);
      Credentials cred = patient.getCred();
      String firstName = null;
      String middleName = null;
      String lastName  = null;
      List<org.hl7.fhir.HumanName> humanNameList = patientFHIR.getName();
      if(humanNameList.size() > 0){
        org.hl7.fhir.HumanName humanName = humanNameList.get(0);
        firstName = humanName.getGiven().get(0).getValue();
        if( humanName.getGiven().size() > 1){
          middleName = humanName.getGiven().get(1).getValue();
        }
        lastName  = humanName.getFamily().get(0).getValue();      
      }
      PatientStatus status = new PatientStatus(); 
      if(patientFHIR.getActive() != null && patientFHIR.getActive().isValue()){
        status.setId(PatientStatus.ACTIVE);
      }else{
        status.setId(PatientStatus.INACTIVE);
      }
      if(StringUtils.isNotEmpty(firstName)){
      cred.setFirstName(firstName);
      }
      if(StringUtils.isNotEmpty(middleName)){
      cred.setMiddleName(middleName);
      }
      if(StringUtils.isNotEmpty(lastName)){
      cred.setLastName(lastName);
      }
      if(StringUtils.isNotEmpty(email)){
      cred.setEmail(email);
      }
      if(status != null ){
      cred.setStatus(status);
      }
      patientDAO.update(cred);
      PFSH pfsh = patient.getPfsh();
      String caretakerName = null;
      List<org.hl7.fhir.ResourceReference> careProviderList = patientFHIR.getCareProvider();
      if(careProviderList.size() > 0){
        caretakerName = careProviderList.get(0).getDisplay().getValue();
      }
      if(patientFHIR.getMultipleBirthInteger() != null){
        Integer numChildren = patientFHIR.getMultipleBirthInteger().getValue();
        pfsh.setNumChildren(numChildren);
      }
      if(StringUtils.isNotEmpty(caretakerName)){
      pfsh.setCaretakerName(caretakerName);
      }
      patientDAO.update(pfsh);
      /*MedicalHistory hist = patient.getHist();
      patientDAO.update(hist);*/
    }
  }
  
  public org.hl7.fhir.Patient getPatientFullRecord(String mrn) throws Exception{
    Patient patient = patientDAO.findPatientByMrn(mrn);
    org.hl7.fhir.Patient fhirpatient = getPatientFHIR(patient);
    return fhirpatient;
  }

  public org.hl7.fhir.Patient getPatient(String mrn) throws Exception{
    Patient patient = patientDAO.findPatientByMrn(mrn);
    return getPatientFHIR(patient);
  }
  
  private org.hl7.fhir.Patient getPatientFHIR(Patient patient){
    org.hl7.fhir.Patient fhirpatient = new org.hl7.fhir.Patient();
    org.hl7.fhir.DateTime birthDate = new org.hl7.fhir.DateTime();
    birthDate.setValue(patient.getDemo().getDob().toString());
      
    fhirpatient.setBirthDate(birthDate);
      
    org.hl7.fhir.Identifier identifier = new org.hl7.fhir.Identifier();
    org.hl7.fhir.IdentifierUse identifierUse = new  org.hl7.fhir.IdentifierUse();
    identifierUse.setId(org.hl7.fhir.IdentifierUseList.USUAL.value());
    identifier.setUse(identifierUse);
    org.hl7.fhir.String mrn = new org.hl7.fhir.String();
    mrn.setValue("MRN");
    identifier.setLabel(mrn);
    org.hl7.fhir.String mrnValue = new org.hl7.fhir.String();
    mrnValue.setValue(patient.getCred().getMrn());
    identifier.setValue(mrnValue);
    fhirpatient.getIdentifier().add(identifier);
    
    org.hl7.fhir.CodeableConcept maritalStatusCodeableConcept = new org.hl7.fhir.CodeableConcept();
    org.hl7.fhir.Coding maritalStatusCoding = new org.hl7.fhir.Coding();
    MaritalStatus maritalStatus = patient.getDemo().getMaritalStatus();
    org.hl7.fhir.String maritalStatusDisplay = new org.hl7.fhir.String();
    maritalStatusDisplay.setValue(maritalStatus.getName());
    maritalStatusCoding.setDisplay(maritalStatusDisplay);
    org.hl7.fhir.Code maritalStatusCode = new org.hl7.fhir.Code();
    maritalStatusCode.setValue(maritalStatus.getCode());
    maritalStatusCoding.setCode(maritalStatusCode);
    maritalStatusCodeableConcept.getCoding().add(maritalStatusCoding);
    fhirpatient.setMaritalStatus(maritalStatusCodeableConcept);
    
    org.hl7.fhir.HumanName humanName = new org.hl7.fhir.HumanName();
    List<org.hl7.fhir.String> familyNameList = new ArrayList<org.hl7.fhir.String>();
    org.hl7.fhir.String familyName = new org.hl7.fhir.String();
    familyName.setValue(patient.getCred().getLastName());
    familyNameList.add(familyName);
    humanName.getFamily().addAll(familyNameList);
    List<org.hl7.fhir.String> givenNameList = new ArrayList<org.hl7.fhir.String>();
    org.hl7.fhir.String givenName1 = new org.hl7.fhir.String();
    givenName1.setValue(patient.getCred().getFirstName());
    givenNameList.add(givenName1);
    org.hl7.fhir.String givenName2 = new org.hl7.fhir.String();
    givenName2.setValue(patient.getCred().getMiddleName());
    givenNameList.add(givenName2);
    humanName.getGiven().addAll(givenNameList);
    fhirpatient.getName().add(humanName);
    
    org.hl7.fhir.Contact telecom = new org.hl7.fhir.Contact();
    org.hl7.fhir.String email = new org.hl7.fhir.String();
    email.setValue(patient.getCred().getEmail());
    telecom.setValue(email);
    fhirpatient.getTelecom().add(telecom);
    org.hl7.fhir.Contact telecom2 = new org.hl7.fhir.Contact();
    org.hl7.fhir.String primaryPhone = new org.hl7.fhir.String();
    primaryPhone.setValue(patient.getDemo().getPrimaryPhone());
    telecom2.setValue(primaryPhone);
    fhirpatient.getTelecom().add(telecom2);
    
    org.hl7.fhir.CodeableConcept genderCodeableConcept = new org.hl7.fhir.CodeableConcept();
    org.hl7.fhir.Coding genderStatusCoding = new org.hl7.fhir.Coding();
    org.hl7.fhir.Code genderCode = new org.hl7.fhir.Code();
    org.hl7.fhir.String genderDisplay = new org.hl7.fhir.String();
    genderDisplay.setValue(patient.getDemo().getGender().getName());
    genderCode.setValue(patient.getDemo().getGender().getCode());
    genderStatusCoding.setDisplay(genderDisplay);
    genderStatusCoding.setCode(genderCode);
    genderCodeableConcept.getCoding().add(genderStatusCoding);
    fhirpatient.setGender(genderCodeableConcept);
    
    org.hl7.fhir.Address address = new org.hl7.fhir.Address();
    org.hl7.fhir.String line1 = new org.hl7.fhir.String();
    line1.setValue(patient.getDemo().getStreetAddress1());
    address.getLine().add(line1);
    org.hl7.fhir.String line2 = new org.hl7.fhir.String();
    line2.setValue(patient.getDemo().getStreetAddress2());
    address.getLine().add(line2);
    org.hl7.fhir.String city = new org.hl7.fhir.String();
    city.setValue(patient.getDemo().getCity());
    address.setCity(city);
    org.hl7.fhir.String state = new org.hl7.fhir.String();
    state.setValue(patient.getDemo().getUsState().getName());
    address.setState(state);
    org.hl7.fhir.String zip = new org.hl7.fhir.String();
    zip.setValue(patient.getDemo().getPostalCode());
    address.setZip(zip);
    org.hl7.fhir.String country = new org.hl7.fhir.String();
    country.setValue(patient.getDemo().getCountry().getName());
    address.setCountry(country);
    fhirpatient.getAddress().add(address);
    
    org.hl7.fhir.Integer numChildren = new org.hl7.fhir.Integer();
    numChildren.setValue(patient.getPfsh().getNumChildren());
    fhirpatient.setMultipleBirthInteger(numChildren);
    return fhirpatient;
    
  }
  
  public  String importPatients(PatientsFHIR patientsFHIR) throws Exception {    
    List<org.hl7.fhir.Patient> patientFHIRList = patientsFHIR.getPatient();
    for(org.hl7.fhir.Patient patientFHIR : patientFHIRList){
      importPatient(patientFHIR);
    }    
    return null;
  }
  
  private  String importPatient(org.hl7.fhir.Patient patientFHIR) throws Exception {
    Patient patient = new Patient();
    patientDAO.create(patient);
    String email = null;
    String primaryPhone = null;
    String secondaryPhone = null;
    List<org.hl7.fhir.Contact> telecomList = patientFHIR.getTelecom();
    if(telecomList.size() > 0){
      org.hl7.fhir.Contact telecom = telecomList.get(0);
      email = telecom.getValue().getValue();
      if(telecomList.size() > 1){
        telecom = telecomList.get(1);
        primaryPhone = telecom.getValue().getValue();
        if(telecomList.size() > 2){
          telecom = telecomList.get(2);
          secondaryPhone = telecom.getValue().getValue();
        }
      }
    }
    String streetAddress1 = null;
    String streetAddress2 = null;
    String city = null;
    USState usState = null;
    String postalCode = null;
    Country country = null;
    List<org.hl7.fhir.Address> addressList = patientFHIR.getAddress();
    if(addressList.size() > 0){
      List<org.hl7.fhir.String> lineList = addressList.get(0).getLine();
      if(lineList.size() > 0){
        streetAddress1 = lineList.get(0).getValue();
        if(lineList.size() > 1){
          streetAddress2 = lineList.get(1).getValue();
        }
      }
      city = addressList.get(0).getCity().getValue();
      usState = patientDAO.findUSStateByName(addressList.get(0).getState().getValue());
      postalCode = addressList.get(0).getZip().getValue();
      country = patientDAO.findCountryByName(addressList.get(0).getCountry().getValue());
    }
    Gender gender = patientDAO.findGenderByCode(patientFHIR.getGender().getCoding().get(0).getCode().getValue());
    MaritalStatus maritalStatus = patientDAO.findMaritalStatusByCode(patientFHIR.getMaritalStatus().getCoding().get(0).getCode().getValue());
    org.hl7.fhir.DateTime birthDate = patientFHIR.getBirthDate();
    //String profileImagePath = null;
    Demographics demo = new Demographics();
    demo.setPatientId(patient.getId());
    demo.setPrimaryPhone(primaryPhone);
    demo.setSecondaryPhone(secondaryPhone);
    demo.setStreetAddress1(streetAddress1);
    demo.setStreetAddress2(streetAddress2);
    demo.setCity(city);
    demo.setUsState(usState);
    demo.setPostalCode(postalCode);
    demo.setCountry(country);
    demo.setGender(gender);
    demo.setMaritalStatus(maritalStatus);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date dob = dateFormat.parse(birthDate.getValue());
    demo.setDob(dob);
    patientDAO.create(demo);
    patient.setDemo(demo);
    String firstName = null;
    String middleName = null;
    String lastName  = null;
    List<org.hl7.fhir.HumanName> humanNameList = patientFHIR.getName();
    if(humanNameList.size() > 0){
      org.hl7.fhir.HumanName humanName = humanNameList.get(0);
      firstName = humanName.getGiven().get(0).getValue();
      if( humanName.getGiven().size() > 1){
        middleName = humanName.getGiven().get(1).getValue();
      }
      lastName  = humanName.getFamily().get(0).getValue();      
    }
    PatientStatus status = new PatientStatus(); 
    if(patientFHIR.getActive() != null && patientFHIR.getActive().isValue()){
      status.setId(PatientStatus.ACTIVE);
    }else{
      status.setId(PatientStatus.INACTIVE);
    }
    String mrn = null;
    List<org.hl7.fhir.Identifier> identifierList = patientFHIR.getIdentifier();
    if(identifierList.size() > 0){
      org.hl7.fhir.Identifier identifier = identifierList.get(0);
      if(identifier.getLabel().getValue().equalsIgnoreCase("MRN")){
        mrn = identifier.getValue().getValue();
      }
    }
    Credentials cred = new Credentials(); 
    cred.setPatientId(patient.getId());
    cred.setMrn(mrn);
    cred.setFirstName(firstName);
    cred.setMiddleName(middleName);
    cred.setLastName(lastName);
    cred.setEmail(email);
    cred.setStatus(status);
    cred.setPassword("not a password");
    patientDAO.create(cred);
    patient.setCred(cred);
    
    String caretakerName = null;
    List<org.hl7.fhir.ResourceReference> careProviderList = patientFHIR.getCareProvider();
    if(careProviderList.size() > 0){
      caretakerName = careProviderList.get(0).getDisplay().getValue();
    }
    PFSH pfsh = new PFSH();
    pfsh.setPatientId(patient.getId());
    if(patientFHIR.getMultipleBirthInteger() != null){
        Integer numChildren = patientFHIR.getMultipleBirthInteger().getValue();
      pfsh.setNumChildren(numChildren);
    }
    pfsh.setCaretakerName(caretakerName);
    patientDAO.create(pfsh);
    patient.setPfsh(pfsh);
    
    patientDAO.update(patient);
    return null;
  }
  
 
  
  public void createPatientAndEncounter(PatientDTO dto) throws Exception {
    Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
    Patient patient = new Patient();
    patientDAO.create(patient);
    
    Encounter encounter = patientDAO.createEncounter(patient, clinician);
    patient.setCurrentEncounterId(encounter.getId());
    
    Demographics demo = new Demographics();
    demo.setGender(dto.getEncounter().getPatient().getDemo().getGender());
    demo.setProfileImagePath(Core.appDefaultHeadshot); 
    demo.setPatientId(patient.getId());
    patientDAO.create(demo);
    patient.setDemo(demo);
    
    Credentials cred = new Credentials();
    cred.setFirstName(dto.getEncounter().getPatient().getCred().getFirstName());
    cred.setLastName(dto.getEncounter().getPatient().getCred().getLastName());
    cred.setAdditionalName(dto.getEncounter().getPatient().getCred().getAdditionalName());
    cred.setPassword("not a password"); 
    cred.setStatus(appDAO.findPatientStatusById(PatientStatus.ACTIVE));
    cred.setPatientId(patient.getId());
    patientDAO.create(cred);
    patient.setCred(cred);
    
    PFSH pfsh = new PFSH();
    pfsh.setPatientId(patient.getId());
    patientDAO.create(pfsh);
    patient.setPfsh(pfsh);
    
    MedicalHistory hist = new MedicalHistory();
    hist.setPatientId(patient.getId());
    patientDAO.create(hist);
    patient.setHist(hist);
    
    patientDAO.update(patient);

    encounter.setFollowUp(dto.getEncounter().getFollowUp());
    encounter.setCompleted(dto.getEncounter().getCompleted());
    encounter.setLockStatus(dto.getEncounter().getLockStatus());
    patientDAO.update(encounter);
    
    for (int i=0; i<3; i++) {
      addEncounterQuestion(encounter.getId()); // encounter.supp
      addEncounterMedication(patient.getId()); // patient.hist
    }

    Runtime runtime = Runtime.getRuntime();
    String patientDirPath =  Core.appBaseDir + Core.patientDirPath + "/" + patient.getId() + "/";
    new File(patientDirPath).mkdir();
    String[] cpArgs = {"cp", Core.appBaseDir + "images/" + Core.appDefaultHeadshot, patientDirPath};
    runtime.exec(cpArgs);
  }
  
  
  public  void addEncounterMedication(Integer patientId) throws Exception {
    EncounterMedication encounterMedication = new EncounterMedication();
    encounterMedication.setPatientId(patientId);
    patientDAO.create(encounterMedication);
  }
  
  
  public  void addEncounterQuestion(Integer encounterId) throws Exception {
    EncounterQuestion encounterQuestion = new EncounterQuestion();
    encounterQuestion.setEncounterId(encounterId);
    patientDAO.create(encounterQuestion);
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
    Clinician clinician = appDAO.findClinicianBySessionId(dto.getSessionId());
    Encounter encounter = patientDAO.createEncounter(patient, clinician);
    for (int i=0; i<3; i++) {
      addEncounterQuestion(encounter.getId()); // encounter.supp
      addEncounterMedication(patient.getId()); // patient.hist
    }
    activityLogService.logNewEncounter(clinician.getId(), dto.getPatientId(), clinician.getId(), encounter.getId());
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
    return note;
  }
  
  public Encounter getEncounter(PatientDTO dto) throws Exception {
    Encounter encounter = patientDAO.findEncounterById(dto.getEncounterId());
    return encounter;
  }
  
  public Encounter getEncounter(int id) throws Exception {
    Encounter encounter = patientDAO.findEncounterById(id);
    return encounter;
  }
  
  public Patient getPatient(int id) throws Exception {
    Patient patient = patientDAO.findPatientById(id);
    return patient;
  }
  
  public org.hl7.fhir.Encounter buildPatientEncounter(PatientDTO dto) throws Exception {
    Patient patient = getPatient(dto.getId());
    Encounter wdmEncounter = getEncounter(patient.getCurrentEncounterId());
    
    org.hl7.fhir.Encounter encounter = new org.hl7.fhir.Encounter();
    org.hl7.fhir.Identifier identifier = new org.hl7.fhir.Identifier();
    org.hl7.fhir.IdentifierUse identifierUse = new  org.hl7.fhir.IdentifierUse();
    identifierUse.setId(org.hl7.fhir.IdentifierUseList.TEMP.value());
    identifier.setUse(identifierUse);
    org.hl7.fhir.String mrn = new org.hl7.fhir.String();
    mrn.setValue("Sara's encounter on March eleventh 2013");
    identifier.setLabel(mrn);
    org.hl7.fhir.String mrnValue = new org.hl7.fhir.String();
    mrnValue.setValue("Encounter_Sara_20130311");
    identifier.setValue(mrnValue);
    encounter.getIdentifier().add(identifier);
    org.hl7.fhir.String reasonValue = new org.hl7.fhir.String();
    reasonValue.setValue(wdmEncounter.getCc().getDescription());
    org.hl7.fhir.CodeableConcept reasonCodeableConcept = new org.hl7.fhir.CodeableConcept();
    reasonCodeableConcept.setText(reasonValue);
    encounter.setReason(reasonCodeableConcept);
    if(wdmEncounter.getCompleted()){
      org.hl7.fhir.EncounterState encounterState = new org.hl7.fhir.EncounterState();
      org.hl7.fhir.EncounterStateList enStLi = org.hl7.fhir.EncounterStateList.FINISHED;
      encounterState.setValue(enStLi);
      encounter.setStatus(encounterState);
      org.hl7.fhir.EncounterClass enClass = new org.hl7.fhir.EncounterClass();
      org.hl7.fhir.EncounterClassList enClLi = org.hl7.fhir.EncounterClassList.OUTPATIENT;
      enClass.setValue(enClLi);
      encounter.setClazz(enClass);
    } else {
      org.hl7.fhir.EncounterState encounterState = new org.hl7.fhir.EncounterState();
      org.hl7.fhir.EncounterStateList enStLi = org.hl7.fhir.EncounterStateList.IN_PROGRESS;
      encounterState.setValue(enStLi);
      encounter.setStatus(encounterState);
      org.hl7.fhir.EncounterClass enClass = new org.hl7.fhir.EncounterClass();
      org.hl7.fhir.EncounterClassList enClLi = org.hl7.fhir.EncounterClassList.AMBULATORY;
      enClass.setValue(enClLi);
      encounter.setClazz(enClass);
    }
    org.hl7.fhir.String subjectValue = new org.hl7.fhir.String();
    subjectValue.setValue(patient.getCred().getFirstName() + " " + patient.getCred().getLastName());
    org.hl7.fhir.ResourceReference reRef = new org.hl7.fhir.ResourceReference();
    reRef.setDisplay(subjectValue);
    encounter.setSubject(reRef);
    
    org.hl7.fhir.String indValue = new org.hl7.fhir.String();
    indValue.setValue(wdmEncounter.getClinician().getFull_name());
    org.hl7.fhir.ResourceReference indRef = new org.hl7.fhir.ResourceReference();
    indRef.setDisplay(indValue);
    org.hl7.fhir.EncounterParticipant encP = new org.hl7.fhir.EncounterParticipant();
    encP.setIndividual(indRef);
    encounter.getParticipant().add(encP);
    return encounter;
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
    }else{
      clinician = encounter.getClinician();
    }    
    activityLogService.logViewEncounter(clinician.getId(), patient.getId(), clinician.getId(), encounter.getId());
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
    
    String property = dto.getUpdateProperty();
    String value = dto.getUpdatePropertyValue();
    if (property.equals("firstName")) {cred.setFirstName(value);updateClass = "Credentials";} 
    else if (property.equals("middleName")) {cred.setMiddleName(value);updateClass = "Credentials";} 
    else if (property.equals("lastName")) {cred.setLastName(value);updateClass = "Credentials";} 
    else if (property.equals("gender")) {demo.setGender(patientDAO.findGenderByCode(value));updateClass = "Demographics";} 
    else if (property.equals("consultLocation")) {encounter.setConsultLocation(value);updateClass = "Encounter";} 
    else if (property.equals("notes")) {encounter.setNotes(value);updateClass = "Encounter";} 
    else if (property.equals("community")) {encounter.setCommunity(value);updateClass = "Encounter";} 
    else if (property.equals("govtId")) {cred.setGovtId(value);updateClass = "Credentials";} 
    
    else if (property.equals("ageInYears")) {
      Integer ageInYears; try { ageInYears = new Integer(value); } catch (NumberFormatException nfe) {ageInYears = null;}
      encounter.setAgeInYears(ageInYears);
      updateClass = "Encounter";
    }
    else if (property.equals("ageInMonths")) {
      Integer ageInMonths; try { ageInMonths = new Integer(value); } catch (NumberFormatException nfe) {ageInMonths = null;}
      encounter.setAgeInMonths(ageInMonths);
      updateClass = "Encounter";
    }
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
    else if (property.equals("arm")) {
      Float arm; try { arm = new Float(value); } catch (NumberFormatException nfe) {arm = null;}
      vitals.setArm(arm);
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
    else if (property.equals("description")) {cc.setDescription(dto.getCcDescription());updateClass = "ChiefComplaint";} 
    else if (property.equals("specificLocation")) {cc.setSpecificLocation(dto.getSpecificLocation());updateClass = "ChiefComplaint";} 
    else if (property.equals("occursWhen")) {cc.setOccursWhen(dto.getOccursWhen());updateClass = "ChiefComplaint";} 
    else if (property.equals("hoursSince")) {
      Integer hoursSince; try { hoursSince = new Integer(dto.getHoursSince()); } catch (NumberFormatException nfe) {hoursSince = null;}
      cc.setHoursSince(hoursSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("daysSince")) {
      Integer daysSince; try { daysSince = new Integer(dto.getDaysSince()); } catch (NumberFormatException nfe) {daysSince = null;}
      cc.setDaysSince(daysSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("weeksSince")) {
      Integer weeksSince; try { weeksSince = new Integer(dto.getWeeksSince()); } catch (NumberFormatException nfe) {weeksSince = null;}
      cc.setWeeksSince(weeksSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("monthsSince")) {
      Integer monthsSince; try { monthsSince = new Integer(dto.getMonthsSince()); } catch (NumberFormatException nfe) {monthsSince = null;}
      cc.setMonthsSince(monthsSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("yearsSince")) {
      Integer yearsSince; try { yearsSince = new Integer(dto.getYearsSince()); } catch (NumberFormatException nfe) {yearsSince = null;}
      cc.setYearsSince(yearsSince);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("howLongOther")) {cc.setHowLongOther(dto.getHowLongOther());} 
    else if (property.equals("painXHour")) {
      Integer painXHour; try { painXHour = new Integer(dto.getPainXHour()); } catch (NumberFormatException nfe) {painXHour = null;}
      cc.setPainXHour(painXHour);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painXDay")) {
      Integer painXDay; try { painXDay = new Integer(dto.getPainXDay()); } catch (NumberFormatException nfe) {painXDay = null;}
      cc.setPainXDay(painXDay);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painXWeek")) {
      Integer painXWeek; try { painXWeek = new Integer(dto.getPainXWeek()); } catch (NumberFormatException nfe) {painXWeek = null;}
      cc.setPainXWeek(painXWeek);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painXMonth")) {
      Integer painXMonth; try { painXMonth = new Integer(dto.getPainXMonth()); } catch (NumberFormatException nfe) {painXMonth = null;}
      cc.setPainXMonth(painXMonth);
      updateClass = "ChiefComplaint";
    } 
    else if (property.equals("painDuration")) {cc.setPainDuration(dto.getPainDuration());} 
    else if (property.equals("painScale")) {
      Integer painScale; try { painScale = new Integer(value); } catch (NumberFormatException nfe) {painScale = null;}
      cc.setPainScale(painScale);
      updateClass = "ChiefComplaint";
    }
    else if (property.equals("painType")) {cc.setPainType(dto.getPainType());updateClass = "ChiefComplaint";} 
    else if (property.equals("denies")) {cc.setDenies(dto.getDenies());cc.setDeniesOther(dto.getDeniesOther());updateClass = "ChiefComplaint";} 
    else if (property.equals("deniesOther")) {cc.setDeniesOther(value);updateClass = "ChiefComplaint";} 
    else if (property.equals("obgynG")) {obgyn.setG(dto.getObgynG());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynP")) {obgyn.setP(dto.getObgynP());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynT")) {obgyn.setT(dto.getObgynT());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynA")) {obgyn.setA(dto.getObgynA());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynL")) {obgyn.setL(dto.getObgynL());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("pregStatus")) {obgyn.setPregStatus(dto.getPregStatus());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("breastfeeding")) {obgyn.setBreastfeeding(dto.getBreastfeeding());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("breastfeedingMonths")) {obgyn.setBreastfeedingMonths(dto.getBreastfeedingMonths());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("lastPeriod")) {obgyn.setLastPeriod(dto.getLastPeriod());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("ageFirstPeriod")) {obgyn.setAgeFirstPeriod(dto.getAgeFirstPeriod());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("papSmearStatus")) {obgyn.setPapSmearStatus(dto.getPapSmearStatus());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("birthControlStatus")) {obgyn.setBirthControlStatus(dto.getBirthControlStatus());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("birthControlType")) {obgyn.setBirthControlType(dto.getBirthControlType());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynHistory")) {obgyn.setHistory(dto.getObgynHistory());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("obgynHistoryOther")) {obgyn.setHistoryOther(dto.getObgynHistoryOther());updateClass = "OBGYNEncounterData";} 
    else if (property.equals("numResidents")) {
      Integer numResidents; try { numResidents = new Integer(value); } catch (NumberFormatException nfe) {numResidents = null;}
      pfsh.setNumResidents(numResidents);
      updateClass = "PFSH";
    }
    else if (property.equals("jobType")) {pfsh.setJobType(dto.getJobType());updateClass = "PFSH";} 
    else if (property.equals("motherAlive")) {pfsh.setMotherAlive(dto.isMotherAlive());updateClass = "PFSH";} 
    else if (property.equals("motherDeathReason")) {pfsh.setMotherDeathReason(dto.getMotherDeathReason());updateClass = "PFSH";} 
    else if (property.equals("fatherAlive")) {pfsh.setFatherAlive(dto.isFatherAlive());updateClass = "PFSH";} 
    else if (property.equals("fatherDeathReason")) {pfsh.setFatherDeathReason(dto.getFatherDeathReason());updateClass = "PFSH";} 
    else if (property.equals("partnerAlive")) {pfsh.setPartnerAlive(dto.isPartnerAlive());updateClass = "PFSH";} 
    else if (property.equals("partnerDeathReason")) {pfsh.setPartnerDeathReason(dto.getPartnerDeathReason());updateClass = "PFSH";} 
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
    else if (property.equals("waterSource")) {supp.setWaterSource(dto.getWaterSource());updateClass = "SuppQuestions";} 
    else if (property.equals("pastSM")) {hist.setPastSM(dto.getPastSM());updateClass = "MedicalHistory";} 
    else if (property.equals("famHist")) {hist.setFamHist(dto.getFamHist());updateClass = "SuppQuestions";} 
    else if (property.equals("famHistOther")) {hist.setFamHistOther(dto.getFamHistOther());updateClass = "SuppQuestions";} 
    else if (property.equals("famHistNotes")) {hist.setFamHistNotes(dto.getFamHistNotes());updateClass = "SuppQuestions";} 
    else if (property.equals("allergFood")) {hist.setAllergFood(dto.getAllergFood());updateClass = "SuppQuestions";} 
    else if (property.equals("allergDrug")) {hist.setAllergDrug(dto.getAllergDrug());updateClass = "SuppQuestions";} 
    else if (property.equals("allergEnv")) {hist.setAllergEnv(dto.getAllergEnv());updateClass = "SuppQuestions";} 
    else if (property.equals("vacc")) {hist.setVacc(dto.getVacc());updateClass = "SuppQuestions";} 
    else if (property.equals("vaccNotes")) {hist.setVaccNotes(dto.getVaccNotes());updateClass = "SuppQuestions";} 
    else if (property.equals("subst")) {hist.setSubst(dto.getSubst());updateClass = "SuppQuestions";} 
    else if (property.equals("smokePksDay")) {
      Float smokePksDay; try { smokePksDay = new Float(dto.getSmokePksDay()); } catch (NumberFormatException nfe) {smokePksDay = null;}
      hist.setSmokePksDay(smokePksDay);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("yearsSmoked")) {
      Float yearsSmoked; try { yearsSmoked = new Float(dto.getYearsSmoked()); } catch (NumberFormatException nfe) {yearsSmoked = null;}
      hist.setYearsSmoked(yearsSmoked);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("smokeYearsQuit")) {
      Float smokeYearsQuit; try { smokeYearsQuit = new Float(dto.getSmokeYearsQuit()); } catch (NumberFormatException nfe) {smokeYearsQuit = null;}
      hist.setSmokeYearsQuit(smokeYearsQuit);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("etohUnitsWeek")) {
      Float etohUnitsWeek; try { etohUnitsWeek = new Float(dto.getEtohUnitsWeek()); } catch (NumberFormatException nfe) {etohUnitsWeek = null;}
      hist.setEtohUnitsWeek(etohUnitsWeek);
      updateClass = "SuppQuestions";
    } 
    else if (property.equals("currentDrugs")) {hist.setCurrentDrugs(dto.getCurrentDrugs());updateClass = "SuppQuestions";} 
    else if (property.equals("hb")) {lab.setHb(dto.getHb());updateClass = "Lab";} 
    else if (property.equals("glucose")) {lab.setGlucose(dto.getGlucose());updateClass = "Lab";} 
    else if (property.equals("urineDIP")) {lab.setUrineDIP(dto.getUrineDIP());updateClass = "Lab";} 
    else if (property.equals("hs")) {exam.setHs(dto.getHs());updateClass = "Exam";} 
    else if (property.equals("heartRhythm")) {exam.setHeartRhythm(dto.getHeartRhythm());updateClass = "Exam";} 
    else if (property.equals("diagnosis")) {exam.setDiagnosis(dto.getDiagnosis());updateClass = "Exam";} 
    else if (property.equals("dxCode")) {exam.setDxCode(dto.getDxCode());updateClass = "Exam";} 
    else if (property.equals("treatmentPlan")) {exam.setTreatmentPlan(dto.getTreatmentPlan());updateClass = "Exam";} 
    else if (property.equals("txCode")) {exam.setTxCode(dto.getTxCode());updateClass = "Exam";} 
    else if (property.equals("followUpLevel")) {followUp.setLevel(dto.getFollowUpLevel());} 
    else if (property.equals("followUpWhen")) {followUp.setWhen(dto.getFollowUpWhen());updateClass = "FollowUp";} 
    else if (property.equals("followUpCondition")) {followUp.setCondition(dto.getFollowUpCondition());updateClass = "FollowUp";} 
    else if (property.equals("followUpDispenseRx")) {followUp.setDispenseRx(dto.getFollowUpDispenseRx());updateClass = "FollowUp";} 
    else if (property.equals("followUpUSS")) {followUp.setUSS(dto.getFollowUpUSS());updateClass = "FollowUp";} 
    else if (property.equals("followUpPregnant")) {followUp.setPregnant(dto.getFollowUpPregnant());updateClass = "FollowUp";} 
    else if (property.equals("followUpWoundCare")) {followUp.setWoundCare(dto.getFollowUpWoundCare());updateClass = "FollowUp";} 
    else if (property.equals("followUpRefToSpecialist")) {followUp.setRefToSpecialist(dto.getFollowUpRefToSpecialist());updateClass = "FollowUp";} 
    else if (property.equals("followUpDentalList")) {followUp.setDentalList(dto.getFollowUpDentalList());updateClass = "FollowUp";} 
    else if (property.equals("followUpPhysiotherapy")) {followUp.setPhysiotherapy(dto.getFollowUpPhysiotherapy());updateClass = "FollowUp";} 
    else if (property.equals("followUpBloodLabs")) {followUp.setBloodLabs(dto.getFollowUpBloodLabs());updateClass = "FollowUp";} 
    else if (property.equals("followUpOther")) {followUp.setOther(dto.getFollowUpOther());updateClass = "FollowUp";} 
    else if (property.equals("followUpPulmonaryFXTest")) {followUp.setPulmonaryFXTest(dto.getFollowUpPulmonaryFXTest());updateClass = "FollowUp";} 
    else if (property.equals("followUpVision")) {followUp.setVision(dto.getFollowUpVision());updateClass = "FollowUp";} 
    else if (property.equals("followUpCompleted")) {followUp.setCompleted(dto.getFollowUpCompleted());updateClass = "FollowUp";} 
    else if (property.equals("followUpDate")) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      Date followUpDate; try { followUpDate = sdf.parse(value); } catch (ParseException pe) {followUpDate = null;}
      followUp.setFollowUpDate(followUpDate);
      updateClass = "FollowUp";
    }
    else if (property.equals("followUpNotes")) {followUp.setNotes(dto.getFollowUpNotes());updateClass = "FollowUp";} 
    
    fieldSet.add(property);
    if(updateClass.equals("FollowUp")) {}
    else if(updateClass.equals("FollowUp")) {patientDAO.update(followUp);}
    else if(updateClass.equals("Encounter")) {patientDAO.update(encounter);}
    else if(updateClass.equals("PFSH")) {patientDAO.update(pfsh);}
    else if(updateClass.equals("Vitals")) {patientDAO.update(vitals);}
    else if(updateClass.equals("Exam")) {patientDAO.update(exam);}
    else if(updateClass.equals("OBGYNEncounterData")) {patientDAO.update(obgyn);}
    else if(updateClass.equals("Lab")) {patientDAO.update(lab);}
    else if(updateClass.equals("ChiefComplaint")) {patientDAO.update(cc);}
    else if(updateClass.equals("SuppQuestions")) {patientDAO.update(supp);}
    else if(updateClass.equals("MedicalHistory")) {patientDAO.update(hist);}
    else if(updateClass.equals("Credentials")) {patientDAO.update(cred);}
    else if(updateClass.equals("Demographics")) {patientDAO.update(demo);}
    else if(updateClass.equals("FollowUp")) {patientDAO.update(followUp);}
    fieldSet.add(updateClass);
    activityLogService.logEditPatient(dto.getClinicianId(), patient.getId(), dto.getClinicianId(), encounter.getId(), fieldSet); 
  }

}
