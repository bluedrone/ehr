/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.service;


import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wdeanmedical.ehr.persistence.AdminDAO;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.Credential;
import com.wdeanmedical.ehr.entity.Role;
import com.wdeanmedical.ehr.dto.BooleanResultDTO;
import com.wdeanmedical.ehr.util.OneWayPasswordEncoder;

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

public class AdminService {

  private static Log log = LogFactory.getLog(AppService.class);
  public static int RETURN_CODE_DUP_USERNAME = -1;
  public static int RETURN_CODE_INVALID_PASSWORD = -2;
  
  private ServletContext context;
  private WebApplicationContext wac;
  private AdminDAO adminDAO;


  public AdminService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    adminDAO = (AdminDAO) wac.getBean("adminDAO");
  }
  

  public  void saveNewClinician(AdminDTO dto) throws Exception {
  
   if(adminDAO.checkUsername(dto.getUsername()) == false) {
      dto.setResult(false);
      dto.setErrorMsg("Username already in system");
      dto.setReturnCode(RETURN_CODE_DUP_USERNAME);
      return;
    }
    
    if (testPassword(dto.getPassword()) == false) {
      dto.setResult(false);
      dto.setErrorMsg("Insufficient Password");
      dto.setReturnCode(RETURN_CODE_INVALID_PASSWORD);
      return;
    }
    
    Clinician clinician = new Clinician();
    String salt = UUID.randomUUID().toString();
    clinician.setSalt(salt);
    String encodedPassword = OneWayPasswordEncoder.getInstance().encode(dto.getPassword(), salt);
    clinician.setPassword(encodedPassword);
    
    clinician.setFirstName(dto.getFirstName());
    clinician.setMiddleName(dto.getMiddleName());
    clinician.setLastName(dto.getLastName());
    clinician.setUsername(dto.getUsername());
    clinician.setPrimaryPhone(dto.getPrimaryPhone());
    clinician.setSecondaryPhone(dto.getSecondaryPhone());
    clinician.setEmail(dto.getEmail());
    clinician.setPager(dto.getPager());
    clinician.setGroupName(dto.getGroupName());
    clinician.setPracticeName(dto.getPracticeName());
    clinician.setRole(adminDAO.findRoleById(dto.getRoleId()));
    clinician.setCredential(adminDAO.findCredentialById(dto.getCredentialId()));
    clinician.setActive(true);
    adminDAO.createClinician(clinician);
  }
  
  
  public boolean testPassword(String password) {
  
   if (password.length() < 6) {
    log.info("Submitted passwords is not at least six characters long");
    return false;
    }
    Pattern lowerCasePattern = Pattern.compile("[a-z]+");
    Matcher lowerCaseMatcher = lowerCasePattern.matcher(password);
        
    Pattern upperCasePattern = Pattern.compile("[A-Z]+");
    Matcher upperCaseMatcher = upperCasePattern.matcher(password);
        
    if (lowerCaseMatcher.find() == false || upperCaseMatcher.find() == false) {
      log.info("Sumitted passwords does not include at least one uppercase and one lowercase letter");
      return false;
    }
          
    Pattern numericPattern = Pattern.compile("\\d+");
    Matcher numericMatcher = numericPattern.matcher(password);
        
    Pattern punctuationPattern = Pattern.compile("\\p{Punct}+");
    Matcher punctuationMatcher = punctuationPattern.matcher(password);
         
    if (numericMatcher.find() == false || punctuationMatcher.find() == false) {
      log.info("Submitted passwords does not include at least one numeric character and one punctuation character");
      return false;
    }
    return true;
  }
  
  
  public BooleanResultDTO checkUsername(AdminDTO adminDTO) throws Exception {
    BooleanResultDTO booleanResultDTO = new BooleanResultDTO();
    booleanResultDTO.setResult(adminDAO.checkUsername(adminDTO.getUsername()));
    return booleanResultDTO;
  }
  
  
  public void activateClinician(AdminDTO dto) throws Exception {
    Clinician clinician = adminDAO.findClinicianById(dto.getClinicianId());
    clinician.setActive(true);
    adminDAO.update(clinician);
  }
  
  
  public void deactivateClinician(AdminDTO dto) throws Exception {
    Clinician clinician = adminDAO.findClinicianById(dto.getClinicianId());
    clinician.setActive(false);
    adminDAO.update(clinician);
  }
  
  
  public void purgeClinician(AdminDTO dto) throws Exception {
    Clinician clinician = adminDAO.findClinicianById(dto.getClinicianId());
    clinician.setPurged(true);
    adminDAO.update(clinician);
  }
  
  
  public void updateClinician(AdminDTO dto) throws Exception {
    Clinician clinician = adminDAO.findClinicianById(dto.getClinicianId());
    String property = dto.getUpdateProperty();
    String value = dto.getUpdatePropertyValue();
    if (property.equals("firstName")) {clinician.setFirstName(value);} 
    else if (property.equals("middleName")) {clinician.setMiddleName(value);} 
    else if (property.equals("lastName")) {clinician.setLastName(value);} 
    else if (property.equals("email")) {clinician.setEmail(value);} 
    else if (property.equals("pager")) {clinician.setPager(value);} 
    else if (property.equals("primaryPhone")) {clinician.setPrimaryPhone(value);} 
    else if (property.equals("secondaryPhone")) {clinician.setSecondaryPhone(value);} 
    else if (property.equals("groupName")) {clinician.setGroupName(value);} 
    else if (property.equals("practiceName")) {clinician.setPracticeName(value);} 
    else if (property.equals("roleId")) {
      Integer roleId; try { roleId = new Integer(value); } catch (NumberFormatException nfe) {roleId = null;}
      if (roleId != null) {
        Role role = adminDAO.findRoleById(roleId);
        clinician.setRole(role);
      }
    } 
    else if (property.equals("credentialId")) {
      Integer credentialId; try { credentialId = new Integer(value); } catch (NumberFormatException nfe) {credentialId = null;}
      if (credentialId != null) {
        Credential credential = adminDAO.findCredentialById(credentialId);
        clinician.setCredential(credential);
      } 
    } 
    
    else if (property.equals("username")) {
     if(adminDAO.checkUsername(value) == false) {
        dto.setResult(false);
        dto.setErrorMsg("Username already in system");
        dto.setReturnCode(RETURN_CODE_DUP_USERNAME);
        return;
      }
      clinician.setUsername(value);
    } 
    
    else if (property.equals("password")) {
      if (testPassword(value) == false) {
        dto.setResult(false);
        dto.setErrorMsg("Insufficient Password");
        dto.setReturnCode(RETURN_CODE_INVALID_PASSWORD);
        return;
      }
      String salt = UUID.randomUUID().toString();
      clinician.setSalt(salt);
      String encodedPassword = OneWayPasswordEncoder.getInstance().encode(value, salt);
      clinician.setPassword(encodedPassword);
    } 
    
    adminDAO.update(clinician);
  }  
  
}
