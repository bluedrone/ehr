/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.service;


import java.net.MalformedURLException;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import com.wdeanmedical.ehr.persistence.AdminDAO;
import com.wdeanmedical.ehr.persistence.AppDAO;
import com.wdeanmedical.ehr.persistence.ExternalDAO;
import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.dto.AdminDTO;
import com.wdeanmedical.ehr.dto.AuthorizedDTO;
import com.wdeanmedical.ehr.dto.LoginDTO;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.Credential;
import com.wdeanmedical.ehr.entity.Role;
import com.wdeanmedical.ehr.dto.BooleanResultDTO;
import com.wdeanmedical.ehr.util.ClinicianSessionData;
import com.wdeanmedical.ehr.util.OneWayPasswordEncoder;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
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
  private ActivityLogService activityLogService;


  public ExternalService() throws MalformedURLException {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    appDAO = (AppDAO) wac.getBean("appDAO");
    adminDAO = (AdminDAO) wac.getBean("adminDAO");
    externalDAO = (ExternalDAO) wac.getBean("externalDAO");
    activityLogService = new ActivityLogService();
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
  


}
