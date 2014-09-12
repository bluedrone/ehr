/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.service;


import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import com.wdeanmedical.ehr.persistence.AdminDAO;
import com.wdeanmedical.ehr.persistence.AppDAO;
import com.wdeanmedical.ehr.persistence.ExternalDAO;
import com.wdeanmedical.ehr.persistence.PatientDAO;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.Country;
import com.wdeanmedical.ehr.entity.Credential;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.PatientHistoryMedication;
import com.wdeanmedical.ehr.entity.EncounterQuestion;
import com.wdeanmedical.ehr.entity.Gender;
import com.wdeanmedical.ehr.entity.MaritalStatus;
import com.wdeanmedical.ehr.entity.MedicalHistory;
import com.wdeanmedical.ehr.entity.PFSH;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientStatus;
import com.wdeanmedical.ehr.entity.Role;
import com.wdeanmedical.ehr.entity.USState;
import com.wdeanmedical.ehr.dto.BooleanResultDTO;
import com.wdeanmedical.ehr.util.ClinicianSessionData;
import com.wdeanmedical.ehr.util.OneWayPasswordEncoder;
import com.wdeanmedical.external.fhir.PatientFullRecordFHIR;
import com.wdeanmedical.external.fhir.PatientsFHIR;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExternalService {

  private static Log log = LogFactory.getLog(ExternalService.class);
 public static int RETURN_CODE_INVALID_PASSWORD = -2;
  
  private ServletContext context;
  private WebApplicationContext wac;
  private AppDAO appDAO;
  private AdminDAO adminDAO;
  private ExternalDAO externalDAO;
  private PatientDAO patientDAO;
  private ActivityLogService activityLogService;
  private PatientService patientService;


  public ExternalService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    appDAO = (AppDAO) wac.getBean("appDAO");
    adminDAO = (AdminDAO) wac.getBean("adminDAO");
    externalDAO = (ExternalDAO) wac.getBean("externalDAO");
    patientDAO = (PatientDAO) wac.getBean("patientDAO");
    activityLogService = new ActivityLogService();
    patientService = new PatientService();
  }
  
  public AuthorizedDTO auth(LoginDTO loginDTO, String ipAddress) throws Exception {
    AuthorizedDTO dto = new AuthorizedDTO();
    dto.setAuthenticated(false);
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
      log.info("======= Added " + clinicianSession.toString()); 
      activityLogService.logLogin(clinician.getId());
      dto.setAuthenticated(true);
      dto.setSessionId(clinician.getSessionId());
    }
    return dto;
  }
  
  public org.hl7.fhir.Patient getPatient(String mrn) throws Exception{
    Patient patient = patientDAO.findPatientByMrn(mrn);
    patientService.decrypt(patient);
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
      patientService.encrypt(patient);
      patientDAO.update(patient);
    }
  }
  
  public PatientFullRecordFHIR getPatientFullRecord(String mrn) throws Exception {
    PatientFullRecordFHIR patientFullRecordFHIR = new PatientFullRecordFHIR();    
    Patient patient = patientDAO.findPatientByMrn(mrn);
    patientService.decrypt(patient);
    org.hl7.fhir.Patient fhirpatient = getPatientFHIR(patient);
    patientFullRecordFHIR.setPatient(fhirpatient);
    MedicalHistory medicalHistory = patient.getHist();
    List<org.hl7.fhir.SensitivityType> sensitivityTypes = new ArrayList<org.hl7.fhir.SensitivityType>();
    
    org.hl7.fhir.SensitivityType sensitivityTypeAllergyFood = new org.hl7.fhir.SensitivityType();
    sensitivityTypeAllergyFood.setValue(org.hl7.fhir.SensitivityTypeList.ALLERGY);
    sensitivityTypeAllergyFood.setId(medicalHistory.getAllergFood());
    sensitivityTypes.add(sensitivityTypeAllergyFood);
    
    org.hl7.fhir.SensitivityType sensitivityTypeAllergyDrug = new org.hl7.fhir.SensitivityType();
    sensitivityTypeAllergyDrug.setValue(org.hl7.fhir.SensitivityTypeList.ALLERGY);
    sensitivityTypeAllergyDrug.setId(medicalHistory.getAllergDrug());
    sensitivityTypes.add(sensitivityTypeAllergyDrug);
    
    org.hl7.fhir.SensitivityType sensitivityTypeAllergyEnv = new org.hl7.fhir.SensitivityType();
    sensitivityTypeAllergyEnv.setValue(org.hl7.fhir.SensitivityTypeList.ALLERGY);
    sensitivityTypeAllergyEnv.setId(medicalHistory.getAllergEnv());
    sensitivityTypes.add(sensitivityTypeAllergyEnv);    
    
    patientFullRecordFHIR.setSensitivityTypes(sensitivityTypes);
    
    List<org.hl7.fhir.MedicationAdministration> medicationAdministrations = new ArrayList<org.hl7.fhir.MedicationAdministration>();
    
    List<PatientHistoryMedication> patientMedicationList = patientDAO.getPatientMedicationsByPatient(patient.getId());
    
    if(patientMedicationList != null){
    
      for(PatientHistoryMedication patientHistoryMedication : patientMedicationList){
        
        org.hl7.fhir.MedicationAdministration medicationAdministration = new org.hl7.fhir.MedicationAdministration();
        
        org.hl7.fhir.ResourceReference medicationResourceReference = new org.hl7.fhir.ResourceReference();
        org.hl7.fhir.String medicationString = new org.hl7.fhir.String();
        medicationString.setValue(patientHistoryMedication.getMedication());
        medicationResourceReference.setDisplay(medicationString);
        medicationResourceReference.setReference(medicationString);      
        medicationAdministration.setMedication(medicationResourceReference);
        
        org.hl7.fhir.ResourceReference prescriptionResourceReference = new org.hl7.fhir.ResourceReference();
        org.hl7.fhir.String prescriptionString = new org.hl7.fhir.String();
        prescriptionString.setValue(patientHistoryMedication.getDose());
        prescriptionResourceReference.setDisplay(prescriptionString);
        prescriptionResourceReference.setReference(prescriptionString);  
        medicationAdministration.setPrescription(prescriptionResourceReference);
        
        /*org.hl7.fhir.Period period = new org.hl7.fhir.Period();
        
        period.setStart(value);
        period.setStart(value);
        
        medicationAdministration.setWhenGiven(period);*/
        
        
        medicationAdministrations.add(medicationAdministration);         
        
      }
    
    }
    
    patientFullRecordFHIR.setMedicationAdministrations(medicationAdministrations);
    
    List<EncounterQuestion> encounterQuestions = patientDAO.getEncounterQuestionsByEncounter(patient.getCurrentEncounterId());
    
    List<org.hl7.fhir.Questionnaire> questionnaires = new ArrayList<org.hl7.fhir.Questionnaire>();
    
    for(EncounterQuestion encounterQuestion : encounterQuestions){
      
      org.hl7.fhir.Questionnaire questionnaire = new org.hl7.fhir.Questionnaire();
      
      org.hl7.fhir.ResourceReference subjectResourceReference = new org.hl7.fhir.ResourceReference();
      org.hl7.fhir.String subjectString = new org.hl7.fhir.String();
      subjectString.setValue(encounterQuestion.getQuestion());
      subjectResourceReference.setDisplay(subjectString);
      subjectResourceReference.setReference(subjectString);       
      questionnaire.setSubject(subjectResourceReference);
      
      org.hl7.fhir.Narrative narrative = new org.hl7.fhir.Narrative();
      
      narrative.setId(encounterQuestion.getResponse());
      
      questionnaire.setText(narrative);
      
      questionnaires.add(questionnaire);
      
    }
    
    patientFullRecordFHIR.setQuestionnaires(questionnaires);
    
    List<org.hl7.fhir.RelatedPerson> relatedPersons = new ArrayList<org.hl7.fhir.RelatedPerson>();
    
    PFSH pfsh = patient.getPfsh();
    
    org.hl7.fhir.RelatedPerson motherRelatedPerson = new org.hl7.fhir.RelatedPerson();
    org.hl7.fhir.String motherRelationshipString = new org.hl7.fhir.String();
    motherRelationshipString.setValue("Mother");
    org.hl7.fhir.CodeableConcept motherCodeableConcept = new org.hl7.fhir.CodeableConcept();
    motherCodeableConcept.setText(motherRelationshipString);
    org.hl7.fhir.HumanName motherHumanName = new  org.hl7.fhir.HumanName();
    org.hl7.fhir.String motherNameString = new org.hl7.fhir.String();
    motherNameString.setValue(pfsh.getMotherName());
    motherHumanName.setText(motherNameString);
    motherRelatedPerson.setName(motherHumanName);
    motherRelatedPerson.setRelationship(motherCodeableConcept);
    
    relatedPersons.add(motherRelatedPerson);
    
    patientFullRecordFHIR.setRelatedPersons(relatedPersons);
    
    
    return patientFullRecordFHIR;
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
    patientService.encrypt(patient); 
    patientDAO.update(cred);
    patientDAO.update(demo);
    patientDAO.update(patient);
    return null;
  }
  
  /* Note: not finished, not ready */
  public org.hl7.fhir.Encounter buildPatientEncounter(PatientDTO dto) throws Exception {
    Patient patient = patientService.getPatient(dto.getId());
    patientService.decrypt(patient);
    Encounter wdmEncounter = patientService.getEncounter(patient.getCurrentEncounterId());
    
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
  
  
  public PatientsFHIR buildPatientResource(List<Patient> patients) throws Exception {
    PatientsFHIR patientsFHIR = new PatientsFHIR();
    
    for(int i = 0; i < patients.size(); i++){
      patientService.decrypt(patients.get(i));
      org.hl7.fhir.Patient fhirpatient = new org.hl7.fhir.Patient();
      org.hl7.fhir.DateTime birthDate = new org.hl7.fhir.DateTime();
      birthDate.setValue(patients.get(i).getDemo().getDob().toString());
        
      fhirpatient.setBirthDate(birthDate);
        
      org.hl7.fhir.Identifier identifier = new org.hl7.fhir.Identifier();
      org.hl7.fhir.IdentifierUse identifierUse = new  org.hl7.fhir.IdentifierUse();
      identifierUse.setId(org.hl7.fhir.IdentifierUseList.USUAL.value());
      identifier.setUse(identifierUse);
      org.hl7.fhir.String mrn = new org.hl7.fhir.String();
      mrn.setValue("MRN");
      identifier.setLabel(mrn);
      org.hl7.fhir.String mrnValue = new org.hl7.fhir.String();
      mrnValue.setValue(patients.get(i).getCred().getMrn());
      identifier.setValue(mrnValue);
      fhirpatient.getIdentifier().add(identifier);
      
      org.hl7.fhir.CodeableConcept maritalStatusCodeableConcept = new org.hl7.fhir.CodeableConcept();
      org.hl7.fhir.Coding maritalStatusCoding = new org.hl7.fhir.Coding();
      org.hl7.fhir.String maritalStatusDisplay = new org.hl7.fhir.String();
      org.hl7.fhir.Code maritalStatusCode = new org.hl7.fhir.Code();
      MaritalStatus maritalStatus = null;
      if(patients.get(i).getDemo() != null && patients.get(i).getDemo().getMaritalStatus() != null){
    	  maritalStatus = patients.get(i).getDemo().getMaritalStatus();
    	  maritalStatusDisplay.setValue(maritalStatus.getName());
    	  maritalStatusCoding.setDisplay(maritalStatusDisplay);
    	  maritalStatusCode.setValue(maritalStatus.getCode());
      }
      maritalStatusCoding.setCode(maritalStatusCode);
      maritalStatusCodeableConcept.getCoding().add(maritalStatusCoding);
      fhirpatient.setMaritalStatus(maritalStatusCodeableConcept);
      
      org.hl7.fhir.HumanName humanName = new org.hl7.fhir.HumanName();
      List<org.hl7.fhir.String> familyNameList = new ArrayList<org.hl7.fhir.String>();
      org.hl7.fhir.String familyName = new org.hl7.fhir.String();
      familyName.setValue(patients.get(i).getCred().getLastName());
      familyNameList.add(familyName);
      humanName.getFamily().addAll(familyNameList);
      List<org.hl7.fhir.String> givenNameList = new ArrayList<org.hl7.fhir.String>();
      org.hl7.fhir.String givenName1 = new org.hl7.fhir.String();
      givenName1.setValue(patients.get(i).getCred().getFirstName());
      givenNameList.add(givenName1);
      org.hl7.fhir.String givenName2 = new org.hl7.fhir.String();
      givenName2.setValue(patients.get(i).getCred().getMiddleName());
      givenNameList.add(givenName2);
      humanName.getGiven().addAll(givenNameList);
      fhirpatient.getName().add(humanName);
      
      org.hl7.fhir.Contact telecom = new org.hl7.fhir.Contact();
      org.hl7.fhir.String email = new org.hl7.fhir.String();
      email.setValue(patients.get(i).getCred().getEmail());
      telecom.setValue(email);
      fhirpatient.getTelecom().add(telecom);
      org.hl7.fhir.Contact telecom2 = new org.hl7.fhir.Contact();
      org.hl7.fhir.String primaryPhone = new org.hl7.fhir.String();
      primaryPhone.setValue(patients.get(i).getDemo().getPrimaryPhone());
      telecom2.setValue(primaryPhone);
      fhirpatient.getTelecom().add(telecom2);
      
      org.hl7.fhir.CodeableConcept genderCodeableConcept = new org.hl7.fhir.CodeableConcept();
      org.hl7.fhir.Coding genderStatusCoding = new org.hl7.fhir.Coding();
      org.hl7.fhir.Code genderCode = new org.hl7.fhir.Code();
      org.hl7.fhir.String genderDisplay = new org.hl7.fhir.String();
      genderDisplay.setValue(patients.get(i).getDemo().getGender().getName());
      genderCode.setValue(patients.get(i).getDemo().getGender().getCode());
      genderStatusCoding.setDisplay(genderDisplay);
      genderStatusCoding.setCode(genderCode);
      genderCodeableConcept.getCoding().add(genderStatusCoding);
      fhirpatient.setGender(genderCodeableConcept);
      
      org.hl7.fhir.Address address = new org.hl7.fhir.Address();
      org.hl7.fhir.String line = new org.hl7.fhir.String();
      line.setValue(patients.get(i).getDemo().getStreetAddress1());
      address.getLine().add(line);
      org.hl7.fhir.String city = new org.hl7.fhir.String();
      city.setValue(patients.get(i).getDemo().getCity());
      address.setCity(city);
      org.hl7.fhir.String state = new org.hl7.fhir.String();
      state.setValue(patients.get(i).getDemo().getUsState().getName());
      address.setState(state);
      org.hl7.fhir.String zip = new org.hl7.fhir.String();
      zip.setValue(patients.get(i).getDemo().getPostalCode());
      address.setZip(zip);
      org.hl7.fhir.String country = new org.hl7.fhir.String();
      if(patients.get(i).getDemo() != null && patients.get(i).getDemo().getCountry() != null){
    	  country.setValue(patients.get(i).getDemo().getCountry().getName());
      }
      address.setCountry(country);  
      fhirpatient.getAddress().add(address);
      org.hl7.fhir.Integer numChildren = new org.hl7.fhir.Integer();
      numChildren.setValue(patients.get(i).getPfsh().getNumChildren());
      fhirpatient.setMultipleBirthInteger(numChildren);
      patientsFHIR.getPatient().add(fhirpatient);
    }
    return patientsFHIR;
  }
  
  


}
