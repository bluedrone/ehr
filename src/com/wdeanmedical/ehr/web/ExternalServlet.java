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
import com.wdeanmedical.ehr.entity.FHIRIdentifierDT;
import com.wdeanmedical.ehr.entity.FHIRPatient;
import com.wdeanmedical.ehr.entity.FHIRPeriodDT;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.service.AdminService;
import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.service.PatientService;
import com.wdeanmedical.ehr.core.Core;
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
      
    FHIRPatient fhirpatient = new FHIRPatient();
    //fhirpatient.name = patients.get(0).getCred().getFirstName();
//    fhirpatient.name.add(patients.get(0).getCred().getFirstName());
//    fhirpatient.name.add("john");
      
    FHIRIdentifierDT identifier = new FHIRIdentifierDT();
    identifier.setUse("official");
    FHIRPeriodDT period = new FHIRPeriodDT();
    period.setStart(new Date());
    identifier.setPeriod(period);
    fhirpatient.getIdentifier().add(identifier);
      
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(FHIRPatient.class);
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
 
 
