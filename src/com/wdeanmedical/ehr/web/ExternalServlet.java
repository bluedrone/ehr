/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.web;

import java.io.IOException;
import java.io.OutputStream;
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

import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.MaritalStatus;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.service.ExternalService;
import com.wdeanmedical.ehr.service.PatientService;
import com.wdeanmedical.ehr.util.JSONUtils;
import com.wdeanmedical.external.fhir.PatientsFHIR;
import com.google.gson.Gson;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.log4j.Logger;


public class ExternalServlet extends AppServlet  {
  
  private static final Logger log = Logger.getLogger(ExternalServlet.class);
  
  private static final int FORMAT = 0; 
  private static final int METHOD = 1; 
  private static final int ARG1 = 2; 
  private static final int ARG2 = 3; 
  private static final int ARG3 = 4; 
  private static final String JSON = "json"; 
  private static final String XML = "xml"; 
  
  private AppService appService;
  private PatientService patientService;
  private ExternalService externalService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext context = getServletContext();
    try{
      appService = new AppService();
      patientService = new PatientService();
      externalService = new ExternalService();
    }
    catch(MalformedURLException e){
      e.printStackTrace();
    }
  }
    
  @Override
  public void doPost( HttpServletRequest request, HttpServletResponse response) {
    String returnString = "";
    String format = ""; 
    String arg1 = "";
    String arg2 = "";
    String arg3 = "";
    String pathInfo = request.getPathInfo();
    String servletPath = request.getServletPath();
    boolean isUploadResponse = false;
    
    String[] paths = pathInfo.substring(1).split("/");          
    
    if (XML.equals(paths[FORMAT])) {
      format = XML;
    }
    else if (JSON.equals(paths[FORMAT])) {
      format = JSON;
    }
    
    String method = paths[METHOD];
    if (paths.length > 2) {arg1 = paths[ARG1];}
    if (paths.length > 3) {arg2 = paths[ARG2];}
    if (paths.length > 4) {arg3 = paths[ARG3];}
    
     
    try { 
      if (method.equals("auth")) {
        returnString = auth(request, response);  
      }
      else { 
        if (isValidSession(request, response) == false) {
          returnString = logout(request, response);  
        }
        else {
          if (method.equals("getPatient")) {
            returnString = getPatient(arg1, format);  
          }
          if (method.equals("getPatients")) {
            returnString = getPatients(request, response, format);  
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
  
  
  public String auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    LoginDTO loginDTO = gson.fromJson(data, LoginDTO.class);  
    String ipAddress = request.getRemoteHost();
    AuthorizedDTO dto = externalService.auth(loginDTO, ipAddress); 
    String json = gson.toJson(dto);
    return json;
  }
  
  public String getPatient(String mrn, String format) throws Exception {
    Gson gson = new Gson();
    org.hl7.fhir.Patient patientFHIR = externalService.getPatient(mrn);
    String returnString = gson.toJson(patientFHIR);
    if (XML.equals(format)) {
      JSONObject json = JSONObject.fromObject(returnString);
      returnString = new XMLSerializer().write(json);
   }
    return returnString;
  }
  
    
  public String getPatients(HttpServletRequest request, HttpServletResponse response, String format) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class); 
    List<Patient> patients = appService.getPatients(dto); 

    PatientsFHIR patientsFHIR = externalService.buildPatientResource(patients);
    StringWriter out = new StringWriter();
    if (format.equals(XML)) {
      JAXBContext jaxbContext = JAXBContext.newInstance(PatientsFHIR.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(patientsFHIR, out);
      return out.toString();
    } 
    String json = gson.toJson(patientsFHIR);
    return json;
  }
  
    
  public String getPatientEncounterJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class);
    org.hl7.fhir.Encounter encounter = externalService.buildPatientEncounter(dto);
    
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(org.hl7.fhir.Encounter.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(encounter, System.out);
    } catch (JAXBException e) {
      e.printStackTrace();
    }

    String json = gson.toJson(encounter);
    System.out.println(json);
    return json;
  }
  
  public String getPatientEncounterXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String data = request.getParameter("data");
    Gson gson = new Gson();
    PatientDTO dto = gson.fromJson(data, PatientDTO.class);
    org.hl7.fhir.Encounter encounter = externalService.buildPatientEncounter(dto);
    StringWriter xml = new StringWriter();
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(org.hl7.fhir.Encounter.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(encounter, System.out);
      jaxbMarshaller.marshal(encounter, xml);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return xml.toString();
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
         externalService.importPatients(patientsFHIR);
    }    
    return null;
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
      externalService.updatePatient(patientFHIR);
    }
    return null;
  }
  
  public String getPatientFullRecord(String mrn) throws Exception {
    
    org.hl7.fhir.Patient patientFHIR = externalService.getPatientFullRecord(mrn);
    
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
 
 
