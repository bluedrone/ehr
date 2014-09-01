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
import com.wdeanmedical.ehr.persistence.ReportsDAO;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.Country;
import com.wdeanmedical.ehr.entity.Credential;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.EncounterMedication;
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


  public ReportsService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    reportsDAO = (ReportsDAO) wac.getBean("reportsDAO");
  }
  
  public List<ActivityLog> getActivityLog(AdminDTO dto) throws Exception{
     Clinician clinician = reportsDAO.findClinicianBySessionId(dto.getSessionId());
     return reportsDAO.getActivityLog(clinician.getId());
  }
  
  public HSSFWorkbook getWorkbook(AdminDTO dto) throws Exception{
   
    List<ActivityLog> activityLogs = getActivityLog(dto);
  
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
		
    for (ActivityLog activityLog : activityLogs) {
	HSSFRow aRow = sheet.createRow(rowCount++);
	if(activityLog.getUserId() != null){
		aRow.createCell(0).setCellValue(activityLog.getUserId());
	}else{
		aRow.createCell(0).setCellValue("");
	}
	if(activityLog.getPatientId() != null){
		aRow.createCell(1).setCellValue(activityLog.getPatientId());
	}else{
		aRow.createCell(1).setCellValue("");
	}
	if(activityLog.getTimePerformed() != null){
		aRow.createCell(2).setCellValue(activityLog.getTimePerformed());
	}else{
		aRow.createCell(2).setCellValue("");
	}
	if(activityLog.getClinicianId() != null){
		aRow.createCell(3).setCellValue(activityLog.getClinicianId());
	}else{
		aRow.createCell(3).setCellValue("");
	}
	if(activityLog.getEncounterId() != null){
		aRow.createCell(4).setCellValue(activityLog.getEncounterId());
	}else{
		aRow.createCell(4).setCellValue("");
	}
	if(activityLog.getFieldName() != null){
		aRow.createCell(5).setCellValue(activityLog.getFieldName());
	}else{
		aRow.createCell(5).setCellValue("");
	}
	if(activityLog.getActivity() != null){
		aRow.createCell(6).setCellValue(activityLog.getActivity().getActivityType());
	}else{
		aRow.createCell(6).setCellValue("");
	}
	if(activityLog.getModule() != null){
		aRow.createCell(7).setCellValue(activityLog.getModule().getModuleType());
	}else{
		aRow.createCell(7).setCellValue("");
	}
	}
	return workbook;	  
  }
	  
}
