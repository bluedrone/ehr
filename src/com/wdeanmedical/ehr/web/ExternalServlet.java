/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.MaritalStatus;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.service.PatientService;
import com.wdeanmedical.ehr.util.JSONUtils;
import com.wdeanmedical.external.fhir.PatientsFHIR;
import com.google.gson.Gson;

import org.apache.log4j.Logger;


public class ExternalServlet extends AppServlet  {
  
  private static final Logger log = Logger.getLogger(ExternalServlet.class);
  
  private AppService appService;
  
  private PatientService patientService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    try{
      appService = new AppService();
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
        if (pathInfo.equals("/patientExport")) {
          returnString = patientExport(request, response);  
          //returnString = patientsImport(request, response); 
        }else if(pathInfo.equals("/patientEncounter")) {
          returnString = patientEncounter(request, response); 
        }else if(pathInfo.equals("/patientImport")) {
            returnString = patientsImport(request, response);  
        }else if(pathInfo.split("/").length > 2){
        	String[] paths = pathInfo.split("/");        	
        	if(paths[1].equals("json")){
        		if(paths[2].equals("auth")){
        			 returnString = auth(request, response);
        		}else if(paths[2].equals("getPatient")){        			
        			String mrn = paths[3];  
        			returnString = getPatient(mrn);
        		}        		
        	}else if(paths[1].equals("xml")){
        		if(paths[2].equals("updatePatient")){
        			returnString = updatePatient(request, response);
        		}else if(paths[2].equals("getPatientFullRecord")){        			
        			String mrn = paths[3];
        			returnString = getPatientFullRecord(mrn);
        		}        		
        	}       	
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
  
  public String patientEncounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<Patient> patients = appService.getPatients(dto); 
    dto.setPatients(patients);
    System.out.println("patient encounter");
    
    String json = gson.toJson(patients);
    System.out.println(json);
    return json;
  }
    
  public String patientExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<Patient> patients = appService.getPatients(dto); 
    dto.setPatients(patients);
//     System.out.println("number of patients: "+patients.size());
//     System.out.println("all patients: "+patients);
    PatientsFHIR patientsFHIR = new PatientsFHIR();
    
    int patientsLength = 19;
    for(int i = 0; i < 1; i++){
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
	    MaritalStatus maritalStatus = patients.get(i).getDemo().getMaritalStatus();
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
	    country.setValue(patients.get(i).getDemo().getCountry().getName());
	    address.setCountry(country);  
	    fhirpatient.getAddress().add(address);
	    org.hl7.fhir.Integer numChildren = new org.hl7.fhir.Integer();
	    numChildren.setValue(patients.get(i).getPfsh().getNumChildren());
	    fhirpatient.setMultipleBirthInteger(numChildren);
	    patientsFHIR.getPatient().add(fhirpatient);
    }      
    try {
	      JAXBContext jaxbContext = JAXBContext.newInstance(PatientsFHIR.class);
	      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	      jaxbMarshaller.marshal(patientsFHIR, System.out);
      } catch (JAXBException e) {
    	  e.printStackTrace();
      }
      String json = gson.toJson(patientsFHIR);
      System.out.println(json);
      return json;
  } 
  
  public String patientsImport(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  String data = request.getParameter("data");
	  //String data = "<patients xmlns:ns2=\"http://www.w3.org/1999/xhtml\" xmlns:ns3=\"http://hl7.org/fhir\"><patient><ns3:identifier><ns3:use id=\"usual\"/><ns3:label value=\"MRN\"/><ns3:value value=\"ABC123\"/></ns3:identifier><ns3:name><ns3:family value=\"Smith\"/><ns3:given value=\"Sara\"/><ns3:given value=\"J.\"/></ns3:name><ns3:telecom><ns3:value value=\"patient01@pleasantvillemedical.com\"/></ns3:telecom><ns3:telecom><ns3:value value=\"413 567-9988\"/></ns3:telecom><ns3:gender><ns3:coding><ns3:code value=\"F\"/><ns3:display value=\"Female\"/></ns3:coding></ns3:gender><ns3:birthDate value=\"1977-04-04 13:00:00.0\"/><ns3:address><ns3:line value=\"71 State Street\"/><ns3:city value=\"Springfield\"/><ns3:state value=\"Massachusetts\"/><ns3:zip value=\"01011\"/><ns3:country value=\"UNITED STATES\"/></ns3:address><ns3:maritalStatus><ns3:coding><ns3:code value=\"married\"/><ns3:display value=\"Married\"/></ns3:coding></ns3:maritalStatus></patient></patients>";
	  //String data = "{\"patient\":[{\"identifier\":[{\"use\":{\"id\":\"usual\"},\"label\":{\"value\":\"MRN\"},\"value\":{\"value\":\"ABC123\"}}],\"name\":[{\"family\":[{\"value\":\"Smith\"}],\"given\":[{\"value\":\"Sara\"},{\"value\":\"J.\"}]}],\"telecom\":[{\"value\":{\"value\":\"patient01@pleasantvillemedical.com\"}},{\"value\":{\"value\":\"413 567-9988\"}}],\"gender\":{\"coding\":[{\"code\":{\"value\":\"F\"},\"display\":{\"value\":\"Female\"}}]},\"birthDate\":{\"value\":\"1977-04-04 13:00:00.0\"},\"address\":[{\"line\":[{\"value\":\"71 State Street\"}],\"city\":{\"value\":\"Springfield\"},\"state\":{\"value\":\"Massachusetts\"},\"zip\":{\"value\":\"01011\"},\"country\":{\"value\":\"UNITED STATES\"}}],\"maritalStatus\":{\"coding\":[{\"code\":{\"value\":\"married\"},\"display\":{\"value\":\"Married\"}}]},\"multipleBirthInteger\":{\"value\":1}}]}";
	  PatientsFHIR patientsFHIR = null;
	  
	  if(JSONUtils.isJSONValid(data, PatientsFHIR.class)){
	    Gson gson = new Gson();
	    patientsFHIR = gson.fromJson(data, PatientsFHIR.class); 
	  }else{

	  try {
    	    JAXBContext jaxbContext = JAXBContext.newInstance(PatientsFHIR.class);
    	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    	    StringReader stringReader = new StringReader(data);
    	    patientsFHIR = (PatientsFHIR)jaxbUnmarshaller.unmarshal(stringReader);
  	      } catch (JAXBException e) {
	        e.printStackTrace();
	      }
	  }
	  
	  if(patientsFHIR != null){
	       patientService.importPatients(patientsFHIR);
	  }	  
	  return null;
  }
  
