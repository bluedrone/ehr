/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import com.wdeanmedical.ehr.persistence.ReportsDAO;
import com.wdeanmedical.ehr.util.WDMConstants;
import com.wdeanmedical.ehr.util.DataEncryptor;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.ActivityLogDTO;
import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.entity.Activity;
import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.Report;
import com.wdeanmedical.ehr.entity.User;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class ReportsService {

  private static Log log = LogFactory.getLog(ReportsService.class);

  private ServletContext context;
  private WebApplicationContext wac;
  private ReportsDAO reportsDAO;
  private PatientService patientService;

  public ReportsService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    reportsDAO = (ReportsDAO) wac.getBean("reportsDAO");
    patientService = new PatientService();
  }

  public List<ActivityLogDTO> getActivityLog(AdminDTO dto) throws Exception {
    List<ActivityLogDTO> activityLogDTOList = new ArrayList<ActivityLogDTO>();
    Clinician clinician = reportsDAO.findClinicianBySessionId(dto.getSessionId());
    List<ActivityLog> activityLogList = reportsDAO.getActivityLog(clinician.getId());
    ActivityLogDTO activityLogDTO = null;
    for(ActivityLog activityLog : activityLogList){      
      activityLogDTO = new ActivityLogDTO();
      if(activityLog.getUserId() != null){
        User user = reportsDAO.findUserById(activityLog.getUserId());
        if(user != null){
          activityLogDTO.setUserName(getFullName(user.getFirstName(), user.getMiddleName(), user.getLastName()));
        }
      }
      if(activityLog.getPatientId() != null){
        Patient loggedPatient = reportsDAO.findPatientById(activityLog.getPatientId());
        if(loggedPatient != null){
          patientService.decrypt(loggedPatient);
          activityLogDTO.setPatientName(getFullName(loggedPatient.getCred().getFirstName(), loggedPatient.getCred().getMiddleName(), loggedPatient.getCred().getLastName()));
        }
      }
      activityLogDTO.setTimePerformed(activityLog.getTimePerformed());
      if(activityLog.getClinicianId() != null){
        Clinician loggedClinician = reportsDAO.findClinicianById(activityLog.getClinicianId());
        if(loggedClinician != null){
          activityLogDTO.setClinicianName(getFullName(loggedClinician.getFirstName(), loggedClinician.getMiddleName(), loggedClinician.getLastName()));
        }
      }
      activityLogDTO.setEncounterId(activityLog.getEncounterId());
      activityLogDTO.setFieldName(activityLog.getFieldName());
      activityLogDTO.setActivity(activityLog.getActivity().getActivityType());
      activityLogDTO.setModule(activityLog.getModule().getModuleType());
      activityLogDTOList.add(activityLogDTO);
    }
    return activityLogDTOList;
  }

  public List<Report> getReportList(AdminDTO dto) throws Exception {
    Clinician clinician = reportsDAO.findClinicianBySessionId(dto.getSessionId());
    return reportsDAO.getReportList(clinician.getId());
  }

  public HSSFWorkbook getWorkbook(AdminDTO dto) throws Exception {

    List<ActivityLogDTO> activityLogs = getActivityLog(dto);

    HSSFWorkbook workbook = new HSSFWorkbook();
    // create a new Excel sheet
    HSSFSheet sheet = workbook.createSheet("Activity Logs");
    sheet.setDefaultColumnWidth(30);

    // create style for header cells
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Arial");
    style.setFillForegroundColor(HSSFColor.BLUE.index);
    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    font.setColor(HSSFColor.WHITE.index);
    style.setFont(font);

    // create header row
    HSSFRow header = sheet.createRow(0);

    header.createCell(0).setCellValue("User Id");
    header.getCell(0).setCellStyle(style);

    header.createCell(1).setCellValue("Patient Id");
    header.getCell(1).setCellStyle(style);

    header.createCell(2).setCellValue("Time Performed");
    header.getCell(2).setCellStyle(style);

    header.createCell(3).setCellValue("Clinician Id");
    header.getCell(3).setCellStyle(style);

    header.createCell(4).setCellValue("Encounter Id");
    header.getCell(4).setCellStyle(style);

    header.createCell(5).setCellValue("Field Name");
    header.getCell(5).setCellStyle(style);

    header.createCell(6).setCellValue("Activity");
    header.getCell(6).setCellStyle(style);

    header.createCell(7).setCellValue("Module");
    header.getCell(7).setCellStyle(style);

    // create data rows
    int rowCount = 1;

    for (ActivityLogDTO activityLog : activityLogs) {
      HSSFRow aRow = sheet.createRow(rowCount++);
      if (activityLog.getUserName() != null) {
        aRow.createCell(0).setCellValue(activityLog.getUserName());
      } else {
        aRow.createCell(0).setCellValue("");
      }
      if (activityLog.getPatientName() != null) {
        aRow.createCell(1).setCellValue(activityLog.getPatientName());
      } else {
        aRow.createCell(1).setCellValue("");
      }
      if (activityLog.getTimePerformed() != null) {
        aRow.createCell(2).setCellValue(activityLog.getTimePerformed());
      } else {
        aRow.createCell(2).setCellValue("");
      }
      if (activityLog.getClinicianName() != null) {
        aRow.createCell(3).setCellValue(activityLog.getClinicianName());
      } else {
        aRow.createCell(3).setCellValue("");
      }
      if (activityLog.getEncounterId() != null) {
        aRow.createCell(4).setCellValue(activityLog.getEncounterId());
      } else {
        aRow.createCell(4).setCellValue("");
      }
      if (activityLog.getFieldName() != null) {
        aRow.createCell(5).setCellValue(activityLog.getFieldName());
      } else {
        aRow.createCell(5).setCellValue("");
      }
      if (activityLog.getActivity() != null) {
        aRow.createCell(6).setCellValue(activityLog.getActivity());
      } else {
        aRow.createCell(6).setCellValue("");
      }
      if (activityLog.getModule() != null) {
        aRow.createCell(7).setCellValue(activityLog.getModule());
      } else {
        aRow.createCell(7).setCellValue("");
      }
    }
    return workbook;
  }
  
  /*private void decrypt(Patient patient) throws Exception { 
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
  }*/
  
  private String getFullName(String firstName, String middleName, String lastName){
    
    StringBuilder fullNameBuilder = new StringBuilder();
    
    if(middleName == null){
      fullNameBuilder.append(firstName);
      fullNameBuilder.append(WDMConstants.SINGLE_SPACE);
      fullNameBuilder.append(lastName);
    }else{
      fullNameBuilder.append(firstName);
      fullNameBuilder.append(WDMConstants.SINGLE_SPACE);
      fullNameBuilder.append(middleName);
      fullNameBuilder.append(WDMConstants.SINGLE_SPACE);
      fullNameBuilder.append(lastName);
    }
    
    return fullNameBuilder.toString();
    
  }  
  
  public boolean getActivityLogSearchTypeAheads(ActivityLogDTO dto) throws Exception {
    
    List<Clinician> clinicians = reportsDAO.getClinicians();
    List<Patient> patients = reportsDAO.getPatients();
    List<Activity> clinicianActivityList = reportsDAO.activityLogGetActivity();
    Set<String> clinicianFullNames = new TreeSet<String>();
    Set<String> patientFullNames = new TreeSet<String>();
    
    for(Clinician clinician : clinicians){
      clinicianFullNames.add(getFullName(clinician.getFirstName(), clinician.getMiddleName(), clinician.getLastName()));
    }
    
    for (Patient patient : patients) {
      patientService.decrypt(patient);
      patientFullNames.add(getFullName(patient.getCred().getFirstName(), patient.getCred().getMiddleName(), patient.getCred().getLastName()));
    }
    
    dto.activityLogClinicianSearchTypeAheads.put("clinicianFullNames", clinicianFullNames);    
    dto.activityLogPatientSearchTypeAheads.put("patientFullNames", patientFullNames);
    dto.clinicianActivityList.put("clinicianActivity", clinicianActivityList);
    
    return true;
  }
  
  public List<ActivityLogDTO> filterActivityLog(ActivityLogDTO dto) throws Exception {
    
    Integer clinicianId = null;
    Activity activity = null;
    Integer patientId = null;
    
    if(dto.getClinicianName().length() > 0){
      Clinician clinician = reportsDAO.getClinicianByFullName(dto.getClinicianName()); 
      clinicianId = clinician.getId();
    }
    if(dto.getActivityId() != 0){
      activity = reportsDAO.findActivityById(dto.getActivityId());
    }
    if(dto.getPatientName().length() > 0){
      Patient patient = reportsDAO.getPatientByFullName(dto.getPatientName());
      patientId = patient.getId();
    }
    
    List<ActivityLogDTO> activityLogDTOList = new ArrayList<ActivityLogDTO>();
    List<ActivityLog> activityLogList  = reportsDAO.getFilteredActivityLog(clinicianId, activity, patientId);
    ActivityLogDTO activityLogDTO = null;
    for(ActivityLog activityLog : activityLogList){      
      activityLogDTO = new ActivityLogDTO();
      if(activityLog.getUserId() != null){
        User user = reportsDAO.findUserById(activityLog.getUserId());
        if(user != null){
          activityLogDTO.setUserName(getFullName(user.getFirstName(), user.getMiddleName(), user.getLastName()));
        }
      }
      if(activityLog.getPatientId() != null){
        Patient loggedPatient = reportsDAO.findPatientById(activityLog.getPatientId());
        if(loggedPatient != null){
          patientService.decrypt(loggedPatient);
          activityLogDTO.setPatientName(getFullName(loggedPatient.getCred().getFirstName(), loggedPatient.getCred().getMiddleName(), loggedPatient.getCred().getLastName()));
        }
      }
      activityLogDTO.setTimePerformed(activityLog.getTimePerformed());
      if(activityLog.getClinicianId() != null){
        Clinician loggedClinician = reportsDAO.findClinicianById(activityLog.getClinicianId());
        if(loggedClinician != null){
          activityLogDTO.setClinicianName(getFullName(loggedClinician.getFirstName(), loggedClinician.getMiddleName(), loggedClinician.getLastName()));
        }
      }
      activityLogDTO.setEncounterId(activityLog.getEncounterId());
      activityLogDTO.setFieldName(activityLog.getFieldName());
      activityLogDTO.setActivity(activityLog.getActivity().getActivityType());
      activityLogDTO.setModule(activityLog.getModule().getModuleType());
      activityLogDTOList.add(activityLogDTO);
    }
    return activityLogDTOList;
  }

}
