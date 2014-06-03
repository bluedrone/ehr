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

import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.service.AdminService;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.service.PatientService;
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
import com.wdeanmedical.external.fhir.Period;
import com.wdeanmedical.external.fhir.Telecom;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class ExternalServlet extends AppServlet  {
  
  private static final Logger log = Logger.getLogger(ExternalServlet.class);
  
  private AppService appService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    try{
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
    boolean isUploadResponse = false;
     
    try { 
      if (isValidSession(request, response) == false) {
        returnString = logout(request, response);  
      }
      else { 
        if (pathInfo.equals("/patientExport")) {
          returnString = patientExport(request, response);  
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
      
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(PatientFHIR.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(fhirpatient, System.out);
      } catch (JAXBException e) {
      e.printStackTrace();
      }
      
      String json = gson.toJson(dto);
      System.out.println(json);
      return json;
  } 
 
}
 
 
