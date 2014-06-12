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
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.service.AdminService;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.service.PatientService;
import com.wdeanmedical.ehr.util.JSONUtils;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.external.fhir.Address;
import com.wdeanmedical.external.fhir.CodeableConcept;
import com.wdeanmedical.external.fhir.Coding;
import com.wdeanmedical.external.fhir.Enums;
import com.wdeanmedical.external.fhir.Gender;
import com.wdeanmedical.external.fhir.HumanName;
import com.wdeanmedical.external.fhir.Identifier;
import com.wdeanmedical.external.fhir.MaritalStatus;
import com.wdeanmedical.external.fhir.PatientFHIR;
import com.wdeanmedical.external.fhir.PatientsFHIR;
import com.wdeanmedical.external.fhir.Period;
import com.wdeanmedical.external.fhir.Telecom;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
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
        			String mrn = paths[3]; 
        			returnString = updatePatient(mrn);
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
    
  public String patientExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<Patient> patients = appService.getPatients(dto); 
    dto.setPatients(patients);
//     System.out.println("number of patients: "+patients.size());
//     System.out.println("all aatients: "+patients);
    PatientFHIR fhirpatient = new PatientFHIR();
    
    fhirpatient.setBirthDate(patients.get(0).getDemo().getDob());
      
    Identifier identifier = new Identifier();
    identifier.setUse(Enums.IdentifierUse.usual.name());
    identifier.setLabel("MRN");
    identifier.setValue(patients.get(0).getCred().getMrn());
    fhirpatient.getIdentifier().add(identifier);
    
    MaritalStatus maritalStatus = new MaritalStatus();
    Coding codingm = new Coding();
    String sss = patients.get(0).getDemo().getMaritalStatus().getName();
    codingm.setDisplay(Enums.MaritalStatus.valueOf(sss).name());
    codingm.setCode(Enums.MaritalStatus.valueOf(sss).getValue());
    maritalStatus.setCoding(codingm);
    fhirpatient.setMaritalStatus(maritalStatus);
    
    HumanName name = new HumanName();
    List<String> family = new ArrayList<String>();
    family.add(patients.get(0).getCred().getLastName());
    name.setFamily(family);
    List<String> given = new ArrayList<String>();
    given.add(patients.get(0).getCred().getFirstName());
    given.add(patients.get(0).getCred().getMiddleName());
    name.setGiven(given);
    fhirpatient.getName().add(name);
    
    Telecom telecom = new Telecom();
    telecom.setValue(patients.get(0).getCred().getEmail());
    fhirpatient.getTelecom().add(telecom);
    Telecom telecom2 = new Telecom();
    telecom2.setValue(patients.get(0).getDemo().getPrimaryPhone());
    fhirpatient.getTelecom().add(telecom2);
    
    Coding coding = new Coding();
    coding.setCode(patients.get(0).getDemo().getGender().getCode());
    coding.setDisplay(patients.get(0).getDemo().getGender().getName());
    Gender gender = new Gender();
    gender.setCoding(coding);
    fhirpatient.setGender(gender);
    
    Address address = new Address();
    List<String> line = new ArrayList<String>();
    line.add(patients.get(0).getDemo().getStreetAddress1());
    address.setLine(line);
    address.setCity(patients.get(0).getDemo().getCity());
    address.setState(patients.get(0).getDemo().getUsState().getName());
    address.setZip(patients.get(0).getDemo().getPostalCode());
    address.setCountry(patients.get(0).getDemo().getCountry().getName());
    fhirpatient.getAddress().add(address);
    
    List<PatientFHIR> patientFHIRList = new ArrayList<PatientFHIR>();
    patientFHIRList.add(fhirpatient);
    
    PatientsFHIR patientsFHIR = new PatientsFHIR();    
    patientsFHIR.setPatients(patientFHIRList);
      
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(PatientsFHIR.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(patientsFHIR, System.out);
      } catch (JAXBException e) {
      e.printStackTrace();
      }
      
      //String json = gson.toJson(dto);
      String json = gson.toJson(patientsFHIR);
      System.out.println(json);
      return json;
  } 
  
  public String patientsImport(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  String data = request.getParameter("data");
      //String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><patients><patients><address><city>Springfield</city><country>UNITED STATES</country><line>71 State Street</line><state>Massachusetts</state><zip>01011</zip></address><birthDate>1977-04-04T13:00:00-05:00</birthDate><gender><coding><code>F</code><display>Female</display></coding></gender><identifier><label>MRN</label><use>usual</use><value>ABC123</value></identifier><maritalStatus><coding><code>M</code><display>Married</display></coding></maritalStatus><name><family>Smith</family><given>Sara</given><given>J.</given></name><telecom><value>patient01@pleasantvillemedical.com</value></telecom><telecom><value>413 567-9988</value></telecom></patients></patients>";
      //String data = "{\"patients\":[{\"identifier\":[{\"use\":\"usual\",\"label\":\"MRN\",\"value\":\"ABC123\"}],\"name\":[{\"family\":[\"Smith\"],\"given\":[\"Sara\",\"J.\"],\"prefix\":[],\"suffix\":[]}],\"telecom\":[{\"value\":\"patient01@pleasantvillemedical.com\"},{\"value\":\"413 567-9988\"}],\"gender\":{\"coding\":{\"code\":\"F\",\"display\":\"Female\"}},\"birthDate\":\"Apr 4, 1977 1:00:00 PM\",\"address\":[{\"line\":[\"71 State Street\"],\"city\":\"Springfield\",\"state\":\"Massachusetts\",\"zip\":\"01011\",\"country\":\"UNITED STATES\"}],\"maritalStatus\":{\"coding\":{\"code\":\"M\",\"display\":\"Married\"}},\"contact\":[],\"communication\":[],\"careProvider\":[],\"link\":[]}]}";
	  
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
	  PatientFHIR patientFHIR = patientService.getPatient(mrn);
	  String json = gson.toJson(patientFHIR);
	  return json;
  }
  
  public String updatePatient(String mrn) throws Exception {
	  System.out.println("******* mrn: " + mrn);
	  return null;
  }
  
  public String getPatientFullRecord(String mrn) throws Exception {
	  Gson gson = new Gson();
	  PatientFHIR patientFHIR = patientService.getPatientFullRecord(mrn);
	  String json = gson.toJson(patientFHIR);
	  return json;
  }
 
}
 
 