  public String auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  String data = request.getParameter("data");
	  Gson gson = new Gson();
	  LoginDTO loginDTO = gson.fromJson(data, LoginDTO.class);  
	  String ipAddress = request.getRemoteHost();
	  Clinician clinician = appService.login(loginDTO, ipAddress); 
	  String json = gson.toJson(clinician);
	  return json;
  }
  
  public String getPatient(String mrn) throws Exception {
	  Gson gson = new Gson();
	  org.hl7.fhir.Patient patientFHIR = patientService.getPatient(mrn);
	  String json = gson.toJson(patientFHIR);
	  return json;
  }
  
  public String updatePatient(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  String data = request.getParameter("data");
	  //String data = "<patients xmlns:ns2=\"http://www.w3.org/1999/xhtml\" xmlns:ns3=\"http://hl7.org/fhir\"><patient><ns3:identifier><ns3:use id=\"usual\"/><ns3:label value=\"MRN\"/><ns3:value value=\"ABC123\"/></ns3:identifier><ns3:name><ns3:family value=\"Smith\"/><ns3:given value=\"Sara\"/><ns3:given value=\"J.\"/></ns3:name><ns3:telecom><ns3:value value=\"patient01@pleasantvillemedical.com\"/></ns3:telecom><ns3:telecom><ns3:value value=\"413 567-9988\"/></ns3:telecom><ns3:gender><ns3:coding><ns3:code value=\"F\"/><ns3:display value=\"Female\"/></ns3:coding></ns3:gender><ns3:birthDate value=\"1977-04-04 13:00:00.0\"/><ns3:address><ns3:line value=\"490 Douglas Pike\"/><ns3:city value=\"Springfield\"/><ns3:state value=\"Massachusetts\"/><ns3:zip value=\"01011\"/><ns3:country value=\"UNITED STATES\"/></ns3:address><ns3:maritalStatus><ns3:coding><ns3:code value=\"married\"/><ns3:display value=\"Married\"/></ns3:coding></ns3:maritalStatus><ns3:multipleBirthInteger value=\"1\"/></patient></patients>";

	  org.hl7.fhir.Patient patientFHIR = null;
	  try {
  	    JAXBContext jaxbContext = JAXBContext.newInstance(org.hl7.fhir.Patient.class);
  	    //JAXBContext jaxbContext = JAXBContext.newInstance(PatientsFHIR.class);
  	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
  	    StringReader stringReader = new StringReader(data);
  	    patientFHIR = (org.hl7.fhir.Patient)jaxbUnmarshaller.unmarshal(stringReader);
  	    //PatientsFHIR patientsFHIR = (PatientsFHIR)jaxbUnmarshaller.unmarshal(stringReader);
  	    //patientFHIR = patientsFHIR.getPatient().get(0);
	    } catch (JAXBException e) {
	      e.printStackTrace();
	    }
	  if(patientFHIR != null){
		  patientService.updatePatient(patientFHIR);
	  }
	  return null;
  }
  
  public String getPatientFullRecord(String mrn) throws Exception {
	  
	  org.hl7.fhir.Patient patientFHIR = patientService.getPatientFullRecord(mrn);
	  
	  StringWriter stringWriter = new StringWriter();
	  
	  try {
	      JAXBContext jaxbContext = JAXBContext.newInstance(org.hl7.fhir.Patient.class);
	      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	      jaxbMarshaller.marshal(patientFHIR, stringWriter);
      } catch (JAXBException e) {
    	  e.printStackTrace();
      }
	  return stringWriter.toString();
  }
 
}
 
 
